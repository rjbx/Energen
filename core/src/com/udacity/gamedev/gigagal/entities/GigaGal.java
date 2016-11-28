package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums.*;
import com.udacity.gamedev.gigagal.util.Utils;
import java.lang.String;

// mutable
public class GigaGal implements Physical {

    // fields
    public final static String TAG = GigaGal.class.getName();
    private Level level;
    private Vector2 spawnLocation;
    private Vector2 position;
    private Vector2 previousFramePosition;
    private Vector2 velocity;
    private Direction facing;
    private AerialState aerialState;
    private GroundState groundState;
    private boolean canStride;
    private boolean canDash;
    private boolean canJump;
    private boolean canHover;
    private boolean canRicochet;
    private boolean canCharge;
    private boolean canChangeDirection;
    private boolean isCharged;
    private boolean knockedBack;
    private boolean slidPlatform;
    private boolean groundedPlatform;
    private long strideStartTime;
    private long jumpStartTime;
    private long dashStartTime;
    private long hoverStartTime;
    private long ricochetStartTime;
    private long chargeStartTime;
    private float strideAcceleration;
    private float hoverTimeSeconds;
    private float aerialTakeoff;
    private int lives;
    private int ammo;
    private int health;
    public boolean leftButtonPressed;
    public boolean rightButtonPressed;
    public boolean jumpButtonPressed;
    public boolean shootButtonPressed;

    // ctor
    public GigaGal(Vector2 spawnLocation, Level level) {
        this.spawnLocation = spawnLocation;
        this.level = level;
        position = new Vector2();
        velocity = new Vector2();
        previousFramePosition = new Vector2();
        init();
    }

    public void init() {
        ammo = Constants.INITIAL_AMMO;
        health = Constants.INITIAL_HEALTH;
        lives = Constants.INITIAL_LIVES;
        respawn();
    }

    public void update(float delta) {
        previousFramePosition.set(position);
        position.mulAdd(velocity, delta);
        touchPlatforms(level.getPlatforms());
        recoilFromHazards(level.getHazards());
        collectPowerups(level.getPowerups());
        enableRespawn();
        enableShoot();

        if (aerialState == AerialState.GROUNDED && groundState != GroundState.AIRBORNE) {
            velocity.y = 0;
            if (groundState == GroundState.STANDING) {
                stand();
                enableStride();
                enableDash();
                enableJump();
            } else if (groundState == GroundState.STRIDING) {
                enableStride();
                enableDash();
                enableJump();
            } else if (groundState == GroundState.DASHING) {
                enableDash();
                enableJump();
            }
        }

        if (groundState == GroundState.AIRBORNE && aerialState != AerialState.GROUNDED) {
            velocity.y -= Constants.GRAVITY;
            if (aerialState == AerialState.FALLING) {
                fall();
                enableHover();
                enableRicochet();
            } else if (aerialState == AerialState.JUMPING) {
                enableJump();
                enableRicochet();
            } else if (aerialState == AerialState.HOVERING) {
                enableHover();
                enableRicochet();
            } else if (aerialState == AerialState.RICOCHETING) {
                enableRicochet();
            }
        }
    }

    private void touchPlatforms(Array<Platform> platforms) {
        float slidPlatformTop = 0;
        float slidPlatformBottom = 0;
        float groundedPlatformLeft = 0;
        float groundedPlatformRight = 0;
        for (Platform platform : platforms) {
            // if currently within platform left and right sides
            if (getRight() >= platform.getLeft() && getLeft() <= platform.getRight()) {
                // apply following rules (bump side and bottom) only if platform height > ledge height
                // ledges only apply collision detection on top, and not on sides and bottom as do platforms
                if (platform.getHeight() > Constants.MAX_LEDGE_HEIGHT
                && getBottom() <= platform.getTop() && getTop() >= platform.getBottom()) {
                    // detects contact with platform sides
                    if (!Utils.bisectsLaterally(platform, previousFramePosition.x, velocity.x)) {
                        if (groundState == GroundState.AIRBORNE) {
                            if (Math.abs(aerialTakeoff - previousFramePosition.x) > 1
                                && Math.abs(velocity.x) > Constants.GIGAGAL_MAX_SPEED / 3) {
                                if (aerialState == AerialState.RICOCHETING) {
                                    velocity.x = 0;
                                }
                                velocity.x += Utils.getLateralVelocity(Constants.GIGAGAL_STARTING_SPEED, facing);
                                canRicochet = true;
                                slidPlatform = true;
                                slidPlatformTop = platform.getTop();
                                slidPlatformBottom = platform.getBottom();
                            } else {
                                if (aerialState != AerialState.HOVERING && velocity.y < 0) {
                                    canHover = false;
                                }
                                fall();
                            }
                        }
                        strideStartTime = 0;
                        canStride = false;
                        canDash = false;
                        position.x = previousFramePosition.x;
                    } else {
                        canRicochet = false;
                    }
                    // detects contact with platform bottom
                    if ((previousFramePosition.y + Constants.GIGAGAL_HEAD_RADIUS) <= platform.getBottom()) {
                        velocity.y = 0;
                        position.y = previousFramePosition.y;
                        fall();
                    }
                }
                // detects contact with platform top
                if ((previousFramePosition.y - Constants.GIGAGAL_EYE_HEIGHT) >= platform.getTop()
                && getBottom() <= platform.getTop()
                && platform.getTop() != slidPlatformTop) {
                    velocity.y = 0; // prevents from descending beneath platform top
                    position.y = platform.getTop() + Constants.GIGAGAL_EYE_HEIGHT; // sets Gigagal atop platform
                    canChangeDirection = true;
                    groundedPlatform = true;
                    groundedPlatformLeft = platform.getLeft();
                    groundedPlatformRight = platform.getRight();
                    hoverStartTime = 0;
                    knockedBack = false;
                    canHover = true;
                    if (groundState == GroundState.AIRBORNE) {
                        stand();
                    }
                }
                // disables ricochet and hover if below minimum ground distance
                if (velocity.y < 0
                && getBottom() < (platform.getTop() + Constants.MIN_GROUND_DISTANCE)
                && getBottom() > platform.getTop()
                && aerialState != AerialState.RICOCHETING) {
                    canRicochet = false; // disables ricochet
                    canHover = false; // disables hover
                }
            }
        }
        // disables ricochet if no contact with slid platform side
        if (slidPlatform) {
            if (getBottom() > slidPlatformTop  || getTop() < slidPlatformBottom) {
                canRicochet = false;
                slidPlatform = false;
            }
        }
        // falls if no detection with grounded platform top
        if (groundedPlatform) {
            if (getRight() < groundedPlatformLeft || getLeft() > groundedPlatformRight) {
                groundedPlatform = false;
                fall();
            }
        }
    }

    private void handleDirectionalInput() {
        boolean left = Gdx.input.isKeyPressed(Keys.A) || leftButtonPressed;
        boolean right = Gdx.input.isKeyPressed(Keys.S) || rightButtonPressed;
        boolean directionChanged = false;
        if (left && !right) {
            directionChanged = Utils.setDirection(this, Direction.LEFT);
        } else if (!left && right) {
            directionChanged = Utils.setDirection(this, Direction.RIGHT);
        }
        if (groundState != GroundState.AIRBORNE) {
            if (groundState != GroundState.DASHING) {
                if (left && !right || right && !left) {
                    if (directionChanged) {
                        strideStartTime = 0;
                        stand();
                    } else if (!canStride) {
                        if (strideStartTime == 0) {
                            canStride = true;
                        } else if (Utils.secondsSince(strideStartTime) > Constants.DOUBLE_TAP_SPEED) {
                            strideStartTime = 0;
                        } else {
                            canDash = true;
                        }
                    }
                } else {
                    stand();
                    canStride = false;
                }
            }
        } else if (directionChanged) {
            recoil(new Vector2(velocity.x / 2, velocity.y));
        }
    }

    private void collectPowerups(DelayedRemovalArray<Powerup> powerups) {
        for (Powerup powerup : powerups) {
            Rectangle bounds = new Rectangle(powerup.getLeft(), powerup.getBottom(), powerup.getWidth(), powerup.getHeight());
            if (getBounds().overlaps(bounds)) {
                if (powerup.getSubclass() == AmmoPowerup.class) {
                    ammo += Constants.POWERUP_AMMO;
                    level.setScore(level.getScore() + Constants.POWERUP_SCORE);
                    powerups.removeValue(powerup, true);
                }
                if (powerup.getSubclass() == HealthPowerup.class) {
                    health += Constants.POWERUP_HEALTH;
                    if (health > 100) {
                        health = 100;
                    }
                    level.setScore(level.getScore() + Constants.POWERUP_SCORE);
                    powerups.removeValue(powerup, true);
                }
            }
        }
    }

    // detects contact with enemy (change aerial & ground state to recoil until grounded)
    private void recoilFromHazards(Array<Hazard> hazards) {
        for (Hazard hazard : hazards) {
            Rectangle bounds = new Rectangle(hazard.getLeft(), hazard.getBottom(), hazard.getWidth(), hazard.getHeight());
            if (getBounds().overlaps(bounds)) {
                if (!knockedBack) {
                    health -= hazard.getDamage();
                    knockedBack = true;
                }
                float oneThirdWidth = hazard.getWidth() / 3;
                if (getPosition().x < (hazard.getLeft() + oneThirdWidth)) {
                    recoil(new Vector2(-Constants.KNOCKBACK_VELOCITY.x, Constants.KNOCKBACK_VELOCITY.y));
                } else if (getPosition().x > (hazard.getRight() - oneThirdWidth)) {
                    recoil(Constants.KNOCKBACK_VELOCITY);
                } else {
                    recoil(new Vector2((Utils.getLateralVelocity(Constants.KNOCKBACK_VELOCITY.x, facing)), Constants.KNOCKBACK_VELOCITY.y));
                }

            }
        }
    }

    // disables all else by virtue of neither top level update conditions being satisfied due to state
    private void recoil(Vector2 velocity) {
        aerialState = AerialState.RECOILING;
        strideStartTime = 0;
        chargeStartTime = 0;
        canStride = false;
        canDash = false;
        isCharged = false;
        canHover = false;
        canRicochet = false;
        canChangeDirection = false;
        this.velocity.x = velocity.x;
        this.velocity.y = velocity.y;
    }

    private void enableShoot() {
        if (Gdx.input.isKeyPressed(Keys.ENTER) || shootButtonPressed) {
            if (chargeStartTime == 0) {
                canCharge = true;
                chargeStartTime = TimeUtils.nanoTime();
            } else if (Utils.secondsSince(chargeStartTime) > Constants.CHARGE_DURATION) {
                isCharged = true;
            }
        } else if (canCharge) {
            if (isCharged) {
                shoot(AmmoType.CHARGE);
            } else {
                shoot(AmmoType.REGULAR);
            }
            chargeStartTime = 0;
            isCharged = false;
            canCharge = false;
        }
    }

    public void shoot(AmmoType ammoType) {
        if (ammo > 0) {
            ammo--;
            Vector2 bulletPosition;
            if (facing == Direction.RIGHT) {
                bulletPosition = new Vector2(
                        position.x + Constants.GIGAGAL_CANNON_OFFSET.x,
                        position.y + Constants.GIGAGAL_CANNON_OFFSET.y);
            } else {
                bulletPosition = new Vector2(
                        position.x - Constants.GIGAGAL_CANNON_OFFSET.x - 5,
                        position.y + Constants.GIGAGAL_CANNON_OFFSET.y);
            }
            level.spawnBullet(bulletPosition, facing, ammoType);
        }
    }

    private void enableRespawn() {
        if (position.y < Constants.KILL_PLANE || health < 1) {
            health = 0;
            lives--;
            if (lives > -1) {
                respawn();
            }
        }
    }

    private void respawn() {
        position.set(spawnLocation);
        velocity.setZero();
        facing = Direction.RIGHT;
        groundState = GroundState.AIRBORNE;
        aerialState = AerialState.FALLING;
        canStride = false;
        canJump = false;
        canDash = false;
        canHover = false;
        canRicochet = false;
        canCharge = false;
        canChangeDirection = false;
        isCharged = false;
        slidPlatform = false;
        groundedPlatform = false;
        chargeStartTime = 0;
        strideStartTime = 0;
        jumpStartTime = 0;
        dashStartTime = 0;
        health = 100;
    }

    private void enableStride() {
        handleDirectionalInput();
        if (canStride) {
            stride();
        }
    }

    private void stride() {
        if (strideStartTime == 0) {
            groundState = GroundState.STRIDING;
            strideStartTime = TimeUtils.nanoTime();
        }
        strideAcceleration = Utils.secondsSince(strideStartTime) + Constants.GIGAGAL_STARTING_SPEED;
        velocity.x = Utils.getLateralVelocity(Math.min(Constants.GIGAGAL_MAX_SPEED * strideAcceleration + Constants.GIGAGAL_STARTING_SPEED, Constants.GIGAGAL_MAX_SPEED), facing);
    }

    private void enableDash() {
        handleDirectionalInput();
        if (canDash) {
            dash();
        }
    }

    private void dash() {
        if (groundState != GroundState.DASHING) {
            groundState = GroundState.DASHING;
            dashStartTime = TimeUtils.nanoTime();
            strideStartTime = 0;
            canStride = false;
        }
        if (Utils.secondsSince(dashStartTime) < Constants.MAX_DASH_DURATION) {
            velocity.x = Utils.getLateralVelocity(Constants.GIGAGAL_MAX_SPEED, facing);
        } else {
            canDash = false;
            dashStartTime = 0;
            stand();
        }
    }

    private void enableJump() {
        if (((Gdx.input.isKeyJustPressed(Keys.BACKSLASH) || jumpButtonPressed) && canJump)
                || aerialState == AerialState.JUMPING) {
            jump();
        }
    }

    private void jump() {
        if (canJump) {
            aerialTakeoff = position.x;
            aerialState = AerialState.JUMPING;
            groundState = GroundState.AIRBORNE;
            jumpStartTime = TimeUtils.nanoTime();
            canJump = false;
        }
        velocity.x += Utils.getLateralVelocity(Constants.GIGAGAL_STARTING_SPEED * Constants.STRIDING_JUMP_MULTIPLIER, facing);
        if (Utils.secondsSince(jumpStartTime) < Constants.MAX_JUMP_DURATION) {
            velocity.y = Constants.JUMP_SPEED;
            velocity.y *= Constants.STRIDING_JUMP_MULTIPLIER;
        } else {
            fall();
        }
    }

    private void enableHover() {
        if (canHover) {
            if (Gdx.input.isKeyJustPressed(Keys.BACKSLASH) || jumpButtonPressed) {
                if (aerialState == AerialState.HOVERING) {
                    canHover = false;
                    hoverStartTime = 0;
                    fall();

                } else {
                    hover(); // else hover if canHover is true (set to false after beginning hover)
                }
                // if jump key not pressed, but already hovering, continue to hover
            } else if (aerialState == AerialState.HOVERING) {
                hover();
            }
        }
    }

    private void hover() {
        // canHover can only be true just before beginning to hover
        if (hoverStartTime == 0) {
            aerialState = AerialState.HOVERING; // indicates currently hovering
            hoverStartTime = TimeUtils.nanoTime(); // begins timing hover duration
        }
        hoverTimeSeconds = Utils.secondsSince(hoverStartTime); // for comparing with max hover time
        if (hoverTimeSeconds < Constants.MAX_HOVER_DURATION) {
            velocity.y = 0; // disables impact of gravity
        } else {
            canHover = false;
            fall(); // when max hover time is exceeded
        }
    }

    private void enableRicochet() {
        if (((Gdx.input.isKeyJustPressed(Keys.BACKSLASH) || jumpButtonPressed) && canRicochet)
        || aerialState == AerialState.RICOCHETING) {
            ricochet();
        }
    }

    private void ricochet() {
        if (canRicochet) {
            aerialState = AerialState.RICOCHETING;
            ricochetStartTime = TimeUtils.nanoTime();
            canRicochet = false;
            hoverStartTime = 0;
            canJump = true;
        }
        if (Utils.secondsSince(ricochetStartTime) >= Constants.RICOCHET_FRAME_DURATION) {
            facing = Utils.getOppositeDirection(facing);
            velocity.x = Utils.getLateralVelocity(Constants.GIGAGAL_MAX_SPEED, facing);
            jump();
        } else {
            canChangeDirection = false;
            canHover = true;
        }
    }

    private void stand() {
        velocity.x = 0;
        groundState = GroundState.STANDING;
        aerialState = AerialState.GROUNDED;
        canJump = true;
    }

    private void fall() {
        strideStartTime = 0;
        aerialState = AerialState.FALLING;
        if (groundState != GroundState.AIRBORNE) {
            aerialTakeoff = position.x;
            groundState = GroundState.AIRBORNE;
        }
        canJump = false;
        canDash = false;
        if (canChangeDirection) {
            handleDirectionalInput();
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion region = Assets.getInstance().getGigaGalAssets().standRight;
        if (facing == Direction.RIGHT) {
            if (aerialState != AerialState.GROUNDED) {
                if (aerialState == AerialState.HOVERING) {
                    hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
                    region = Assets.getInstance().getGigaGalAssets().hoverRightAnimation.getKeyFrame(hoverTimeSeconds);
                } else if (aerialState == AerialState.RICOCHETING) {
                    region = Assets.getInstance().getGigaGalAssets().ricochetLeft;
                } else {
                    region = Assets.getInstance().getGigaGalAssets().jumpRight;
                }
            } else if (groundState == GroundState.STANDING) {
                region = Assets.getInstance().getGigaGalAssets().standRight;
            } else if (groundState == GroundState.STRIDING) {
                region = Assets.getInstance().getGigaGalAssets().strideRightAnimation.getKeyFrame(Math.min(strideAcceleration * strideAcceleration, strideAcceleration));
            } else if (groundState == GroundState.DASHING) {
                region = Assets.getInstance().getGigaGalAssets().dashRight;
            }
        } else if (facing == Direction.LEFT) {
            if (aerialState != AerialState.GROUNDED) {
                if (aerialState == AerialState.HOVERING) {
                    hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
                    region = Assets.getInstance().getGigaGalAssets().hoverLeftAnimation.getKeyFrame(hoverTimeSeconds);
                } else if (aerialState == AerialState.RICOCHETING) {
                    region = Assets.getInstance().getGigaGalAssets().ricochetRight;
                } else {
                    region = Assets.getInstance().getGigaGalAssets().jumpLeft;
                }
            } else if (groundState == GroundState.STANDING) {
                region = Assets.getInstance().getGigaGalAssets().standLeft;
            } else if (groundState == GroundState.STRIDING) {
                region = Assets.getInstance().getGigaGalAssets().strideLeftAnimation.getKeyFrame(Math.min(strideAcceleration * strideAcceleration, strideAcceleration));
            } else if (groundState == GroundState.DASHING ) {
                region = Assets.getInstance().getGigaGalAssets().dashLeft;
            }
        }
        Utils.drawTextureRegion(batch, region, position, Constants.GIGAGAL_EYE_POSITION);
    }

    // Getters
    public int getAmmo() { return ammo; }
    public int getHealth() { return health; }
    public int getLives() { return lives; }
    public Direction getDirection() { return facing; }
    public Vector2 getPosition() { return position; }
    public float getWidth() { return Constants.GIGAGAL_STANCE_WIDTH; }
    public float getHeight() { return Constants.GIGAGAL_HEIGHT; }
    public float getLeft() { return position.x - (Constants.GIGAGAL_STANCE_WIDTH / 2); }
    public float getRight() { return position.x + (Constants.GIGAGAL_STANCE_WIDTH / 2); }
    public float getTop() { return position.y + Constants.GIGAGAL_HEAD_RADIUS; }
    public float getBottom() { return position.y - Constants.GIGAGAL_EYE_HEIGHT; }
    public Rectangle getBounds() { return  new Rectangle( getLeft(), getBottom(), getWidth(), getHeight()); }
    public boolean getJumpStatus() { return canJump; }
    public boolean getHoverStatus() { return canHover; }
    public boolean getRicochetStatus() { return canRicochet; }
    public boolean getChargeStatus() { return isCharged; }

    // Setters
    public void setDirection(Direction facing) { this.facing = facing; }
    public void setChargeStartTime(long chargeStartTime) { this.chargeStartTime = chargeStartTime; }
}
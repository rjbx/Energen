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
public class GigaGal {

    // fields
    public final static String TAG = GigaGal.class.getName();
    private Level level;
    private Vector2 spawnLocation;
    private Vector2 position;
    private Vector2 lastFramePosition;
    private Vector2 velocity;
    private Direction facing;
    private AerialState aerialState;
    private GroundState groundState;
    private long walkStartTime;
    private float walkTimeSeconds;
    private long hoverStartTime;
    private float hoverTimeSeconds;
    private boolean canHover;
    private long jumpStartTime;
    private long dashStartTime;
    private boolean isCharged;
    private boolean canDashLeft;
    private boolean canDashRight;
    private long ricochetStartTime;
    private int ammo;
    private int lives;
    private Vector2 jumpStartingPoint;
    private Platform slidPlatform;
    public boolean leftButtonPressed;
    public boolean rightButtonPressed;
    public boolean jumpButtonPressed;
    public boolean shootButtonPressed;
    public long chargeStartTime;

    // ctor
    public GigaGal(Vector2 spawnLocation, Level level) {
        this.spawnLocation = spawnLocation;
        this.level = level;
        position = new Vector2();
        lastFramePosition = new Vector2();
        velocity = new Vector2();
        init();
    }

    public void init() {
        ammo = Constants.INITIAL_AMMO;
        lives = Constants.INITIAL_LIVES;
        respawn();
    }

    public void update(float delta) {

        lastFramePosition.set(position);
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        touchPlatforms(level.getPlatforms());
        recoilFromEnemies(level.getEnemies());
        collectPowerups(level.getPowerups());
        enableRespawn();
        enableShoot();

        if (aerialState == AerialState.GROUNDED && groundState != GroundState.AIRBORNE && groundState != GroundState.RECOILING) {

            velocity.y = 0;
            if (groundState == GroundState.STANDING) {
                enableStride();
                // enableDash();
                enableJump();
            } else if (groundState == GroundState.STRIDING) {
                 enableStride();
                 enableJump();
            } else if (groundState == GroundState.DASHING) {
                // enableJump(velocity.x, facing);
            }
        }

        if (groundState == GroundState.AIRBORNE && aerialState != AerialState.GROUNDED && aerialState != AerialState.RECOILING) {

            velocity.y -= Constants.GRAVITY;
            if (aerialState == AerialState.JUMPING) {
                enableHover();
                // enableRicochet(facing);
            } else if (aerialState == AerialState.FALLING) {
                //fall();
                enableHover();
                // enableRicochet(facing);
            } else if (aerialState == AerialState.HOVERING) {
                velocity.y += Constants.GRAVITY;
                enableHover();
                // enableRicochet(facing);
            }
        }
    }

    private boolean isGrounded(Platform platform) {
        boolean leftFootIn = false;
        boolean rightFootIn = false;
        boolean straddle = false;

        if ((lastFramePosition.y - Constants.GIGAGAL_EYE_HEIGHT >= platform.getTop()) &&
                (position.y - Constants.GIGAGAL_EYE_HEIGHT <= platform.getTop())) {

            float leftFoot = position.x - Constants.GIGAGAL_STANCE_WIDTH / 2;
            float rightFoot = position.x + Constants.GIGAGAL_STANCE_WIDTH / 2;

            leftFootIn = (platform.getLeft() < leftFoot) && (platform.getRight() > leftFoot);
            rightFootIn = (platform.getLeft() < rightFoot) && (platform.getRight() > rightFoot);
            straddle = (platform.getLeft() > leftFoot && platform.getRight() < rightFoot);
        }
        return leftFootIn || rightFootIn || straddle;
    }

    //  -bump (detect contact with top, sides or bottom of platform and reset position to previous frame;
    //  velocity.y equal and opposite to downward velocity i.e. gravity if top, set canRicochet
    //  to true if jumping and side)
    // detect platform contact under feet (changes aerial state to grounded or falling)
    private boolean isCollidingWith(PhysicalEntity entity) {
        Rectangle gigaGalBounds = new Rectangle(
                position.x - Constants.GIGAGAL_STANCE_WIDTH / 2,
                position.y - Constants.GIGAGAL_EYE_HEIGHT,
                Constants.GIGAGAL_STANCE_WIDTH,
                Constants.GIGAGAL_HEIGHT
        );

        Rectangle entityBounds = new Rectangle(
                entity.getPosition().x - (entity.getWidth() / 2),
                entity.getPosition().y - (entity.getHeight() / 2),
                entity.getWidth(),
                entity.getHeight()
        );

        if (gigaGalBounds.overlaps(entityBounds)) {
            return true;
        }
        return false;
    }

    private void touchPlatforms(Array<Platform> platforms) {
        boolean isGrounded = false;
        for (Platform platform : platforms) {
            if (isGrounded(platform)) {
                velocity.y = 0;
                velocity.x = 0;
                position.y = platform.getTop() + Constants.GIGAGAL_EYE_HEIGHT;
                canHover = false;
                aerialState = AerialState.GROUNDED;
                if (groundState == GroundState.AIRBORNE) {
                    groundState = GroundState.STANDING;
                }
                isGrounded = true;
            /* } else if (isCollidingWith(platform)) {
                fall();
                position.x = lastFramePosition.x;
                if (aerialState != AerialState.GROUNDED
                        && aerialState != AerialState.RECOILING
                        && aerialState != AerialState.RICOCHETING) {
                    if (position.y - Constants.GIGAGAL_HEAD_RADIUS <= platform.getTop()
                            && jumpStartingPoint.x != position.x
                            && (Math.abs(velocity.x) > (Constants.GIGAGAL_MAX_SPEED / 2))
                            && position.y - Constants.GIGAGAL_EYE_HEIGHT > platform.getBottom()) {
                        hoverStartTime = TimeUtils.nanoTime();
                        velocity.x = 0;
                        slidPlatform = new Platform(platform);
                    } else {
                        aerialState = AerialState.FALLING;
                        groundState = GroundState.AIRBORNE;
                        walkStartTime = TimeUtils.nanoTime();
                        walkTimeSeconds = 0;
                    }
                } */
            }
        }

        if (!isGrounded && aerialState == AerialState.GROUNDED || aerialState == AerialState.JUMPING) {
            groundState = GroundState.AIRBORNE;
            aerialState = AerialState.FALLING;
        }
    }

    private void collectPowerups(DelayedRemovalArray<Powerup> powerups) {

        for (Powerup powerup : powerups) {
            if (isCollidingWith(powerup)) {
                ammo += Constants.POWERUP_AMMO;
                level.setScore(level.getScore() + Constants.POWERUP_SCORE);
                powerups.removeValue(powerup, true);
            }
        }
    }

    // detect contact with enemy (change aerial & ground state to recoil until grounded) */
    private void recoilFromEnemies(DelayedRemovalArray<Zoomba> zoombas) {

        for (Zoomba zoomba : zoombas) {
            if (isCollidingWith(zoomba)) {
                enableRecoil(zoomba.getPosition().x);
            }
        }
    }

    private void enableRecoil(float xPosition) {
        if (position.x < xPosition) {
            recoil(Direction.LEFT);
        } else {
            recoil(Direction.RIGHT);
        }
    }

    // disables all else by virtue of neither top level update conditions being satisfied due to state
    private void recoil(Direction direction) {
        walkTimeSeconds = 0;
        aerialState = AerialState.RECOILING;
        groundState = GroundState.RECOILING;
        velocity.y = Constants.KNOCKBACK_VELOCITY.y;

        if (direction == Direction.LEFT) {
            velocity.x = -Constants.KNOCKBACK_VELOCITY.x;
        } else {
            velocity.x = Constants.KNOCKBACK_VELOCITY.x;
        }
    }

    private void enableShoot() {

        // incorporate into enableShoot()
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            shoot(AmmoType.REGULAR);
            chargeStartTime = TimeUtils.nanoTime();
        }

        if (Gdx.input.isKeyPressed(Keys.ENTER) || shootButtonPressed) {
            // Shoot
            if (Utils.secondsSince(chargeStartTime) > Constants.CHARGE_DURATION) {
                isCharged = true;
            }
        } else {
            if (isCharged) {
                shoot(AmmoType.CHARGE);
                isCharged = false;
            }
        }
    }

    public void shoot(AmmoType ammoType) {
        if (ammo > 0) {

            ammo--;
            Vector2 bulletPosition;

            if (facing == Direction.RIGHT) {
                bulletPosition = new Vector2(
                        position.x + Constants.GIGAGAL_CANNON_OFFSET.x,
                        position.y + Constants.GIGAGAL_CANNON_OFFSET.y
                );
            } else {
                bulletPosition = new Vector2(
                        position.x - Constants.GIGAGAL_CANNON_OFFSET.x - 5,
                        position.y + Constants.GIGAGAL_CANNON_OFFSET.y
                );
            }
            level.spawnBullet(bulletPosition, facing, ammoType);
        }
    }

    private void enableRespawn() {
        if (position.y < Constants.KILL_PLANE) {
            lives--;
            if (lives > -1) {
                respawn();
            }
        }
    }

    private void respawn() {
        position.set(spawnLocation);
        lastFramePosition.set(spawnLocation);
        velocity.setZero();
        facing = Direction.RIGHT;
        groundState = GroundState.AIRBORNE;
        aerialState = AerialState.FALLING;
        jumpStartTime = 0;
        dashStartTime = 0;
        canHover = false;
        canDashLeft = false;
        canDashRight = false;
        jumpStartingPoint = new Vector2();
    }

    // bump sides disables; change ground state to striding at key detection;
    // change state to standing if single key hold is interrupted by release or keying of
    // other directional in order to reset momentum)
    private void enableStride() {

        if (Gdx.input.isKeyPressed(Keys.A) || leftButtonPressed) {
            facing = Direction.LEFT;
            stride();
        } else if (Gdx.input.isKeyPressed(Keys.S) || rightButtonPressed) {
            facing = Direction.RIGHT;
            stride();
        } else {
            stop();
        }
    }

    private void stride() {
        if (aerialState == AerialState.GROUNDED) {
            if (groundState != GroundState.STRIDING) {
                walkStartTime = TimeUtils.nanoTime();
                groundState = GroundState.STRIDING;
            }
            walkTimeSeconds = Utils.secondsSince(walkStartTime);
        }
        if (facing == Direction.LEFT) {
            velocity.x = Math.max(-Constants.GIGAGAL_MAX_SPEED * walkTimeSeconds, -Constants.GIGAGAL_MAX_SPEED);
        } else {
            velocity.x = Math.min(Constants.GIGAGAL_MAX_SPEED * walkTimeSeconds, Constants.GIGAGAL_MAX_SPEED);
        }
    }

    //  jump (detecting velocity.x prior to key press and adjusting jump height and distance accordingly;
    //  bump platform bottom disables; change state to falling after reaching jump peak; maintain
    //  lateral speed and direction)
    private void enableJump() {
        if (Gdx.input.isKeyJustPressed(Keys.BACKSLASH) || jumpButtonPressed) {
            jump();
        }
    }

    private void jump() {
        jumpStartingPoint = new Vector2(position);
        aerialState = AerialState.JUMPING;
        jumpStartTime = TimeUtils.nanoTime();
        canHover = true;
        if (aerialState == AerialState.JUMPING) {
            if (Utils.secondsSince(jumpStartTime) < Constants.MAX_JUMP_DURATION) {
                velocity.y = Constants.JUMP_SPEED;
                if (Math.abs(velocity.x) >= Constants.GIGAGAL_MAX_SPEED / 2){
                    velocity.y *= Constants.RUNNING_JUMP_MULTIPLIER;
                }
            } else {
                groundState = GroundState.AIRBORNE;
                aerialState = AerialState.FALLING;
            }
        }
    }

    // dash (max speed for short burst in direction facing, no movement in opposite direction
    //        or building momentum, reset momentum;
    private void enableDash() {
        // detect if previously grounded & standing, then striding, then grounded & standing, all within
        // certain timespan, canDash = true && ground state to dashing at key detection)
    }

    private void dash() {
        if (aerialState == AerialState.GROUNDED) {
            groundState = GroundState.DASHING;
            dashStartTime = TimeUtils.nanoTime();
        }
        if ((Utils.secondsSince(dashStartTime) < Constants.MAX_DASH_DURATION) || aerialState == AerialState.HOVERING || aerialState == AerialState.FALLING) {
            if (facing == Direction.LEFT) {
                velocity.x = -Constants.GIGAGAL_MAX_SPEED;
            } else {
                velocity.x = Constants.GIGAGAL_MAX_SPEED;
            }
        } else {
            groundState = GroundState.STANDING;
        }
    }

    //  hover (maintain forward momentum, velocity.y equal and opposite to downward velocity i.e. gravity
    //  until disabled manually or exceed max hover duration)
    private void enableHover() {
        if (Gdx.input.isKeyJustPressed(Keys.BACKSLASH) || aerialState == AerialState.HOVERING) {
            hover();
        }
    }

    private void hover() {
        if (canHover) {
            aerialState = AerialState.HOVERING;
            hoverStartTime = TimeUtils.nanoTime();
            canHover = false;
        }
        hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
        if (aerialState == AerialState.HOVERING) {
            if (hoverTimeSeconds < Constants.MAX_HOVER_DURATION) {
                velocity.y = Constants.GRAVITY;
            } else {
                aerialState = AerialState.FALLING;
            }
        }
    }

    // fix start jump method
    private void enableRicochet() {
        if (Gdx.input.isKeyJustPressed(Keys.BACKSLASH)) {
            ricochetStartTime = TimeUtils.nanoTime();
            ricochet();
        }
    }

    private void ricochet() {
        aerialState = AerialState.RICOCHETING;
        if (Utils.secondsSince(ricochetStartTime) > Constants.RICOCHET_DURATION) {
            if (facing == Direction.LEFT) {
                facing = Direction.RIGHT;
                jump();
            } else {
                facing = Direction.LEFT;
                jump();
            }
        } else {
            ricochetStartTime = 0;
        }
    }

    private void enableFall(Array<Platform> platforms) {

    }

    // update velocity.y
    private void fall() {
        if (groundState == GroundState.STANDING) {
            aerialState = AerialState.GROUNDED;
        }
        if (aerialState == AerialState.GROUNDED) {
            groundState = GroundState.STANDING;
        }
    }

    private void stop() {
        if (groundState != GroundState.STANDING) {
            groundState = GroundState.STANDING;
        }
        velocity.x = 0;
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
                region = Assets.getInstance().getGigaGalAssets().walkRightAnimation.getKeyFrame(Math.min(walkTimeSeconds * walkTimeSeconds, walkTimeSeconds));
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
                region = Assets.getInstance().getGigaGalAssets().walkLeftAnimation.getKeyFrame(Math.min(walkTimeSeconds * walkTimeSeconds, walkTimeSeconds));
            } else if (groundState == GroundState.DASHING ) {
                region = Assets.getInstance().getGigaGalAssets().dashLeft;
            }
        }
        Utils.drawTextureRegion(batch, region, position, Constants.GIGAGAL_EYE_POSITION);
    }

    public int getAmmo() {
        return ammo;
    }

    public int getLives() {
        return lives;
    }

    public Vector2 getPosition() {
        return position;
    }
}

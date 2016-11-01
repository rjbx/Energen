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
    public final static String TAG = GigaGal.class.getName();
    private Level level;
    private Vector2 spawnLocation;
    private Vector2 position;
    private Vector2 lastFramePosition;
    private Vector2 velocity;
    private Direction facing;
    private AerialMove aerialMove;
    private GroundMove groundMove;
    private long walkStartTime;
    private float walkTimeSeconds;
    private long hoverStartTime;
    private float hoverTimeSeconds;
    private boolean hasHovered;
    private long jumpStartTime;
    private long dashStartTime;
    private boolean isCharged;
    private boolean canDashLeft;
    private boolean canDashRight;
    private int doubleTapDirectional;
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

    public GigaGal(Vector2 spawnLocation, Level level) {
        this.spawnLocation = spawnLocation;
        this.level = level;
        position = new Vector2();
        lastFramePosition = new Vector2();
        velocity = new Vector2();
        init();
    }

    public int getAmmo() {
        return ammo;
    }

    public int getLives() {
        return lives;
    }

    public void init() {
        ammo = Constants.INITIAL_AMMO;
        lives = Constants.INITIAL_LIVES;
        respawn();
    }

    private void respawn() {
        position.set(spawnLocation);
        lastFramePosition.set(spawnLocation);
        velocity.setZero();
        aerialMove = AerialMove.FALLING;
        facing = Direction.RIGHT;
        groundMove = GroundMove.STANDING;
        jumpStartTime = 0;
        hasHovered = false;
        canDashLeft = false;
        canDashRight = false;
        doubleTapDirectional = 0;
        jumpStartingPoint = new Vector2();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void update(float delta, Array<Platform> platforms) {

        lastFramePosition.set(position);

        if (aerialMove != AerialMove.RICOCHETING) {
            velocity.y -= Constants.GRAVITY;
        } else if (Utils.secondsSince(ricochetStartTime) > Constants.RICOCHET_DURATION) {
            if (facing == Direction.LEFT) {
                facing = Direction.RIGHT;
                velocity.x = Constants.GIGAGAL_MAX_SPEED;
            } else {
                facing = Direction.LEFT;
                velocity.x = -Constants.GIGAGAL_MAX_SPEED;
            }
            startJump();
        }
        position.mulAdd(velocity, delta);

        if (position.y < Constants.KILL_PLANE) {
            lives--;
            if (lives > -1) {
                respawn();
            }
        }

        // Land on/fall off platforms
        // TODO: fix momentum after jumping into collisions post- recoil
        for (Platform platform : platforms) {
            if ((lastFramePosition.y + Constants.GIGAGAL_HEAD_RADIUS < platform.bottom)
                    && position.y + Constants.GIGAGAL_HEAD_RADIUS >= platform.bottom
                    && position.x < platform.right
                    && position.x > platform.left) {
                endJump();
                position.y = lastFramePosition.y;
                velocity.y = -Constants.GRAVITY;
            }

            if (isLanding(platform)) {
                if (aerialMove == AerialMove.RECOILING) {
                    velocity.x = 0;
                    walkStartTime = TimeUtils.nanoTime();
                    jumpStartTime = 0;
                }
                if (jumpStartTime != 0) {
                    walkStartTime += TimeUtils.nanoTime() - jumpStartTime;
                    jumpStartTime = 0;
                }
                aerialMove = AerialMove.GROUNDED;
                hasHovered = false;
                velocity.y = 0;
                position.y = platform.top + Constants.GIGAGAL_EYE_HEIGHT;
            } else if (isBumping(platform)) {

                position.x = lastFramePosition.x;
                if (aerialMove != AerialMove.GROUNDED
                        && aerialMove != AerialMove.RECOILING
                        && aerialMove != AerialMove.RICOCHETING) {
                    if (position.y - Constants.GIGAGAL_HEAD_RADIUS <= platform.top
                            && jumpStartingPoint.x != position.x
                            && (Math.abs(velocity.x) > (Constants.GIGAGAL_MAX_SPEED / 2))
                            && position.y - Constants.GIGAGAL_EYE_HEIGHT > platform.bottom) {
                        aerialMove = AerialMove.SLIDING;
                        hoverStartTime = TimeUtils.nanoTime();
                        groundMove = GroundMove.LEANING;
                        velocity.x = 0;

                        slidPlatform = new Platform(platform);
                    } else {
                        aerialMove = AerialMove.FALLING;
                        groundMove = GroundMove.STANDING;
                        walkStartTime = TimeUtils.nanoTime();
                        walkTimeSeconds = 0;
                    }
                }
            }
        }
        // Collide with enemies
        Rectangle gigaGalBounds = new Rectangle(
                position.x - Constants.GIGAGAL_STANCE_WIDTH / 2,
                position.y - Constants.GIGAGAL_EYE_HEIGHT,
                Constants.GIGAGAL_STANCE_WIDTH,
                Constants.GIGAGAL_HEIGHT);

        for (Zoomba zoomba : level.getEnemies()) {
            Rectangle zoombaBounds = new Rectangle(
                    zoomba.position.x - Constants.ZOOMBA_COLLISION_RADIUS,
                    zoomba.position.y - Constants.ZOOMBA_COLLISION_RADIUS,
                    2 * Constants.ZOOMBA_COLLISION_RADIUS,
                    2 * Constants.ZOOMBA_COLLISION_RADIUS
            );
            if (gigaGalBounds.overlaps(zoombaBounds)) {

                if (position.x < zoomba.position.x) {
                    recoilFromHit(Direction.LEFT);
                } else {
                    recoilFromHit(Direction.RIGHT);
                }
            }
        }

        // TODO: update OnScreenControls with new physics
        // Move left/right
        if (groundMove != GroundMove.DASHING) {
            if (aerialMove == AerialMove.GROUNDED) {
                boolean left = Gdx.input.isKeyPressed(Keys.A) || leftButtonPressed;
                boolean right = Gdx.input.isKeyPressed(Keys.S) || rightButtonPressed;

                if (left && !right) {
                    moveLeft();
                } else if (right && !left) {
                    moveRight();
                } else {
                    walkTimeSeconds = 0;
                    walkStartTime = TimeUtils.nanoTime();
                    velocity.x /= 2;
                    groundMove = GroundMove.STANDING;
                    if (velocity.x >= -.01f && velocity.x <= .01f) {
                        velocity.x = 0;
                        groundMove = GroundMove.STANDING;
                    }
                }

                // TODO: enable dash on touch screen left/right button double press
                if (Gdx.input.isKeyJustPressed(Keys.A)) {
                    if (canDashLeft && Utils.secondsSince(dashStartTime) < Constants.DOUBLE_TAP_SPEED) {
                        startDash();
                        canDashLeft = false;
                    } else {
                        canDashRight = false;
                        canDashLeft = true;
                        dashStartTime = TimeUtils.nanoTime();
                    }
                } else if (Gdx.input.isKeyJustPressed(Keys.S)) {
                    if (canDashRight && Utils.secondsSince(dashStartTime) < Constants.DOUBLE_TAP_SPEED) {
                        startDash();
                        canDashRight = false;
                    } else {
                        canDashLeft = false;
                        canDashRight = true;
                        dashStartTime = TimeUtils.nanoTime();
                    }
                }
            }
        } else {
            continueDash();
        }

        // Jump
        if (Gdx.input.isKeyJustPressed(Keys.BACKSLASH) || jumpButtonPressed) {
            switch (aerialMove) {
                case GROUNDED:
                    startJump();
                    break;
                case JUMPING:
                    continueJump();
                    break;
                case FALLING:
                    startHover();
                    break;
                case HOVERING:
                    endHover();
                    break;
                case SLIDING:
                    if (position.y - Constants.GIGAGAL_HEAD_RADIUS > slidPlatform.bottom) {
                        aerialMove = AerialMove.RICOCHETING;
                        ricochetStartTime = TimeUtils.nanoTime();
                    }
                    break;
            }
        } else {
            continueHover();
            endJump();
        }

        // Check powerups
        DelayedRemovalArray<Powerup> powerups = level.getPowerups();
        powerups.begin();
        for (int i = 0; i < powerups.size; i++) {
            Powerup powerup = powerups.get(i);
            Rectangle powerupBounds = new Rectangle(
                    powerup.position.x - Constants.POWERUP_CENTER.x,
                    powerup.position.y - Constants.POWERUP_CENTER.y,
                    Assets.getInstance().getPowerupAssets().powerup.getRegionWidth(),
                    Assets.getInstance().getPowerupAssets().powerup.getRegionHeight()
            );
            if (gigaGalBounds.overlaps(powerupBounds)) {
                ammo += Constants.POWERUP_AMMO;
                level.score += Constants.POWERUP_SCORE;
                powerups.removeIndex(i);
            }
        }
        powerups.end();

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

    private boolean isLanding(Platform platform) {
        boolean leftFootIn = false;
        boolean rightFootIn = false;
        boolean straddle = false;

        if (lastFramePosition.y - Constants.GIGAGAL_EYE_HEIGHT >= platform.top &&
                position.y - Constants.GIGAGAL_EYE_HEIGHT < platform.top + Constants.GIGAGAL_HEAD_RADIUS) {

            float leftFoot = position.x - Constants.GIGAGAL_STANCE_WIDTH / 2;
            float rightFoot = position.x + Constants.GIGAGAL_STANCE_WIDTH / 2;

            leftFootIn = (platform.left < leftFoot && platform.right > leftFoot);
            rightFootIn = (platform.left < rightFoot && platform.right > rightFoot);
            straddle = (platform.left > leftFoot && platform.right < rightFoot);
        }
        return leftFootIn || rightFootIn || straddle;
    }

    private boolean isBumping(Platform platform) {
        if (platform.top - platform.bottom > Constants.MAX_LEDGE_HEIGHT) {

            float margin = Constants.GIGAGAL_STANCE_WIDTH / 2;

            if ((lastFramePosition.x + margin) <= platform.left &&
                    (position.x + margin) > platform.left
                    && (position.y - Constants.GIGAGAL_EYE_HEIGHT) < platform.top
                    && (position.y + Constants.GIGAGAL_HEAD_RADIUS > platform.bottom)) {
                return true;
            }
            if ((lastFramePosition.x - margin) >= platform.right &&
                    (position.x - margin) < platform.right
                    && (position.y - Constants.GIGAGAL_EYE_HEIGHT) < platform.top
                    && (position.y + Constants.GIGAGAL_HEAD_RADIUS > platform.bottom)) {
                return true;
            }
        }
        return false;
    }

    private void moveLeft() {

        facing = Direction.LEFT;
        if (aerialMove == AerialMove.GROUNDED) {
            if (groundMove != GroundMove.WALKING) {
                walkStartTime = TimeUtils.nanoTime();
                groundMove = GroundMove.WALKING;
            }
            walkTimeSeconds = Utils.secondsSince(walkStartTime);
        }
        velocity.x = -Math.min(walkTimeSeconds * Constants.GIGAGAL_MAX_SPEED, Constants.GIGAGAL_MAX_SPEED);
    }

    private void moveRight() {

        facing = Direction.RIGHT;
        if (aerialMove == AerialMove.GROUNDED) {
            if (groundMove != GroundMove.WALKING) {
                walkStartTime = TimeUtils.nanoTime();
                groundMove = GroundMove.WALKING;
            }
            walkTimeSeconds = Utils.secondsSince(walkStartTime);
        }
        velocity.x = Math.min(walkTimeSeconds * Constants.GIGAGAL_MAX_SPEED, Constants.GIGAGAL_MAX_SPEED);
    }

    private void startDash() {
        if (aerialMove == AerialMove.GROUNDED) {
            groundMove = GroundMove.DASHING;
            dashStartTime = TimeUtils.nanoTime();
            continueDash();
        }
    }

    private void continueDash() {
        if ((Utils.secondsSince(dashStartTime) < Constants.MAX_DASH_DURATION) || aerialMove == AerialMove.HOVERING || aerialMove == AerialMove.FALLING) {
            if (facing == Direction.LEFT) {
                velocity.x = -Constants.GIGAGAL_MAX_SPEED;
            } else {
                velocity.x = Constants.GIGAGAL_MAX_SPEED;
            }
        } else {
            endDash();
        }
    }

    private void endDash() {
        groundMove = GroundMove.LEANING;
        velocity.x = 0;
    }


    public void startJump() {
        jumpStartingPoint = new Vector2(position);
        aerialMove = AerialMove.JUMPING;
        jumpStartTime = TimeUtils.nanoTime();
        continueJump();
    }

    private void continueJump() {
        if (aerialMove == AerialMove.JUMPING) {
            if (Utils.secondsSince(jumpStartTime) < Constants.MAX_JUMP_DURATION) {
                velocity.y = Constants.JUMP_SPEED;
                if (Math.abs(velocity.x) >= Constants.GIGAGAL_MAX_SPEED / 2){
                    velocity.y *= Constants.RUNNING_JUMP_MULTIPLIER;
                }
            } else {
                endJump();
            }
        }
    }

    private void endJump() {
        if (aerialMove == AerialMove.JUMPING) {
            aerialMove = AerialMove.FALLING;
        }
    }

    private void startHover() {
        if (!hasHovered) {
            aerialMove = AerialMove.HOVERING;
            hoverStartTime = TimeUtils.nanoTime();
        }
        continueHover();
    }

    private void continueHover() {
        hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
        if (aerialMove == AerialMove.HOVERING) {
            if (hoverTimeSeconds < Constants.MAX_HOVER_DURATION) {
                velocity.y = 0;
            } else {
                endHover();
            }
        }
    }

    private void endHover() {
        if (aerialMove == AerialMove.HOVERING) {
            aerialMove = AerialMove.FALLING;
            hasHovered = true;
        }
    }

    private void recoilFromHit(Direction direction) {
        walkTimeSeconds = 0;
        aerialMove = AerialMove.RECOILING;
        velocity.y = Constants.KNOCKBACK_VELOCITY.y;

        if (direction == Direction.LEFT) {
            velocity.x = -Constants.KNOCKBACK_VELOCITY.x;
        } else {
            velocity.x = Constants.KNOCKBACK_VELOCITY.x;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion region = Assets.getInstance().getGigaGalAssets().standRight;
        if (facing == Direction.RIGHT) {
            if (aerialMove != AerialMove.GROUNDED) {
                if (aerialMove == AerialMove.HOVERING) {
                    hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
                    region = Assets.getInstance().getGigaGalAssets().hoverRightAnimation.getKeyFrame(hoverTimeSeconds);
                } else if (aerialMove == AerialMove.RICOCHETING) {
                    region = Assets.getInstance().getGigaGalAssets().ricochetLeft;
                } else {
                    region = Assets.getInstance().getGigaGalAssets().jumpRight;
                }
            } else if (groundMove == GroundMove.STANDING) {
                region = Assets.getInstance().getGigaGalAssets().standRight;
            } else if (groundMove == GroundMove.WALKING) {
                region = Assets.getInstance().getGigaGalAssets().walkRightAnimation.getKeyFrame(Math.min(walkTimeSeconds * walkTimeSeconds, walkTimeSeconds));
            } else if (groundMove == GroundMove.DASHING
                    || groundMove == GroundMove.LEANING) {
                region = Assets.getInstance().getGigaGalAssets().dashRight;
            }
        } else if (facing == Direction.LEFT) {
            if (aerialMove != AerialMove.GROUNDED) {
                if (aerialMove == AerialMove.HOVERING) {
                    hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
                    region = Assets.getInstance().getGigaGalAssets().hoverLeftAnimation.getKeyFrame(hoverTimeSeconds);
                } else if (aerialMove == AerialMove.RICOCHETING) {
                    region = Assets.getInstance().getGigaGalAssets().ricochetRight;
                } else {
                    region = Assets.getInstance().getGigaGalAssets().jumpLeft;
                }
            } else if (groundMove == GroundMove.STANDING) {
                region = Assets.getInstance().getGigaGalAssets().standLeft;
            } else if (groundMove == GroundMove.WALKING) {
                region = Assets.getInstance().getGigaGalAssets().walkLeftAnimation.getKeyFrame(Math.min(walkTimeSeconds * walkTimeSeconds, walkTimeSeconds));
            } else if (groundMove == GroundMove.DASHING
                    || groundMove == GroundMove.LEANING) {
                region = Assets.getInstance().getGigaGalAssets().dashLeft;
            }
        }

        Utils.drawTextureRegion(batch, region, position, Constants.GIGAGAL_EYE_POSITION);
    }
}

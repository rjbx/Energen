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

    private void respawn() {
        position.set(spawnLocation);
        lastFramePosition.set(spawnLocation);
        velocity.setZero();
        aerialMove = AerialMove.FALLING;
        facing = Direction.RIGHT;
        groundMove = GroundMove.STANDING;
        jumpStartTime = 0;
        dashStartTime = 0;
        hasHovered = false;
        canDashLeft = false;
        canDashRight = false;
        canDashLeft = false;
        canDashRight = false;
        jumpStartingPoint = new Vector2();
    }

    public void update(float delta, Array<Platform> platforms) {

        /*
        enums
        -each represents a unique render texture region (eliminate slide)

        methods
        -recoil disables all else
        -bump (detect contact with top, sides or bottom of platform and reset position to previous frame;
            velocity.y equal and opposite to downward velocity if top, set canRicochet to true if jumping and side)
        -move left / right (building up momentum according to key hold duration; velocity.x < max;
            bump sides disables)
        -jump (detecting velocity.x prior to key press and adjusting jump height and distance accordingly;
            bump bottom disables)
        -dash (max speed for short burst in direction facing, no movement in opposite direction
            or building momentum, reset momentum)

        at all times
        1. enable shoot (with or without charge)
        2. detect platform contact under feet (change state to grounded)
        3. detect contact with enemy (change aerial & ground state to recoil until grounded)

        if grounded and not recoiling
            if standing (not keying left or right directional)
                1. reset momentum
                2. enable move left / right (change ground state to striding at key detection)
                3. if previously grounded & standing, then striding, then grounded & standing, all within
                    certain timespan, enable dash (canDash + ground state to dashing at key detection)
            else if striding (keying left or right directional)
                1. build momentum so long as one key is held
                2. change state to standing if single key hold is interrupted (by release or keying of
                    other directional in order to reset momentum
                3. enable jump (in same direction and speed as stride)
            else if dashing
                1. trigger dash method
                2. enable jump during dash (in same direction and speed as dash)

        if airborne and not recoiling
            if jumping


         */

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
            if ((lastFramePosition.y + Constants.GIGAGAL_HEAD_RADIUS < platform.getBottom())
                    && position.y + Constants.GIGAGAL_HEAD_RADIUS >= platform.getBottom()
                    && position.x < platform.getRight()
                    && position.x > platform.getLeft()) {
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
                position.y = platform.getTop() + Constants.GIGAGAL_EYE_HEIGHT;
            } else if (isBumping(platform)) {

                position.x = lastFramePosition.x;
                if (aerialMove != AerialMove.GROUNDED
                        && aerialMove != AerialMove.RECOILING
                        && aerialMove != AerialMove.RICOCHETING) {
                    if (position.y - Constants.GIGAGAL_HEAD_RADIUS <= platform.getTop()
                            && jumpStartingPoint.x != position.x
                            && (Math.abs(velocity.x) > (Constants.GIGAGAL_MAX_SPEED / 2))
                            && position.y - Constants.GIGAGAL_EYE_HEIGHT > platform.getBottom()) {
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
                    zoomba.getPosition().x - Constants.ZOOMBA_COLLISION_RADIUS,
                    zoomba.getPosition().y - Constants.ZOOMBA_COLLISION_RADIUS,
                    2 * Constants.ZOOMBA_COLLISION_RADIUS,
                    2 * Constants.ZOOMBA_COLLISION_RADIUS
            );
            if (gigaGalBounds.overlaps(zoombaBounds)) {

                if (position.x < zoomba.getPosition().x) {
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
                if (!right && left) {
                    if (canDashLeft == false) {
                        if (dashStartTime == 0) {
                            canDashLeft = true;
                        } else if (Utils.secondsSince(dashStartTime) < Constants.DOUBLE_TAP_SPEED) {
                            startDash();
                        } else {
                            dashStartTime = 0;
                        }
                        canDashRight = false;
                    } else {
                        moveLeft();
                    }
                } else if (right && !left) {
                    if (canDashRight == false) {
                        if (dashStartTime == 0) {
                            canDashRight = true;
                        } else if (Utils.secondsSince(dashStartTime) < Constants.DOUBLE_TAP_SPEED) {
                            startDash();
                        } else {
                            dashStartTime = 0;
                        }
                        canDashLeft = false;
                    } else {
                       moveRight();
                    }
                } else {
                    walkTimeSeconds = 0;
                    walkStartTime = TimeUtils.nanoTime();
                    velocity.x /= 2;
                    groundMove = GroundMove.STANDING;
                    if (dashStartTime == 0) {
                        if (canDashLeft == true) {
                            canDashLeft = false;
                            canDashRight = true;
                            dashStartTime = TimeUtils.nanoTime();
                        } else if (canDashRight == true) {
                            canDashRight = false;
                            canDashLeft = true;
                            dashStartTime = TimeUtils.nanoTime();
                        }
                    } else if (Utils.secondsSince(dashStartTime) > Constants.DOUBLE_TAP_SPEED) {
                        canDashLeft = false;
                        canDashRight = false;
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
                    if (position.y - Constants.GIGAGAL_HEAD_RADIUS > slidPlatform.getBottom()) {
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
                    powerup.getPosition().x - Constants.POWERUP_CENTER.x,
                    powerup.getPosition().y - Constants.POWERUP_CENTER.y,
                    Assets.getInstance().getPowerupAssets().powerup.getRegionWidth(),
                    Assets.getInstance().getPowerupAssets().powerup.getRegionHeight()
            );
            if (gigaGalBounds.overlaps(powerupBounds)) {
                ammo += Constants.POWERUP_AMMO;
                level.setScore(level.getScore() + Constants.POWERUP_SCORE);
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

        if (lastFramePosition.y - Constants.GIGAGAL_EYE_HEIGHT >= platform.getTop() &&
                position.y - Constants.GIGAGAL_EYE_HEIGHT < platform.getTop() + Constants.GIGAGAL_HEAD_RADIUS) {

            float leftFoot = position.x - Constants.GIGAGAL_STANCE_WIDTH / 2;
            float rightFoot = position.x + Constants.GIGAGAL_STANCE_WIDTH / 2;

            leftFootIn = (platform.getLeft() < leftFoot && platform.getRight() > leftFoot);
            rightFootIn = (platform.getLeft() < rightFoot && platform.getRight() > rightFoot);
            straddle = (platform.getLeft() > leftFoot && platform.getRight() < rightFoot);
        }
        return leftFootIn || rightFootIn || straddle;
    }

    private boolean isBumping(Platform platform) {
        if (platform.getTop() - platform.getBottom() > Constants.MAX_LEDGE_HEIGHT) {

            float margin = Constants.GIGAGAL_STANCE_WIDTH / 2;

            if ((lastFramePosition.x + margin) <= platform.getLeft() &&
                    (position.x + margin) > platform.getLeft()
                    && (position.y - Constants.GIGAGAL_EYE_HEIGHT) < platform.getTop()
                    && (position.y + Constants.GIGAGAL_HEAD_RADIUS > platform.getBottom())) {
                return true;
            }
            if ((lastFramePosition.x - margin) >= platform.getRight() &&
                    (position.x - margin) < platform.getRight()
                    && (position.y - Constants.GIGAGAL_EYE_HEIGHT) < platform.getTop()
                    && (position.y + Constants.GIGAGAL_HEAD_RADIUS > platform.getBottom())) {
                return true;
            }
        }
        return false;
    }

    private void moveLeft() {

        facing = Direction.LEFT;
        if (aerialMove == AerialMove.GROUNDED) {
            if (groundMove != GroundMove.STRIDING) {
                walkStartTime = TimeUtils.nanoTime();
                groundMove = GroundMove.STRIDING;
            }
            walkTimeSeconds = Utils.secondsSince(walkStartTime);
        }
        velocity.x = -Math.min(walkTimeSeconds * Constants.GIGAGAL_MAX_SPEED, Constants.GIGAGAL_MAX_SPEED);
    }

    private void moveRight() {

        facing = Direction.RIGHT;
        if (aerialMove == AerialMove.GROUNDED) {
            if (groundMove != GroundMove.STRIDING) {
                walkStartTime = TimeUtils.nanoTime();
                groundMove = GroundMove.STRIDING;
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
            } else if (groundMove == GroundMove.STRIDING) {
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
            } else if (groundMove == GroundMove.STRIDING) {
                region = Assets.getInstance().getGigaGalAssets().walkLeftAnimation.getKeyFrame(Math.min(walkTimeSeconds * walkTimeSeconds, walkTimeSeconds));
            } else if (groundMove == GroundMove.DASHING
                    || groundMove == GroundMove.LEANING) {
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

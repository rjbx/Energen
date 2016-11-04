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
        aerialState = AerialState.FALLING;
        facing = Direction.RIGHT;
        groundState = GroundState.STANDING;
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

        methods: "enable[X]" incorporate all key press / button tap listeners
        -recoil disables all else
        -bump (detect contact with top, sides or bottom of platform and reset position to previous frame;
            velocity.y equal and opposite to downward velocity i.e. gravity if top, set canRicochet
            to true if jumping and side)
        -move left / right (building up momentum according to key hold duration; velocity.x < max;
            bump sides disables; change ground state to striding at key detection;
            change state to standing if single key hold is interrupted by release or keying of
                other directional in order to reset momentum)
        -jump (detecting velocity.x prior to key press and adjusting jump height and distance accordingly;
            bump platform bottom disables; change state to falling after reaching jump peak; maintain
            lateral speed and direction)
        -dash (max speed for short burst in direction facing, no movement in opposite direction
            or building momentum, reset momentum; if previously grounded & standing, then striding, then grounded & standing, all within
            certain timespan, canDash = true && ground state to dashing at key detection)
        -hover (maintain forward momentum, velocity.y equal and opposite to downward velocity i.e. gravity
            until disabled manually or exceed max hover duration)
        -slide (reset forward momentum, enable wall jump upon key detection)

        at all times
        1. enable shoot (with or without charge)
        2. detect platform contact under feet (changes aerial state to grounded or falling)
        3. detect contact with enemy (change aerial & ground state to recoil until grounded)

        if (aerialState == AerialState.GROUNDED && groundState != GroundState.RECOILING)
            if (groundState == GroundState.STANDING (not keying left or right directional)
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
                1. enable hover upon key detection
                2. change state to sliding upon platform side collision detection
            else if falling
                1. enable hover upon key detection
                2. change state to sliding upon platform side collision detection
            else if hovering
                1. disable hover upon key detection
                2. change state to sliding upon platform side collision detection
            else if sliding
                1. enable wall jump
         */

        if (aerialState == AerialState.GROUNDED && groundState != GroundState.RECOILING) {
            if (groundState == GroundState.STANDING) {
                stop();
                enableStride();
                enableDash();
                enableJump();
            } else if (groundState == GroundState.STRIDING) {
                enableStride();
                enableJump();
            } else if (groundState == GroundState.DASHING) {
                enableJump();
            }
        }

        if (groundState == GroundState.AIRBORNE && aerialState != AerialState.RECOILING) {
            if (aerialState == AerialState.JUMPING) {
                enableHover();
                enableSlide();
            } else if (aerialState == AerialState.FALLING) {
                enableHover();
                enableSlide();
            } else if (aerialState == AerialState.HOVERING) {
                disableHover();
            } else if (aerialState == AerialState.SLIDING) {
                enableRicochet();
            }
        }

        lastFramePosition.set(position);

        if (aerialState != AerialState.RICOCHETING) {
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
                if (aerialState == AerialState.RECOILING) {
                    velocity.x = 0;
                    walkStartTime = TimeUtils.nanoTime();
                    jumpStartTime = 0;
                }
                if (jumpStartTime != 0) {
                    walkStartTime += TimeUtils.nanoTime() - jumpStartTime;
                    jumpStartTime = 0;
                }
                aerialState = AerialState.GROUNDED;
                hasHovered = false;
                velocity.y = 0;
                position.y = platform.getTop() + Constants.GIGAGAL_EYE_HEIGHT;
            } else if (isBumping(platform)) {

                position.x = lastFramePosition.x;
                if (aerialState != AerialState.GROUNDED
                        && aerialState != AerialState.RECOILING
                        && aerialState != AerialState.RICOCHETING) {
                    if (position.y - Constants.GIGAGAL_HEAD_RADIUS <= platform.getTop()
                            && jumpStartingPoint.x != position.x
                            && (Math.abs(velocity.x) > (Constants.GIGAGAL_MAX_SPEED / 2))
                            && position.y - Constants.GIGAGAL_EYE_HEIGHT > platform.getBottom()) {
                        aerialState = AerialState.SLIDING;
                        hoverStartTime = TimeUtils.nanoTime();
                        groundState = GroundState.LEANING;
                        velocity.x = 0;
                        slidPlatform = new Platform(platform);
                    } else {
                        aerialState = AerialState.FALLING;
                        groundState = GroundState.STANDING;
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
        if (groundState != GroundState.DASHING) {
            if (aerialState == AerialState.GROUNDED) {
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
                    groundState = GroundState.STANDING;
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
            switch (aerialState) {
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
                        aerialState = AerialState.RICOCHETING;
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


    private void stop() {
        velocity.x = 0;
    }

    private void fall() {

    }

    //  jump (detecting velocity.x prior to key press and adjusting jump height and distance accordingly;
    //  bump platform bottom disables; change state to falling after reaching jump peak; maintain
    //  lateral speed and direction)
    private void enableJump(float lateralVelocity, Direction facing) {
        if (Gdx.input.isKeyJustPressed(Keys.BACKSLASH)) {
            startJump(lateralVelocity, facing);
        }
    }

    private void jump(float lateralVelocity, Direction facing) {

    }

    // dash (max speed for short burst in direction facing, no movement in opposite direction
    //        or building momentum, reset momentum;
    private void enableDash() {
        // detect if previously grounded & standing, then striding, then grounded & standing, all within
        // ertain timespan, canDash = true && ground state to dashing at key detection)
    }

    private void dash() {

    }


    //  hover (maintain forward momentum, velocity.y equal and opposite to downward velocity i.e. gravity
    //  until disabled manually or exceed max hover duration)
    private void enableHover(float lateralVelocity, Direction facing) {
        if (Gdx.input.isKeyJustPressed(Keys.BACKSLASH)) {
            startHover(lateralVelocity, facing);
        }
    }

    private void hover(float lateralVelocity, Direction facing) {

    }

    // (reset forward momentum, enable wall jump upon key detection)
    private void enableSlide() {

    }

    private void slide() {

    }

    // fix start jump method
    private void enableRicochet(Direction facing) {
        if (Gdx.input.isKeyJustPressed(Keys.BACKSLASH)) {
            if (facing == Direction.LEFT) {
                startJump(Direction.RIGHT, Constants.GIGAGAL_MAX_SPEED);
            } else {
                startJump(Direction.LEFT, Constants.GIGAGAL_MAX_SPEED);
            }
        }
    }

    private void ricochet(Direction facing) {

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

    //  -bump (detect contact with top, sides or bottom of platform and reset position to previous frame;
    //  velocity.y equal and opposite to downward velocity i.e. gravity if top, set canRicochet
    //  to true if jumping and side)
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

    // move left / right (building up momentum according to key hold duration; velocity.x < max;
    // bump sides disables; change ground state to striding at key detection;
    // change state to standing if single key hold is interrupted by release or keying of
    // other directional in order to reset momentum)
    private void enableStride(float lateralVelocity) {

    }

    // integrate with enableStride()
    private void moveLeft() {

        facing = Direction.LEFT;
        if (aerialState == AerialState.GROUNDED) {
            if (groundState != GroundState.STRIDING) {
                walkStartTime = TimeUtils.nanoTime();
                groundState = GroundState.STRIDING;
            }
            walkTimeSeconds = Utils.secondsSince(walkStartTime);
        }
        velocity.x = -Math.min(walkTimeSeconds * Constants.GIGAGAL_MAX_SPEED, Constants.GIGAGAL_MAX_SPEED);
    }

    // integrate with enableStride()
    private void moveRight() {

        facing = Direction.RIGHT;
        if (aerialState == AerialState.GROUNDED) {
            if (groundState != GroundState.STRIDING) {
                walkStartTime = TimeUtils.nanoTime();
                groundState = GroundState.STRIDING;
            }
            walkTimeSeconds = Utils.secondsSince(walkStartTime);
        }
        velocity.x = Math.min(walkTimeSeconds * Constants.GIGAGAL_MAX_SPEED, Constants.GIGAGAL_MAX_SPEED);
    }

    private void startDash() {
        if (aerialState == AerialState.GROUNDED) {
            groundState = GroundState.DASHING;
            dashStartTime = TimeUtils.nanoTime();
            continueDash();
        }
    }

    private void continueDash() {
        if ((Utils.secondsSince(dashStartTime) < Constants.MAX_DASH_DURATION) || aerialState == AerialState.HOVERING || aerialState == AerialState.FALLING) {
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
        groundState = GroundState.LEANING;
        velocity.x = 0;
    }

    // combine start and continue jump adding parameters to set velocity and direction
    public void startJump() {
        jumpStartingPoint = new Vector2(position);
        aerialState = AerialState.JUMPING;
        jumpStartTime = TimeUtils.nanoTime();
        continueJump();
    }

    private void continueJump() {
        if (aerialState == AerialState.JUMPING) {
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
        if (aerialState == AerialState.JUMPING) {
            aerialState = AerialState.FALLING;
        }
    }

    private void startHover() {
        if (!hasHovered) {
            aerialState = AerialState.HOVERING;
            hoverStartTime = TimeUtils.nanoTime();
        }
        continueHover();
    }

    private void continueHover() {
        hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
        if (aerialState == AerialState.HOVERING) {
            if (hoverTimeSeconds < Constants.MAX_HOVER_DURATION) {
                velocity.y = 0;
            } else {
                endHover();
            }
        }
    }

    private void endHover() {
        if (aerialState == AerialState.HOVERING) {
            aerialState = AerialState.FALLING;
            hasHovered = true;
        }
    }

    // disables all else by virtue of neither top level update conditions being satisfied due to state
    private void recoilFromHit(Direction direction) {
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
            } else if (groundState == GroundState.DASHING
                    || groundState == GroundState.LEANING) {
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
            } else if (groundState == GroundState.DASHING
                    || groundState == GroundState.LEANING) {
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

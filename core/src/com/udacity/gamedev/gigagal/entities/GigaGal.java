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
        // MUCH BETTER

        velocity.y -= Constants.GRAVITY;
        position.mulAdd(velocity, delta);
        lastFramePosition.set(position);
        enableRespawn();
        enableShoot();

        // refactor into single detect collision method?
        detectPlatformCollision(platforms);
        detectEnemyCollision(level.getEnemies());
        detectPowerupCollision(level.getPowerups());

        if (aerialState == AerialState.GROUNDED && groundState != GroundState.RECOILING) {
            if (groundState == GroundState.STANDING) {
                stop();
                enableStride(velocity.x);
                enableDash();
                enableJump(velocity.x, facing);
            } else if (groundState == GroundState.STRIDING) {
                enableStride(velocity.x);
                enableJump(velocity.x, facing);
            } else if (groundState == GroundState.DASHING) {
                enableJump(velocity.x, facing);
            }
        }

        if (groundState == GroundState.AIRBORNE && aerialState != AerialState.RECOILING) {
            if (aerialState == AerialState.JUMPING) {
                enableHover(velocity.x, facing);
                enableSlide();
            } else if (aerialState == AerialState.FALLING) {
                enableHover(velocity.x, facing);
                enableSlide();
            } else if (aerialState == AerialState.HOVERING) {
                enableSlide();
            } else if (aerialState == AerialState.SLIDING) {
                enableRicochet(facing);
            }
        }
    }

/*
        if (groundState != GroundState.DASHING) {
            if (aerialState == AerialState.GROUNDED) {
                boolean left = Gdx.input.isKeyPressed(Keys.A) || leftButtonPressed;
                boolean right = Gdx.input.isKeyPressed(Keys.S) || rightButtonPressed;
                if (!right && left) {
                    if (canDashLeft == false) {
                        if (dashStartTime == 0) {
                            canDashLeft = true;
                        } else if (Utils.secondsSince(dashStartTime) < Constants.DOUBLE_TAP_SPEED) {
                      //      startDash();
                        } else {
                            dashStartTime = 0;
                        }
                        canDashRight = false;
                    } else {
                     //   moveLeft();
                    }
                } else if (right && !left) {
                    if (canDashRight == false) {
                        if (dashStartTime == 0) {
                            canDashRight = true;
                        } else if (Utils.secondsSince(dashStartTime) < Constants.DOUBLE_TAP_SPEED) {
                         //   startDash();
                        } else {
                            dashStartTime = 0;
                        }
                        canDashLeft = false;
                    } else {
                       // moveRight();
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
            // continueDash();
        }

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
            // startJump();
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
        }*/

    // refactor

    private void detectPlatformCollision(Array<Platform> platforms) {
        for (Platform platform : platforms) {
            if ((lastFramePosition.y + Constants.GIGAGAL_HEAD_RADIUS < platform.getBottom())
                    && position.y + Constants.GIGAGAL_HEAD_RADIUS >= platform.getBottom()
                    && position.x < platform.getRight()
                    && position.x > platform.getLeft()) {
                fall();
                position.y = lastFramePosition.y;
                velocity.y = -Constants.GRAVITY;
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

    //  -bump (detect contact with top, sides or bottom of platform and reset position to previous frame;
    //  velocity.y equal and opposite to downward velocity i.e. gravity if top, set canRicochet
    //  to true if jumping and side)
    // detect platform contact under feet (changes aerial state to grounded or falling)
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

    // detect contact with enemy (change aerial & ground state to recoil until grounded) */
    private void detectEnemyCollision(DelayedRemovalArray<Zoomba> zoombas) {
        Rectangle gigaGalBounds = new Rectangle(
                position.x - Constants.GIGAGAL_STANCE_WIDTH / 2,
                position.y - Constants.GIGAGAL_EYE_HEIGHT,
                Constants.GIGAGAL_STANCE_WIDTH,
                Constants.GIGAGAL_HEIGHT);

        for (Zoomba zoomba : zoombas) {
            Rectangle zoombaBounds = new Rectangle(
                    zoomba.getPosition().x - Constants.ZOOMBA_COLLISION_RADIUS,
                    zoomba.getPosition().y - Constants.ZOOMBA_COLLISION_RADIUS,
                    2 * Constants.ZOOMBA_COLLISION_RADIUS,
                    2 * Constants.ZOOMBA_COLLISION_RADIUS
            );

            if (gigaGalBounds.overlaps(zoombaBounds)) {
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

    private void detectPowerupCollision(DelayedRemovalArray<Powerup> powerups) {

        Rectangle gigaGalBounds = new Rectangle(
                position.x - Constants.GIGAGAL_STANCE_WIDTH / 2,
                position.y - Constants.GIGAGAL_EYE_HEIGHT,
                Constants.GIGAGAL_STANCE_WIDTH,
                Constants.GIGAGAL_HEIGHT);

        // Check powerups
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
    }

    private void stop() {
        if (groundState == GroundState.DASHING) {
            groundState = GroundState.LEANING;
        }
        velocity.x = 0;
    }

    // update velocity.y
    private void fall() {
        if (aerialState == AerialState.HOVERING) {
            hasHovered = true;
        }
        aerialState = AerialState.FALLING;
        velocity.y = -Constants.GRAVITY;
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
        hasHovered = false;
        canDashLeft = false;
        canDashRight = false;
        canDashLeft = false;
        canDashRight = false;
        jumpStartingPoint = new Vector2();
    }

    // bump sides disables; change ground state to striding at key detection;
    // change state to standing if single key hold is interrupted by release or keying of
    // other directional in order to reset momentum)
    private void enableStride(float lateralVelocity) {
        if (Gdx.input.isKeyPressed(Keys.A)) {
            facing = Direction.LEFT;
        } else if (Gdx.input.isKeyPressed(Keys.S)) {
            facing = Direction.RIGHT;
        }
        stride(lateralVelocity, facing);
    }

    private void stride(float lateralVelocity, Direction facing) {
        groundState = GroundState.STRIDING;
        if (aerialState == AerialState.GROUNDED) {
            if (groundState != GroundState.STRIDING) {
                walkStartTime = TimeUtils.nanoTime();
                groundState = GroundState.STRIDING;
            }
            walkTimeSeconds = Utils.secondsSince(walkStartTime);
        }
        if (facing == Direction.LEFT) {
            velocity.x = -Math.min(walkTimeSeconds * Constants.GIGAGAL_MAX_SPEED, Constants.GIGAGAL_MAX_SPEED);
        } else {
            velocity.x = Math.min(walkTimeSeconds * Constants.GIGAGAL_MAX_SPEED, Constants.GIGAGAL_MAX_SPEED);
        }
    }

    //  jump (detecting velocity.x prior to key press and adjusting jump height and distance accordingly;
    //  bump platform bottom disables; change state to falling after reaching jump peak; maintain
    //  lateral speed and direction)
    private void enableJump(float lateralVelocity, Direction facing) {
        if (Gdx.input.isKeyJustPressed(Keys.BACKSLASH)) {
            jump(lateralVelocity, facing);
        }
    }

    private void jump(float lateralVelocity, Direction facing) {
        jumpStartingPoint = new Vector2(position);
        aerialState = AerialState.JUMPING;
        jumpStartTime = TimeUtils.nanoTime();
        if (aerialState == AerialState.JUMPING) {
            if (Utils.secondsSince(jumpStartTime) < Constants.MAX_JUMP_DURATION) {
                velocity.y = Constants.JUMP_SPEED;
                if (Math.abs(velocity.x) >= Constants.GIGAGAL_MAX_SPEED / 2){
                    velocity.y *= Constants.RUNNING_JUMP_MULTIPLIER;
                }
            } else {
                fall();
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
            stop();
        }
    }

    //  hover (maintain forward momentum, velocity.y equal and opposite to downward velocity i.e. gravity
    //  until disabled manually or exceed max hover duration)
    private void enableHover(float lateralVelocity, Direction facing) {
        if (Gdx.input.isKeyJustPressed(Keys.BACKSLASH)) {
            hover(lateralVelocity, facing);
        }
    }

    private void hover(float lateralVelocity, Direction facing) {
        if (!hasHovered) {
            aerialState = AerialState.HOVERING;
            hoverStartTime = TimeUtils.nanoTime();
        }
        hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
        if (aerialState == AerialState.HOVERING) {
            if (hoverTimeSeconds < Constants.MAX_HOVER_DURATION) {
                velocity.y = 0;
            } else {
                fall();
            }
        }
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
                jump(Constants.GIGAGAL_MAX_SPEED, Direction.RIGHT);
            } else {
                jump(Constants.GIGAGAL_MAX_SPEED, Direction.LEFT);
            }
        }
    }

    private void ricochet(Direction facing) {

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

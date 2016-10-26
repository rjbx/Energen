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
import com.sun.org.apache.xpath.internal.operations.*;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.Direction;
import com.udacity.gamedev.gigagal.util.Enums.JumpState;
import com.udacity.gamedev.gigagal.util.Enums.WalkState;
import com.udacity.gamedev.gigagal.util.Utils;

import java.lang.String;

public class GigaGal {

    public final static String TAG = GigaGal.class.getName();
    public boolean jumpButtonPressed;
    public boolean leftButtonPressed;
    public boolean rightButtonPressed;
    private Level level;
    private Vector2 spawnLocation;
    private Vector2 position;
    private Vector2 lastFramePosition;
    private Vector2 velocity;
    private Direction facing;
    private JumpState jumpState;
    private WalkState walkState;
    private long walkStartTime;
    private float walkTimeSeconds;
    private long hoverStartTime;
    private float hoverTimeSeconds;
    private boolean hasHovered;
    private long jumpStartTime;
    private long chargeStartTime;
    private boolean isCharged;
    private boolean isDashing;
    private int ammo;
    private int lives;

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
        jumpState = Enums.JumpState.FALLING;
        facing = Direction.RIGHT;
        walkState = Enums.WalkState.NOT_WALKING;
        jumpStartTime = 0;
        hasHovered = false;
        isDashing = false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void update(float delta, Array<Platform> platforms) {

        lastFramePosition.set(position);
        if (jumpState != JumpState.HOVERING) {
            velocity.y -= Constants.GRAVITY;
        }
        position.mulAdd(velocity, delta);

        if (position.y < Constants.KILL_PLANE) {
            lives--;
            if (lives > -1) {
                respawn();
            }
        }

        // Land on/fall off platforms
        if (jumpState != Enums.JumpState.JUMPING) {
            if (jumpState != JumpState.RECOILING && jumpState != JumpState.HOVERING) {
                jumpState = Enums.JumpState.FALLING;
            }

            for (Platform platform : platforms) {
                if (isTouchingPlatform(platform)) {
                    if (jumpState == JumpState.RECOILING) {
                        walkStartTime = TimeUtils.nanoTime();
                        velocity.x = 0;
                    }
                    if (jumpStartTime != 0) {
                        walkStartTime += TimeUtils.nanoTime() - jumpStartTime;
                        jumpStartTime = 0;
                    }
                    jumpState = Enums.JumpState.GROUNDED;
                    hasHovered = false;
                    velocity.y = 0;
                    position.y = platform.top + Constants.GIGAGAL_EYE_HEIGHT;
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


        if (jumpState != JumpState.RECOILING) {
            boolean left = Gdx.input.isKeyPressed(Keys.A) || leftButtonPressed;
            boolean right = Gdx.input.isKeyPressed(Keys.S) || rightButtonPressed;

            if (left && !right && !isDashing) {
                moveLeft();
            } else if (right && !left && !isDashing) {
                moveRight();
            } else {
                walkTimeSeconds = 0;
                walkStartTime = TimeUtils.nanoTime();
                velocity.x /= 2;
                if (velocity.x >= -.00001f && velocity.x <= .00001f) {
                    walkState = Enums.WalkState.NOT_WALKING;
                    velocity.x = 0;
                }
            }
        }

        // Jump
        if (Gdx.input.isKeyJustPressed(Keys.BACKSLASH) || jumpButtonPressed) {
            switch (jumpState) {
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
                    Assets.instance.powerupAssets.powerup.getRegionWidth(),
                    Assets.instance.powerupAssets.powerup.getRegionHeight()
            );
            if (gigaGalBounds.overlaps(powerupBounds)) {
                ammo += Constants.POWERUP_AMMO;
                level.score += Constants.POWERUP_SCORE;
                powerups.removeIndex(i);
            }
        }
        powerups.end();

        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            shoot(Enums.BulletType.REGULAR);
            chargeStartTime = TimeUtils.nanoTime();
        }

        if (Gdx.input.isKeyPressed(Keys.ENTER)) {
            // Shoot
            if (Utils.secondsSince(chargeStartTime) > Constants.CHARGE_DURATION) {
                isCharged = true;
            }
        } else {
            if (isCharged) {
                shoot(Enums.BulletType.CHARGE);
                isCharged = false;
            }
        }
    }

    public void shoot(Enums.BulletType bulletType) {
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
            level.spawnBullet(bulletPosition, facing, bulletType);
        }
    }

    boolean isTouchingPlatform(Platform platform) {
        boolean leftFootIn = false;
        boolean rightFootIn = false;
        boolean straddle = false;

        if (lastFramePosition.y - Constants.GIGAGAL_EYE_HEIGHT >= platform.top &&
                position.y - Constants.GIGAGAL_EYE_HEIGHT < platform.top) {

            float leftFoot = position.x - Constants.GIGAGAL_STANCE_WIDTH / 2;
            float rightFoot = position.x + Constants.GIGAGAL_STANCE_WIDTH / 2;

            leftFootIn = (platform.left < leftFoot && platform.right > leftFoot);
            rightFootIn = (platform.left < rightFoot && platform.right > rightFoot);
            straddle = (platform.left > leftFoot && platform.right < rightFoot);
        }
        return leftFootIn || rightFootIn || straddle;
    }

    private void moveLeft() {

        facing = Direction.LEFT;
        if (jumpState == JumpState.GROUNDED) {
            if (walkState != Enums.WalkState.WALKING) {
                walkStartTime = TimeUtils.nanoTime();
                walkState = Enums.WalkState.WALKING;
            }
            walkTimeSeconds = Utils.secondsSince(walkStartTime);
        }
        velocity.x = -Math.min(walkTimeSeconds * Constants.GIGAGAL_MAX_SPEED, Constants.GIGAGAL_MAX_SPEED);
    }

    private void moveRight() {

        facing = Direction.RIGHT;
        if (jumpState == JumpState.GROUNDED) {
            if (walkState != Enums.WalkState.WALKING) {
                walkStartTime = TimeUtils.nanoTime();
                walkState = Enums.WalkState.WALKING;
            }
            walkTimeSeconds = Utils.secondsSince(walkStartTime);
        }
        velocity.x = Math.min(walkTimeSeconds * Constants.GIGAGAL_MAX_SPEED, Constants.GIGAGAL_MAX_SPEED);
    }

    private void startDash() {

    }

    private void startJump() {
        jumpState = Enums.JumpState.JUMPING;
        jumpStartTime = TimeUtils.nanoTime();
        continueJump();
    }

    private void continueJump() {
        if (jumpState == Enums.JumpState.JUMPING) {
            if (Utils.secondsSince(jumpStartTime) < Constants.MAX_JUMP_DURATION) {
                velocity.y = Constants.JUMP_SPEED;
            } else {
                endJump();
            }
        }
    }

    private void endJump() {
        if (jumpState == Enums.JumpState.JUMPING) {
            jumpState = Enums.JumpState.FALLING;
        }
    }

    private void startHover() {
        if (!hasHovered) {
            jumpState = JumpState.HOVERING;
            hoverStartTime = TimeUtils.nanoTime();
        }
        continueHover();
    }

    private void continueHover() {
        hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
        if (jumpState == JumpState.HOVERING) {
            if (hoverTimeSeconds < Constants.MAX_HOVER_DURATION) {
                velocity.y = 0;

            } else {
                endHover();
            }
        }
    }

    private void endHover() {
        if (jumpState == JumpState.HOVERING) {
            jumpState = JumpState.FALLING;
            hasHovered = true;
        }
    }

    private void recoilFromHit(Direction direction) {
        walkTimeSeconds = 0;
        jumpState = JumpState.RECOILING;
        velocity.y = Constants.KNOCKBACK_VELOCITY.y;

        if (direction == Direction.LEFT) {
            velocity.x = -Constants.KNOCKBACK_VELOCITY.x;
        } else {
            velocity.x = Constants.KNOCKBACK_VELOCITY.x;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion region = Assets.instance.gigaGalAssets.standingRight;
        if (facing == Direction.RIGHT && jumpState != Enums.JumpState.GROUNDED) {
            if (jumpState == JumpState.HOVERING) {

                hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
                region = Assets.instance.gigaGalAssets.hoverRightAnimation.getKeyFrame(hoverTimeSeconds);
            } else {
                region = Assets.instance.gigaGalAssets.jumpingRight;
            }
        } else if (facing == Direction.RIGHT && walkState == Enums.WalkState.NOT_WALKING) {
            region = Assets.instance.gigaGalAssets.standingRight;
        } else if (facing == Direction.RIGHT && walkState == Enums.WalkState.WALKING) {

            region = Assets.instance.gigaGalAssets.walkingRightAnimation.getKeyFrame(Math.min(walkTimeSeconds * walkTimeSeconds, walkTimeSeconds));
        } else if (facing == Direction.LEFT && jumpState != Enums.JumpState.GROUNDED) {
            if (jumpState == JumpState.HOVERING) {

                hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
                region = Assets.instance.gigaGalAssets.hoverLeftAnimation.getKeyFrame(hoverTimeSeconds);
            } else {
                region = Assets.instance.gigaGalAssets.jumpingLeft;
            }
        } else if (facing == Direction.LEFT && walkState == Enums.WalkState.NOT_WALKING) {
            region = Assets.instance.gigaGalAssets.standingLeft;
        } else if (facing == Direction.LEFT && walkState == Enums.WalkState.WALKING) {

            region = Assets.instance.gigaGalAssets.walkingLeftAnimation.getKeyFrame(Math.min(walkTimeSeconds * walkTimeSeconds, walkTimeSeconds));
        }

        Utils.drawTextureRegion(batch, region, position, Constants.GIGAGAL_EYE_POSITION);
    }
}

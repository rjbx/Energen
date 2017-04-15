package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;
import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.List;

public class Rollen implements DestructibleHazard {

    // fields
    private Level level;
    private Vector2 position;
    private Vector2 previousFramePosition; // class-level instantiation
    private Enums.Direction xDirection;
    private Enums.WeaponType type;
    private Vector2 velocity; // class-level instantiation
    private long startTime;
    private float health;
    private float speedAtChangeXDirection;
    private long rollStartTime;
    private float rollTimeSeconds;
    private List<Ground> grounds;
    private float radius;
    private Animation animation;

    // ctor
    public Rollen(Level level, Vector2 position, Enums.WeaponType type) {
        this.level = level;
        this.type = type;
        this.position = position;
        previousFramePosition = new Vector2();
        velocity = new Vector2(0, 0);
        grounds = level.getGrounds();
        radius = getWidth() / 2;
        health = Constants.ROLLEN_MAX_HEALTH;
        xDirection = null;
        speedAtChangeXDirection = 0;
        rollStartTime = 0;
        rollTimeSeconds = 0;
        switch (type) {
            case PLASMA:
                animation = Assets.getInstance().getRollenAssets().chargedRollen;
                break;
            case GAS:
                animation = Assets.getInstance().getRollenAssets().fieryRollen;
                break;
            case SOLID:
                animation = Assets.getInstance().getRollenAssets().sharpRollen;
                break;
            case ORE:
                animation = Assets.getInstance().getRollenAssets().whirlingRollen;
                break;
            case LIQUID:
                animation = Assets.getInstance().getRollenAssets().gushingRollen;
                break;
            default:
                animation = Assets.getInstance().getRollenAssets().whirlingRollen;
        }
    }

    public void update(float delta) {
        previousFramePosition.set(position);
        position.mulAdd(velocity, delta);

        Viewport viewport = level.getViewport();
        Vector2 worldSpan = new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight());
        Vector3 camera = new Vector3(viewport.getCamera().position);
        Vector2 activationDistance = new Vector2(worldSpan.x / 2, worldSpan.y / 2);

        if (xDirection != null) {
            if (rollStartTime == 0) {
                speedAtChangeXDirection = velocity.x;
                rollStartTime = TimeUtils.nanoTime();
            }
            rollTimeSeconds = Helpers.secondsSince(rollStartTime);
            velocity.x = speedAtChangeXDirection + Helpers.absoluteToDirectionalValue(Math.min(Constants.ROLLEN_MOVEMENT_SPEED * rollTimeSeconds, Constants.ROLLEN_MOVEMENT_SPEED), xDirection, Enums.Orientation.X);
        }

        boolean touchingSide = false;
        boolean touchingTop = false;
        for (Ground ground : grounds) {
            if (ground instanceof SolidGround) {
                if (Helpers.overlapsBetweenFourSides(position, radius, radius, ground.getLeft(), ground.getRight(), ground.getBottom(), ground.getTop())) {
                    if (!(Helpers.overlapsBetweenTwoSides(previousFramePosition.x, radius, ground.getLeft(), ground.getRight()))) {
                        touchingSide = true;
                    }
                    if (!(Helpers.overlapsBetweenTwoSides(previousFramePosition.y, radius, ground.getBottom(), ground.getTop()))) {
                        touchingTop = true;
                    }
                }
            }
        }

        if (touchingSide) {
            xDirection = null;
            startTime = 0;
            velocity.x = 0;
            position.x = previousFramePosition.x;
        }
        if (touchingTop) {
            velocity.y = 0;
            position.y = previousFramePosition.y;
            if ((position.x < camera.x - activationDistance.x)
            || (position.x > camera.x + activationDistance.x)) {
                xDirection = null;
                startTime = 0;
                velocity.x = 0;
            } else if ((position.x > camera.x - activationDistance.x) && (position.x < camera.x)) {
                xDirection = Enums.Direction.RIGHT;
            } else if ((position.x > camera.x) && (position.x < camera.x + activationDistance.x)) {
                xDirection = Enums.Direction.LEFT;
            }
        } else {
            velocity.y -= Constants.GRAVITY;
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (xDirection == Enums.Direction.RIGHT) {
            animation.setPlayMode(Animation.PlayMode.REVERSED);
        } else {
            animation.setPlayMode(Animation.PlayMode.NORMAL);
        }
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(rollTimeSeconds, true), position, Constants.ROLLEN_CENTER, Constants.ROLLEN_TEXTURE_SCALE);
    }

    @Override public Vector2 getPosition() { return position; }
    @Override public final float getHealth() { return health; }
    @Override public final float getWidth() { return Constants.ROLLEN_COLLISION_WIDTH; }
    @Override public final float getHeight() { return Constants.ROLLEN_COLLISION_HEIGHT; }
    @Override public final float getLeft() { return position.x - Constants.ROLLEN_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.ROLLEN_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.ROLLEN_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.ROLLEN_CENTER.y; }
    @Override public final float getShotRadius() { return Constants.ROLLEN_SHOT_RADIUS; }
    @Override public final int getHitScore() { return Constants.ROLLEN_HIT_SCORE; }
    @Override public final int getKillScore() { return Constants.ROLLEN_KILL_SCORE; }
    @Override public final int getDamage() { return Constants.ROLLEN_STANDARD_DAMAGE; }
    @Override public final Vector2 getKnockback() { return Constants.ROLLEN_KNOCKBACK; }
    @Override public final void setHealth( float health ) { this.health = health; }
    @Override public final Enums.WeaponType getType() { return type; }
    public final long getStartTime() { return startTime; }
}
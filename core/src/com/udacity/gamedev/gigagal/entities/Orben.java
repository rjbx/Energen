package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

public class Orben implements DestructibleHazard {

    // fields
    private Level level;
    private Vector2 position;
    private Enums.Direction xDirection;
    private Enums.Direction yDirection;
    private Enums.WeaponType type;
    private Vector2 velocity;
    private long startTime;
    private float health;
    private boolean active;

    // ctor
    public Orben(Level level, Vector2 position, Enums.WeaponType type) {
        this.level = level;
        this.type = type;
        this.position = position;
        xDirection = null;
        yDirection = null;
        velocity = new Vector2(0, 0);
        health = Constants.ORBEN_MAX_HEALTH;
    }

    public void update(float delta) {
        position.x += velocity.x;
        position.y += velocity.y;

        Viewport viewport = level.getViewport();
        Vector2 worldSpan = new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight());
        Vector3 camera = new Vector3(viewport.getCamera().position);
        Vector2 activationDistance = new Vector2(worldSpan.x / 4, worldSpan.y / 4);

        if (xDirection != null) {
            switch (xDirection) {
                case LEFT:
                    velocity.x = -Constants.ORBEN_MOVEMENT_SPEED * delta;
                    break;
                case RIGHT:
                    velocity.x = Constants.ORBEN_MOVEMENT_SPEED * delta;
                    break;
            }
        } else {
            velocity.x = 0;
        }
        if ((position.x < camera.x - activationDistance.x)
        || (position.x > camera.x + activationDistance.x)) {
            xDirection = null;
        } else if ((position.x > camera.x - activationDistance.x) && (position.x < camera.x)) {
            xDirection = Enums.Direction.RIGHT;
        } else if ((position.x > camera.x) && (position.x < camera.x + activationDistance.x)) {
            xDirection = Enums.Direction.LEFT;
        }

        if (yDirection != null) {
            switch (yDirection) {
                case DOWN:
                    velocity.y = -Constants.ORBEN_MOVEMENT_SPEED * delta;
                    break;
                case UP:
                    velocity.y = Constants.ORBEN_MOVEMENT_SPEED * delta;
                    break;
            }
        } else {
            velocity.y = 0;
        }
        if ((position.y < camera.y - activationDistance.y)
                || (position.y > camera.y + activationDistance.y)) {
            yDirection = null;
        } else if ((position.y > camera.y - activationDistance.y) && (position.y < camera.y)) {
            yDirection = Enums.Direction.UP;
        } else if ((position.y > camera.y) && (position.y < camera.y + activationDistance.y)) {
            yDirection = Enums.Direction.DOWN;
        }

        if (xDirection != null && yDirection != null) {
            active = true;
        } else {
            startTime = TimeUtils.nanoTime();
            active = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region;
        if (xDirection == null || yDirection == null) {
            region = Assets.getInstance().getOrbenAssets().dormantOrben;
        } else {
            switch (type) {
                case ELECTRIC:
                    region = Assets.getInstance().getOrbenAssets().chargedOrben.getKeyFrame(elapsedTime, true);
                    break;
                case FIRE:
                    region = Assets.getInstance().getOrbenAssets().fieryOrben.getKeyFrame(elapsedTime, true);
                    break;
                case METAL:
                    region = Assets.getInstance().getOrbenAssets().sharpOrben.getKeyFrame(elapsedTime, true);
                    break;
                case RUBBER:
                    region = Assets.getInstance().getOrbenAssets().whirlingOrben.getKeyFrame(elapsedTime, true);
                    break;
                case WATER:
                    region = Assets.getInstance().getOrbenAssets().gushingOrben.getKeyFrame(elapsedTime, true);
                    break;
                default:
                    region = null;
            }
        }
        Utils.drawTextureRegion(batch, region, position, Constants.ORBEN_CENTER, Constants.ORBEN_TEXTURE_SCALE);
    }

    @Override public Vector2 getPosition() { return position; }
    @Override public final float getHealth() { return health; }
    @Override public final float getWidth() { return Constants.ORBEN_COLLISION_WIDTH; }
    @Override public final float getHeight() { return Constants.ORBEN_COLLISION_HEIGHT; }
    @Override public final float getLeft() { return position.x - Constants.ORBEN_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.ORBEN_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.ORBEN_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.ORBEN_CENTER.y; }
    @Override public final float getShotRadius() { return Constants.ORBEN_SHOT_RADIUS; }
    @Override public final int getHitScore() { return Constants.ORBEN_HIT_SCORE; }
    @Override public final int getKillScore() { return Constants.ORBEN_KILL_SCORE; }
    @Override public final int getDamage() { return Constants.ORBEN_STANDARD_DAMAGE; }
    @Override public final Vector2 getKnockback() { return Constants.ORBEN_KNOCKBACK; }
    @Override public final void setHealth( float health ) { this.health = health; }
    @Override public Enums.WeaponType getType() { return type; }
    public final long getStartTime() { return startTime; }
    public final boolean isActive() { return active; }
}

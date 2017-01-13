package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

public class Orben implements DestructibleHazard {

    // fields
    private Level level;
    private Vector2 position;
    private Enums.Direction direction;
    private Enums.WeaponType type;
    private Vector2 velocity;
    private long startTime;
    private float bobOffset;
    private int health;
    private boolean active;

    // ctor
    public Orben(Level level, Vector2 position, Enums.WeaponType type) {
        this.level = level;
        this.type = type;
        this.position = position;
        direction = null;
        velocity = new Vector2(0, 0);
        health = Constants.SWOOPA_MAX_HEALTH;
    }

    public void update(float delta) {
        Viewport viewport = level.getViewport();
        Vector2 worldSpan = new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight());
        Vector3 camera = new Vector3(viewport.getCamera().position);
        float activationDistance = viewport.getScreenWidth() / 8;

        if (direction != null) {
            active = true;
            switch (direction) {
                case LEFT:
                    velocity.x = -Constants.ORBEN_MOVEMENT_SPEED * delta;
                    break;
                case RIGHT:
                    velocity.x = Constants.ORBEN_MOVEMENT_SPEED * delta;
                    break;
            }
        } else {
            velocity.x = 0;
            startTime = TimeUtils.nanoTime();
            active = false;
        }

        position.x += velocity.x;



        if ((position.x < camera.x - activationDistance)
        || (position.x > camera.x + activationDistance)) {
            direction = null;
        } else if ((position.x > camera.x - activationDistance) && (position.x < camera.x)) {
            direction = Enums.Direction.RIGHT;
        } else if ((position.x > camera.x) && (position.x < camera.x + activationDistance)) {
            direction = Enums.Direction.LEFT;
        }

        position.y = worldSpan.y + Constants.ORBEN_CENTER.y + Constants.ZOOMBA_BOB_AMPLITUDE;
    }

    @Override
    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region;
        if (velocity.x == 0) {
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
        Utils.drawTextureRegion(batch, region, position, Constants.ORBEN_CENTER, 1.5f);
    }

    @Override public Vector2 getPosition() { return position; }
    @Override public final int getHealth() { return health; }
    @Override public final float getWidth() { return Constants.SWOOPA_COLLISION_WIDTH; }
    @Override public final float getHeight() { return Constants.SWOOPA_COLLISION_HEIGHT; }
    @Override public final float getLeft() { return position.x - Constants.SWOOPA_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.SWOOPA_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.SWOOPA_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.SWOOPA_CENTER.y; }
    @Override public final float getShotRadius() { return Constants.SWOOPA_SHOT_RADIUS; }
    @Override public final int getHitScore() { return Constants.SWOOPA_HIT_SCORE; }
    @Override public final int getKillScore() { return Constants.SWOOPA_KILL_SCORE; }
    @Override public final int getDamage() { return Constants.SWOOPA_STANDARD_DAMAGE; }
    @Override public final Vector2 getKnockback() { return Constants.SWOOPA_KNOCKBACK; }
    @Override public final void setHealth( int health ) { this.health = health; }
    @Override public Enums.WeaponType getType() { return type; }
    public final long getStartTime() { return startTime; }
    public final boolean isActive() { return active; }
}

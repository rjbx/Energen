package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

public class Orben implements DestructibleHazard {

    // fields
    private final Platform platform;
    private Level level;
    private Enums.WeaponType type;
    private Vector2 velocity;
    private Vector2 position;
    private final long startTime;
    private float bobOffset;
    private int health;
    private long descentStartTime;

    // ctor
    public Orben(Platform platform, Level level, Enums.WeaponType type) {
        this.platform = platform;
        this.level = level;
        this.type = type;
        position = new Vector2(platform.getRight() + Constants.SWOOPA_CENTER.x * 3, platform.getTop() + Constants.SWOOPA_CENTER.y * 3);
        velocity = new Vector2(0, -Constants.SWOOPA_MOVEMENT_SPEED * 2);
        startTime = TimeUtils.nanoTime();
        health = Constants.SWOOPA_MAX_HEALTH;
    }

    public void update(float delta) {
        bobOffset = MathUtils.random() * 100;
        Vector2 worldSpan = new Vector2(level.getViewport().getWorldWidth(), level.getViewport().getWorldHeight());
        Vector3 camera = new Vector3(level.getViewport().getCamera().position);
        // while the swoopa is witin a screens' width from the screen center on either side, permit movement
        if (position.x < (camera.x + worldSpan.x)
                && position.x > (camera.x - worldSpan.x)
                && Utils.secondsSince(descentStartTime) > 1.5f) {
            if (position.y > (platform.getTop() + Constants.SWOOPA_COLLISION_HEIGHT)) {
                velocity.x = -Constants.SWOOPA_MOVEMENT_SPEED;
                velocity.y /= 1.005f;
            } else {
                velocity.x *= 1.05f;
                velocity.y /= 1.1f;
            }
            velocity.x = Math.max(velocity.x, -Constants.SWOOPA_MOVEMENT_SPEED * 4);
            position = position.mulAdd(velocity, delta);
        }


        // when the swoopa progresses past the center screen position with a margin of one screen's width, reset x and y position
        if (position.x < (camera.x - worldSpan.x)) {
            descentStartTime = TimeUtils.nanoTime();
            position.x = platform.getRight() + (Constants.WORLD_SIZE) / 1.5f;
            position.y = (platform.getTop() + (Constants.WORLD_SIZE));
            velocity.y = -Constants.SWOOPA_MOVEMENT_SPEED * 2;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        final TextureRegion region;
        switch (type) {

        }
        Utils.drawTextureRegion(batch, region, position, Constants.ORBEN_CENTER);
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
    @Override public Enums.WeaponType getType() { return null;
    public final long getStartTime() { return startTime; }
}

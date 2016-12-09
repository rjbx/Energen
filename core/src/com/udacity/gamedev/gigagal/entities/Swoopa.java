package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

// mutable
public class Swoopa extends Destructible {

    // fields
    private final long startTime;
    private final float bobOffset;
    private final Platform platform;
    private Level level;
    private Vector2 position;
    private int health;

    // ctor
    public Swoopa(Platform platform, Level level) {
        this.platform = platform;
        this.level = level;
        position = new Vector2(platform.getLeft(), platform.getTop() + Constants.SWOOPA_CENTER.y);
        startTime = TimeUtils.nanoTime();
        health = Constants.SWOOPA_MAX_HEALTH;
        bobOffset = MathUtils.random();
    }

    public void update(float delta) {
        position.x -= Constants.SWOOPA_MOVEMENT_SPEED * delta;

        if (position.x < platform.getLeft()) {
            position.x = platform.getLeft();
        } else if (position.x > platform.getRight()) {
            position.x = platform.getRight();
        }

        final float elapsedTime = Utils.secondsSince(startTime);
        final float bobMultiplier = 1 + MathUtils.sin(MathUtils.PI2 * (bobOffset + elapsedTime / Constants.SWOOPA_BOB_PERIOD));
        position.y = platform.getTop() + Constants.SWOOPA_CENTER.y + Constants.SWOOPA_BOB_AMPLITUDE * bobMultiplier;
    }

    public void render(SpriteBatch batch) {
        final TextureRegion region = Assets.getInstance().getSwoopaAssets().swoopa;
        Utils.drawTextureRegion(batch, region, position, Constants.SWOOPA_CENTER);
    }

    public final long getStartTime() { return startTime; }
    public Vector2 getPosition() { return position; }
    public final int getHealth() { return health; }
    public final float getWidth() { return Constants.SWOOPA_COLLISION_WIDTH; }
    public final float getHeight() { return Constants.SWOOPA_COLLISION_HEIGHT; }
    public final float getLeft() { return position.x - Constants.SWOOPA_CENTER.x; }
    public final float getRight() { return position.x + Constants.SWOOPA_CENTER.x; }
    public final float getTop() { return position.y + Constants.SWOOPA_CENTER.y; }
    public final float getBottom() { return position.y - Constants.SWOOPA_CENTER.y; }
    public final float getShotRadius() { return Constants.SWOOPA_SHOT_RADIUS; }
    public final int getHitScore() { return Constants.SWOOPA_HIT_SCORE; }
    public final int getKillScore() { return Constants.SWOOPA_KILL_SCORE; }
    public final int getDamage() { return Constants.SWOOPA_STANDARD_DAMAGE; }
    public int getMountDamage() { return Constants.SWOOPA_STANDARD_DAMAGE; }
    public final Vector2 getKnockback() { return Constants.SWOOPA_KNOCKBACK; }
    public Vector2 getMountKnockback() { return Constants.SWOOPA_KNOCKBACK; }
    public final Class getSubclass() { return this.getClass(); }
    public final void setHealth( int health ) { this.health = health; }
}

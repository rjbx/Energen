package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.Direction;
import com.udacity.gamedev.gigagal.util.Utils;

// mutable
public class Zoomba implements DestructibleHazard {

    // fields
    private final Enums.WeaponType type;
    private final long startTime;
    private final float bobOffset;
    private final float bobNadir;
    private float range;
    private Vector2 position;
    private final Vector2 startingPosition;
    private float health;
    private Direction direction;

    // ctor
    public Zoomba(Vector2 position, Enums.WeaponType type) {
        this.position = position;
        this.startingPosition = new Vector2(position);
        bobNadir = position.y;
        this.type = type;
        direction = Direction.RIGHT;
        startTime = TimeUtils.nanoTime();
        health = Constants.ZOOMBA_MAX_HEALTH;
        bobOffset = MathUtils.random();
        range = Constants.ZOOMBA_RANGE;
    }

    public void update(float delta) {
        switch (direction) {
            case LEFT:
                position.x -= Constants.ZOOMBA_MOVEMENT_SPEED * delta;
                break;
            case RIGHT:
                position.x += Constants.ZOOMBA_MOVEMENT_SPEED * delta;
        }

        if (position.x < startingPosition.x - (range / 2)) {
            position.x = startingPosition.x - (range / 2);
            direction = Direction.RIGHT;
        } else if (position.x > startingPosition.x + (range / 2)) {
            position.x = startingPosition.x + (range / 2);
            direction = Direction.LEFT;
        }

        final float elapsedTime = Utils.secondsSince(startTime);
        final float bobMultiplier = 1 + MathUtils.sin(MathUtils.PI2 * (bobOffset + elapsedTime / Constants.ZOOMBA_BOB_PERIOD));
        position.y = bobNadir + Constants.ZOOMBA_CENTER.y + Constants.ZOOMBA_BOB_AMPLITUDE * bobMultiplier;
    }

    @Override
    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region;
        switch (type) {
            case PLASMA:
                region = Assets.getInstance().getZoombaAssets().chargedZoomba.getKeyFrame(elapsedTime, true);
                break;
            case GAS:
                region = Assets.getInstance().getZoombaAssets().fieryZoomba.getKeyFrame(elapsedTime, true);
                break;
            case SOLID:
                region = Assets.getInstance().getZoombaAssets().sharpZoomba.getKeyFrame(elapsedTime, true);
                break;
            case POLYMER:
                region = Assets.getInstance().getZoombaAssets().whirlingZoomba.getKeyFrame(elapsedTime, true);
                break;
            case LIQUID:
                region = Assets.getInstance().getZoombaAssets().gushingZoomba.getKeyFrame(elapsedTime, true);
                break;
            default:
                region = Assets.getInstance().getZoombaAssets().zoomba;
        }
        Utils.drawTextureRegion(batch, region, position, Constants.ZOOMBA_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHealth() { return health; }
    @Override public final float getWidth() { return Constants.ZOOMBA_COLLISION_WIDTH; }
    @Override public final float getHeight() { return Constants.ZOOMBA_COLLISION_HEIGHT; }
    @Override public final float getLeft() { return position.x - Constants.ZOOMBA_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.ZOOMBA_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.ZOOMBA_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.ZOOMBA_CENTER.y; }
    @Override public final float getShotRadius() { return Constants.ZOOMBA_SHOT_RADIUS; }
    @Override public final int getHitScore() { return Constants.ZOOMBA_HIT_SCORE; }
    @Override public final int getKillScore() { return Constants.ZOOMBA_KILL_SCORE; }
    @Override public final int getDamage() { return Constants.ZOOMBA_STANDARD_DAMAGE; }
    @Override public Enums.WeaponType getType() { return null; }
    @Override public final Vector2 getKnockback() { return Constants.ZOOMBA_KNOCKBACK; }
    public int getMountDamage() { return Constants.ZOOMBA_STANDARD_DAMAGE; }
    public Vector2 getMountKnockback() { return Constants.ZOOMBA_KNOCKBACK; }
    public final Direction getDirection() { return direction; }
    public final long getStartTime() { return startTime; }
    public final float getRange() { return range; }

    @Override public final void setHealth( float health ) { this.health = health; }
    public final void setRange(float range) { this.range = range; }
}

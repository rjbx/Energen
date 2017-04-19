package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.Direction;
import com.udacity.gamedev.gigagal.util.Helpers;

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
    private Animation animation;

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
        switch (type) {
            case PLASMA:
                animation = Assets.getInstance().getZoombaAssets().chargedZoomba;
                break;
            case GAS:
                animation = Assets.getInstance().getZoombaAssets().fieryZoomba;
                break;
            case SOLID:
                animation = Assets.getInstance().getZoombaAssets().sharpZoomba;
                break;
            case ORE:
                animation = Assets.getInstance().getZoombaAssets().whirlingZoomba;
                break;
            case LIQUID:
                animation = Assets.getInstance().getZoombaAssets().gushingZoomba;
                break;
            default:
                animation = Assets.getInstance().getZoombaAssets().sharpZoomba;
        }
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

        final float elapsedTime = Helpers.secondsSince(startTime);
        final float bobMultiplier = 1 + MathUtils.sin(MathUtils.PI2 * (bobOffset + elapsedTime / Constants.ZOOMBA_BOB_PERIOD));
        position.y = bobNadir + Constants.ZOOMBA_CENTER.y + Constants.ZOOMBA_BOB_AMPLITUDE * bobMultiplier;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime), true), position, Constants.ZOOMBA_CENTER);
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
    @Override public Enums.WeaponType getType() { return type; }
    @Override public final void setHealth( float health ) { this.health = health; }
    @Override public final Vector2 getKnockback() { return Constants.ZOOMBA_KNOCKBACK; }
    public int getMountDamage() { return Constants.ZOOMBA_STANDARD_DAMAGE; }
    public Vector2 getMountKnockback() { return Constants.ZOOMBA_KNOCKBACK; }
    public final Direction getDirection() { return direction; }
    public final long getStartTime() { return startTime; }
    public final float getRange() { return range; }

    public final void setRange(float range) { this.range = range; }
}

package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;

public class Treadmill extends Ground implements Nonstatic, Propelling, Convertible {

    // fields
    public final static String TAG = Treadmill.class.getName();

    private Vector2 position;
    private Vector2 scale;
    private Vector2 adjustedCenter;
    private Enums.Direction direction;
    private long startTime;
    private Animation<TextureRegion> animation;
    private boolean tripped;

    // ctor
    public Treadmill(Vector2 position, Vector2 scale, Vector2 adjustedCenter, Enums.Direction direction) {
        this.position = position;
        this.scale = scale;
        this.adjustedCenter = adjustedCenter;
        this.direction = direction;
        startTime = TimeUtils.nanoTime();
        tripped = false;
        animation = AssetManager.getInstance().getGroundAssets().treadmillRight;
    }

    @Override
    public Treadmill safeClone() {
        Treadmill clone = new Treadmill(position, scale, adjustedCenter, direction);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void update(float delta) {
        if (direction == Enums.Direction.RIGHT) {
            animation = AssetManager.getInstance().getGroundAssets().treadmillRight;
        } else {
            animation = AssetManager.getInstance().getGroundAssets().treadmillLeft;
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime), true), position, adjustedCenter, scale);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.TREADMILL_CENTER.y * 2 * scale.y; }
    @Override public final float getWidth() { return Constants.TREADMILL_CENTER.x * 2 * scale.x; }
    @Override public final float getLeft() { return position.x - Constants.TREADMILL_CENTER.x * scale.x; }
    @Override public final float getRight() { return position.x + Constants.TREADMILL_CENTER.x * scale.x; }
    @Override public final float getTop() { return position.y + Constants.TREADMILL_CENTER.y * scale.y; }
    @Override public final float getBottom() { return position.y - Constants.TREADMILL_CENTER.y * scale.y; }
    @Override public final boolean isDense() { return true; }
    @Override public final Enums.Direction getDirectionX() { return direction; }
    @Override public void convert() { tripped = !tripped; direction = Helpers.getOppositeDirection(direction); }
    @Override public boolean isConverted() { return tripped; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_HIGH;
    }
}
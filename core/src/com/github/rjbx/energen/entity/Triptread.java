package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;

public class Triptread extends Ground implements Trippable, Convertible, Propelling {

    // fields
    public final static String TAG = Triptread.class.getName();

    private Vector2 position;
    private Vector2 velocity;
    private boolean converted;
    private Rectangle bounds;
    private long startTime;
    private boolean state;
    private Enums.Direction direction;
    private boolean previousState;
    private int adjustments;
    private Animation<TextureRegion> animation;

    // ctor
    public Triptread(Vector2 position, Rectangle bounds, boolean state, Enums.Direction direction) {
        this.position = position;
        this.bounds = bounds;
        this.state = state;
        this.direction = direction;
        startTime = TimeUtils.nanoTime();
        converted = false;
        adjustments = 0;
        animation = AssetManager.getInstance().getGroundAssets().treadmillRight;
    }

    @Override
    public Triptread safeClone() {
        Triptread clone = new Triptread(position, bounds, state, direction);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void update(float delta) {
        converted = false;
        previousState = state;
        if (state) {
            if (direction == Enums.Direction.LEFT) {
                animation = AssetManager.getInstance().getGroundAssets().triptreadLeftOn;
            } else {
                animation = AssetManager.getInstance().getGroundAssets().triptreadRightOn;
            }
        } else {
            if (direction == Enums.Direction.LEFT) {
                animation = AssetManager.getInstance().getGroundAssets().triptreadLeftOff;
            } else {
                animation = AssetManager.getInstance().getGroundAssets().triptreadRightOff;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime)), position, Constants.TRIPTREAD_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.TRIPTREAD_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.TRIPTREAD_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.TRIPTREAD_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.TRIPTREAD_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.TRIPTREAD_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.TRIPTREAD_CENTER.y; }
    @Override public final boolean isDense() { return true; }
    public final long getStartTime() { return startTime; }
    public Rectangle getConvertBounds() { return bounds; }
    @Override public boolean isActive() { return state; }
    @Override public void setState(boolean state) { this.state = state; converted = true; }
    @Override public void convert() { direction = Helpers.getOppositeDirection(direction); }
    @Override public boolean isConverted() { return converted; }
    @Override public void addCamAdjustment() { adjustments++; }
    @Override public boolean maxAdjustmentsReached() { return adjustments > 3; }
    @Override public boolean tripped() { return previousState != state; }
    @Override public final Enums.Direction getDirectionX() { return direction; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_HIGH;
    }
}
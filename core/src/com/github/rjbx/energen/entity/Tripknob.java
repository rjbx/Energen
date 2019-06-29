package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.app.LevelAssets;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Helpers;

// class name avoids confusion with existing button assets and constants
public class Tripknob extends Ground implements Trippable, Convertible, Strikeable, Impermeable, Groundable {

    // fields
    public final static String TAG = Tripknob.class.getName();

    private Vector2 position;
    private boolean converted;
    private Rectangle bounds;
    private long startTime;
    private int rotation;
    private Vector2 offset;
    private boolean state;
    private boolean previousState;
    private int adjustments;

    // ctor
    public Tripknob(Vector2 position, Rectangle bounds, float rotation, boolean state) {
        this.position = position;
        this.bounds = bounds;
        this.rotation = (int) rotation;
        switch (this.rotation) {
            case 90:
                offset = new Vector2(-Constants.TRIPKNOB_CENTER.x, Constants.TRIPKNOB_CENTER.y);
                break;
            case 180:
                offset = new Vector2(-Constants.TRIPKNOB_CENTER.x, -Constants.TRIPKNOB_CENTER.y);
                break;
            case 270:
                offset = new Vector2(Constants.TRIPKNOB_CENTER.x, -Constants.TRIPKNOB_CENTER.y);
                break;
            default:
                offset = Constants.TRIPKNOB_CENTER;
        }
        startTime = 0;
        converted = false;
        this.state = state;
        previousState = state;
        adjustments++;
    }

    @Override
    public Tripknob safeClone() {
        Tripknob clone = new Tripknob(position, bounds, rotation, state);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void update(float delta) {
        converted = false;
        previousState = state;
        for (Ground ground : LevelAssets.getClonedGrounds()) {
            if (Helpers.overlapsPhysicalObject(this, ground)) {
                if (ground instanceof Brick) {
                    resetStartTime();
                    setState(!isActive());
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (state) {
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
            Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().tripknobOn.getKeyFrame(Helpers.secondsSince(startTime), false), position, offset, 1, rotation);
        } else {
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
            Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().tripknobOff.getKeyFrame(Helpers.secondsSince(startTime), false), position, offset, 1, rotation);
        }
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.TRIPKNOB_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.TRIPKNOB_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.TRIPKNOB_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.TRIPKNOB_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.TRIPKNOB_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.TRIPKNOB_CENTER.y; }
    @Override public final boolean isDense() { return true; }
    public final long getStartTime() { return startTime; }
    public final void resetStartTime() { this.startTime = 0; }
    public Rectangle getConvertBounds() { return bounds; }
    @Override public boolean isActive() { return state; }
    @Override public void setState(boolean state) { this.state = state; converted = true; }
    @Override public void convert() { state = !state; converted = true; }
    @Override public boolean isConverted() { return converted; }
    @Override public void addCamAdjustment() { adjustments++; }
    @Override public boolean maxAdjustmentsReached() { return adjustments > 3; }
    @Override public boolean tripped() { return previousState != state; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}

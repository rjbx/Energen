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

public class Tripspring extends Ground implements Trippable, Compressible, Reboundable, Impermeable, Groundable {

    // fields
    public final static String TAG = Tripspring.class.getName();

    private Ground topGround;
    private Vector2 position;
    private Rectangle bounds;
    private long startTime;
    private boolean state;
    private boolean underneatheGround;
    private int adjustments;
    private boolean previousState;
    private boolean converted;

    // ctor
    public Tripspring(Vector2 position, Rectangle bounds, boolean state) {
        this.position = position;
        startTime = 0;
        underneatheGround = false;
        startTime = 0;
        converted = false;
        this.bounds = bounds;
        this.state = state;
        previousState = state;
        adjustments++;
    }

    @Override
    public Tripspring safeClone() {
        Tripspring clone = new Tripspring(position, bounds, state);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void update(float delta) {
        converted = false;
        previousState = state;
        for (Ground ground : LevelAssets.getClonedGrounds()) {
            if (Helpers.overlapsPhysicalObject(this, ground)) {
                if (Helpers.betweenTwoValues(getTop(), ground.getBottom() - 1, ground.getBottom() + 1)) {
                    if (!state) {
                        resetStartTime();
                    }
                    state = true;
                    underneatheGround = true;
                    topGround = ground;
                } else {
                    underneatheGround = false;
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
            Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().loadedLever.getKeyFrame(Helpers.secondsSince(startTime), false), position, Constants.LEVER_CENTER);
        } else {
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
            Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().unloadedLever.getKeyFrame(Helpers.secondsSince(startTime), false), position, Constants.LEVER_CENTER);
        }
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final Rectangle getConvertBounds() { return bounds; }
    @Override public final float getHeight() { return Constants.LEVER_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.LEVER_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.LEVER_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.LEVER_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.LEVER_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.LEVER_CENTER.y; }
    @Override public final boolean isDense() { return !Helpers.betweenTwoValues(Avatar.getInstance().getPosition().x, getLeft(), getRight()); }
    @Override public final boolean isBeneatheGround() { return underneatheGround; }
    @Override public final Ground getTopGround() { return topGround; }
    @Override public final long getStartTime() { return startTime; }
    public final void setStartTime(long startTime) { this.startTime = startTime; }
    @Override public final void setState(boolean state) { this.state = state; }
    @Override public final boolean getState() { return state; }
    @Override public final void resetStartTime() { this.startTime = 0; }
    @Override public void addCamAdjustment() { adjustments++; }
    @Override public boolean maxAdjustmentsReached() { return adjustments > 3; }
    @Override public boolean tripped() { return previousState != state; }
    @Override public boolean isActive() { return state; }
    @Override public void convert() { state = !state; converted = true; }
    @Override public boolean isConverted() { return converted; }
    @Override public final float jumpMultiplier() { return Constants.LEVER_JUMP_MULTIPLIER; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}

package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;

public class Tripchamber extends Ground implements Trippable, Convertible, Chargeable, Strikeable, Groundable {

    // fields
    public final static String TAG = Tripchamber.class.getName();

    private Vector2 position;
    private Rectangle bounds;
    private boolean active;
    private boolean charged;
    private float chargeTimeSeconds;
    private long startTime;
    private Enums.Upgrade type;
    private boolean converted;
    private boolean previousState;
    private int adjustments;

    // ctor
    public Tripchamber(Vector2 position, Rectangle bounds, boolean state) {
        this.position = position;
        this.active = state;
        this.charged = false;
        this.bounds = bounds;
        startTime = TimeUtils.nanoTime();
        chargeTimeSeconds = 0;
        adjustments = 0;
    }

    public Tripchamber safeClone() {
        Tripchamber clone = new Tripchamber(position, bounds, active);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void update(float delta) {
        converted = false;
        previousState = active;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {

        if (active) {
            if (charged) {
                Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().tripchamberOn.getKeyFrame(Helpers.secondsSince(startTime)), position, Constants.TRIPCHAMBER_CENTER);
            } else {
                Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().tripchamberOn.getKeyFrame(0), position, Constants.TRIPCHAMBER_CENTER);
            }
        } else {
            if (charged) {
                Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().tripchamberOff.getKeyFrame(Helpers.secondsSince(startTime)), position, Constants.TRIPCHAMBER_CENTER);
            } else {
                Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().tripchamberOff.getKeyFrame(0), position, Constants.TRIPCHAMBER_CENTER);
            }
        }
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.TRIPCHAMBER_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.TRIPCHAMBER_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.TRIPCHAMBER_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.TRIPCHAMBER_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.TRIPCHAMBER_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.TRIPCHAMBER_CENTER.y; }
    @Override public final boolean isDense() { return true; }
    @Override public final void setState(boolean state) { active = state; }
    @Override public final boolean isActive() { return active; }
    @Override public final void charge() { charged = true; }
    @Override public final void uncharge() { charged = false;}
    public final boolean isCharged() { return charged; }
    @Override public final void setChargeTime(float chargeTimeSeconds) { this.chargeTimeSeconds = chargeTimeSeconds; }
    public void setUpgrade(Enums.Upgrade type) { this.type = type; }
    public Enums.Upgrade getUpgrade() { return type; }
    @Override public void convert() { active = !active; converted = true; }
    @Override public boolean isConverted() { return converted; }
    @Override public boolean tripped() { return previousState != active; }
    @Override public Rectangle getConvertBounds() { return bounds; }
    @Override public void addCamAdjustment() { this.adjustments++; }
    @Override public boolean maxAdjustmentsReached() { return adjustments >= 2; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}
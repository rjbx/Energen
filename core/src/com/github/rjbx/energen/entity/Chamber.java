package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;

public class Chamber extends Ground implements Chargeable, Strikeable, Groundable {

    // fields
    public final static String TAG = Chamber.class.getName();

    private Vector2 position;
    private boolean active;
    private boolean charged;
    private float chargeTimeSeconds;
    private Enums.Upgrade type;

    // ctor
    public Chamber(Vector2 position) {
        this.position = position;
        this.active = false;
        this.charged = false;
        chargeTimeSeconds = 0;
    }

    public Chamber safeClone() {
        Chamber clone = new Chamber(position);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (active) {
            if (chargeTimeSeconds > 1) {
                charged = true;
                Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().chargedChamber.getKeyFrame(chargeTimeSeconds, true), position, Constants.CHAMBER_CENTER);
            } else {
                Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().activeChamber, position, Constants.CHAMBER_CENTER);
            }
        } else {
            Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().inactiveChamber, position, Constants.CHAMBER_CENTER);
            chargeTimeSeconds = 0;
            charged = false;
        }
    }
    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.CHAMBER_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.CHAMBER_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.CHAMBER_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.CHAMBER_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.CHAMBER_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.CHAMBER_CENTER.y; }
    @Override public final boolean isDense() { return true; }
    @Override public final void setState(boolean state) { this.active = state; }
    @Override public final boolean isActive() { return active; }
    @Override public final void charge() { charged = true; }
    @Override public final void uncharge() { charged = false;}
    public final boolean isCharged() { return charged; }
    @Override public final void setChargeTime(float chargeTimeSeconds) { this.chargeTimeSeconds = chargeTimeSeconds; }
    public void setUpgrade(Enums.Upgrade type) { this.type = type; }
    public Enums.Upgrade getUpgrade() { return type; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}
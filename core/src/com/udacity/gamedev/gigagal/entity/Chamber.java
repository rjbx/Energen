package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Chamber implements Chargeable, Strikeable, Ground {

    // fields
    private Vector2 position;
    private boolean active;
    private float chargeTimeSeconds;
    private Enums.Upgrade type;

    // ctor
    public Chamber(Vector2 position) {
        this.position = position;
        this.active = false;
        chargeTimeSeconds = 0;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (active) {
            if (chargeTimeSeconds != 0) {
                Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().chargedChamber.getKeyFrame(chargeTimeSeconds, true), position, Constants.CHAMBER_CENTER);
            } else {
                Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().activeChamber, position, Constants.CHAMBER_CENTER);
            }
        } else {
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().inactiveChamber, position, Constants.CHAMBER_CENTER);
            chargeTimeSeconds = 0;
        }
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.CHAMBER_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.CHAMBER_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.CHAMBER_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.CHAMBER_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.CHAMBER_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.CHAMBER_CENTER.y; }
    @Override public final void activate() { this.active = true; }
    @Override public final void deactivate() { this.active = false; }
    @Override public final void charge(float chargeTimeSeconds) { this.chargeTimeSeconds = chargeTimeSeconds; }
    @Override public final boolean isActive() { return active; }
    public void setUpgrade(Enums.Upgrade type) { this.type = type; }
    public Enums.Upgrade getUpgrade() { return type; }
    @Override public float getShotRadius() { return Constants.CHAMBER_CENTER.x; }

    @Override public Chamber clone() { return new Chamber(position); }
}
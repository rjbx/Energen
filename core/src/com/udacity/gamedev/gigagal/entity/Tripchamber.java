package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Tripchamber implements Trippable, Convertible, Chargeable, Strikeable, Ground {

    // fields
    public final static String TAG = Tripchamber.class.getName();

    private LevelUpdater level;
    private Vector2 position;
    private Rectangle bounds;
    private boolean active;
    private boolean charged;
    private float chargeTimeSeconds;
    private long startTime;
    private Enums.Upgrade type;
    private boolean convert;
    private boolean previousState;
    private int conversions;

    // ctor
    public Tripchamber(LevelUpdater level, Vector2 position, Rectangle bounds, boolean state) {
        this.level = level;
        this.position = position;
        this.active = state;
        this.charged = false;
        this.bounds = bounds;
        startTime = TimeUtils.nanoTime();
        chargeTimeSeconds = 0;
        conversions = 0;
    }

    @Override
    public void update(float delta) {
        if (convert) {
            for (Ground ground : level.getGrounds()) {
                if (ground instanceof Convertible && ground != this) {
                    if (Helpers.betweenFourValues(ground.getPosition(), bounds.x, bounds.x + bounds.width, bounds.y, bounds.y + bounds.height)) {
                        ((Convertible) ground).convert();
                        conversions++;
                    }
                }
            }
        }
        convert = false;
        previousState = active;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (active) {
            if (charged) {
                Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().tripchamberOn.getKeyFrame(Helpers.secondsSince(startTime)), position, Constants.TRIPCHAMBER_CENTER);
            } else {
                Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().tripchamberOn.getKeyFrame(0), position, Constants.TRIPCHAMBER_CENTER);
            }
        } else {
            if (charged) {
                Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().tripchamberOff.getKeyFrame(Helpers.secondsSince(startTime)), position, Constants.TRIPCHAMBER_CENTER);
            } else {
                Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().tripchamberOff.getKeyFrame(0), position, Constants.TRIPCHAMBER_CENTER);
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
    public final void charge() { charged = true; }
    public final void uncharge() { charged = false;}
    public final boolean isCharged() { return charged; }
    @Override public final void setChargeTime(float chargeTimeSeconds) { this.chargeTimeSeconds = chargeTimeSeconds; }
    public void setUpgrade(Enums.Upgrade type) { this.type = type; }
    public Enums.Upgrade getUpgrade() { return type; }
    @Override public void convert() { active = !active; convert = true; }
    @Override public boolean isConverted() { return active; }
    @Override public boolean tripped() { return previousState != active; }
    @Override public Rectangle getBounds() { return bounds; }
    @Override public boolean hasSetCam() { return conversions >= 2; }
    @Override public Tripchamber clone() { return new Tripchamber(level, position, bounds, active); }
}
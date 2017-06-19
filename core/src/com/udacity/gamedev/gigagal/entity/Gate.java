package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Gate implements Strikeable, Nonstatic, Ground {

    // fields
    public final static String TAG = Gate.class.getName();

    private final Vector2 position;
    private long startTime;
    private boolean active;
    private boolean dense;

    // ctor
    public Gate(Vector2 position) {
        this.position = position;
        this.startTime = 0;
        active = true;
        dense = true;
    }

    @Override
    public void update(float delta) {
        if (!active) {
            if (startTime == 0 && dense) {
                startTime = TimeUtils.nanoTime();
            } else if (startTime != 0 && Helpers.secondsSince(startTime - Constants.GATE_FRAME_DURATION) >= Assets.getInstance().getGroundAssets().gateOpen.getAnimationDuration()) {
                startTime = 0;
                dense = false;
            } else if (position.x < GigaGal.getInstance().getPosition().x - GigaGal.getInstance().getWidth() / 2) { // prevents from re-unlocking after crossing gate boundary (always left to right)
                startTime = TimeUtils.nanoTime();
                dense = true;
                active = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (startTime != 0) {
            if (!active) {
                Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().gateOpen.getKeyFrame(Helpers.secondsSince(startTime)), position, Constants.GATE_CENTER);
            } else {
                Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().gateClose.getKeyFrame(Helpers.secondsSince(startTime)), position, Constants.GATE_CENTER);
            }
        } else {
            if (dense) {
                Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().gateOpen.getKeyFrame(0), position, Constants.GATE_CENTER);
            } else {
                Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().gateOpen.getKeyFrame(Constants.GATE_FRAME_DURATION * 6), position, Constants.GATE_CENTER);
            }
        }
    }

    public boolean equals(Object object) {
        if (object instanceof Gate) {
            Gate gate = (Gate) object;
            return getTop() == gate.getTop() && getBottom() == gate.getBottom() && getLeft() == gate.getLeft() && getRight() == gate.getRight();
        }
        return false;
    }
    public final boolean isActive() { return active; }
    public void deactivate() { active = false; }
    private void setState(boolean state) { active = state; }
    public void setDensity(boolean dense) { this.dense = dense; }
    private void setStartTime(long startTime) { this.startTime = startTime; }
    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.GATE_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.GATE_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.GATE_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.GATE_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.GATE_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.GATE_CENTER.y; }
    @Override public final boolean isDense() { return dense; }
    @Override public Gate clone() { Gate clone = new Gate(position); clone.setDensity(isDense()); clone.setState(active); clone.setStartTime(startTime); return clone; }
}

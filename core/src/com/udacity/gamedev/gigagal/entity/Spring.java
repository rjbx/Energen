package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Spring extends Ground implements Reboundable {

    // fields
    public final static String TAG = Spring.class.getName();

    private Vector2 position;
    private long startTime;
    private boolean isLoaded;

    // ctor
    public Spring(Vector2 position) {
        this.position = position;
        startTime = 0;
        isLoaded = false;
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (isLoaded) {
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().loadedSpring.getKeyFrame(Helpers.secondsSince(startTime), false), position, Constants.SPRING_CENTER);
        } else {
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().unloadedSpring.getKeyFrame(Helpers.secondsSince(startTime), false), position, Constants.SPRING_CENTER);
        }
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.SPRING_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.SPRING_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.SPRING_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.SPRING_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.SPRING_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.SPRING_CENTER.y; }
    @Override public final boolean isDense() { return true; }
    @Override public final long getStartTime() { return startTime; }
    public final void setStartTime(long startTime) { this.startTime = startTime; }
    @Override public final void setState(boolean state) { this.isLoaded = state; }
    @Override public final boolean getState() { return isLoaded; }
    @Override public final void resetStartTime() { this.startTime = 0; }
}

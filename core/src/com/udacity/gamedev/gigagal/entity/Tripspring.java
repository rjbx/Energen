package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Tripspring extends Ground implements Trippable, Compressible, Reboundable {

    // fields
    public final static String TAG = Tripspring.class.getName();

    private Ground topGround;
    private Vector2 position;
    private Rectangle bounds;
    private long startTime;
    private boolean loaded;
    private boolean beingCarried;
    private boolean underGround;
    private int adjustments;
    private boolean previousState;
    private boolean converted;

    // ctor
    public Tripspring(Vector2 position, Rectangle bounds) {
        this.position = position;
        startTime = 0;
        loaded = false;
        beingCarried = false;
        underGround = false;
        startTime = 0;
        converted = false;
        this.bounds = bounds;
        previousState = loaded;
        adjustments++;
    }

    @Override
    public void update(float delta) {
        converted = false;
        previousState = loaded;
        for (Ground ground : LevelUpdater.getInstance().getGrounds()) {
            if (Helpers.overlapsPhysicalObject(this, ground)) {
                if (Helpers.betweenTwoValues(getTop(), ground.getBottom() - 1, ground.getBottom() + 1)) {
                    if (!loaded) {
                        resetStartTime();
                    }
                    loaded = true;
                    underGround = true;
                } else {
                    underGround = false;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (loaded) {
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
            Gdx.app.log(TAG, Helpers.secondsSince(startTime) + "");
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().loadedLever.getKeyFrame(Helpers.secondsSince(startTime), false), position, Constants.LEVER_CENTER);
        } else {
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().unloadedLever.getKeyFrame(Helpers.secondsSince(startTime), false), position, Constants.LEVER_CENTER);
        }
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final Rectangle getBounds() { return bounds; }
    @Override public final float getHeight() { return Constants.LEVER_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.LEVER_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.LEVER_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.LEVER_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.LEVER_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.LEVER_CENTER.y; }
    @Override public final boolean isDense() { return !Helpers.betweenTwoValues(GigaGal.getInstance().getPosition().x, getLeft(), getRight()) || beingCarried; }
    @Override public final boolean isUnderneatheGround() { return underGround; }
    @Override public final Ground getTopGround() { return topGround; }
    @Override public final long getStartTime() { return startTime; }
    public final void setStartTime(long startTime) { this.startTime = startTime; }
    @Override public final void setState(boolean state) { this.loaded = state; }
    @Override public final boolean getState() { return loaded; }
    @Override public final void resetStartTime() { this.startTime = 0; }
    @Override public void addCamAdjustment() { adjustments++; }
    @Override public boolean maxAdjustmentsReached() { return adjustments > 2; }
    @Override public boolean tripped() { return previousState != loaded; }
    @Override public boolean isActive() { return loaded; }
    @Override public void convert() { loaded = !loaded; converted = true; }
    @Override public boolean isConverted() { return converted; }
    @Override public final float jumpMultiplier() { return Constants.LEVER_JUMP_MULTIPLIER; }
}

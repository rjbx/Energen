package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Helpers;

// class name avoids confusion with existing button assets and constants
public class Tripknob implements Trippable, Convertible, Strikeable, Ground {

    // fields
    public final static String TAG = Tripknob.class.getName();

    private Vector2 position;
    private boolean convert;
    private LevelUpdater level;
    private Rectangle bounds;
    private long startTime;
    private int rotation;
    private Vector2 offset;
    private boolean state;

    // ctor
    public Tripknob(LevelUpdater level, Vector2 position, Rectangle bounds, float rotation, boolean state) {
        this.position = position;
        this.level = level;
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
        convert = false;
        this.state = state;
    }

    @Override
    public void update(float delta) {
        if (convert) {
            for (Ground ground : level.getGrounds()) {
                if (ground instanceof Convertible && ground != this) {
                    if (Helpers.betweenFourValues(ground.getPosition(), bounds.x, bounds.x + bounds.width, bounds.y, bounds.y + bounds.height)) {
                            ((Convertible) ground).convert();
                    }
                }
            }
        }
        convert = false;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (state) {
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().tripknobOn.getKeyFrame(Helpers.secondsSince(startTime), false), position, offset, 1, rotation);
        } else {
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().tripknobOff.getKeyFrame(Helpers.secondsSince(startTime), false), position, offset, 1, rotation);
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
    @Override public final void resetStartTime() { this.startTime = 0; }
    public Rectangle getBounds() { return bounds; }
    @Override public boolean getState() { return state; }
    @Override public void setState(boolean state) { this.state = state; convert = true; }
    @Override public void convert() { state = !state; convert = true; }
    @Override public boolean isConverted() { return state; }
    @Override public Tripknob clone() { return new Tripknob(level, position, bounds, rotation, state); }
}
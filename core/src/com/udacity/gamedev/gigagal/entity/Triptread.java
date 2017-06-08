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
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Triptread implements Trippable, Convertible, Rideable, Ground {

    // fields
    public final static String TAG = Triptread.class.getName();

    private Vector2 position;
    private boolean convert;
    private LevelUpdater level;
    private Rectangle bounds;
    private long startTime;
    private boolean state;
    private Enums.Direction direction;

    // ctor
    public Triptread(LevelUpdater level, Vector2 position, Rectangle bounds, boolean state, Enums.Direction direction) {
        this.position = position;
        this.level = level;
        this.bounds = bounds;
        this.state = state;
        this.direction = direction;
        startTime = TimeUtils.nanoTime();
        convert = false;
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
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().triptreadOn.getKeyFrame(Helpers.secondsSince(startTime)), position, Constants.TRIPTREAD_CENTER);
        } else {
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().triptreadOff.getKeyFrame(Helpers.secondsSince(startTime)), position, Constants.TRIPTREAD_CENTER);
        }
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.TRIPTREAD_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.TRIPTREAD_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.TRIPTREAD_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.TRIPTREAD_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.TRIPTREAD_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.TRIPTREAD_CENTER.y; }
    @Override public final boolean isDense() { return true; }
    public final long getStartTime() { return startTime; }
    @Override public final void resetStartTime() { this.startTime = 0; }
    public Rectangle getBounds() { return bounds; }
    @Override public boolean getState() { return state; }
    @Override public void setState(boolean state) { this.state = state; convert = true; }
    @Override public void convert() { state = !state; convert = true; }
    @Override public boolean isConverted() { return state; }
    @Override public final Enums.Direction getDirection() { return direction; }
    @Override public Triptread clone() { return new Triptread(level, position, bounds, state, direction); }
}

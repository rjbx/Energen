package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
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

public class Triptread extends Ground implements Trippable, Convertible, Propelling {

    // fields
    public final static String TAG = Triptread.class.getName();

    private Vector2 position;
    private Vector2 velocity;
    private boolean converted;
    private LevelUpdater level;
    private Rectangle bounds;
    private long startTime;
    private boolean state;
    private Enums.Direction direction;
    private boolean previousState;
    private int adjustments;
    private Animation animation;

    // ctor
    public Triptread(LevelUpdater level, Vector2 position, Rectangle bounds, boolean state, Enums.Direction direction) {
        this.position = position;
        this.level = level;
        this.bounds = bounds;
        this.state = state;
        this.direction = direction;
        startTime = TimeUtils.nanoTime();
        converted = false;
        adjustments = 0;
    }

    @Override
    public void update(float delta) {
        converted = false;
        previousState = state;
        if (state) {
            if (direction == Enums.Direction.LEFT) {
                animation = Assets.getInstance().getGroundAssets().triptreadLeftOn;
            } else {
                animation = Assets.getInstance().getGroundAssets().triptreadRightOn;
            }
        } else {
            if (direction == Enums.Direction.LEFT) {
                animation = Assets.getInstance().getGroundAssets().triptreadLeftOff;
            } else {
                animation = Assets.getInstance().getGroundAssets().triptreadRightOff;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime)), position, Constants.TRIPTREAD_CENTER);
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
    public Rectangle getBounds() { return bounds; }
    @Override public boolean isActive() { return state; }
    @Override public void setState(boolean state) { this.state = state; converted = true; }
    @Override public void convert() { direction = Helpers.getOppositeDirection(direction); }
    @Override public boolean isConverted() { return converted; }
    @Override public void addCamAdjustment() { adjustments++; }
    @Override public boolean maxAdjustmentsReached() { return adjustments >= 2; }
    @Override public boolean tripped() { return previousState != state; }
    @Override public final Enums.Direction getDirectionX() { return direction; }
}
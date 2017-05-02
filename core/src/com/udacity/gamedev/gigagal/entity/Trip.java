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

import static com.udacity.gamedev.gigagal.util.Enums.Material.NATIVE;

public class Trip implements Reboundable, Ground, Destructible, Hazard {

    // fields
    private Vector2 position;
    private LevelUpdater level;
    private Rectangle bounds;
    private long startTime;
    private boolean state;

    // ctor
    public Trip(LevelUpdater level, Vector2 position, Rectangle bounds) {
        this.position = position;
        this.level = level;
        this.bounds = bounds;
        startTime = 0;
        state = false;
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (state) {
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().tripOff.getKeyFrame(Helpers.secondsSince(startTime), false), position, Constants.TRIP_CENTER);
        } else {
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().tripOn.getKeyFrame(Helpers.secondsSince(startTime), false), position, Constants.TRIP_CENTER);
        }
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.TRIP_CENTER.x * 2; }
    @Override public final float getWidth() { return Constants.TRIP_CENTER.y * 2; }
    @Override public final float getLeft() { return position.x - Constants.TRIP_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.TRIP_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.TRIP_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.TRIP_CENTER.y; }
    @Override public final long getStartTime() { return startTime; }
    @Override public final void setLoaded(boolean state) { this.state = state; }
    @Override public final void resetStartTime() { this.startTime = 0; }
    @Override public Enums.Material getType() { return NATIVE; }
    @Override public float getHealth() { return 1; }
    @Override public void setHealth(float health) { ; }
    @Override public float getShotRadius() { return Constants.TRIP_SHOT_RADIUS; }
    @Override public int getHitScore() { return 0; }
    @Override public int getKillScore() { return 0; }
    public Rectangle getBounds() { return bounds; }
    public boolean getState() { return state; }
    public int getDamage() { return 0; }
    @Override public Vector2 getKnockback() { return Vector2.Zero; }
    @Override public Trip clone() { return new Trip(level, position, bounds); }

    public void setState(boolean state) {
        this.state = state;
        for (Ground ground : level.getGrounds()) {
            if (ground instanceof Trippable) {
                if (Helpers.betweenFourValues(ground.getPosition(), bounds.x, bounds.x + bounds.width, bounds.y, bounds.y + bounds.height)) {
                    ((Treadmill) ground).trip();
                }
            }
        }
    }
}

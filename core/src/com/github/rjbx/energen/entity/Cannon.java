package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;

public class Cannon extends Ground implements Energized, Nonstatic, Rappelable, Convertible {

    // fields
    public final static String TAG = Cannon.class.getName();
    private Vector2 position;
    private Vector2 center;
    private Enums.Orientation orientation;
    private Enums.ShotIntensity intensity;
    private TextureRegion region;
    private long startTime;
    private boolean active;
    private boolean canDispatch;

    // ctor
    public Cannon(Vector2 position, Enums.Orientation orientation, Enums.ShotIntensity intensity, boolean active) {
        this.position = position;
        this.orientation = orientation;
        this.intensity = intensity;
        startTime = 0;
        canDispatch = false;
        this.active = active;
        switch (orientation) {
            case Y:
                region = AssetManager.getInstance().getGroundAssets().yCannon;
                center = Constants.Y_CANNON_CENTER;
                break;
            case X:
                region = AssetManager.getInstance().getGroundAssets().xCannon;
                center = Constants.X_CANNON_CENTER;
                break;
        }
    }

    public Cannon safeClone() {
        Cannon clone = new Cannon(position, orientation, intensity, active);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    public void update(float delta) {
        canDispatch = false;
        if (active) {
            if (this.getStartTime() == 0) {
                this.setStartTime(TimeUtils.nanoTime());
            }
            if ((Helpers.secondsSince(this.getStartTime()) > Constants.CANNON_DISPATCH_RATE)) {
                this.setStartTime(TimeUtils.nanoTime());
                canDispatch = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, region, getPosition(), getCenter());
    }

    @Override public final Vector2 getPosition() { return position; }
    public final Vector2 getCenter() { return center; }
    @Override public final float getWidth() { return center.x * 2; }
    @Override public final float getHeight() { return center.y * 2; }
    @Override public final float getLeft() { return position.x - center.x; }
    @Override public final float getRight() { return position.x + center.x; }
    @Override public final float getTop() { return position.y + center.y; }
    @Override public final float getBottom() { return position.y - center.y; }
    @Override public final boolean isDense() { return true; }
    @Override public void convert() { active = !active; }
    @Override public boolean isConverted() {  return active; }
    @Override public final boolean getDispatchStatus() { return canDispatch; }
    @Override public final Enums.ShotIntensity getIntensity() { return intensity; }
    public final Enums.Orientation getOrientation() { return orientation; }
    public final long getStartTime() { return startTime; }
    public final void setStartTime(long startTime) { this.startTime = startTime; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}

package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Cannon implements Ground {

    // fields
    private Vector2 position;
    private Enums.Orientation orientation;
    private Enums.AmmoIntensity intensity;
    private TextureRegion region;
    private Vector2 center;
    private float offset;
    private long startTime;

    // ctor
    public Cannon(Vector2 position, Enums.Orientation orientation, Enums.AmmoIntensity intensity) {

        this.position = position;
        this.orientation = orientation;
        this.intensity = intensity;
        startTime = 0;
        offset = 0;
        switch (orientation) {
            case Y:
                region = Assets.getInstance().getGroundAssets().yCannon;
                center = Constants.Y_CANNON_CENTER;
                break;
            case X:
                region = Assets.getInstance().getGroundAssets().xCannon;
                center = Constants.X_CANNON_CENTER;
                break;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Helpers.drawTextureRegion(batch, region, position, center);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getWidth() { return center.x * 2; }
    @Override public final float getHeight() { return center.y * 2; }
    @Override public final float getLeft() { return position.x - center.x; }
    @Override public final float getRight() { return position.x + center.x; }
    @Override public final float getTop() { return position.y + center.y; }
    @Override public final float getBottom() { return position.y - center.y; }
    public final Enums.Orientation getOrientation() { return orientation; }
    public final Enums.AmmoIntensity getIntensity() { return intensity; }
    public final float getOffset() { return offset; }
    public final void setOffset(float offset) { this.offset = offset; }
    public final long getStartTime() { return startTime; }
    public final void setStartTime(long startTime) { this.startTime = startTime; }
}

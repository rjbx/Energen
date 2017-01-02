package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

public class Cannon implements Ground {

    // fields
    private Vector2 position;
    private Enums.Orientation orientation;
    private TextureRegion region;
    private Vector2 center;
    private float offset;
    private long startTime;

    // ctor
    public Cannon(Vector2 position, Enums.Orientation orientation) {

        this.position = position;
        this.orientation = orientation;
        startTime = 0;
        offset = 0;
        switch (orientation) {
            case VERTICAL:
                region = Assets.getInstance().getCannonAssets().verticalCannon;
                center = Constants.VERTICAL_CANNON_CENTER;
                break;
            case LATERAL:
                region = Assets.getInstance().getCannonAssets().lateralCannon;
                center = Constants.LATERAL_CANNON_CENTER;
                break;
        }
    }

    public void render(SpriteBatch batch) {
        Utils.drawTextureRegion(batch, region, position, center);
    }

    public final Vector2 getPosition() { return position; }
    public final float getWidth() { return center.x * 2; }
    public final float getHeight() { return center.y * 2; }
    public final float getLeft() { return position.x - center.x; }
    public final float getRight() { return position.x + center.x; }
    public final float getTop() { return position.y + center.y; }
    public final float getBottom() { return position.y - center.y; }
    public final Enums.Orientation getOrientation() { return orientation; }
    public final Class getSubclass() { return this.getClass(); }
    public final float getOffset() { return offset; }
    public final void setOffset(float offset) { this.offset = offset; }
    public final long getStartTime() { return startTime; }
    public final void setStartTime(long startTime) { this.startTime = startTime; }
}

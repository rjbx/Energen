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

    // ctor
    public Cannon(Vector2 position, Enums.Orientation orientation) {

        this.position = position;
        this.orientation = orientation;
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
    public final float getHeight() { return Constants.LATERAL_CANNON_CENTER.x * 2; }
    public final float getWidth() { return Constants.LATERAL_CANNON_CENTER.y * 2; }
    public final float getLeft() { return position.x - Constants.LATERAL_CANNON_CENTER.x; }
    public final float getRight() { return position.x + Constants.LATERAL_CANNON_CENTER.x; }
    public final float getTop() { return position.y + Constants.LATERAL_CANNON_CENTER.y; }
    public final float getBottom() { return position.y - Constants.LATERAL_CANNON_CENTER.y; }
    public final Class getSubclass() { return this.getClass(); }
}

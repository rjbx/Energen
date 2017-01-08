package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

public class Treadmill implements RideableGround {

    // fields
    private Vector2 position;
    private Enums.Direction direction;
    private long startTime;

    // ctor
    public Treadmill(Vector2 position, Enums.Direction direction) {
        this.position = position;
        this.direction = direction;
        this.startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        TextureRegion region;

        if (direction == Enums.Direction.RIGHT) {
            region = Assets.getInstance().getTreadmillAssets().treadmillRight.getKeyFrame(elapsedTime, true);
        } else {
            region = Assets.getInstance().getTreadmillAssets().treadmillLeft.getKeyFrame(elapsedTime, true);
        }
        Utils.drawTextureRegion(batch, region, position, Constants.TREADMILL_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.TREADMILL_CENTER.x * 2; }
    @Override public final float getWidth() { return Constants.TREADMILL_CENTER.y * 2; }
    @Override public final float getLeft() { return position.x - Constants.TREADMILL_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.TREADMILL_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.TREADMILL_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.TREADMILL_CENTER.y; }
    public final Enums.Direction getDirection() { return direction; }
}

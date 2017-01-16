package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

public class Treadmill implements SkateableGround {

    // fields
    private Vector2 position;
    private Vector2 scale;
    private Vector2 adjustedCenter;
    private Enums.Direction direction;
    private long startTime;

    // ctor
    public Treadmill(Vector2 position, Vector2 scale, Vector2 adjustedCenter, Enums.Direction direction) {
        this.position = position;
        this.scale = scale;
        this.adjustedCenter = adjustedCenter;
        this.direction = direction;
        startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region;
        if (direction == Enums.Direction.RIGHT) {
            region = Assets.getInstance().getTreadmillAssets().treadmillRight.getKeyFrame(elapsedTime, true);
        } else {
            region = Assets.getInstance().getTreadmillAssets().treadmillLeft.getKeyFrame(elapsedTime, true);
        }
        Utils.drawTextureRegion(batch, region, position, adjustedCenter, scale);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.TREADMILL_CENTER.x * 2 * scale.x; }
    @Override public final float getWidth() { return Constants.TREADMILL_CENTER.y * 2 * scale.y; }
    @Override public final float getLeft() { return position.x - Constants.TREADMILL_CENTER.x * scale.x; }
    @Override public final float getRight() { return position.x + Constants.TREADMILL_CENTER.x * scale.x; }
    @Override public final float getTop() { return position.y + Constants.TREADMILL_CENTER.y * scale.y; }
    @Override public final float getBottom() { return position.y - Constants.TREADMILL_CENTER.y * scale.y; }
}
package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Treadmill implements RideableGround {

    // fields
    private Vector2 position;
    private Vector2 scale;
    private Vector2 adjustedCenter;
    private Enums.Direction direction;
    private long startTime;
    private Animation animation;

    // ctor
    public Treadmill(Vector2 position, Vector2 scale, Vector2 adjustedCenter, Enums.Direction direction) {
        this.position = position;
        this.scale = scale;
        this.adjustedCenter = adjustedCenter;
        this.direction = direction;
        startTime = TimeUtils.nanoTime();
        if (direction == Enums.Direction.RIGHT) {
            animation = Assets.getInstance().getGroundAssets().treadmillRight;
        } else {
            animation = Assets.getInstance().getGroundAssets().treadmillLeft;
        }
    }

    public void update() {}

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime), true), position, adjustedCenter, scale);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.TREADMILL_CENTER.x * 2 * scale.x; }
    @Override public final float getWidth() { return Constants.TREADMILL_CENTER.y * 2 * scale.y; }
    @Override public final float getLeft() { return position.x - Constants.TREADMILL_CENTER.x * scale.x; }
    @Override public final float getRight() { return position.x + Constants.TREADMILL_CENTER.x * scale.x; }
    @Override public final float getTop() { return position.y + Constants.TREADMILL_CENTER.y * scale.y; }
    @Override public final float getBottom() { return position.y - Constants.TREADMILL_CENTER.y * scale.y; }
    @Override public final Enums.Direction getDirection() { return direction; }
}
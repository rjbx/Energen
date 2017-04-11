package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Coals implements UnbearableGround {

    // fields
    private Vector2 position;
    private Vector2 scale;
    private Vector2 adjustedCenter;
    private long startTime;

    // ctor
    public Coals(Vector2 position, Vector2 scale, Vector2 adjustedCenter) {
        this.position = position;
        this.scale = scale;
        this.adjustedCenter = adjustedCenter;
        this.startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(SpriteBatch batch) {
        final float elapsedTime = Helpers.secondsSince(startTime);
        final TextureRegion region = Assets.getInstance().getGroundAssets().coals.getKeyFrame(elapsedTime, true);
        Helpers.drawTextureRegion(batch, region, position, adjustedCenter, scale);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.COALS_CENTER.x * 2 * scale.x; }
    @Override public final float getWidth() { return Constants.COALS_CENTER.y * 2 * scale.y; }
    @Override public final float getLeft() { return position.x - Constants.COALS_CENTER.x * scale.x; }
    @Override public final float getRight() { return position.x + Constants.COALS_CENTER.x * scale.x; }
    @Override public final float getTop() { return position.y + Constants.COALS_CENTER.y * scale.y; }
    @Override public final float getBottom() { return position.y - Constants.COALS_CENTER.y * scale.y; }
}

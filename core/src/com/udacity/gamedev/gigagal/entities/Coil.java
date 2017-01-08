package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class Coil implements IndestructibleHazard {

    // fields
    private Vector2 position;
    long startTime;

    // ctor
    public Coil(Vector2 position) {
        this.position = position;
        startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region = Assets.getInstance().getCoilAssets().coil.getKeyFrame(elapsedTime, true);
        Utils.drawTextureRegion(batch, region, position, Constants.COIL_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getWidth() { return Constants.COIL_COLLISION_WIDTH; }
    @Override public final float getHeight() { return Constants.COIL_COLLISION_HEIGHT; }
    @Override public final float getLeft() { return position.x - Constants.COIL_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.COIL_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.COIL_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.COIL_CENTER.y; }
    @Override public final int getDamage() { return Constants.COIL_DAMAGE; }
    @Override public final Vector2 getKnockback() { return Constants.COIL_KNOCKBACK; }
}

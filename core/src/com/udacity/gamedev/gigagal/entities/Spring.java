package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class Spring implements BounceableGround {

    // fields
    private Vector2 position;
    private long startTime;
    private boolean loaded;

    // ctor
    public Spring(Vector2 position) {
        this.position = position;
        startTime = 0;
        loaded = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion loadedSpring = Assets.getInstance().getSpringAssets().loaded.getKeyFrame(elapsedTime, false);
        final TextureRegion unloadedSpring = Assets.getInstance().getSpringAssets().unloaded.getKeyFrame(elapsedTime, false);
        if (loaded) {
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
            Utils.drawTextureRegion(batch, loadedSpring, position, Constants.SPRING_CENTER);
        } else {
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
            Utils.drawTextureRegion(batch, unloadedSpring, position, Constants.SPRING_CENTER);
        }
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.SPRING_CENTER.x * 2; }
    @Override public final float getWidth() { return Constants.SPRING_CENTER.y * 2; }
    @Override public final float getLeft() { return position.x - Constants.SPRING_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.SPRING_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.SPRING_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.SPRING_CENTER.y; }
    @Override public final long getStartTime() { return startTime; }
    @Override public final void setLoaded(boolean state) { this.loaded = state; }
    @Override public final void resetStartTime() { this.startTime = 0; }
}

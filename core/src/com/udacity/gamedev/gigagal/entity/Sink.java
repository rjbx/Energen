package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.ImageLoader;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Sink implements Sinkable, Ground {

    // fields
    private final Vector2 position;
    private final long startTime;

    // ctor
    public Sink(Vector2 position) {
        this.position = position;
        startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, ImageLoader.getInstance().getGroundAssets().sink.getKeyFrame(Helpers.secondsSince(startTime)), position, Constants.SINK_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.SINK_CENTER.x * 2; }
    @Override public final float getWidth() { return Constants.SINK_CENTER.y * 2; }
    @Override public final float getLeft() { return position.x - Constants.SINK_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.SINK_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.SINK_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.SINK_CENTER.y; }
    @Override public Sink clone() { return new Sink(position); }
}

package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class Sink implements Ground, Descendable {

    // fields
    private Vector2 position;
    private long startTime;

    // ctor
    public Sink(Vector2 position) {
        this.position = position;
        this.startTime = TimeUtils.nanoTime();
    }

    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region = Assets.getInstance().getSinkAssets().sink.getKeyFrame(elapsedTime, true);
        Utils.drawTextureRegion(batch, region, position, Constants.SINK_CENTER);
    }

    public final Vector2 getPosition() { return position; }
    public final float getHeight() { return Constants.SINK_CENTER.x * 2; }
    public final float getWidth() { return Constants.SINK_CENTER.y * 2; }
    public final float getLeft() { return position.x - Constants.SINK_CENTER.x; }
    public final float getRight() { return position.x + Constants.SINK_CENTER.x; }
    public final float getTop() { return position.y + Constants.SINK_CENTER.y; }
    public final float getBottom() { return position.y - Constants.SINK_CENTER.y; }
    public final Class getSubclass() { return this.getClass(); }
}

package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class Coals implements Ground {

    // fields
    private Vector2 position;
    private long startTime;

    // ctor
    public Coals(Vector2 position) {
        this.position = position;
        this.startTime = TimeUtils.nanoTime();
    }

    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region = Assets.getInstance().getCoalsAssets().coals.getKeyFrame(elapsedTime, true);
        Utils.drawTextureRegion(batch, region, position, Constants.COALS_CENTER);
    }

    public final Vector2 getPosition() { return position; }
    public final float getHeight() { return Constants.COALS_CENTER.x * 2; }
    public final float getWidth() { return Constants.COALS_CENTER.y * 2; }
    public final float getLeft() { return position.x - Constants.COALS_CENTER.x; }
    public final float getRight() { return position.x + Constants.COALS_CENTER.x; }
    public final float getTop() { return position.y + Constants.COALS_CENTER.y; }
    public final float getBottom() { return position.y - Constants.COALS_CENTER.y; }
    public final Class getSubclass() { return this.getClass(); }
}

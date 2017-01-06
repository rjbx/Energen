package com.udacity.gamedev.gigagal.entities;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class Pole implements Ground, Climbable {

    // fields
    private Vector2 position;
    private long startTime;

    // ctor
    public Pole(Vector2 position) {
        this.position = position;
        this.startTime = TimeUtils.nanoTime();
    }

    public void render(SpriteBatch batch) {

        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region = Assets.getInstance().getPoleAssets().pole.getKeyFrame(elapsedTime, true);
        Utils.drawTextureRegion(batch, region, position, Constants.POLE_CENTER);
    }

    public final Vector2 getPosition() { return position; }
    public final float getHeight() { return Constants.POLE_CENTER.y * 2; }
    public final float getWidth() { return Constants.POLE_CENTER.x * 2; }
    public final float getLeft() { return position.x - Constants.POLE_CENTER.x; }
    public final float getRight() { return position.x + Constants.POLE_CENTER.x; }
    public final float getTop() { return position.y + Constants.POLE_CENTER.y; }
    public final float getBottom() { return position.y - Constants.POLE_CENTER.y; }
    public final Class getSubclass() { return this.getClass(); }
}

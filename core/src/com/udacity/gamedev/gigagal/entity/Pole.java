package com.udacity.gamedev.gigagal.entity;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Pole implements Climbable, Ground {

    // fields
    private Vector2 position;
    private long startTime;

    // ctor
    public Pole(Vector2 position) {
        this.position = position;
        this.startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().pole.getKeyFrame(Helpers.secondsSince(startTime), true), position, Constants.POLE_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.POLE_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.POLE_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.POLE_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.POLE_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.POLE_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.POLE_CENTER.y; }
    @Override public Pole clone() { return new Pole(position); }
}

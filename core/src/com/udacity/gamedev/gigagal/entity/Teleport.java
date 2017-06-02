package com.udacity.gamedev.gigagal.entity;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Teleport implements Transport {

    //fields
    public final static String TAG = Teleport.class.getName();

    private Vector2 position;
    private Vector2 destination;
    private long startTime;
    
    public Teleport(Vector2 position, Vector2 destination) {
        this.position = position;
        this.destination = destination;
        this.startTime = TimeUtils.nanoTime();
    }
    
    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getPortalAssets().teleport.getKeyFrame(Helpers.secondsSince(startTime), true), position, Constants.TELEPORT_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.TELEPORT_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.TELEPORT_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.TELEPORT_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.TELEPORT_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.TELEPORT_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.TELEPORT_CENTER.y; }
    @Override public final Vector2 getDestination() { return destination; }
    @Override public Pole clone() { return new Pole(position); }
}

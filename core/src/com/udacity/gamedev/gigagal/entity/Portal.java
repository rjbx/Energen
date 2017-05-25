package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Helpers;

// immutable
public final class Portal implements Transport {

    // fields
    public final static String TAG = Portal.class.getName();
    private final Vector2 position;
    private final Vector2 destination;
    private final long startTime;

    //ctor
    public Portal(Vector2 position) {
        this.position = position;
        this.destination = position;
        startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getPortalAssets().portal.getKeyFrame(Helpers.secondsSince(startTime)), position, Constants.PORTAL_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final Vector2 getDestination() { return destination; }
    @Override public final float getHeight() { return Constants.PORTAL_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.PORTAL_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.PORTAL_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.PORTAL_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.PORTAL_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.PORTAL_CENTER.y; }
}


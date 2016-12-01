package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

// immutable
public final class Portal {

    // fields
    public final static String TAG = Portal.class.getName();
    private final Vector2 position;
    private final long startTime;

    //ctor
    public Portal(Vector2 position) {
        this.position = position;
        startTime = TimeUtils.nanoTime();
    }

    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region = Assets.getInstance().getPortalAssets().portal.getKeyFrame(elapsedTime, true);
        Utils.drawTextureRegion(batch, region, position, Constants.PORTAL_CENTER);
    }

    public final Vector2 getPosition() { return position; }
}


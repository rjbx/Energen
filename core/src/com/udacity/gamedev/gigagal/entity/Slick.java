package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Helpers;

import java.io.InvalidObjectException;

public class Slick implements Rappelable, Hurdleable, Skateable, Ground {
    
    // fields
    public final static String TAG = Slick.class.getName();

    private Vector2 position;
    private Vector2 scale;
    private Vector2 adjustedCenter;
    private long startTime;

    // ctor
    public Slick(Vector2 position, Vector2 scale, Vector2 adjustedCenter) {
        this.position = position;
        this.scale = scale;
        this.adjustedCenter = adjustedCenter;
        this.startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().slick.getKeyFrame(Helpers.secondsSince(startTime), true), position, adjustedCenter, scale);
    }


    @Override
    public boolean equals(Object object) {
        if (object instanceof Slick) {
            Slick slick = (Slick) object;
            return getTop() == slick.getTop() && getBottom() == slick.getBottom() && getLeft() == slick.getLeft() && getRight() == slick.getRight();
        }
        return false;
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.SLICK_CENTER.y * 2 * scale.y; }
    @Override public final float getWidth() { return Constants.SLICK_CENTER.x * 2 * scale.x; }
    @Override public final float getLeft() { return position.x - Constants.SLICK_CENTER.x * scale.x; }
    @Override public final float getRight() { return position.x + Constants.SLICK_CENTER.x * scale.x; }
    @Override public final float getTop() { return position.y + Constants.SLICK_CENTER.y * scale.y; }
    @Override public final float getBottom() { return position.y - Constants.SLICK_CENTER.y * scale.y; }
    @Override public final boolean isDense() { return getHeight() > Constants.MAX_LEDGE_HEIGHT; }
    @Override public Slick clone() { return new Slick(position, scale, adjustedCenter); }
}

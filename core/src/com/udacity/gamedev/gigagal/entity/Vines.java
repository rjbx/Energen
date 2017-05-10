package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.ImageLoader;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Vines implements Climbable, Ground {

    // fields
    private Vector2 position;
    private Vector2 adjustedCenter;
    private Vector2 scale;

    // ctor
    public Vines(Vector2 position, Vector2 scale, Vector2 adjustedCenter) {
        this.position = position;
        this.adjustedCenter = adjustedCenter;
        this.scale = scale;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, ImageLoader.getInstance().getGroundAssets().vines, position, adjustedCenter, scale);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.VINES_CENTER.x * 2 * scale.x; }
    @Override public final float getWidth() { return Constants.VINES_CENTER.y * 2 * scale.y; }
    @Override public final float getLeft() { return position.x - Constants.VINES_CENTER.x * scale.x; }
    @Override public final float getRight() { return position.x + Constants.VINES_CENTER.x * scale.x; }
    @Override public final float getTop() { return position.y + Constants.VINES_CENTER.y * scale.y; }
    @Override public final float getBottom() { return position.y - Constants.VINES_CENTER.y * scale.y; }
    @Override public Vines clone() { return new Vines(position, scale, adjustedCenter); }
}

package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class Vines implements Ground, Climbable{

    // fields
    private Vector2 position;

    // ctor
    public Vines(Vector2 position) {
        this.position = position;
    }

    public void render(SpriteBatch batch) {
        final TextureRegion region = Assets.getInstance().getVinesAssets().vines;
        Utils.drawTextureRegion(batch, region, position, Constants.VINES_CENTER);
    }

    public final Vector2 getPosition() { return position; }
    public final float getHeight() { return Constants.VINES_CENTER.y * 2; }
    public final float getWidth() { return Constants.VINES_CENTER.x * 2; }
    public final float getLeft() { return position.x - Constants.VINES_CENTER.x; }
    public final float getRight() { return position.x + Constants.VINES_CENTER.x; }
    public final float getTop() { return position.y + Constants.VINES_CENTER.y; }
    public final float getBottom() { return position.y - Constants.VINES_CENTER.y; }
    public final Class getSubclass() { return this.getClass(); }
}

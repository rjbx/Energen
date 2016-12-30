package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class Ladder implements Ground {

    // fields
    private Vector2 position;
    private float width;
    private float height;

    // ctor
    public Ladder(Vector2 position) {
        this.position = position;
        this.width = Constants.LADDER_CENTER.x * 2;
        this.height = Constants.LADDER_CENTER.y * 2;
    }

    public Ladder (float x, float y, float width, float height) {
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
    }


    public void render(SpriteBatch batch) {
        final TextureRegion region = Assets.getInstance().getLadderAssets().ladder;
        Utils.drawTextureRegion(batch, region, position, Constants.LADDER_CENTER);
    }

    public final Vector2 getPosition() { return position; }
    public final float getWidth() { return height; }
    public final float getHeight() { return width; }
    public final float getLeft() { return position.x - Constants.LADDER_CENTER.x; }
    public final float getRight() { return position.x + Constants.LADDER_CENTER.x; }
    public final float getTop() { return position.y + Constants.LADDER_CENTER.y; }
    public final float getBottom() { return position.y - Constants.LADDER_CENTER.y; }
    public final Class getSubclass() { return this.getClass(); }
}

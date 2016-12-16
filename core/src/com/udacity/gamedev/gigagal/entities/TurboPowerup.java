package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

// immutable
public final class TurboPowerup extends Powerup {

    private final Vector2 position;

    public TurboPowerup(Vector2 position) {
        this.position = position;
    }

    public void render(SpriteBatch batch) {
        final TextureRegion region = Assets.getInstance().getPowerupAssets().turboPowerup;
        Utils.drawTextureRegion(batch, region, position, Constants.POWERUP_CENTER);
    }

    public final Vector2 getPosition() { return position; }
    public final float getWidth() { return Constants.POWERUP_CENTER.x * 2; }
    public final float getHeight() { return Constants.POWERUP_CENTER.y * 2; }
    public final float getLeft() { return position.x - Constants.POWERUP_CENTER.x; }
    public final float getRight() { return position.x + Constants.POWERUP_CENTER.x; }
    public final float getTop() { return position.y + Constants.POWERUP_CENTER.y; }
    public final float getBottom() { return position.y - Constants.POWERUP_CENTER.y; }
    public final Class getSubclass() { return this.getClass(); }
}
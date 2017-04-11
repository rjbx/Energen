package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Helpers;

public final class HealthPowerup implements Powerup {

    // fields
    private final Vector2 position;

    // ctor
    public HealthPowerup(Vector2 position) {
        this.position = position;
    }

    @Override
    public void render(SpriteBatch batch) {
        final TextureRegion region = Assets.getInstance().getPowerupAssets().healthPowerup;
        Helpers.drawTextureRegion(batch, region, position, Constants.POWERUP_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getWidth() { return Constants.POWERUP_CENTER.x * 2; }
    @Override public final float getHeight() { return Constants.POWERUP_CENTER.y * 2; }
    @Override public final float getLeft() { return position.x - Constants.POWERUP_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.POWERUP_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.POWERUP_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.POWERUP_CENTER.y; }
}

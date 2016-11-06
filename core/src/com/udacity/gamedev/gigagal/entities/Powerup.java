package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

// immutable
public final class Powerup implements PhysicalEntity {

    // fields
    private final Vector2 position;

    // ctor
    public Powerup(Vector2 position) {
        this.position = position;
    }

    public void render(SpriteBatch batch) {
        final TextureRegion region = Assets.getInstance().getPowerupAssets().powerup;
        Utils.drawTextureRegion(batch, region, position, Constants.POWERUP_CENTER);
    }

    public final Vector2 getPosition() { return position; }
    public final float getWidth() { return position.x * 2; }
    public final float getHeight() { return position.y * 2; }
}

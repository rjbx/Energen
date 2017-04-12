package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.util.Helpers;

public final class Backdrop {

    // fields
    public static final String TAG = Backdrop.class.getName();

    // ctor
    public Backdrop() {
    }

    public void render(SpriteBatch batch, ExtendViewport viewport, TextureRegion region, Vector2 position, Vector2 offset) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        Helpers.drawTextureRegion(batch, region, position, offset, .375f);
        batch.end();
    }
}

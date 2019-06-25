package com.github.rjbx.energen.overlay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.Helpers;

public final class Backdrop {

    // fields
    public static final String TAG = Backdrop.class.getName();
    private final TextureRegion region;

    // ctor
    public Backdrop(TextureRegion region) {
        this.region = region;
    }

    public void render(SpriteBatch batch, Viewport viewport, Vector2 position, Vector2 offset, float scale) {
        Helpers.drawTextureRegion(batch, viewport, this.region, position, offset, scale);
    }
}

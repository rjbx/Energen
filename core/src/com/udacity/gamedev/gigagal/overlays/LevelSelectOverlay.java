package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entities.Cursor;
import com.udacity.gamedev.gigagal.util.Constants;

//immutable
public final class LevelSelectOverlay {

    // fields
    private ExtendViewport viewport;
    private final Cursor cursor;

    // ctor
    public LevelSelectOverlay() {
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        cursor = new Cursor(new Vector2(viewport.getWorldWidth() / 7, viewport.getWorldHeight() / 2.5f));
    }

    public void init() {
    }

    public void render(SpriteBatch batch) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        cursor.render(batch);

    }

    public ExtendViewport getViewport() { return viewport; }
}
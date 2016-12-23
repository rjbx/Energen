package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

import javafx.geometry.VerticalDirection;

//immutable
public final class CursorOverlay {

    // fields
    private ExtendViewport viewport;
    private float verticalPosition;

    // ctor
    public CursorOverlay() {
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        verticalPosition = 145;
    }

    public void init() {

    }

    public void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (verticalPosition >= 70) {
                verticalPosition -= 15;
            } else {
                verticalPosition = 145;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (verticalPosition <= 130) {
                verticalPosition += 15;
            } else {
                verticalPosition = 55;
            }
        }
    }

    public void render(SpriteBatch batch) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        Utils.drawTextureRegion(batch,
                Assets.getInstance().getOnscreenControlsAssets().selectionCursor,
                7.5f, verticalPosition);
    }

    public ExtendViewport getViewport() { return viewport; }
    public float getPosition() { return verticalPosition; }
}
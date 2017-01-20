package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.util.*;
import com.udacity.gamedev.gigagal.util.InputControls;

public class CursorOverlay {

    // fields
    private ExtendViewport viewport;
    private float yPosition;
    private float startingPosition;
    private float endingPosition;
    private InputControls inputControls;

    // ctor
    public CursorOverlay(float startingPosition, float endingPosition) {
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        yPosition = startingPosition;
        this.endingPosition = endingPosition;
        this.startingPosition = yPosition;
        inputControls = InputControls.getInstance();
    }

    public void init() {

    }

    public void update() {
        if (inputControls.downButtonJustPressed || inputControls.rightButtonJustPressed) {
            if (yPosition >= endingPosition + 15) {
                yPosition -= 15;
            } else {
                yPosition = startingPosition;
            }
        }
        if (inputControls.upButtonJustPressed || inputControls.leftButtonJustPressed) {
            if (yPosition <= startingPosition - 15) {
                yPosition += 15;
            } else {
                yPosition = endingPosition;
            }
        }
    }

    public void render(SpriteBatch batch) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        Utils.drawTextureRegion(batch,
                Assets.getInstance().getOverlayAssets().selectionCursor,
                viewport.getWorldWidth() / 3.5f, yPosition);
    }

    public ExtendViewport getViewport() { return viewport; }
    public float getPosition() { return yPosition; }
}
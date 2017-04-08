package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.util.*;
import com.udacity.gamedev.gigagal.app.InputControls;

public class CursorOverlay {

    // fields
    public final static String TAG = CursorOverlay.class.getName();
    private final SpriteBatch batch;
    private final ExtendViewport viewport;
    private Enums.Orientation orientation;
    private float position;
    private float startingPosition;
    private float endingPosition;
    private InputControls inputControls;

    // ctor
    public CursorOverlay(float startingPosition, float endingPosition, Enums.Orientation orientation) {
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        this.batch = new SpriteBatch();
        this.orientation = orientation;
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;
        position = this.startingPosition;
        inputControls = InputControls.getInstance();
    }

    public void update() {
        if (orientation == Enums.Orientation.X) {
            if (inputControls.downButtonJustPressed || inputControls.rightButtonJustPressed || inputControls.upButtonJustPressed || inputControls.leftButtonJustPressed) {
                if (position == endingPosition) {
                    position = startingPosition;
                } else {
                    position = endingPosition;
                }
            }
        } else if (orientation == Enums.Orientation.Y) {
            if (inputControls.downButtonJustPressed || inputControls.rightButtonJustPressed) {
                if (position >= endingPosition + 15) {
                    position -= 15;
                } else {
                    position = startingPosition;
                }
            }
            if (inputControls.upButtonJustPressed || inputControls.leftButtonJustPressed) {
                if (position <= startingPosition - 15) {
                    position += 15;
                } else {
                    position = endingPosition;
                }
            }
        }
    }

    public void render() {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        if (orientation == Enums.Orientation.X) {
            Utils.drawTextureRegion(batch, Assets.getInstance().getOverlayAssets().selectionCursor, position, viewport.getWorldHeight() / 3);
        } else {
            Utils.drawTextureRegion(batch, Assets.getInstance().getOverlayAssets().selectionCursor, viewport.getWorldWidth() / 4, position);
        }
        batch.end();
    }

    public void dispose() { batch.dispose(); }
    public ExtendViewport getViewport() { return viewport; }
    public float getPosition() { return position; }
}
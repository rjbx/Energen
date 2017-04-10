package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.util.*;
import com.udacity.gamedev.gigagal.app.InputControls;

import java.util.ArrayList;
import java.util.ListIterator;

public class CursorOverlay {

    // fields
    public final static String TAG = CursorOverlay.class.getName();
    private final ExtendViewport viewport; // class-level instantiation
    private Enums.Orientation orientation;
    private float startingPosition;
    private float endingPosition;
    private static InputControls inputControls;
    private ListIterator<String> iterator;
    private float position;

    // ctor
    public CursorOverlay(float startingPosition, float endingPosition, Enums.Orientation orientation) {
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
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
                if (position == endingPosition) {
                    position = startingPosition;
                    if (iterator != null) {
                        while (iterator.hasPrevious()) {
                            iterator.previous();
                        }
                    }
                } else if (position >= endingPosition + 15) {
                    position -= 15;
                    if (iterator != null) {
                        iterator.next();
                    }
                } else {
                    position = endingPosition;
                }
            } else if (inputControls.upButtonJustPressed || inputControls.leftButtonJustPressed) {
                if (position == startingPosition) {
                    position = endingPosition;
                    if (iterator != null) {
                        while (iterator.hasNext()) {
                            iterator.next();
                        }
                    }
                } else if (position <= startingPosition - 15) {
                    position += 15;
                    if (iterator != null) {
                        iterator.previous();
                    }
                } else {
                    position = startingPosition;
                }
            }
        }
    }

    public void render(SpriteBatch batch, ExtendViewport viewport) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        if (orientation == Enums.Orientation.X) {
            Utils.drawTextureRegion(batch, Assets.getInstance().getOverlayAssets().selectionCursor, position, viewport.getWorldHeight() / 3);
        } else {
            Utils.drawTextureRegion(batch, Assets.getInstance().getOverlayAssets().selectionCursor, viewport.getWorldWidth() / 4, position);
        }
    }

    public ExtendViewport getViewport() { return viewport; }
    public float getPosition() { return position; }
    public float getStartingPosition() { return startingPosition; }
    public Enums.Orientation getOrientation() { return orientation; }
    public void setRange(float start, float end) { this.startingPosition = start; this.endingPosition = end; }
    public void setOrientation(Enums.Orientation o) { this.orientation = o; }
    public void resetPosition() { position = startingPosition; }
    public ListIterator<String> getIterator() { return iterator; }
    public void setIterator(ArrayList<String> optionStrings) {
        this.iterator = optionStrings.listIterator();
        iterator.next();
    }
}
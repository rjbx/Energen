package com.github.rjbx.energen.overlay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.*;
import com.github.rjbx.energen.util.InputControls;

import java.util.ListIterator;

// singleton
public class Cursor {

    // fields
    public static final String TAG = Cursor.class.getName();
    private static final Cursor INSTANCE = new Cursor();
    private Enums.Orientation orientation;
    private float startingPosition;
    private float endingPosition;
    private static InputControls inputControls;
    private ListIterator<String> iterator;
    private float position;

    // cannot be subclassed
    private Cursor() {}

    // static factory method
    public static Cursor getInstance() { return INSTANCE; }

    public void create() {
        this.orientation = Enums.Orientation.Y;
        this.startingPosition = 0;
        this.endingPosition = 0;
        position = this.startingPosition;
        inputControls = InputControls.getInstance();
    }

    public void update() {
        if (orientation == Enums.Orientation.X) {
            if (startingPosition == position) {
                position = startingPosition - 25;
            }
            if (inputControls.downButtonJustPressed || inputControls.rightButtonJustPressed || inputControls.upButtonJustPressed || inputControls.leftButtonJustPressed) {
                if (position == endingPosition - 25) {
                    position = startingPosition - 25;
                } else {
                    position = endingPosition - 25;
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
                        // TODO: Resolve NoSuchElementException
                        iterator.previous();
                    }
                } else {
                    position = startingPosition;
                }
            }
        }
    }

    public void render(SpriteBatch batch, ExtendViewport viewport) {
        if (orientation == Enums.Orientation.X) {
            Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getOverlayAssets().selectionCursor, position, viewport.getCamera().position.y);
        } else {
            Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getOverlayAssets().selectionCursor, viewport.getCamera().position.x - viewport.getWorldWidth() / 4, position);
        }
    }

    public float getPosition() { return position; }
    public float getStartingPosition() { return startingPosition; }
    public Enums.Orientation getOrientation() { return orientation; }
    public void setRange(float start, float end) { this.startingPosition = start; this.endingPosition = end; }
    public void setOrientation(Enums.Orientation o) { this.orientation = o; }
    public void resetPosition() { position = startingPosition; }
    public ListIterator<String> getIterator() { return iterator; }
    public void setIterator(ListIterator<String> iterator) {
        if (iterator == null) {
            this.iterator = null;
        } else {
            this.iterator = iterator;
            iterator.next();
        }
    }
}
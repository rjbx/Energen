package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

// mutable
public class Box implements SolidGround {

    // fields
    private final Enums.LevelName type;
    private final Vector2 position; // class-level instantiation
    private final float top;
    private final float bottom;
    private final float left;
    private final float right;
    private final float width;
    private final float height;
    private final NinePatch ninePatch;

    // ctor
    public Box(Rectangle shape, Enums.LevelName type) {
        this.width = shape.getWidth();
        this.height = shape.getHeight();
        this.top = shape.getY() + height;
        this.bottom = shape.getY();
        this.left = shape.getX();
        this.right = shape.getX() + width;
        this.position = new Vector2(left + (width / 2), bottom + (height / 2));
        this.type = type;
        switch (type) {
            case GRAVITATIONAL:
                ninePatch = Assets.getInstance().getBoxAssets().redBox;
                break;
            case MECHANICAL:
                ninePatch = Assets.getInstance().getBoxAssets().greyBox;
                break;
            case ELECTROMAGNETIC:
                ninePatch = Assets.getInstance().getBoxAssets().blackBox;
                break;
            case THERMAL:
                ninePatch = Assets.getInstance().getBoxAssets().yellowBox;
                break;
            case NUCLEAR:
                ninePatch = Assets.getInstance().getBoxAssets().blueBox;
                break;
            case MYSTERIOUS:
                ninePatch = Assets.getInstance().getBoxAssets().clearBox;
                break;
            case FINAL:
                ninePatch = Assets.getInstance().getBoxAssets().magentaBox;
                break;
            default:
                ninePatch = Assets.getInstance().getBoxAssets().defaultBox;
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawNinePatch(batch, viewport, ninePatch, left - 1, bottom - 1, width + 2, height + 2);
    }

    // Getters
    @Override public float getTop() { return top; }
    @Override public float getBottom() {return bottom; }
    @Override public float getLeft() { return left; }
    @Override public float getRight() { return right; }
    @Override public float getWidth() { return width;}
    @Override public float getHeight() {return height; }
    @Override public Vector2 getPosition() { return position; }
}

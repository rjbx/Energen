package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Enums;

// mutable
public class Box implements SolidGround {

    // fields
    private final Enums.LevelName type;
    private final Vector2 position;
    private final float top;
    private final float bottom;
    private final float left;
    private final float right;
    private final float width;
    private final float height;

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
    }

    @Override
    public void render(SpriteBatch batch) {
        switch (type) {
            case hi:
                Assets.getInstance().getBoxAssets().redBox.draw(batch, left - 1, bottom - 1, width + 2, height + 2);
                break;
            case MECHANICAL:
                Assets.getInstance().getBoxAssets().greyBox.draw(batch, left - 1, bottom - 1, width + 2, height + 2);
                break;
            case ELECTROMAGNETIC:
                Assets.getInstance().getBoxAssets().blackBox.draw(batch, left - 1, bottom - 1, width + 2, height + 2);
                break;
            case THERMAL:
                Assets.getInstance().getBoxAssets().yellowBox.draw(batch, left - 1, bottom - 1, width + 2, height + 2);
                break;
            case NUCLEAR:
                Assets.getInstance().getBoxAssets().blueBox.draw(batch, left - 1, bottom - 1, width + 2, height + 2);
                break;
            case MYSTERIOUS:
                Assets.getInstance().getBoxAssets().clearBox.draw(batch, left - 1, bottom - 1, width + 2, height + 2);
                break;
            case FINAL:
                Assets.getInstance().getBoxAssets().magentaBox.draw(batch, left - 1, bottom - 1, width + 2, height + 2);
                break;
            default:
                Assets.getInstance().getBoxAssets().defaultBox.draw(batch, left - 1, bottom - 1, width + 2, height + 2);
        }
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

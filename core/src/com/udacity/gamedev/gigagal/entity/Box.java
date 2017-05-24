package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

// mutable
public class Box implements Dense, Ground {

    // fields
    private final Enums.Material type;
    private final Vector2 position; // class-level instantiation
    private final float top;
    private final float bottom;
    private final float left;
    private final float right;
    private final float width;
    private final float height;
    private boolean ledge;
    private NinePatch ninePatch;

    //default ctor
    public Box() {
        this.width = 0;
        this.height = 0;
        this.top = 0;
        this.bottom = 0;
        this.left = 0;
        this.right = 0;
        this.position = new Vector2();
        this.type = Enums.Material.NATIVE;
        ninePatch = Assets.getInstance().getGroundAssets().getNinePatch(this);
        ninePatch.setColor(type.theme().color());
    }

    // ctor
    public Box(float xPos, float yPos, float width, float height, Enums.Material type) {
        this.width = width;
        this.height = height;
        this.top = yPos + height;
        this.bottom = yPos;
        this.left = xPos;
        this.right = xPos + width;
        this.position = new Vector2(left + (width / 2), bottom + (height / 2));
        this.type = type;
        ninePatch = Assets.getInstance().getGroundAssets().getNinePatch(this);
        ninePatch.setColor(type.theme().color());
        ledge = false;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawNinePatch(batch, viewport, ninePatch, left, bottom, width, height);
    }

    // Getters
    public void setLedge(boolean state) { ledge = state; }
    public boolean isLedge() { return ledge || getHeight() < Constants.MAX_LEDGE_HEIGHT; }
    public Enums.Material getType() { return type; }
    @Override public float getTop() { return top; }
    @Override public float getBottom() {return bottom; }
    @Override public float getLeft() { return left; }
    @Override public float getRight() { return right; }
    @Override public float getWidth() { return width;}
    @Override public float getHeight() {return height; }
    @Override public Vector2 getPosition() { return position; }
    @Override public Box clone() { return new Box(left, bottom, width, height, type); }
}

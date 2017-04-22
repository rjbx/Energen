package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

// mutable
public class BreakableBox implements Destructible, Solid, Ground, Hazard {

    // fields
    private final Enums.Material type;
    private final Vector2 position; // class-level instantiation
    private final float top;
    private final float bottom;
    private final float left;
    private final float right;
    private final float width;
    private final float height;
    private float damage;
    private boolean active;
    private final NinePatch ninePatch;

    // ctor
    public BreakableBox(Rectangle shape, Enums.Material type) {
        active = true;
        damage = 50;
        this.width = shape.getWidth();
        this.height = shape.getHeight();
        this.top = shape.getY() + height;
        this.bottom = shape.getY();
        this.left = shape.getX();
        this.right = shape.getX() + width;
        this.position = new Vector2(left + (width / 2), bottom + (height / 2));
        this.type = type;
        ninePatch = Assets.getInstance().getBoxAssets().breakableBox;
        ninePatch.setColor(type.theme().color());
    }

    @Override
    public void update(float delta) {
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
    @Override public int getKillScore() { return 0; }
    @Override public int getHitScore() { return 0; }
    @Override public int getDamage() { return 0; }
    @Override public float getShotRadius() { return 29; }
    @Override public void setHealth(float damage) { this.damage = damage; }
    @Override public float getHealth() { return damage; }
    @Override public Vector2 getKnockback() { return Vector2.Zero; }
    @Override public Enums.Material getType() { return type; }
    public boolean isActive() { return active; }
    public void deactivate() { this.active = false; }
}

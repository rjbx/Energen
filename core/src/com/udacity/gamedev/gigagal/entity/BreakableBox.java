package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

// mutable
public class BreakableBox extends Box implements Destructible {

    // fields
    private float damage;
    private boolean active;

    // ctor
    public BreakableBox(float xPos, float yPos, float width, float height, Enums.Material type) {
        super(xPos, yPos, width, height, type);
        active = true;
        damage = 50;
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        super.render(batch, viewport);
    }

    // Getters
    @Override public float getTop() { return super.getTop(); }
    @Override public float getBottom() {return super.getBottom(); }
    @Override public float getLeft() { return super.getLeft(); }
    @Override public float getRight() { return super.getRight(); }
    @Override public float getWidth() { return super.getWidth();}
    @Override public float getHeight() {return super.getHeight(); }
    @Override public Vector2 getPosition() { return super.getPosition(); }
    @Override public int getKillScore() { return 0; }
    @Override public int getHitScore() { return 0; }
    @Override public float getShotRadius() { return Math.min(getWidth(), getHeight()) / 2; }
    @Override public void setHealth(float damage) { this.damage = damage; }
    @Override public float getHealth() { return damage; }
    @Override public Enums.Material getType() { return super.getType(); }
    @Override public Box clone() { return super.clone(); }
}

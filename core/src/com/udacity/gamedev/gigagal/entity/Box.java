package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Enums;

// mutable
public class Box extends Barrier implements Destructible {

    // fields
    public final static String TAG = Box.class.getName();

    private float damage;

    // ctor
    public Box(float xPos, float yPos, float width, float height, Enums.Material type, boolean dense) {
        super(xPos, yPos, width, height, type, dense);
        damage = 50;
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        super.render(batch, viewport);
    }

    // Getters
    @Override public int getKillScore() { return 0; }
    @Override public int getHitScore() { return 0; }
    @Override public float getShotRadius() { return Math.min(getWidth(), getHeight()) / 2; }
    @Override public void setHealth(float damage) { this.damage = damage; }
    @Override public float getHealth() { return damage; }
    @Override public Enums.Material getType() { return super.getType(); }
}

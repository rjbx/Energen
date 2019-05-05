package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.Enums;

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

    // Getters
    @Override public int getKillScore() { return 0; }
    @Override public int getHitScore() { return 0; }
    @Override public float getShotRadius() { return Math.min(getWidth(), getHeight()) / 2; }
    @Override public void setHealth(float damage) { this.damage = damage; }
    @Override public float getHealth() { return damage; }
}

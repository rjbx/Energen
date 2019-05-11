package com.github.rjbx.energen.entity;

import com.github.rjbx.energen.util.Enums;

// TODO: Add bounds attribute for ledge switching when all activators in area are tripped
// mutable
public class Box extends Barrier implements Destructible {

    // fields
    public final static String TAG = Box.class.getName();

    private float damage;

    // ctor
    public Box(float xPos, float yPos, float width, float height, Enums.Energy type) {
        super(xPos, yPos, width, height, type, false, false);
        damage = 50;
    }

    // Getters
    @Override public int getKillScore() { return 0; }
    @Override public int getHitScore() { return 0; }
    @Override public float getShotRadius() { return Math.min(getWidth(), getHeight()) / 2; }
    @Override public void setHealth(float damage) { this.damage = damage; }
    @Override public float getHealth() { return damage; }
}

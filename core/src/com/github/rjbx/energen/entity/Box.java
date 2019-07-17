package com.github.rjbx.energen.entity;

import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;

// TODO: Add bounds attribute for ledge switching when all activators in area are tripped
// mutable
public class Box extends Barrier implements Destructible {

    // fields
    public final static String TAG = Box.class.getName();

    private float damage;

    // ctor
    public Box(float xPos, float yPos, float width, float height, Enums.Energy type) {
        super(xPos, yPos, width, height, type, true, false);
        damage = 50;
    }

    public Box safeClone() {
        Box clone = new Box(position.x, position.y, getWidth(), getHeight(), getType());
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    // Getters
    @Override public int getKillScore() { return 0; }
    @Override public int getHitScore() { return 0; }
    @Override public float getShotRadius() { return Math.min(getWidth(), getHeight()) / 2; }
    @Override public void setHealth(float damage) { this.damage = damage; }
    @Override public float getHealth() { return damage; }

    @Override
    public int getPriority() { return Constants.PRIORITY_OVERRIDE; }
}

package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Enums;

// mutable
public class Box extends Block implements Destructible {

    // fields
    public final static String TAG = Box.class.getName();

    private float damage;
    private int hashCode;

    // ctor
    public Box(float xPos, float yPos, float width, float height, Enums.Material type, boolean dense) {
        super(xPos, yPos, width, height, type, dense);
        hashCode = hashCode();
        damage = 50;
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        super.render(batch, viewport);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Box) {
            Box box = (Box) object;
            return getTop() == box.getTop() && getBottom() == box.getBottom() && getLeft() == box.getLeft() && getRight() == box.getRight();
        }
        return false;
    }
    @Override public int hashCode() { return hashCode; }
    public void setHashCode(int hashCode) { this.hashCode = hashCode; }

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
    @Override public Box clone() { Box clone = new Box(getLeft(), getBottom(), getWidth(), getHeight(), getType(), super.isDense()); clone.setHealth(damage); clone.setHashCode(hashCode); return clone; }
}

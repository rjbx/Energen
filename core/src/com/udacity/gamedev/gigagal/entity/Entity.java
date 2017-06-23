package com.udacity.gamedev.gigagal.entity;
import com.badlogic.gdx.math.Vector2;

public class Entity implements Physical {

    private int hashCode;
    private Vector2 position;
    private Vector2 center;

    // default ctor
    private Entity() {}

    public Entity(Vector2 position) {
        this.position = position;
        this.center = new Vector2();
        this.hashCode = hashCode();
    }

    public final Vector2 getPosition() { return position; }
    public final Vector2 getCenter() { return center; }
    public final float getWidth() { return center.x * 2; }
    public final float getHeight() { return center.y * 2; }
    public final float getLeft() { return position.x - center.x; }
    public final float getRight() { return position.x + center.x; }
    public final float getTop() { return position.y + center.y; }
    public final float getBottom() { return position.y - center.y; }
    public boolean equals(Object object) { return this.hashCode() == object.hashCode(); }
    public int hashCode() { return hashCode; }

    public void setHashCode(int hashCode) { this.hashCode = hashCode; }
    public final void setCenter(Vector2 center) { this.center.set(center); }
}

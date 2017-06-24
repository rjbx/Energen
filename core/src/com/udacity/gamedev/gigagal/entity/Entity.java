package com.udacity.gamedev.gigagal.entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity implements Physical, Cloneable {

    public static final String TAG = Entity.class.toString();

    // default ctor
    public Entity() {}


    public abstract Vector2 getPosition();
    public abstract float getWidth();
    public abstract float getHeight();
    public abstract float getLeft();
    public abstract float getRight();
    public abstract float getTop();
    public abstract float getBottom();
    @Override public Entity clone() {
        try {
            Gdx.app.log(TAG, super.clone().equals(this) + "");
            return (Entity) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
}

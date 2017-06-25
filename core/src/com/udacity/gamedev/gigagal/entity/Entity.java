package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.Gdx;

public abstract class Entity implements Physical, Visible, Cloneable {

    public static final String TAG = Entity.class.toString();

    private int hashCode;

    // default ctor
    public Entity() { hashCode = this.hashCode(); }

    @Override public int hashCode() { return hashCode; }
    @Override public boolean equals(Object object) {
        return this.hashCode() == object.hashCode();
    }
    @Override public Entity clone() {
        try {
            Gdx.app.log(TAG, super.clone().equals(this) + "");
            Entity clone = (Entity) super.clone();
            clone.hashCode = hashCode;
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
}
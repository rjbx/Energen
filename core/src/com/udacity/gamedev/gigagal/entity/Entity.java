package com.udacity.gamedev.gigagal.entity;

public abstract class Entity implements Physical, Visible, Cloneable {

    public static final String TAG = Entity.class.toString();

    private int cloneHashCode;

    // default ctor
    public Entity() { cloneHashCode = hashCode(); }

    @Override public boolean equals(Object object) {
        if (object instanceof Entity) {
            return this.cloneHashCode == ((Entity) object).cloneHashCode;
        }
        return false;
    }
    @Override public Entity clone() {
        try {
           // Gdx.app.log(TAG, super.clone().equals(this) + "");
            Entity clone = (Entity) super.clone();
            clone.cloneHashCode = this.hashCode();
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
    public int cloneHashCode() { return cloneHashCode; }
}
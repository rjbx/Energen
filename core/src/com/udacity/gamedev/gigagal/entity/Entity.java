package com.udacity.gamedev.gigagal.entity;

public abstract class Entity implements Physical, Visible, Cloneable {

    public static final String TAG = Entity.class.toString();

    private int cloneHashCode;

    // default ctor
    public Entity() { cloneHashCode = hashCode(); }

    @Override public final boolean equals(Object object) {
        if (object instanceof Entity) {
            return this.cloneHashCode == ((Entity) object).cloneHashCode;
        }
        return false;
    }
    @Override public final Entity clone() {
        try {
            Entity clone = (Entity) super.clone();
            clone.cloneHashCode = this.hashCode();
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
    protected final int cloneHashCode() { return cloneHashCode; }
}
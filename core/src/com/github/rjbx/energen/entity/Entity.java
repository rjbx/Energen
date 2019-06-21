package com.github.rjbx.energen.entity;

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

    public abstract Entity safeClone();
    protected final int cloneHashCode() { return cloneHashCode; }
}
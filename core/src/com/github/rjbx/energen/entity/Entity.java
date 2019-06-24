package com.github.rjbx.energen.entity;

public abstract class Entity implements Physical, Visible, Cloneable {

    public static final String TAG = Entity.class.toString();

    private int cloneHashCode;

    // TODO: Add field for determining sort order so as to replace multiple passes through
    //  the same loops with a single entity loop

    // default ctor
    public Entity() { cloneHashCode = hashCode(); }

    @Override public final boolean equals(Object object) {
        if (object instanceof Entity) {
            return this.cloneHashCode == ((Entity) object).cloneHashCode;
        }
        return false;
    }

    public abstract Entity safeClone();
    protected final void setClonedHashCode(int hashCode) { this.cloneHashCode = hashCode; }
}
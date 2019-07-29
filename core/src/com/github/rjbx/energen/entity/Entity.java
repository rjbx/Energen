package com.github.rjbx.energen.entity;

public abstract class Entity implements Physical, Visible, Cloneable, Sortable {

    public static final String TAG = Entity.class.toString();

    private int cloneHashCode;
    private int id;

    // default ctor
    public Entity() { cloneHashCode = hashCode(); }

    @Override public final boolean equals(Object object) {
        if (object instanceof Entity) {
            return this.id == ((Entity) object).id;
        }
        return false;
    }

    public abstract Entity safeClone();
    protected final void setClonedHashCode(int hashCode) { this.cloneHashCode = hashCode; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    //    public int getRenderPriority() { return renderPriority; }
//    public void setRenderPriority(int renderPriority) { this.renderPriority = renderPriority; }
//    public int getUpdatePriority() { return updatePriority; }
//    public void setUpdatePriority(int updatePriority) { this.updatePriority = updatePriority; }
}
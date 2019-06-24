package com.github.rjbx.energen.entity;

public abstract class Entity implements Physical, Visible, Cloneable {

    public static final String TAG = Entity.class.toString();

//    private int cloneHashCode;
//    private int renderPriority;
//    private int updatePriority;

    // TODO: Add field for determining sort priority so as to eliminate need for
    //  multiple and/or distinct passes through subtype loops due to logic ordering
    //  with one pass through the presorted global entity loop
    //
    //  Considerations:
    //   -Distinction by subtype is currently handled seamlessly by direct load into subtype lists
    //   -Alternative to fields is to direct load into lists filtering subtype by condition
    //   -Further need for distinction applies to one additional ground loop for each render and update call


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

//    public int getRenderPriority() { return renderPriority; }
//    public void setRenderPriority(int renderPriority) { this.renderPriority = renderPriority; }
//    public int getUpdatePriority() { return updatePriority; }
//    public void setUpdatePriority(int updatePriority) { this.updatePriority = updatePriority; }
}
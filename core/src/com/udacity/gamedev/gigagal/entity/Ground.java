package com.udacity.gamedev.gigagal.entity;

public abstract class Ground extends Entity implements Physical, Visible {

    public abstract boolean isDense();

    public boolean equals(Object object) {
        if (object instanceof Ground) {
            Ground ground = (Ground) object;
            return this.getLeft() == ground.getLeft() && this.getRight() == ground.getRight() && this.getBottom() == ground.getBottom() && this.getTop() == ground.getTop();
        }
        return false;
    }
}
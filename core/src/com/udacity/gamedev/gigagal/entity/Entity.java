package com.udacity.gamedev.gigagal.entity;

public class Entity {

    private int hashCode;

    @Override public boolean equals(Object object) { return this.hashCode() == object.hashCode(); }
    @Override public int hashCode() { return hashCode; }
    public void setHashCode(int hashCode) { this.hashCode = hashCode; }
}

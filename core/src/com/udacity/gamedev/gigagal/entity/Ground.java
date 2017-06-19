package com.udacity.gamedev.gigagal.entity;

public interface Ground extends Physical, Visible {

    @Override boolean equals(Object object);
    Ground clone();
    boolean isDense();
}
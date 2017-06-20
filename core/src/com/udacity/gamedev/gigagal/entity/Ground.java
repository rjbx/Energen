package com.udacity.gamedev.gigagal.entity;

public interface Ground extends Physical, Visible {

    boolean equals(Object object);
    Ground clone();
    boolean isDense();
}
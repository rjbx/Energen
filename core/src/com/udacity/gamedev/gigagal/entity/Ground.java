package com.udacity.gamedev.gigagal.entity;

public interface Ground extends Physical, Visible {
    Ground clone();
    boolean isDense();
}

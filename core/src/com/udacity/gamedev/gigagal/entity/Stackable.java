package com.udacity.gamedev.gigagal.entity;

public interface Stackable {
    boolean isBeneatheGround();
    Ground getTopGround();
}

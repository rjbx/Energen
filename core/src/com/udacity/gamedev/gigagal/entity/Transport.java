package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.math.Vector2;

public interface Transport extends Physical, Visible {

    Vector2 getDestination();
}
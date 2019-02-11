package com.udacity.gamedev.gigagal.entity;

import com.udacity.gamedev.gigagal.util.Enums;

public interface Destructible extends Strikeable {

    Enums.Material getType();
    float getShotRadius();
    float getHealth();
    void setHealth(float health);
    void update(float delta);
    int getHitScore();
    int getKillScore();
}
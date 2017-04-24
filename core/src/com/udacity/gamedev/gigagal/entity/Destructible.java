package com.udacity.gamedev.gigagal.entity;

import com.udacity.gamedev.gigagal.util.Enums;

public interface Destructible extends Entity {

    Enums.Material getType();
    float getHealth();
    void setHealth(float health);
    void update(float delta);
    float getShotRadius();
    int getHitScore();
    int getKillScore();
}
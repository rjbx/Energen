package com.github.rjbx.energen.entity;

import com.github.rjbx.energen.util.Enums;

public interface Destructible extends Strikeable {

    Enums.Material getType();
    float getShotRadius();
    float getHealth();
    void setHealth(float health);
    void update(float delta);
    int getHitScore();
    int getKillScore();
}
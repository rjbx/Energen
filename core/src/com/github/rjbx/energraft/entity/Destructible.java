package com.github.rjbx.energraft.entity;

import com.github.rjbx.energraft.util.Enums;

public interface Destructible extends Strikeable {

    Enums.Material getType();
    float getShotRadius();
    float getHealth();
    void setHealth(float health);
    void update(float delta);
    int getHitScore();
    int getKillScore();
}
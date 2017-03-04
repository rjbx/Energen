package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.udacity.gamedev.gigagal.util.Enums;

public interface DestructibleHazard extends Hazard {

    Enums.WeaponType getType();
    float getHealth();
    void setHealth(float health);
    void update(float delta);
    float getShotRadius();
    int getHitScore();
    int getKillScore();
}

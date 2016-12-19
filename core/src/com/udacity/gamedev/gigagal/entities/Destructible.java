package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.udacity.gamedev.gigagal.util.Enums;

public abstract class Destructible extends Hazard {

    abstract public Enums.Weapon getType();
    abstract public int getHealth();
    abstract public void setHealth(int health);
    abstract public void update(float delta);
    abstract public void render(SpriteBatch batch);
    abstract public float getShotRadius();
    abstract public int getHitScore();
    abstract public int getKillScore();
}

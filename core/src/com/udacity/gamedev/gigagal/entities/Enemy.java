package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Enemy implements PhysicalEntity {

    abstract public int getHealth();
    abstract public void setHealth(int health);
    abstract public void update(float delta);
    abstract public void render(SpriteBatch batch);
    abstract public float getShotRadius();
    abstract public int getHitScore();
    abstract public Class getSubclass();
}

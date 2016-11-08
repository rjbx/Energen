package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Enemy implements PhysicalEntity {

    abstract int getHealth();
    abstract void setHealth(int health);
    abstract void update(float delta);
    abstract void render(SpriteBatch batch);
}

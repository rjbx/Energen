package com.udacity.gamedev.gigagal.entities;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Hazard implements Physical {

    abstract public void render(SpriteBatch batch);
    abstract public int getDamage();
    abstract public Class getSubclass();
}

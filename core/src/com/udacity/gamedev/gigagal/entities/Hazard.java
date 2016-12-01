package com.udacity.gamedev.gigagal.entities;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Hazard implements Physical {

    abstract public void render(SpriteBatch batch);
    abstract public int getDamage();
    abstract public Vector2 getKnockback();
    abstract public Class getSubclass();
}

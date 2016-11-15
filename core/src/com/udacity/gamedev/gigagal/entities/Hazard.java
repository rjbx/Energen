package com.udacity.gamedev.gigagal.entities;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Hazard implements PhysicalEntity {

    abstract public void render(SpriteBatch batch);
}

package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// immutable
public interface Powerup extends Entity {

    void render(SpriteBatch batch);
}

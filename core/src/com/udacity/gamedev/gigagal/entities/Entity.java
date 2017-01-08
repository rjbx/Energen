package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public interface Entity {

    void render(SpriteBatch batch);
    Vector2 getPosition();
    float getWidth();
    float getHeight();
    float getLeft();
    float getRight();
    float getTop();
    float getBottom();
}

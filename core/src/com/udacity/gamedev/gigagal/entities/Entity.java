package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public interface Entity {

    void render(SpriteBatch batch, Viewport viewport);
    Vector2 getPosition();
    float getWidth();
    float getHeight();
    float getLeft();
    float getRight();
    float getTop();
    float getBottom();
}

package com.github.rjbx.energraft.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public interface Physical {

    Vector2 getPosition();
    float getWidth();
    float getHeight();
    float getLeft();
    float getRight();
    float getTop();
    float getBottom();
    // Rectangle getBounds();
}

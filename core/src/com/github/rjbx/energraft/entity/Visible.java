package com.github.rjbx.energage.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public interface Visible {

    void render(SpriteBatch batch, Viewport viewport);
}

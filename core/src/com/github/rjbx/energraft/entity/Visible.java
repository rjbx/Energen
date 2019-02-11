package com.github.rjbx.energraft.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public interface Visible {

    void render(SpriteBatch batch, Viewport viewport);
}

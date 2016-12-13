package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class Cursor {


    // fields
    private Vector2 position;

    // ctor
    public Cursor(Vector2 position) {
        this.position = position;
    }

    public void render(SpriteBatch batch) {
    }

    public final Vector2 getPosition() { return position; }
}

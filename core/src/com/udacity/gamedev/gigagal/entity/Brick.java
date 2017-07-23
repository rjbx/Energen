package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Enums;

public class Brick extends Block implements Tossable {

    // fields
    public final static String TAG = Block.class.getName();

    // ctor
    public Brick(float xPos, float yPos, float width, float height, Enums.Material type, boolean dense) {
        super(xPos, yPos, Math.min(width, 35), Math.min(height, 20), type, dense);
        velocity = new Vector2(0, 0);
    }

    @Override public final void toss(float velocityX) { velocity.x = velocityX; }
}

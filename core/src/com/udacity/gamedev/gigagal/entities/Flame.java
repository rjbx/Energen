package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class Flame extends Indestructible {

    // fields
    private Vector2 position;
    long startTime;

    // ctor
    public Flame(Vector2 position) {
        this.position = position;
        startTime = TimeUtils.nanoTime();
    }

    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region = Assets.getInstance().getFlameAssets().flame.getKeyFrame(elapsedTime, true);
        Utils.drawTextureRegion(batch, region, position, Constants.FLAME_CENTER);
    }

    public final Vector2 getPosition() { return position; }
    public final float getWidth() { return Constants.FLAME_COLLISION_WIDTH; }
    public final float getHeight() { return Constants.FLAME_COLLISION_HEIGHT; }
    public final float getLeft() { return position.x - Constants.FLAME_CENTER.x; }
    public final float getRight() { return position.x + Constants.FLAME_CENTER.x; }
    public final float getTop() { return position.y + Constants.FLAME_CENTER.y; }
    public final float getBottom() { return position.y - Constants.FLAME_CENTER.y; }
    public final int getDamage() { return Constants.FLAME_DAMAGE; }
    public final Class getSubclass() { return this.getClass(); }
}

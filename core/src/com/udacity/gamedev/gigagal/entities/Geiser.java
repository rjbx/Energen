package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class Geiser extends Indestructible {

    // fields
    private Vector2 position;
    private long startTime;

    // ctor
    public Geiser(Vector2 position) {
        this.position = position;
        startTime = TimeUtils.nanoTime();
    }

    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region = Assets.getInstance().getGeiserAssets().geiser.getKeyFrame(elapsedTime, true);
        Utils.drawTextureRegion(batch, region, position, Constants.GEISER_CENTER);
    }

    public final Vector2 getPosition() { return position; }
    public final float getWidth() { return Constants.GEISER_COLLISION_WIDTH; }
    public final float getHeight() { return Constants.GEISER_COLLISION_HEIGHT; }
    public final float getLeft() { return position.x - Constants.GEISER_CENTER.x; }
    public final float getRight() { return position.x + Constants.GEISER_CENTER.x; }
    public final float getTop() { return position.y + Constants.GEISER_CENTER.y; }
    public final float getBottom() { return position.y - Constants.GEISER_CENTER.y; }
    public final int getDamage() { return Constants.GEISER_DAMAGE; }
    public final Class getSubclass() { return this.getClass(); }
}

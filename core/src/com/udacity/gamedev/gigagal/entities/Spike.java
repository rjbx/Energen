package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

public class Spike extends Enemy {

    // fields
    private final Platform platform;
    private final Vector2 position;
    private int health;

    // ctor
    public Spike(Platform platform) {
        this.platform = platform;
        position = new Vector2(platform.getLeft(), platform.getTop() + Constants.SPIKE_CENTER.y);
        health = Constants.SPIKE_MAX_HEALTH;
    }

    // static render
    public void update(float delta) {
        position.y = platform.getTop() + Constants.SPIKE_CENTER.y;
    }

    public void render(SpriteBatch batch) {
        final TextureRegion region = Assets.getInstance().getSpikeAssets().spike;
        Utils.drawTextureRegion(batch, region, position, Constants.SPIKE_CENTER);
    }

    public final Vector2 getPosition() { return position; }
    public final int getHealth() { return health; }
    public final float getWidth() { return Constants.SPIKE_COLLISION_WIDTH; }
    public final float getHeight() { return Constants.SPIKE_COLLISION_HEIGHT; }
    public final float getLeft() { return position.x - Constants.SPIKE_CENTER.x; }
    public final float getRight() { return position.x + Constants.SPIKE_CENTER.x; }
    public final float getTop() { return position.y + Constants.SPIKE_CENTER.y; }
    public final float getBottom() { return position.y - Constants.SPIKE_CENTER.y; }
    public final float getShotRadius() { return 4.5f; }
    public final int getHitScore() { return Constants.SPIKE_HIT_SCORE; }
    public final int getKillScore() { return Constants.SPIKE_KILL_SCORE; }
    public final Class getSubclass() { return this.getClass(); }
    public final void setHealth( int health ) { this.health = health; }
}

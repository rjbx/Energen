package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.AssetManager;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Deposit extends Ground implements Destructible {
    // fields
    public final static String TAG = Box.class.getName();

    private Vector2 position;
    private float damage;

    // ctor
    public Deposit(Vector2 position) {
        this.position = position;
        damage = 50;
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().deposit, position, Constants.BOX_DEPOSIT_CENTER);
    }

    // Getters
    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.BOX_DEPOSIT_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.BOX_DEPOSIT_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.BOX_DEPOSIT_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.BOX_DEPOSIT_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.BOX_DEPOSIT_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.BOX_DEPOSIT_CENTER.y; }
    @Override public int getKillScore() { return 0; }
    @Override public int getHitScore() { return 0; }
    @Override public float getShotRadius() { return Math.min(getWidth(), getHeight()) / 2; }
    @Override public void setHealth(float damage) { this.damage = damage; }
    @Override public float getHealth() { return damage; }
    @Override public Enums.Material getType() { return Enums.Material.ORE; }
    @Override public boolean isDense() { return true; }
}

package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class Vacuum implements IndestructibleHazard {

    // fields
    private Vector2 position;
    private long startTime;

    // ctor
    public Vacuum(Vector2 position) {
        this.position = position;
        this.startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region = Assets.getInstance().getVacuumAssets().vacuum.getKeyFrame(elapsedTime, true);
        Utils.drawTextureRegion(batch, region, position, Constants.VACUUM_CENTER);
    }

    @Override public  final Vector2 getPosition() { return position; }
    @Override public  final float getWidth() { return Constants.VACUUM_COLLISION_WIDTH; }
    @Override public  final float getHeight() { return Constants.VACUUM_COLLISION_HEIGHT; }
    @Override public  final float getLeft() { return position.x - Constants.VACUUM_CENTER.x; }
    @Override public  final float getRight() { return position.x + Constants.VACUUM_CENTER.x; }
    @Override public  final float getTop() { return position.y + Constants.VACUUM_CENTER.y; }
    @Override public  final float getBottom() { return position.y - Constants.VACUUM_CENTER.y; }
    @Override public  final int getDamage() { return Constants.VACUUM_DAMAGE; }
    @Override public  final Vector2 getKnockback() { return Constants.VACUUM_KNOCKBACK; }
}

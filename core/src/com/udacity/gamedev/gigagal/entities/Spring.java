package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class Spring implements Ground {

    // fields
    private Vector2 position;
    private long startTime;
    private boolean active;

    // ctor
    public Spring(Vector2 position) {
        this.position = position;
        this.startTime = TimeUtils.nanoTime();
    }

    public void render(SpriteBatch batch) {
        
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion inactiveSpring = Assets.getInstance().getSpringAssets().spring.getKeyFrame(0, false);
        final TextureRegion activeSpring = Assets.getInstance().getSpringAssets().spring.getKeyFrame(elapsedTime, true);

        Utils.drawTextureRegion(batch, inactiveSpring, Constants.SPRING_CENTER);

        if (active) {
            Utils.drawTextureRegion(batch, activeSpring, position, Constants.SPRING_CENTER);
        }
    }

    public final Vector2 getPosition() { return position; }
    public final float getHeight() { return Constants.SPRING_CENTER.x * 2; }
    public final float getWidth() { return Constants.SPRING_CENTER.y * 2; }
    public final float getLeft() { return position.x - Constants.SPRING_CENTER.x; }
    public final float getRight() { return position.x + Constants.SPRING_CENTER.x; }
    public final float getTop() { return position.y + Constants.SPRING_CENTER.y; }
    public final float getBottom() { return position.y - Constants.SPRING_CENTER.y; }
    public final Class getSubclass() { return this.getClass(); }
    public final void setActive(boolean state) { this.active = state; }
}

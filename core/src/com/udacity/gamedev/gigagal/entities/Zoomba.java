package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums.Direction;
import com.udacity.gamedev.gigagal.util.Utils;

// mutable
public class Zoomba extends Destructible {

    // fields
    private final long startTime;
    private final float bobOffset;
    private final Platform platform;
    private final Vector2 position;
    private int health;
    private Direction direction;

    // ctor
    public Zoomba(Platform platform) {
        this.platform = platform;
        direction = Direction.RIGHT;
        position = new Vector2(platform.getLeft(), platform.getTop() + Constants.ZOOMBA_CENTER.y);
        startTime = TimeUtils.nanoTime();
        health = Constants.ZOOMBA_MAX_HEALTH;
        bobOffset = MathUtils.random();
    }

    public void update(float delta) {
        switch (direction) {
            case LEFT:
                position.x -= Constants.ZOOMBA_MOVEMENT_SPEED * delta;
                break;
            case RIGHT:
                position.x += Constants.ZOOMBA_MOVEMENT_SPEED * delta;
        }

        if (position.x < platform.getLeft()) {
            position.x = platform.getLeft();
            direction = Direction.RIGHT;
        } else if (position.x > platform.getRight()) {
            position.x = platform.getRight();
            direction = Direction.LEFT;
        }

        final float elapsedTime = Utils.secondsSince(startTime);
        final float bobMultiplier = 1 + MathUtils.sin(MathUtils.PI2 * (bobOffset + elapsedTime / Constants.ZOOMBA_BOB_PERIOD));
        position.y = platform.getTop() + Constants.ZOOMBA_CENTER.y + Constants.ZOOMBA_BOB_AMPLITUDE * bobMultiplier;
    }

    public void render(SpriteBatch batch) {
        final TextureRegion region = Assets.getInstance().getZoombaAssets().zoomba;
        Utils.drawTextureRegion(batch, region, position, Constants.ZOOMBA_CENTER);
    }

    public Direction getDirection() { return direction; }
    public final Vector2 getPosition() { return position; }
    public final int getHealth() { return health; }
    public final float getWidth() { return Constants.ZOOMBA_COLLISION_RADIUS * 2; }
    public final float getHeight() { return Constants.ZOOMBA_COLLISION_RADIUS * 2; }
    public final float getLeft() { return position.x - Constants.ZOOMBA_CENTER.x; }
    public final float getRight() { return position.x + Constants.ZOOMBA_CENTER.x; }
    public final float getTop() { return position.y + Constants.ZOOMBA_CENTER.y; }
    public final float getBottom() { return position.y - Constants.ZOOMBA_CENTER.y; }
    public final float getShotRadius() { return Constants.ZOOMBA_SHOT_RADIUS; }
    public final int getHitScore() { return Constants.ZOOMBA_HIT_SCORE; }
    public final int getKillScore() { return Constants.ZOOMBA_KILL_SCORE; }
    public final int getDamage() { return Constants.ZOOMBA_DAMAGE; }
    public final Class getSubclass() { return this.getClass(); }
    public final void setHealth( int health ) { this.health = health; }
}

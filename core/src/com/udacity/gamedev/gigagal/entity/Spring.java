package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Spring extends Ground implements Reboundable, Portable {

    // fields
    public final static String TAG = Spring.class.getName();

    private Vector2 position;
    private Groundable movingGround;
    private long startTime;
    private Vector2 velocity;
    private boolean loaded;
    private boolean beingCarried;
    private boolean atopGround;
    private boolean atopMovingGround;
    private boolean tossed;
    private Entity carrier;

    // ctor
    public Spring(Vector2 position) {
        this.position = position;
        startTime = 0;
        loaded = false;
        beingCarried = false;
        atopGround = true;
        atopMovingGround = false;
        tossed = false;
        velocity = new Vector2(0, 0);
    }

    @Override
    public void update(float delta) {
        if (beingCarried) {
            this.position.set(carrier.getPosition().x, carrier.getTop());
            atopGround = false;
        } else if (!atopGround) {
            velocity.x /= Constants.DRAG_FACTOR;
            velocity.y = -Constants.GRAVITY * 15;
            if (tossed) {
                position.mulAdd(velocity, delta);
            }
            for (Ground ground : LevelUpdater.getInstance().getGrounds()) {
                if (!atopGround) { // prevents setting to unreachable, encompassing ground
                    if (Helpers.overlapsPhysicalObject(this, ground)) {
                        if (Helpers.betweenTwoValues(getBottom(), ground.getTop() - 3, ground.getTop() + 3)
                                && ground.getWidth() > this.getWidth()) { // prevents setting to unreachable, narrower ground
                            position.y = ground.getTop() + getHeight() / 2;
                            velocity.setZero();
                            atopGround = true;
                            tossed = false;
                        } else if (ground.isDense()) {
                            if (position.x < ground.getPosition().x) {
                                position.x = ground.getLeft() - getWidth() / 2;
                            } else {
                                position.x = ground.getRight() + getWidth() / 2;
                            }
                        }
                    }
                }
            }
        }
        atopMovingGround = false;
        movingGround = null;
        // resets to nonstatic position of ground which is cloned every frame
        for (Hazard hazard : LevelUpdater.getInstance().getHazards()) {
            if (hazard instanceof Groundable && hazard instanceof Moving) {
                if (Helpers.overlapsPhysicalObject(this, hazard) && Helpers.betweenTwoValues(this.getBottom(), hazard.getTop() - 6, hazard.getTop() + 6)) {
                    position.x = hazard.getPosition().x + ((Moving) hazard).getVelocity().x;
                    position.y = hazard.getTop() + getHeight() / 2 + ((Moving) hazard).getVelocity().y;
                    atopMovingGround = true;
                    movingGround = (Groundable) hazard;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (loaded) {
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().loadedSpring.getKeyFrame(Helpers.secondsSince(startTime), false), position, Constants.SPRING_CENTER);
        } else {
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().unloadedSpring.getKeyFrame(Helpers.secondsSince(startTime), false), position, Constants.SPRING_CENTER);
        }
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final void setPosition(Vector2 position) { this.position.set(position); }
    @Override public final Entity getCarrier() { return carrier; }
    @Override public final void setCarrier(Entity entity) { this.carrier = entity; beingCarried = (carrier != null); }
    @Override public final Groundable getMovingGround() { return movingGround; }
    @Override public final float getHeight() { return Constants.SPRING_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.SPRING_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.SPRING_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.SPRING_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.SPRING_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.SPRING_CENTER.y; }
    @Override public final boolean isDense() { return !Helpers.betweenTwoValues(GigaGal.getInstance().getPosition().x, getLeft(), getRight()) || beingCarried; }
    @Override public final void toss(float velocityX) { velocity.x = velocityX * 3; tossed = true; }
    @Override public final boolean isBeingCarried() { return beingCarried; }
    public final boolean isAtopMovingGround() { return atopMovingGround; }
    @Override public final long getStartTime() { return startTime; }
    public final void setStartTime(long startTime) { this.startTime = startTime; }
    @Override public final void setState(boolean state) { this.loaded = state; }
    @Override public final boolean getState() { return loaded; }
    @Override public final void resetStartTime() { this.startTime = 0; }
}

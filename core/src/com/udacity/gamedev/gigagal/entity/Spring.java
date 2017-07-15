package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Spring extends Ground implements Reboundable, Portable {

    // fields
    public final static String TAG = Spring.class.getName();

    private Vector2 position;
    private Groundable movingGround;
    private long startTime;
    private boolean loaded;
    private boolean beingCarried;
    private boolean atopGround;
    private boolean atopMovingGround;
    private boolean tossed;
    private boolean extraPass;
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
        extraPass = false;
    }

    @Override
    public void update(float delta) {
        if (beingCarried) {
            this.position.set(carrier.getPosition().x, carrier.getTop());
            atopGround = false;
            extraPass = false;
        } else if (!atopGround || extraPass) {
            if (tossed) {
                setPosition(new Vector2(this.getPosition().x + GigaGal.getInstance().getVelocity().x / 4, this.getPosition().y));
                tossed = false;
            }
            if (!atopGround) {
                position.y -= Constants.GRAVITY * 15 * delta;
            }
            for (Ground ground : LevelUpdater.getInstance().getGrounds()) {
                if (!atopGround) {Gdx.app.log(TAG + 1, position.toString());
                    if (Helpers.overlapsPhysicalObject(this, ground)) {
                        if (Helpers.betweenTwoValues(getBottom(), ground.getTop() - 3, ground.getTop() + 3)) {
                            position.y = ground.getTop() + getHeight() / 2;
                            atopGround = true;
                        } else if (ground.isDense()) {
                            if (position.x < ground.getPosition().x) {
                                position.x = ground.getLeft() - getWidth() / 2;
                            } else {
                                position.x = ground.getRight() + getWidth() / 2;
                            }
                        }
                    }
                } else if (extraPass) {
                    if (atopGround && extraPass && !ground.isDense() && (Math.abs(position.x - GigaGal.getInstance().getPosition().x) > Constants.GIGAGAL_MAX_SPEED / 4 || (Helpers.encompassesPhysicalObject(ground, this)))) {
                        position.set(GigaGal.getInstance().getPosition().x, GigaGal.getInstance().getBottom() + getHeight() / 2);
                    }
                }
            }
            if (atopGround) {
                if (!extraPass) {
                    extraPass = true;
                } else {
                    extraPass = false;
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
    @Override public final void toss() { tossed = true; }
    @Override public final boolean isBeingCarried() { return beingCarried; }
    public final boolean isAtopMovingGround() { return atopMovingGround; }
    @Override public final long getStartTime() { return startTime; }
    public final void setStartTime(long startTime) { this.startTime = startTime; }
    @Override public final void setState(boolean state) { this.loaded = state; }
    @Override public final boolean getState() { return loaded; }
    @Override public final void resetStartTime() { this.startTime = 0; }
}

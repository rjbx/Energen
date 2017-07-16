package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

// mutable
public class Block extends Barrier implements Draggable, Vehicular {

    // fields
    public final static String TAG = Block.class.getName();

    private Groundable movingGround;
    private Vector2 velocity;
    private boolean loaded;
    private boolean beingCarried;
    private boolean atopGround;
    private boolean atopMovingGround;
    private Dynamic carrier;
    private Enums.Direction direction;

    // ctor
    public Block(float xPos, float yPos, float width, float height, Enums.Material type, boolean dense) {
        super(xPos, yPos, width, height, type, dense);
        loaded = false;
        beingCarried = false;
        atopGround = false;
        atopMovingGround = false;
        velocity = new Vector2(0, 0);
    }

    @Override
    public void update(float delta) {
        position.mulAdd(velocity, delta);
        if (beingCarried) {
            position.y = carrier.getBottom() + getHeight() / 2;
            velocity.set(carrier.getVelocity().x, carrier.getVelocity().y);
        } else {
            beingCarried = false;
            direction = Enums.Direction.UP;
            velocity.x /= Constants.DRAG_FACTOR;
            if (!atopGround) {
                velocity.y = -Constants.GRAVITY * 15;
            }
        }
        atopGround = false;
        for (Ground ground : LevelUpdater.getInstance().getGrounds()) {
            if (Helpers.overlapsPhysicalObject(this, ground)) {
                if (Helpers.betweenTwoValues(getBottom(), ground.getTop() - 3 * weightFactor(), ground.getTop() + 3 * weightFactor())
                        && ground.getWidth() >= this.getWidth()) { // prevents setting to unreachable, narrower ground
                    atopGround = true;
                    position.y = ground.getTop() + getHeight() / 2;
                    velocity.y = 0;
                } else if (ground.isDense() && Helpers.betweenTwoValues(position.y, getBottom(), getTop())) {
                    switch (direction) {
                        case LEFT:
                            if (position.x > ground.getPosition().x) {
                                position.x = ground.getLeft() - getWidth() / 2;
                            }
                            break;
                        case RIGHT:
                            if (position.x < ground.getPosition().x) {
                                position.x = ground.getRight() + getWidth() / 2;
                            }
                            break;
                        default:
                            if (position.x < ground.getPosition().x) {
                                position.x = ground.getLeft() - getWidth() / 2;
                            } else {
                                position.x = ground.getRight() + getWidth() / 2;
                            }
                    }
                }
            }
        }
        atopMovingGround = false;
        movingGround = null;
        // resets to nonstatic position of ground which is cloned every frame
        for (Hazard hazard : LevelUpdater.getInstance().getHazards()) {
            if (hazard instanceof Groundable && hazard instanceof Vehicular) {
                if (Helpers.overlapsPhysicalObject(this, hazard) && Helpers.betweenTwoValues(this.getBottom(), hazard.getTop() - 6, hazard.getTop() + 6)) {
                    position.x = hazard.getPosition().x + ((Vehicular) hazard).getVelocity().x;
                    position.y = hazard.getTop() + getHeight() / 2 + ((Vehicular) hazard).getVelocity().y;
                    atopMovingGround = true;
                    movingGround = (Groundable) hazard;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        super.render(batch, viewport);
    }

    // Getters
    @Override public final void setPosition(Vector2 position) { super.position.set(position); }
    @Override public final Vector2 getVelocity() { return velocity; }
    @Override public final Dynamic getCarrier() { return carrier; }
    @Override public final void setCarrier(Dynamic entity) {
        this.carrier = entity;
        beingCarried = (carrier != null);
        if (beingCarried) {
            if (carrier.getVelocity().x >= 0) {
                direction = Enums.Direction.RIGHT;
            } else {
                direction = Enums.Direction.LEFT;
            }
        }
    }
    @Override public final Groundable getMovingGround() { return movingGround; }
    @Override public Enums.Material getType() { return super.getType(); }
    @Override public final float weightFactor() { return Constants.MAX_WEIGHT * Math.max(2 / 3, ((getWidth() * getHeight()) / 3600)); }
    @Override public final boolean isBeingCarried() { return beingCarried; }
    @Override public final boolean isAtopMovingGround() { return atopMovingGround; }
}

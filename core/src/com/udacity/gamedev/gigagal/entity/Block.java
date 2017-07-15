package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;

// mutable
public class Block extends Barrier implements Draggable {

    // fields
    public final static String TAG = Box.class.getName();

    private Groundable movingGround;
    private long startTime;
    private Vector2 velocity;
    private boolean loaded;
    private boolean beingCarried;
    private boolean atopGround;
    private boolean atopMovingGround;
    private Entity carrier;

    // ctor
    public Block(float xPos, float yPos, float width, float height, Enums.Material type, boolean dense) {
        super(xPos, yPos, width, height, type, dense);
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        super.render(batch, viewport);
    }

    // Getters
    @Override public final void setPosition(Vector2 position) { super.position.set(position); }
    @Override public final Entity getCarrier() { return carrier; }
    @Override public final void setCarrier(Entity entity) { this.carrier = entity; beingCarried = (carrier != null); }
    @Override public final Groundable getMovingGround() { return movingGround; }
    @Override public Enums.Material getType() { return super.getType(); }
    @Override public final float weightFactor() { return Constants.MAX_WEIGHT * 2 / 3; }
    @Override public final boolean isBeingCarried() { return beingCarried; }
    @Override public final boolean isAtopMovingGround() { return atopMovingGround; }
}

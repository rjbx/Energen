package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

// mutable
public class Block extends Barrier implements Draggable {

    // fields
    public final static String TAG = Block.class.getName();

    private Moving movingGround;
    protected Vector2 velocity;
    private boolean loaded;
    private boolean beingCarried;
    private boolean againstStaticGround;
    private boolean atopMovingGround;
    private Dynamic carrier;

    // ctor
    public Block(float xPos, float yPos, float width, float height, Enums.Material type, boolean dense) {
        super(xPos, yPos, width, height, type, dense);
        loaded = false;
        beingCarried = false;
        againstStaticGround = false;
        atopMovingGround = false;
        velocity = new Vector2(0, 0);
    }

    @Override
    public void update(float delta) {
        if (beingCarried && !againstStaticGround) {
            position.set(carrier.getPosition().x, carrier.getBottom() + getHeight() / 2);
            velocity.x = carrier.getVelocity().x;
        }
        position.y += velocity.y * delta;
        velocity.y = -Constants.GRAVITY * 15 * weightFactor();
        againstStaticGround = false;
        atopMovingGround = false;
        movingGround = null;
        for (Ground ground : LevelUpdater.getInstance().getGrounds()) {
            if (Helpers.overlapsPhysicalObject(this, ground)) {
                if (Helpers.betweenTwoValues(getBottom(), ground.getTop() - 3 * weightFactor(), ground.getTop())) { // prevents setting to unreachable, narrower ground
                    position.y = ground.getTop() + getHeight() / 2;
                    velocity.y = 0;
                } else if (ground.isDense() && getTop() > ground.getBottom()) {
                    if ((!(ground instanceof Block) ||
                            (((Block) ground).isAgainstStaticGround() && !((Block) ground).isBeingCarried())
                            || (!beingCarried && !againstStaticGround && !((Block) ground).isAgainstStaticGround()))) {
                        if (!(ground instanceof Block) || !((Block) ground).isBeingCarried()) {
                            if (!beingCarried || velocity.x != 0) {
                                againstStaticGround = true;
                            }
                        }
                        velocity.x = 0;
                        if (position.x < ground.getPosition().x) {
                            position.x = ground.getLeft() - getWidth() / 2;
                        } else {
                            position.x = ground.getRight() + getWidth() / 2;
                        }
                    } else if (ground instanceof Pliable) {
                        if (beingCarried && ((Block) ground).isBeingCarried()) {
                            beingCarried = false;
                        } else {
                            movingGround = (Moving) ground;
                        }
                    }
                }
            }
        }
        // resets to nonstatic position of ground which is cloned every frame
        for (Hazard hazard : LevelUpdater.getInstance().getHazards()) {
            if (hazard instanceof Groundable && hazard instanceof Vehicular) {
                if (Helpers.overlapsPhysicalObject(this, hazard) && Helpers.betweenTwoValues(this.getBottom(), hazard.getTop() - 6, hazard.getTop() + 6)) {
                    position.x = hazard.getPosition().x + ((Vehicular) hazard).getVelocity().x;
                    position.y = hazard.getTop() + getHeight() / 2 + ((Vehicular) hazard).getVelocity().y;
                    atopMovingGround = true;
                    movingGround = (Moving) hazard;
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
    @Override public final void setCarrier(Dynamic entity) { againstStaticGround = false; this.carrier = entity; beingCarried = (carrier != null); }
    @Override public final Moving getMovingGround() { return movingGround; }
    @Override public Enums.Material getType() { return super.getType(); }
    @Override public final float weightFactor() { return Constants.MAX_WEIGHT * Math.max(.67f, ((getWidth() * getHeight()) / 3600)); }
    @Override public final boolean isBeingCarried() { return beingCarried; }
    @Override public final boolean isAtopMovingGround() { return atopMovingGround; }
    public final boolean isAgainstStaticGround() { return againstStaticGround; }
    @Override public final boolean isDense() { return !Helpers.betweenTwoValues(GigaGal.getInstance().getPosition().x, getLeft(), getRight()) || beingCarried; }
}

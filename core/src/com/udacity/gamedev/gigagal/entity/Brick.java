package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelAssets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

// also functions as shield
public class Brick extends Barrier implements Tossable, Impermeable, Hurdleable {

    // fields
    public final static String TAG = Brick.class.getName();

    private Humanoid carrier;
    private Moving movingGround;
    private Ground topGround;
    private Vector2 velocity;
    private boolean beneatheGround;
    private boolean againstStaticGround;
    private boolean atopMovingGround;
    private boolean beingCarried;
    private float payload;

    // ctor
    public Brick(float xPos, float yPos, float width, float height, Enums.Material type, boolean dense) {
        super(xPos, yPos, width, height, type, dense);
        velocity = new Vector2(0, 0);
        beingCarried = false;
        againstStaticGround = false;
        atopMovingGround = false;
        beneatheGround = false;
        payload = 0;
    }

    @Override
    public void update(float delta) {
        if (beingCarried && !againstStaticGround) {
            super.position.set(carrier.getPosition().x + Helpers.speedToVelocity(this.getWidth() / 2, carrier.getDirectionX(), Enums.Orientation.X), carrier.getBottom() + getHeight() / 2);
            velocity.x = carrier.getVelocity().x;
        }
        super.position.mulAdd(velocity, delta);
        float multiplier = Math.max(1, weightFactor());
        velocity.x /= Constants.DRAG_FACTOR * multiplier;
        velocity.y = -Constants.GRAVITY * 5 * multiplier;
        againstStaticGround = false;
        atopMovingGround = false;
        beneatheGround = false;
        movingGround = null;
        topGround = null;
        payload = 0;
        for (Ground ground : LevelAssets.getClonedGrounds()) {
            if (Helpers.overlapsPhysicalObject(this, ground)) {
                if (Helpers.betweenTwoValues(getBottom(), ground.getTop() - 8 * multiplier, ground.getTop() + 1) && getBottom() > ground.getBottom()
                && getLeft() != ground.getRight() && getRight() != ground.getLeft()) { // prevents setting atop lower of adjacently stacked grounds when dropping from rappel
                    if (ground instanceof Moving) {
                        if (!beingCarried) {
                            super.position.x = ground.getPosition().x;
                            super.position.y = ground.getTop() + getHeight() / 2;
                            velocity.x = ((Moving) ground).getVelocity().x;
                            velocity.y = ((Moving) ground).getVelocity().y;
                        }
                        atopMovingGround = true;
                        movingGround = (Moving) ground;
                    } else if ((!(ground instanceof Climbable))
                            && ground.getWidth() >= this.getWidth()) { // prevents setting to unreachable, narrower ground
                        super.position.y = ground.getTop() + getHeight() / 2;
                        if (Helpers.overlapsBetweenTwoSides(position.x, getWidth() / 2, ground.getLeft() + 2, ground.getRight() - 2)) {
                            velocity.y = 0;
                        }
                    }
                    if (ground instanceof Propelling) {
                        velocity.x = Helpers.speedToVelocity(Constants.TREADMILL_SPEED, ((Propelling) ground).getDirectionX(), Enums.Orientation.X);
                        velocity.y = 0;
                    } else if (ground instanceof Skateable) {
                        if (Math.abs(velocity.x) > 0.005f) {
                            velocity.x /= 1.005;
                        } else {
                            velocity.x = 0;
                        }
                        super.position.x += velocity.x * delta;
                        velocity.y = 0;
                    } else if (!atopMovingGround) {
                        velocity.x = 0;
                    }
                } else if ((ground.isDense()
                        && getTop() > ground.getBottom()
                        && !(ground instanceof Pliable)
                        && !(ground instanceof Propelling) && !(ground instanceof Box) && !(ground instanceof Climbable))
                        || (ground instanceof Pliable && (!beingCarried || ((Pliable) ground).isAgainstStaticGround()))) {
                    if ((!(ground instanceof Pliable) ||
                            (((Pliable) ground).isAgainstStaticGround() && !((Pliable) ground).isBeingCarried())
                            || (!beingCarried && !againstStaticGround && !((Pliable) ground).isAgainstStaticGround()))) {
                        if (!(ground instanceof Pliable) || !((Pliable) ground).isBeingCarried()) {
                            if (!beingCarried || velocity.x != 0) {
                                againstStaticGround = true;
                            }
                        }
                    }
                    if (Helpers.betweenTwoValues(position.x, ground.getLeft() + 2, ground.getRight() - 2)) {
                        if (!beingCarried && ground instanceof Moving && getBottom() == ground.getBottom()) {
//                            velocity.y = ((Moving) ground).getVelocity().y;
//                            position.y = ground.getTop() + (getHeight() / 2);
                        }
                    } else if (!againstStaticGround && (!(ground instanceof Pliable) || getBottom() == ground.getBottom())) {
                        if (position.x < ground.getPosition().x) {
                            super.position.x = ground.getLeft() - getWidth() / 2;
                        } else {
                            super.position.x = ground.getRight() + getWidth() / 2;
                        }
                    }
                } else if (ground instanceof Box) {
                    velocity.y = 0;
                }
                if (Helpers.betweenTwoValues(getTop(), ground.getBottom() - 4, ground.getBottom() + 4)) {
                    beneatheGround = true;
                    topGround = ground;
                } else if (!atopMovingGround && !(ground instanceof Propelling)) {
                    velocity.x = 0;
                }
            }
            if (ground instanceof Pliable && ((Pliable) ground).isAtopMovingGround() && ((Pliable) ground).getMovingGround().equals(this)) {
                payload = ((Pliable) ground).weightFactor();
            }
        }

        if (Helpers.overlapsPhysicalObject(Avatar.getInstance(), this) && Helpers.betweenTwoValues(Avatar.getInstance().getBottom(), this.getTop() - 1, this.getTop() + 1)) {
            payload += Avatar.getInstance().weightFactor();
        }

        // resets to nonstatic super.position of ground which is cloned every frame
        for (Hazard hazard : LevelAssets.getClonedHazards()) {
            if (hazard instanceof Groundable && hazard instanceof Vehicular) {
                if (Helpers.overlapsPhysicalObject(this, hazard) && Helpers.betweenTwoValues(this.getBottom(), hazard.getBottom(), hazard.getTop())) {
                    super.position.x = hazard.getPosition().x;
                    super.position.y = hazard.getTop() + getHeight() / 2;
                    velocity.x = ((Vehicular) hazard).getVelocity().x;
                    velocity.y = ((Vehicular) hazard).getVelocity().y;
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
    
    public void touchAllGrounds(Array<Ground> grounds) {}
    public void touchAllHazards(Array<Hazard> hazards) {}

    // Getters
    @Override public final void setPosition(Vector2 position) { super.position.set(position); }
    @Override public final Vector2 getVelocity() { return velocity; }
    @Override public final Humanoid getCarrier() { return carrier; }
    @Override public final void setCarrier(Humanoid entity) { againstStaticGround = false; beneatheGround = false; this.carrier = entity; beingCarried = (carrier != null); }
    @Override public final Moving getMovingGround() { return movingGround; }
    @Override public Enums.Material getType() { return super.getType(); }
    @Override public final float weightFactor() { return Constants.MAX_WEIGHT * Math.max(.1f, ((getWidth() * getHeight()) / 1000)) + payload; }
    @Override public final boolean isBeingCarried() { return beingCarried; }
    @Override public final boolean isAtopMovingGround() { return atopMovingGround; }
    @Override public final boolean isDense() { return super.dense || beingCarried; }
    @Override public final void toss(float velocityX) { velocity.x = velocityX; }
    @Override public final Ground getTopGround() { return topGround; }
    @Override public final boolean isBeneatheGround() { return beneatheGround; }
    public final boolean isAgainstStaticGround() { return againstStaticGround; }
    public final void setAgainstStaticGround() { this.againstStaticGround = true; }
    public final void setVelocity(Vector2 velocity) { this.velocity.set(velocity); }
    public final void setMovingGround(Moving ground) { movingGround = ground; }
    public final void stopCarrying() { beingCarried = false; }
}
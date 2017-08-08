package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

// also functions as shield
public class Brick extends Barrier implements Tossable {

    // fields
    public final static String TAG = Brick.class.getName();

    private boolean againstStaticGround;
    private Moving movingGround;
    protected Vector2 velocity;
    private boolean beingCarried;
    private boolean atopMovingGround;
    private Dynamic carrier;
    private float payload;

    // ctor
    public Brick(float xPos, float yPos, float width, float height, Enums.Material type, boolean dense) {
        super(xPos, yPos, width, height, type, dense);
        beingCarried = false;
        atopMovingGround = false;
        velocity = new Vector2(0, 0);
        payload = 0;
    }

    @Override
    public void update(float delta) {
        if (beingCarried) {
            position.set(carrier.getPosition().x, carrier.getBottom() + getHeight() / 2);
            velocity.x = carrier.getVelocity().x;
        }
        position.mulAdd(velocity, delta);
        velocity.x /= Constants.DRAG_FACTOR * Math.max(.67f, weightFactor());
        velocity.y = -Constants.GRAVITY * 15 * weightFactor();
        payload = 0;
        againstStaticGround = false;
        atopMovingGround = false;
        movingGround = null;
        for (Ground ground : LevelUpdater.getInstance().getGrounds()) {
            if (Helpers.overlapsPhysicalObject(this, ground)) {
                if (Helpers.betweenTwoValues(getBottom(), ground.getTop() - 3 * weightFactor(), ground.getTop()) && getBottom() > ground.getBottom()
                && getLeft() != ground.getRight() && getRight() != ground.getLeft()) { // prevents setting atop lower of adjacently stacked grounds when dropping from rappel
                    if (ground instanceof Moving) {
                        if (!beingCarried && (ground instanceof Roving || ((Pliable) ground).isBeingCarried())) {
                            position.x = ground.getPosition().x + ((Moving) ground).getVelocity().x;
                        }
                        position.y = ground.getTop() + getHeight() / 2;
                        if (ground instanceof Aerial) {
                            velocity.y = ((Aerial) ground).getVelocity().y;
                        } else {
                            velocity.y = 0;
                        }
                        atopMovingGround = true;
                        movingGround = (Moving) ground;
                    } else if ((!(ground instanceof Climbable))
                            && ground.getWidth() >= this.getWidth()) { // prevents setting to unreachable, narrower ground
                        position.y = ground.getTop() + getHeight() / 2;
                        velocity.y = 0;
                    }
                    if (ground instanceof Propelling) {
                        velocity.x = Helpers.absoluteToDirectionalValue(Constants.TREADMILL_SPEED, ((Propelling) ground).getDirectionX(), Enums.Orientation.X);
                        velocity.y = 0;
                    } else if (ground instanceof Skateable) {
                        if (Math.abs(velocity.x) > 0.005f) {
                            velocity.x /= 1.005;
                        } else {
                            velocity.x = 0;
                        }
                        position.x += velocity.x * delta;
                        velocity.y = 0;
                    } else {
                        velocity.x = 0;
                    }
                } else if ((ground.isDense()
                && getTop() > ground.getBottom()
                && !(ground instanceof Pliable)
                && !(ground instanceof Propelling) && !(ground instanceof Box) && !(ground instanceof Climbable))
                || (ground instanceof Pliable && !beingCarried)) {
                    if ((!(ground instanceof Pliable) ||
                            (((Pliable) ground).isAgainstStaticGround() && !((Pliable) ground).isBeingCarried())
                            || (!beingCarried && !againstStaticGround && !((Pliable) ground).isAgainstStaticGround()))) {
                        if (!(ground instanceof Pliable) || !((Pliable) ground).isBeingCarried()) {
                            if (!beingCarried || velocity.x != 0) {
                                againstStaticGround = true;
                            }
                        }
                    }
                    velocity.x = 0;
                    if (!againstStaticGround && (!(ground instanceof Pliable) || ground.getBottom() == getBottom())) {
                        if (position.x < ground.getPosition().x) {
                            position.x = ground.getLeft() - getWidth() / 2;
                        } else {
                            position.x = ground.getRight() + getWidth() / 2;
                        }
                    }
                } else if (ground instanceof Box) {
                    velocity.y = 0;
                }
            }
            if (ground instanceof Pliable && ((Pliable) ground).isAtopMovingGround() && ((Pliable) ground).getMovingGround().equals(this)) {
                payload = ((Pliable) ground).weightFactor();
            }
        }
        // resets to nonstatic position of ground which is cloned every frame
        for (Hazard hazard : LevelUpdater.getInstance().getHazards()) {
            if (hazard instanceof Groundable && hazard instanceof Vehicular) {
                if (Helpers.overlapsPhysicalObject(this, hazard) && Helpers.betweenTwoValues(this.getBottom(), hazard.getTop() - 3 * weightFactor(), hazard.getTop())) {
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
    @Override public final float weightFactor() { return Constants.MAX_WEIGHT * Math.max(.2f, ((getWidth() * getHeight()) / 3600) + payload); }
    @Override public final boolean isBeingCarried() { return beingCarried; }
    @Override public final boolean isAtopMovingGround() { return atopMovingGround; }
    @Override public final boolean isDense() { return super.dense || beingCarried; }
    @Override public final void toss(float velocityX) { velocity.x = velocityX; }
    public final boolean isAgainstStaticGround() { return againstStaticGround; }
    public final void setAgainstStaticGround() { this.againstStaticGround = true; }
    public final void setVelocity(Vector2 velocity) { this.velocity.set(velocity); }
    public final void setMovingGround(Moving ground) { movingGround = ground; }
    public final void stopCarrying() { beingCarried = false; }
}
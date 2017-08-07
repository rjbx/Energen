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
        }
        position.mulAdd(velocity, delta);
        velocity.x /= Constants.DRAG_FACTOR * weightFactor();
        velocity.y = -Constants.GRAVITY * 15 * weightFactor();
        payload = 0;
        for (Ground ground : LevelUpdater.getInstance().getGrounds()) {
            if (Helpers.overlapsPhysicalObject(this, ground)) {
                if (Helpers.betweenTwoValues(getBottom(), ground.getTop() - 3 * weightFactor(), ground.getTop())
                        && getLeft() != ground.getRight() && getRight() != ground.getLeft()) { // prevents setting atop lower of adjacently stacked grounds when dropping from rappel
                    if (ground instanceof Moving) {
                        position.x = ground.getPosition().x + ((Moving) ground).getVelocity().x;
                        position.y = ground.getTop() + getHeight() / 2 + ((Moving) ground).getVelocity().y;
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
                } else if (ground.isDense()
                        && getTop() > ground.getBottom()
                        && !(ground instanceof Pliable)
                        && !(ground instanceof Propelling) && !(ground instanceof Box && ((Box) ground).getHealth() < 1)) {
                    float bounceBack = 0;
                    if (ground instanceof Tripknob && ((Tripknob) ground).isConverted()) {
                        bounceBack = 5;
                    }
                    if (position.x < ground.getPosition().x) {
                        position.x = ground.getLeft() - getWidth() / 2 - bounceBack;
                    } else {
                        position.x = ground.getRight() + getWidth() / 2 + bounceBack;
                    }
                    velocity.x = 0;
                } else if (ground instanceof Box) {
                    velocity.y = 0;
                }
            }
            if (ground instanceof Pliable && ((Pliable) ground).isAtopMovingGround() && ((Pliable) ground).getMovingGround().equals(this)) {
                payload = ((Pliable) ground).weightFactor();
            }
        }
        atopMovingGround = false;
        movingGround = null;
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
    @Override public final void setCarrier(Dynamic entity) { this.carrier = entity; beingCarried = (carrier != null); }
    @Override public final Moving getMovingGround() { return movingGround; }
    @Override public Enums.Material getType() { return super.getType(); }
    @Override public final float weightFactor() { return Constants.MAX_WEIGHT * Math.max(.2f, ((getWidth() * getHeight()) / 3600) + payload); }
    @Override public final boolean isBeingCarried() { return beingCarried; }
    @Override public final boolean isAtopMovingGround() { return atopMovingGround; }
    @Override public final boolean isDense() { return super.dense || beingCarried; }
    @Override public final void toss(float velocityX) { velocity.x = velocityX; }public final boolean isAgainstStaticGround() { return againstStaticGround; }
    public final void setAgainstStaticGround() { this.againstStaticGround = true; }
    public final void setVelocity(Vector2 velocity) { this.velocity.set(velocity); }
    public final void setMovingGround(Moving ground) { movingGround = ground; }
    public final void stopCarrying() { beingCarried = false; }
}
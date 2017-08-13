package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Spring extends Ground implements Reboundable, Rappelable, Tossable, Compressible {

    // fields
    public final static String TAG = Spring.class.getName();

    private Humanoid carrier;
    private boolean againstStaticGround;
    private Vector2 position;
    private Moving movingGround;
    private Ground topGround;
    private long startTime;
    private Vector2 velocity;
    private boolean loaded;
    private boolean beingCarried;
    private boolean atopMovingGround;
    private boolean underneatheGround;

    // ctor
    public Spring(Vector2 position) {
        this.position = position;
        startTime = 0;
        loaded = false;
        beingCarried = false;
        atopMovingGround = false;
        underneatheGround = false;
        velocity = new Vector2(0, 0);
    }

    @Override
    public void update(float delta) {
        if (beingCarried && !againstStaticGround) {
            this.position.set(carrier.getPosition().x, carrier.getBottom() + getHeight());
            this.velocity.x = carrier.getVelocity().x;
        }
        position.mulAdd(velocity, delta);
        float multiplier = Math.max(1, weightFactor());
        velocity.x /= Constants.DRAG_FACTOR * multiplier;
        velocity.y = -Constants.GRAVITY * 15 * multiplier;
        againstStaticGround = false;
        atopMovingGround = false;
        movingGround = null;
        underneatheGround = false;
        topGround = null;
        for (Ground ground : LevelUpdater.getInstance().getGrounds()) {
            if (Helpers.overlapsPhysicalObject(this, ground)) {
                if (Helpers.betweenTwoValues(getBottom(), ground.getTop() - 6 * multiplier, ground.getTop()) && getBottom() > ground.getBottom()
                && getLeft() != ground.getRight() && getRight() != ground.getLeft()) { // prevents setting atop lower of adjacently stacked grounds when dropping from rappel
                    if (ground instanceof Moving) {
                        position.x = ground.getPosition().x;
                        position.y = ground.getTop() + (getHeight() / 2);
                        velocity.x = ((Moving) ground).getVelocity().x;
                        velocity.y = ((Moving) ground).getVelocity().y;
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
                        if ((!beingCarried && ground instanceof Moving && ground.getBottom() == getBottom())) {
                            position.y = ground.getTop() + (getHeight() / 2);
                        }
                    } else {
                        if (!againstStaticGround && (!(ground instanceof Pliable) || ((Pliable) ground).isBeingCarried())) {
                            if (position.x < ground.getPosition().x) {
                                position.x = ground.getLeft() - getWidth() / 2;
                            } else {
                                position.x = ground.getRight() + getWidth() / 2;
                            }
                        }
                    }
                } else if (ground instanceof Box) {
                    velocity.y = 0;
                }
                if (Helpers.betweenTwoValues(getTop(), ground.getBottom() - 2, ground.getBottom() + 2)) {
                    loaded = true;
                    underneatheGround = true;
                    topGround = ground;
                } else if (!atopMovingGround && !(ground instanceof Propelling)) {
                    velocity.x = 0;
                }
            }
        }
        // resets to nonstatic position of ground which is cloned every frame
        for (Hazard hazard : LevelUpdater.getInstance().getHazards()) {
            if (hazard instanceof Groundable && hazard instanceof Vehicular) {
                if (Helpers.overlapsPhysicalObject(this, hazard) && Helpers.betweenTwoValues(this.getBottom(), hazard.getBottom(), hazard.getTop())) {
                    position.x = hazard.getPosition().x;
                    position.y = hazard.getTop() + getHeight() / 2;
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
    @Override public final Vector2 getVelocity() { return velocity; }
    @Override public final void setPosition(Vector2 position) { this.position.set(position); }
    @Override public final Humanoid getCarrier() { return carrier; }
    @Override public final void setCarrier(Humanoid entity) { againstStaticGround = false; this.carrier = entity; beingCarried = (carrier != null); }
    @Override public final Moving getMovingGround() { return movingGround; }
    @Override public final Ground getTopGround() { return topGround; }
    @Override public final float getHeight() { return Constants.SPRING_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.SPRING_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.SPRING_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.SPRING_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.SPRING_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.SPRING_CENTER.y; }
    @Override public final boolean isDense() { return beingCarried || GigaGal.getInstance().getAction() != Enums.Action.CLIMBING; }
    @Override public final void toss(float velocityX) { velocity.x = velocityX; underneatheGround = true; }
    @Override public final float weightFactor() { return Constants.MAX_WEIGHT * .2f; }
    @Override public final boolean isBeingCarried() { return beingCarried; }
    @Override public final boolean isUnderneatheGround() { return underneatheGround; }
    public final boolean isAtopMovingGround() { return atopMovingGround; }
    @Override public final long getStartTime() { return startTime; }
    public final void setStartTime(long startTime) { this.startTime = startTime; }
    @Override public final void setState(boolean state) { this.loaded = state; }
    @Override public final boolean getState() { return loaded; }
    @Override public final void resetStartTime() { this.startTime = 0; }
    @Override public final float jumpMultiplier() { return Constants.SPRING_JUMP_MULTIPLIER; }
    public final boolean isAgainstStaticGround() { return againstStaticGround; }
    public final void setAgainstStaticGround() { this.againstStaticGround = true; }
    public final void setVelocity(Vector2 velocity) { this.velocity.set(velocity); }
    public final void setMovingGround(Moving ground) { movingGround = ground; }
    public final void stopCarrying() { beingCarried = false; }
}

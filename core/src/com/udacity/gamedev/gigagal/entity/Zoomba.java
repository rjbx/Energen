package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.Direction;
import com.udacity.gamedev.gigagal.util.Helpers;

// mutable
public class Zoomba extends Hazard implements Destructible, Dynamic, Groundable, Convertible {

    // fields
    public final static String TAG = Zoomba.class.getName();

    private final Enums.Material type;
    private final long startTime;
    private final float bobOffset;
    private final float bobNadir;
    private float range;
    private Vector2 position;
    private final Vector2 startingPosition;
    private Vector2 velocity;
    private float health;
    private Direction direction;
    private Enums.Orientation orientation;
    private Array<Animation> animations;
    private boolean converted;

    // ctor
    public Zoomba(Vector2 position, Enums.Material type, float range) {
        this.position = position;
        this.startingPosition = new Vector2(position);
        velocity = new Vector2();
        bobNadir = position.y;
        this.type = type;
        direction = Direction.RIGHT;
        this.orientation = Enums.Orientation.X;
        startTime = TimeUtils.nanoTime();
        health = Constants.ZOOMBA_MAX_HEALTH;
        bobOffset = MathUtils.random();
        this.range = range;
        switch (type) {
//            case ORE:
//                animation = Assets.getInstance().getZoombaAssets().oreZoomba;
//                break;
//            case PLASMA:
//                animation = Assets.getInstance().getZoombaAssets().plasmaZoomba;
//                break;
            case GAS:
                animations = Assets.getInstance().getZoombaAssets().gasAnimations;
                break;
//            case LIQUID:
//                animation = Assets.getInstance().getZoombaAssets().liquidZoomba;
//                break;
//            case SOLID:
//                animation = Assets.getInstance().getZoombaAssets().solidZoomba;
//                break;
            default:
                animations = Assets.getInstance().getZoombaAssets().gasAnimations;
        }
    }

    public void update(float delta) {
        if (orientation == Enums.Orientation.X) {
            position.set(position.x + velocity.x, velocity.y);
            velocity.x = Helpers.absoluteToDirectionalValue(Constants.ZOOMBA_MOVEMENT_SPEED * delta, direction, Enums.Orientation.X);

            if (position.x < startingPosition.x - (range / 2)) {
                position.x = startingPosition.x - (range / 2);
                direction = Direction.RIGHT;
            } else if (position.x > startingPosition.x + (range / 2)) {
                position.x = startingPosition.x + (range / 2);
                direction = Direction.LEFT;
            }

            float bobMultiplier = 1 + MathUtils.sin(MathUtils.PI2 * (bobOffset + Helpers.secondsSince(startTime) / Constants.ZOOMBA_BOB_PERIOD));
            velocity.y = bobNadir + Constants.ZOOMBA_CENTER.y + Constants.ZOOMBA_BOB_AMPLITUDE * bobMultiplier;
        } else {
            position.set(position.x, position.y + velocity.y);
            velocity.y = Helpers.absoluteToDirectionalValue(Constants.ZOOMBA_MOVEMENT_SPEED * delta, direction, Enums.Orientation.Y);

            if (position.y < startingPosition.y - (range / 2)) {
                position.y = startingPosition.y - (range / 2);
                direction = Direction.UP;
            } else if (position.y > startingPosition.y + (range / 2)) {
                position.y = startingPosition.y + (range / 2);
                direction = Direction.DOWN;
            }

            float bobMultiplier = 1 + MathUtils.sin(MathUtils.PI2 * (bobOffset + Helpers.secondsSince(startTime) / Constants.ZOOMBA_BOB_PERIOD));
            velocity.x = bobNadir + Constants.ZOOMBA_CENTER.x + Constants.ZOOMBA_BOB_AMPLITUDE * bobMultiplier;
        }

        if (converted) {
            if (orientation == Enums.Orientation.X) {
                position = startingPosition;
                direction = Direction.LEFT;
            } else {
                position = startingPosition;
                direction = Direction.DOWN;
            }
            converted = false;
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        switch (direction) {
            case LEFT:
                Helpers.drawTextureRegion(batch, viewport, animations.get(0).getKeyFrame(Helpers.secondsSince(startTime), true), position, Constants.ZOOMBA_CENTER);
                break;
            case RIGHT:
                Helpers.drawTextureRegion(batch, viewport, animations.get(1).getKeyFrame(Helpers.secondsSince(startTime), true), position, Constants.ZOOMBA_CENTER);
                break;
            case DOWN:
                Helpers.drawTextureRegion(batch, viewport, animations.get(2).getKeyFrame(Helpers.secondsSince(startTime), true), position, Constants.ZOOMBA_CENTER);
                break;
            case UP:
                Helpers.drawTextureRegion(batch, viewport, animations.get(3).getKeyFrame(Helpers.secondsSince(startTime), true), position, Constants.ZOOMBA_CENTER);
                break;
        }
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final Vector2 getVelocity() { return velocity; }
    @Override public final float getHealth() { return health; }
    @Override public final float getWidth() { return Constants.ZOOMBA_COLLISION_WIDTH; }
    @Override public final float getHeight() { return Constants.ZOOMBA_COLLISION_HEIGHT; }
    @Override public final float getLeft() { return position.x - Constants.ZOOMBA_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.ZOOMBA_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.ZOOMBA_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.ZOOMBA_CENTER.y; }
    @Override public final float getShotRadius() { return Constants.ZOOMBA_SHOT_RADIUS; }
    @Override public final int getHitScore() { return Constants.ZOOMBA_HIT_SCORE; }
    @Override public final int getKillScore() { return Constants.ZOOMBA_KILL_SCORE; }
    @Override public final int getDamage() { return Constants.ZOOMBA_STANDARD_DAMAGE; }
    @Override public Enums.Material getType() { return type; }
    @Override public final void setHealth( float health ) { this.health = health; }
    @Override public final Vector2 getKnockback() { return Constants.ZOOMBA_KNOCKBACK; }
    @Override public Enums.Direction getDirectionX() { return direction; }
    @Override public Enums.Direction getDirectionY() { return direction; }
    @Override public void setDirectionX(Enums.Direction direction) { this.direction = direction; }
    @Override public void setDirectionY(Enums.Direction direction) { this.direction = direction; }
    @Override public Enums.Orientation getOrientation() { return orientation; }
    @Override public void convert() { this.orientation = Helpers.getOppositeOrientation(orientation); this.converted = true; }
    @Override public boolean isConverted() { return converted; }
    @Override public final boolean isDense() { return true; }
    public int getMountDamage() { return Constants.ZOOMBA_STANDARD_DAMAGE; }
    public Vector2 getMountKnockback() { return Constants.ZOOMBA_KNOCKBACK; }
    public final Direction getDirection() { return direction; }
    public final long getStartTime() { return startTime; }
}

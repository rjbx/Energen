package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.app.LevelAssets;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Enums.Direction;
import com.github.rjbx.energen.util.Helpers;

// mutable
public class Zoomba extends Hazard implements Destructible, Dynamic, Groundable, Rappelable, Hurdleable, Convertible, Impermeable {

    // fields
    public final static String TAG = Zoomba.class.getName();

    private final Enums.Energy type;
    private final long startTime;
    private final float bobOffset;
    private final float range;
    private float bobNadir;
    private Vector2 position;
    private final Vector2 startingPosition;
    private Rectangle hazardBounds;
    private Vector2 velocity;
    private float health;
    private Direction direction;
    private Enums.Orientation orientation;
    private Array<Animation> animations;
    private Animation<TextureRegion> animation;
    private boolean converted;

    // ctor
    public Zoomba(Vector2 position, Enums.Orientation orientation, Enums.Energy type, float range) {
        this.position = position;
        this.startingPosition = new Vector2(position);
        velocity = new Vector2();
        this.type = type;
        switch (type) {
//            case ORE:
//                animation = AssetManager.getInstance().getZoombaAssets().oreZoomba;
//                break;
//            case PLASMA:
//                animation = AssetManager.getInstance().getZoombaAssets().plasmaZoomba;
//                break;
            case GAS:
                animations = AssetManager.getInstance().getZoombaAssets().gasAnimations;
                break;
//            case LIQUID:
//                animation = AssetManager.getInstance().getZoombaAssets().liquidZoomba;
//                break;
//            case SOLID:
//                animation = AssetManager.getInstance().getZoombaAssets().solidZoomba;
//                break;
            default:
                animations = AssetManager.getInstance().getZoombaAssets().gasAnimations;
        }
        animation = animations.get(0);
        startTime = TimeUtils.nanoTime();
        health = Constants.ZOOMBA_MAX_HEALTH;
        bobOffset = MathUtils.random();
        hazardBounds = new Rectangle();
        this.range = range;
        this.orientation = orientation;
        updateOrientation(this.orientation);
        updateDirection(this.direction);
    }

    @Override
    public Zoomba safeClone() {
        Zoomba clone = new Zoomba(position, orientation, type, range);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    public void update(float delta) {
        if (orientation == Enums.Orientation.X) {
            position.mulAdd(velocity, delta);
            velocity.x = Helpers.speedToVelocity(Constants.ZOOMBA_MOVEMENT_SPEED, direction, Enums.Orientation.X);
            if (position.x < startingPosition.x - (range / 2)) {
                position.x = startingPosition.x - (range / 2);
                updateDirection(Direction.RIGHT);
            } else if (position.x > startingPosition.x + (range / 2)) {
                position.x = startingPosition.x + (range / 2);
                updateDirection(Direction.LEFT);
            }
            float bobMultiplier = 1 + MathUtils.sin(MathUtils.PI2 * (bobOffset + Helpers.secondsSince(startTime) / Constants.ZOOMBA_BOB_PERIOD));
            velocity.y = (Constants.ZOOMBA_CENTER.y + Constants.ZOOMBA_BOB_AMPLITUDE * bobMultiplier + bobNadir - position.y) * (1 / delta);
        } else {
            position.mulAdd(velocity, delta);
            velocity.y = Helpers.speedToVelocity(Constants.ZOOMBA_MOVEMENT_SPEED, direction, Enums.Orientation.Y);
            if (position.y < startingPosition.y - (range / 2)) {
                position.y = startingPosition.y - (range / 2);
                updateDirection(Direction.UP);
            } else if (position.y > startingPosition.y + (range / 2)) {
                position.y = startingPosition.y + (range / 2);
                updateDirection(Direction.DOWN);
            }
            float bobMultiplier = 1 + MathUtils.sin(MathUtils.PI2 * (bobOffset + Helpers.secondsSince(startTime) / Constants.ZOOMBA_BOB_PERIOD));
            velocity.x = (Constants.ZOOMBA_CENTER.x + Constants.ZOOMBA_BOB_AMPLITUDE * bobMultiplier + bobNadir - position.x) * (1 / delta);
        }
        updateDirection(direction);

        for (Ground ground : LevelAssets.getClonedGrounds()) {
            if (ground.isDense() && !(ground instanceof Pliable)) {
                if (Helpers.overlapsPhysicalObject(this, ground)) {
                    direction = Helpers.getOppositeDirection(direction);
                    velocity.set(Helpers.speedToVelocity(-velocity.x, direction, Enums.Orientation.X), Helpers.speedToVelocity(-velocity.y, direction, Enums.Orientation.Y));
                    position.add(velocity);
                    if (Helpers.overlapsPhysicalObject(this, ground)) { // prevents post conversion reposition below ground top
                        position.set(startingPosition.x, ground.getTop() + getHeight());
                        bobNadir = Helpers.vectorToAxisValue(position, Helpers.getOppositeOrientation(orientation));
                    }
                }
            }
        }
        for (Hazard hazard : LevelAssets.getClonedHazards()) {
            if (Helpers.overlapsPhysicalObject(this, hazard)) {
                if (hazard instanceof Zoomba) {
                    direction = Helpers.getOppositeDirection(direction);
                    velocity.set(Helpers.speedToVelocity(-velocity.x, direction, Enums.Orientation.X), Helpers.speedToVelocity(-velocity.y, direction, Enums.Orientation.Y));
                    position.add(velocity);
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
         Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime), true), position, Constants.ZOOMBA_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final Vector2 getVelocity() { return velocity; }
    @Override public final float getHealth() { return health; }
    @Override public final float getWidth() { return Constants.ZOOMBA_COLLISION_WIDTH; }
    @Override public final float getHeight() { return Constants.ZOOMBA_COLLISION_HEIGHT; }
    @Override public final float getLeft() { return position.x - Constants.ZOOMBA_COLLISION_WIDTH / 2; }
    @Override public final float getRight() { return position.x + Constants.ZOOMBA_COLLISION_WIDTH / 2; }
    @Override public final float getTop() { return position.y + Constants.ZOOMBA_COLLISION_HEIGHT / 2; }
    @Override public final float getBottom() { return position.y - Constants.ZOOMBA_COLLISION_HEIGHT / 2; }
    @Override public final float getShotRadius() { return Constants.ZOOMBA_SHOT_RADIUS; }
    @Override public final int getHitScore() { return Constants.ZOOMBA_HIT_SCORE; }
    @Override public final int getKillScore() { return Constants.ZOOMBA_KILL_SCORE; }
    @Override public final int getDamage() { return Constants.ZOOMBA_STANDARD_DAMAGE; }
    @Override public Enums.Energy getType() { return type; }
    @Override public final void setHealth( float health ) { this.health = health; }
    @Override public final Vector2 getKnockback() { return Constants.ZOOMBA_KNOCKBACK; }
    @Override public Enums.Orientation getOrientation() { return orientation; }
    @Override public Enums.Direction getDirectionX() { return direction; }
    @Override public Enums.Direction getDirectionY() { return direction; }
    @Override public void setDirectionX(Enums.Direction direction) { updateDirection(direction); }
    @Override public void setDirectionY(Enums.Direction direction) { updateDirection(direction); }
    @Override public void convert() {
        position.set(startingPosition);
        updateOrientation(Helpers.getOppositeOrientation(orientation));
        this.converted = true;
    }
    @Override public boolean isConverted() { return converted; }
    @Override public final boolean isDense() { return true; }
    public int getMountDamage() { return Constants.ZOOMBA_STANDARD_DAMAGE; }
    public Vector2 getMountKnockback() { return Constants.ZOOMBA_KNOCKBACK; }
    public Direction getDirection() { return direction; }
    public final long getStartTime() { return startTime; }
    public Rectangle getHazardBounds() { return hazardBounds; }
    private void updateOrientation(Enums.Orientation orientation) {
        this.orientation = orientation;
        if (orientation == Enums.Orientation.X) {
            bobNadir = startingPosition.y - (range / 2);
            direction = Direction.RIGHT;
        } else {
            bobNadir = startingPosition.x - (range / 2);
            direction = Direction.UP;
        }
    }
    private void updateDirection(Direction direction) {
        this.direction = direction;
        switch (this.direction) {
            case LEFT:
                hazardBounds.set(position.x, getBottom() + 5, getWidth() / 2, getHeight() - 10);
                animation = animations.get(0);
                break;
            case RIGHT:
                hazardBounds.set(getLeft(), getBottom() + 5, getWidth() / 2, getHeight() - 10);
                animation = animations.get(1);
                break;
            case DOWN:
                hazardBounds.set(getLeft() + 5, position.y, getWidth() - 10, getHeight() / 2);
                animation = animations.get(2);
                break;
            case UP:
                hazardBounds.set(getLeft() + 5, getBottom(), getWidth() - 10, getHeight() / 2);
                animation = animations.get(3);
                break;
        }
    }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_HIGH;
    }
}

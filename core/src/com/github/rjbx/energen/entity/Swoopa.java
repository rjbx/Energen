package com.github.rjbx.energen.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.ChaseCam;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;

// mutable
public class Swoopa extends Hazard implements Destructible, Vehicular, Groundable {

    // fields
    public final static String TAG = Swoopa.class.getName();

    private final long startTime;
    private Vector2 velocity; // class-level instantiation
    private Vector2 position;
    private final Vector2 startPosition;
    private final Enums.Direction direction;
    private final Enums.Energy type;
    private float health;
    private long descentStartTime;
    private Animation<TextureRegion> animation;
    private Sound sound;
    private boolean active;

    // ctor
    public Swoopa(Vector2 position, Enums.Direction direction, Enums.Energy type) {
        this.position = position;
        startPosition = position;
        this.direction = direction;
        this.type = type;
        velocity = new Vector2(Helpers.speedToVelocity(3, direction, Enums.Orientation.X), -5);
        startTime = TimeUtils.nanoTime();
        health = Constants.SWOOPA_MAX_HEALTH;
        sound = AssetManager.getInstance().getSoundAssets().flight;
        switch (type) {
            case ORE:
                animation = AssetManager.getInstance().getSwoopaAssets().oreSwoopa;
                break;
            case PLASMA:
                animation = AssetManager.getInstance().getSwoopaAssets().plasmaSwoopa;
                break;
            case GAS:
                if (direction == Enums.Direction.LEFT) {
                    animation = AssetManager.getInstance().getSwoopaAssets().gasSwoopaLeft;
                } else {
                    animation = AssetManager.getInstance().getSwoopaAssets().gasSwoopaRight;
                }
                break;
            case LIQUID:
                animation = AssetManager.getInstance().getSwoopaAssets().liquidSwoopa;
                break;
            case SOLID:
                animation = AssetManager.getInstance().getSwoopaAssets().solidSwoopa;
                break;
            default:
                if (direction == Enums.Direction.LEFT) {
                    animation = AssetManager.getInstance().getSwoopaAssets().gasSwoopaLeft;
                } else {
                    animation = AssetManager.getInstance().getSwoopaAssets().gasSwoopaRight;
                }
        }
    }

    @Override
    public Swoopa safeClone() {
        Swoopa clone = new Swoopa(position, direction, type);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    public void update(float delta) {
        if (active) {
            Viewport viewport = ChaseCam.getInstance().getViewport();
            Vector2 worldSpan = new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight());
            Vector3 camera = new Vector3(viewport.getCamera().position);
            // while the swoopa is within a screens' width from the screen center on either side, permit movement
            if (Helpers.betweenTwoValues(position.x, (camera.x - worldSpan.x), (camera.x + worldSpan.x))
                    && Helpers.betweenTwoValues(position.y, (camera.y - (worldSpan.y * 1.5f)), (camera.y + (worldSpan.y * 1.5f)))) {
                if (descentStartTime == 0) {
                    sound.play();
                    descentStartTime = TimeUtils.nanoTime();
                }
                if (Helpers.secondsSince(descentStartTime) < .5f) {
                    velocity.x /= 1.1f;
                    velocity.y /= 1.1f;
                } else {
                    velocity.x = Helpers.speedToVelocity(Math.min(Constants.SWOOPA_MOVEMENT_SPEED, Helpers.speedToVelocity(velocity.x, direction, Enums.Orientation.X) * 1.0375f), direction, Enums.Orientation.X);
                    velocity.y = 0;
                }
            }
            position.mulAdd(velocity, delta);

            // when the swoopa progresses past the center screen position with a margin of ten screen widths, reset x and y position
            if (position.x > (camera.x + Math.abs(worldSpan.x * 2))) {
                descentStartTime = 0;
                position.x = startPosition.x - Helpers.speedToVelocity(worldSpan.x + 1, direction, Enums.Orientation.X);
//            position.y = Avatar.getInstance().getTop() + Constants.SWOOPA_COLLISION_HEIGHT;
                velocity.set(Helpers.speedToVelocity(5, direction, Enums.Orientation.X), -5);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime), true), position, Constants.SWOOPA_CENTER);
    }

    @Override public Vector2 getPosition() { return position; }
    @Override public Vector2 getVelocity() { return velocity; }
    @Override public final float getHealth() { return health; }
    @Override public final float getWidth() { return Constants.SWOOPA_COLLISION_WIDTH; }
    @Override public final float getHeight() { return Constants.SWOOPA_COLLISION_HEIGHT; }
    @Override public final float getLeft() { return position.x - Constants.SWOOPA_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.SWOOPA_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.SWOOPA_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.SWOOPA_CENTER.y; }
    @Override public final float getShotRadius() { return Constants.SWOOPA_SHOT_RADIUS; }
    @Override public final int getHitScore() { return Constants.SWOOPA_HIT_SCORE; }
    @Override public final int getKillScore() { return Constants.SWOOPA_KILL_SCORE; }
    @Override public final int getDamage() { return Constants.SWOOPA_STANDARD_DAMAGE; }
    @Override public final Vector2 getKnockback() { return Constants.SWOOPA_KNOCKBACK; }
    @Override public final Enums.Energy getType() { return type; }
    @Override public final boolean isDense() { return true; }
    @Override public final void setHealth(float health ) { this.health = health; }
    public int getMountDamage() { return Constants.SWOOPA_STANDARD_DAMAGE; }
    public Vector2 getMountKnockback() { return Constants.SWOOPA_KNOCKBACK; }
    public final long getStartTime() { return startTime; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_HIGH;
    }

    public void dispose() {
        sound.stop();
    }
}

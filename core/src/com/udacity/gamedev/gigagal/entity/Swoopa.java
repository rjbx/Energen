package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

// mutable
public class Swoopa extends Hazard implements Destructible, Vehicular, Groundable {

    // fields
    public final static String TAG = Swoopa.class.getName();

    private final long startTime;
    private final float bobOffset;
    private Vector2 velocity; // class-level instantiation
    private Vector2 position;
    private final Enums.Direction direction;
    private final Enums.Material type;
    private float health;
    private long descentStartTime;
    private Animation animation;
    private Sound sound;

    // ctor
    public Swoopa(Vector2 position, Enums.Direction direction, Enums.Material type) {
        this.position = position;
        this.direction = direction;
        this.type = type;
        velocity = new Vector2(Helpers.absoluteToDirectionalValue(5, direction, Enums.Orientation.X), -5);
        startTime = TimeUtils.nanoTime();
        health = Constants.SWOOPA_MAX_HEALTH;
        bobOffset = MathUtils.random();
        sound = Assets.getInstance().getSoundAssets().flight;
        switch (type) {
            case ORE:
                animation = Assets.getInstance().getSwoopaAssets().oreSwoopa;
                break;
            case PLASMA:
                animation = Assets.getInstance().getSwoopaAssets().plasmaSwoopa;
                break;
            case GAS:
                if (direction == Enums.Direction.LEFT) {
                    animation = Assets.getInstance().getSwoopaAssets().gasSwoopaLeft;
                } else {
                    animation = Assets.getInstance().getSwoopaAssets().gasSwoopaRight;
                }
                break;
            case LIQUID:
                animation = Assets.getInstance().getSwoopaAssets().liquidSwoopa;
                break;
            case SOLID:
                animation = Assets.getInstance().getSwoopaAssets().solidSwoopa;
                break;
            default:
                if (direction == Enums.Direction.LEFT) {
                    animation = Assets.getInstance().getSwoopaAssets().gasSwoopaLeft;
                } else {
                    animation = Assets.getInstance().getSwoopaAssets().gasSwoopaRight;
                }
        }
    }

    public void update(float delta) {
        Viewport viewport = LevelUpdater.getInstance().getViewport();
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
                velocity.x = Helpers.absoluteToDirectionalValue(Math.min(10, Helpers.absoluteToDirectionalValue(velocity.x, direction, Enums.Orientation.X) * 1.0375f), direction, Enums.Orientation.X);
                velocity.y = 0;
            }
        }
        position.add(velocity);

        // when the swoopa progresses past the center screen position with a margin of ten screen widths, reset x and y position
        if (position.x > (camera.x + Math.abs(worldSpan.x * 20))) {
            descentStartTime = 0;
            position.x = camera.x - Helpers.absoluteToDirectionalValue(worldSpan.x + 1, direction, Enums.Orientation.X);
            position.y = GigaGal.getInstance().getTop() + Constants.SWOOPA_COLLISION_HEIGHT;
            velocity.set(Helpers.absoluteToDirectionalValue(5, direction, Enums.Orientation.X), -5);
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
    @Override public final Enums.Material getType() { return type; }
    @Override public final boolean isDense() { return true; }
    public int getMountDamage() { return Constants.SWOOPA_STANDARD_DAMAGE; }
    public Vector2 getMountKnockback() { return Constants.SWOOPA_KNOCKBACK; }
    public final long getStartTime() { return startTime; }

    @Override public final void setHealth( float health ) { this.health = health; }

    public void dispose() {
        sound.stop();
    }
}

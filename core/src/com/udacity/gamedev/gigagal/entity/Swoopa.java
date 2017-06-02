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
public class Swoopa implements Destructible, Hazard {

    // fields
    public final static String TAG = Swoopa.class.getName();

    private final long startTime;
    private final float bobOffset;
    private LevelUpdater level;
    private Vector2 velocity; // class-level instantiation
    private Vector2 position;
    private Enums.Material type;
    private float health;
    private long descentStartTime;
    private Animation animation;
    private Sound sound;

    // ctor
    public Swoopa(LevelUpdater level, Vector2 position, Enums.Material type) {
        this.level = level;
        this.position = position;
        this.type = type;
        velocity = new Vector2();
        startTime = TimeUtils.nanoTime();
        descentStartTime = 0;
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
                animation = Assets.getInstance().getSwoopaAssets().gasSwoopa;
                break;
            case LIQUID:
                animation = Assets.getInstance().getSwoopaAssets().liquidSwoopa;
                break;
            case SOLID:
                animation = Assets.getInstance().getSwoopaAssets().solidSwoopa;
                break;
            default:
                animation = Assets.getInstance().getSwoopaAssets().oreSwoopa;
        }
    }

    public void update(float delta) {
        Vector2 worldSpan = new Vector2(level.getViewport().getWorldWidth(), level.getViewport().getWorldHeight());
        Vector3 camera = new Vector3(level.getViewport().getCamera().position);
        // while the swoopa is witin a screens' width from the screen center on either side, permit movement
        if (Helpers.betweenTwoValues(position.x, (camera.x - worldSpan.x), (camera.x + worldSpan.x))
            && Helpers.betweenTwoValues(position.y, (camera.y - (worldSpan.y * 1.5f)), (camera.y + (worldSpan.y * 1.5f)))) {
            if (descentStartTime == 0) {
                sound.play();
                descentStartTime = TimeUtils.nanoTime();
            }
            if (Helpers.secondsSince(descentStartTime) < .75f) {
                velocity.x = Math.min(-20, velocity.x * 1.01f);
                velocity.y = Math.min(-Constants.SWOOPA_MOVEMENT_SPEED, velocity.y * 1.01f);
            } else {
                velocity.x = velocity.x * 1.035f;
                velocity.y = velocity.y / 1.035f;
            }
        }
        position = position.mulAdd(velocity, delta);

        // when the swoopa progresses past the center screen position with a margin of ten screen widths, reset x and y position
        if (position.x < (camera.x - (worldSpan.x * 20))) {
            descentStartTime = 0;
            position.x = (camera.x + worldSpan.x - 1);
            position.y = (camera.y + (worldSpan.y / 1.5f));
            velocity.set(0, 0);
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime), true), position, Constants.SWOOPA_CENTER);
    }

    @Override public Vector2 getPosition() { return position; }
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
    @Override public Enums.Material getType() { return type; }
    public int getMountDamage() { return Constants.SWOOPA_STANDARD_DAMAGE; }
    public Vector2 getMountKnockback() { return Constants.SWOOPA_KNOCKBACK; }
    public final long getStartTime() { return startTime; }

    @Override public final void setHealth( float health ) { this.health = health; }

    public void dispose() {
        sound.stop();
    }
}

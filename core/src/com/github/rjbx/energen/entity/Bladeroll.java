package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.app.LevelAssets;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.ChaseCam;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Bladeroll extends Hazard implements Armored, Gravitating, Bladed, Groundable, Roving, Destructible, Impermeable, Trippable {

    // fields
    public final static String TAG = Rollen.class.getName();

    private Vector2 position;
    private Vector2 previousFramePosition; // class-level instantiation
    private Enums.Direction xDirection;
    private Enums.Energy type;
    private Vector2 velocity; // class-level instantiation
    private Vector2 center;
    private final float collision;
    private boolean converted;
    private Rectangle bounds;
    private boolean state;
    private int camAdjustments;
    private Enums.Direction direction;
    private float speed;
    private long startTime;
    private float health;
    private float speedAtChangeXDirection;
    private long rollStartTime;
    private float rollTimeSeconds;
    private float radius;
    private Animation<TextureRegion> animation;
    private Array<Enums.Direction> equippedRegions;
    private boolean vulnerable;
    private boolean armorStruck;
    private int vulnerabilityCount;

    // ctor
    public Bladeroll(Vector2 position, Rectangle bounds, Enums.Energy type, float vulnerabilities) {
        this.type = type;
        this.position = position;
        this.vulnerabilityCount = (int) vulnerabilities;
        this.speed = 4f;
        this.center = Constants.ROLLEN_CENTER;
        this.bounds = bounds;
        camAdjustments = 0;
        equippedRegions = new Array<Enums.Direction>();
        vulnerable = false;
        armorStruck = false;
        previousFramePosition = new Vector2();
        velocity = new Vector2(0, 0);
        radius = getWidth() / 2;
        health = Constants.ROLLEN_MAX_HEALTH;
        xDirection = null;
        speedAtChangeXDirection = 0;
        rollStartTime = 0;
        rollTimeSeconds = 0;
        collision = rollTimeSeconds;
        animation = AssetManager.getInstance().getRollenAssets().liquidRollen;
    }

    public Bladeroll safeClone() {
        Bladeroll clone = new Bladeroll(position, bounds, type, vulnerabilityCount);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    public void update(float delta) {
        if (armorStruck) {
            velocity.x = 0;
            if (startTime == 0 || Helpers.secondsSince(startTime) % 1 == 0) {
                rollStartTime = TimeUtils.nanoTime();
                vulnerable = true;
                for (int i = 0; i < vulnerabilityCount; i++) {
                    int index;
                    do {
                        index = MathUtils.random(0, 3);
                    } while (equippedRegions.contains(Enums.Direction.values()[index], true));
                    equippedRegions.add(Enums.Direction.values()[index]);
                }
                animation = AssetManager.getInstance().getArmorolloAssets().vulnerableLiquid;
            } else if (Helpers.secondsSince(startTime) > speed) {
                vulnerable = false;
                armorStruck = false;
                animation = AssetManager.getInstance().getRollenAssets().liquidRollen;
                equippedRegions.clear();
            }
            if (startTime == 0) {
                startTime = TimeUtils.nanoTime();
            }
        } else {
            previousFramePosition.set(position);
            position.mulAdd(velocity, delta);

            Viewport viewport = ChaseCam.getInstance().getViewport();
            Vector2 worldSpan = new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight());
            Vector3 camera = new Vector3(viewport.getCamera().position);
            Vector2 activationDistance = new Vector2(worldSpan.x / 1.5f, worldSpan.y / 1.5f);

            boolean touchingSide = false;
            boolean touchingTop = false;
            boolean canSink = false;
            for (Ground ground : LevelAssets.getClonedGrounds()) {
                if (Helpers.overlapsPhysicalObject(this, ground)) {
                    if (ground instanceof Pourous) {
                        canSink = true;
                    }
                    if (ground.isDense()) {
                        if (Helpers.overlapsBetweenTwoSides(position.x, radius, ground.getLeft(), ground.getRight())
                                && !(Helpers.overlapsBetweenTwoSides(previousFramePosition.x, radius, ground.getLeft(), ground.getRight()))) {
                            touchingSide = true;
                            if (position.x < ground.getPosition().x) {
                                velocity.x -= 5;
                            } else {
                                velocity.x += 5;
                            }
                        }
                    }
                    if (Helpers.overlapsBetweenTwoSides(position.y, radius, ground.getBottom(), ground.getTop())
                            && !(Helpers.overlapsBetweenTwoSides(previousFramePosition.y, radius, ground.getBottom(), ground.getTop()))) {
                        if (!canSink) {
                            touchingTop = true;
                        }
                    }
                }
            }

            if (touchingTop) {
                velocity.y = 0;
                position.y = previousFramePosition.y;
                if (Helpers.betweenFourValues(position, camera.x - activationDistance.x, camera.x + activationDistance.x, camera.y - activationDistance.y, camera.y + activationDistance.y)) {
                    if ((position.x >= camera.x - activationDistance.x) && (position.x < camera.x)) {
                        xDirection = Enums.Direction.RIGHT;
                    } else if ((position.x < camera.x + activationDistance.x) && (position.x >= camera.x)) {
                        xDirection = Enums.Direction.LEFT;
                    }
                } else {
                    xDirection = null;
                    startTime = 0;
                    velocity.x = 0;
                }

                if (xDirection != null) {
                    if (rollStartTime == 0) {
                        speedAtChangeXDirection = velocity.x;
                        rollStartTime = TimeUtils.nanoTime();
                    }
                    rollTimeSeconds = Helpers.secondsSince(rollStartTime);

                    velocity.x = speedAtChangeXDirection + Helpers.speedToVelocity(Math.min(Constants.ROLLEN_MOVEMENT_SPEED * rollTimeSeconds, Constants.ROLLEN_MOVEMENT_SPEED), xDirection, Enums.Orientation.X);
                }
                for (Hazard hazard : LevelAssets.getClonedHazards()) {
                    if (hazard instanceof Rollen && Helpers.overlapsPhysicalObject(this, hazard) && !(hazard.equals(this))) {
                        position.set(previousFramePosition);
                        if (!touchingSide && position.x < hazard.getPosition().x) {
                            velocity.x -= 5;
                        } else {
                            velocity.x += 5;
                        }
                    }
                }
            } else {
                if (!canSink) {
                    velocity.y -= Constants.GRAVITY;
                } else {
                    velocity.y = -8;
                }
            }

            if (touchingSide) {
                xDirection = null;
                startTime = 0;
                velocity.x = 0;
                position.x = previousFramePosition.x;
                rollStartTime = TimeUtils.nanoTime();

                if (canSink) {
                    velocity.y = -5;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        center = Constants.ROLLEN_CENTER;
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(rollTimeSeconds, true), position, center, Constants.ROLLEN_TEXTURE_SCALE);
        if (armorStruck) {
            boolean flipX;
            float frame = 0;
            if (xDirection == Enums.Direction.RIGHT) {
                animation.setPlayMode(Animation.PlayMode.REVERSED);
                flipX = false;
            } else {
                animation.setPlayMode(Animation.PlayMode.NORMAL);
                flipX = true;
            }
            if (Helpers.secondsSince(startTime) > speed - Constants.FLIPSWIPE_FRAME_DURATION * 6) {
                frame = (Helpers.secondsSince(startTime) - (speed - Constants.FLIPSWIPE_FRAME_DURATION * 6)) / 2;
                center = Constants.BLADE_CENTER;
            }
            for (Enums.Direction region : equippedRegions) {
                switch (region) {
                    case LEFT:
                        Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getBladeAssets().solidForehand.getKeyFrame(frame, true), position, Constants.BLADE_CENTER, 1, 0, true, false);
                        break;
                    case RIGHT:
                        Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getBladeAssets().solidForehand.getKeyFrame(frame, true), position, Constants.BLADE_CENTER, 1, 0, false, false);
                        break;
                    case DOWN:
                        Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getBladeAssets().solidUppercut.getKeyFrame(frame, true), position, Constants.BLADE_CENTER, 1, 0, flipX, false);
                        break;
                    case UP:
                        Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getBladeAssets().solidUppercut.getKeyFrame(frame, true), position, Constants.BLADE_CENTER, 1, 0, flipX, true);
                        break;
                }
            }
        }
    }

    @Override public Vector2 getPosition() { return position; }
    public Vector2 getVelocity() { return velocity; }
    @Override public final float getHealth() { return health; }
    @Override public final float getWidth() { return center.x * 2; }
    @Override public final float getHeight() { return center.y * 2; }
    @Override public final float getLeft() { return position.x - center.x; }
    @Override public final float getRight() { return position.x + center.x; }
    @Override public final float getTop() { return position.y + center.y; }
    @Override public final float getBottom() { return position.y - center.y; }
    @Override public final float getShotRadius() { return Constants.ROLLEN_SHOT_RADIUS; }
    @Override public final int getHitScore() { return Constants.ROLLEN_HIT_SCORE; }
    @Override public final int getKillScore() { return Constants.ROLLEN_KILL_SCORE; }
    @Override public final int getDamage() { return Constants.ROLLEN_STANDARD_DAMAGE; }
    @Override public final Vector2 getKnockback() { return Constants.ROLLEN_KNOCKBACK; }
    @Override public final void setHealth( float health ) { this.health = health; }
    @Override public final Enums.Energy getType() { return type; }
    @Override public Enums.Direction getDirectionX() { return xDirection; }
    @Override public void setDirectionX(Enums.Direction direction) { xDirection = direction; }
    @Override public void strikeArmor() { armorStruck = true; }
    @Override public boolean isVulnerable() { return vulnerable; }
    @Override public Enums.Direction getVulnerability() { return null; }
    @Override public Array<Enums.Direction> getEquippedRegions() { return equippedRegions; }
    @Override public final void resetStartTime() { startTime = 0; }
    @Override public final boolean isDense() { return true; }
    @Override public final long getStartTime() { return startTime; }
    @Override public final float getRecoverySpeed() { return speed; }

    @Override public void setState(boolean state) { this.state = !state; }
    @Override public boolean tripped() { return state; }
    @Override public boolean isActive() { return state; }
    @Override public void addCamAdjustment() { camAdjustments++; }
    @Override public boolean maxAdjustmentsReached() { return camAdjustments >= 2; }
    @Override public Rectangle getConvertBounds() { return bounds; }
    @Override public boolean isConverted() { return state; }
    @Override public void convert() { state = !state; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}

package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public abstract class Roller extends Hazard implements MultidirectionalX, Destructible {
    // fields
    public final static String TAG = Rollen.class.getName();

    protected LevelUpdater level;
    protected Vector2 position;
    protected Vector2 previousFramePosition; // class-level instantiation
    protected Enums.Direction xDirection;
    protected Enums.Material type;
    protected Vector2 velocity; // class-level instantiation
    protected long startTime;
    protected float speedAtChangeXDirection;
    protected long rollStartTime;
    protected float rollTimeSeconds;
    protected float radius;
    protected float collision;

    // default ctor
    public Roller() {}

    // ctor
    public Roller(LevelUpdater level, Vector2 position, Enums.Material type) {
        this.level = level;
        this.type = type;
        this.position = position;
        previousFramePosition = new Vector2();
        velocity = new Vector2(0, 0);
        radius = getWidth() / 2;
        xDirection = null;
        speedAtChangeXDirection = 0;
        rollStartTime = 0;
        rollTimeSeconds = 0;
        collision = rollTimeSeconds;
        collision = rollTimeSeconds;
    }

    public void update(float delta) {
        previousFramePosition.set(position);
        position.mulAdd(velocity, delta);

        Viewport viewport = level.getViewport();
        Vector2 worldSpan = new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight());
        Vector3 camera = new Vector3(viewport.getCamera().position);
        Vector2 activationDistance = new Vector2(worldSpan.x / 1.5f, worldSpan.y / 1.5f);

        boolean touchingSide = false;
        boolean touchingTop = false;
        for (Ground ground : LevelUpdater.getInstance().getGrounds()) {
            if (Helpers.overlapsPhysicalObject(this, ground)) {
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
                    touchingTop = true;
                }
            }
        }

        if (touchingTop) {
            velocity.y = 0;
            position.y = previousFramePosition.y;
            if ((position.x < camera.x - activationDistance.x)
                    || (position.x > camera.x + activationDistance.x)) {
                xDirection = null;
                startTime = 0;
                velocity.x = 0;
            } else if ((position.x >= camera.x - activationDistance.x) && (position.x < camera.x)) {
                xDirection = Enums.Direction.RIGHT;
            } else if ((position.x < camera.x + activationDistance.x) && (position.x >= camera.x)) {
                xDirection = Enums.Direction.LEFT;
            }

            if (xDirection != null) {
                if (rollStartTime == 0) {
                    speedAtChangeXDirection = velocity.x;
                    rollStartTime = TimeUtils.nanoTime();
                }
                rollTimeSeconds = Helpers.secondsSince(rollStartTime);
                velocity.x = speedAtChangeXDirection + Helpers.absoluteToDirectionalValue(Math.min(Constants.ROLLEN_MOVEMENT_SPEED * rollTimeSeconds, Constants.ROLLEN_MOVEMENT_SPEED), xDirection, Enums.Orientation.X);
            }
            for (Hazard hazard : LevelUpdater.getInstance().getHazards()) {
                if (hazard instanceof Roller && Helpers.overlapsPhysicalObject(this, hazard)) {
                    position.set(previousFramePosition);
                    if (!touchingSide && position.x < hazard.getPosition().x) {
                        velocity.x -= 5;
                    } else {
                        velocity.x += 5;
                    }
                }
            }
        } else {
            velocity.y -= Constants.GRAVITY;
        }

        if (touchingSide) {
            xDirection = null;
            startTime = 0;
            velocity.x = 0;
            position.x = previousFramePosition.x;
            rollStartTime = TimeUtils.nanoTime();
            rollTimeSeconds = 0;
        }
    }

    @Override public Vector2 getPosition() { return position; }
    @Override public final Enums.Material getType() { return type; }
    @Override public Enums.Direction getDirectionX() { return xDirection; }
    @Override public void setDirectionX(Enums.Direction direction) { xDirection = direction; }
}

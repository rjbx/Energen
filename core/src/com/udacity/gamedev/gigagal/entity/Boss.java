package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.ChaseCam;
import com.udacity.gamedev.gigagal.util.InputControls;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class Boss implements Humanoid, Destructible, com.udacity.gamedev.gigagal.entity.Hazard {
    
    // fields
    public final static String TAG = Boss.class.getName();

    private final LevelUpdater level;
    private final float width;
    private final float height;
    private final float headRadius;
    private final float eyeHeight;
    private final float halfWidth;
    private float left;
    private float right;
    private float top;
    private float bottom;
    private Rectangle bounds; // class-level instantiation
    private Vector2 position; // class-level instantiation
    private Vector2 previousFramePosition; // class-level instantiation
    private Vector2 spawnPosition;
    private Vector3 chaseCamPosition; // class-level instantiation
    private Vector2 velocity; // class-level instantiation
    private Enums.Direction directionX;
    private Enums.Direction directionY;
    private TextureRegion region; // class-level instantiation
    private Enums.Action action;
    private Enums.GroundState groundState;
    private Ground touchedGround; // class-level instantiation
    private Enums.ShotIntensity shotIntensity;
    private Enums.Material weapon;
    private List<Enums.Material> weaponList; // class-level instantiation
    private ListIterator<Enums.Material> weaponToggler; // class-level instantiation
    private boolean onRideable;
    private boolean onSkateable;
    private boolean onUnbearable;
    private boolean onClimbable;
    private boolean onSinkable;
    private boolean onBounceable;
    private boolean canShoot;
    private boolean canLook;
    private boolean canDash;
    private boolean canJump;
    private boolean canHover;
    private boolean canRappel;
    private boolean canClimb;
    private boolean canStride;
    private long chargeStartTime;
    private long lookStartTime;
    private long jumpStartTime;
    private long dashStartTime;
    private long hoverStartTime;
    private long rappelStartTime;
    private long climbStartTime;
    private long strideStartTime;
    private long recoveryStartTime;
    private float chargeTimeSeconds;
    private float lookTimeSeconds;
    private float hoverTimeSeconds;
    private float climbTimeSeconds;
    private float strideTimeSeconds;
    private float strideSpeed;
    private float strideAcceleration;
    private float turboDuration;
    private float startTurbo;
    private float turbo;
    private float killPlane;
    private float ammo;
    private float health;
    private int lives;
    private boolean paused;
    private float pauseTimeSeconds;
    private Rectangle roomBounds;
    private InputControls inputControls;
    private com.udacity.gamedev.gigagal.entity.GigaGal gigaGal;

    public static class Builder {

        private LevelUpdater level;
        private Vector2 spawnPosition;
        private Enums.Material weapon = Enums.Material.NATIVE;
        private float height = Constants.GIGAGAL_HEIGHT;
        private float eyeHeight = Constants.GIGAGAL_EYE_HEIGHT;
        private float width = Constants.GIGAGAL_STANCE_WIDTH;

        public Builder(LevelUpdater level, Vector2 spawnPosition) {
            this.level = level;
            this.spawnPosition = spawnPosition;
        }

        public Builder weapon(Enums.Material weapon) {
            this.weapon = weapon; return this;
        }

        public Builder height(float height) {
            this.height = height; return this;
        }

        public Builder eyeHeight(float eyeHeight) {
            this.eyeHeight = eyeHeight; return this;
        }

        public Builder width(float width) {
            this.width = width;
            return this;
        }

        public Boss build() {
            return new Boss(this);
        }
    }

    private Boss(Builder builder) {
        level = builder.level;
        spawnPosition = builder.spawnPosition;
        position = new Vector2(spawnPosition);
        previousFramePosition = new Vector2();
        chaseCamPosition = new Vector3();
        roomBounds = new Rectangle(spawnPosition.x - 125, spawnPosition.y - 125, 250, 250);
        velocity = new Vector2();
        weapon = builder.weapon;
        height = builder.height;
        eyeHeight = builder.eyeHeight;
        width = builder.width;
        headRadius = height - eyeHeight;
        halfWidth = width / 2;
        respawn();
    }

    public void respawn() {
        position.set(spawnPosition);
        chaseCamPosition.set(position, 0);
        left = position.x - halfWidth;
        right = position.x + halfWidth;
        top = position.y + headRadius;
        bottom = position.y - eyeHeight;
        bounds = new Rectangle(left, bottom, width, height);
        velocity.setZero();
        directionX = Enums.Direction.RIGHT;
        action = Enums.Action.FALLING;
        groundState = Enums.GroundState.AIRBORNE;
        ammo = Constants.INITIAL_AMMO;
        health = Constants.INITIAL_HEALTH;
        turbo = Constants.MAX_TURBO;
        shotIntensity = Enums.ShotIntensity.NORMAL;
        startTurbo = turbo;
        turboDuration = 0;
        touchedGround = null;
        paused = false;
        canClimb = false;
        canLook = false;
        canStride = false;
        canJump = false;
        canDash = false;
        canHover = false;
        canRappel = false;
        canShoot = true;
        onRideable = false;
        onSkateable = false;
        onUnbearable = false;
        onClimbable = false;
        onSinkable = false;
        onBounceable = false;
        chargeStartTime = 0;
        strideStartTime = 0;
        climbStartTime = 0;
        jumpStartTime = 0;
        dashStartTime = 0;
        pauseTimeSeconds = 0;
        turboDuration = 0;
        recoveryStartTime = TimeUtils.nanoTime();
        pauseTimeSeconds = 0;
    }

    public void update(float delta) {
        gigaGal = level.getGigaGal();
        // checks inner condition only when can state != boss; once set, does not revert
        if (ChaseCam.getInstance().getState() != Enums.ChaseCamState.BOSS) {
            if (this.getRoomBounds().overlaps(GigaGal.getInstance().getBounds())) {
                ChaseCam.getInstance().setState(Enums.ChaseCamState.BOSS);
            }
        }

        // positioning
        previousFramePosition.set(position);
        position.mulAdd(velocity, delta);
        setBounds();

        // collision detection
//        touchGround(level.getGrounds());
//        touchHazards(level.getHazards());
//        touchPowerups(level.getPowerups());

        // abilities
        if (groundState == Enums.GroundState.PLANTED) {
            velocity.y = 0;
            /*if (action == Enums.Action.STANDING) {
                stand();
                enableStride();
                enableDash();
                enableClimb(); // must come before jump (for now)
                enableJump();
                enableShoot(weapon);
            } else if (action == Enums.Action.STRIDING) {
                enableStride();
                enableDash();
                enableJump();
                enableShoot(weapon);
            } else if (action == Enums.Action.CLIMBING) {
                enableClimb();
                enableShoot(weapon);
            } else if (action == Enums.Action.DASHING) {
                enableDash();
                enableJump();
                enableShoot(weapon);
            }*/
        } else if (groundState == Enums.GroundState.AIRBORNE) {
            velocity.y -= Constants.GRAVITY;
            /*if (action == Enums.Action.FALLING) {
                fall();
                enableClimb();
                enableHover();
                enableRappel();
                enableShoot(weapon);
            } else if (action == Enums.Action.JUMPING) {
                enableJump();
                enableRappel();
                enableShoot(weapon);
            } else if (action == Enums.Action.HOVERING) {
                enableHover();
                enableRappel();
                enableClimb();
                enableShoot(weapon);
            } else if (action == Enums.Action.RAPPELLING) {
                enableJump();
                enableRappel();
                enableShoot(weapon);
            } else if (action == Enums.Action.RECOILING) {
                enableRappel();
                enableShoot(weapon);
            }*/
        }

        rush();
    }

    private void setBounds() {
        left = position.x - halfWidth;
        right = position.x + halfWidth;
        top = position.y + headRadius;
        bottom = position.y - eyeHeight;
        bounds = new Rectangle(left, bottom, width, height);
    }
    
    private void rush() {
        if (gigaGal.getDirectionX() != this.getDirectionX()) {
            if (Math.abs(gigaGal.getVelocity().x) > Constants.GIGAGAL_MAX_SPEED / 2) {
                stride();
            } else if (Math.abs(gigaGal.getPosition().x - this.position.x) > 5) {
                dash();
            } else {
                jump();
                if (Math.abs(gigaGal.getPosition().x - this.position.x) < 5) {
                    directionY = Enums.Direction.DOWN;
                    look();
                    attack();
                }
            }

            if (Math.abs(gigaGal.getPosition().y - this.position.y) > 15) {
                if (Math.abs(gigaGal.getPosition().x - this.position.x) > 5) {
                    dash();
                } else {
                    directionY = Enums.Direction.UP;
                    look();
                    attack();
                }
            } else if (Math.abs(gigaGal.getPosition().x - this.position.x) > 10) {
                stride();
            }
        }
    }
    
    private void attack() {
        
    }

    private void touchGround(DelayedRemovalArray<Ground> grounds) {
        onUnbearable = false;
        onRideable = false;
        onSkateable = false;
        onClimbable = false;
        onSinkable = false;
        for (Ground ground : grounds) {
            // if currently within ground left and right sides
            if (Helpers.overlapsBetweenTwoSides(position.x, getHalfWidth(), ground.getLeft(), ground.getRight())) {
                // apply following rules (bump side and bottom) only if ground height > ledge height
                // ledges only apply collision detection on top, and not on sides and bottom as do grounds
                if (getBottom() <= ground.getTop() && getTop() >= ground.getBottom()) {
                    if (ground.getHeight() > Constants.MAX_LEDGE_HEIGHT) {
                        touchGroundSide(ground);
                        touchGroundBottom(ground);
                    } else {
                        canRappel = false; // deactivate rappel if ground below max ledge height
                    }
                    touchGroundTop(ground);
                   // if below minimum ground distance while descending excluding post-rappel, disable rappel and hover
                    // caution when crossing plane between ground top and minimum hover height / ground distance
                    // cannons, which inherit ground, can be mounted along sides of grounds causing accidental plane breakage
                    if (getBottom() < (ground.getTop() + Constants.MIN_GROUND_DISTANCE)
                            && getBottom() > ground.getTop() // GG's bottom is greater than ground top but less than boundary
                            && velocity.y < 0 // prevents disabling features when crossing boundary while ascending on jump
                            && rappelStartTime == 0 // only if have not rappeled since last grounded
                            && !(ground instanceof com.udacity.gamedev.gigagal.entity.Cannon) // only if ground is not instance of cannon
                            ) {
                        canRappel = false; // disables rappel
                        canHover = false; // disables hover
                    }
                }
            }
        }
        untouchGround();
    }

    private void touchGroundSide(Ground ground) {
        // if during previous frame was not, while currently is, between ground left and right sides
        if (!Helpers.overlapsBetweenTwoSides(previousFramePosition.x, getHalfWidth(), ground.getLeft(), ground.getRight())) {
            // only when not grounded and not recoiling
            if (groundState != Enums.GroundState.PLANTED) {
                // if x velocity (magnitude, without concern for direction) greater than one third max speed,
                // boost x velocity by starting speed, enable rappel, verify rappelling ground and capture rappelling ground boundaries
                if (Math.abs(velocity.x) > Constants.GIGAGAL_MAX_SPEED / 4) {
                    // if already rappeling, halt x progression
                    if (action != Enums.Action.RAPPELLING) {
                        canRappel = true; // enable rappel
                        touchedGround = ground;
                        killPlane = touchedGround.getBottom() + Constants.KILL_PLANE;
                    }
                    // if absval x velocity not greater than one fourth max speed but aerial and bumping ground side, fall
                } else {
                    // if not already hovering and descending, also disable hover
                    if (action != Enums.Action.HOVERING && velocity.y < 0) {
                        canHover = false; // disable hover
                    }
                    canRappel = false;
                    fall(); // fall regardless of whether or not inner condition met
                }
                // only when planted
            } else if (groundState == Enums.GroundState.PLANTED) {
                if (Math.abs(getBottom() - ground.getTop()) > 1) {
                    strideSpeed = 0;
                    velocity.x = 0;
                }
                if (action == Enums.Action.DASHING) {
                    stand(); // deactivates dash when bumping ground side
                }
            }
            if ((!(ground instanceof Rideable && (Math.abs(getBottom() - ground.getTop()) <= 1)))
                    && !(ground instanceof Skateable && (Math.abs(getBottom() - ground.getTop()) <= 1))
                    && !(ground instanceof Unbearable && (Math.abs(getBottom() - ground.getTop()) <= 1))) {
                // if contact with ground sides detected without concern for ground state (either grounded or airborne),
                // reset stride acceleration, disable stride and dash, and set gigagal at ground side
                if (action != Enums.Action.STRIDING || action != Enums.Action.DASHING) {
                    strideStartTime = 0; // reset stride acceleration
                }
                canStride = false; // disable stride
                canDash = false; // disable dash
                position.x = previousFramePosition.x; // halt x progression
            }
        } else {
            canRappel = false;
        }
    }

    private void touchGroundBottom(Ground ground) {
        // if contact with ground bottom detected, halts upward progression and set gigagal at ground bottom
        if ((previousFramePosition.y + Constants.GIGAGAL_HEAD_RADIUS) <= ground.getBottom()) {
            velocity.y = 0; // prevents from ascending above ground bottom
            position.y = previousFramePosition.y;  // sets gigagal at ground bottom
            fall(); // descend from point of contact with ground bottom
        }
    }

    private void touchGroundTop(Ground ground) {
        // if contact with ground top detected, halt downward progression and set gigagal atop ground
        if ((getBottom() <= ground.getTop() && (!canRappel || (touchedGround != null && ground.getTop() != touchedGround.getTop())))
                && (previousFramePosition.y - Constants.GIGAGAL_EYE_HEIGHT >= ground.getTop() - 1)) {
            velocity.y = 0; // prevents from descending beneath ground top
            position.y = ground.getTop() + Constants.GIGAGAL_EYE_HEIGHT; // sets Gigagal atop ground
            setAtopGround(ground);
            if (action != Enums.Action.DASHING) {
                pauseTimeSeconds = 0;
            }
            if (ground instanceof Skateable) {
                onSkateable = true;
                if (groundState == Enums.GroundState.AIRBORNE) {
                    stand(); // set groundstate to standing
                    lookStartTime = 0;
                }
            } else if (ground instanceof Hoverable) {
                lookStartTime = 0;
                Hoverable hoverable = (Hoverable) ground;
                Enums.Orientation orientation = hoverable.getOrientation();
                Enums.Direction direction = hoverable.getDirection();
                if (orientation == Enums.Orientation.X) {
                    velocity.x = hoverable.getVelocity().x;
                    position.x += velocity.x;
                }
                if (direction == Enums.Direction.DOWN) {
                    position.y -= 1;
                }
            } else if (ground instanceof Reboundable) {
                onBounceable = true;
                Reboundable reboundable = (Reboundable) ground;
                reboundable.setState(true);
            } else if (ground instanceof Rideable) {
                onRideable = true;
            } else if (ground instanceof Unbearable) {
                onUnbearable = true;
                canHover = false;
                Random xKnockback = new Random();
                velocity.set(Helpers.absoluteToDirectionalValue(xKnockback.nextFloat() * 200, directionX, Enums.Orientation.X), Constants.PROTRUSION_GAS_KNOCKBACK.y);
                recoil(velocity);
            }
        }
    }

    private void touchDescendableGround(Ground ground) {
        if (ground instanceof Sinkable) {
            setAtopGround(ground);
            onSinkable = true;
            canDash = false;
            canHover = false;
            canClimb = false;
            lookStartTime = 0;
            lookTimeSeconds = 0;
        } else if (ground instanceof Climbable) {
            if (Helpers.betweenTwoValues(position.x, ground.getLeft(), ground.getRight())) {
                if (getTop() > ground.getBottom()) {
                    onClimbable = true;
                }
            }
            if (climbTimeSeconds == 0) {
                if ((getBottom() <= ground.getTop() && (!canRappel || (touchedGround != null && ground.getTop() != touchedGround.getTop()))
                        && previousFramePosition.y - Constants.GIGAGAL_EYE_HEIGHT >= ground.getTop())
                        || canClimb && climbStartTime != 0) {
                    setAtopGround(ground);
                    if (action != Enums.Action.CLIMBING) {
                        velocity.y = 0; // prevents from descending beneath ground top
                        position.y = ground.getTop() + Constants.GIGAGAL_EYE_HEIGHT; // sets Gigagal atop ground
                    }
                }
                if (action != Enums.Action.CLIMBING) {
                    if (canClimb /*&& !inputControls.jumpButtonPressed*/ && action == Enums.Action.STANDING) {
                        if (!(ground instanceof com.udacity.gamedev.gigagal.entity.Pole)) {
                            canJump = true;
                        }
                        jump();
                    }
                }
            }
        }
    }

    private void setAtopGround(Ground ground) {
        touchedGround = ground;
        killPlane = touchedGround.getBottom() + Constants.KILL_PLANE;
        hoverStartTime = 0;
        rappelStartTime = 0;
        canLook = true;
        canHover = false;
        if (groundState == Enums.GroundState.AIRBORNE && !(ground instanceof Skateable)) {
            stand(); // set groundstate to standing
            lookStartTime = 0;
        }
    }

    private void untouchGround() {
        if (touchedGround != null && action != Enums.Action.HOVERING) {
            if (getBottom() > touchedGround.getTop() || getTop() < touchedGround.getBottom())
                /*(!Helpers.overlapsBetweenTwoSides(position.y, (getTop() - getBottom()) / 2, touchedGround.getBottom(), touchedGround.getTop()) */{
                if (onBounceable) {
                    Reboundable reboundable = (Reboundable) touchedGround;
                    reboundable.resetStartTime();
                    reboundable.setState(false);
                    onBounceable = false;
                }
                canRappel = false;
                fall();
            } else if (!Helpers.overlapsBetweenTwoSides(position.x, getHalfWidth(), touchedGround.getLeft(), touchedGround.getRight())) {
                if (onBounceable) {
                    Reboundable reboundable = (Reboundable) touchedGround;
                    reboundable.resetStartTime();
                    reboundable.setState(false);
                    onBounceable = false;
                }
                onSinkable = false;
                lookTimeSeconds = 0;
                lookStartTime = TimeUtils.nanoTime();
                if (action != Enums.Action.RAPPELLING) {
                    fall();
                }
            }
        }
    }

    // detects contact with enemy (change aerial & ground state to recoil until grounded)
    private void touchHazards(DelayedRemovalArray<com.udacity.gamedev.gigagal.entity.Hazard> hazards) {
        for (com.udacity.gamedev.gigagal.entity.Hazard hazard : hazards) {
            if (!(hazard instanceof com.udacity.gamedev.gigagal.entity.Ammo && ((com.udacity.gamedev.gigagal.entity.Ammo) hazard).isFromGigagal())) {
                float recoveryTimeSeconds = Helpers.secondsSince(recoveryStartTime) - pauseTimeSeconds;
                if (action != Enums.Action.RECOILING && recoveryTimeSeconds > Constants.RECOVERY_TIME) {
                    Rectangle bounds = new Rectangle(hazard.getLeft(), hazard.getBottom(), hazard.getWidth(), hazard.getHeight());
                    if (getBounds().overlaps(bounds)) {
                        recoveryStartTime = TimeUtils.nanoTime();
                        chaseCamPosition.set(position, 0);
                        Vector2 intersectionPoint = new Vector2();
                        intersectionPoint.x = Math.max(getBounds().x, bounds.x);
                        intersectionPoint.y = Math.max(getBounds().y, bounds.y);
                        level.spawnImpact(intersectionPoint, hazard.getType());
                        int damage = hazard.getDamage();
                        float margin = 0;
                        if (hazard instanceof Destructible) {
                            margin = hazard.getWidth() / 6;
                        }
                        if (position.x < (hazard.getPosition().x - (hazard.getWidth() / 2) + margin)) {
                            if (hazard instanceof Swoopa) {
                                Swoopa swoopa = (Swoopa) hazard;
                                recoil(new Vector2(-swoopa.getMountKnockback().x, swoopa.getMountKnockback().y));
                                damage = swoopa.getMountDamage();
                            } else {
                                recoil(new Vector2(-hazard.getKnockback().x, hazard.getKnockback().y));
                            }
                        } else if (position.x > (hazard.getPosition().x + (hazard.getWidth() / 2) - margin)) {
                            if (hazard instanceof Swoopa) {
                                Swoopa swoopa = (Swoopa) hazard;
                                recoil(swoopa.getMountKnockback());
                                damage = swoopa.getMountDamage();
                            } else {
                                recoil(hazard.getKnockback());
                            }
                        } else {
                            if (hazard instanceof Zoomba) {
                                Zoomba zoomba = (Zoomba) hazard;
                                recoil(new Vector2((Helpers.absoluteToDirectionalValue(zoomba.getMountKnockback().x, directionX, Enums.Orientation.X)), zoomba.getMountKnockback().y));
                                damage = zoomba.getMountDamage();
                            } else {
                                recoil(new Vector2((Helpers.absoluteToDirectionalValue(hazard.getKnockback().x, directionX, Enums.Orientation.X)), hazard.getKnockback().y));
                            }
                        }
                        health -= damage;
                    }
                }
            }
        }
    }

    /*private void touchPowerups(List<Powerup> powerups) {
        ListIterator<Powerup> iterator = powerups.listIterator();
        while (iterator.hasNext()) {
            Powerup health = iterator.next();
            Rectangle bounds = new Rectangle(health.getLeft(), health.getBottom(), health.getWidth(), health.getHeight());
            if (getBounds().overlaps(bounds)) {
                if (health instanceof AmmoPowerup) {
                    ammo += Constants.POWERUP_AMMO;
                    if (ammo > Constants.MAX_AMMO) {
                        ammo = Constants.MAX_AMMO;
                    }
                } else if (health instanceof HealthPowerup) {
                    health += Constants.POWERUP_HEALTH;
                    if (health > Constants.MAX_HEALTH) {
                        health = Constants.MAX_HEALTH;
                    }
                } else if (health instanceof TurboPowerup) {
                    turbo += Constants.POWERUP_TURBO;
                    if (action == Enums.Action.HOVERING) {
                        hoverStartTime = TimeUtils.nanoTime();
                    }
                    if (action == Enums.Action.DASHING) {
                        dashStartTime = TimeUtils.nanoTime();
                    }
                }
                level.setTotalScore(level.getTotalScore() + Constants.POWERUP_SCORE);
                iterator.remove();
            }
        }
        if (turbo > Constants.MAX_TURBO) {
            turbo = Constants.MAX_TURBO;
        }
    }*/
/*

    private void handleXInputs() {
        boolean left = inputControls.leftButtonPressed;
        boolean right = inputControls.rightButtonPressed;
        boolean directionChanged = false;
        boolean inputtingX = ((left || right) && !(left && right));
        if (inputtingX) {
            if (left && !right) {
                directionChanged = Helpers.changeDirection(this, Enums.Direction.LEFT, Enums.Orientation.X);
            } else if (!left && right) {
                directionChanged = Helpers.changeDirection(this, Enums.Direction.RIGHT, Enums.Orientation.X);
            }
        }
        if (groundState != Enums.GroundState.AIRBORNE && action != Enums.Action.CLIMBING) {
            if (lookStartTime == 0) {
                if (directionChanged) {
                    if (action == Enums.Action.DASHING) {
                        dashStartTime = 0;
                        canDash = false;
                    }
                    strideSpeed = velocity.x;
                    strideStartTime = 0;
                    stand();
                } else if (action != Enums.Action.DASHING) {
                    if (inputtingX) {
                        if (!canStride) {
                            if (strideStartTime == 0) {
                                canStride = true;
                            } else if (Helpers.secondsSince(strideStartTime) > Constants.DOUBLE_TAP_SPEED) {
                                strideStartTime = 0;
                            } else if (!onSinkable){
                                canDash = true;
                            } else {
                                canDash = false;
                            }
                        }
                    } else {
                        pauseTimeSeconds = 0;
                        stand();
                        canStride = false;
                    }
                }
            }
        } else if (groundState == Enums.GroundState.AIRBORNE) {
            if (directionChanged) {
                if (action != Enums.Action.HOVERING) {
                    velocity.x /= 2;
                } else {
                    velocity.x /= 4;
                }
            }
        } else if (action == Enums.Action.CLIMBING) {
            if (canClimb) {
                if (inputtingX) {
                    velocity.y = 0;
                    canHover = false;
                    if (inputControls.jumpButtonPressed) {
                        climb(Enums.Orientation.X);
                    }
                } else {
                    velocity.x = 0; // disable movement when climbing but directional not pressed
                }
            } else {
                velocity.x = 0; // disable movement when climbing but directional not pressed
            }
        }
    }

    private void handleYInputs() {
        boolean up = inputControls.upButtonPressed;
        boolean down = inputControls.downButtonPressed;
        boolean directionChanged = false;
        boolean inputtingY = ((up || down) && !(up && down));
        if (inputtingY) {
            if (down && !up) {
                directionChanged = Helpers.changeDirection(this, Enums.Direction.DOWN, Enums.Orientation.Y);
            } else if (!down && up) {
                directionChanged = Helpers.changeDirection(this, Enums.Direction.UP, Enums.Orientation.Y);
            }
            if (directionY == Enums.Direction.DOWN) {
                if (onSinkable) {
                    velocity.y *= 5;
                }
            }
            if (canLook) {
                canStride = false;
                if (inputControls.jumpButtonJustPressed) {
                    canHover = false;
                    toggleWeapon(directionY);
                }
                look(); // also sets chase cam
            }
        } else if (action == Enums.Action.STANDING || action == Enums.Action.CLIMBING) { // if neither up nor down pressed (and either standing or climbing)
            resetChaseCamPosition();
        } else { // if neither standing nor climbing and not inputting y
            chaseCamPosition.set(position, 0);
            lookStartTime = 0;
        }
        if (canClimb) {
            if (inputtingY) {
                velocity.x = 0;
                canHover = false;
                if (lookStartTime == 0) {
                    if (inputControls.jumpButtonPressed) {
                        // double tap handling while climbing
                        if (climbTimeSeconds == 0) {  // if directional released
                            if (!directionChanged) { // if tapping in same direction
                                // if difference between current time and previous tap start time is less than double tap speed
                                if (((TimeUtils.nanoTime() - climbStartTime) * MathUtils.nanoToSec) < Constants.DOUBLE_TAP_SPEED) {
                                    if (directionY == Enums.Direction.UP) { // enable increased ascension speed
                                        canDash = true; // checks can dash after calling climb() to apply speed boost
                                    } else if (directionY == Enums.Direction.DOWN) { // drop down from climbable (drop handled from climb())
                                        lookStartTime = TimeUtils.nanoTime(); // prevents from reengaging climbable from enableclimb() while falling
                                        onClimbable = false; // meets requirement within climb() to disable climb and enable fall
                                    }
                                }
                                climbStartTime = TimeUtils.nanoTime(); // replace climb start time with that of most recent tap
                            }
                        }
                        climb(Enums.Orientation.Y);
                        if (canDash) { // apply multiplier on top of speed set by climb()
                            velocity.y *= 2; // double speed
                        }
                    } else {
                        velocity.y = 0; // disable movement when climbing but jump button not pressed
                    }
                }
            } else {
                climbTimeSeconds = 0; // indicates release of directional for enabling double tap
                canDash = false; // reset dash when direction released
            }
        }
    }
*/

    private void stand() {
        if (onSinkable) {
            strideStartTime = 0;
            strideTimeSeconds = 0;
            strideAcceleration = 0;
            velocity.x = 0;
            velocity.y = -3;
        } else if (onSkateable) {
            if (Math.abs(velocity.x) > 0.005f) {
                velocity.x /= 1.005;
            } else {
                velocity.x = 0;
            }
        } else if (onRideable) {
            velocity.x = 0;
            velocity.x += Helpers.absoluteToDirectionalValue(Constants.TREADMILL_SPEED, ((Rideable) touchedGround).getDirection(), Enums.Orientation.X);
        } else {
            velocity.x = 0;
        }
        action = Enums.Action.STANDING;
        groundState = Enums.GroundState.PLANTED;
        if (!canClimb) {
            canJump = true;
//            handleYInputs(); // disabled when canclimb to prevent look from overriding climb
        } else {
            canJump = false;
        }
        if (turbo < Constants.MAX_TURBO) {
            turbo += Constants.STAND_TURBO_INCREMENT;
        }
    }

    private void fall() {
//        handleXInputs();
//        handleYInputs();
        action = Enums.Action.FALLING;
        groundState = Enums.GroundState.AIRBORNE;
        canJump = false;
        canDash = false;
        canLook = true;
        if (!onSkateable) {
            canHover = false;
            strideStartTime = 0;
        }
        if (!canRappel) {
            touchedGround = null;
            canHover = true;
        }
        if (onSinkable) {
            canHover = false;
        }
        if (turbo < Constants.MAX_TURBO) {
            turbo += Constants.FALL_TURBO_INCREMENT;
        }
    }

    // disables all else by virtue of neither top level update conditions being satisfied due to state
    private void recoil(Vector2 velocity) {
        action = Enums.Action.RECOILING;
        groundState = Enums.GroundState.AIRBORNE;
        shotIntensity = Enums.ShotIntensity.NORMAL;
        chargeStartTime = 0;
        strideStartTime = 0;
        lookStartTime = 0;
        turbo = 0;
        canStride = false;
        canDash = false;
        canHover = false;
        canLook = false;
        this.velocity.x = velocity.x;
        this.velocity.y = velocity.y;
    }

    private void enableShoot(Enums.Material weapon) {
        if (canShoot) {
//            if (inputControls.shootButtonPressed) {
                if (chargeStartTime == 0) {
                    chargeStartTime = TimeUtils.nanoTime();
                } else if (chargeTimeSeconds > Constants.CHARGE_DURATION) {
                    shotIntensity = Enums.ShotIntensity.BLAST;
                } else if (chargeTimeSeconds > Constants.CHARGE_DURATION / 3) {
                    shotIntensity = Enums.ShotIntensity.CHARGED;
                }
                chargeTimeSeconds = Helpers.secondsSince(chargeStartTime);
          /*  } else */if (chargeStartTime != 0) {
                int ammoUsed;

                if (weapon == Enums.Material.NATIVE
                        || (ammo < Constants.BLAST_AMMO_CONSUMPTION && shotIntensity == Enums.ShotIntensity.BLAST)
                        || ammo < Constants.SHOT_AMMO_CONSUMPTION) {
                    ammoUsed = 0;
                    weapon = Enums.Material.NATIVE;
                } else {
                    ammoUsed = Helpers.useAmmo(shotIntensity);
                }

                shoot(shotIntensity, weapon, ammoUsed);
                chargeStartTime = 0;
                chargeTimeSeconds = 0;
                this.shotIntensity = Enums.ShotIntensity.NORMAL;
            }
        }
    }

    public void shoot(Enums.ShotIntensity shotIntensity, Enums.Material weapon, int ammoUsed) {
        ammo -= ammoUsed;
        Vector2 ammoPosition = new Vector2(
                position.x + Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_X_CANNON_OFFSET.x, directionX, Enums.Orientation.X),
                position.y + Constants.GIGAGAL_X_CANNON_OFFSET.y
        );
        if (lookStartTime != 0) {
            ammoPosition.add(Helpers.absoluteToDirectionalValue(0, directionX, Enums.Orientation.X), Helpers.absoluteToDirectionalValue(6, directionY, Enums.Orientation.Y));
            level.spawnAmmo(ammoPosition, directionY, Enums.Orientation.Y, shotIntensity, weapon, true);
        } else {
            level.spawnAmmo(ammoPosition, directionX, Enums.Orientation.X, shotIntensity, weapon, true);
        }
    }

    private void look() {
        float offset = 0;
        if (lookStartTime == 0) {
            lookStartTime = TimeUtils.nanoTime();
//            chaseCamPosition.set(position, 0);
        } else if (action == Enums.Action.STANDING || action == Enums.Action.CLIMBING) {
//            setChaseCamPosition(offset);
        }
    }

    private void enableStride() {
//        handleXInputs();
        if (canStride) {
            stride();
        }
    }

    private void stride() {
        if (turbo < Constants.MAX_TURBO) {
            turbo += Constants.STRIDE_TURBO_INCREMENT;
        }
        canLook = false;
        if (strideStartTime == 0) {
            strideSpeed = velocity.x;
            action = Enums.Action.STRIDING;
            strideStartTime = TimeUtils.nanoTime();
        }
        strideTimeSeconds = Helpers.secondsSince(strideStartTime) - pauseTimeSeconds;
        strideAcceleration = strideTimeSeconds + Constants.GIGAGAL_STARTING_SPEED;
        velocity.x = Helpers.absoluteToDirectionalValue(Math.min(Constants.GIGAGAL_MAX_SPEED * strideAcceleration + Constants.GIGAGAL_STARTING_SPEED, Constants.GIGAGAL_MAX_SPEED), directionX, Enums.Orientation.X);
        if (onRideable) {
            velocity.x += Helpers.absoluteToDirectionalValue(Constants.TREADMILL_SPEED, ((Rideable) touchedGround).getDirection(), Enums.Orientation.X);
        } else if (onSkateable) {
            velocity.x = strideSpeed + Helpers.absoluteToDirectionalValue(Math.min(Constants.GIGAGAL_MAX_SPEED * strideAcceleration / 2 + Constants.GIGAGAL_STARTING_SPEED, Constants.GIGAGAL_MAX_SPEED * 2), directionX, Enums.Orientation.X);
        } else if (onSinkable) {
            velocity.x = Helpers.absoluteToDirectionalValue(10, directionX, Enums.Orientation.X);
            velocity.y = -3;
        }
    }

    private void enableDash() {
//        handleXInputs();
        if (canDash) {
            dash();
        }
    }

    private void dash() {
        if (action != Enums.Action.DASHING) {
            startTurbo = turbo;
            turboDuration = Constants.MAX_DASH_DURATION * (startTurbo / Constants.MAX_TURBO);
            action = Enums.Action.DASHING;
            dashStartTime = TimeUtils.nanoTime();
            strideStartTime = 0;
            canStride = false;
        }
        float dashSpeed = Constants.GIGAGAL_MAX_SPEED;
        if (onSkateable) {
            dashSpeed *= 1.75f;
        }
        if (turbo >= 1) {
            turbo -= Constants.FALL_TURBO_INCREMENT * 3;
            velocity.x = Helpers.absoluteToDirectionalValue(dashSpeed, directionX, Enums.Orientation.X);
        } else {
            canDash = false;
            dashStartTime = 0;
            pauseTimeSeconds = 0;
            stand();
        }
    }

    private void enableJump() {
        if (canJump) {
            if ((/*inputControls.jumpButtonJustPressed || */action == Enums.Action.JUMPING)
                    && lookStartTime == 0) {
                jump();
            }
        }
    }

    private void jump() {
        if (canJump) {
            action = Enums.Action.JUMPING;
            groundState = Enums.GroundState.AIRBORNE;
            jumpStartTime = TimeUtils.nanoTime();
            canJump = false;
        }
        velocity.x += Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_STARTING_SPEED * Constants.STRIDING_JUMP_MULTIPLIER, directionX, Enums.Orientation.X);
        float jumpTimeSeconds = Helpers.secondsSince(jumpStartTime) - pauseTimeSeconds;
        if (jumpTimeSeconds < Constants.MAX_JUMP_DURATION) {
            velocity.y = Constants.JUMP_SPEED;
            velocity.y *= Constants.STRIDING_JUMP_MULTIPLIER;
            if (onBounceable) {
                velocity.y *= 2;
            } else if (onSinkable) {
                fall(); // causes fall texture to render for one frame
            }
        } else {
            pauseTimeSeconds = 0;
            fall();
        }
    }

    private void enableHover() {
        if (canHover) {
          /*  if (inputControls.jumpButtonJustPressed) {*/
                if (action == Enums.Action.HOVERING) {
                    //   canHover = false;
                    hoverStartTime = 0;
                    velocity.x -= velocity.x / 2;
                    fall();
                } else {
                    hover(); // else hover if canHover is true (set to false after beginning hover)
                }
                // if jump key not pressed, but already hovering, continue to hover
         /*   } else*/ if (action == Enums.Action.HOVERING) {
//                handleYInputs();
                hover();
            }
        }
    }

    private void hover() {
        // canHover can only be true just before beginning to hover
        if (action != Enums.Action.HOVERING) {
            startTurbo = turbo;
            turboDuration = Constants.MAX_HOVER_DURATION * (startTurbo / Constants.MAX_TURBO);
            action = Enums.Action.HOVERING; // indicates currently hovering
            hoverStartTime = TimeUtils.nanoTime(); // begins timing hover duration
        }
        hoverTimeSeconds = (Helpers.secondsSince(hoverStartTime) - pauseTimeSeconds); // for comparing with max hover time
        if (turbo >= 1) {
            velocity.y = 0; // disables impact of gravity
            turbo -= Constants.FALL_TURBO_INCREMENT;
        } else {
            canHover = false;
            fall(); // when max hover time is exceeded
            pauseTimeSeconds = 0;
        }
//        handleXInputs();
    }

    private void enableRappel() {
        if (action == Enums.Action.RAPPELLING) {
            rappel();
        } else if (canRappel){
            if (!canHover || action == Enums.Action.HOVERING) {
                fall(); // begin descent from ground side sans access to hover
                canHover = false; // disable hover if not already
            }
//            if (inputControls.jumpButtonJustPressed) {
                rappel();
//            }
        }
    }

    private void rappel() {
        if (canRappel) {
            action = Enums.Action.RAPPELLING;
            groundState = Enums.GroundState.AIRBORNE;
            startTurbo = turbo;
            rappelStartTime = TimeUtils.nanoTime();
            turboDuration = Constants.MAX_RAPPEL_DURATION * (startTurbo / Constants.MAX_TURBO);
            if (!Helpers.movingOppositeDirection(velocity.x, directionX, Enums.Orientation.X)) {
                directionX = Helpers.getOppositeDirection(directionX);
            }
            hoverStartTime = 0;
            canJump = true;
            canRappel = false;
        }
        float rappelTimeSeconds = (Helpers.secondsSince(rappelStartTime) - pauseTimeSeconds);
//        if (!inputControls.jumpButtonPressed) {
            if (rappelTimeSeconds >= Constants.RAPPEL_FRAME_DURATION) {
                velocity.x = Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_MAX_SPEED, directionX, Enums.Orientation.X);
                jump();
            } else {
                pauseTimeSeconds = 0;
                canHover = true;
            }
//        } else {
            lookStartTime = 0;
//            if (inputControls.downButtonPressed) {
                velocity.y += Constants.RAPPEL_GRAVITY_OFFSET;
        /*    } else*/ if (turbo < 1) {
                turbo = 0;
                velocity.y += Constants.RAPPEL_GRAVITY_OFFSET;
//            } else {
                turbo -= Constants.FALL_TURBO_INCREMENT;
                velocity.y = 0;
            }
    }

    private void enableClimb() {
        if (onClimbable) {
//            if (inputControls.jumpButtonPressed) {
                if (lookStartTime == 0) { // cannot initiate climb if already looking; must first neurtralize
                    canLook = false; // prevents look from overriding climb
                    canClimb = true; // enables climb handling from handleY()
                }
//            } else {
                canLook = true; // enables look when engaging climbable but not actively climbing
                canClimb  = false; // prevents climb initiation when jumpbutton released
//            }
//            handleXInputs(); // enables change of x direction for shooting left or right
//            handleYInputs(); // enables change of y direction for looking and climbing up or down
        } else {
            if (action == Enums.Action.CLIMBING) {
                fall();
                if (!(touchedGround instanceof Climbable && Helpers.overlapsBetweenTwoSides(position.x, getHalfWidth(), touchedGround.getLeft(), touchedGround.getRight())))  {
                    velocity.x = Helpers.absoluteToDirectionalValue(Constants.CLIMB_SPEED, directionX, Enums.Orientation.X);
                }
            }
            canClimb = false;
        }
    }

    private void climb(Enums.Orientation orientation) {
        if (onClimbable) { // onclimbable set to false from handleYinputs() if double tapping down
            if (action != Enums.Action.CLIMBING) { // at the time of climb initiation
                climbStartTime = 0; // overrides assignment of current time preventing nanotime - climbstarttime < doubletapspeed on next handleY() call
                groundState = Enums.GroundState.PLANTED;
                action = Enums.Action.CLIMBING;
            }
            canHover = false;
            climbTimeSeconds = Helpers.secondsSince(climbStartTime);
            if (orientation == Enums.Orientation.X) {
                velocity.x = Helpers.absoluteToDirectionalValue(Constants.CLIMB_SPEED, directionX, Enums.Orientation.X);
            } else if (orientation == Enums.Orientation.Y) {
                velocity.y = Helpers.absoluteToDirectionalValue(Constants.CLIMB_SPEED, directionY, Enums.Orientation.Y);
            }
            int climbAnimationPercent = (int) (climbTimeSeconds * 100);
            if ((climbAnimationPercent) % 25 >= 0 && (climbAnimationPercent) % 25 <= 13) {
                directionX = Enums.Direction.RIGHT;
            } else {
                directionX = Enums.Direction.LEFT;
            }
        } else { // if double tapping down, fall from climbable
            climbStartTime = 0;
            climbTimeSeconds = 0;
            canClimb = false;
            fall();
        }
    }

    public void render(SpriteBatch batch, Viewport viewport) {
        if (directionX == Enums.Direction.RIGHT) {
            if (lookStartTime != 0) {
                if (directionY == Enums.Direction.UP) {
                    region = Assets.getInstance().getGigaGalAssets().lookupStandRight;
                    if (action == Enums.Action.FALLING || action == Enums.Action.CLIMBING) {
                        region = Assets.getInstance().getGigaGalAssets().lookupFallRight;
                    } else if (action == Enums.Action.HOVERING) {
                        region = Assets.getInstance().getGigaGalAssets().lookupHoverRight.getKeyFrame(hoverTimeSeconds);
                    }
                } else if (directionY == Enums.Direction.DOWN) {
                    region = Assets.getInstance().getGigaGalAssets().lookdownStandRight;
                    if (action == Enums.Action.FALLING || action == Enums.Action.CLIMBING) {
                        region = Assets.getInstance().getGigaGalAssets().lookdownFallRight;
                    } else if (action == Enums.Action.HOVERING) {
                        region = Assets.getInstance().getGigaGalAssets().lookdownHoverRight.getKeyFrame(hoverTimeSeconds);
                    }
                }
            } else if (action == Enums.Action.CLIMBING) {
                region = Assets.getInstance().getGigaGalAssets().climb.getKeyFrame(0.25f);
            } else if (action == Enums.Action.STANDING) {
                region = Assets.getInstance().getGigaGalAssets().standRight;
            } else if (action == Enums.Action.STRIDING) {
                region = Assets.getInstance().getGigaGalAssets().strideRight.getKeyFrame(Math.min(strideAcceleration * strideAcceleration, strideAcceleration));
            } else if (action == Enums.Action.DASHING) {
                region = Assets.getInstance().getGigaGalAssets().dashRight;
            } else if (action == Enums.Action.HOVERING) {
                region = Assets.getInstance().getGigaGalAssets().hoverRight.getKeyFrame(hoverTimeSeconds);
            } else if (action == Enums.Action.RAPPELLING) {
                region = Assets.getInstance().getGigaGalAssets().rappelRight;
            } else if (action == Enums.Action.RECOILING){
                region = Assets.getInstance().getGigaGalAssets().recoilRight;
            } else if (action == Enums.Action.FALLING) {
                region = Assets.getInstance().getGigaGalAssets().fallRight;
            }
        } else if (directionX == Enums.Direction.LEFT) {
            if (lookStartTime != 0) {
                if (directionY == Enums.Direction.UP) {
                    region = Assets.getInstance().getGigaGalAssets().lookupStandLeft;
                    if (action == Enums.Action.FALLING || action == Enums.Action.CLIMBING) {
                        region = Assets.getInstance().getGigaGalAssets().lookupFallLeft;
                    } else if (action == Enums.Action.HOVERING) {
                        region = Assets.getInstance().getGigaGalAssets().lookupHoverLeft.getKeyFrame(hoverTimeSeconds);
                    }
                } else if (directionY == Enums.Direction.DOWN) {
                    region = Assets.getInstance().getGigaGalAssets().lookdownStandLeft;
                    if (action == Enums.Action.FALLING || action == Enums.Action.CLIMBING) {
                        region = Assets.getInstance().getGigaGalAssets().lookdownFallLeft;
                    } else if (action == Enums.Action.HOVERING) {
                        region = Assets.getInstance().getGigaGalAssets().lookdownHoverLeft.getKeyFrame(hoverTimeSeconds);
                    }
                }
            } else if (action == Enums.Action.CLIMBING) {
                region = Assets.getInstance().getGigaGalAssets().climb.getKeyFrame(0.12f);
            } else if (action == Enums.Action.STANDING) {
                region = Assets.getInstance().getGigaGalAssets().standLeft;
            } else if (action == Enums.Action.STRIDING) {
                region = Assets.getInstance().getGigaGalAssets().strideLeft.getKeyFrame(Math.min(strideAcceleration * strideAcceleration, strideAcceleration));
            } else if (action == Enums.Action.DASHING) {
                region = Assets.getInstance().getGigaGalAssets().dashLeft;
            } else if (action == Enums.Action.HOVERING) {
                region = Assets.getInstance().getGigaGalAssets().hoverLeft.getKeyFrame(hoverTimeSeconds);
            } else if (action == Enums.Action.RAPPELLING) {
                region = Assets.getInstance().getGigaGalAssets().rappelLeft;
            } else if (action == Enums.Action.RECOILING) {
                region = Assets.getInstance().getGigaGalAssets().recoilLeft;
            } else if (action == Enums.Action.FALLING) {
                region = Assets.getInstance().getGigaGalAssets().fallLeft;
            }
        }
        Helpers.drawTextureRegion(batch, viewport, region, position, Constants.GIGAGAL_EYE_POSITION);
    }

    // Getters
    @Override public final Vector2 getPosition() { return position; }
    @Override public final Vector2 getVelocity() { return velocity; }
    @Override public final Enums.Direction getDirectionX() { return directionX; }
    @Override public final Enums.Direction getDirectionY() { return directionY; }
    @Override public final Rectangle getBounds() { return bounds; }
    @Override public final float getLeft() { return left; }
    @Override public final float getRight() { return right; }
    @Override public final float getTop() { return top; }
    @Override public final float getBottom() { return bottom; }
    @Override public final float getWidth() { return width; }
    @Override public final float getHeight() { return height; }
    @Override public final float getTurbo() { return turbo; }
    @Override public final float getHealth() { return health; }
    @Override public final boolean getJumpStatus() { return canJump; }
    @Override public final boolean getHoverStatus() { return canHover; }
    @Override public final boolean getRappelStatus() { return canRappel; }
    @Override public final boolean getDashStatus() { return canDash; }
    @Override public final boolean getClimbStatus() { return canClimb; }
    @Override public final Enums.GroundState getGroundState() { return groundState; }
    @Override public final Enums.Action getAction() { return action; }
    public final Enums.ShotIntensity getShotIntensity() { return shotIntensity; }
    @Override public final Enums.Material getWeapon() { return weapon; }
    @Override public final int getDamage() { return Constants.AMMO_STANDARD_DAMAGE; }
    @Override public final Vector2 getKnockback() { return Constants.ZOOMBA_KNOCKBACK; }
    @Override public final Enums.Material getType() { return weapon; }
    @Override public final float getShotRadius() { return Constants.ZOOMBA_SHOT_RADIUS; }
    @Override public final int getHitScore() { return Constants.ZOOMBA_HIT_SCORE; }
    @Override public final int getKillScore() { return Constants.ZOOMBA_KILL_SCORE; }
    @Override public final void setHealth( float health ) { this.health = health; }
    private final float getHalfWidth() { return halfWidth; }
    public final Rectangle getRoomBounds() { return roomBounds; }

    // Setters
    public void setDirectionX(Enums.Direction direction) { this.directionX = direction; }
    public void setDirectionY(Enums.Direction direction) { this.directionY = direction; }

    public void dispose() {
        weaponList.clear();
    }
}

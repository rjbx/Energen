package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.app.Level;
import com.udacity.gamedev.gigagal.app.InputControls;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums.*;
import com.udacity.gamedev.gigagal.util.Utils;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

// mutable
public class GigaGal implements Humanoid {

    // fields
    public final static String TAG = GigaGal.class.getName();
    private Level level;
    private InputControls inputControls;
    private Vector2 position;
    private Vector2 previousFramePosition;
    private Vector2 spawnPosition;
    private Vector3 chaseCamPosition;
    private Vector2 velocity;
    private Direction directionX;
    private Direction directionY;
    private AerialState aerialState;
    private GroundState groundState;
    private Ground touchedGround;
    private AmmoIntensity ammoIntensity;
    private WeaponType weapon;
    private List<WeaponType> weaponList;
    private ListIterator<WeaponType> weaponToggler;
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
    private boolean canCling;
    private boolean canClimb;
    private boolean canStride;
    private long chargeStartTime;
    private long lookStartTime;
    private long jumpStartTime;
    private long dashStartTime;
    private long hoverStartTime;
    private long clingStartTime;
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
    private int lives;
    private int ammo;
    private int health;

    private boolean paused;
    private float pauseTimeSeconds;

    // ctor
    public GigaGal(Level level, Vector2 spawnPosition) {
        this.level = level;
        this.spawnPosition = spawnPosition;
        position = new Vector2(spawnPosition);
        inputControls = InputControls.getInstance();
        velocity = new Vector2();
        previousFramePosition = new Vector2();
        chaseCamPosition = new Vector3();
        weaponList = new ArrayList<WeaponType>();
        init();
    }

    public void init() {
        ammo = Constants.INITIAL_AMMO;
        health = Constants.INITIAL_HEALTH;
        lives = Constants.INITIAL_LIVES;
        weaponList.add(WeaponType.NATIVE);
        weaponToggler = weaponList.listIterator();
        weapon = weaponToggler.next();
        respawn();
    }

    public void respawn() {
        position.set(spawnPosition);
        chaseCamPosition.set(position, 0);
        velocity.setZero();
        directionX = Direction.RIGHT;
        groundState = GroundState.AIRBORNE;
        aerialState = AerialState.FALLING;
        touchedGround = null;
        paused = false;
        canClimb = false;
        canLook = false;
        canStride = false;
        canJump = false;
        canDash = false;
        canHover = false;
        canCling = false;
        canShoot = true;
        turboDuration = 0;
        ammoIntensity = AmmoIntensity.SHOT;
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
        health = Constants.MAX_HEALTH;
        turbo = Constants.MAX_TURBO;
        startTurbo = turbo;
    }

    public void update(float delta) {
        // positioning
        previousFramePosition.set(position);
        position.mulAdd(velocity, delta);

        // collision detection
        touchGround(level.getGrounds());
        touchHazards(level.getHazards());
        touchPowerups(level.getPowerups());

        // abilities
        if (aerialState == AerialState.GROUNDED) {
            velocity.y = 0;
            if (groundState == GroundState.STANDING) {
                stand(); // default ground state
                enableClimb(); // must call before look
                enableLook();
                enableStride();
                enableDash();
                enableJump();
                enableShoot(weapon);
            } else if (groundState == GroundState.STRIDING) {
                enableStride();
                enableDash();
                enableJump();
                enableShoot(weapon);
            } else if (groundState == GroundState.CLIMBING) {
                enableClimb();
                enableShoot(weapon);
            } else if (groundState == GroundState.DASHING) {
                enableDash();
                enableJump();
                enableShoot(weapon);
            }
        } else if (groundState == GroundState.AIRBORNE) {
            velocity.y -= Constants.GRAVITY;
            if (aerialState == AerialState.FALLING) {
                fall(); // default aerial state
                enableClimb(); // must call before look
                enableLook();
                enableHover();
                enableCling();
                enableShoot(weapon);
            } else if (aerialState == AerialState.JUMPING) {
                enableLook();
                enableJump();
                enableCling();
                enableShoot(weapon);
            } else if (aerialState == AerialState.HOVERING) {
                enableLook();
                enableHover();
                enableCling();
                enableClimb();
                enableShoot(weapon);
            } else if (aerialState == AerialState.CLINGING) {
                enableJump();
                enableCling();
                enableShoot(weapon);
            } else if (aerialState == AerialState.RECOILING) {
                enableShoot(weapon);
            }
        }
    }

    private void touchGround(Array<Ground> grounds) {
        onUnbearable = false;
        onRideable = false;
        onSkateable = false;
        onClimbable = false;
        onSinkable = false;
        for (Ground ground : grounds) {
            // if currently within ground left and right sides
            if (Utils.overlapsBetweenTwoSides(position.x, getHalfWidth(), ground.getLeft(), ground.getRight())) {
                // apply following rules (bump side and bottom) only if ground height > ledge height
                // ledges only apply collision detection on top, and not on sides and bottom as do grounds
                if (getBottom() <= ground.getTop() && getTop() >= ground.getBottom()) {
                    // alternate collision handling to allow passing through top of descendables and prevent setting atop as with other grounds
                    if (!(ground instanceof DescendableGround)
                    && (climbTimeSeconds == 0 || (touchedGround != null && touchedGround instanceof DescendableGround && touchedGround.getBottom() > ground.getTop()))) {
                        if (ground.getHeight() > Constants.MAX_LEDGE_HEIGHT) {
                            // if during previous frame was not, while currently is, between ground left and right sides
                            if (!Utils.overlapsBetweenTwoSides(previousFramePosition.x, getHalfWidth(), ground.getLeft(), ground.getRight())) {
                                // only when not grounded and not recoiling
                                if (aerialState != AerialState.GROUNDED && aerialState != AerialState.RECOILING) {
                                    // if x velocity (magnitude, without concern for direction) greater than one third max speed,
                                    // boost x velocity by starting speed, enable cling, verify rappelling ground and capture rappelling ground boundaries
                                    if (Math.abs(velocity.x) > Constants.GIGAGAL_MAX_SPEED / 4) {
                                        // if already clinging, halt x progression
                                        if (aerialState != AerialState.CLINGING) {
                                            canCling = true; // enable cling
                                            touchedGround = ground;
                                        }
                                    // if absval x velocity  not greater than one third max speed but aerial and bumping ground side, fall
                                    } else {
                                        // if not already hovering and descending, also disable hover
                                        if (aerialState != AerialState.HOVERING && velocity.y < 0) {
                                            canHover = false; // disable hover
                                        }
                                        canCling = false;
                                        fall(); // fall regardless of whether or not inner condition met
                                    }
                                    // only when grounded
                                } else if (aerialState == AerialState.GROUNDED) {
                                    if (Math.abs(getBottom() - ground.getTop()) > 1) {
                                        strideSpeed = 0;
                                        velocity.x = 0;
                                    }
                                    if (groundState == GroundState.DASHING) {
                                        stand(); // deactivates dash when bumping ground side
                                    }
                                }
                                if ((!(ground instanceof RideableGround && (Math.abs(getBottom() - ground.getTop()) <= 1)))
                                        && !(ground instanceof SkateableGround && (Math.abs(getBottom() - ground.getTop()) <= 1))
                                        && !(ground instanceof UnbearableGround && (Math.abs(getBottom() - ground.getTop()) <= 1))) {
                                    // if contact with ground sides detected without concern for ground state (either grounded or airborne),
                                    // reset stride acceleration, disable stride and dash, and set gigagal at ground side
                                    if (groundState != GroundState.STRIDING || groundState != GroundState.DASHING) {
                                        strideStartTime = 0; // reset stride acceleration
                                    }
                                    canStride = false; // disable stride
                                    canDash = false; // disable dash
                                    position.x = previousFramePosition.x; // halt x progression
                                }
                                // else if no detection with ground sides, disable cling
                            } else {
                                canCling = false; // disable cling
                            }
                            // if contact with ground bottom detected, halts upward progression and set gigagal at ground bottom
                            if ((previousFramePosition.y + Constants.GIGAGAL_HEAD_RADIUS) <= ground.getBottom()) {
                                velocity.y = 0; // prevents from ascending above ground bottom
                                position.y = previousFramePosition.y;  // sets gigagal at ground bottom
                                fall(); // descend from point of contact with ground bottom
                            }
                        } else {
                            canCling = false;
                        }
                        // if contact with ground top detected, halt downward progression and set gigagal atop ground
                        if ((getBottom() <= ground.getTop() && (!canCling || (touchedGround != null && ground.getTop() != touchedGround.getTop())))
                                && (previousFramePosition.y - Constants.GIGAGAL_EYE_HEIGHT >= ground.getTop() - 1)) {
                            setAtop(ground);
                            velocity.y = 0; // prevents from descending beneath ground top
                            position.y = ground.getTop() + Constants.GIGAGAL_EYE_HEIGHT; // sets Gigagal atop ground
                            if (groundState == GroundState.AIRBORNE && !(ground instanceof SkateableGround)) {
                                stand(); // set groundstate to standing
                                lookStartTime = 0;
                            }
                            if (groundState != GroundState.DASHING) {
                                pauseTimeSeconds = 0;
                            }

                            if (ground instanceof SkateableGround) {
                                onSkateable = true;
                                if (groundState == GroundState.AIRBORNE) {
                                    stand(); // set groundstate to standing
                                    lookStartTime = 0;
                                }
                            } else if (ground instanceof HoverableGround) {
                                lookStartTime = 0;
                                HoverableGround hoverable = (HoverableGround) ground;
                                Orientation orientation = hoverable.getOrientation();
                                Direction direction = hoverable.getDirection();
                                if (orientation == Orientation.X) {
                                    velocity.x = hoverable.getVelocity().x;
                                    position.x += velocity.x;
                                }
                                if (direction == Direction.DOWN) {
                                    position.y -= 1;
                                }
                            } else if (ground instanceof BounceableGround) {
                                onBounceable = true;
                                BounceableGround bounceable = (BounceableGround) ground;
                                bounceable.setLoaded(true);
                            } else if (ground instanceof RideableGround) {
                                onRideable = true;
                            } else if (ground instanceof UnbearableGround) {
                                onUnbearable = true;
                                canHover = false;
                                Random xKnockback = new Random();
                                velocity.set(Utils.absoluteToDirectionalValue(xKnockback.nextFloat() * 200, directionX, Orientation.X), Constants.FLAME_KNOCKBACK.y);
                                recoil(velocity);
                            }
                        }
                    // alt ground handling
                    } else if (ground instanceof DescendableGround) {
                        // enable default set atop if passing through bottom of descendable and contacting other ground top
                        if (touchedGround != null && !(touchedGround instanceof DescendableGround) && (touchedGround.getTop() < ground.getBottom())) {
                            onClimbable = false;
                            climbStartTime = 0;
                            climbTimeSeconds = 0;
                        } else if (ground instanceof SinkableGround) {
                            setAtop(ground);
                            onSinkable = true;
                            canDash = false;
                            canHover = false;
                            canHover = false;
                            canClimb = false;
                            velocity.y = -3;
                            lookStartTime = 0;
                            lookTimeSeconds = 0;
                            if (groundState == GroundState.AIRBORNE) {
                                stand();
                            }
                        } else if (ground instanceof ClimbableGround) {
                            if (Utils.betweenTwoValues(position.x, ground.getLeft(), ground.getRight())) {
                                if (getTop() > ground.getBottom()) {
                                    onClimbable = true;
                                }
                            }
                            if (climbTimeSeconds == 0) {
                                if ((getBottom() <= ground.getTop() && (!canCling || (touchedGround != null && ground.getTop() != touchedGround.getTop()))
                                        && previousFramePosition.y - Constants.GIGAGAL_EYE_HEIGHT >= ground.getTop())
                                        || canClimb && climbStartTime != 0) {
                                    setAtop(ground);
                                    if (lookStartTime != 0 && aerialState != AerialState.GROUNDED) {
                                        lookStartTime = 0;
                                    }
                                    if (groundState == GroundState.AIRBORNE) {
                                        stand(); // set groundstate to standing
                                    }
                                    if (climbStartTime == 0) {
                                        velocity.y = 0; // prevents from descending beneath ground top
                                        position.y = ground.getTop() + Constants.GIGAGAL_EYE_HEIGHT; // sets Gigagal atop ground
                                    }
                                }
                                if (climbStartTime == 0) {
                                    if (canClimb && !inputControls.jumpButtonPressed && groundState == GroundState.STANDING) {
                                        if (!(ground instanceof Pole)) {
                                            canJump = true;
                                        }
                                        jump();
                                    }
                                }
                            }
                        }
                    }
                    // if below minimum ground distance while descending excluding post-cling, disable cling and hover
                    // caution when crossing plane between ground top and minimum hover height / ground distance
                    // cannons, which inherit ground, can be mounted along sides of grounds causing accidental plane breakage
                    if (getBottom() < (ground.getTop() + Constants.MIN_GROUND_DISTANCE)
                            && getBottom() > ground.getTop() // GG's bottom is greater than ground top but less than boundary
                            && velocity.y < 0 // prevents disabling features when crossing boundary while ascending on jump
                            && clingStartTime == 0 // only if have not clinged since last grounded
                            && !(ground instanceof Cannon) // only if ground is not instance of cannon
                            ) {
                        canCling = false; // disables cling
                        canHover = false; // disables hover
                    }
                }
            }
        }
        if (!onClimbable) {
            climbStartTime = 0;
            climbTimeSeconds = 0;
        }
        if (touchedGround != null && aerialState != AerialState.HOVERING) {
            if (getBottom() > touchedGround.getTop() || getTop() < touchedGround.getBottom())
                /*(!Utils.overlapsBetweenTwoSides(position.y, (getTop() - getBottom()) / 2, touchedGround.getBottom(), touchedGround.getTop()) */{
                if (onBounceable) {
                    BounceableGround bounceable = (BounceableGround) touchedGround;
                    bounceable.resetStartTime();
                    bounceable.setLoaded(false);
                    onBounceable = false;
                }
                canCling = false;
                fall();
            } else if (!Utils.overlapsBetweenTwoSides(position.x, getHalfWidth(), touchedGround.getLeft(), touchedGround.getRight())) {
                if (onBounceable) {
                    BounceableGround bounceable = (BounceableGround) touchedGround;
                    bounceable.resetStartTime();
                    bounceable.setLoaded(false);
                    onBounceable = false;
                }
                onSinkable = false;
                lookTimeSeconds = 0;
                lookStartTime = TimeUtils.nanoTime();
                if (aerialState != AerialState.CLINGING) {
                    fall();
                }
            }
        }
    }

    // detects contact with enemy (change aerial & ground state to recoil until grounded)
    private void touchHazards(Array<Hazard> hazards) {
        for (Hazard hazard : hazards) {
            float recoveryTimeSeconds = Utils.secondsSince(recoveryStartTime) - pauseTimeSeconds;
            if (aerialState != AerialState.RECOILING && recoveryTimeSeconds > Constants.RECOVERY_TIME) {
                Rectangle bounds = new Rectangle(hazard.getLeft(), hazard.getBottom(), hazard.getWidth(), hazard.getHeight());
                if (getBounds().overlaps(bounds)) {
                    ammoIntensity = AmmoIntensity.SHOT;
                    recoveryStartTime = TimeUtils.nanoTime();
                    chargeStartTime = 0;
                    int damage = hazard.getDamage();
                    float margin = 0;
                    if (hazard instanceof DestructibleHazard) {
                        margin = hazard.getWidth() / 6;
                    }
                    if (!(hazard instanceof Ammo && ((Ammo) hazard).isFromGigagal())) {
                        chaseCamPosition.set(position, 0);
                        Vector2 intersection = new Vector2();
                        intersection.x = Math.max(getBounds().x, bounds.x);
                        intersection.y = Math.max(getBounds().y, bounds.y);
                        level.spawnExplosion(intersection);
                        turbo = 0;
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
                                recoil(new Vector2((Utils.absoluteToDirectionalValue(zoomba.getMountKnockback().x, directionX, Orientation.X)), zoomba.getMountKnockback().y));
                                damage = zoomba.getMountDamage();
                            } else {
                                recoil(new Vector2((Utils.absoluteToDirectionalValue(hazard.getKnockback().x, directionX, Orientation.X)), hazard.getKnockback().y));
                            }
                        }
                        health -= damage;
                    }
                }
            }
        }
    }

    private void touchPowerups(DelayedRemovalArray<Powerup> powerups) {
        for (Powerup powerup : powerups) {
            Rectangle bounds = new Rectangle(powerup.getLeft(), powerup.getBottom(), powerup.getWidth(), powerup.getHeight());
            if (getBounds().overlaps(bounds)) {
                if (powerup instanceof AmmoPowerup) {
                    ammo += Constants.POWERUP_AMMO;
                    if (ammo > Constants.MAX_AMMO) {
                        ammo = Constants.MAX_AMMO;
                    }
                } else if (powerup instanceof HealthPowerup) {
                    health += Constants.POWERUP_HEALTH;
                    if (health > Constants.MAX_HEALTH) {
                        health = Constants.MAX_HEALTH;
                    }
                } else if (powerup instanceof TurboPowerup) {
                    turbo += Constants.POWERUP_TURBO;
                    if (aerialState == AerialState.HOVERING) {
                        hoverStartTime = TimeUtils.nanoTime();
                    }
                    if (groundState == GroundState.DASHING) {
                        dashStartTime = TimeUtils.nanoTime();
                    }
                }
                level.setLevelScore(level.getLevelScore() + Constants.POWERUP_SCORE);
                powerups.removeValue(powerup, true);
            }
        }
        if (turbo > Constants.MAX_TURBO) {
            turbo = Constants.MAX_TURBO;
        }
    }

    private void handleXInputs() {
        boolean left = inputControls.leftButtonPressed;
        boolean right = inputControls.rightButtonPressed;
        boolean directionChanged = false;
        boolean striding = true;
        if (left && !right) {
            directionChanged = Utils.changeDirection(this, Direction.LEFT, Orientation.X);
        } else if (!left && right) {
            directionChanged = Utils.changeDirection(this, Direction.RIGHT, Orientation.X);
        } else {
            striding = false;
        }
        if (groundState != GroundState.AIRBORNE && climbStartTime == 0) {
            if (lookStartTime == 0) {
                if (directionChanged) {
                    if (groundState == GroundState.DASHING) {
                        dashStartTime = 0;
                        canDash = false;
                    }
                    strideSpeed = velocity.x;
                    strideStartTime = 0;
                    stand();
                } else if (groundState != GroundState.DASHING) {
                    if (striding) {
                        if (!canStride) {
                            if (strideStartTime == 0) {
                                canStride = true;
                            } else if (Utils.secondsSince(strideStartTime) > Constants.DOUBLE_TAP_SPEED) {
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
        } else if (directionChanged) {
            if (aerialState != AerialState.HOVERING) {
                velocity.x /= 2;
            } else {
                velocity.x /= 4;
            }
        }
    }

    private void handleYInputs() {
        boolean up = inputControls.upButtonPressed;
        boolean down = inputControls.downButtonPressed;
        boolean looking = up || down;
        boolean lookInitiated = false;
        if (canLook) {
            if (looking && climbTimeSeconds == 0) {
                canStride = false;
                if (up) {
                    if (chaseCamPosition.y == position.y) {  // prevents change ydirection opposite chasecam offset while resetting
                        directionY = Direction.UP; // cannot change direction to up while offset is below, causing reset to shift cam in direction of offset (below)
                    } else if (chaseCamPosition.y < position.y) {
                        lookInitiated = true;
                    }
                } else if (down) {
                    if (chaseCamPosition.y == position.y) {  // prevents change ydirection opposite chasecam offset while resetting
                        directionY = Direction.DOWN; // cannot change direction to up while offset is below, causing reset to shift cam in direction of offset (below)
                    } else if (chaseCamPosition.y > position.y) {
                        lookInitiated = true;
                    }
                    if (onSinkable) {
                        velocity.y *= 5;
                    }
                }
                // offset chasecam
                if (groundState == GroundState.STANDING) { // if up or down pressed while standing and not actively climbing
                    if (lookInitiated ) {
                        chaseCamPosition.y += Utils.absoluteToDirectionalValue(.75f, directionY, Orientation.Y);
                    }
                    if (inputControls.jumpButtonJustPressed) {
                        canHover = false;
                        toggleWeapon(directionY);
                    }
                }
                look();
            // reset chasecam
            } else if (groundState == GroundState.STANDING) { // if can look but up or down not pressed (and since standing, not in the act of climbing)
                if (Math.abs(chaseCamPosition.y - position.y) > 5) { // if chasecam offset from gigagal yposition more than five pixels
                    chaseCamPosition.y -= Utils.absoluteToDirectionalValue(2.5f, directionY, Orientation.Y); // move chasecam back towards gigagal yposition provided yposition cannot be changed until fully reset
                    chaseCamPosition.x = position.x; // set chasecam position to gigagal xposition
                } else if (chaseCamPosition.y != position.y) { // if chasecam offset less than 5 but greater than 0 and actively looking
                    chaseCamPosition.set(position, 0); // reset chasecam
                    canLook = false; // disable look
                } else {
                    lookStartTime = 0;
                }
            // if can look and not standing (either airborne or climbing) and either not inputting y and/or not actively climbing
            } else {
                chaseCamPosition.set(position, 0);
                lookStartTime = 0;
            }
        }
        if (canClimb) {
            if (up || down) {
                velocity.x = 0;
                canHover = false;
                if (lookStartTime == 0) {
                    if (up) {
                        directionY = Direction.UP;
                    } else if (down) {
                        directionY = Direction.DOWN;
                    }
                    climb();
                }
            } else {
                climbTimeSeconds = 0;
            }
        }
     }

    private void setAtop(Ground ground) {
        touchedGround = ground;
        hoverStartTime = 0;
        clingStartTime = 0;
        canLook = true;
    }

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
            velocity.x += Utils.absoluteToDirectionalValue(Constants.TREADMILL_SPEED, ((RideableGround) touchedGround).getDirection(), Orientation.X);
        } else {
            velocity.x = 0;
        }

        groundState = GroundState.STANDING;
        aerialState = AerialState.GROUNDED;
        if (!canClimb) {
            canJump = true;
        } else {
            canJump = false;
        }
        canLook = true;
        if (turbo < Constants.MAX_TURBO) {
            turbo += Constants.STAND_TURBO_INCREMENT;
        }
    }

    private void fall() {
        if (onSinkable) {
            stand();
        }
        if (!onSkateable) {
            strideStartTime = 0;
        }
        aerialState = AerialState.FALLING;
        groundState = GroundState.AIRBORNE;
        canJump = false;
        canDash = false;
        canLook = true;
        if (turbo < Constants.MAX_TURBO) {
            turbo += Constants.FALL_TURBO_INCREMENT;
        }
        if (onUnbearable) {
            canHover = false;
            recoil(velocity);
        }
        if (!canCling) {
            touchedGround = null;
            canHover = true;
        }
        handleXInputs();
    }

    // disables all else by virtue of neither top level update conditions being satisfied due to state
    private void recoil(Vector2 velocity) {
        aerialState = AerialState.RECOILING;
        groundState = GroundState.AIRBORNE;
        strideStartTime = 0;
        canStride = false;
        canDash = false;
        canHover = false;
        canCling = false;
        this.velocity.x = velocity.x;
        this.velocity.y = velocity.y;
        if (aerialState != AerialState.RECOILING) {
            if (!canLook) {
                lookStartTime = 0;
            }
            canLook = true;
        } else {
            canLook = false;
            lookStartTime = 0;
        }
    }

    private void enableShoot(WeaponType weapon) {
        if (canShoot) {
            if (inputControls.shootButtonPressed) {
                if (chargeStartTime == 0) {
                    chargeStartTime = TimeUtils.nanoTime();
                } else if (chargeTimeSeconds > Constants.CHARGE_DURATION) {
                    ammoIntensity = AmmoIntensity.BLAST;
                } else if (chargeTimeSeconds > Constants.CHARGE_DURATION / 3) {
                    ammoIntensity = AmmoIntensity.CHARGE_SHOT;
                }
                chargeTimeSeconds = Utils.secondsSince(chargeStartTime);
            } else if (chargeStartTime != 0) {
                int ammoUsed;

                if (weapon == WeaponType.NATIVE
                        || (ammo < Constants.BLAST_AMMO_CONSUMPTION && ammoIntensity == AmmoIntensity.BLAST)
                        || ammo < Constants.SHOT_AMMO_CONSUMPTION) {
                    ammoUsed = 0;
                    weapon = WeaponType.NATIVE;
                } else {
                    ammoUsed = Utils.useAmmo(ammoIntensity);
                }

                shoot(ammoIntensity, weapon, ammoUsed);
                chargeStartTime = 0;
                chargeTimeSeconds = 0;
                this.ammoIntensity = AmmoIntensity.SHOT;
            }
        }
    }

    public void shoot(AmmoIntensity ammoIntensity, WeaponType weapon, int ammoUsed) {
        ammo -= ammoUsed;
        Vector2 ammoPosition = new Vector2(
                position.x + Utils.absoluteToDirectionalValue(Constants.GIGAGAL_CANNON_OFFSET.x, directionX, Orientation.X),
                position.y + Constants.GIGAGAL_CANNON_OFFSET.y
        );
        if (lookStartTime != 0) {
            ammoPosition.add(Utils.absoluteToDirectionalValue(0, directionX, Orientation.X),  Utils.absoluteToDirectionalValue(6, directionY, Orientation.Y));
            level.spawnAmmo(ammoPosition, directionY, Orientation.Y, ammoIntensity, weapon, true);
        } else {
            level.spawnAmmo(ammoPosition, directionX, Orientation.X, ammoIntensity, weapon, true);
        }
    }

    private void enableLook() {
        handleYInputs();
    }

    private void look() {
        float offset = 0;
        if (lookStartTime == 0) {
            lookStartTime = TimeUtils.nanoTime();
            chaseCamPosition.set(position, 0);
        } else if (groundState == GroundState.STANDING) {
            lookTimeSeconds = Utils.secondsSince(lookStartTime) - pauseTimeSeconds;
            if (lookTimeSeconds > 1) {
                offset += 1.5f;
                if (Math.abs(chaseCamPosition.y - position.y) < Constants.MAX_LOOK_DISTANCE) {
                    chaseCamPosition.y += Utils.absoluteToDirectionalValue(offset, directionY, Orientation.Y);
                    chaseCamPosition.x = position.x;
                }
            }
        }
        canJump = false;
    }

    private void enableStride() {
        handleXInputs();
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
            groundState = GroundState.STRIDING;
            strideStartTime = TimeUtils.nanoTime();
        }
        strideTimeSeconds = Utils.secondsSince(strideStartTime) - pauseTimeSeconds;
        strideAcceleration = strideTimeSeconds + Constants.GIGAGAL_STARTING_SPEED;
        velocity.x = Utils.absoluteToDirectionalValue(Math.min(Constants.GIGAGAL_MAX_SPEED * strideAcceleration + Constants.GIGAGAL_STARTING_SPEED, Constants.GIGAGAL_MAX_SPEED), directionX, Orientation.X);
        if (onRideable) {
            velocity.x += Utils.absoluteToDirectionalValue(Constants.TREADMILL_SPEED, ((RideableGround) touchedGround).getDirection(), Orientation.X);
        } else if (onSkateable) {
            velocity.x = strideSpeed + Utils.absoluteToDirectionalValue(Math.min(Constants.GIGAGAL_MAX_SPEED * strideAcceleration / 2 + Constants.GIGAGAL_STARTING_SPEED, Constants.GIGAGAL_MAX_SPEED * 2), directionX, Orientation.X);
        } else if (onSinkable) {
            velocity.x = Utils.absoluteToDirectionalValue(10, directionX, Orientation.X);
            velocity.y = -3;
        }
    }

    private void enableDash() {
        handleXInputs();
        if (canDash) {
            dash();
        }
    }

    private void dash() {
        if (groundState != GroundState.DASHING) {
            startTurbo = turbo;
            turboDuration = Constants.MAX_DASH_DURATION * (startTurbo / Constants.MAX_TURBO);
            groundState = GroundState.DASHING;
            dashStartTime = TimeUtils.nanoTime();
            strideStartTime = 0;
            canStride = false;
        }
        float dashTimeSeconds = Utils.secondsSince(dashStartTime) - pauseTimeSeconds;
        turbo = ((turboDuration - dashTimeSeconds) / turboDuration) * startTurbo;
        float dashSpeed = Constants.GIGAGAL_MAX_SPEED;
        if (onSkateable) {
            dashSpeed *= 1.75f;
        }
        if (turbo >= 1) {
            velocity.x = Utils.absoluteToDirectionalValue(dashSpeed, directionX, Orientation.X);
        } else {
            canDash = false;
            dashStartTime = 0;
            pauseTimeSeconds = 0;
            stand();
        }
    }

    private void enableJump() {
        if (((inputControls.jumpButtonJustPressed && canJump)
                || aerialState == AerialState.JUMPING)
                && lookStartTime == 0) {
            jump();
        }
    }

    private void jump() {
        if (canJump) {
            aerialState = AerialState.JUMPING;
            groundState = GroundState.AIRBORNE;
            jumpStartTime = TimeUtils.nanoTime();
            canJump = false;
        }
        velocity.x += Utils.absoluteToDirectionalValue(Constants.GIGAGAL_STARTING_SPEED * Constants.STRIDING_JUMP_MULTIPLIER, directionX, Orientation.X);
        float jumpTimeSeconds = Utils.secondsSince(jumpStartTime) - pauseTimeSeconds;
        if (jumpTimeSeconds < Constants.MAX_JUMP_DURATION) {
            velocity.y = Constants.JUMP_SPEED;
            velocity.y *= Constants.STRIDING_JUMP_MULTIPLIER;
            if (onBounceable) {
                velocity.y *= 2;
            }
            if (onSinkable) {
                velocity.y /= 1.25f;
            }
        } else {
            pauseTimeSeconds = 0;
            fall();
        }
    }

    private void enableHover() {
        if (canHover) {
            if (inputControls.jumpButtonJustPressed) {
                if (aerialState == AerialState.HOVERING) {
                    //   canHover = false;
                    hoverStartTime = 0;
                    velocity.x -= velocity.x / 2;
                    fall();
                } else {
                    hover(); // else hover if canHover is true (set to false after beginning hover)
                }
                // if jump key not pressed, but already hovering, continue to hover
            } else if (aerialState == AerialState.HOVERING) {
                hover();
            }
        }
    }

    private void hover() {
        // canHover can only be true just before beginning to hover
        if (aerialState != AerialState.HOVERING) {
            startTurbo = turbo;
            turboDuration = Constants.MAX_HOVER_DURATION * (startTurbo / Constants.MAX_TURBO);
            aerialState = AerialState.HOVERING; // indicates currently hovering
            hoverStartTime = TimeUtils.nanoTime(); // begins timing hover duration
        }
        hoverTimeSeconds = (Utils.secondsSince(hoverStartTime) - pauseTimeSeconds); // for comparing with max hover time
        turbo = (((turboDuration - hoverTimeSeconds)) / turboDuration * startTurbo);
        if (turbo >= 1) {
            velocity.y = 0; // disables impact of gravity
        } else {
            canHover = false;
            fall(); // when max hover time is exceeded
            pauseTimeSeconds = 0;
        }
        handleXInputs();
    }

    private void enableCling() {
        if (aerialState == AerialState.CLINGING) {
            cling();
        } else if (canCling){
            if (!canHover || aerialState == AerialState.HOVERING) {
                fall(); // begin descent from ground side sans access to hover
                canHover = false; // disable hover if not already
            }
            if (inputControls.jumpButtonJustPressed) {
                cling();
            }
        }
    }

    private void cling() {
        if (canCling) {
            aerialState = AerialState.CLINGING;
            groundState = GroundState.AIRBORNE;
            clingStartTime = TimeUtils.nanoTime();
            turboDuration = Constants.MAX_CLING_DURATION * (startTurbo / Constants.MAX_TURBO);
            if (!Utils.movingOppositeDirection(velocity.x, directionX, Orientation.X)) {
                directionX = Utils.getOppositeDirection(directionX);
            }
            hoverStartTime = 0;
            canJump = true;
            canCling = false;
        }
        float clingTimeSeconds = (Utils.secondsSince(clingStartTime) - pauseTimeSeconds) + ((100 - startTurbo) / Constants.MAX_CLING_DURATION);
        if (!inputControls.jumpButtonPressed) {
            if (clingTimeSeconds >= Constants.CLING_FRAME_DURATION) {
                velocity.x = Utils.absoluteToDirectionalValue(Constants.GIGAGAL_MAX_SPEED, directionX, Orientation.X);
                jump();
            } else {
                pauseTimeSeconds = 0;
                canHover = true;
            }
        } else {
            if (inputControls.downButtonPressed || clingTimeSeconds > Constants.MAX_CLING_DURATION) {
                velocity.y += Constants.CLING_GRAVITY_OFFSET;
            } else {
                turbo = ((turboDuration - clingTimeSeconds) / turboDuration * startTurbo);
                velocity.y = 0;
            }
        }
    }

    private void enableClimb() {
        if (onClimbable) {
            if (inputControls.jumpButtonPressed) {
                if (lookStartTime == 0) {
                    canLook = false;
                    canClimb = true;
                    handleYInputs();
                }
            } else {
                climbTimeSeconds = 0;
            }
        } else {
            canClimb = false;
        }
    }

    private void climb() {
        if (climbStartTime == 0) {
            climbStartTime = TimeUtils.nanoTime();
        }
        canHover = false;
        climbTimeSeconds = Utils.secondsSince(climbStartTime);
        velocity.y = Utils.absoluteToDirectionalValue(Constants.CLIMB_SPEED, directionY, Orientation.Y);
        int climbAnimationPercent = (int) (climbTimeSeconds * 100);
        if ((climbAnimationPercent) % 25 >= 0 && (climbAnimationPercent) % 25 <= 13) {
            directionX = Direction.RIGHT;
        } else {
            directionX = Direction.LEFT;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion region = Assets.getInstance().getGigaGalAssets().standRight;
        if (climbTimeSeconds != 0
                || (canClimb && lookStartTime == 0 && climbStartTime != 0)) {
            if (directionX == Direction.LEFT) {
                region = Assets.getInstance().getGigaGalAssets().climb.getKeyFrame(0.12f);
            } else if (directionX == Direction.RIGHT) {
                region = Assets.getInstance().getGigaGalAssets().climb.getKeyFrame(0.25f);
            }
        } else if (directionX == Direction.RIGHT) {
            if (aerialState != AerialState.GROUNDED) {
                if (aerialState == AerialState.HOVERING) {
                    hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
                    if (lookStartTime != 0) {
                        if (directionY == Direction.UP) {
                            region = Assets.getInstance().getGigaGalAssets().lookupHoverRight.getKeyFrame(hoverTimeSeconds);
                        } else if (directionY == Direction.DOWN) {
                            region = Assets.getInstance().getGigaGalAssets().lookdownHoverRight.getKeyFrame(hoverTimeSeconds);
                        }
                    } else {
                        region = Assets.getInstance().getGigaGalAssets().hoverRight.getKeyFrame(hoverTimeSeconds);
                    }
                } else if (aerialState == AerialState.CLINGING) {
                    region = Assets.getInstance().getGigaGalAssets().clingRight;
                } else if (aerialState == AerialState.RECOILING){
                    region = Assets.getInstance().getGigaGalAssets().recoilRight;
                } else if (lookStartTime != 0) {
                    if (directionY == Direction.UP) {
                        region = Assets.getInstance().getGigaGalAssets().lookupFallRight;
                    } else if (directionY == Direction.DOWN) {
                        region = Assets.getInstance().getGigaGalAssets().lookdownFallRight;
                    }
                } else {
                    region = Assets.getInstance().getGigaGalAssets().fallRight;
                }
            } else if (groundState == GroundState.STANDING) {
                if (lookStartTime != 0) {
                    if (directionY == Direction.UP) {
                        if (climbStartTime == 0) {
                            region = Assets.getInstance().getGigaGalAssets().lookupStandRight;
                        } else {
                            region = Assets.getInstance().getGigaGalAssets().lookupFallRight;
                        }
                    } else if (directionY == Direction.DOWN) {
                        if (climbStartTime == 0) {
                            region = Assets.getInstance().getGigaGalAssets().lookdownStandRight;
                        } else {
                            region = Assets.getInstance().getGigaGalAssets().lookdownFallRight;
                        }
                    }
                } else {
                    region = Assets.getInstance().getGigaGalAssets().standRight;
                }
            } else if (groundState == GroundState.STRIDING) {
                region = Assets.getInstance().getGigaGalAssets().strideRight.getKeyFrame(Math.min(strideAcceleration * strideAcceleration, strideAcceleration));
            } else if (groundState == GroundState.DASHING) {
                region = Assets.getInstance().getGigaGalAssets().dashRight;
            }
        } else if (directionX == Direction.LEFT) {
            if (aerialState != AerialState.GROUNDED) {
                if (aerialState == AerialState.HOVERING) {
                    hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
                    if (lookStartTime != 0) {
                        if (directionY == Direction.UP) {
                            region = Assets.getInstance().getGigaGalAssets().lookupHoverLeft.getKeyFrame(hoverTimeSeconds);
                        } else if (directionY == Direction.DOWN) {
                            region = Assets.getInstance().getGigaGalAssets().lookdownHoverLeft.getKeyFrame(hoverTimeSeconds);
                        }
                    } else {
                        region = Assets.getInstance().getGigaGalAssets().hoverLeft.getKeyFrame(hoverTimeSeconds);
                    }
                } else if (aerialState == AerialState.CLINGING) {
                    region = Assets.getInstance().getGigaGalAssets().clingLeft;
                } else if (aerialState == AerialState.RECOILING) {
                    region = Assets.getInstance().getGigaGalAssets().recoilLeft;
                } else if (lookStartTime != 0) {
                    if (directionY == Direction.UP) {
                        region = Assets.getInstance().getGigaGalAssets().lookupFallLeft;
                    } else if (directionY == Direction.DOWN) {
                        region = Assets.getInstance().getGigaGalAssets().lookdownFallLeft;
                    }
                } else {
                    region = Assets.getInstance().getGigaGalAssets().fallLeft;
                }
            } else if (groundState == GroundState.STANDING) {
                if (lookStartTime != 0) {
                    if (directionY == Direction.UP) {
                        if (climbStartTime == 0) {
                            region = Assets.getInstance().getGigaGalAssets().lookupStandLeft;
                        } else {
                            region = Assets.getInstance().getGigaGalAssets().lookupFallLeft;
                        }
                    } else if (directionY == Direction.DOWN) {
                        if (climbStartTime == 0) {
                            region = Assets.getInstance().getGigaGalAssets().lookdownStandLeft;
                        } else {
                            region = Assets.getInstance().getGigaGalAssets().lookdownFallLeft;
                        }
                    }
                } else {
                    region = Assets.getInstance().getGigaGalAssets().standLeft;
                }
            } else if (groundState == GroundState.STRIDING) {
                region = Assets.getInstance().getGigaGalAssets().strideLeft.getKeyFrame(Math.min(strideAcceleration * strideAcceleration, strideAcceleration));
            } else if (groundState == GroundState.DASHING) {
                region = Assets.getInstance().getGigaGalAssets().dashLeft;
            }
        }
        Utils.drawTextureRegion(batch, region, position, Constants.GIGAGAL_EYE_POSITION);
    }

    // Getters
    @Override public int getHealth() { return health; }
    @Override public float getTurbo() { return turbo; }
    @Override public Direction getDirectionX() { return directionX; }
    @Override public Direction getDirectionY() { return directionY; }
    @Override public Vector2 getPosition() { return position; }
    @Override public float getWidth() { return Constants.GIGAGAL_STANCE_WIDTH; }
    @Override public float getHeight() { return Constants.GIGAGAL_HEIGHT; }
    @Override public float getLeft() { return position.x - getHalfWidth(); }
    @Override public float getRight() { return position.x + getHalfWidth(); }
    @Override public float getTop() { return position.y + Constants.GIGAGAL_HEAD_RADIUS; }
    @Override public float getBottom() { return position.y - Constants.GIGAGAL_EYE_HEIGHT; }
    @Override public Rectangle getBounds() { return  new Rectangle(getLeft(), getBottom(), getWidth(), getHeight()); }
    @Override public boolean getJumpStatus() { return canJump; }
    @Override public boolean getHoverStatus() { return canHover; }
    @Override public boolean getClingStatus() { return canCling; }
    @Override public boolean getDashStatus() { return canDash; }
    @Override public boolean getClimbStatus() { return canClimb; }
    @Override public AmmoIntensity getAmmoIntensity() { return ammoIntensity; }
    @Override public WeaponType getWeapon() { return weapon; }
    public float getHalfWidth() { return Constants.GIGAGAL_STANCE_WIDTH / 2; }
    public List<WeaponType> getWeaponList() { return weaponList; }
    public int getAmmo() { return ammo; }
    public int getLives() { return lives; }
    public float getPauseTimeSeconds() { return pauseTimeSeconds; }
    public boolean getPaused() { return paused; }
    public Vector3 getChaseCamPosition() { return chaseCamPosition; }
    public long getLookStartTime() { return lookStartTime; }
    public float getChargeTimeSeconds() { return chargeTimeSeconds; }
    public AerialState getAerialState() { return aerialState; }
    public GroundState getGroundState() { return groundState; }

    // Setters
    public void setDirectionX(Direction directionX) { this.directionX = directionX; }
    public void setDirectionY(Direction directionY) { this.directionY = directionY; }
    public void setLives(int lives) { this.lives = lives; }
    public void setHealth(int health) { this.health = health; }
    public void setPauseTimeSeconds(float pauseTimeSeconds) { this.pauseTimeSeconds = pauseTimeSeconds; }
    public void setInputControls(InputControls inputControls) { this.inputControls = inputControls; }
    public void addWeapon(WeaponType weapon) { weaponToggler.add(weapon); }
    public void toggleWeapon(Direction toggleDirection) {
        if (weaponList.size() > 1) {
            if (toggleDirection == Direction.UP) {
                if (!weaponToggler.hasNext()) {
                    while (weaponToggler.hasPrevious()) {
                        weaponToggler.previous();
                    }
                }
                if (weapon == weaponToggler.next()) {
                    toggleWeapon(toggleDirection);
                } else {
                    weapon = weaponToggler.previous();
                }
            } else if (toggleDirection == Direction.DOWN) {
                if (!weaponToggler.hasPrevious()) {
                    while (weaponToggler.hasNext()) {
                        weaponToggler.next();
                    }
                }
                if (weapon == weaponToggler.previous()) {
                    toggleWeapon(toggleDirection);
                } else {
                    weapon = weaponToggler.next();
                }
            }
        }
    }
}
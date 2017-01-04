package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.overlays.InputControls;
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
public class GigaGal implements Physical {

    // fields
    public final static String TAG = GigaGal.class.getName();
    private List<WeaponType> weaponList;
    private ListIterator<WeaponType> weaponToggler;
    private Level level;
    private Vector2 spawnLocation;
    private Vector2 position;
    private Vector2 previousFramePosition;
    private Vector2 velocity;
    private Vector3 chaseCamPosition;
    private Direction facing;
    private AerialState aerialState;
    private GroundState groundState;
    private WeaponType weapon;
    private AmmoIntensity ammoIntensity;
    private Direction lookDirection;
    private Direction toggleDirection;
    private Direction treadDirection;
    private Direction climbDirection;
    private Spring loadedSpring;
    private long lookStartTime;
    private long strideStartTime;
    private long jumpStartTime;
    private long dashStartTime;
    private long hoverStartTime;
    private long ricochetStartTime;
    private long chargeStartTime;
    private long recoveryStartTime;
    private long climbStartTime;
    private float pauseDuration;
    private float turboDuration;
    private float turbo;
    private float startTurbo;
    private float strideAcceleration;
    private float lookTimeSeconds;
    private float hoverTimeSeconds;
    private float dashTimeSeconds;
    private float jumpTimeSeconds;
    private float strideTimeSeconds;
    private float ricochetTimeSeconds;
    private float recoveryTimeSeconds;
    private float climbTimeSeconds;
    private float chargeTimeSeconds;
    private float aerialTakeoff;
    private float speedAtChangeFacing;
    private int lives;
    private int ammo;
    private int health;
    private boolean canLook;
    private boolean canStride;
    private boolean canDash;
    private boolean canJump;
    private boolean canHover;
    private boolean canRicochet;
    private boolean canCharge;
    private boolean canChangeDirection;
    private boolean canShoot;
    private boolean canClimb;
    private boolean knockedBack;
    private boolean slidGround;
    private boolean groundedAtop;
    private boolean pauseState;
    private boolean onTreadmill;
    private boolean onSkateable;
    private boolean onCoals;
    private boolean onClimbable;
    private boolean onSink;
    private InputControls inputControls;

    // ctor
    public GigaGal(Vector2 spawnLocation, Level level) {
        this.spawnLocation = spawnLocation;
        this.level = level;
        position = new Vector2();
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

    public void update(float delta) {
        previousFramePosition.set(position);
        position.mulAdd(velocity, delta);
        touchGround(level.getGrounds());
        recoilFromHazards(level.getHazards());
        collectPowerups(level.getPowerups());
        enableShoot(weapon);
        enableClimb();

        if (aerialState == AerialState.GROUNDED && groundState != GroundState.AIRBORNE) {
            velocity.y = 0;
            if (groundState == GroundState.STANDING) {
                stand();
                enableLook();
                enableStride();
                enableDash();
                enableJump();
            } else if (groundState == GroundState.STRIDING) {
                enableStride();
                enableDash();
                enableJump();
            } else if (groundState == GroundState.DASHING) {
                enableDash();
                enableJump();
            }
        }

        if (groundState == GroundState.AIRBORNE && aerialState != AerialState.GROUNDED) {
            velocity.y -= Constants.GRAVITY;
            if (aerialState == AerialState.FALLING) {
                fall();
                enableLook();
                enableHover();
                enableRicochet();
            } else if (aerialState == AerialState.JUMPING) {
                enableLook();
                enableJump();
                enableRicochet();
            } else if (aerialState == AerialState.HOVERING) {
                enableHover();
                enableRicochet();
            } else if (aerialState == AerialState.RICOCHETING) {
                enableRicochet();
            }
        }
    }

    private void touchGround(Array<Ground> grounds) {
        float slidGroundTop = 0;
        float slidGroundBottom = 0;
        float groundedAtopLeft = 0;
        float groundedAtopRight = 0;
        onTreadmill = false;
        onSkateable = false;
        treadDirection = null;
        onClimbable = false;
        for (Ground ground : grounds) {
            // if currently within ground left and right sides
            if (Utils.contactingSides(ground, position.x)) {
                // apply following rules (bump side and bottom) only if ground height > ledge height
                // ledges only apply collision detection on top, and not on sides and bottom as do grounds
                if (getBottom() <= ground.getTop() && getTop() >= ground.getBottom()) {
                    if (ground instanceof Climbable && Utils.betweenSides(ground, position.x)) {
                            onClimbable = true;
                    }
                    if (ground.getHeight() > Constants.MAX_LEDGE_HEIGHT) {
                        // if during previous frame was not, while currently is, between ground left and right sides
                        if (!Utils.contactingSides(ground, previousFramePosition.x)) {
                            // only when not grounded and not recoiling
                            if (groundState == GroundState.AIRBORNE && aerialState != AerialState.RECOILING) {
                                // if lateral velocity (magnitude, without concern for direction) greater than one third max speed,
                                // boost lateral velocity by starting speed, enable ricochet, verify slid ground and capture slid ground boundaries
                                if (Math.abs(velocity.x) > Constants.GIGAGAL_MAX_SPEED / 3 && !(ground instanceof Sink)) {
                                    // if already ricocheting, halt lateral progression
                                    if (aerialState == AerialState.RICOCHETING) {
                                        velocity.x = 0; // halt lateral progression
                                        // if not already ricocheting and hover was previously activated before grounding
                                    } else if (!canHover || aerialState == AerialState.HOVERING) {
                                        fall(); // begin descent from ground side sans access to hover
                                        canHover = false; // disable hover if not already
                                    }
                                    if (aerialState != AerialState.RICOCHETING) {
                                        if (!slidGround) {
                                            startTurbo = Math.max(turbo, Constants.GROUND_SLIDE_MIN_TURBO);
                                        }
                                        turbo = Math.min((Math.abs((getTop() - ground.getBottom()) / (ground.getTop() + getHeight() - ground.getBottom())) * startTurbo), Constants.MAX_TURBO);
                                    }
                                    velocity.x += Utils.absoluteToDirectionalValue(Constants.GIGAGAL_STARTING_SPEED, facing, Orientation.LATERAL); // boost lateral velocity by starting speed
                                    canRicochet = true; // enable ricochet
                                    slidGround = true; // verify slid ground
                                    slidGroundTop = ground.getTop(); // capture slid ground boundary
                                    slidGroundBottom = ground.getBottom(); // capture slid ground boundary
                                    // if absval lateral velocity  not greater than one third max speed but aerial and bumping ground side, fall
                                } else {
                                    // if not already hovering and descending, also disable hover
                                    if (aerialState != AerialState.HOVERING && velocity.y < 0) {
                                        canHover = false; // disable hover
                                    }
                                    slidGround = false;
                                    fall(); // fall regardless of whether or not inner condition met
                                }
                                // only when grounded
                            } else if (aerialState == AerialState.GROUNDED) {
                                if (Math.abs(getBottom() - ground.getTop()) > 1) {
                                    speedAtChangeFacing = 0;
                                    velocity.x = 0;
                                }
                                //   stand();
                            }
                            if ((!(ground instanceof Treadmill && (Math.abs(getBottom() - ground.getTop()) <= 1)))
                                    && (!(ground instanceof Rope))
                                    && !(ground instanceof Skateable && (Math.abs(getBottom() - ground.getTop()) <= 1))
                                    && !(ground instanceof Coals && (Math.abs(getBottom() - ground.getTop()) <= 1))
                                    && !(ground instanceof Sink)) {
                                // if contact with ground sides detected without concern for ground state (either grounded or airborne),
                                // reset stride acceleration, disable stride and dash, and set gigagal at ground side
                                if (groundState != GroundState.STRIDING || groundState != GroundState.DASHING) {
                                    strideStartTime = 0; // reset stride acceleration
                                }
                                canStride = false; // disable stride
                                canDash = false; // disable dash
                                position.x = previousFramePosition.x; // halt lateral progression
                            }
                            // else if no detection with ground sides, disable ricochet
                        } else {
                            if (ground instanceof Sink) {
                                if (onSink == false) {
                                    stand();
                                }
                                lookTimeSeconds = 0;
                                lookStartTime = 0;
                                canDash = false;
                                canHover = false;
                                onSink = true;
                                velocity.y = -3;
                                groundedAtop = true; // verify contact with ground top
                                groundedAtopLeft = ground.getLeft(); // capture grounded ground boundary
                                groundedAtopRight = ground.getRight(); // capture grounded ground boundary
                            } else {
                                onSink = false;
                            }
                            canRicochet = false; // disable ricochet
                            slidGround = false;
                        }
                        // if contact with ground bottom detected, halts upward progression and set gigagal at ground bottom
                        if (((previousFramePosition.y + Constants.GIGAGAL_HEAD_RADIUS) <= ground.getBottom()
                                && !(ground instanceof Climbable)
                                && !onSink)
                                && climbDirection == null) {
                            velocity.y = 0; // prevents from ascending above ground bottom
                            position.y = previousFramePosition.y;  // sets gigagal at ground bottom
                            fall(); // descend from point of contact with ground bottom
                        }
                    }
                    // if contact with ground top detected, halt downward progression and set gigagal atop ground
                    if (((getBottom() <= ground.getTop() && ground.getTop() != slidGroundTop
                    && previousFramePosition.y - Constants.GIGAGAL_EYE_HEIGHT >= ground.getTop())
                    && climbDirection == null)
                    || (ground instanceof Climbable && climbStartTime != 0 && climbDirection == null && canClimb)) {
                        if (groundState != GroundState.DASHING) {
                            pauseDuration = 0;
                        }
                        if (!(ground instanceof Climbable)) {
                            velocity.y = 0; // prevents from descending beneath ground top
                            if (!onSink) {
                                position.y = ground.getTop() + Constants.GIGAGAL_EYE_HEIGHT; // sets Gigagal atop ground
                            }
                        }
                        canChangeDirection = true; // enable change of direction
                        groundedAtop = true; // verify contact with ground top
                        groundedAtopLeft = ground.getLeft(); // capture grounded ground boundary
                        groundedAtopRight = ground.getRight(); // capture grounded ground boundary
                        hoverStartTime = 0; // reset hover
                        ricochetStartTime = 0; // reset ricochet
                        knockedBack = false; // reset knockback boolean
                        if (climbDirection == null && !onCoals && !Utils.movingOppositeDirectionFacing(velocity.x, facing)) {
                            canHover = true; // enable hover
                        } else {
                            canHover = false;
                        }
                        if (lookDirection != null && aerialState != AerialState.GROUNDED) {
                            lookStartTime = 0;
                            lookDirection = null;
                        }
                        canLook = true;
                        if (ground instanceof Skateable) {
                            onSkateable = true;
                        }
                        // if groundstate is airborne, set to standing
                        if (groundState == GroundState.AIRBORNE) {
                            stand(); // set groundstate to standing
                        }
                        if (ground instanceof Spring) {
                            loadedSpring = (Spring) ground;
                            loadedSpring.setLoaded(true);
                        } else if (ground instanceof Treadmill) {
                            Treadmill treadmill = (Treadmill) ground;
                            onTreadmill = true;
                            treadDirection = treadmill.getDirection();
                        } else if (ground instanceof Coals) {
                            onCoals = true;
                            canHover = false;
                            Random lateralKnockback = new Random();
                            velocity.set(Utils.absoluteToDirectionalValue(lateralKnockback.nextFloat() * 200, facing, Orientation.LATERAL), Constants.FLAME_KNOCKBACK.y);
                            recoil(velocity);
                        } else {
                            onCoals = false;
                        }
                    }
                    // if below minimum ground distance while descending excluding post-ricochet, disable ricochet and hover
                    // caution when crossing plane between ground top and minimum hover height / ground distance
                    // cannons, which inherit ground, can be mounted along sides of grounds causing accidental plane breakage
                    if (getBottom() < (ground.getTop() + Constants.MIN_GROUND_DISTANCE)
                            && getBottom() > ground.getTop() // GG's bottom is greater than ground top but less than boundary
                            && velocity.y < 0 // prevents disabling features when crossing boundary while ascending on jump
                            && ricochetStartTime == 0 // only if have not ricocheted since last grounded
                            && !(ground instanceof Cannon) // only if ground is not instance of ground
                            ) {
                        canRicochet = false; // disables ricochet
                        canHover = false; // disables hover
                    }
                }
            }
        }
        if (!onClimbable) {
            climbStartTime = 0;
            climbTimeSeconds = 0;
            climbDirection = null;
        }
        // disables ricochet if no contact with slid ground side
        if (slidGround) {
            if (getBottom() > slidGroundTop  || getTop() < slidGroundBottom) {
                canRicochet = false;
                slidGround = false;
            }
        }
        // falls if no detection with grounded ground top
        if (groundedAtop) {
            if (getRight() < groundedAtopLeft || getLeft() > groundedAtopRight) {
                if (loadedSpring != null) {
                    loadedSpring.resetStartTime();
                    loadedSpring.setLoaded(false);
                    if (Utils.secondsSince(loadedSpring.getStartTime()) > Constants.SPRING_UNLOAD_DURATION) {
                        loadedSpring.resetStartTime();
                        loadedSpring = null;
                    }
                }
                if ((aerialState != AerialState.RECOILING)){
                    onSink = false;
                    lookTimeSeconds = 0;
                    lookStartTime = TimeUtils.nanoTime();
                    groundedAtop = false;
                    fall();
                }
            }
        }
    }

    private void handleLateralInputs() {
        boolean left = inputControls.leftButtonPressed;
        boolean right = inputControls.rightButtonPressed;
        boolean directionChanged = false;
        boolean isStriding = true;
        if (left && !right) {
            directionChanged = Utils.setFacing(this, Direction.LEFT);
        } else if (!left && right) {
            directionChanged = Utils.setFacing(this, Direction.RIGHT);
        } else {
            isStriding = false;
        }
        if (groundState != GroundState.AIRBORNE && climbStartTime == 0) {
            if (lookDirection == null) {
                if (directionChanged) {
                    if (groundState == groundState.DASHING) {
                        dashStartTime = 0;
                        canDash = false;
                    }
                    speedAtChangeFacing = velocity.x;
                    strideStartTime = 0;
                    stand();
                } else if (groundState != GroundState.DASHING) {
                    if (isStriding) {
                        if (!canStride) {
                            if (strideStartTime == 0) {
                                canStride = true;
                            } else if (Utils.secondsSince(strideStartTime) > Constants.DOUBLE_TAP_SPEED) {
                                strideStartTime = 0;
                            } else if (!onSink){
                                canDash = true;
                            }
                        }
                    } else {
                        pauseDuration = 0;
                        stand();
                        canStride = false;
                    }
                }
            }
        } else if (directionChanged) {
            recoil(new Vector2(velocity.x / 2, velocity.y));
        }
    }

    private void collectPowerups(DelayedRemovalArray<Powerup> powerups) {
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

    // detects contact with enemy (change aerial & ground state to recoil until grounded)
    private void recoilFromHazards(Array<Hazard> hazards) {
        for (Hazard hazard : hazards) {
            recoveryTimeSeconds = Utils.secondsSince(recoveryStartTime) - pauseDuration;
            if (!knockedBack
                    &&  recoveryTimeSeconds > Constants.RECOVERY_TIME) {
                Rectangle bounds = new Rectangle(hazard.getLeft(), hazard.getBottom(), hazard.getWidth(), hazard.getHeight());
                if (getBounds().overlaps(bounds)) {
                    knockedBack = true;
                    ammoIntensity = AmmoIntensity.SHOT;
                    recoveryStartTime = TimeUtils.nanoTime();
                    chargeStartTime = 0;
                    int damage = hazard.getDamage();
                    float margin = 0;
                    if (hazard instanceof Destructible) {
                        margin = hazard.getWidth() / 6;
                    }
                    if (!(hazard instanceof Ammo && ((Ammo) hazard).isFromGigagal())) {
                        chaseCamPosition.set(position, 0);
                        lookDirection = null;
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
                                recoil(new Vector2((Utils.absoluteToDirectionalValue(zoomba.getMountKnockback().x, facing, Orientation.LATERAL)), zoomba.getMountKnockback().y));
                                damage = zoomba.getMountDamage();
                            } else {
                                recoil(new Vector2((Utils.absoluteToDirectionalValue(hazard.getKnockback().x, facing, Orientation.LATERAL)), hazard.getKnockback().y));
                            }
                        }
                        health -= damage;
                    }
                }
            }
        }
    }

    // disables all else by virtue of neither top level update conditions being satisfied due to state
    private void recoil(Vector2 velocity) {
        aerialState = AerialState.RECOILING;
        groundState = GroundState.AIRBORNE;
        strideStartTime = 0;
        canStride = false;
        canDash = false;
        canHover = false;
        canRicochet = false;
        canChangeDirection = false;
        canLook = false;
        lookStartTime = 0;
        this.velocity.x = velocity.x;
        this.velocity.y = velocity.y;
    }

    public void enableToggle(Direction toggleDirection) {
        boolean directionChanged = (this.toggleDirection != toggleDirection);
        if (inputControls.jumpButtonJustPressed) {
            lookStartTime = TimeUtils.nanoTime();
            if (directionChanged) {
                toggle(toggleDirection);
            }
            toggle(toggleDirection);
            this.toggleDirection = toggleDirection;
        }
    }

    private void toggle(Direction toggleDirection) {
        if (toggleDirection == Direction.UP) {
            if (weaponToggler.hasNext()) {
                weapon = weaponToggler.next();
            } else {
                while (weaponToggler.hasPrevious()) {
                    weaponToggler.previous();
                }
                weapon = weaponToggler.next();
            }
            this.toggleDirection = toggleDirection;
        } else if (toggleDirection == Direction.DOWN) {
            if (weaponToggler.hasPrevious()) {
                weapon = weaponToggler.previous();
            } else {
                while (weaponToggler.hasNext()) {
                    weaponToggler.next();
                }
                weapon = weaponToggler.previous();
            }
            this.toggleDirection = toggleDirection;
        }
    }

    private void enableShoot(WeaponType weapon) {
        if (canShoot) {
            if (inputControls.shootButtonPressed) {
                if (chargeStartTime == 0) {
                    canCharge = true;
                    chargeStartTime = TimeUtils.nanoTime();
                } else if (chargeTimeSeconds > Constants.CHARGE_DURATION) {
                    ammoIntensity = AmmoIntensity.BLAST;
                }
                chargeTimeSeconds = Utils.secondsSince(chargeStartTime);
            } else if (canCharge) {
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
                canCharge = false;
            }
        }
    }

    public void shoot(AmmoIntensity ammoIntensity, WeaponType weapon, int ammoUsed) {
        ammo -= ammoUsed;
        Vector2 ammoPosition = new Vector2(
                position.x + Utils.absoluteToDirectionalValue(Constants.GIGAGAL_CANNON_OFFSET.x, facing, Orientation.LATERAL),
                position.y + Constants.GIGAGAL_CANNON_OFFSET.y
        );
        if (lookDirection == Direction.UP || lookDirection == Direction.DOWN) {
            ammoPosition.add(Utils.absoluteToDirectionalValue(0, facing, Orientation.LATERAL),  Utils.absoluteToDirectionalValue(6, lookDirection, Orientation.VERTICAL));
            level.spawnAmmo(ammoPosition, lookDirection, Orientation.VERTICAL, ammoIntensity, weapon, true);
        } else {
            level.spawnAmmo(ammoPosition, facing, Orientation.LATERAL, ammoIntensity, weapon, true);
        }
    }

    public void respawn() {
        position.set(spawnLocation);
        chaseCamPosition.set(position, 0);
        velocity.setZero();
        facing = Direction.RIGHT;
        climbDirection = null;
        groundState = GroundState.AIRBORNE;
        aerialState = AerialState.FALLING;
        pauseState = false;
        canClimb = false;
        canLook = false;
        canStride = false;
        canJump = false;
        canDash = false;
        canHover = false;
        canRicochet = false;
        canShoot = true;
        turboDuration = 0;
        canCharge = false;
        canChangeDirection = false;
        ammoIntensity = AmmoIntensity.SHOT;
        slidGround = false;
        groundedAtop = false;
        knockedBack = false;
        onTreadmill = false;
        onSkateable = false;
        onCoals = false;
        onClimbable = false;
        onSink = false;
        chargeStartTime = 0;
        strideStartTime = 0;
        climbStartTime = 0;
        jumpStartTime = 0;
        dashStartTime = 0;
        chargeTimeSeconds = 0;
        pauseDuration = 0;
        turboDuration = 0;
        recoveryStartTime = TimeUtils.nanoTime();
        health = Constants.MAX_HEALTH;
        turbo = Constants.MAX_TURBO;
        startTurbo = turbo;
    }

    private void enableLook() {
        boolean up = inputControls.upButtonPressed;
        boolean down = inputControls.downButtonPressed;
        boolean looking = up || down;
        boolean directionChanged = false;
        if (canLook) {
            if (looking && climbDirection == null) {
                canStride = false;
                canHover = false;
                if (up) {
                    lookDirection = Direction.UP;
                    if (chaseCamPosition.y < position.y) {
                        directionChanged = true;
                    }
                } else if (down) {
                    lookDirection = Direction.DOWN;
                    if (chaseCamPosition.y > position.y) {
                        directionChanged = true;
                    }
                    if (onSink) {
                        velocity.y *= 5;
                    }
                }
                if (directionChanged && groundState == GroundState.STANDING) {
                    chaseCamPosition.y += Utils.absoluteToDirectionalValue(.75f, lookDirection, Orientation.VERTICAL);
                }
                enableToggle(lookDirection);
                look();
            } else if ( groundState == GroundState.STANDING) {
                if (Math.abs(chaseCamPosition.y - position.y) > 5) {
                    chaseCamPosition.y -= Utils.absoluteToDirectionalValue(2.5f, lookDirection, Orientation.VERTICAL);
                    chaseCamPosition.x = position.x;
                } else if (chaseCamPosition.y != position.y && lookStartTime != 0) {
                    chaseCamPosition.set(position, 0);
                    lookDirection = null;
                    canLook = false;
                } else {
                    chaseCamPosition.set(position, 0);
                    lookDirection = null;
                    lookStartTime = 0;
                }
            } else {
                if (climbDirection != null
                        || (canClimb && lookDirection == null && climbStartTime != 0)
                        || (Utils.movingOppositeDirectionFacing(velocity.x, facing))) {
                    canHover = false;
                } else if (hoverStartTime == 0 && !onCoals && !onSink) {
                    canHover = true;
                }
                chaseCamPosition.set(position, 0);
                lookDirection = null;
                lookStartTime = 0;
            }
        }
    }

    private void look() {
        float offset = 0;
        if (groundState == GroundState.STANDING) {
            if (lookStartTime == 0) {
                lookStartTime = TimeUtils.nanoTime();
                chaseCamPosition.set(position, 0);
            } else {
                lookTimeSeconds = Utils.secondsSince(lookStartTime) - pauseDuration;
                if (lookTimeSeconds > 1) {
                    offset += 1.5f;
                    if (Math.abs(chaseCamPosition.y - position.y) < Constants.MAX_LOOK_DISTANCE) {
                        chaseCamPosition.y += Utils.absoluteToDirectionalValue(offset, lookDirection, Orientation.VERTICAL);
                        chaseCamPosition.x = position.x;
                    }
                }
            }
        }
        canJump = false;
    }

    private void enableStride() {
        handleLateralInputs();
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
            speedAtChangeFacing = velocity.x;
            groundState = GroundState.STRIDING;
            strideStartTime = TimeUtils.nanoTime();
        }
        strideTimeSeconds = Utils.secondsSince(strideStartTime) - pauseDuration;
        strideAcceleration = strideTimeSeconds + Constants.GIGAGAL_STARTING_SPEED;
        velocity.x = Utils.absoluteToDirectionalValue(Math.min(Constants.GIGAGAL_MAX_SPEED * strideAcceleration + Constants.GIGAGAL_STARTING_SPEED, Constants.GIGAGAL_MAX_SPEED), facing, Orientation.LATERAL);
        if (onTreadmill) {
            velocity.x += Utils.absoluteToDirectionalValue(Constants.TREADMILL_SPEED, treadDirection, Orientation.LATERAL);
        } else if (onSkateable) {
            velocity.x = speedAtChangeFacing + Utils.absoluteToDirectionalValue(Math.min(Constants.GIGAGAL_MAX_SPEED * strideAcceleration / 2 + Constants.GIGAGAL_STARTING_SPEED, Constants.GIGAGAL_MAX_SPEED * 2), facing, Orientation.LATERAL);
        } else if (onSink) {
            velocity.x = Utils.absoluteToDirectionalValue(10, facing, Orientation.LATERAL);
            velocity.y = -3;
        }
    }

    private void enableDash() {
        handleLateralInputs();
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
        dashTimeSeconds = Utils.secondsSince(dashStartTime) - pauseDuration;
        turbo = ((turboDuration - dashTimeSeconds) / turboDuration) * startTurbo;
        float dashSpeed = Constants.GIGAGAL_MAX_SPEED;
        if (onSkateable) {
            dashSpeed *= 1.75f;
        }
        if (turbo >= 1) {
            velocity.x = Utils.absoluteToDirectionalValue(dashSpeed, facing, Orientation.LATERAL);
        } else {
            canDash = false;
            dashStartTime = 0;
            pauseDuration = 0;
            stand();
        }
    }

    private void enableJump() {
        if (((inputControls.jumpButtonJustPressed && canJump)
                || aerialState == AerialState.JUMPING)
                && lookDirection == null) {
            jump();
        }
    }

    private void jump() {
        if (canJump) {
            aerialTakeoff = position.x;
            aerialState = AerialState.JUMPING;
            groundState = GroundState.AIRBORNE;
            jumpStartTime = TimeUtils.nanoTime();
            canJump = false;
        }
        velocity.x += Utils.absoluteToDirectionalValue(Constants.GIGAGAL_STARTING_SPEED * Constants.STRIDING_JUMP_MULTIPLIER, facing, Orientation.LATERAL);
        jumpTimeSeconds = Utils.secondsSince(jumpStartTime) - pauseDuration;
        if (jumpTimeSeconds < Constants.MAX_JUMP_DURATION) {
            velocity.y = Constants.JUMP_SPEED;
            velocity.y *= Constants.STRIDING_JUMP_MULTIPLIER;
            if (loadedSpring != null) {
                velocity.y *= 2;
            }
            if (onSink) {
                velocity.y /= 2;
            }
        } else if (!onSink) {
            pauseDuration = 0;
            fall();
        } else {
            stand();
        }
    }

    private void enableHover() {
        if (canHover) {
            if (inputControls.jumpButtonJustPressed) {
                if (aerialState == AerialState.HOVERING) {
                    canHover = false;
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
            turboDuration = Constants.MAX_HOVER_DURATION * ((float) startTurbo / Constants.MAX_TURBO);
            aerialState = AerialState.HOVERING; // indicates currently hovering
            hoverStartTime = TimeUtils.nanoTime(); // begins timing hover duration
        }
        hoverTimeSeconds = (Utils.secondsSince(hoverStartTime) - pauseDuration); // for comparing with max hover time
        turbo = (((turboDuration - hoverTimeSeconds)) / turboDuration * startTurbo);
        if (turbo >= 1) {
            velocity.y = 0; // disables impact of gravity
        } else {
            canHover = false;
            fall(); // when max hover time is exceeded
            pauseDuration = 0;
        }
    }

    private void enableRicochet() {
        if ((inputControls.jumpButtonJustPressed && canRicochet)
                || aerialState == AerialState.RICOCHETING) {
            ricochet();
        }
    }

    private void ricochet() {
        if (canRicochet) {
            aerialState = AerialState.RICOCHETING;
            ricochetStartTime = TimeUtils.nanoTime();
            canRicochet = false;
            hoverStartTime = 0;
            canJump = true;
        }
        ricochetTimeSeconds = (Utils.secondsSince(ricochetStartTime) - pauseDuration);
        if (ricochetTimeSeconds >= Constants.RICOCHET_FRAME_DURATION) {
            facing = Utils.getOppositeDirection(facing);
            velocity.x = Utils.absoluteToDirectionalValue(Constants.GIGAGAL_MAX_SPEED, facing, Orientation.LATERAL);
            jump();
            turbo = Math.max(turbo, Constants.GROUND_SLIDE_MIN_TURBO);
        } else {
            pauseDuration = 0;
            canChangeDirection = false;
            canHover = true;
        }
    }

    private void enableClimb() {
        if (onClimbable) {
            if (inputControls.jumpButtonPressed && aerialState != AerialState.RECOILING) {
                canHover = false;
                canClimb = true;
                if (inputControls.upButtonPressed || inputControls.downButtonPressed) {
                    velocity.x = 0;
                    if (climbDirection == null) {
                        velocity.y = 0;
                    }
                    if (lookDirection == null) {
                        climb();
                    }
                } else {
                    climbDirection = null;
                }
            } else {
                climbDirection = null;
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
        int climbAnimationPercent = (int) (climbTimeSeconds * 100);
        if ((climbAnimationPercent) % 25 >= 0
                && (climbAnimationPercent) % 25 <= 13) {
            facing = Direction.RIGHT;
        } else {
            facing = Direction.LEFT;
        }
        if (inputControls.upButtonPressed) {
            climbDirection = Direction.UP;
            velocity.y = Constants.CLIMB_SPEED;
        } if (inputControls.downButtonPressed) {
            climbDirection = Direction.DOWN;
            velocity.y = -Constants.CLIMB_SPEED;
        }
    }

    private void stand() {
        if (onSink) {
            velocity.y = -3;
        }
        if (onSkateable) {
            if (Math.abs(velocity.x) > 0.005f) {
                velocity.x /= 1.005;
            } else {
                velocity.x = 0;
            }
        } else {
            velocity.x = 0;
        }
        if (onTreadmill) {
            velocity.x += Utils.absoluteToDirectionalValue(Constants.TREADMILL_SPEED, treadDirection, Orientation.LATERAL);
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
        if (!onSkateable) {
            strideStartTime = 0;
        }
        aerialState = AerialState.FALLING;
        if (groundState != GroundState.AIRBORNE) {
            aerialTakeoff = position.x;
            groundState = GroundState.AIRBORNE;
        }
        canJump = false;
        canDash = false;
        canLook = true;
        if (canChangeDirection) {
            handleLateralInputs();
        }
        if (turbo < Constants.MAX_TURBO) {
            turbo += Constants.FALL_TURBO_INCREMENT;
        }
        if (Utils.movingOppositeDirectionFacing(velocity.x, facing) || onCoals) {
            canHover = false;
            recoil(velocity);
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion region = Assets.getInstance().getGigaGalAssets().standRight;
        if (climbDirection != null
                || (canClimb && lookDirection == null && climbStartTime != 0)) {
            if (facing == Direction.LEFT) {
                region = Assets.getInstance().getGigaGalAssets().climb.getKeyFrame(0.12f);
            } else if (facing == Direction.RIGHT) {
                region = Assets.getInstance().getGigaGalAssets().climb.getKeyFrame(0.25f);
            }
        } else if (facing == Direction.RIGHT) {
            if (lookDirection == Direction.UP) {
                region = Assets.getInstance().getGigaGalAssets().lookupRight;
            } else if (lookDirection == Direction.DOWN) {
                region = Assets.getInstance().getGigaGalAssets().lookdownRight;
            } else if (aerialState != AerialState.GROUNDED) {
                if (aerialState == AerialState.HOVERING) {
                    hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
                    region = Assets.getInstance().getGigaGalAssets().hoverRight.getKeyFrame(hoverTimeSeconds);
                } else if (aerialState == AerialState.RICOCHETING) {
                    region = Assets.getInstance().getGigaGalAssets().ricochetLeft;
                } else if (aerialState == AerialState.RECOILING){
                    region = Assets.getInstance().getGigaGalAssets().recoilRight;
                } else {
                    region = Assets.getInstance().getGigaGalAssets().fallRight;
                }
            } else if (groundState == GroundState.STANDING) {
                region = Assets.getInstance().getGigaGalAssets().standRight;
            } else if (groundState == GroundState.STRIDING) {
                region = Assets.getInstance().getGigaGalAssets().strideRight.getKeyFrame(Math.min(strideAcceleration * strideAcceleration, strideAcceleration));
            } else if (groundState == GroundState.DASHING) {
                region = Assets.getInstance().getGigaGalAssets().dashRight;
            }
        } else if (facing == Direction.LEFT) {
            if (lookDirection == Direction.UP) {
                region = Assets.getInstance().getGigaGalAssets().lookupLeft;
            } else if (lookDirection == Direction.DOWN) {
                region = Assets.getInstance().getGigaGalAssets().lookdownLeft;
            } else if (aerialState != AerialState.GROUNDED) {
                if (aerialState == AerialState.HOVERING) {
                    hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
                    region = Assets.getInstance().getGigaGalAssets().hoverLeft.getKeyFrame(hoverTimeSeconds);
                } else if (aerialState == AerialState.RICOCHETING) {
                    region = Assets.getInstance().getGigaGalAssets().ricochetRight;
                } else if (aerialState == AerialState.RECOILING){
                    region = Assets.getInstance().getGigaGalAssets().recoilLeft;
                } else {
                    region = Assets.getInstance().getGigaGalAssets().fallLeft;
                }
            } else if (groundState == GroundState.STANDING) {
                region = Assets.getInstance().getGigaGalAssets().standLeft;
            } else if (groundState == GroundState.STRIDING) {
                region = Assets.getInstance().getGigaGalAssets().strideLeft.getKeyFrame(Math.min(strideAcceleration * strideAcceleration, strideAcceleration));
            } else if (groundState == GroundState.DASHING ) {
                region = Assets.getInstance().getGigaGalAssets().dashLeft;
            }
        }
        Utils.drawTextureRegion(batch, region, position, Constants.GIGAGAL_EYE_POSITION);
    }

    // Getters
    public int getAmmo() { return ammo; }
    public int getHealth() { return health; }
    public int getLives() { return lives; }
    public float getTurbo() { return turbo; }
    public Direction getFacing() { return facing; }
    public Vector2 getPosition() { return position; }
    public float getWidth() { return Constants.GIGAGAL_STANCE_WIDTH; }
    public float getHeight() { return Constants.GIGAGAL_HEIGHT; }
    public float getLeft() { return position.x - (Constants.GIGAGAL_STANCE_WIDTH / 2); }
    public float getRight() { return position.x + (Constants.GIGAGAL_STANCE_WIDTH / 2); }
    public float getTop() { return position.y + Constants.GIGAGAL_HEAD_RADIUS; }
    public float getBottom() { return position.y - Constants.GIGAGAL_EYE_HEIGHT; }
    public Rectangle getBounds() { return  new Rectangle(getLeft(), getBottom(), getWidth(), getHeight()); }
    public boolean getJumpStatus() { return canJump; }
    public boolean getHoverStatus() { return canHover; }
    public boolean getRicochetStatus() { return canRicochet; }
    public boolean getDashStatus() { return canDash; }
    public boolean getClimbStatus() { return canClimb; }
    public boolean getChargeStatus() { return canCharge; }
    public AmmoIntensity getAmmoIntensity() { return ammoIntensity; }
    public WeaponType getWeapon() { return weapon; }
    public List<WeaponType> getWeaponList() { return weaponList; }
    public float getPauseDuration() { return pauseDuration; }
    public boolean getPauseState() { return pauseState; }
    public Vector3 getChaseCamPosition() { return chaseCamPosition; }
    public long getLookStartTime() { return lookStartTime; }
    public float getChargeTimeSeconds() { return chargeTimeSeconds; }
    public Direction getClimbDirection() { return climbDirection; }
    public AerialState getAerialState() { return aerialState; }
    public GroundState getGroundState() { return groundState; }

    // Setters
    public void setFacing(Direction facing) { this.facing = facing; }
    public void setLives(int lives) { this.lives = lives; }
    public void setHealth(int health) { this.health = health; }
    public void setPauseDuration(float pauseDuration) { this.pauseDuration = pauseDuration; }
    public void setInputControls(InputControls inputControls) { this.inputControls = inputControls; }

    public void addWeapon(WeaponType weapon) { weaponToggler.add(weapon); }
}
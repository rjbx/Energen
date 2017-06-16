package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.app.SaveData;
import com.udacity.gamedev.gigagal.util.InputControls;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.*;
import com.udacity.gamedev.gigagal.util.Helpers;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

// mutable
public class GigaGal implements Humanoid {

    // fields
    public final static String TAG = GigaGal.class.getName();
    public static final GigaGal INSTANCE = new GigaGal();

    private LevelUpdater level;
    private float width;
    private float height;
    private float headRadius;
    private float eyeHeight;
    private float halfWidth;
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
    private Direction directionX;
    private Direction directionY;
    private TextureRegion region; // class-level instantiation
    private Action action;
    private GroundState groundState;
    private Ground touchedGround; // class-level instantiation
    private ShotIntensity shotIntensity;
    private Material weapon;
    private List<Material> weaponList; // class-level instantiation
    private ListIterator<Material> weaponToggler; // class-level instantiation
    private boolean canShoot;
    private boolean canLook;
    private boolean canPeer;
    private boolean canDash;
    private boolean canJump;
    private boolean canHover;
    private boolean canRappel;
    private boolean canClimb;
    private boolean canCling;
    private boolean canStride;
    private boolean canSink;
    private boolean canHurdle;
    private long chargeStartTime;
    private long standStartTime;
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
    private float dashTimeSeconds;
    private float hoverTimeSeconds;
    private float strideTimeSeconds;
    private float strideSpeed;
    private float strideAcceleration;
    private float turboDuration;
    private float turboMultiplier;
    private float ammoMultiplier;
    private float healthMultiplier;
    private float chargeModifier;
    private float startTurbo;
    private float turbo;
    private float killPlane;
    private float ammo;
    private float health;
    private int lives;
    private InputControls inputControls;

    // ctor
    private GigaGal() {}

    public static GigaGal getInstance() { return INSTANCE; }

    public void create() {
        position = new Vector2();
        spawnPosition = new Vector2();
        previousFramePosition = new Vector2();
        chaseCamPosition = new Vector3();
        velocity = new Vector2();
        weaponList = new ArrayList<Material>();
        weaponToggler = weaponList.listIterator();
        height = Constants.GIGAGAL_HEIGHT;
        eyeHeight = Constants.GIGAGAL_EYE_HEIGHT;
        headRadius = Constants.GIGAGAL_HEAD_RADIUS;
        width = Constants.GIGAGAL_STANCE_WIDTH;
        halfWidth = width / 2;
        lives = Constants.INITIAL_LIVES;
        killPlane = -10000;
        turboDuration = 1;
        turboMultiplier = SaveData.getTurboMultiplier();
        ammoMultiplier = SaveData.getAmmoMultiplier();
        healthMultiplier = SaveData.getHealthMultiplier();
        chargeModifier = 0;
        String savedWeapons = SaveData.getWeapons();
        if (!savedWeapons.equals(Material.NATIVE.name())) {
            List<String> savedWeaponsList = Arrays.asList(savedWeapons.split(", "));
            for (String weaponString : savedWeaponsList) {
                addWeapon(Material.valueOf(weaponString));
            }
            weapon = weaponToggler.previous();
        } else {
            addWeapon(Material.NATIVE);
            weapon = weaponToggler.previous();
        }
    }

    public void respawn() {
        position.set(spawnPosition);
        killPlane = position.y + Constants.KILL_PLANE;
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
        shotIntensity = ShotIntensity.NORMAL;
        startTurbo = turbo;
        turboDuration = 0;
        touchedGround = null;
        canClimb = false;
        canCling = false;
        canLook = false;
        canPeer = false;
        canStride = false;
        canJump = false;
        canDash = false;
        canHover = false;
        canRappel = false;
        canHurdle = false;
        canShoot = true;
        canSink = false;
        chargeStartTime = 0;
        strideStartTime = 0;
        climbStartTime = 0;
        jumpStartTime = 0;
        dashStartTime = 0;
        turboDuration = 0;
        standStartTime = TimeUtils.nanoTime();
        recoveryStartTime = TimeUtils.nanoTime();
    }

    public void update(float delta) {
        // positioning
        previousFramePosition.set(position);
        position.mulAdd(velocity, delta);
        setBounds();
        detectInput();

        // collision detection
        touchGround(level.getGrounds());
        touchHazards(level.getHazards());
        touchPowerups(level.getPowerups());

        // abilities
        if (groundState == GroundState.PLANTED) {
            velocity.y = 0;
            if (action == Action.STANDING) {
                stand();
                enableStride();
                enableDash();
                enableClimb(); // must come before jump (for now)
                enableJump();
                enableShoot(weapon);
            } else if (action == Action.STRIDING) {
                enableStride();
                enableDash();
                enableJump();
                enableShoot(weapon);
            } else if (action == Action.CLIMBING) {
                enableClimb();
                enableShoot(weapon);
            } else if (action == Action.DASHING) {
                enableDash();
                enableJump();
                enableShoot(weapon);
            }
        } else if (groundState == GroundState.AIRBORNE) {
            velocity.y -= Constants.GRAVITY;
            if (action == Action.FALLING) {
                fall();
                enableClimb();
                enableHover();
                enableRappel();
                enableShoot(weapon);
            } else if (action == Action.JUMPING) {
                enableJump();
                enableClimb();
                enableRappel();
                enableShoot(weapon);
            } else if (action == Action.HOVERING) {
                enableHover();
                enableRappel();
                enableClimb();
                enableShoot(weapon);
            } else if (action == Action.RAPPELLING) {
                enableJump();
                enableRappel();
                enableShoot(weapon);
            } else if (action == Action.RECOILING) {
                enableRappel();
                enableShoot(weapon);
            }
        }
    }

    public void updateMultipliers() {
        turboMultiplier = SaveData.getTurboMultiplier();
        ammoMultiplier = SaveData.getAmmoMultiplier();
        healthMultiplier = SaveData.getHealthMultiplier();
    }

    private void setBounds() {
        left = position.x - halfWidth;
        right = position.x + halfWidth;
        top = position.y + headRadius;
        bottom = position.y - eyeHeight;
        bounds = new Rectangle(left, bottom, width, height);
    }

    private void touchGround(DelayedRemovalArray<Ground> grounds) {
        for (Ground ground : grounds) {
            if (Helpers.overlapsPhysicalObject(this, ground)) {// if overlapping ground boundries

                if (ground.isDense()) { // for dense grounds: apply side, bottom collision and top collision

                    touchGroundBottom(ground);
                    touchGroundSide(ground);
                    touchGroundTop(ground);

                } else { // for non-dense grounds:

                    canRappel = false; // prevent from rappelling on non dense grounds
                    // additional ground collision instructions specific to certain types of grounds
                    if (ground instanceof Climbable) {
                        if (!(touchedGround instanceof Skateable && groundState == GroundState.PLANTED)) {  // prevents from overwriting saved skateable and overriding ground physics
                            touchedGround = ground; // saves for untouchground where condition within touchgroundtop unmet
                        }
                        if (!(canClimb && directionY == Direction.DOWN)) { // ignore side and bottom collision always and top collision when can climb and looking downward
                            touchGroundTop(ground); // prevents descending below top when on non dense, non sinkable
                        }
                        canCling = true;
                    } else if (ground instanceof Sinkable) {
                        setAtopGround(ground); // when any kind of collision detected and not only when breaking plane of ground.top
                        canCling = false;
                        canClimb = false;
                        canSink = true;
                        canDash = false;
                        canHover = false;
                        lookStartTime = 0;
                        lookTimeSeconds = 0;
                    } else {
                        canCling = false;
                        if (!(canClimb && directionY == Direction.DOWN)) { /// ignore side and bottom collision always and top collision when can climb and looking downward
                            touchGroundTop(ground); // prevents descending below top when on non dense, non sinkable
                        }
                    }

                }
                // if below minimum ground distance while descending excluding post-rappel, disable rappel and hover
                // caution when crossing plane between ground top and minimum hover height / ground distance
                // cannons, which inherit ground, can be mounted along sides of grounds causing accidental plane breakage
                if (getBottom() < (ground.getTop() + Constants.MIN_GROUND_DISTANCE)
                    && getBottom() > ground.getTop() // GG's bottom is greater than ground top but less than boundary
                    && velocity.y < 0 // prevents disabling features when crossing boundary while ascending on jump
                    && rappelStartTime == 0 // only if have not rappeled since last grounded
                    && !(ground instanceof Cannon)) { // only if ground is not instance of cannon
                    canRappel = false; // disables rappel
                    canHover = false; // disables hover
                }
            }
            untouchGround();
        }
    }

    private void touchGroundSide(Ground ground) {
        if (touchedGround == null || touchedGround == ground || touchedGround.getTop() != ground.getTop()) {
            // if during previous frame was not, while currently is, between ground left and right sides
            if (!Helpers.overlapsBetweenTwoSides(previousFramePosition.x, getHalfWidth(), ground.getLeft(), ground.getRight())) {
                // only when not grounded and not recoiling
                if (groundState != GroundState.PLANTED) {
                    // if x velocity (magnitude, without concern for direction) greater than one third max speed,
                    // boost x velocity by starting speed, enable rappel, verify rappelling ground and capture rappelling ground boundaries
                    if (Math.abs(velocity.x) > Constants.GIGAGAL_MAX_SPEED / 4) {
                        // if already rappelling, halt x progression
                        if (action != Action.RAPPELLING) {
                            if (ground instanceof Rappelable) {
                                canRappel = true; // enable rappel
                            }
                            touchedGround = ground;
                            killPlane = touchedGround.getBottom() + Constants.KILL_PLANE;
                        }
                        // if absval x velocity not greater than one fourth max speed but aerial and bumping ground side, fall
                    } else {
                        // if not already hovering and descending, also disable hover
                        if (action != Action.HOVERING && velocity.y < 0) {
                            canHover = false; // disable hover
                        }
                        canRappel = false;
                        fall(); // fall regardless of whether or not inner condition met
                    }
                    // only when planted
                } else if (groundState == GroundState.PLANTED) {
                    if (Math.abs(getBottom() - ground.getTop()) > 1) {
                        strideSpeed = 0;
                        velocity.x = 0;
                    }
                    if (action == Action.DASHING) {
                        stand(); // deactivates dash when bumping ground side
                    }
                    if (ground instanceof Chargeable) {
                        Chargeable chargeable = (Chargeable) ground;
                        if (chargeable instanceof Tripchamber) {
                            if (shotIntensity == ShotIntensity.BLAST && !chargeable.isCharged()) {
                                chargeable.charge();
                            }
                        } else {
                            if (chargeStartTime != 0 && directionX == Direction.RIGHT) {
                                if (!chargeable.isActive() && chargeable instanceof Chamber) {
                                    chargeable.setState(true);
                                } else if (chargeTimeSeconds > 1) {
                                    chargeable.setChargeTime(chargeTimeSeconds);
                                }
                            } else {
                                chargeable.setChargeTime(0);
                            }
                        }
                    }
                }
                if ((!(ground instanceof Rideable && (Math.abs(getBottom() - ground.getTop()) <= 1)))
                        && !(ground instanceof Skateable && (Math.abs(getBottom() - ground.getTop()) <= 1))
                        && !(ground instanceof Unbearable && (Math.abs(getBottom() - ground.getTop()) <= 1))) {
                    // if contact with ground sides detected without concern for ground state (either grounded or airborne),
                    // reset stride acceleration, disable stride and dash, and set gigagal at ground side
                    if (action != Action.STRIDING || action != Action.DASHING) {
                        strideStartTime = 0; // reset stride acceleration
                    }
                    canStride = false; // disable stride
                    canDash = false; // disable dash
                    position.x = previousFramePosition.x;
                }
            // reset position to ground side edge when both position and previous position overlap ground side edge and are between ground top and bottom (to prevent resetting to grounds simultaneously planted upon)
            } else if (Helpers.betweenTwoValues(position.y, ground.getBottom(), ground.getTop())){
                if (Math.abs(position.x - ground.getLeft()) < Math.abs(position.x - ground.getRight())) {
                    position.x = ground.getLeft() - getHalfWidth() - 1;
                } else {
                    position.x = ground.getRight() + getHalfWidth() + 1;
                }
            }
        }
    }

    private void touchGroundBottom(Ground ground) {
        // if contact with ground bottom detected, halts upward progression and set gigagal at ground bottom
        if ((previousFramePosition.y + Constants.GIGAGAL_HEAD_RADIUS) < ground.getBottom()) {
            velocity.y = 0; // prevents from ascending above ground bottom
            position.y = previousFramePosition.y;  // sets gigagal at ground bottom
            fall(); // descend from point of contact with ground bottom
        }
    }

    // applicable to all dense grounds as well as non-sinkables when not climbing downward
    private void touchGroundTop(Ground ground) {
        // if contact with ground top detected, halt downward progression and set gigagal atop ground
        if (getTop() > ground.getTop() && !canRappel || (touchedGround != null && ground.getTop() != touchedGround.getTop())) {
            if (previousFramePosition.y - Constants.GIGAGAL_EYE_HEIGHT >= ground.getTop() - 1) { // and not simultaneously touching two different grounds (prevents stand which interrupts striding atop)

                velocity.y = 0; // prevents from descending beneath ground top
                position.y = ground.getTop() + Constants.GIGAGAL_EYE_HEIGHT; // sets Gigagal atop ground

                setAtopGround(ground); // basic ground top collision instructions common to all types of grounds

                // additional ground top collision instructions specific to certain types of grounds
                if (ground instanceof Skateable) {
                    if (groundState == GroundState.AIRBORNE) {
                        stand(); // set groundstate to standing
                        lookStartTime = 0;
                    } else if (canClimb) {
                        canCling = false;
                    }
                } else if (ground instanceof Hoverable) {
                    lookStartTime = 0;
                    Hoverable hoverable = (Hoverable) ground;
                    Orientation orientation = hoverable.getOrientation();
                    Direction direction = hoverable.getDirection();
                    if (orientation == Orientation.X) {
                        velocity.x = hoverable.getVelocity().x;
                        position.x += velocity.x;
                    }
                    if (direction == Direction.DOWN) {
                        position.y -= 1;
                    }
                } else if (ground instanceof Reboundable) {
                    canClimb = false;
                    canCling = false;
                    Reboundable reboundable = (Reboundable) ground;
                    reboundable.setState(true);
                } else if (ground instanceof Unbearable) {
                    canHover = false;
                    Random xKnockback = new Random();
                    velocity.set(Helpers.absoluteToDirectionalValue(xKnockback.nextFloat() * 200, directionX, Orientation.X), Constants.PROTRUSION_GAS_KNOCKBACK.y);
                    recoil(velocity);
                } else if (ground instanceof Destructible) {
                    if (((Box) ground).getHealth() < 1) {
                        fall();
                    }
                }
            // reset when previous position is also below ground top
            } else if (ground.isDense() && Helpers.betweenTwoValues(position.x, ground.getLeft(), ground.getRight())) {
                position.y = ground.getTop() + Constants.GIGAGAL_EYE_HEIGHT + 1;
                setAtopGround(ground);
                stand();
            }
        }
    }

    // basic ground top collision instructions; applicable to sinkables even when previousframe.x < ground.top
    private void setAtopGround(Ground ground) {
        touchedGround = ground;
        killPlane = touchedGround.getBottom() + Constants.KILL_PLANE;
        hoverStartTime = 0;
        rappelStartTime = 0;
        canRappel = false;
        canLook = true;
        canHover = false;
        if (groundState == GroundState.AIRBORNE && !(ground instanceof Skateable)) {
            stand(); // in each frame all grounds save for skateable rely upon this call to switch action from airborne
            lookStartTime = 0;
        }
        if (canClimb && !inputControls.jumpButtonPressed && action == Action.STANDING) {
            canJump = true;
            jump();
        } else if (action == Action.CLIMBING && !(ground instanceof Climbable)) {
            stand();
        }
    }

    private void untouchGround() {
        if (touchedGround != null) {
            if (!Helpers.overlapsPhysicalObject(this, touchedGround)) {
                if (touchedGround instanceof Reboundable) {
                    Reboundable reboundable = (Reboundable) touchedGround;
                    if (reboundable.getState() && !(reboundable instanceof Tripknob)) {
                        reboundable.resetStartTime();
                        reboundable.setState(false);
                    }
                }
                if (getBottom() > touchedGround.getTop() || getTop() < touchedGround.getBottom())
                /*(!Helpers.overlapsBetweenTwoSides(position.y, (getTop() - getBottom()) / 2, touchedGround.getBottom(), touchedGround.getTop()) */ {
                    if (touchedGround instanceof Reboundable) {
                        Reboundable reboundable = (Reboundable) touchedGround;
                        reboundable.resetStartTime();
                        reboundable.setState(false);
                    }
                    if (action == Action.RAPPELLING) {
                        velocity.x = 0; // prevents falling with backward momentum after rappel-sliding down platform side through its bottom
                    }
                    canRappel = false;
                    fall();
                } else if (!Helpers.overlapsBetweenTwoSides(position.x, getHalfWidth(), touchedGround.getLeft(), touchedGround.getRight())) {
                    canSink = false;
                    lookTimeSeconds = 0;
                    lookStartTime = 0;
                    if (action != Action.RAPPELLING && action != Action.CLIMBING) {
                        fall();
                    }
                    canCling = false;
                    canClimb = false;
                } else if (touchedGround instanceof Destructible) {
                    Destructible destructible = (Destructible) touchedGround;
                    if (destructible.getHealth() < 1) {
                        fall();
                    }
                }
                // when no collision detected
                canRappel = false;
                touchedGround = null;  // after handling touchedground conditions above
            }
        } else if (action == Action.STANDING) { // if no ground detected and suspended midair (prevents climb after crossing climbable plane)
            fall();
        }
    }

    // detects contact with enemy (change aerial & ground state to recoil until grounded)
    private void touchHazards(DelayedRemovalArray<Hazard> hazards) {
        for (Hazard hazard : hazards) {
            if (!(hazard instanceof Ammo && ((Ammo) hazard).isFromGigagal())) {
                float recoveryTimeSeconds = Helpers.secondsSince(recoveryStartTime);
                if (action != Action.RECOILING && recoveryTimeSeconds > Constants.RECOVERY_TIME) {
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
                                recoil(new Vector2((Helpers.absoluteToDirectionalValue(zoomba.getMountKnockback().x, directionX, Orientation.X)), zoomba.getMountKnockback().y));
                                damage = zoomba.getMountDamage();
                            } else {
                                recoil(new Vector2((Helpers.absoluteToDirectionalValue(hazard.getKnockback().x, directionX, Orientation.X)), hazard.getKnockback().y));
                            }
                        }
                        Assets.getInstance().getSoundAssets().damage.play();
                        health -= damage * healthMultiplier;
                        chargeModifier = 0;
                    } else if (
                            action == Action.STANDING
                            && position.dst(bounds.getCenter(new Vector2())) < Constants.WORLD_SIZE
                            && Helpers.absoluteToDirectionalValue(position.x - bounds.x, directionX, Orientation.X) > 0) {
                        canPeer = true;
                    } else if (canPeer && position.dst(bounds.getCenter(new Vector2())) < Constants.WORLD_SIZE / 2) {
                        canPeer = false;
                    }
                }
            }
        }
    }

    private void touchPowerups(DelayedRemovalArray<Powerup> powerups) {
        for (Powerup powerup : powerups) {
            Rectangle bounds = new Rectangle(powerup.getLeft(), powerup.getBottom(), powerup.getWidth(), powerup.getHeight());
            if (getBounds().overlaps(bounds)) {
                switch(powerup.getType()) {
                    case AMMO:
                        Assets.getInstance().getSoundAssets().ammo.play();
                        ammo += Constants.POWERUP_AMMO;
                        if (ammo > Constants.MAX_AMMO) {
                            ammo = Constants.MAX_AMMO;
                        }
                        break;
                    case HEALTH:
                        Assets.getInstance().getSoundAssets().health.play();
                        health += Constants.POWERUP_HEALTH;
                        if (health > Constants.MAX_HEALTH) {
                            health = Constants.MAX_HEALTH;
                        }
                        break;
                    case TURBO:
                        Assets.getInstance().getSoundAssets().turbo.play();
                        turbo += Constants.POWERUP_TURBO;
                        if (action == Action.HOVERING) {
                            hoverStartTime = TimeUtils.nanoTime();
                        }
                        if (action == Action.DASHING) {
                            dashStartTime = TimeUtils.nanoTime();
                        }
                        break;
                    case LIFE:
                        Assets.getInstance().getSoundAssets().life.play();
                        lives += 1;
                        break;
                    case CANNON:
                        Assets.getInstance().getSoundAssets().cannon.play();
                        chargeModifier = 1;
                        ammo += Constants.POWERUP_AMMO;
                        break;
                }
                powerup.deactivate();
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
        boolean inputtingX = ((left || right) && !(left && right));
        if (inputtingX) {
            if (left && !right) {
                directionChanged = Helpers.changeDirection(this, Direction.LEFT, Orientation.X);
            } else if (!left && right) {
                directionChanged = Helpers.changeDirection(this, Direction.RIGHT, Orientation.X);
            }
        }
        if (groundState != GroundState.AIRBORNE && action != Action.CLIMBING) {
            if (lookStartTime == 0) {
                if (directionChanged) {
                    if (action == Action.DASHING) {
                        dashStartTime = 0;
                        canDash = false;
                    }
                    strideSpeed = velocity.x;
                    strideStartTime = 0;
                    stand();
                } else if (action != Action.DASHING) {
                    if (inputtingX) {
                        if (!canStride) {
                            if (strideStartTime == 0) {
                                canStride = true;
                            } else if (Helpers.secondsSince(strideStartTime) > Constants.DOUBLE_TAP_SPEED) {
                                strideStartTime = 0;
                            } else if (!canSink){
                                canDash = true;
                            } else {
                                canDash = false;
                            }
                        }
                    } else {
                        stand();
                        canStride = false;
                    }
                }
            }
        } else if (groundState == GroundState.AIRBORNE) {
            if (directionChanged) {
                if (action != Action.HOVERING) {
                    velocity.x /= 2;
                } else {
                    velocity.x /= 4;
                }
            }
        } else if (action == Action.CLIMBING) {
            if (canClimb) {
                if (inputtingX) {
                    velocity.y = 0;
                    canHover = false;
                    if (inputControls.jumpButtonPressed) {
                        climb(Orientation.X);
                    }
                } else {
                    velocity.x = 0; // disable movement when climbing but directional not pressed
                }
            } else {
                velocity.x = 0; // disable movement when climbing but jumpbutton not pressed
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
                directionChanged = Helpers.changeDirection(this, Direction.DOWN, Orientation.Y);
            } else if (!down && up) {
                directionChanged = Helpers.changeDirection(this, Direction.UP, Orientation.Y);
            }
            if (directionY == Direction.DOWN) {
                if (canSink) {
                    velocity.y *= 5;
                }
            }
            if (canLook && !canClimb) {
                canStride = false;
                if (inputControls.jumpButtonJustPressed && !canRappel && !canHurdle) { // prevents accidental toggle due to simultaneous jump and directional press for hurdle
                    toggleWeapon(directionY);
                }
                look(); // also sets chase cam
            }
        } else if (action == Action.STANDING || action == Action.CLIMBING) { // if neither up nor down pressed (and either standing or climbing)
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
                        if (dashTimeSeconds == 0) {  // if directional released
                            if (!directionChanged) { // if tapping in same direction
                                // if difference between current time and previous tap start time is less than double tap speed
                                if (((TimeUtils.nanoTime() - dashStartTime) * MathUtils.nanoToSec) < Constants.DOUBLE_TAP_SPEED) {
                                    if (directionY == Direction.UP) { // enable increased ascension speed
                                        canDash = true; // checks can dash after calling climb() to apply speed boost
                                    } else if (directionY == Direction.DOWN) { // drop down from climbable (drop handled from climb())
                                        lookStartTime = TimeUtils.nanoTime(); // prevents from reengaging climbable from enableclimb() while falling
                                        canCling = false; // meets requirement within climb() to disable climb and enable fall
                                    }
                                }
                                dashStartTime = TimeUtils.nanoTime(); // replace climb start time with that of most recent tap
                            }
                        }
                        if (touchedGround instanceof Climbable) {
                            if (position.x < touchedGround.getLeft()) {
                                position.x = touchedGround.getLeft();
                            } else if (position.x > touchedGround.getRight()) {
                                position.x = touchedGround.getRight();
                            }
                        }
                        climb(Orientation.Y);
                        if (canDash) { // apply multiplier on top of speed set by climb()
                            velocity.y *= 2; // double speed
                        }
                    } else {
                        velocity.y = 0; // disable movement when climbing but jump button not pressed
                    }
                }
            } else {
                dashTimeSeconds = 0; // indicates release of directional for enabling double tap
                canDash = false; // reset dash when direction released
            }
        }
    }

    private void stand() {
        if (touchedGround instanceof Sinkable) {
            strideStartTime = 0;
            strideTimeSeconds = 0;
            strideAcceleration = 0;
            velocity.x = 0;
            velocity.y = -3;
        } else if (touchedGround instanceof Skateable) {
            if (Math.abs(velocity.x) > 0.005f) {
                velocity.x /= 1.005;
            } else {
                velocity.x = 0;
            }
        } else if (touchedGround instanceof Rideable) {
            velocity.x = 0;
            velocity.x += Helpers.absoluteToDirectionalValue(Constants.TREADMILL_SPEED, ((Rideable) touchedGround).getDirection(), Orientation.X);
        } else {
            velocity.x = 0;
        }

        action = Action.STANDING;
        groundState = GroundState.PLANTED;

        if (!canClimb) {
            canJump = true;
            handleYInputs(); // disabled when canclimb to prevent look from overriding climb
        } else {
            canJump = false;
        }

        if (turbo < Constants.MAX_TURBO) {
            turbo += Constants.STAND_TURBO_INCREMENT;
        }
    }

    private void fall() {
        handleXInputs();
        handleYInputs();
        action = Action.FALLING;
        groundState = GroundState.AIRBORNE;
        canJump = false;
        canDash = false;
        canLook = true;
        if (!(touchedGround instanceof Skateable)) {
            strideStartTime = 0;
        }

        // deactivates rappel and climb to prevent inappropriate activation when holding jumpbutton, crossing and no longer overlapping climbable plane
        if (touchedGround == null && canClimb) {
            canCling = false;
        }

        if (touchedGround instanceof Sinkable && getBottom() < touchedGround.getTop()) {
            canHover = false; // prevents hover icon flashing from indicator hud when tapping jump while submerged in sink
        } else if (!canRappel) {
            touchedGround = null;
            canHover = true;
        }

        canSink = false;

        if (turbo < Constants.MAX_TURBO) {
            turbo += Constants.FALL_TURBO_INCREMENT;
        }
    }

    // disables all else by virtue of neither top level update conditions being satisfied due to state
    private void recoil(Vector2 velocity) {
        action = Action.RECOILING;
        groundState = GroundState.AIRBORNE;
        shotIntensity = ShotIntensity.NORMAL;
        chargeStartTime = 0;
        strideStartTime = 0;
        lookStartTime = 0;
        turbo = 0;
        canStride = false;
        canDash = false;
        canHover = false;
        canLook = false;
        canCling = false;
        canClimb = false;
        canRappel = false;
        canHurdle = false;
        this.velocity.x = velocity.x;
        this.velocity.y = velocity.y;
    }

    private void enableShoot(Material weapon) {
        if (canShoot) {
            if (inputControls.shootButtonPressed || (action == Action.RAPPELLING && (inputControls.rightButtonPressed || inputControls.leftButtonPressed))) {
                if (chargeStartTime == 0) {
                    chargeStartTime = TimeUtils.nanoTime();
                } else if (chargeTimeSeconds > Constants.CHARGE_DURATION) {
                    shotIntensity = ShotIntensity.BLAST;
                } else if (chargeTimeSeconds > Constants.CHARGE_DURATION / 3) {
                    shotIntensity = ShotIntensity.CHARGED;
                }
                chargeTimeSeconds = Helpers.secondsSince(chargeStartTime) + chargeModifier;
            } else if (chargeStartTime != 0) {
                int ammoUsed;

                if (weapon == Material.NATIVE
                        || (ammo < Constants.BLAST_AMMO_CONSUMPTION && shotIntensity == ShotIntensity.BLAST)
                        || ammo < Constants.SHOT_AMMO_CONSUMPTION) {
                    ammoUsed = 0;
                    weapon = Material.NATIVE;
                } else {
                    ammoUsed = Helpers.useAmmo(shotIntensity);
                }

                shoot(shotIntensity, weapon, ammoUsed);
                chargeStartTime = 0;
                chargeTimeSeconds = 0;
                this.shotIntensity = ShotIntensity.NORMAL;
            }
        }
    }

    public void shoot(ShotIntensity shotIntensity, Material weapon, int ammoUsed) {
        if (shotIntensity == ShotIntensity.BLAST) {
            Assets.getInstance().getSoundAssets().getMaterialSound(weapon).play();
        } else {
            Assets.getInstance().getSoundAssets().getMaterialSound(weapon).play(1, 2, 0);
        }
        ammo -= ammoUsed * ammoMultiplier;
        if (lookStartTime != 0) {
            if (directionY == Direction.UP) {
                level.spawnAmmo(new Vector2(position.x + Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_Y_CANNON_OFFSET.x, directionX, Orientation.X), position.y + Constants.GIGAGAL_Y_CANNON_OFFSET.y), directionY, Orientation.Y, shotIntensity, weapon, true);
            } else {
                level.spawnAmmo(new Vector2(position.x + Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_Y_CANNON_OFFSET.x - 3, directionX, Orientation.X), position.y - Constants.GIGAGAL_Y_CANNON_OFFSET.y - 8), directionY, Orientation.Y, shotIntensity, weapon, true);
            }
        } else {
            level.spawnAmmo(new Vector2(position.x + Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_X_CANNON_OFFSET.x, directionX, Orientation.X), position.y + Constants.GIGAGAL_X_CANNON_OFFSET.y), directionX, Orientation.X, shotIntensity, weapon, true);
        }
    }

    private void look() {
        float offset = 0;
        if (lookStartTime == 0) {
            lookStartTime = TimeUtils.nanoTime();
            chaseCamPosition.set(position, 0);
        } else if (action == Action.STANDING || action == Action.CLIMBING) {
            setChaseCamPosition(offset);
        }
    }

    private void enableStride() {
        handleXInputs();
        if (canStride) {
            stride();
        }
    }

    private void stride() {
        action = Action.STRIDING;
        groundState = GroundState.PLANTED;
        if (turbo < Constants.MAX_TURBO) {
            turbo += Constants.STRIDE_TURBO_INCREMENT;
        }
        canLook = false;
        if (strideStartTime == 0) {
            strideSpeed = velocity.x;
            action = Action.STRIDING;
            groundState = GroundState.PLANTED;
            strideStartTime = TimeUtils.nanoTime();
        }
        strideTimeSeconds = Helpers.secondsSince(strideStartTime);
        strideAcceleration = strideTimeSeconds + Constants.GIGAGAL_STARTING_SPEED;
        velocity.x = Helpers.absoluteToDirectionalValue(Math.min(Constants.GIGAGAL_MAX_SPEED * strideAcceleration + Constants.GIGAGAL_STARTING_SPEED, Constants.GIGAGAL_MAX_SPEED), directionX, Orientation.X);
        if (touchedGround instanceof Rideable) {
            velocity.x += Helpers.absoluteToDirectionalValue(Constants.TREADMILL_SPEED, ((Rideable) touchedGround).getDirection(), Orientation.X);
        } else if (touchedGround instanceof Skateable) {
            velocity.x = strideSpeed + Helpers.absoluteToDirectionalValue(Math.min(Constants.GIGAGAL_MAX_SPEED * strideAcceleration / 2 + Constants.GIGAGAL_STARTING_SPEED, Constants.GIGAGAL_MAX_SPEED * 2), directionX, Orientation.X);
        } else if (canSink) {
            velocity.x = Helpers.absoluteToDirectionalValue(10, directionX, Orientation.X);
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
        if (action != Action.DASHING) {
            startTurbo = turbo;
            turboDuration = Constants.MAX_DASH_DURATION * (startTurbo / Constants.MAX_TURBO);
            action = Action.DASHING;
            groundState = GroundState.PLANTED;
            dashStartTime = TimeUtils.nanoTime();
            strideStartTime = 0;
            canStride = false;
            if (touchedGround instanceof Triptread) {
                Triptread triptread = (Triptread) touchedGround;
                triptread.setState(!triptread.isActive());
            }
        }
        float dashSpeed = Constants.GIGAGAL_MAX_SPEED;
        if (turbo >= 1) {
            turbo -= Constants.FALL_TURBO_INCREMENT * Constants.DASH_TURBO_MULTIPLIER * turboMultiplier;
            velocity.x = Helpers.absoluteToDirectionalValue(dashSpeed, directionX, Orientation.X);
        } else {
            canDash = false;
            dashStartTime = 0;
            stand();
        }
        if (touchedGround instanceof Skateable
        || (touchedGround instanceof Rideable && directionX == ((Rideable) touchedGround).getDirection())) {
            velocity.x = Helpers.absoluteToDirectionalValue(dashSpeed + Constants.TREADMILL_SPEED, directionX, Orientation.X);
        }
    }

    private void enableJump() {
        if (canJump) {
            if ((inputControls.jumpButtonJustPressed && action != Action.JUMPING)
                    && lookStartTime == 0) {
                jump();
            }
        }
    }

    private void jump() {
        if (canJump) {
            action = Action.JUMPING;
            groundState = GroundState.AIRBORNE;
            jumpStartTime = TimeUtils.nanoTime();
            canJump = false;
        }
        velocity.x += Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_STARTING_SPEED * Constants.STRIDING_JUMP_MULTIPLIER, directionX, Orientation.X);
        velocity.y = Constants.JUMP_SPEED;
        velocity.y *= Constants.STRIDING_JUMP_MULTIPLIER;
        if (touchedGround instanceof Reboundable) {
            velocity.y *= 2;
        } else {
            fall(); // causes fall texture to render for one frame
        }
    }

    private void enableHover() {
        if (canHover) {
            if (!(inputControls.upButtonPressed || inputControls.downButtonPressed)  // prevents from deactivating hover when toggling weapon
            && inputControls.jumpButtonJustPressed) {
                if (action == Action.HOVERING) {
                    //   canHover = false;
                    hoverStartTime = 0;
                    velocity.x -= velocity.x / 2;
                    fall();
                } else {
                    hover(); // else hover if canHover is true (set to false after beginning hover)
                }
                // if jump key not pressed, but already hovering, continue to hover
            } else if (action == Action.HOVERING) {
                handleYInputs();
                hover();
            }
        }
    }

    private void hover() {
        // canHover can only be true just before beginning to hover
        if (action != Action.HOVERING) {
            canClimb = false;
            canCling = false;
            startTurbo = turbo;
            turboDuration = Constants.MAX_HOVER_DURATION * (startTurbo / Constants.MAX_TURBO);
            action = Action.HOVERING; // indicates currently hovering
            groundState = GroundState.AIRBORNE;
            hoverStartTime = TimeUtils.nanoTime(); // begins timing hover duration
        }
        hoverTimeSeconds = Helpers.secondsSince(hoverStartTime); // for comparing with max hover time
        if (turbo >= 1) {
            velocity.y = 0; // disables impact of gravity
            turbo -= Constants.FALL_TURBO_INCREMENT * turboMultiplier;
        } else {
            canHover = false;
            fall(); // when max hover time is exceeded
        }
        handleXInputs();
    }

    private void enableRappel() {
        if (action == Action.RAPPELLING) {
            rappel();
        } else if (canRappel){
            if (!canHover || action == Action.HOVERING) {
                fall(); // begin descent from ground side sans access to hover
                canHover = false; // disable hover if not already
            }
            if (inputControls.jumpButtonJustPressed) {
                if (position.y > touchedGround.getTop() - 10) {
                    position.y = touchedGround.getTop() - 10;
                    if (touchedGround instanceof Hurdleable) {
                        canHurdle = true;
                    }
                }
                rappel();
            }
        }
    }

    private void rappel() {
        if (canRappel) {
            action = Action.RAPPELLING;
            groundState = GroundState.AIRBORNE;
            startTurbo = turbo;
            rappelStartTime = TimeUtils.nanoTime();
            turboDuration = Constants.MAX_RAPPEL_DURATION * (startTurbo / Constants.MAX_TURBO);
            if (!Helpers.movingOppositeDirection(velocity.x, directionX, Orientation.X)) {
                directionX = Helpers.getOppositeDirection(directionX);
            }
            hoverStartTime = 0;
            canJump = true;
            canRappel = false;
        }
        if (directionX == Direction.LEFT) {
            position.x = touchedGround.getLeft() - getHalfWidth();
        } else {
            position.x = touchedGround.getRight() + getHalfWidth();
        }
        float rappelTimeSeconds = Helpers.secondsSince(rappelStartTime);
        if (!inputControls.jumpButtonPressed) {
            if (rappelTimeSeconds >= Constants.RAPPEL_FRAME_DURATION) {
                velocity.x = Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_MAX_SPEED, directionX, Orientation.X);
                jump();
            } else {
                canHover = true;
            }
            canHurdle = false;
        } else {
            lookStartTime = 0;
            if (inputControls.downButtonPressed) {
                velocity.y += Constants.RAPPEL_GRAVITY_OFFSET;
            } else if (inputControls.upButtonPressed && canHurdle) {
                canHurdle = false;
                canRappel = false;
                directionX = Helpers.getOppositeDirection(directionX);
                velocity.x = Helpers.absoluteToDirectionalValue(Constants.CLIMB_SPEED, directionX, Orientation.X);
                jump();
            } else if (turbo < 1) {
                turbo = 0;
                velocity.y += Constants.RAPPEL_GRAVITY_OFFSET;
            } else {
                turbo -= Constants.FALL_TURBO_INCREMENT * turboMultiplier;
                if (touchedGround instanceof Treadmill) {
                    turbo -= 2;
                }
                velocity.y = 0;
            }
        }
    }

    private void enableClimb() {
        if (canCling) {
            // when overlapping all but top, set canrappel which if action enablesclimb will set canclimb to true
            if (inputControls.jumpButtonPressed) {
                if (lookStartTime == 0) { // cannot initiate climb if already looking; must first neutralize
                    canLook = false; // prevents look from overriding climb
                    canClimb = true; // enables climb handling from handleY()
                }
            } else {
                canClimb = false;
                canLook = true; // enables look when engaging climbable but not actively climbing
            }
            handleXInputs(); // enables change of x direction for shooting left or right
            handleYInputs(); // enables change of y direction for looking and climbing up or down
        } else {
            if (action == Action.CLIMBING) {
                fall();
                if (!(touchedGround instanceof Climbable && Helpers.overlapsBetweenTwoSides(position.x, getHalfWidth(), touchedGround.getLeft(), touchedGround.getRight())))  {
                    velocity.x = Helpers.absoluteToDirectionalValue(Constants.CLIMB_SPEED, directionX, Orientation.X);
                }
            }
            canClimb = false;
        }
    }

    private void climb(Orientation orientation) {
        if (canCling) { // canrappel set to false from handleYinputs() if double tapping down
            if (action != Action.CLIMBING) { // at the time of climb initiation
                climbStartTime = 0; // overrides assignment of current time preventing nanotime - climbstarttime < doubletapspeed on next handleY() call
                groundState = GroundState.PLANTED;
                action = Action.CLIMBING;
            }
            canHover = false;
            dashTimeSeconds = Helpers.secondsSince(dashStartTime);
            if (orientation == Orientation.X) {
                velocity.x = Helpers.absoluteToDirectionalValue(Constants.CLIMB_SPEED, directionX, Orientation.X);
            } else if (orientation == Orientation.Y) {
                velocity.y = Helpers.absoluteToDirectionalValue(Constants.CLIMB_SPEED, directionY, Orientation.Y);
            }
            int climbAnimationPercent = (int) (dashTimeSeconds * 100);
            if ((climbAnimationPercent) % 25 >= 0 && (climbAnimationPercent) % 25 <= 13) {
                directionX = Direction.RIGHT;
            } else {
                directionX = Direction.LEFT;
            }
        } else { // if double tapping down, fall from climbable
            dashTimeSeconds = 0;
            canCling = false;
            canClimb = false;
            fall();
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (directionX == Direction.RIGHT) {
            if (lookStartTime != 0) {
                if (directionY == Direction.UP) {
                    region = Assets.getInstance().getGigaGalAssets().lookupStandRight;
                    if (action == Action.FALLING || action == Action.CLIMBING) {
                        region = Assets.getInstance().getGigaGalAssets().lookupFallRight;
                    } else if (action == Action.HOVERING) {
                        region = Assets.getInstance().getGigaGalAssets().lookupHoverRight.getKeyFrame(hoverTimeSeconds);
                    }
                } else if (directionY == Direction.DOWN) {
                    region = Assets.getInstance().getGigaGalAssets().lookdownStandRight;
                    if (action == Action.FALLING || action == Action.CLIMBING) {
                        region = Assets.getInstance().getGigaGalAssets().lookdownFallRight;
                    } else if (action == Action.HOVERING) {
                        region = Assets.getInstance().getGigaGalAssets().lookdownHoverRight.getKeyFrame(hoverTimeSeconds);
                    }
                }
            } else if (action == Action.CLIMBING) {
                region = Assets.getInstance().getGigaGalAssets().climb.getKeyFrame(0.25f);
            } else if (action == Action.STANDING) {
                if (canPeer) {
                    region = Assets.getInstance().getGigaGalAssets().lookbackRight;
                } else if ((!(Helpers.secondsSince(standStartTime) < 1) &&
                      ((Helpers.secondsSince(standStartTime) % 20 < .15f)
                    || (Helpers.secondsSince(standStartTime) % 34 < .1f)
                    || (Helpers.secondsSince(standStartTime) % 35 < .25f)
                    || (Helpers.secondsSince(standStartTime) > 60)))) {
                    region = Assets.getInstance().getGigaGalAssets().blinkRight;
                } else {
                    region = Assets.getInstance().getGigaGalAssets().standRight;
                }
            } else if (action == Action.STRIDING) {
                region = Assets.getInstance().getGigaGalAssets().strideRight.getKeyFrame(Math.min(strideAcceleration * strideAcceleration, strideAcceleration));
            } else if (action == Action.DASHING) {
                region = Assets.getInstance().getGigaGalAssets().dashRight;
            } else if (action == Action.HOVERING) {
                region = Assets.getInstance().getGigaGalAssets().hoverRight.getKeyFrame(hoverTimeSeconds);
            } else if (action == Action.RAPPELLING) {
                if (canHurdle) {
                    region = Assets.getInstance().getGigaGalAssets().graspRight;
                } else {
                    region = Assets.getInstance().getGigaGalAssets().rappelRight;
                }
            } else if (action == Action.RECOILING){
                region = Assets.getInstance().getGigaGalAssets().recoilRight;
            } else if (action == Action.FALLING /*|| action == Action.JUMPING*/) {
                region = Assets.getInstance().getGigaGalAssets().fallRight;
            }
        } else if (directionX == Direction.LEFT) {
            if (lookStartTime != 0) {
                if (directionY == Direction.UP) {
                    region = Assets.getInstance().getGigaGalAssets().lookupStandLeft;
                    if (action == Action.FALLING || action == Action.CLIMBING) {
                        region = Assets.getInstance().getGigaGalAssets().lookupFallLeft;
                    } else if (action == Action.HOVERING) {
                        region = Assets.getInstance().getGigaGalAssets().lookupHoverLeft.getKeyFrame(hoverTimeSeconds);
                    }
                } else if (directionY == Direction.DOWN) {
                    region = Assets.getInstance().getGigaGalAssets().lookdownStandLeft;
                    if (action == Action.FALLING || action == Action.CLIMBING) {
                        region = Assets.getInstance().getGigaGalAssets().lookdownFallLeft;
                    } else if (action == Action.HOVERING) {
                        region = Assets.getInstance().getGigaGalAssets().lookdownHoverLeft.getKeyFrame(hoverTimeSeconds);
                    }
                }
            } else if (action == Action.CLIMBING) {
                region = Assets.getInstance().getGigaGalAssets().climb.getKeyFrame(0.12f);
            } else if (action == Action.STANDING) {
                if (canPeer) {
                    region = Assets.getInstance().getGigaGalAssets().lookbackLeft;
                } else if ((!(Helpers.secondsSince(standStartTime) < 1) &&
                  ((Helpers.secondsSince(standStartTime) % 20 < .15f)
                || (Helpers.secondsSince(standStartTime) % 34 < .1f)
                || (Helpers.secondsSince(standStartTime) % 35 < .25f)
                || (Helpers.secondsSince(standStartTime) > 60)))) {
                    region = Assets.getInstance().getGigaGalAssets().blinkLeft;
                } else {
                    region = Assets.getInstance().getGigaGalAssets().standLeft;
                }
            } else if (action == Action.STRIDING) {
                region = Assets.getInstance().getGigaGalAssets().strideLeft.getKeyFrame(Math.min(strideAcceleration * strideAcceleration, strideAcceleration));
            } else if (action == Action.DASHING) {
                region = Assets.getInstance().getGigaGalAssets().dashLeft;
            } else if (action == Action.HOVERING) {
                region = Assets.getInstance().getGigaGalAssets().hoverLeft.getKeyFrame(hoverTimeSeconds);
            } else if (action == Action.RAPPELLING) {
                if (canHurdle) {
                    region = Assets.getInstance().getGigaGalAssets().graspLeft;
                } else {
                    region = Assets.getInstance().getGigaGalAssets().rappelLeft;
                }
            } else if (action == Action.RECOILING) {
                region = Assets.getInstance().getGigaGalAssets().recoilLeft;
            } else if (action == Action.FALLING /*|| action == Action.JUMPING*/) {
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
    public final ShotIntensity getShotIntensity() { return shotIntensity; }
    @Override public final Material getWeapon() { return weapon; }
    private final float getHalfWidth() { return halfWidth; }
    public List<Material> getWeaponList() { return weaponList; }
    public final float getAmmo() { return ammo; }
    public int getLives() { return lives; }
    public Vector3 getChaseCamPosition() { return chaseCamPosition; }
    public long getLookStartTime() { return lookStartTime; }
    public float getChargeTimeSeconds() { return chargeTimeSeconds; }
    public float getKillPlane() { return killPlane; }

    // Setters
    public void setDirectionX(Direction directionX) { this.directionX = directionX; }
    public void setDirectionY(Direction directionY) { this.directionY = directionY; }
    public void setLives(int lives) { this.lives = lives; }
    public void setHealth(int health) { this.health = health; }
    public void setInputControls(InputControls inputControls) { this.inputControls = inputControls; }
    public void setChaseCamPosition(float offset) {
        lookTimeSeconds = Helpers.secondsSince(lookStartTime);
        if (lookTimeSeconds > 1) {
            offset += 1.5f;
            if (Math.abs(chaseCamPosition.y - position.y) < Constants.MAX_LOOK_DISTANCE) {
                chaseCamPosition.y += Helpers.absoluteToDirectionalValue(offset, directionY, Orientation.Y);
                chaseCamPosition.x = position.x;
            }
        }
    }
    public void resetChaseCamPosition() {
        float offsetDistance = chaseCamPosition.y - position.y;
        // move chasecam back towards gigagal yposition provided yposition cannot be changed until fully reset
        if (Math.abs(offsetDistance) > 5) { // if chasecam offset from gigagal yposition more than five pixels
            if (offsetDistance < 0) {
                chaseCamPosition.y += 2.5f;
            } else if (offsetDistance > 0) {
                chaseCamPosition.y -= 2.5f;
            }
            chaseCamPosition.x = position.x; // set chasecam position to gigagal xposition
        } else if (chaseCamPosition.y != position.y) { // if chasecam offset less than 5 but greater than 0 and actively looking
            chaseCamPosition.set(position, 0); // reset chasecam
            canLook = false; // disable look
        } else {
            lookStartTime = 0;
            lookTimeSeconds = 0;
        }
    }
    public void addWeapon(Material weapon) { weaponToggler.add(weapon); }
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

    public void detectInput() { if (InputControls.getInstance().hasInput()) { standStartTime = TimeUtils.nanoTime(); canPeer = false; } }
    public void setLevel(LevelUpdater level) { this.level = level; }
    public void setSpawnPosition(Vector2 spawnPosition) { this.spawnPosition.set(spawnPosition); }
    public void dispose() {
        weaponList.clear();
    }
}
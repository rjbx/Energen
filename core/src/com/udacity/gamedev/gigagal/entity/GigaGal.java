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
    private boolean onClimbable;
    private boolean onSinkable;
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
        canLook = false;
        canStride = false;
        canJump = false;
        canDash = false;
        canHover = false;
        canCling = false;
        canShoot = true;
        onClimbable = false;
        onSinkable = false;
        chargeStartTime = 0;
        strideStartTime = 0;
        climbStartTime = 0;
        jumpStartTime = 0;
        dashStartTime = 0;
        turboDuration = 0;
        recoveryStartTime = TimeUtils.nanoTime();
    }

    public void update(float delta) {
        // positioning
        previousFramePosition.set(position);
        position.mulAdd(velocity, delta);
        setBounds();

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
                enableCling();
                enableShoot(weapon);
            } else if (action == Action.JUMPING) {
                enableJump();
                enableCling();
                enableShoot(weapon);
            } else if (action == Action.HOVERING) {
                enableHover();
                enableCling();
                enableClimb();
                enableShoot(weapon);
            } else if (action == Action.CLINGING) {
                enableJump();
                enableCling();
                enableShoot(weapon);
            } else if (action == Action.RECOILING) {
                enableCling();
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
        onClimbable = false;
        onSinkable = false;
        for (Ground ground : grounds) {
            // if currently within ground left and right sides
            if (Helpers.overlapsBetweenTwoSides(position.x, getHalfWidth(), ground.getLeft(), ground.getRight())) {
                // apply following rules (bump side and bottom) only if ground height > ledge height
                // ledges only apply collision detection on top, and not on sides and bottom as do grounds
                if (getBottom() <= ground.getTop() && getTop() >= ground.getBottom()) {
                    // alternate collision handling to allow passing through top of descendables and prevent setting atop as with other grounds
                    if (!(ground instanceof Descendable)) {
                        // for ledge and climbable box, ignore side and bottom collision always and top collision when not climbing downward
                        if (ground.getHeight() > Constants.MAX_LEDGE_HEIGHT
                        && !(ground instanceof Box && ((Box) ground).getClimbable())) {
                            touchGroundBottom(ground);
                            touchGroundSide(ground);
                            touchGroundTop(ground);
                        } else if (!(action == Action.CLIMBING && directionY == Direction.DOWN)) {
                            touchGroundTop(ground);
                            canCling = false; // deactivate cling if ground below max ledge height
                        }
                        // alt ground collision for descendables (does not override normal ground collision in order to prevent descending through nondescendable grounds)
                    } else if (ground instanceof Descendable && (touchedGround == null || touchedGround instanceof Descendable)) {
                        touchDescendableGround(ground);
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
            } else if (ground instanceof Reboundable) {
                Reboundable reboundable = (Reboundable) ground;
                if (reboundable.getState() && !(reboundable instanceof Button)) {
                    reboundable.resetStartTime();
                    reboundable.setState(false);
                }
            }
        }
        untouchGround();
    }

    private void touchGroundSide(Ground ground) {
        // if during previous frame was not, while currently is, between ground left and right sides
        if (!Helpers.overlapsBetweenTwoSides(previousFramePosition.x, getHalfWidth(), ground.getLeft(), ground.getRight()) && (touchedGround == null || touchedGround == ground || touchedGround.getTop() != ground.getTop())) {
            // only when not grounded and not recoiling
            if (groundState != GroundState.PLANTED) {
                // if x velocity (magnitude, without concern for direction) greater than one third max speed,
                // boost x velocity by starting speed, enable cling, verify rappelling ground and capture rappelling ground boundaries
                if (Math.abs(velocity.x) > Constants.GIGAGAL_MAX_SPEED / 4) {
                    // if already clinging, halt x progression
                    if (action != Action.CLINGING) {
                        canCling = true; // enable cling
                        touchedGround = ground;
                        killPlane = touchedGround.getBottom() + Constants.KILL_PLANE;
                    }
                    // if absval x velocity not greater than one fourth max speed but aerial and bumping ground side, fall
                } else {
                    // if not already hovering and descending, also disable hover
                    if (action != Action.HOVERING && velocity.y < 0) {
                        canHover = false; // disable hover
                    }
                    canCling = false;
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
                    if (chargeStartTime != 0 && directionX == Direction.RIGHT) {
                        if (!chargeable.isActive()) {
                            chargeable.activate();
                        } else if (chargeTimeSeconds > 1) {
                            chargeable.charge(chargeTimeSeconds);
                        }
                    } else {
                        chargeable.charge(0);
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
        } else {
            canCling = false;
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
        if ((getBottom() <= ground.getTop()
                && (!canCling || (touchedGround != null && ground.getTop() != touchedGround.getTop()))) // distinguishes when touching two different grounds and permits uninterrupted striding atop
                && (previousFramePosition.y - Constants.GIGAGAL_EYE_HEIGHT >= ground.getTop() - 1)) {
            velocity.y = 0; // prevents from descending beneath ground top
            position.y = ground.getTop() + Constants.GIGAGAL_EYE_HEIGHT; // sets Gigagal atop ground
            setAtopGround(ground);
            if (ground instanceof Skateable) {
                if (groundState == GroundState.AIRBORNE) {
                    stand(); // set groundstate to standing
                    lookStartTime = 0;
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
                Reboundable reboundable = (Reboundable) ground;
                reboundable.setState(true);
            } else if (ground instanceof Unbearable) {
                canHover = false;
                Random xKnockback = new Random();
                velocity.set(Helpers.absoluteToDirectionalValue(xKnockback.nextFloat() * 200, directionX, Orientation.X), Constants.PROTRUSION_GAS_KNOCKBACK.y);
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
        } else if (ground instanceof Climbable && !(touchedGround instanceof Sinkable)) {
            touchedGround = ground;
            if (Helpers.overlapsBetweenTwoSides(position.x, getHalfWidth(), ground.getLeft(), ground.getRight())) {
                if (getTop() > ground.getBottom()) {
                    onClimbable = true;
                }
            }

            if (climbTimeSeconds == 0) {
                if ((getBottom() <= ground.getTop() && (!canCling || (touchedGround != null && ground.getTop() != touchedGround.getTop()))
                        && previousFramePosition.y - Constants.GIGAGAL_EYE_HEIGHT >= ground.getTop())
                        || canClimb && climbStartTime != 0) {
                    setAtopGround(ground);
                    if (action != Action.CLIMBING) {
                        velocity.y = 0; // prevents from descending beneath ground top
                        position.y = ground.getTop() + Constants.GIGAGAL_EYE_HEIGHT; // sets Gigagal atop ground
                    }
                }
                if (action != Action.CLIMBING) {
                    if (canClimb && !inputControls.jumpButtonPressed && action == Action.STANDING) {
                        if (!(ground instanceof Pole)) {
                            canJump = true;
                        }
                        jump();
                    }
                }
            }
        } else if (ground instanceof Transportable) {
            if ((position.dst(ground.getPosition()) < (Constants.TELEPORT_CENTER.x + getHalfWidth())) && inputControls.jumpButtonPressed) {
                position.set(((Teleport) ground).getDestination());
            }
        }
    }

    private void setAtopGround(Ground ground) {
        touchedGround = ground;
        killPlane = touchedGround.getBottom() + Constants.KILL_PLANE;
        hoverStartTime = 0;
        clingStartTime = 0;
        canLook = true;
        canHover = false;
        if (groundState == GroundState.AIRBORNE && !(ground instanceof Skateable)) {
            stand(); // set groundstate to standing
            lookStartTime = 0;
        }
    }

    private void untouchGround() {
        if (touchedGround != null && action != Action.HOVERING) {
            if (getBottom() > touchedGround.getTop() || getTop() < touchedGround.getBottom())
                /*(!Helpers.overlapsBetweenTwoSides(position.y, (getTop() - getBottom()) / 2, touchedGround.getBottom(), touchedGround.getTop()) */{
                if (touchedGround instanceof Reboundable) {
                    Reboundable reboundable = (Reboundable) touchedGround;
                    reboundable.resetStartTime();
                    reboundable.setState(false);
                }
                if (action == Action.CLINGING) {
                    velocity.x = 0; // prevents falling with backward momentum after cling-sliding down platform side through its bottom
                }
                canCling = false;
                fall();
            } else if (!Helpers.overlapsBetweenTwoSides(position.x, getHalfWidth(), touchedGround.getLeft(), touchedGround.getRight())) {
                onSinkable = false;
                lookTimeSeconds = 0;
                lookStartTime = 0;
                if (action != Action.CLINGING) {
                    fall();
                }
            } else if (touchedGround instanceof Destructible) {
                Destructible destructible = (Destructible) touchedGround;
                if (destructible.getHealth() < 1) {
                    fall();
                }
            }
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
                        level.spawnExplosion(intersectionPoint, hazard.getType());
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
                            } else if (!onSinkable){
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
                if (onSinkable) {
                    velocity.y *= 5;
                }
            }
            if (canLook) {
                canStride = false;
                if (inputControls.jumpButtonJustPressed && !canCling) {
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
                        if (climbTimeSeconds == 0) {  // if directional released
                            if (!directionChanged) { // if tapping in same direction
                                // if difference between current time and previous tap start time is less than double tap speed
                                if (((TimeUtils.nanoTime() - climbStartTime) * MathUtils.nanoToSec) < Constants.DOUBLE_TAP_SPEED) {
                                    if (directionY == Direction.UP) { // enable increased ascension speed
                                        canDash = true; // checks can dash after calling climb() to apply speed boost
                                    } else if (directionY == Direction.DOWN) { // drop down from climbable (drop handled from climb())
                                        lookStartTime = TimeUtils.nanoTime(); // prevents from reengaging climbable from enableclimb() while falling
                                        onClimbable = false; // meets requirement within climb() to disable climb and enable fall
                                    }
                                }
                                climbStartTime = TimeUtils.nanoTime(); // replace climb start time with that of most recent tap
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
                climbTimeSeconds = 0; // indicates release of directional for enabling double tap
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
            canHover = false;
            strideStartTime = 0;
        } else if (touchedGround instanceof Sinkable) {
            canHover = false;
        }
        if (!canCling) {
            touchedGround = null;
            canHover = true;
        }
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
        this.velocity.x = velocity.x;
        this.velocity.y = velocity.y;
    }

    private void enableShoot(Material weapon) {
        if (canShoot) {
            if (inputControls.shootButtonPressed || (action == Action.CLINGING && (inputControls.rightButtonPressed || inputControls.leftButtonPressed))) {
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
            Assets.getInstance().getSoundAssets().getMaterialSound(weapon).play(1,2,0);
        }
        if (chargeModifier == 0) {
            ammo -= ammoUsed * ammoMultiplier;
        }
        Vector2 ammoPosition = new Vector2(
                position.x + Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_CANNON_OFFSET.x, directionX, Orientation.X),
                position.y + Constants.GIGAGAL_CANNON_OFFSET.y
        );
        if (lookStartTime != 0) {
            ammoPosition.add(Helpers.absoluteToDirectionalValue(0, directionX, Orientation.X), Helpers.absoluteToDirectionalValue(6, directionY, Orientation.Y));
            level.spawnAmmo(ammoPosition, directionY, Orientation.Y, shotIntensity, weapon, true);
        } else {
            level.spawnAmmo(ammoPosition, directionX, Orientation.X, shotIntensity, weapon, true);
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
        if (turbo < Constants.MAX_TURBO) {
            turbo += Constants.STRIDE_TURBO_INCREMENT;
        }
        canLook = false;
        if (strideStartTime == 0) {
            strideSpeed = velocity.x;
            action = Action.STRIDING;
            strideStartTime = TimeUtils.nanoTime();
        }
        strideTimeSeconds = Helpers.secondsSince(strideStartTime);
        strideAcceleration = strideTimeSeconds + Constants.GIGAGAL_STARTING_SPEED;
        velocity.x = Helpers.absoluteToDirectionalValue(Math.min(Constants.GIGAGAL_MAX_SPEED * strideAcceleration + Constants.GIGAGAL_STARTING_SPEED, Constants.GIGAGAL_MAX_SPEED), directionX, Orientation.X);
        if (touchedGround instanceof Rideable) {
            velocity.x += Helpers.absoluteToDirectionalValue(Constants.TREADMILL_SPEED, ((Rideable) touchedGround).getDirection(), Orientation.X);
        } else if (touchedGround instanceof Skateable) {
            velocity.x = strideSpeed + Helpers.absoluteToDirectionalValue(Math.min(Constants.GIGAGAL_MAX_SPEED * strideAcceleration / 2 + Constants.GIGAGAL_STARTING_SPEED, Constants.GIGAGAL_MAX_SPEED * 2), directionX, Orientation.X);
        } else if (onSinkable) {
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
            dashStartTime = TimeUtils.nanoTime();
            strideStartTime = 0;
            canStride = false;
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
            if ((inputControls.jumpButtonJustPressed || action == Action.JUMPING)
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
        float jumpTimeSeconds = Helpers.secondsSince(jumpStartTime);
        if (jumpTimeSeconds < Constants.MAX_JUMP_DURATION) {
            velocity.y = Constants.JUMP_SPEED;
            velocity.y *= Constants.STRIDING_JUMP_MULTIPLIER;
            if (touchedGround instanceof Reboundable) {
                velocity.y *= 2;
            } else if (onSinkable) {
                fall(); // causes fall texture to render for one frame
            }
        } else {
            fall();
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
            startTurbo = turbo;
            turboDuration = Constants.MAX_HOVER_DURATION * (startTurbo / Constants.MAX_TURBO);
            action = Action.HOVERING; // indicates currently hovering
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

    private void enableCling() {
        if (action == Action.CLINGING) {
            cling();
        } else if (canCling){
            if (!canHover || action == Action.HOVERING) {
                fall(); // begin descent from ground side sans access to hover
                canHover = false; // disable hover if not already
            }
            if (inputControls.jumpButtonJustPressed) {
                if (position.y > touchedGround.getTop() - 10) {
                    position.y = touchedGround.getTop() - 10;
                    canClimb = true;
                }
                cling();
            }
        }
    }

    private void cling() {
        if (canCling) {
            action = Action.CLINGING;
            groundState = GroundState.AIRBORNE;
            startTurbo = turbo;
            clingStartTime = TimeUtils.nanoTime();
            turboDuration = Constants.MAX_CLING_DURATION * (startTurbo / Constants.MAX_TURBO);
            if (!Helpers.movingOppositeDirection(velocity.x, directionX, Orientation.X)) {
                directionX = Helpers.getOppositeDirection(directionX);
            }
            hoverStartTime = 0;
            canJump = true;
            canCling = false;
        }
        if (directionX == Direction.LEFT) {
            position.x = touchedGround.getLeft() - getHalfWidth();
        } else {
            position.x = touchedGround.getRight() + getHalfWidth();
        }
        float clingTimeSeconds = Helpers.secondsSince(clingStartTime);
        if (!inputControls.jumpButtonPressed) {
            if (clingTimeSeconds >= Constants.CLING_FRAME_DURATION) {
                velocity.x = Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_MAX_SPEED, directionX, Orientation.X);
                jump();
            } else {
                canHover = true;
            }
        } else {
            lookStartTime = 0;
            if (inputControls.downButtonPressed) {
                velocity.y += Constants.CLING_GRAVITY_OFFSET;
            } else if (inputControls.upButtonPressed && canClimb) {
                canClimb = false;
                canCling = false;
                directionX = Helpers.getOppositeDirection(directionX);
                velocity.x = Helpers.absoluteToDirectionalValue(Constants.CLIMB_SPEED, directionX, Orientation.X);
                jump();
            } else if (turbo < 1) {
                turbo = 0;
                velocity.y += Constants.CLING_GRAVITY_OFFSET;
            } else {
                turbo -= Constants.FALL_TURBO_INCREMENT * turboMultiplier;
                velocity.y = 0;
            }
        }
    }

    private void enableClimb() {
        if (onClimbable) {
            if (inputControls.jumpButtonPressed) {
                if (lookStartTime == 0) { // cannot initiate climb if already looking; must first neutralize
                    canLook = false; // prevents look from overriding climb
                    canClimb = true; // enables climb handling from handleY()
                }
            } else {
                canLook = true; // enables look when engaging climbable but not actively climbing
                canClimb  = false; // prevents climb initiation when jumpbutton released
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
        if (onClimbable) { // onclimbable set to false from handleYinputs() if double tapping down
            if (action != Action.CLIMBING) { // at the time of climb initiation
                climbStartTime = 0; // overrides assignment of current time preventing nanotime - climbstarttime < doubletapspeed on next handleY() call
                groundState = GroundState.PLANTED;
                action = Action.CLIMBING;
            }
            canHover = false;
            climbTimeSeconds = Helpers.secondsSince(climbStartTime);
            if (orientation == Orientation.X) {
                velocity.x = Helpers.absoluteToDirectionalValue(Constants.CLIMB_SPEED, directionX, Orientation.X);
            } else if (orientation == Orientation.Y) {
                velocity.y = Helpers.absoluteToDirectionalValue(Constants.CLIMB_SPEED, directionY, Orientation.Y);
            }
            int climbAnimationPercent = (int) (climbTimeSeconds * 100);
            if ((climbAnimationPercent) % 25 >= 0 && (climbAnimationPercent) % 25 <= 13) {
                directionX = Direction.RIGHT;
            } else {
                directionX = Direction.LEFT;
            }
        } else { // if double tapping down, fall from climbable
            climbStartTime = 0;
            climbTimeSeconds = 0;
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
                region = Assets.getInstance().getGigaGalAssets().standRight;
            } else if (action == Action.STRIDING) {
                region = Assets.getInstance().getGigaGalAssets().strideRight.getKeyFrame(Math.min(strideAcceleration * strideAcceleration, strideAcceleration));
            } else if (action == Action.DASHING) {
                region = Assets.getInstance().getGigaGalAssets().dashRight;
            } else if (action == Action.HOVERING) {
                region = Assets.getInstance().getGigaGalAssets().hoverRight.getKeyFrame(hoverTimeSeconds);
            } else if (action == Action.CLINGING) {
                if (canClimb) {
                    region = Assets.getInstance().getGigaGalAssets().graspRight;
                } else {
                    region = Assets.getInstance().getGigaGalAssets().clingRight;
                }
            } else if (action == Action.RECOILING){
                region = Assets.getInstance().getGigaGalAssets().recoilRight;
            } else if (action == Action.FALLING) {
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
                region = Assets.getInstance().getGigaGalAssets().standLeft;
            } else if (action == Action.STRIDING) {
                region = Assets.getInstance().getGigaGalAssets().strideLeft.getKeyFrame(Math.min(strideAcceleration * strideAcceleration, strideAcceleration));
            } else if (action == Action.DASHING) {
                region = Assets.getInstance().getGigaGalAssets().dashLeft;
            } else if (action == Action.HOVERING) {
                region = Assets.getInstance().getGigaGalAssets().hoverLeft.getKeyFrame(hoverTimeSeconds);
            } else if (action == Action.CLINGING) {
                if (canClimb) {
                    region = Assets.getInstance().getGigaGalAssets().graspLeft;
                } else {
                    region = Assets.getInstance().getGigaGalAssets().clingLeft;
                }
            } else if (action == Action.RECOILING) {
                region = Assets.getInstance().getGigaGalAssets().recoilLeft;
            } else if (action == Action.FALLING) {
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
    @Override public final boolean getClingStatus() { return canCling; }
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

    public void setLevel(LevelUpdater level) { this.level = level; }
    public void setSpawnPosition(Vector2 spawnPosition) { this.spawnPosition.set(spawnPosition); }
    public void dispose() {
        weaponList.clear();
    }
}
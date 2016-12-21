package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums.*;
import com.udacity.gamedev.gigagal.util.Utils;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
    private Direction facing;
    private AerialState aerialState;
    private GroundState groundState;
    private WeaponType weapon;
    private ShotIntensity shotIntensity;
    private Direction lookDirection;
    private Direction toggleDirection;
    private long strideStartTime;
    private long jumpStartTime;
    private long dashStartTime;
    private long hoverStartTime;
    private long ricochetStartTime;
    private long chargeStartTime;
    private long recoveryStartTime;
    private float turboDuration;
    private float turbo;
    private float startTurbo;
    private float strideAcceleration;
    private float hoverTimeSeconds;
    private float aerialTakeoff;
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
    private boolean knockedBack;
    private boolean slidPlatform;
    private boolean groundedPlatform;
    public boolean leftButtonPressed;
    public boolean rightButtonPressed;
    public boolean upButtonPressed;
    public boolean downButtonPressed;
    public boolean jumpButtonPressed;
    public boolean shootButtonPressed;

    // ctor
    public GigaGal(Vector2 spawnLocation, Level level) {
        this.spawnLocation = spawnLocation;
        this.level = level;
        position = new Vector2();
        velocity = new Vector2();
        previousFramePosition = new Vector2();
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
        float slidPlatformTop = 0;
        float slidPlatformBottom = 0;
        float groundedPlatformLeft = 0;
        float groundedPlatformRight = 0;
        for (Ground ground : grounds) {
            // if currently within ground left and right sides
            if (Utils.betweenSides(ground, position.x)) {
                // apply following rules (bump side and bottom) only if ground height > ledge height
                // ledges only apply collision detection on top, and not on sides and bottom as do platforms
                if (ground.getHeight() > Constants.MAX_LEDGE_HEIGHT
                        && getBottom() <= ground.getTop() && getTop() >= ground.getBottom()) {
                    // if during previous frame was not, while currently is, between ground left and right sides
                    if (!Utils.betweenSides(ground, previousFramePosition.x)) {
                        // only when not grounded and not recoiling
                        if (groundState == GroundState.AIRBORNE && aerialState != AerialState.RECOILING) {
                            // if lateral velocity (magnitude, without concern for direction) greater than one third max speed,
                            // boost lateral velocity by starting speed, enable ricochet, verify slid ground and capture slid ground boundaries
                            if (Math.abs(velocity.x) > Constants.GIGAGAL_MAX_SPEED / 3) {
                                // if already ricocheting, halt lateral progression
                                if (aerialState == AerialState.RICOCHETING) {
                                    velocity.x = 0; // halt lateral progression
                                    // if not already ricocheting and hover was previously activated before grounding
                                } else if (!canHover || aerialState == AerialState.HOVERING){
                                    fall(); // begin descent from ground side sans access to hover
                                    canHover = false; // disable hover if not already
                                }

                                if (aerialState != AerialState.RICOCHETING){
                                    startTurbo = turbo;
                                    turbo = ((Math.abs(getBottom() - ground.getBottom()) / (ground.getTop() - ground.getBottom())) * startTurbo);
                                }
                                velocity.x += Utils.absoluteToDirectionalValue(Constants.GIGAGAL_STARTING_SPEED, facing, Orientation.LATERAL); // boost lateral velocity by starting speed
                                canRicochet = true; // enable ricochet
                                slidPlatform = true; // verify slid ground
                                slidPlatformTop = ground.getTop(); // capture slid ground boundary
                                slidPlatformBottom = ground.getBottom(); // capture slid ground boundary
                            // if absval lateral velocity  not greater than one third max speed but aerial and bumping ground side, fall
                            } else {
                                // if not already hovering and descending, also disable hover
                                if (aerialState != AerialState.HOVERING && velocity.y < 0) {
                                    canHover = false; // disable hover
                                }
                                fall(); // fall regardless of whether or not inner condition met
                            }
                        // only when grounded
                        } else if (aerialState == AerialState.GROUNDED){
                            stand();
                        }
                        // if contact with ground sides detected without concern for ground state (either grounded or airborne),
                        // reset stride acceleration, disable stride and dash, and set gigagal at ground side
                        strideStartTime = 0; // reset stride acceleration
                        canStride = false; // disable stride
                        canDash = false; // disable dash
                        position.x = previousFramePosition.x; // halt lateral progression
                    // else if no detection with ground sides, disable ricochet
                    } else {
                        canRicochet = false; // disable ricochet
                    }
                    // if contact with ground bottom detected, halts upward progression and set gigagal at ground bottom
                    if ((previousFramePosition.y + Constants.GIGAGAL_HEAD_RADIUS) <= ground.getBottom()) {
                        velocity.y = 0; // prevents from ascending above ground bottom
                        position.y = previousFramePosition.y;  // sets gigagal at ground bottom
                        fall(); // descend from point of contact with ground bottom
                    }
                }
                // if contact with ground top detected, halt downward progression and set gigagal atop ground
                if ((previousFramePosition.y - Constants.GIGAGAL_EYE_HEIGHT) >= ground.getTop()
                        && getBottom() <= ground.getTop()
                        && ground.getTop() != slidPlatformTop) {
                    velocity.y = 0; // prevents from descending beneath ground top
                    position.y = ground.getTop() + Constants.GIGAGAL_EYE_HEIGHT; // sets Gigagal atop ground
                    canChangeDirection = true; // enable change of direction
                    groundedPlatform = true; // verify contact with ground top
                    groundedPlatformLeft = ground.getLeft(); // capture grounded ground boundary
                    groundedPlatformRight = ground.getRight(); // capture grounded ground boundary
                    hoverStartTime = 0; // reset hover
                    ricochetStartTime = 0; // reset ricochet
                    knockedBack = false; // reset knockback boolean
                    canHover = true; // enable hover
                    // if groundstate is airborne, set to standing
                    if (groundState == GroundState.AIRBORNE) {
                        stand(); // set groundstate to standing
                    }
                }
                // if below minimum ground distance while descending excluding post-ricochet, disable ricochet and hover
                // caution when crossing plane between ground top and minimum hover height / ground distance
                // cannons, which inherit ground, can be mounted along sides of platforms causing accidental plane breakage
                if (getBottom() < (ground.getTop() + Constants.MIN_GROUND_DISTANCE)
                        && getBottom() > ground.getTop() // GG's bottom is greater than ground top but less than boundary
                        && velocity.y < 0 // prevents disabling features when crossing boundary while ascending on jump
                        && ricochetStartTime == 0 // only if have not ricocheted since last grounded
                        && !(ground instanceof Cannon) // only if ground is not instance of platform
                        ) {
                    canRicochet = false; // disables ricochet
                    canHover = false; // disables hover
                }
            }
        }
        // disables ricochet if no contact with slid platform side
        if (slidPlatform) {
            if (getBottom() > slidPlatformTop  || getTop() < slidPlatformBottom) {
                canRicochet = false;
                slidPlatform = false;
            }
        }
        // falls if no detection with grounded platform top
        if (groundedPlatform && aerialState != AerialState.RECOILING) {
            if (getRight() < groundedPlatformLeft || getLeft() > groundedPlatformRight) {
                groundedPlatform = false;
                fall();
            }
        }
    }

    private void handleLateralInputs() {
        boolean left = Gdx.input.isKeyPressed(Keys.A) || leftButtonPressed;
        boolean right = Gdx.input.isKeyPressed(Keys.S) || rightButtonPressed;
        boolean directionChanged = false;
        boolean isStriding = true;
        if (left && !right) {
            directionChanged = Utils.setFacing(this, Direction.LEFT);
        } else if (!left && right) {
            directionChanged = Utils.setFacing(this, Direction.RIGHT);
        } else {
            isStriding = false;
        }

        if (groundState != GroundState.AIRBORNE && lookDirection == null) {
            if (directionChanged) {
               if (groundState == groundState.DASHING){
                    dashStartTime = 0;
                    canDash = false;
                }
                strideStartTime = 0;
                stand();
            } else if (isStriding && !canStride && groundState != GroundState.DASHING) {
                if (strideStartTime == 0) {
                    canStride = true;
                } else if (Utils.secondsSince(strideStartTime) > Constants.DOUBLE_TAP_SPEED) {
                    strideStartTime = 0;
                } else {
                    canDash = true;
                }
            } else if (!isStriding && groundState != GroundState.DASHING) {
                stand();
                canStride = false;
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
                    if (ammo > 100) {
                        ammo = 100;
                    }
                } else if (powerup instanceof HealthPowerup) {
                    health += Constants.POWERUP_HEALTH;
                    if (health > 100) {
                        health = 100;
                    }
                } else if (powerup instanceof TurboPowerup) {
                    turbo += Constants.POWERUP_TURBO;
                    if (aerialState == AerialState.HOVERING) {
                        hoverStartTime = TimeUtils.nanoTime();
                    }
                    if (groundState == GroundState.DASHING) {
                        dashStartTime = TimeUtils.nanoTime();
                    }
                    if (turbo > 100) {
                        turbo = 100;
                    }
                }
                level.setScore(level.getScore() + Constants.POWERUP_SCORE);
                powerups.removeValue(powerup, true);
            }
        }
    }

    // detects contact with enemy (change aerial & ground state to recoil until grounded)
    private void recoilFromHazards(Array<Hazard> hazards) {
        for (Hazard hazard : hazards) {
            if (!knockedBack
                && Utils.secondsSince(recoveryStartTime) > Constants.RECOVERY_TIME) {
                Rectangle bounds = new Rectangle(hazard.getLeft(), hazard.getBottom(), hazard.getWidth(), hazard.getHeight());
                if (getBounds().overlaps(bounds)) {
                    knockedBack = true;
                    shotIntensity = ShotIntensity.NORMAL;
                    recoveryStartTime = TimeUtils.nanoTime();
                    chargeStartTime = 0;
                    lookDirection = null;
                    canLook = false;
                    turbo = 0;
                    int damage = hazard.getDamage();
                    float margin = 0;
                    if (hazard instanceof Destructible) {
                        margin = hazard.getWidth() / 6;
                    }
                    if (!(hazard instanceof Ammo && ((Ammo) hazard).isFromGigagal())) {
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
        this.velocity.x = velocity.x;
        this.velocity.y = velocity.y;
    }

    private void enableToggle(Direction toggleDirection) {
        boolean directionChanged = (this.toggleDirection != toggleDirection);
        if (Gdx.input.isKeyJustPressed(Keys.BACKSLASH) || jumpButtonPressed) {
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
            if (Gdx.input.isKeyPressed(Keys.ENTER) || shootButtonPressed) {
                if (chargeStartTime == 0) {
                    canCharge = true;
                    chargeStartTime = TimeUtils.nanoTime();
                } else if (Utils.secondsSince(chargeStartTime) > Constants.CHARGE_DURATION) {
                    shotIntensity = ShotIntensity.CHARGED;
                }

            } else if (canCharge) {
                int ammoUsed;

                if ((weapon == WeaponType.NATIVE
                || ammo < 3 && shotIntensity == ShotIntensity.CHARGED)
                || ammo < 1) {
                    ammoUsed = 0;
                    weapon = WeaponType.NATIVE;
                } else {
                    ammoUsed = Utils.useAmmo(shotIntensity);
                }

                shoot(shotIntensity, weapon, ammoUsed);
                chargeStartTime = 0;
                this.shotIntensity = ShotIntensity.NORMAL;
                canCharge = false;
            }
        }
    }

    public void shoot(ShotIntensity shotIntensity, WeaponType weapon, int ammoUsed) {
        ammo -= ammoUsed;
        Vector2 ammoPosition = new Vector2(
                    position.x + Utils.absoluteToDirectionalValue(Constants.GIGAGAL_CANNON_OFFSET.x, facing, Orientation.LATERAL),
                    position.y + Constants.GIGAGAL_CANNON_OFFSET.y
        );
        if (lookDirection == Direction.UP || lookDirection == Direction.DOWN) {
            ammoPosition.add(Utils.absoluteToDirectionalValue(0, facing, Orientation.LATERAL),  Utils.absoluteToDirectionalValue(6, lookDirection, Orientation.VERTICAL));
            level.spawnAmmo(ammoPosition, lookDirection, Orientation.VERTICAL, shotIntensity, weapon, true);
        } else {
            level.spawnAmmo(ammoPosition, facing, Orientation.LATERAL, shotIntensity, weapon, true);
        }
    }

    public void respawn() {
        position.set(spawnLocation);
        velocity.setZero();
        facing = Direction.RIGHT;
        groundState = GroundState.AIRBORNE;
        aerialState = AerialState.FALLING;
        canLook = false;
        canStride = false;
        canJump = false;
        canDash = false;
        canHover = false;
        canRicochet = false;
        canShoot = true;
        canCharge = false;
        canChangeDirection = false;
        shotIntensity = ShotIntensity.NORMAL;
        slidPlatform = false;
        groundedPlatform = false;
        knockedBack = false;
        chargeStartTime = 0;
        strideStartTime = 0;
        jumpStartTime = 0;
        dashStartTime = 0;
        recoveryStartTime = TimeUtils.nanoTime();
        health = 100;
        turbo = 100;
        startTurbo = turbo;
        turboDuration = 0;
    }

    private void enableLook() {
        if (canLook) {
            look();
        }
    }

    private void look() {

        boolean up = Gdx.input.isKeyPressed(Keys.W) || upButtonPressed;
        boolean down = Gdx.input.isKeyPressed(Keys.Z) || downButtonPressed;
        boolean looking = up || down;
        if (looking) {
            if (up) {
                lookDirection = Direction.UP;
            } else if (down) {
                lookDirection = Direction.DOWN;
            }
            canHover = false;
            canJump = false;
            enableToggle(lookDirection);
        } else {
            canLook = false;
            lookDirection = null;
        }
    }

    private void enableStride() {
        handleLateralInputs();
        if (canStride) {
            stride();
        }
    }

    private void stride() {
        if (turbo < 100) {
            turbo += .75f;
        }
        canLook = false;
        lookDirection = null;
        if (strideStartTime == 0) {
            groundState = GroundState.STRIDING;
            strideStartTime = TimeUtils.nanoTime();
        }
        strideAcceleration = Utils.secondsSince(strideStartTime) + Constants.GIGAGAL_STARTING_SPEED;
        velocity.x = Utils.absoluteToDirectionalValue(Math.min(Constants.GIGAGAL_MAX_SPEED * strideAcceleration + Constants.GIGAGAL_STARTING_SPEED, Constants.GIGAGAL_MAX_SPEED), facing, Orientation.LATERAL);
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
            turboDuration = Constants.MAX_DASH_DURATION * ((float) startTurbo / 100);
            groundState = GroundState.DASHING;
            dashStartTime = TimeUtils.nanoTime();
            strideStartTime = 0;
            canStride = false;
        }
        turbo = (((turboDuration - Utils.secondsSince(dashStartTime)) / turboDuration) * startTurbo);
        if (turbo >= 1) {
            velocity.x = Utils.absoluteToDirectionalValue(Constants.GIGAGAL_MAX_SPEED, facing, Orientation.LATERAL);
        } else {
            canDash = false;
            dashStartTime = 0;
            stand();
        }
    }

    private void enableJump() {
        if (((Gdx.input.isKeyJustPressed(Keys.BACKSLASH) || jumpButtonPressed) && canJump)
                || aerialState == AerialState.JUMPING) {
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
        if (Utils.secondsSince(jumpStartTime) < Constants.MAX_JUMP_DURATION) {
            velocity.y = Constants.JUMP_SPEED;
            velocity.y *= Constants.STRIDING_JUMP_MULTIPLIER;
        } else {
            fall();
        }
    }

    private void enableHover() {
        if (canHover) {
            if (Gdx.input.isKeyJustPressed(Keys.BACKSLASH) || jumpButtonPressed) {
                if (aerialState == AerialState.HOVERING) {
                    canHover = false;
                    hoverStartTime = 0;
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
        if (hoverStartTime == 0) {
            startTurbo = turbo;
            turboDuration = Constants.MAX_HOVER_DURATION * ((float) startTurbo / 100);
            aerialState = AerialState.HOVERING; // indicates currently hovering
            hoverStartTime = TimeUtils.nanoTime(); // begins timing hover duration
        }
        hoverTimeSeconds = Utils.secondsSince(hoverStartTime); // for comparing with max hover time
        turbo = (((turboDuration - Utils.secondsSince(hoverStartTime)) / turboDuration * startTurbo));
        if (turbo >= 1) {
            velocity.y = 0; // disables impact of gravity
        } else {
            canHover = false;
            fall(); // when max hover time is exceeded
        }
    }

    private void enableRicochet() {
        if (((Gdx.input.isKeyJustPressed(Keys.BACKSLASH) || jumpButtonPressed) && canRicochet)
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
            if (turbo > 10) {
           //     turbo -= 10;
            }
        }
        if (Utils.secondsSince(ricochetStartTime) >= Constants.RICOCHET_FRAME_DURATION) {
            facing = Utils.getOppositeDirection(facing);
            velocity.x = Utils.absoluteToDirectionalValue(Constants.GIGAGAL_MAX_SPEED, facing, Orientation.LATERAL);
            jump();
        } else {
            canChangeDirection = false;
            canHover = true;
        }
    }

    private void stand() {
        velocity.x = 0;
        groundState = GroundState.STANDING;
        aerialState = AerialState.GROUNDED;
        canJump = true;
        canLook = true;
        if (turbo < 100) {
            turbo += 1.5f;
        }
    }

    private void fall() {
        strideStartTime = 0;
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
        if (turbo < 100) {
            turbo += .5f;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion region = Assets.getInstance().getGigaGalAssets().standRight;
        if (facing == Direction.RIGHT) {
            if (lookDirection == Direction.UP) {
                region = Assets.getInstance().getGigaGalAssets().lookupRight;
            } else if (lookDirection == Direction.DOWN) {
                region = Assets.getInstance().getGigaGalAssets().lookdownRight;
            } else if (aerialState != AerialState.GROUNDED) {
                if (aerialState == AerialState.HOVERING) {
                    hoverTimeSeconds = Utils.secondsSince(hoverStartTime);
                    region = Assets.getInstance().getGigaGalAssets().hoverRightAnimation.getKeyFrame(hoverTimeSeconds);
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
                region = Assets.getInstance().getGigaGalAssets().strideRightAnimation.getKeyFrame(Math.min(strideAcceleration * strideAcceleration, strideAcceleration));
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
                    region = Assets.getInstance().getGigaGalAssets().hoverLeftAnimation.getKeyFrame(hoverTimeSeconds);
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
                region = Assets.getInstance().getGigaGalAssets().strideLeftAnimation.getKeyFrame(Math.min(strideAcceleration * strideAcceleration, strideAcceleration));
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
    public Rectangle getBounds() { return  new Rectangle( getLeft(), getBottom(), getWidth(), getHeight()); }
    public boolean getJumpStatus() { return canJump; }
    public boolean getHoverStatus() { return canHover; }
    public boolean getRicochetStatus() { return canRicochet; }
    public boolean getDashStatus() { return canDash; }
    public ShotIntensity getShotIntensity() { return shotIntensity; }
    public WeaponType getWeapon() { return weapon; }
    public List<WeaponType> getWeaponList() { return weaponList; }
    public void addWeapon(WeaponType weapon) { weaponToggler.add(weapon); }

    // Setters
    public void setFacing(Direction facing) { this.facing = facing; }
    public void setChargeStartTime(long chargeStartTime) { this.chargeStartTime = chargeStartTime; }
    public void setLives(int lives) { this.lives = lives; }
    public void setHealth(int health) { this.health = health; }
}
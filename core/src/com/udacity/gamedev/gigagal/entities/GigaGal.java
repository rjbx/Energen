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
    private List<Weapon> weaponList;
    private ListIterator<Weapon> weaponToggler;
    private Level level;
    private Vector2 spawnLocation;
    private Vector2 position;
    private Vector2 previousFramePosition;
    private Vector2 velocity;
    private Direction facing;
    private AerialState aerialState;
    private GroundState groundState;
    private Weapon weapon;
    private Direction lookDirection;
    private boolean canLook;
    private boolean canStride;
    private boolean canDash;
    private boolean canJump;
    private boolean canHover;
    private boolean canRicochet;
    private boolean canCharge;
    private boolean canChangeDirection;
    private boolean canShoot;
    private boolean canChangeWeapon;
    private boolean isCharged;
    private boolean knockedBack;
    private boolean slidPlatform;
    private boolean groundedPlatform;
    private long strideStartTime;
    private long jumpStartTime;
    private long dashStartTime;
    private long hoverStartTime;
    private long ricochetStartTime;
    private long chargeStartTime;
    private long changeWeaponStartTime;
    private long recoveryStartTime;
    private float strideAcceleration;
    private float hoverTimeSeconds;
    private float aerialTakeoff;
    private int lives;
    private int ammo;
    private int health;
    public boolean leftButtonPressed;
    public boolean rightButtonPressed;
    public boolean jumpButtonPressed;
    public boolean shootButtonPressed;

    // ctor
    public GigaGal(Vector2 spawnLocation, Level level) {
        this.spawnLocation = spawnLocation;
        this.level = level;
        position = new Vector2();
        velocity = new Vector2();
        previousFramePosition = new Vector2();
        weaponList = new ArrayList<Weapon>();
        init();
    }

    public void init() {
        ammo = Constants.INITIAL_AMMO;
        health = Constants.INITIAL_HEALTH;
        lives = Constants.INITIAL_LIVES;
        weaponList.add(Weapon.NATIVE);
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
        enableRespawn();
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
                                velocity.x += Utils.absValToDirectional(Constants.GIGAGAL_STARTING_SPEED, facing); // boost lateral velocity by starting speed
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
                if (getBottom() < (ground.getTop() + Constants.MIN_GROUND_DISTANCE)
                        && getBottom() > ground.getTop() // GG's bottom is greater than ground top but less than boundary
                        && velocity.y < 0 // prevents disabling features when crossing boundary while ascending on jump
                        && ricochetStartTime == 0 // only if have not ricocheted since last grounded
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

    private void handleDirectionalInput() {
        boolean left = Gdx.input.isKeyPressed(Keys.A) || leftButtonPressed;
        boolean right = Gdx.input.isKeyPressed(Keys.S) || rightButtonPressed;
        boolean directionChanged = false;
        if (left && !right) {
            directionChanged = Utils.setDirection(this, Direction.LEFT);
        } else if (!left && right) {
            directionChanged = Utils.setDirection(this, Direction.RIGHT);
        }
        if (groundState != GroundState.AIRBORNE && lookDirection == null) {
            if (groundState != GroundState.DASHING) {
                if (left && !right || right && !left) {
                    if (directionChanged) {
                        strideStartTime = 0;
                        stand();
                    } else if (!canStride) {
                        if (strideStartTime == 0) {
                            canStride = true;
                        } else if (Utils.secondsSince(strideStartTime) > Constants.DOUBLE_TAP_SPEED) {
                            strideStartTime = 0;
                        } else {
                            canDash = true;
                        }
                    }
                } else {
                    stand();
                    canStride = false;
                    if (left && right) {
                        canShoot = false;
                        if (changeWeaponStartTime == 0) {
                            changeWeaponStartTime = TimeUtils.nanoTime();
                        }
                        changeWeapon();
                    } else {
                        canShoot = true;
                        changeWeaponStartTime = 0;
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
                if (powerup.getSubclass() == AmmoPowerup.class) {
                    ammo += Constants.POWERUP_AMMO;
                    level.setScore(level.getScore() + Constants.POWERUP_SCORE);
                    powerups.removeValue(powerup, true);
                }
                if (powerup.getSubclass() == HealthPowerup.class) {
                    health += Constants.POWERUP_HEALTH;
                    if (health > 100) {
                        health = 100;
                    }
                    level.setScore(level.getScore() + Constants.POWERUP_SCORE);
                    powerups.removeValue(powerup, true);
                }
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
                    isCharged = false;
                    recoveryStartTime = TimeUtils.nanoTime();
                    chargeStartTime = 0;
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
                            recoil(new Vector2((Utils.absValToDirectional(zoomba.getMountKnockback().x, facing)), zoomba.getMountKnockback().y));
                            damage = zoomba.getMountDamage();
                        } else {
                            recoil(new Vector2((Utils.absValToDirectional(hazard.getKnockback().x, facing)), hazard.getKnockback().y));
                        }
                    }
                    health -= damage;
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

    private void changeWeapon() {
        if (Utils.secondsSince(changeWeaponStartTime) > Constants.WEAPON_TOGGLER_DELAY) {
            if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
                canChangeWeapon = true;
            } else if (canChangeWeapon) {
                canChangeWeapon = false;
                if (weaponToggler.hasPrevious()) {
                    weapon = weaponToggler.previous();
                } else {
                    while (weaponToggler.hasNext()) {
                        weaponToggler.next();
                    }
                    weapon = weaponToggler.previous();
                }
            }
        }
    }

    private void enableShoot(Weapon weapon) {
        if (canShoot) {
            if (Gdx.input.isKeyPressed(Keys.ENTER) || shootButtonPressed) {
                if (chargeStartTime == 0) {
                    canCharge = true;
                    chargeStartTime = TimeUtils.nanoTime();
                } else if (Utils.secondsSince(chargeStartTime) > Constants.CHARGE_DURATION) {
                    isCharged = true;
                }

            } else if (canCharge) {
                ShotIntensity shotIntensity;
                int ammoUsed;
                if (isCharged) {
                    shotIntensity = ShotIntensity.CHARGED;
                } else {
                    shotIntensity = ShotIntensity.NORMAL;
                }

                if ((weapon == Weapon.NATIVE
                || ammo < 3 && shotIntensity == ShotIntensity.CHARGED)
                || ammo < 1) {
                    ammoUsed = 0;
                    weapon = Weapon.NATIVE;
                } else {
                    ammoUsed = Utils.useAmmo(shotIntensity);
                }

                shoot(shotIntensity, weapon, ammoUsed);
                chargeStartTime = 0;
                isCharged = false;
                canCharge = false;
            }
        }
    }

    public void shoot(ShotIntensity shotIntensity, Weapon weapon, int ammoUsed) {
        ammo -= ammoUsed;
        Vector2 ammoPosition = new Vector2();
        if (facing == Direction.RIGHT) {
            ammoPosition = new Vector2(
                    position.x + Constants.GIGAGAL_CANNON_OFFSET.x + 5,
                    position.y + Constants.GIGAGAL_CANNON_OFFSET.y);
        } else if (facing == Direction.LEFT) {
            ammoPosition = new Vector2(
                    position.x - Constants.GIGAGAL_CANNON_OFFSET.x - 15,
                    position.y + Constants.GIGAGAL_CANNON_OFFSET.y);
        }
        if (lookDirection == Direction.UP) {
            ammoPosition.x -= Utils.absValToDirectional(5, facing);
            ammoPosition.y += 20;
            level.spawnAmmo(ammoPosition, lookDirection, Orientation.VERTICAL, shotIntensity, weapon, true);
        } else if (lookDirection == Direction.DOWN) {
            ammoPosition.x -= Utils.absValToDirectional(10, facing);
            ammoPosition.y -= 20;
            level.spawnAmmo(ammoPosition, lookDirection, Orientation.VERTICAL, shotIntensity, weapon, true);
        } else {
            level.spawnAmmo(ammoPosition, facing, Orientation.LATERAL, shotIntensity, weapon, true);
        }
    }

    private void enableRespawn() {
        if (position.y < Constants.KILL_PLANE || health < 1) {
            health = 0;
            lives--;
            if (lives > -1) {
                respawn();
            }
        }
    }

    private void respawn() {
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
        isCharged = false;
        slidPlatform = false;
        groundedPlatform = false;
        knockedBack = false;
        canChangeWeapon = false;
        changeWeaponStartTime = 0;
        chargeStartTime = 0;
        strideStartTime = 0;
        jumpStartTime = 0;
        dashStartTime = 0;
        recoveryStartTime = TimeUtils.nanoTime();
        health = 100;
    }
    
    private void enableLook() {
        if (canLook) {
            look();
        }
    }
    
    private void look() {
        if (Gdx.input.isKeyPressed(Keys.W)) {
            lookDirection = Direction.UP;
        } else if (Gdx.input.isKeyPressed(Keys.Z)) {
            lookDirection = Direction.DOWN;
        } else {
            canLook = false;
            lookDirection = null;
        }
    }

    private void enableStride() {
        handleDirectionalInput();
        if (canStride) {
            stride();
        }
    }

    private void stride() {
        canLook = false;
        if (strideStartTime == 0) {
            groundState = GroundState.STRIDING;
            strideStartTime = TimeUtils.nanoTime();
        }
        strideAcceleration = Utils.secondsSince(strideStartTime) + Constants.GIGAGAL_STARTING_SPEED;
        velocity.x = Utils.absValToDirectional(Math.min(Constants.GIGAGAL_MAX_SPEED * strideAcceleration + Constants.GIGAGAL_STARTING_SPEED, Constants.GIGAGAL_MAX_SPEED), facing);
    }

    private void enableDash() {
        handleDirectionalInput();
        if (canDash) {
            dash();
        }
    }

    private void dash() {
        if (groundState != GroundState.DASHING) {
            groundState = GroundState.DASHING;
            dashStartTime = TimeUtils.nanoTime();
            strideStartTime = 0;
            canStride = false;
        }
        if (Utils.secondsSince(dashStartTime) < Constants.MAX_DASH_DURATION) {
            velocity.x = Utils.absValToDirectional(Constants.GIGAGAL_MAX_SPEED, facing);
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
        velocity.x += Utils.absValToDirectional(Constants.GIGAGAL_STARTING_SPEED * Constants.STRIDING_JUMP_MULTIPLIER, facing);
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
            aerialState = AerialState.HOVERING; // indicates currently hovering
            hoverStartTime = TimeUtils.nanoTime(); // begins timing hover duration
        }
        hoverTimeSeconds = Utils.secondsSince(hoverStartTime); // for comparing with max hover time
        if (hoverTimeSeconds < Constants.MAX_HOVER_DURATION) {
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
        }
        if (Utils.secondsSince(ricochetStartTime) >= Constants.RICOCHET_FRAME_DURATION) {
            facing = Utils.getOppositeDirection(facing);
            velocity.x = Utils.absValToDirectional(Constants.GIGAGAL_MAX_SPEED, facing);
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
            handleDirectionalInput();
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
    public Direction getDirection() { return facing; }
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
    public boolean isCharged() { return isCharged; }
    public Weapon getWeapon() { return weapon; }
    public List<Weapon> getWeaponList() { return weaponList; }
    public void addWeapon(Weapon weapon) { weaponToggler.add(weapon); }

    // Setters
    public void setDirection(Direction facing) { this.facing = facing; }
    public void setChargeStartTime(long chargeStartTime) { this.chargeStartTime = chargeStartTime; }
}
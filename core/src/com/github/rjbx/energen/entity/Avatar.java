package com.github.rjbx.energen.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.app.SaveData;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.InputControls;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Enums.*;
import com.github.rjbx.energen.util.Helpers;

import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class Avatar extends Entity implements Impermeable, Humanoid {

    // fields
    public final static String TAG = Avatar.class.getName();
    private static final Avatar INSTANCE = new Avatar();

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
    private Groundable touchedGround; // class-level instantiation
    private Hazardous touchedHazard;
    private Pliable carriedGround;
    private ShotIntensity shotIntensity;
    private BladeState bladeState;
    private Energy energy;
    private Color energyColor;
    private List<Energy> energyList; // class-level instantiation
    private ListIterator<Energy> energyToggler; // class-level instantiation
    private List<Upgrade> upgradeList;
    private boolean canShoot;
    private boolean canDispatch;
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
    private boolean canMove;
    private boolean canFlip;
    private boolean canRush;
    private boolean canCut;
    private boolean autoblast;
    private boolean supercharged;
    private boolean prioritized;
    private long chargeStartTime;
    private long shootStartTime;
    private long activeStartTime;
    private long lookStartTime;
    private long fallStartTime;
    private long jumpStartTime;
    private long dashStartTime;
    private long hoverStartTime;
    private long rappelStartTime;
    private long peerStartTime;
    private long strideStartTime;
    private long recoveryStartTime;
    private long swipeStartTime;
    private long climbStartTime;
    private float superchargeStartTime;
    private float chargeTimeSeconds;
    private float lookTimeSeconds;
    private float dashTimeSeconds;
    private float fallTimeSeconds;
    private float hoverTimeSeconds;
    private float swipeTimeSeconds;
    private float strideTimeSeconds;
    private float strideSpeed;
    private float strideAcceleration;
    private float turboMultiplier;
    private float ammoMultiplier;
    private float healthMultiplier;
    private float strideMultiplier;
    private float jumpMultiplier;
    private float startTurbo;
    private float turbo;
    private float fallLimit;
    private float payload;
    private boolean canSlump;
    private float peerQuadrant;
    private float ammo;
    private float health;
    private int lives;
    private int[] gems = {0,0,0};
    private InputControls inputControls;

    // cannot be subclassed
    private Avatar() {}

    public static Avatar getInstance() {
        return INSTANCE;
    }

    public void create() {
        position = new Vector2();
        spawnPosition = new Vector2();
        previousFramePosition = new Vector2();
        chaseCamPosition = new Vector3();
        velocity = new Vector2();
        energyList = new ArrayList<Energy>();
        energyToggler = energyList.listIterator();
        upgradeList = new ArrayList<Upgrade>();
        height = Constants.AVATAR_HEIGHT;
        eyeHeight = Constants.AVATAR_EYE_HEIGHT;
        headRadius = Constants.AVATAR_HEAD_RADIUS;
        width = Constants.AVATAR_STANCE_WIDTH;
        halfWidth = width / 2;
        lives = Constants.INITIAL_LIVES;
        turboMultiplier = 1;
        ammoMultiplier = 1;
        healthMultiplier = 1;
        strideMultiplier = 1;
        jumpMultiplier = 1;
        String savedEnergies = SaveData.getEnergies();
        if (!savedEnergies.equals(Energy.NATIVE.name())) {
            List<String> savedEnergiesList = Arrays.asList(savedEnergies.split(", "));
            for (String energiestring : savedEnergiesList) {
                addEnergy(Energy.valueOf(energiestring));
            }
        } else {
            addEnergy(Energy.NATIVE);
            addEnergy(Energy.ORE);
            addEnergy(Energy.PLASMA);
            addEnergy(Energy.GAS);
            addEnergy(Energy.LIQUID);
            addEnergy(Energy.SOLID);
            addEnergy(Energy.ANTIMATTER);
            addEnergy(Energy.HYBRID);
            energy = Energy.GAS;
        }
        String[] savedSuit = SaveData.getSuit().split(";");
        energy = Energy.valueOf(savedSuit[0]);
        supercharged = Integer.valueOf(savedSuit[1]) == 1;
        energyColor = energy.theme().color();

        if (supercharged) supercharge();

        String savedUpgrades = SaveData.getUpgrades();
        if (!savedUpgrades.equals(Energy.NATIVE.name())) {
            List<String> savedUpgradesList = Arrays.asList(savedUpgrades.split(", "));
            for (String upgradeString : savedUpgradesList) {
                addUpgrade(Upgrade.valueOf(upgradeString));
            }
        } else {
            addUpgrade(Upgrade.NONE);
        }
        dispenseUpgrades();
    }

    public Avatar safeClone() {
        Avatar clone = Avatar.getInstance();
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    public void respawn() {
        position.set(spawnPosition);
        fallLimit = position.y - Constants.FALL_LIMIT;
        chaseCamPosition.set(position, 0);
        left = position.x - halfWidth;
        right = position.x + halfWidth;
        top = position.y + headRadius;
        bottom = position.y - eyeHeight;
        bounds = new Rectangle(left, bottom, width, height);
        velocity.setZero();
        directionX = Enums.Direction.RIGHT;
        directionY = Direction.UP;
        action = Enums.Action.FALLING;
        groundState = Enums.GroundState.AIRBORNE;
        ammo = Constants.INITIAL_AMMO;
        health = Constants.INITIAL_HEALTH;
        turbo = Constants.MAX_TURBO;
        shotIntensity = ShotIntensity.NORMAL;
        bladeState = BladeState.RETRACTED;
        startTurbo = turbo;
        touchedGround = null;
        touchedHazard = null;
        carriedGround = null;
        canClimb = true;
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
        canDispatch = false;
        canSink = false;
        canMove = false;
        canFlip = false;
        canRush = false;
        canCut = false;
        canSlump = false;
        chargeStartTime = 0;
        strideStartTime = 0;
        peerStartTime = 0;
        jumpStartTime = 0;
        fallStartTime = 0;
        dashStartTime = 0;
        swipeStartTime = 0;
        shootStartTime = 0;
        lookStartTime = 0;
        hoverStartTime = 0;
        rappelStartTime = 0;
        chargeTimeSeconds = 0f;
        lookTimeSeconds = 0f;
        dashTimeSeconds = 0f;
        fallTimeSeconds = 0f;
        hoverTimeSeconds = 0f;
        swipeTimeSeconds = 0f;
        strideTimeSeconds = 0f;
        strideSpeed = 0f;
        strideAcceleration = 0f;
        payload = 0f;
        peerQuadrant = 0f;
        inputControls = InputControls.getInstance();
        activeStartTime = TimeUtils.nanoTime();
        recoveryStartTime = TimeUtils.nanoTime();
        if (supercharged) supercharge();
    }

    public void update(float delta) {
        // abilities
        if (groundState == GroundState.PLANTED) {
            if (action == Action.STANDING) {
                stand();
                enableStride();
                enableDash();
                enableClimb(); // must come before jump (for now)
                enableJump();
                enableShoot(energy);
                enableSwipe();
            } else if (action == Action.STRIDING) {
                enableStride();
                enableDash();
                enableJump();
                enableShoot(energy);
                enableSwipe();
            } else if (action == Action.CLIMBING) {
                enableClimb();
                enableShoot(energy);
                enableSwipe();
            } else if (action == Action.DASHING) {
                enableDash();
                enableJump();
                enableShoot(energy);
            }
        } else if (groundState == GroundState.AIRBORNE) {
            velocity.y -= Constants.GRAVITY;
            if (action == Action.FALLING) {
                fall();
                enableClimb();
                enableHover();
                enableRappel();
                enableShoot(energy);
                enableSwipe();
            } else if (action == Action.HOVERING) {
                enableHover();
                enableRappel();
                enableClimb();
                enableShoot(energy);
                enableSwipe();
            } else if (action == Action.RAPPELLING) {
                enableJump();
                enableRappel();
                enableClimb();
                enableShoot(energy);
                enableSwipe();
            } else if (action == Action.RECOILING) {
                enableRappel();
                enableShoot(energy);
                enableSwipe();
            }
        }
//
//        if (touchedGround != null)
//        Gdx.app.log(TAG + "3", touchedGround.getClass().toString());
    }

    private void enableSwipe() {
        if (getMoveStatus()) {
            canRush = false;
            canCut = false;
            bladeState = BladeState.RETRACTED;
            return;
        }
        if (!canRush && !canCut && (groundState == GroundState.AIRBORNE || action == Action.CLIMBING) && (inputControls.downButtonPressed || inputControls.upButtonPressed)) {
            if (inputControls.jumpButtonJustPressed && action != Action.RAPPELLING) {
                lookStartTime = TimeUtils.nanoTime();
                canFlip = true;
                bladeState = BladeState.FLIP;
            }
        } else if (canFlip) { // manual deactivation by shoot button release
            AssetManager.getInstance().getSoundAssets().getBlastSound(energy).stop();
            swipeStartTime = 0;
            swipeTimeSeconds = 0;
            canFlip = false;
            bladeState = BladeState.RETRACTED;
        }

        if (!canFlip && groundState == GroundState.PLANTED && action != Action.CLIMBING && ((inputControls.downButtonPressed || inputControls.upButtonPressed)
                || (action == Action.DASHING && chargeStartTime > Constants.BLAST_CHARGE_DURATION && inputControls.jumpButtonPressed))) {
            if (inputControls.leftButtonPressed || inputControls.rightButtonPressed) {
                if (action != Action.DASHING) {
                    stand();
                }
                lookStartTime = TimeUtils.nanoTime();
                canRush = true;
                bladeState = BladeState.RUSH;
            }
        } else if (canRush) {  // manual deactivation by dash interrupt
            AssetManager.getInstance().getSoundAssets().getBlastSound(energy).stop();
            swipeStartTime = 0;
            swipeTimeSeconds = 0;
            canRush = false;
            bladeState = BladeState.RETRACTED;
        }

        if (!canFlip && !canRush && groundState == GroundState.PLANTED && action != Action.CLIMBING && (inputControls.downButtonPressed || inputControls.upButtonPressed)) {
            if (inputControls.jumpButtonPressed) {
                lookStartTime = TimeUtils.nanoTime();
                canCut = true;
                bladeState = BladeState.CUT;
            }
        } else if (canCut) {
            AssetManager.getInstance().getSoundAssets().getBlastSound(energy).stop();
            swipeStartTime = 0;
            swipeTimeSeconds = 0;
            canCut = false;
            bladeState = BladeState.RETRACTED;
        }
        swipe();
    }

    private void swipe() {
        if (canFlip) {
            if (swipeStartTime == 0) {
                swipeStartTime = TimeUtils.nanoTime();
                swipeTimeSeconds = 0;
                    if (directionY == Direction.UP) {if (velocity.x < Constants.AVATAR_MAX_SPEED) {
                        velocity.x -= Helpers.speedToVelocity(Constants.AVATAR_MAX_SPEED / 2.25f, directionX, Orientation.X);
                    }
                    if (velocity.y < Constants.AVATAR_MAX_SPEED) {
                        velocity.y += Constants.AVATAR_MAX_SPEED / 2.25f;
                    }
                } else if (directionY == Direction.DOWN) {
                    if (velocity.x < Constants.AVATAR_MAX_SPEED) {
                        velocity.x += Helpers.speedToVelocity(Constants.AVATAR_MAX_SPEED / 2.25f, directionX, Orientation.X);
                    }
                    if (velocity.y < Constants.AVATAR_MAX_SPEED) {
                        velocity.y += Constants.AVATAR_MAX_SPEED / 4.5f;
                    }
                }
            } else if (swipeTimeSeconds < Constants.FLIPSWIPE_FRAME_DURATION * 5) {
                AssetManager.getInstance().getSoundAssets().getBlastSound(energy).play();
                swipeTimeSeconds = Helpers.secondsSince(swipeStartTime);
                if (action != Action.HOVERING && action != Action.RECOILING) turbo -= Constants.FALL_TURBO_INCREMENT;
            } else { // auto deactivation when animation completes
                AssetManager.getInstance().getSoundAssets().getBlastSound(energy).stop();
                swipeStartTime = 0;
                swipeTimeSeconds = 0;
                canFlip = false;
                bladeState = BladeState.RETRACTED;
                if (inputControls.jumpButtonPressed && chargeTimeSeconds > Constants.BLAST_CHARGE_DURATION) {
                    shoot(shotIntensity, energy, Helpers.useAmmo(shotIntensity));
                }
            }
        }

        if (canRush) {
            if (swipeStartTime == 0) {
                swipeStartTime = TimeUtils.nanoTime();
                swipeTimeSeconds = 0;
            } else if (swipeTimeSeconds < Constants.FLIPSWIPE_FRAME_DURATION * 3) {
                AssetManager.getInstance().getSoundAssets().getBlastSound(energy).play();
                swipeTimeSeconds = Helpers.secondsSince(swipeStartTime);
            } else { // auto deactivation when animation completes
                AssetManager.getInstance().getSoundAssets().getBlastSound(energy).stop();
                swipeStartTime = 0;
                swipeTimeSeconds = 0;
                canRush = false;
                bladeState = BladeState.RETRACTED;
                if (chargeTimeSeconds > Constants.BLAST_CHARGE_DURATION && action == Action.DASHING) {
                    shoot(shotIntensity, energy, Helpers.useAmmo(shotIntensity));
                }
                canDash = false;
                stand();
            }
        }

        if (canCut) {
            if (swipeStartTime == 0) {
                swipeStartTime = TimeUtils.nanoTime();
                swipeTimeSeconds = 0;
            } else if (swipeTimeSeconds < Constants.FLIPSWIPE_FRAME_DURATION * 3) {
                AssetManager.getInstance().getSoundAssets().getBlastSound(energy).play();
                swipeTimeSeconds = Helpers.secondsSince(swipeStartTime);
            } else { // auto deactivation when animation completes
                AssetManager.getInstance().getSoundAssets().getBlastSound(energy).stop();
                swipeStartTime = 0;
                swipeTimeSeconds = 0;
                canCut = false;
                bladeState = BladeState.RETRACTED;
                if (chargeTimeSeconds > Constants.BLAST_CHARGE_DURATION && inputControls.jumpButtonPressed) {
                    shoot(shotIntensity, energy, Helpers.useAmmo(shotIntensity));
                }
                canDash = false;
                stand();
            }
        }
    }

    private void setCollisionBounds() {
        left = position.x - halfWidth;
        right = position.x + halfWidth;
        top = position.y + headRadius;
        bottom = position.y - eyeHeight;
        bounds = new Rectangle(left, bottom, width, height);
    }

    public void updatePosition(float delta) {
        // positioning
        previousFramePosition.set(position);
//
//        if (touchedGround != null)
//            Gdx.app.log(TAG + "1", touchedGround.getClass().toString());
        position.mulAdd(velocity, delta);
//
//        if (touchedGround != null)
//            Gdx.app.log(TAG + "2", touchedGround.getClass().toString());

        setCollisionBounds();
        detectInput();
    }

    public void touchAllGrounds(Array<Ground> grounds) {
        for (Ground g : grounds) {
            touchGround(g);
        }
        untouchGround();
    }

    public void touchGround(Groundable g) {
        if (Helpers.overlapsPhysicalObject(this, g)) {// if overlapping ground boundaries
            if (g.isDense()) { // for dense grounds: apply side, bottom collision and top collisionouchGroundBottom(ground);
                touchGroundBottom(g);
                touchGroundSide(g);
                touchGroundTop(g);
            } else { // for non-dense grounds:
                // additional ground collision instructions specific to certain types of grounds
                prioritized = false;
                if (g instanceof Climbable) {
                    prioritized = true;
                    if (!(touchedGround != null && touchedGround.isDense() && touchedGround.getTop() == g.getTop())) { // prevents flickering canclimb state
                        canCling = true;
                    }
                    if (!(!canClimb && groundState == GroundState.PLANTED && touchedGround instanceof Skateable) // prevents from overriding handling of simultaneously touched skateable ground i.e. overriding ground physics
                            && (!(groundState == GroundState.AIRBORNE && touchedGround instanceof Rappelable))) { // prevents from overriding handling of simultaneously touched rappelable ground i.e. for rappel position reset)
                        if (!(g instanceof Unsteady) || (touchedGround == null || (!(touchedGround != null && !touchedGround.equals(g) && touchedGround.isDense() && action != Action.CLIMBING)))) {
                            carriedGround = null;
                            if (canClimb) {
                                touchedGround = g; // saves for untouchground where condition within touchgroundtop unmet
                            }
                            if (canClimb && !inputControls.jumpButtonPressed && action == Action.STANDING) {
                                //   Gdx.app.log(TAG, "???");
                                canJump = true;
                                jump();
                            }
                        }
                    }
                    if (!(canClimb && directionY == Direction.DOWN)) { // ignore side and bottom collision always and top collision when can climb and looking downward
                        if (action != Action.FALLING // prevents from immediately calling stand after calling jump/fall when touching climbable and non-climbable simultaneously
                                || (fallStartTime != 0 && (Helpers.secondsSince(fallStartTime) > .01f))) { // permits call to stand when falling and touching climbable and non-climbable simultaneously and not having immediately called jump/fall
                            if (g instanceof Unsteady) {
                                if (groundState == GroundState.PLANTED) {
                                    if (action != Action.CLIMBING) { // prevents from immediately calling stand after calling jump/fall when touching climbable and non-climbable simultaneously
                                        if (action == Action.STANDING) {
                                            if (canClimb) {
                                                setAtopGround(g);
                                            }
                                        } else if (touchedGround == null || (!touchedGround.isDense() && Helpers.encompassedBetweenFourSides(position, getWidth() / 2, getHeight() / 2, touchedGround.getLeft(), touchedGround.getRight(), touchedGround.getBottom(), touchedGround.getTop()))) {
                                            fall();
                                        }
                                    }
                                }
                            } else {
                                touchGroundTop(g); // prevents descending below top when on non dense, non sinkable
                            }
                        }
                    }
                    if (action == Action.CLIMBING) {
                        velocity.y = 0; // halts progress when no directional input
                    }
                } else if (g instanceof Pourous) {
                    setAtopGround(g); // when any kind of collision detected and not only when breaking plane of ground.top
                    if (g.getBottom() < bottom - 5) {
                        canCling = false;
                        canClimb = false;
                        lookStartTime = 0;
                        lookTimeSeconds = 0;
                    }
                    canSink = true;
                    canDash = false;
//                    canHover = false;
                    prioritized = true;
                } else if (!(g instanceof Pliable) || !(canClimb && directionY == Direction.UP)) { // canclimb set to false from fall to prevent ignoring top collision after initiating climb, holding jump and passing through ledge top
                    if (!(canClimb && directionY == Direction.DOWN)) { /// ignore side and bottom collision always and top collision when can climb and looking downward
                        if (g instanceof Brick) { // prevents setting atop non-dense bricks
                            touchedGround = g;
                        } else {
                            touchGroundTop(g); // prevents descending below top when on non dense, non sinkable
                        }
                    }
                }
            }
            // if below minimum ground distance while descending excluding post-rappel, disable rappel and hover
            // caution when crossing plane between ground top and minimum hover height / ground distance
            // cannons, which inherit ground, can be mounted along sides of grounds causing accidental plane breakage
            if (getBottom() < (g.getTop() + Constants.MIN_GROUND_DISTANCE)
                    && getBottom() > g.getTop() // GG's bottom is greater than ground top but less than boundary
                    && velocity.y < 0 // prevents disabling features when crossing boundary while ascending on jump
                    && rappelStartTime == 0 // only if have not rappeled since last grounded
                    && !(g instanceof Cannon)) { // only if ground is not instance of cannon
                canRappel = false; // disables rappel
                canHover = false; // disables hover
            }
            if (g instanceof Ground && g instanceof Hazardous) {
                touchHazard((Hazardous) g);
            }
            if (g instanceof Replenishing) {
                touchPowerup((Replenishing) g);
            }
        }
    }

    private void touchGroundSide(Groundable g) {
        // ignores case where simultaneously touching two separate grounds with same top position to prevent interrupting stride
        if (!(touchedGround != null && !touchedGround.equals(g) && touchedGround.getTop() == g.getTop() && action != Action.CLIMBING)) {
            // if during previous frame was not, while currently is, between ground left and right sides
            if (!Helpers.overlapsBetweenTwoSides(previousFramePosition.x, getHalfWidth(), g.getLeft(), g.getRight())) {
                // only when not grounded and not recoiling
                if (groundState != GroundState.PLANTED) {
                    // if x velocity (magnitude, without concern for direction) greater than one third max speed,
                    // boost x velocity by starting speed, enable rappel, verify rappelling ground and capture rappelling ground boundaries
                    if ((Math.abs(velocity.x) >= Constants.AVATAR_STARTING_SPEED / 8) || g instanceof Hazard) {
                        // if already rappelling, halt x progression
                        if (action != Action.RAPPELLING) {
                            if (g instanceof Rappelable) {
                                canRappel = true; // enable rappel
                            }
                            touchedGround = g;
                            fallLimit = touchedGround.getBottom() - Constants.FALL_LIMIT;
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
                } else { // only when planted
                    if (Math.abs(getBottom() - g.getTop()) > 1) {
                        strideSpeed = 0;
                        velocity.x = 0;
                    }
                    if (g instanceof Pliable && getBottom() == g.getBottom()) {
                        canMove = true;
                    }
                    if (!(g instanceof Propelling) && action == Action.DASHING && !(g instanceof Armored)) {
                        stand(); // deactivates dash when bumping ground side
                    }
                }
                if ((!(g instanceof Propelling && (Math.abs(getBottom() - g.getTop()) <= 1)))
                        && !(g instanceof Skateable && (Math.abs(getBottom() - g.getTop()) <= 1))
                        && !(g instanceof Hazardous && (Math.abs(getBottom() - g.getTop()) <= 1))) {
                    // if contact with ground sides detected without concern for ground state (either grounded or airborne),
                    // reset stride acceleration, disable stride and dash, and set avatar at ground side
                    if (action != Action.STRIDING || action != Action.DASHING) {
                        strideStartTime = 0; // reset stride acceleration
                    }
                    canStride = false; // disable stride
                    canDash = false; // disable dash
                    position.x = previousFramePosition.x;
                }
            } else { // when both position and previous position overlap ground side edge

                float yTestPosition = position.y;
                if (!(g instanceof Pliable) || (action == Action.FALLING && !((Pliable) g).isBeingCarried() && (!(((Pliable) g).isAtopMovingGround() && ((Pliable) g).getMovingGround() instanceof Pliable && g.getPosition().dst(position) < getHeight() / 2)))) {
                    if (Helpers.betweenTwoValues(yTestPosition, g.getBottom(), g.getTop())) { // when test position is between ground top and bottom (to prevent resetting to grounds simultaneously planted upon)
                        if (Math.abs(position.x - g.getLeft()) < Math.abs(position.x - g.getRight())) {
                            position.x = g.getLeft() - getHalfWidth() - 1; // reset position to ground side edge
                        } else {
                            position.x = g.getRight() + getHalfWidth() + 1; // reset position to ground side edge
                        }
                    }
                }
            }
        } else {
            touchedGround = g;
        }
    }

    private void touchGroundBottom(Groundable g) {
        // ignores case where simultaneously touching two separate dense grounds (since side collision does not apply) with same side position to prevent interrupting fall
        if (!(touchedGround != null && !touchedGround.equals(g) && g.isDense() && touchedGround.isDense()
                && ((touchedGround.getLeft() == g.getLeft() && position.x < touchedGround.getPosition().x) || (touchedGround.getRight() == g.getRight() && position.x > touchedGround.getPosition().x)))) {
            // if contact with ground bottom detected, halts upward progression and set avatar at ground bottom
            if ((previousFramePosition.y + Constants.AVATAR_HEAD_RADIUS) < g.getBottom() + 1) {
                velocity.y = 0; // prevents from ascending above ground bottom
                if (groundState == GroundState.AIRBORNE) { // prevents fall when striding against ground bottom positioned at height distance from ground atop
                    fall(); // descend from point of contact with ground bottom
                    if (!(g instanceof Moving && ((Moving) g).getVelocity().y < 0)) { // prevents from being pushed below ground
                        position.y = g.getBottom() - Constants.AVATAR_HEAD_RADIUS;  // sets avatar at ground bottom
                    }
                } else if (action == Action.CLIMBING) { // prevents from disengaging climb
                    canCling = true;
                    canClimb = true;
                    action = Action.CLIMBING;
                    groundState = GroundState.PLANTED;
                    if (!(g instanceof Moving && ((Moving) g).getVelocity().y < 0)) { // prevents from being pushed below ground
                        position.y = g.getBottom() - Constants.AVATAR_HEAD_RADIUS;  // sets avatar at ground bottom
                    }
                }
                canDash = false;
            }
        }
    }

    // applicable to all dense grounds as well as non-sinkables when not climbing downward
    private void touchGroundTop(Groundable g) {
        if (!(touchedGround != null && !touchedGround.equals(g) && touchedGround.isDense() && g.isDense()
                && ((touchedGround.getLeft() == g.getLeft() && position.x < touchedGround.getPosition().x) || (touchedGround.getRight() == g.getRight() && position.x > touchedGround.getPosition().x)))) {
            // if contact with ground top detected, halt downward progression and set avatar atop ground
            if (previousFramePosition.y - Constants.AVATAR_EYE_HEIGHT >= g.getTop() - 2) { // and not simultaneously touching two different grounds (prevents stand which interrupts striding atop)
                if ((Helpers.overlapsBetweenTwoSides(position.x, halfWidth, g.getLeft() + 1, g.getRight() - 1)
                        || action != Action.FALLING || g instanceof Aerial) // prevents interrupting fall when inputting x directional against and overlapping two separate ground side
                        && !(action == Action.RAPPELLING && g instanceof Pliable)) { // prevents interrupting rappel down stacked moving pliables
                    if (!((touchedGround instanceof Moving && ((Moving) touchedGround).getVelocity().y != 0) || (g instanceof Moving && ((Moving) g).getVelocity().y != 0)) && (action != Action.CLIMBING || getBottom() <= g.getTop())) {
                        velocity.y = 0; // velocity reset for climbing from touchground()
                        position.y = g.getTop() + Constants.AVATAR_EYE_HEIGHT; // sets avatar atop ground
                    }
                    setAtopGround(g); // basic ground top collision instructions common to all types of grounds
                    // additional ground top collision instructions specific to certain types of grounds; touchedground instance checking handles null assignment from canclimb-jump-fall sequence initiated through setatopground
                    if (touchedGround instanceof Moving) {
                        Moving moving = (Moving) g;
                        position.y = g.getTop() + Constants.AVATAR_EYE_HEIGHT;
                        velocity.x = ((Moving) g).getVelocity().x;
                        velocity.y = ((Moving) g).getVelocity().y;
//                        Gdx.app.log(TAG, position.toString() + velocity.toString() + g.getPosition() + ((Moving) g).getVelocity());
                        if (moving instanceof Pliable) {
                            if (((Pliable) moving).isAtopMovingGround() && (touchedGround == null || !touchedGround.equals(((Pliable) moving).getMovingGround()))) { // atop pliable which is atop moving ground and not simultaneously touching both
                                Pliable pliable = (Pliable) moving;
                                if (!pliable.isBeingCarried() && directionY == Direction.DOWN && lookStartTime != 0) {
                                    if (inputControls.shootButtonJustPressed) {
                                        fall();
                                    }
                                }
                            }
                            if (inputControls.downButtonPressed) {
                                canMove = true;
                            }
                        }
                    }
                    if (touchedGround instanceof Reboundable) {
                        if (!(g instanceof Pliable && ((Pliable) g).isBeingCarried() && ((Pliable) g).getCarrier() == this)) {
                            canClimb = false;
                            canCling = false;
                        }
                    }
                    if (touchedGround instanceof Destructible) {
                        if (((Destructible) g).getHealth() < 1) {
                            fall();
                        }
                    }
                }
            }
        } else {
            touchedGround = g;
        }
    }

    // basic ground top collision instructions; applicable to sinkables even when previousframe.x < ground.top
    private void setAtopGround(Groundable g) {
        touchedGround = g;
        fallLimit = touchedGround.getBottom() - Constants.FALL_LIMIT;
        hoverStartTime = 0;
        rappelStartTime = 0;
        canMove = false;
        canRappel = false;
        canLook = true;
        canHover = false;
        if (groundState == GroundState.AIRBORNE) {
            stand(); // in each frame all grounds save for skateable rely upon this call to switch action from airborne
            lookStartTime = 0;
        } else if (canClimb && !inputControls.jumpButtonPressed && action == Action.STANDING) {
            canJump = true;
            jump();
        } else if (action == Action.CLIMBING && !(g instanceof Climbable)) {
            stand();
        }
        if (action == Action.STANDING) {
            if ((canClimb && (g instanceof Climbable) && (touchedGround == null || !(touchedGround instanceof Climbable)))) {
                canClimb = false;  // prevents maintaining canclimb state when previously but no longer overlapping dense, nondense and climbable grounds
            } else if (!canClimb && (g instanceof Climbable || (touchedGround != null && touchedGround instanceof Climbable)) && (touchedGround.equals(g) && touchedGround.getTop() != g.getTop())) {
                canClimb = true;  // prevents setting canclimb to false when overlapping dense, nondense and climbable grounds
            }
            if (!(g instanceof Climbable || touchedGround instanceof Climbable)) {
//                canCling = false;
            }
        }
    }

    public void untouchGround() {
        if (touchedGround != null) {
            if (!Helpers.overlapsPhysicalObject(this, touchedGround)) {
                if (getBottom() > touchedGround.getTop() || getTop() < touchedGround.getBottom()) {
                    if (action == Action.RAPPELLING) {
                        velocity.x = 0;
                    }
                    fall();
                } else if (!Helpers.overlapsBetweenTwoSides(position.x, getHalfWidth(), touchedGround.getLeft(), touchedGround.getRight())) {
                    canSink = false;
                    lookTimeSeconds = 0;
                    lookStartTime = 0;
                    if (action != Action.RAPPELLING && action != Action.CLIMBING && action != Action.HOVERING && action != Action.STRIDING) {
                        fall();
                    } else {
                        canCling = false;
                        canClimb = false;
                    }
                } else if (touchedGround instanceof Destructible) {
                    Destructible destructible = (Destructible) touchedGround;
                    if (destructible.getHealth() < 1) {
                        fall();
                    }
                }
                canMove = false;
                canRappel = false;
                touchedGround = null; // after handling touchedground conditions above
            }
        } else if (action == Action.STANDING || action == Action.STRIDING || action == Action.CLIMBING) { // if no ground detected and suspended midair (prevents climb after crossing climbable plane)
            fall();
        }
    }

    // detects contact with enemy (change aerial & ground state to recoil until grounded)
    public void touchAllHazards(Array<Hazard> hazards) {
        touchedHazard = null;
        if (Helpers.secondsSince(peerStartTime) > 0.5f) {
            peerQuadrant = 1;
            canPeer = false;
        }
        for (Hazard h : hazards) {
            if (!(h instanceof Projectile && ((Projectile) h).getSource() instanceof Avatar)
                    && !(h instanceof Indestructible && h instanceof Convertible && ((Convertible) h).isConverted())) {
                if (Helpers.overlapsPhysicalObject(this, h)) {
                    touchHazard(h);
                } else if (h instanceof Moving) {
                    setPeerTarget(h, 1);
                }
            }
        }
    }

    public void touchHazard(Hazardous h) {
        chaseCamPosition.set(position, 0);
        if (h instanceof Groundable) {
            if (h instanceof Zoomba) {
                Zoomba zoomba = (Zoomba) h;
                if (bounds.overlaps(zoomba.getHazardBounds())) {
                    touchedHazard = h;
                    recoil(h.getKnockback(), h);
                }
                touchGround(zoomba);
            } else if (h instanceof Swoopa) {
                if (getBottom() >= h.getPosition().y && Helpers.betweenTwoValues(position.x, h.getPosition().x - Constants.SWOOPA_COLLISION_WIDTH, h.getPosition().x + Constants.SWOOPA_COLLISION_HEIGHT)) {
                    touchGroundTop((Swoopa) h);
                } else {
                    touchedHazard = h;
                    recoil(h.getKnockback(), h);
                }
            } else if (h instanceof Armored && ((Armored) h).isVulnerable()) {
                if (h instanceof Bladed && Helpers.secondsSince(((Armored) h).getStartTime()) > (((Armored) h).getRecoverySpeed() - Constants.FLIPSWIPE_FRAME_DURATION * 6)) {
                    for (int i = 0; i < ((Bladed) h).getEquippedRegions().size; i++) {
                        if (!((directionX == ((Bladed) h).getEquippedRegions().get(i) && Helpers.betweenTwoValues(position.y, h.getPosition().y - getHeight() / 2, h.getPosition().y + getHeight() / 2))
                        || (((position.y >= h.getTop() && ((Bladed) h).getEquippedRegions().get(i) == Direction.DOWN) || (position.y <= h.getBottom() && ((Bladed) h).getEquippedRegions().get(i) == Direction.UP)) && Helpers.betweenTwoValues(position.x, h.getPosition().x - getHalfWidth(), h.getPosition().x + getHalfWidth())))) {
                            touchedHazard = h;
                            recoil(h.getKnockback(), h);
                        }
                    }
                } else {
                    touchGround((Groundable) h);
                }
            } else if (!h.getKnockback().equals(Vector2.Zero)) {
                touchedHazard = h;
                recoil(h.getKnockback(), h);
            }
        } else {
            touchedHazard = h;
            recoil(h.getKnockback(), h);
        }
    }

   /* public void touchAllPowerups(Array<Powerup> powerups) {
        for (Powerup p : powerups) {
            Rectangle bounds = new Rectangle(p.getLeft(), p.getBottom(), p.getWidth(), p.getHeight());
            if (getConvertBounds().overlaps(bounds)) {
                touchPowerup(p);
            } else {
                setPeerTarget(p, 2);
            }
        }
        if (turbo > Constants.MAX_TURBO) {
            turbo = Constants.MAX_TURBO;
        }
    }
*/
    public void touchPowerup(Replenishing r) {
        switch(r.getType()) {
            case AMMO:
                AssetManager.getInstance().getSoundAssets().ammo.play();
                ammo += Constants.POWERUP_AMMO;
                if (ammo > Constants.MAX_AMMO) ammo = Constants.MAX_AMMO;
                break;
            case HEALTH:
                if (r instanceof Powerup) {
                    AssetManager.getInstance().getSoundAssets().health.play();
                    health += Constants.POWERUP_HEALTH;
                } else if (r instanceof Pod) {
                    if (touchedGround == r) health += .1f;
                }
                if (health > Constants.MAX_HEALTH) health = Constants.MAX_HEALTH;
                break;
            case TURBO:
                AssetManager.getInstance().getSoundAssets().turbo.play();
                turbo = Constants.POWERUP_TURBO;
                if (action == Action.HOVERING) {
                    hoverStartTime = TimeUtils.nanoTime();
                }
                if (action == Action.DASHING) {
                    dashStartTime = TimeUtils.nanoTime();
                }
                if (turbo > Constants.MAX_TURBO) turbo = Constants.MAX_TURBO;
                break;
            case LIFE:
                AssetManager.getInstance().getSoundAssets().life.play();
                if (lives < Constants.MAX_LIVES) lives++;
                break;
            case CANNON:
                AssetManager.getInstance().getSoundAssets().cannon.play();
                ammo += Constants.POWERUP_AMMO;
                shotIntensity = ShotIntensity.BLAST;
                autoblast = true;
                break;
            case SUPER:
                AssetManager.getInstance().getSoundAssets().health.play();
                AssetManager.getInstance().getSoundAssets().health.play();
                AssetManager.getInstance().getSoundAssets().turbo.play();
                AssetManager.getInstance().getSoundAssets().life.play();
                AssetManager.getInstance().getSoundAssets().cannon.play();
                supercharge();
                break;
            case GEM:
                gems[((Powerup) r).getGemType().ordinal()]++;
                Gdx.app.log(TAG, gems[((Powerup) r).getGemType().ordinal()] + "");
        }
    }

    private void handleXInputs() {
        boolean left = inputControls.leftButtonPressed;
        boolean right = inputControls.rightButtonPressed;
        boolean directionChanged = false;
        boolean inputtingX = ((left || right) && !(left && right));
        if (inputtingX && lookStartTime == 0) {
            if (left && !right && !canRush) {
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
                } else if (inputtingX) {
                    if (action != Action.DASHING) {
                        if (!canStride) {
                            if (strideStartTime == 0) {
                                canStride = true;
                            } else if (Helpers.secondsSince(strideStartTime) > Constants.DOUBLE_TAP_SPEED) {
                                strideStartTime = 0;
                            } else if (!canSink && !(canRush && touchedGround instanceof Moving && ((Moving) touchedGround).getVelocity().y != 0)) {
                                canDash = true;
                            } else {
                                canDash = false;
                            }
                        }
                    }
                } else { // not inputting x disables dash
                    stand();
                    canStride = false;
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
        boolean inputtingY = (up || down);
        if (inputtingY) {
            if (down && !up) {
                directionChanged = Helpers.changeDirection(this, Direction.DOWN, Orientation.Y);
            } else if (!down) { // if up
                directionChanged = Helpers.changeDirection(this, Direction.UP, Orientation.Y);
            }
            if (directionY == Direction.DOWN) {
                if (canSink) {
                    velocity.y *= 5;
                }
            }
            if (canLook && !canClimb) {
                canStride = false;
                if (!canRappel && !canHurdle && !getSwipeStatus()) { // prevents accidental toggle due to simultaneous jump and directional press
                    if (((inputControls.downButtonJustPressed && inputControls.upButtonPressed) || (inputControls.upButtonJustPressed && inputControls.downButtonPressed)) && inputControls.shootButtonPressed) {
                        lookStartTime = 0;
                        toggleEnergy(directionY);
                        chargeStartTime = 0;
                        chargeTimeSeconds = 0;
                        canShoot = false; // prevents discharge only if releasing shoot before y input due to stand() condition
                    }
                }
                look(); // also sets chase cam
            }
        } else if (action == Action.STANDING || action == Action.CLIMBING) { // if neither up nor down pressed (and either standing or climbing)
            resetChaseCamPosition();
        } else { // if neither standing nor climbing nor inputting y
            chaseCamPosition.set(position, 0);
            if (action != Action.RAPPELLING) lookStartTime = 0;
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
        if (touchedGround instanceof Pourous) {
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
        } else if (touchedGround instanceof Propelling) {
            velocity.x = 0;
            velocity.x += Helpers.speedToVelocity(Constants.TREADMILL_SPEED, ((Propelling) touchedGround).getDirectionX(), Orientation.X);
        } else if (touchedGround == null || !(touchedGround instanceof Moving) && getBottom() >= touchedGround.getTop()) {
            velocity.x = 0;
        }
        fallStartTime = 0;
        if (action != Action.RECOILING || Helpers.secondsSince(recoveryStartTime) > Constants.RECOVERY_TIME) {
            action = Action.STANDING;
            groundState = GroundState.PLANTED;
        }
        if (!canClimb) {
            canJump = true;
            handleYInputs(); // disabled when canclimb to prevent look from overriding climb
        } else if (touchedGround == null) {
            canClimb = false;
        } else {
            canJump = false;
        }
        if (!inputControls.upButtonPressed && !inputControls.downButtonPressed && !canShoot) { // enables releasing y input before shoot to enable discharge post toggle
            canShoot = true;
        }
        if (turbo < Constants.MAX_TURBO && startTurbo == 0) {
            turbo += Constants.STAND_TURBO_INCREMENT;
        }
    }

    private void fall() {
        handleXInputs();
        handleYInputs();
        if (action != Action.RECOILING || Helpers.secondsSince(recoveryStartTime) > Constants.RECOVERY_TIME) { // prevents overriding recoil before full recovery
            action = Action.FALLING;
            groundState = GroundState.AIRBORNE;
        }
        canJump = false;
        canDash = false;
        canLook = true;
        if (fallStartTime == 0) {
            fallStartTime = TimeUtils.nanoTime();
        }
        fallTimeSeconds = Helpers.secondsSince(fallStartTime);
        if (!(touchedGround instanceof Skateable)) {
            rappelStartTime = 0;
            strideStartTime = 0;
        }

        // deactivates rappel and climb to prevent inappropriate activation when holding jumpbutton, crossing and no longer overlapping climbable plane
        if (touchedGround == null && canClimb) {
            canCling = false;
            canClimb = false;
        }

        if (touchedGround instanceof Pourous && getBottom() < touchedGround.getTop()) {
            canHover = false; // prevents hover icon flashing from indicator hud when tapping jump while submerged in sink
        } else if (!canRappel) {
            touchedGround = null;
            canHover = true;
        }

        if (canMove) canMove = false;

        canSink = false;

        if (turbo < Constants.MAX_TURBO) {
            turbo += Constants.FALL_TURBO_INCREMENT;
        }
    }

    // disables all else by virtue of neither top level update conditions being satisfied due to state
    private void recoil(Vector2 velocity, Hazardous hazard) {
        if (Helpers.secondsSince(recoveryStartTime) > Constants.RECOVERY_TIME * 2) {
            float xRelationship = Math.signum(position.x - hazard.getPosition().x);
            if (xRelationship != 0) {
                this.velocity.x = velocity.x * xRelationship;
            } else {
                this.velocity.x = Helpers.speedToVelocity(velocity.x, directionX, Orientation.X);
            }
            this.velocity.y = velocity.y;
            AssetManager.getInstance().getSoundAssets().damage.play();
            health -= hazard.getDamage() * healthMultiplier;
            groundState = GroundState.AIRBORNE;
            action = Action.RECOILING;
            recoveryStartTime = TimeUtils.nanoTime();
            chargeStartTime = 0;
            rappelStartTime = 0;
            strideStartTime = 0;
            lookStartTime = 0;
            swipeStartTime = 0;
            turbo = 0;
            canStride = false;
            canDash = false;
            canHover = false;
            canCling = false;
            canClimb = false;
            canRappel = false;
            canHurdle = false;
            if (autoblast) {
                if (!supercharged || health < 75) {
                    if (supercharged) {
                        superchargeStartTime = 0;
                        dispenseUpgrades();
                        supercharged = false;
                        energyColor = energy.theme().color();
                    }
                    shotIntensity = ShotIntensity.NORMAL;
                    autoblast = false;
                }
            } else shotIntensity = ShotIntensity.NORMAL;
        }
    }

    private void enableShoot(Energy energy) {
        canDispatch = false;
        if (canShoot) {
            if (getCarriedGround() == null && (inputControls.shootButtonPressed || (action == Action.RAPPELLING && (inputControls.rightButtonPressed || inputControls.leftButtonPressed)))) {
                if (chargeStartTime == 0) {
                    chargeStartTime = TimeUtils.nanoTime();
                } else if (chargeTimeSeconds > Constants.BLAST_CHARGE_DURATION) {
                    shotIntensity = ShotIntensity.BLAST;
                } else if (chargeTimeSeconds > Constants.BLAST_CHARGE_DURATION / 3) {
                    if (!autoblast) shotIntensity = ShotIntensity.CHARGED;
                }
                chargeTimeSeconds = Helpers.secondsSince(chargeStartTime);
            } else if (chargeStartTime != 0) {
                int ammoUsed;
                if (energy == Energy.NATIVE
                        || (ammo < Constants.BLAST_AMMO_CONSUMPTION && shotIntensity == ShotIntensity.BLAST)
                        || ammo < Constants.SHOT_AMMO_CONSUMPTION) {
                    ammoUsed = 0;
                    this.energy = Energy.NATIVE;
                    energyColor = Energy.NATIVE.theme().color();
                } else {
                    ammoUsed = Helpers.useAmmo(shotIntensity);
                }
                chargeStartTime = 0;
                chargeTimeSeconds = 0;
                shoot(shotIntensity, energy, ammoUsed);
            }
        }
    }

    private void shoot(ShotIntensity shotIntensity, Energy energy, int ammoUsed) {
        shootStartTime = TimeUtils.nanoTime();
        canDispatch = true;
        if (shotIntensity == ShotIntensity.BLAST) {
            AssetManager.getInstance().getSoundAssets().getBlastSound(energy).play();
        } else {
            AssetManager.getInstance().getSoundAssets().getShotSound(energy).play();
        }
        if (health < 100 && !supercharged) ammo -= ammoUsed * ammoMultiplier;
    }

    private void look() {
        float offset = 0;
        jumpStartTime = 0;
        if (lookStartTime == 0 && !canRush) {
            lookStartTime = TimeUtils.nanoTime();
            chaseCamPosition.set(position, 0);
        } else if (groundState != GroundState.AIRBORNE) {
            if (!getSwipeStatus() && (velocity.y == 0 || touchedGround instanceof Pourous || Helpers.inputToDirection() != Helpers.velocityToDirection(velocity, Orientation.Y))) {
                if (setChaseCamPosition(offset)) {
                    if (!touchedGround.isDense() && carriedGround == null && inputControls.shootButtonPressed && inputControls.jumpButtonJustPressed && directionY == Direction.DOWN) {
                        groundState = GroundState.AIRBORNE;
                        position.y -= 5;
                        fall();
                    }
                }
            } else {
                resetChaseCamPosition();
            }
        }
    }

    private void enableStride() {
        handleXInputs();
        if (canStride) {
            stride();
        }
    }

    public void stride() {
        action = Action.STRIDING;
        groundState = GroundState.PLANTED;
        if (turbo < Constants.MAX_TURBO && !(touchedGround instanceof Treadmill && ((Treadmill) touchedGround).getDirectionX() == Direction.LEFT)) {
            jumpStartTime = 0;
            turbo += Constants.STRIDE_TURBO_INCREMENT;
        }
        if (strideStartTime == 0) {
            strideSpeed = velocity.x;
            action = Action.STRIDING;
            groundState = GroundState.PLANTED;
            strideStartTime = TimeUtils.nanoTime();
        }
        strideTimeSeconds = Helpers.secondsSince(strideStartTime);
        strideAcceleration = strideTimeSeconds + Constants.AVATAR_STARTING_SPEED;
        velocity.x = Helpers.speedToVelocity(Math.min(Constants.AVATAR_MAX_SPEED * strideAcceleration + Constants.AVATAR_STARTING_SPEED, Constants.AVATAR_MAX_SPEED * strideMultiplier), directionX, Orientation.X);
        if (touchedGround instanceof Propelling) {
            velocity.x += Helpers.speedToVelocity(Constants.TREADMILL_SPEED, ((Propelling) touchedGround).getDirectionX(), Orientation.X);
        } else if (touchedGround instanceof Skateable) {
            velocity.x = strideSpeed + Helpers.speedToVelocity(Math.min(Constants.AVATAR_MAX_SPEED * strideAcceleration / 2 + Constants.AVATAR_STARTING_SPEED, Constants.AVATAR_MAX_SPEED * 2), directionX, Orientation.X);
        } else if (canSink) {
            velocity.x = Helpers.speedToVelocity(10, directionX, Orientation.X);
            velocity.y = -3;
        }
    }

    private void enableDash() {
        handleXInputs();
        if (canDash && !canRush) {
            dash();
        } else if (action == Action.DASHING) {
            dash();
            canDash = true; // false for one frame for triptread activation from level updater
        }
    }

    private void dash() {
        if (action != Action.DASHING) {
            action = Action.DASHING;
            groundState = GroundState.PLANTED;
            dashStartTime = TimeUtils.nanoTime();
            jumpStartTime = 0;
            startTurbo = 0;
            strideStartTime = 0;
            canStride = false;
            canDash = false;
        } else if (turbo >= Constants.DASH_TURBO_DECREMENT) {
            dashTimeSeconds = Helpers.secondsSince(dashStartTime);
            turbo -= Constants.DASH_TURBO_DECREMENT * turboMultiplier;
            velocity.x = Helpers.speedToVelocity(Constants.AVATAR_MAX_SPEED, directionX, Orientation.X);
        } else {
            canDash = false;
            dashStartTime = 0;
            stand();
        }
        if (touchedGround instanceof Skateable
        || (touchedGround instanceof Propelling && directionX == ((Propelling) touchedGround).getDirectionX())) {
            velocity.x = Helpers.speedToVelocity(Constants.AVATAR_MAX_SPEED + Constants.TREADMILL_SPEED, directionX, Orientation.X);
        }
    }

    private void enableJump() {
        if (canJump) {
            if (jumpStartTime != 0 && action == Action.STANDING) {
                if (inputControls.jumpButtonPressed) {
                    if (startTurbo == 0) {
                        startTurbo = turbo;
                    }
                    if (turbo >= Constants.LEAP_TURBO_DECREMENT) {
                        turbo -= Constants.LEAP_TURBO_DECREMENT;
                    }
                } else if (turbo < Constants.LEAP_TURBO_DECREMENT) {
                    jump();
                    velocity.x += Helpers.speedToVelocity(Constants.AVATAR_MAX_SPEED / 8, directionX, Orientation.X);
                    velocity.y *= (1 + (startTurbo/100 * .35f)) * jumpMultiplier;
                    startTurbo = 0;
                    jumpStartTime = 0;
                } else {
                    startTurbo = 0;
                    jumpStartTime = 0;
                }
            } else if (inputControls.jumpButtonJustPressed && lookStartTime == 0) {
                jump();
            } else {
                startTurbo = 0;
            }
        }
    }

    private void jump() {
        if (canJump) {
            action = Action.FALLING; // prevents from rendering stride sprite when striding against ground side and jumping on reboundable
            groundState = GroundState.AIRBORNE;
            if (jumpStartTime <= 1.75f) {
                jumpStartTime = TimeUtils.nanoTime();
            }
            canJump = false;
        } else {
            startTurbo = 0;
        }
        velocity.x += Helpers.speedToVelocity(Constants.AVATAR_STARTING_SPEED * Constants.STRIDING_JUMP_MULTIPLIER, directionX, Orientation.X);
        velocity.y = Constants.JUMP_SPEED;
        velocity.y *= Constants.STRIDING_JUMP_MULTIPLIER;
        if (touchedGround instanceof Reboundable) {
            if (!(touchedGround instanceof Pliable && ((Pliable) touchedGround).isBeingCarried() && ((Pliable) touchedGround).getCarrier() == this)) {
                velocity.y *= ((Reboundable) touchedGround).jumpMultiplier();
                jumpStartTime = 0;
            }
        } else {
            fall(); // causes fall texture to render for one frame
        }
    }

    private void enableHover() {
        if (canHover) {
            if (!(inputControls.upButtonPressed || inputControls.downButtonPressed)  // prevents from deactivating hover when toggling energy
            && inputControls.jumpButtonJustPressed) {
                if (action == Action.HOVERING) {
                    //   canHover = false;
                    hoverStartTime = 0;
                    velocity.x -= velocity.x / 2;
                    fall();
                } else {
                    hoverStartTime = 0;
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
            action = Action.HOVERING; // indicates currently hovering
            groundState = GroundState.AIRBORNE;
            hoverStartTime = TimeUtils.nanoTime(); // begins timing hover duration
        }
        hoverTimeSeconds = Helpers.secondsSince(hoverStartTime); // for comparing with max hover time
        if (turbo >= Constants.HOVER_TURBO_DECREMENT) {
            velocity.y = 0; // disables impact of gravity
            turbo -= Constants.HOVER_TURBO_DECREMENT * turboMultiplier;
        } else {
            canHover = false;
            fall(); // when max hover time is exceeded
        }
        handleXInputs();
    }

    private void enableRappel() {
        if (action == Action.RAPPELLING || (canRappel && inputControls.jumpButtonJustPressed)) {
            rappel();
        }
    }

    private void rappel() {
        if (canRappel) {
            lookStartTime = 0;
            canRappel = false;
            action = Action.RAPPELLING;
            groundState = GroundState.AIRBORNE;
            rappelStartTime = TimeUtils.nanoTime();
            canJump = true;
            canSink = false;
            directionX = Helpers.getOppositeDirection(Helpers.velocityToDirection(velocity, Orientation.X));
        }
        canHurdle = false;
        if (touchedGround != null) {
            if (position.y >= touchedGround.getTop() - 10) { // manage hurdle
                position.y = touchedGround.getTop() - 10;
                if (touchedGround instanceof Hurdleable) {
                    canHurdle = true;
                }
            }
            if (directionX == Direction.LEFT) { // set position relative to rappelled ground
                position.x = touchedGround.getLeft() - getHalfWidth();
            } else {
                position.x = touchedGround.getRight() + getHalfWidth();
            }
        }
        float rappelTimeSeconds = Helpers.secondsSince(rappelStartTime);
        if (!inputControls.jumpButtonPressed) { // discontinue rappel
            if (rappelTimeSeconds >= Constants.RAPPEL_FRAME_DURATION) {
                velocity.x = Helpers.speedToVelocity(Constants.AVATAR_MAX_SPEED, directionX, Orientation.X);
                if (!(touchedGround instanceof Skateable)) {
                    rappelStartTime = 0;
                    jump();
                } else {
                    rappelStartTime = 0;
                    fall();
                }
            } else {
                canHover = true;
            }
            canHurdle = false;
        } else { // rappel
//            lookStartTime = 0;
            // detect moving ground
            boolean yMoving = false;
            if (touchedGround instanceof Moving && ((Moving) touchedGround).getVelocity().y != 0) {
                yMoving = true;
            }
            if (touchedGround instanceof Pliable) {
                canMove = Helpers.inputToDirection() == Helpers.getOppositeDirection(directionX);
                if (((Pliable) touchedGround).isBeneatheGround()) { // prevent hurdle when not positioned atop series of stacked grounds
                    canHurdle = false;
                }
                if (((Pliable) touchedGround).getMovingGround() != null && ((Pliable) touchedGround).getMovingGround().getVelocity().y != 0) {
                    touchedGround = (Groundable) ((Pliable) touchedGround).getMovingGround();
                    yMoving = true;
                }
            }
            // manage ground interaction save for skateables
            if (!(touchedGround == null || touchedGround instanceof Skateable)) {
                if (/*inputControls.downButtonPressed || */turbo < Constants.RAPPEL_TURBO_DECREMENT || (inputControls.downButtonPressed && canSlump)) { // descend on command or turbo depletion
                    rappelStartTime = 0;
                    velocity.y += Constants.RAPPEL_GRAVITY_OFFSET;
                } else if (inputControls.upButtonPressed && canHurdle) { // hurdle on command
                    canHurdle = false;
                    canRappel = false;
                    rappelStartTime = 0;
                    directionX = Helpers.getOppositeDirection(directionX);
                    velocity.x = Helpers.speedToVelocity(Constants.CLIMB_SPEED / 2, directionX, Orientation.X);
                    float jumpBoost = 0;
                    if (yMoving) {
                        jumpBoost = Math.abs(((Moving) touchedGround).getVelocity().y);
                    }
                    jump();
                    velocity.y += jumpBoost;
                } else { // cling by default
                    if (rappelStartTime == 0) rappelStartTime = TimeUtils.nanoTime();
                    if (!canHurdle) { // decrement turbo when ground is not moving and cannot hurdle
                        turbo -= Constants.RAPPEL_TURBO_DECREMENT * turboMultiplier;
                    }
                    if (yMoving) { // maintain position relative to rappelled ground
                        velocity.y = ((Moving) touchedGround).getVelocity().y;
                    } else {
                        velocity.y = 0;
                    }
                }
                if (canCling || touchedGround instanceof Climbable) canClimb = true;
                else {
                    if (inputControls.downButtonJustPressed) {
                        if (lookStartTime == 0) {
                            lookStartTime = TimeUtils.nanoTime();
                            canSlump = false;
                        } else if (Helpers.secondsSince(lookStartTime) < Constants.DOUBLE_TAP_SPEED)
                            canSlump = true;
                    }
                    if (!inputControls.downButtonPressed && Helpers.secondsSince(lookStartTime) > Constants.DOUBLE_TAP_SPEED)
                        lookStartTime = 0;
                }
                handleYInputs();
            }
        }
    }

    private void enableClimb() {
        if (canCling)  {
            if (action != Action.RAPPELLING || inputControls.upButtonPressed) {
                // when overlapping all but top, set canrappel which if action enablesclimb will set canclimb to true
                if (inputControls.jumpButtonPressed) {
                    if (lookStartTime == 0) {// cannot initiate climb if already looking; must first neutralize
                        canLook = false; // prevents look from overriding climb
                        canClimb = true; // enables climb handling from handleY()
                    }
                } else {
                    canClimb = false;
                    canCling = false;
                    canLook = true; // enables look when engaging climbable but not actively climbing
                }
                handleXInputs(); // enables change of x direction for shooting left or right
                handleYInputs(); // enables change of y direction for looking and climbing up or down
            }
            if (!inputControls.hasInput() && turbo < Constants.MAX_TURBO) turbo += Constants.CLIMB_TURBO_INCREMENT;
        } else {
            climbStartTime = 0;
            if (action == Action.CLIMBING) {
                fall();
                if (!(touchedGround instanceof Climbable && Helpers.overlapsBetweenTwoSides(position.x, getHalfWidth(), touchedGround.getLeft(), touchedGround.getRight())))  {
                    velocity.x = Helpers.speedToVelocity(Constants.CLIMB_SPEED, directionX, Orientation.X);
                }
            }
        }
    }

    private void climb(Orientation orientation) {
        if (canCling) { // canrappel set to false from handleYinputs() if double tapping down
            if (action != Action.CLIMBING) { // at the time of climb initiation
                groundState = GroundState.PLANTED;
                action = Action.CLIMBING;
            } else if (climbStartTime == 0) {
                climbStartTime = TimeUtils.nanoTime();
            }
            resetChaseCamPosition();
            canHover = false;
            dashTimeSeconds = Helpers.secondsSince(dashStartTime);
            if (orientation == Orientation.X) {
                velocity.x = Helpers.speedToVelocity(Constants.CLIMB_SPEED, directionX, Orientation.X);
            } else if (orientation == Orientation.Y) {
                velocity.y = Helpers.speedToVelocity(Constants.CLIMB_SPEED, directionY, Orientation.Y);
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
        Array<TextureRegion> body = new Array<TextureRegion>();
        Vector2 center = Constants.AVATAR_EYE_POSITION;
        float rotation = 0;
        boolean inverseX = directionX == Direction.LEFT;
        boolean inverseY = inputControls != null ? false : inputControls.downButtonPressed;
        boolean frontFacing = true;
        float staticIndex = Helpers.secondsSince(Helpers.secondsSince(TimeUtils.nanoTime()) * .25f);
        TextureRegion hair;
        TextureRegion torso;
        TextureRegion waist;
        TextureRegion legs;
        TextureRegion feet;
        TextureRegion rearArm;
        TextureRegion frontArm;
        TextureRegion rearHand;
        TextureRegion frontHand;
        TextureRegion eyes;
        TextureRegion mouth;
        TextureRegion head;
        Animation<TextureRegion> shoot;

        if (bladeState == BladeState.RETRACTED) {
            switch (action) {
                case STANDING:
                    torso = AssetManager.getInstance().getAvatarAssets().midsection;
                    legs = AssetManager.getInstance().getAvatarAssets().legsStand;
                    rearArm = getRearArm(AssetManager.getInstance().getAvatarAssets().armRelax);
                    frontArm = getFrontArm(AssetManager.getInstance().getAvatarAssets().armReleaseForward);
                    hair = AssetManager.getInstance().getAvatarAssets().hair.getKeyFrame(0);
                    head = AssetManager.getInstance().getAvatarAssets().head;
                    mouth = AssetManager.getInstance().getAvatarAssets().mouthClosed;
                    eyes = getEyes(AssetManager.getInstance().getAvatarAssets().eyesOpen.getKeyFrame(0));
                    waist = AssetManager.getInstance().getAvatarAssets().waist.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 2);
                    feet = AssetManager.getInstance().getAvatarAssets().feetStand;
                    shoot = AssetManager.getInstance().getAvatarAssets().handPoint;
                    rearHand = getRearHand(AssetManager.getInstance().getAvatarAssets().handSwing.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 3));
                    frontHand = getFrontHand(shoot, AssetManager.getInstance().getAvatarAssets().handCurl.getKeyFrame(0));
                    break;
                case STRIDING:
                    float strideFrame = Math.min(strideAcceleration * strideAcceleration, strideAcceleration);
                    torso = AssetManager.getInstance().getAvatarAssets().midsection;
                    legs = AssetManager.getInstance().getAvatarAssets().legsStride.getKeyFrame(strideFrame);
                    rearArm = getRearArm(AssetManager.getInstance().getAvatarAssets().armSwing.getKeyFrame(strideFrame / 6));
                    frontArm = getFrontArm(AssetManager.getInstance().getAvatarAssets().armCurl.getKeyFrame(strideFrame / 6));
                    hair = AssetManager.getInstance().getAvatarAssets().hair.getKeyFrame(strideTimeSeconds * Math.max(Math.abs(velocity.x / Constants.AVATAR_MAX_SPEED), .33f));
                    head = AssetManager.getInstance().getAvatarAssets().head;
                    mouth = AssetManager.getInstance().getAvatarAssets().mouthClosed;
                    eyes = getEyes(AssetManager.getInstance().getAvatarAssets().eyesOpen.getKeyFrame(0));
                    waist = AssetManager.getInstance().getAvatarAssets().waist.getKeyFrame(strideFrame);
                    feet = AssetManager.getInstance().getAvatarAssets().feetStride.getKeyFrame(strideFrame);
                    shoot = AssetManager.getInstance().getAvatarAssets().handPoint;
                    rearHand = getRearHand(AssetManager.getInstance().getAvatarAssets().handSwing.getKeyFrame(strideFrame / 6));
                    frontHand = getFrontHand(shoot, AssetManager.getInstance().getAvatarAssets().handCurl.getKeyFrame(strideFrame / 6));
                    break;
                case CLIMBING:
                    frontFacing = false;
                    feet = AssetManager.getInstance().getAvatarAssets().feetClimb;
                    shoot = AssetManager.getInstance().getAvatarAssets().handPoint;
                    frontHand = getFrontHand(shoot, AssetManager.getInstance().getAvatarAssets().handCurl.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 3));
                    rearHand = AssetManager.getInstance().getAvatarAssets().handSwing.getKeyFrame(0);
                    waist = AssetManager.getInstance().getAvatarAssets().waist.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 3);
                    eyes = getEyes(AssetManager.getInstance().getAvatarAssets().eyesOpen.getKeyFrame(0));
                    mouth = AssetManager.getInstance().getAvatarAssets().mouthClosed;
                    head = AssetManager.getInstance().getAvatarAssets().headClimb;
                    hair = AssetManager.getInstance().getAvatarAssets().hairClimb;
                    frontArm = AssetManager.getInstance().getAvatarAssets().obfuscated;
                    rearArm = (lookStartTime != 0 || inputControls.shootButtonPressed) ? AssetManager.getInstance().getAvatarAssets().obfuscated : AssetManager.getInstance().getAvatarAssets().armClimb;
                    legs = AssetManager.getInstance().getAvatarAssets().legsClimb;
                    torso = AssetManager.getInstance().getAvatarAssets().midsectionClimb;
                    break;
                case DASHING:
                    torso = AssetManager.getInstance().getAvatarAssets().midsection;
                    legs = AssetManager.getInstance().getAvatarAssets().legsStride.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 2);
                    rearArm = getRearArm(AssetManager.getInstance().getAvatarAssets().armRelax);
                    frontArm = getFrontArm(AssetManager.getInstance().getAvatarAssets().armCurl.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 3));
                    hair = AssetManager.getInstance().getAvatarAssets().hair.getKeyFrame(Math.max(dashTimeSeconds * Math.abs(velocity.x / Constants.AVATAR_MAX_SPEED), staticIndex));
                    head = AssetManager.getInstance().getAvatarAssets().head;
                    mouth = AssetManager.getInstance().getAvatarAssets().mouthClosed;
                    eyes = getEyes(AssetManager.getInstance().getAvatarAssets().eyesOpen.getKeyFrame(0));
                    waist = AssetManager.getInstance().getAvatarAssets().waist.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 3);
                    feet = AssetManager.getInstance().getAvatarAssets().feetDash.getKeyFrame(dashTimeSeconds);
                    shoot = AssetManager.getInstance().getAvatarAssets().handPoint;
                    rearHand = getRearHand(AssetManager.getInstance().getAvatarAssets().handSwing.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 3));
                    frontHand = getFrontHand(shoot, AssetManager.getInstance().getAvatarAssets().handCurl.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 3));
                    break;
                case FALLING:
                    torso = AssetManager.getInstance().getAvatarAssets().midsection;
                    legs = AssetManager.getInstance().getAvatarAssets().legsFall;
                    rearArm = getRearArm(AssetManager.getInstance().getAvatarAssets().armReach);
                    frontArm = getFrontArm(AssetManager.getInstance().getAvatarAssets().armPoint);
                    hair = AssetManager.getInstance().getAvatarAssets().hair.getKeyFrame(Math.max(fallTimeSeconds * Math.abs(velocity.x / Constants.AVATAR_MAX_SPEED), staticIndex));
                    head = AssetManager.getInstance().getAvatarAssets().head;
                    mouth = velocity.y > 0 ? AssetManager.getInstance().getAvatarAssets().mouthOpen : AssetManager.getInstance().getAvatarAssets().mouthClosed;
                    eyes = getEyes(AssetManager.getInstance().getAvatarAssets().eyesOpen.getKeyFrame(0));
                    waist = AssetManager.getInstance().getAvatarAssets().waist.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 3);
                    shoot = AssetManager.getInstance().getAvatarAssets().handPoint;
                    rearHand = getRearHand(AssetManager.getInstance().getAvatarAssets().handReach);
                    frontHand = getFrontHand(shoot, AssetManager.getInstance().getAvatarAssets().handPoint.getKeyFrame(0));
                    feet = AssetManager.getInstance().getAvatarAssets().feetFall;
                    break;
                case HOVERING:
                    torso = AssetManager.getInstance().getAvatarAssets().midsection;
                    legs = AssetManager.getInstance().getAvatarAssets().legsStand;
                    rearArm = getRearArm(AssetManager.getInstance().getAvatarAssets().armRelax);
                    frontArm = getFrontArm(AssetManager.getInstance().getAvatarAssets().armReleaseForward);
                    hair = AssetManager.getInstance().getAvatarAssets().hair.getKeyFrame(Math.max(hoverTimeSeconds * Math.abs(velocity.x / Constants.AVATAR_MAX_SPEED), staticIndex));
                    head = AssetManager.getInstance().getAvatarAssets().head;
                    mouth = AssetManager.getInstance().getAvatarAssets().mouthClosed;
                    eyes = getEyes(AssetManager.getInstance().getAvatarAssets().eyesOpen.getKeyFrame(0));
                    waist = AssetManager.getInstance().getAvatarAssets().waist.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 2);
                    feet = AssetManager.getInstance().getAvatarAssets().feetHover.getKeyFrame(hoverTimeSeconds);
                    shoot = AssetManager.getInstance().getAvatarAssets().handPoint;
                    rearHand = getRearHand(AssetManager.getInstance().getAvatarAssets().handSwing.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 3));
                    frontHand = getFrontHand(shoot, AssetManager.getInstance().getAvatarAssets().handCurl.getKeyFrame(0));
                    break;
                case RAPPELLING:
                    torso = AssetManager.getInstance().getAvatarAssets().midsection;
                    legs = AssetManager.getInstance().getAvatarAssets().legsRappel;
                    rearArm = AssetManager.getInstance().getAvatarAssets().armReach;
                    frontArm = getFrontArm(AssetManager.getInstance().getAvatarAssets().armReleaseForward);
                    hair = AssetManager.getInstance().getAvatarAssets().hair.getKeyFrame(0);
                    head = AssetManager.getInstance().getAvatarAssets().head;
                    mouth = AssetManager.getInstance().getAvatarAssets().mouthOpen;
                    eyes = getEyes(AssetManager.getInstance().getAvatarAssets().eyesOpen.getKeyFrame(0));
                    waist = AssetManager.getInstance().getAvatarAssets().waist.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 2);
                    shoot = AssetManager.getInstance().getAvatarAssets().handPoint;
                    rearHand = getRearHand(AssetManager.getInstance().getAvatarAssets().handRappel.getKeyFrame(0));
                    frontHand = getFrontHand(shoot, AssetManager.getInstance().getAvatarAssets().handCurl.getKeyFrame(0));
                    feet = AssetManager.getInstance().getAvatarAssets().feetRappel;
                    break;
                case RECOILING:
                    torso = AssetManager.getInstance().getAvatarAssets().midsection;
                    legs = AssetManager.getInstance().getAvatarAssets().legsRecoil;
                    rearArm = getRearArm(AssetManager.getInstance().getAvatarAssets().armReach);
                    frontArm = getFrontArm(AssetManager.getInstance().getAvatarAssets().armPoint);
                    hair = AssetManager.getInstance().getAvatarAssets().hair.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 2);
                    head = AssetManager.getInstance().getAvatarAssets().head;
                    mouth = AssetManager.getInstance().getAvatarAssets().mouthOpen;
                    eyes = AssetManager.getInstance().getAvatarAssets().eyesBlink;
                    waist = AssetManager.getInstance().getAvatarAssets().waist.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 3);
                    shoot = AssetManager.getInstance().getAvatarAssets().handPoint;
                    rearHand = getRearHand(AssetManager.getInstance().getAvatarAssets().handReach);
                    frontHand = getFrontHand(shoot, AssetManager.getInstance().getAvatarAssets().handPoint.getKeyFrame(0));
                    feet = AssetManager.getInstance().getAvatarAssets().feetRecoil;
                    break;
                default:
                    torso = AssetManager.getInstance().getAvatarAssets().midsection;
                    legs = AssetManager.getInstance().getAvatarAssets().legsStand;
                    rearArm = getRearArm(AssetManager.getInstance().getAvatarAssets().armRelax);
                    frontArm = getFrontArm(AssetManager.getInstance().getAvatarAssets().armReleaseForward);
                    hair = AssetManager.getInstance().getAvatarAssets().hair.getKeyFrame(0);
                    head = AssetManager.getInstance().getAvatarAssets().head;
                    mouth = AssetManager.getInstance().getAvatarAssets().mouthClosed;
                    eyes = getEyes(AssetManager.getInstance().getAvatarAssets().eyesOpen.getKeyFrame(0));
                    waist = AssetManager.getInstance().getAvatarAssets().waist.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 2);
                    feet = AssetManager.getInstance().getAvatarAssets().feetStand;
                    shoot = AssetManager.getInstance().getAvatarAssets().handPoint;
                    rearHand = getRearHand(AssetManager.getInstance().getAvatarAssets().handSwing.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 3));
                    frontHand = getFrontHand(shoot, AssetManager.getInstance().getAvatarAssets().handCurl.getKeyFrame(0));
                    break;
            }
        } else {
            if (bladeState == BladeState.RUSH) {
                torso = AssetManager.getInstance().getAvatarAssets().midsection;
                legs = AssetManager.getInstance().getAvatarAssets().legsStand;
                rearArm = getRearArm(AssetManager.getInstance().getAvatarAssets().armToward.getKeyFrame(swipeTimeSeconds));
                frontArm = getFrontArm(AssetManager.getInstance().getAvatarAssets().obfuscated);
                hair = AssetManager.getInstance().getAvatarAssets().hair.getKeyFrame(swipeTimeSeconds);
                head = AssetManager.getInstance().getAvatarAssets().head;
                mouth = AssetManager.getInstance().getAvatarAssets().mouthOpen;
                eyes = AssetManager.getInstance().getAvatarAssets().eyesBlink;
                waist =  AssetManager.getInstance().getAvatarAssets().waist.getKeyFrame(3);
                rearHand = getRearHand(AssetManager.getInstance().getAvatarAssets().obfuscated);
                shoot = AssetManager.getInstance().getAvatarAssets().handPoint;
                frontHand = getFrontHand(shoot, AssetManager.getInstance().getAvatarAssets().obfuscated);
                feet = AssetManager.getInstance().getAvatarAssets().feetStand;
            } else if (bladeState == BladeState.CUT) {
                torso = AssetManager.getInstance().getAvatarAssets().midsection;
                legs = AssetManager.getInstance().getAvatarAssets().legsStand;
                rearArm = getRearArm(AssetManager.getInstance().getAvatarAssets().obfuscated);
                frontArm = getFrontArm(AssetManager.getInstance().getAvatarAssets().obfuscated);
                hair = AssetManager.getInstance().getAvatarAssets().hair.getKeyFrame(swipeTimeSeconds);
                head = AssetManager.getInstance().getAvatarAssets().head;
                mouth = AssetManager.getInstance().getAvatarAssets().mouthOpen;
                eyes = AssetManager.getInstance().getAvatarAssets().eyesBlink;
                waist =  AssetManager.getInstance().getAvatarAssets().waist.getKeyFrame(3);
                shoot = AssetManager.getInstance().getAvatarAssets().handPoint;
                rearHand = getRearHand(AssetManager.getInstance().getAvatarAssets().obfuscated);
                frontHand = getFrontHand(shoot, AssetManager.getInstance().getAvatarAssets().obfuscated);
                feet = AssetManager.getInstance().getAvatarAssets().feetStand;
            } else if (bladeState == BladeState.FLIP) {
                float remainingFrames = (Constants.FLIPSWIPE_FRAME_DURATION * 5) / swipeTimeSeconds;
                rotation = Helpers.booleanToDirectionalValue(inverseX ^ inverseY); // if either but not both directions inverse
                if (remainingFrames < 2) {
                    center = new Vector2(rotation * 24, rotation * -12);
                    rotation *= 270;
                } else if (remainingFrames < 3) {
                    center = new Vector2(-12, -12);
                    rotation *= 180;
                } else if (remainingFrames < 4) {
                    center = new Vector2(rotation * -24, rotation * 24);
                    rotation *= 90;
                } else {
                    rotation *= 0;
                }
                torso = AssetManager.getInstance().getAvatarAssets().midsection;
                legs = AssetManager.getInstance().getAvatarAssets().legsStride.getKeyFrame(swipeTimeSeconds);
                rearArm = getRearArm(AssetManager.getInstance().getAvatarAssets().obfuscated);
                frontArm = getFrontArm(AssetManager.getInstance().getAvatarAssets().obfuscated);
                hair = AssetManager.getInstance().getAvatarAssets().hair.getKeyFrame(swipeTimeSeconds);
                head = AssetManager.getInstance().getAvatarAssets().head;
                mouth = AssetManager.getInstance().getAvatarAssets().mouthOpen;
                eyes = AssetManager.getInstance().getAvatarAssets().eyesBlink;
                waist =  AssetManager.getInstance().getAvatarAssets().waist.getKeyFrame(3);
                shoot = AssetManager.getInstance().getAvatarAssets().handPoint;
                rearHand = getRearHand(AssetManager.getInstance().getAvatarAssets().obfuscated);
                frontHand = getFrontHand(shoot, AssetManager.getInstance().getAvatarAssets().obfuscated);
                feet = AssetManager.getInstance().getAvatarAssets().feetStride.getKeyFrame(swipeTimeSeconds);
            } else {
                torso = AssetManager.getInstance().getAvatarAssets().midsection;
                legs = AssetManager.getInstance().getAvatarAssets().legsStand;
                rearArm = getRearArm(AssetManager.getInstance().getAvatarAssets().armRelax);
                frontArm = getFrontArm(AssetManager.getInstance().getAvatarAssets().armReleaseForward);
                hair = AssetManager.getInstance().getAvatarAssets().hair.getKeyFrame(0);
                head = AssetManager.getInstance().getAvatarAssets().head;
                mouth = AssetManager.getInstance().getAvatarAssets().mouthClosed;
                eyes = getEyes(AssetManager.getInstance().getAvatarAssets().eyesOpen.getKeyFrame(0));
                waist = AssetManager.getInstance().getAvatarAssets().waist.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 2);
                feet = AssetManager.getInstance().getAvatarAssets().feetStand;
                shoot = AssetManager.getInstance().getAvatarAssets().handPoint;
                rearHand = getRearHand(AssetManager.getInstance().getAvatarAssets().handSwing.getKeyFrame(Constants.STRIDE_FRAME_DURATION * 3));
                frontHand = getFrontHand(shoot, AssetManager.getInstance().getAvatarAssets().handCurl.getKeyFrame(0));
            }
        }

        body.add(torso);
        body.add(legs);
        body.add(rearArm);
        body.add(frontArm);
        body.add(hair);
        body.add(head);
        body.add(mouth);
        body.add(eyes);
        body.add(waist);
        body.add(rearHand);
        body.add(frontHand);
        body.add(feet);

        if (supercharged)
            if (Helpers.secondsSince(superchargeStartTime) % 0.75f < 0.325f) energyColor = new Color(0x7a6000ff);
            else energyColor = new Color(0x6a5400ff);

        if (frontFacing) batch.setColor(energyColor);
        else body.reverse();

        Vector2 renderPosition = new Vector2().set(position);
        if (inverseX) renderPosition.x -= 2;

        for (TextureRegion region : body) {
            Helpers.drawTextureRegion(batch, viewport, region, renderPosition, center, 1, rotation, inverseX, false);
            if (region == frontArm) {
                if (frontFacing) batch.setColor(Color.WHITE);
                else batch.setColor(energyColor);
            }
        }
        batch.setColor(Color.WHITE);
        body.clear();
    }

    private TextureRegion getFrontArm(TextureRegion nonShoot) {
        TextureRegion shoot = AssetManager.getInstance().getAvatarAssets().armPoint;
        float frame = (bladeState == BladeState.RETRACTED ? 3 : swipeTimeSeconds);
        if (bladeState == BladeState.RUSH) {
            if (inputControls.leftButtonPressed) {
                nonShoot = AssetManager.getInstance().getAvatarAssets().armPull.getKeyFrame(swipeTimeSeconds);
            } else {
                nonShoot = AssetManager.getInstance().getAvatarAssets().armPush.getKeyFrame(swipeTimeSeconds);
            }
        } else if (lookStartTime != 0) {
            if (directionY == Direction.UP) {
                shoot = AssetManager.getInstance().getAvatarAssets().armRaise;
                nonShoot =  AssetManager.getInstance().getAvatarAssets().armUp.getKeyFrame(frame);
            } else {
                shoot = AssetManager.getInstance().getAvatarAssets().armLower;
                nonShoot = AssetManager.getInstance().getAvatarAssets().armDown.getKeyFrame(frame);
            }
        }
        if (inputControls.shootButtonPressed) {
            return shoot;
        } else {
            return nonShoot; // defaults to parameter value if no conditions met
        }
    }

    private TextureRegion getFrontHand(Animation<TextureRegion> shoot, TextureRegion nonShoot) {
        float frame = (bladeState == BladeState.RETRACTED ? 3 : swipeTimeSeconds);
        if (bladeState == BladeState.RUSH) {
            if (inputControls.leftButtonPressed) {
                nonShoot = AssetManager.getInstance().getAvatarAssets().handBack.getKeyFrame(swipeTimeSeconds);
            } else {
                nonShoot = AssetManager.getInstance().getAvatarAssets().handFore.getKeyFrame(swipeTimeSeconds);
            }
        } else if (lookStartTime != 0) {
            if (directionY == Direction.UP) {
                shoot = AssetManager.getInstance().getAvatarAssets().handRaise;
                nonShoot = AssetManager.getInstance().getAvatarAssets().handUp.getKeyFrame(frame);
            } else {
                shoot = AssetManager.getInstance().getAvatarAssets().handLower;
                nonShoot = AssetManager.getInstance().getAvatarAssets().handDown.getKeyFrame(frame);
            }
        }
        if (inputControls.shootButtonPressed) {
            if (action != Action.RECOILING) {
                return (shoot.getKeyFrame(0));
            } else if (shotIntensity != ShotIntensity.BLAST) {
                return (shoot.getKeyFrame(chargeTimeSeconds / 1.25f));
            } else {
                return (shoot.getKeyFrame(chargeTimeSeconds / 2));
            }
        } else {
            return nonShoot; // defaults to parameter value if no conditions met
        }
    }
    
    private TextureRegion getRearArm(TextureRegion rearArm) {
        if (bladeState == BladeState.RUSH) {
            if (inputControls.leftButtonPressed) {
                rearArm = AssetManager.getInstance().getAvatarAssets().armAway.getKeyFrame(swipeTimeSeconds);
            } else {
                rearArm = AssetManager.getInstance().getAvatarAssets().armToward.getKeyFrame(swipeTimeSeconds);
            }
        } else if (lookStartTime != 0) {
            if (directionY == Direction.UP) {
                rearArm =  AssetManager.getInstance().getAvatarAssets().armClench;
            } else {
                rearArm = AssetManager.getInstance().getAvatarAssets().armReach;
            }
        } else if (carriedGround != null) {
                rearArm = AssetManager.getInstance().getAvatarAssets().armSwing.getKeyFrame(0);
        }
        return rearArm; // defaults to parameter value if no conditions met
    }

    private TextureRegion getRearHand(TextureRegion rearHand) {
        if (bladeState == BladeState.RUSH) {
            if (inputControls.leftButtonPressed) {
                rearHand = AssetManager.getInstance().getAvatarAssets().handAway.getKeyFrame(swipeTimeSeconds);
            } else {
                rearHand = AssetManager.getInstance().getAvatarAssets().handToward.getKeyFrame(swipeTimeSeconds);
            }
        } else if (action == Action.RAPPELLING) {
            float rappelTimeSeconds = Helpers.secondsSince(rappelStartTime);
            if (rappelStartTime != 0 && rappelTimeSeconds > 0.2f && velocity.y == 0) {
                rearHand = AssetManager.getInstance().getAvatarAssets().handRappel.getKeyFrame(rappelTimeSeconds);
            } else {
                rearHand = AssetManager.getInstance().getAvatarAssets().handReach;
            }
        } else if (lookStartTime != 0) {
            if (directionY == Direction.UP) {
                rearHand =  AssetManager.getInstance().getAvatarAssets().handSwing.getKeyFrame(0);
            } else {
                rearHand = AssetManager.getInstance().getAvatarAssets().handReach;
            }
        } else if (carriedGround != null) {
            if (velocity.x != 0) {
                rearHand = AssetManager.getInstance().getAvatarAssets().handMove.getKeyFrame(Helpers.secondsSince(100 - turbo));
            } else {
                rearHand = AssetManager.getInstance().getAvatarAssets().handSwing.getKeyFrame(0);
            }
        }
        return rearHand; // defaults to parameter value if no conditions met
    }

    private TextureRegion getEyes(TextureRegion eyes) {
        if (!inputControls.shootButtonPressed || chargeTimeSeconds > 2) {
            if ((!(Helpers.secondsSince(shootStartTime) < 1) &&
                ((Helpers.secondsSince(shootStartTime) % 5 < .1f)
                || (Helpers.secondsSince(shootStartTime) % 10 < .2f)
                || (Helpers.secondsSince(shootStartTime) % 14 < .1f)
                || (Helpers.secondsSince(shootStartTime) % 15 < .3f)
                || (Helpers.secondsSince(activeStartTime) > 60)))) {
                eyes = AssetManager.getInstance().getAvatarAssets().eyesBlink;
            } else if (canPeer && Helpers.secondsSince(shootStartTime) > 2) {
                eyes = AssetManager.getInstance().getAvatarAssets().eyesOpen.getKeyFrame(peerQuadrant);
            }
        }
        return eyes; // defaults to parameter value if no conditions met
    }

    public void setPeerTarget(Physical target, float rangeMultiplier) {
        if (peerQuadrant != 3 && position.dst(target.getPosition()) < Constants.WORLD_SIZE * rangeMultiplier) {
            if (Helpers.speedToVelocity(position.x - target.getPosition().x, directionX, Orientation.X) > 0) {
                canPeer = true;
                peerStartTime = TimeUtils.nanoTime();
                if (position.y - target.getPosition().y > target.getHeight()) {
                    peerQuadrant = 2;
                } else {
                    peerQuadrant = 1;
                }
            } else if (position.y - target.getPosition().y > target.getHeight()) {
                peerQuadrant = 3;
            }
        }
    }

    // Getters
    @Override public final Vector2 getPosition() { return position; }
    public final void setPosition(Vector2 position) { this.position.set(position); }
    @Override public final Vector2 getVelocity() { return velocity; }
    public final void setVelocity(Vector2 velocity) { this.velocity = velocity; }
    @Override public final Enums.Direction getDirectionX() { return directionX; }
    @Override public final Enums.Direction getDirectionY() { return directionY; }
    @Override public final Rectangle getCollisionBounds() { return bounds; }
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
    public final boolean getSwipeStatus() { return canFlip || canRush || canCut; }
    public final boolean getMoveStatus() { return canMove; }
    public final boolean getClingStatus() { return canCling; }
    public final boolean getDispatchStatus() { return canDispatch; }
    public final Hazardous getTouchedHazard() { return touchedHazard; }
    public final Groundable getTouchedGround() { return touchedGround; }
    public final Pliable getCarriedGround() { return carriedGround; }
    @Override public final Enums.GroundState getGroundState() { return groundState; }
    @Override public final Enums.Action getAction() { return action; }
    public final ShotIntensity getShotIntensity() { return shotIntensity; }
    public final BladeState getBladeState() { return bladeState; }
    @Override public final Energy getEnergy() { return energy; }
    private final float getHalfWidth() { return halfWidth; }
    public List<Energy> getEnergyList() { return energyList; }
    public List<Upgrade> getUpgrades() { return upgradeList; }
    public float weightFactor() { float weight = Constants.AVATAR_WEIGHT; return (carriedGround != null ? weight + carriedGround.weightFactor() : weight); }
    public final float getAmmo() { return ammo; }
    public int getLives() { return lives; }
    public Vector3 getChaseCamPosition() { return chaseCamPosition; }
    public long getLookStartTime() { return lookStartTime; }
    public float getChargeTimeSeconds() { return chargeTimeSeconds; }
    public float getSwipeTimeSeconds() { return swipeTimeSeconds; }
    public float getFallLimit() { return fallLimit; }
    @Override public Orientation getOrientation() { if (action == Action.CLIMBING || lookStartTime != 0) { return Orientation.Y; } return Orientation.X; }

    // Setters
    public void setCarriedGround(Pliable ground) { this.carriedGround = ground; }
    public void setAction(Action action) { this.action = action; }
    public void setDirectionX(Direction directionX) { this.directionX = directionX; }
    public void setDirectionY(Direction directionY) { this.directionY = directionY; }
    public void setLives(int lives) { this.lives = lives; }
    public void setHealth(float health) { this.health = health; }
    public void setTurbo(float turbo) { this.turbo = turbo; }
    public void setMoveStatus(boolean state) { canMove = state; }
    public void setInputControls(InputControls inputControls) { this.inputControls = inputControls; }
    public boolean setChaseCamPosition(float offset) {
        lookTimeSeconds = Helpers.secondsSince(lookStartTime);
        if (lookTimeSeconds > 1 || velocity.y != 0) {
            offset += 1.5f;
            if (Math.abs(chaseCamPosition.y - position.y) < Constants.MAX_LOOK_DISTANCE) {
                chaseCamPosition.y += Helpers.speedToVelocity(offset, directionY, Orientation.Y);
                chaseCamPosition.x = position.x;
            } else return true;
        }
        return false;
    }
    public void resetChaseCamPosition() {
        float offsetDistance = chaseCamPosition.y - position.y;
        // move chasecam back towards avatar yposition provided yposition cannot be changed until fully reset
        if (Math.abs(offsetDistance) > 5) { // if chasecam offset from avatar yposition more than five pixels
            if (offsetDistance < 0) {
                chaseCamPosition.y += 2.5f;
            } else if (offsetDistance > 0) {
                chaseCamPosition.y -= 2.5f;
            }
            chaseCamPosition.x = position.x; // set chasecam position to avatar xposition
        } else if (chaseCamPosition.y == position.y || velocity.y != 0) { // if chasecam offset less than 5 but greater than 0 and actively looking
            lookTimeSeconds = 0;
            lookStartTime = 0;
        } else {
            chaseCamPosition.set(position, 0); // reset chasecam
            canLook = false; // disable look
        }
    }
    public void setFallLimit(float limit) { fallLimit = limit; }
    public void addEnergy(Energy energy) { energyToggler.add(energy); }
    public void toggleEnergy(Direction toggleDirection) { // to enable in-game, must discharge nativeBlast ammo
        if (energyList.size() > 1) {
            if (toggleDirection == Direction.UP) {
                if (!energyToggler.hasNext()) {
                    while (energyToggler.hasPrevious()) {
                        energyToggler.previous();
                    }
                }
                if (energy == energyToggler.next()) {
                    toggleEnergy(toggleDirection);
                } else {
                    energy = energyToggler.previous();
                }
                energyColor = energy.theme().color();
            } else if (toggleDirection == Direction.DOWN) {
                if (!energyToggler.hasPrevious()) {
                    while (energyToggler.hasNext()) {
                        energyToggler.next();
                    }
                }
                if (energy == energyToggler.previous()) {
                    toggleEnergy(toggleDirection);
                } else {
                    energy = energyToggler.next();
                }
                energyColor = energy.theme().color();
            }
        }
    }
    public void addUpgrade(Upgrade upgrade) { Gdx.app.log(TAG, upgradeList.toString()); upgradeList.add(upgrade); Gdx.app.log(TAG, upgradeList.toString()); dispenseUpgrades(); Gdx.app.log(TAG, turboMultiplier + "");}

    private void dispenseUpgrades() {
        if (upgradeList.contains(Upgrade.AMMO)) {
            ammoMultiplier = .9f;
        }
        if (upgradeList.contains(Upgrade.HEALTH)) {
            healthMultiplier = .8f;
        }
        if (upgradeList.contains(Upgrade.TURBO)) {
            turboMultiplier = .7f;
        }
        if (upgradeList.contains(Upgrade.STRIDE)) {
            strideMultiplier = 1.35f;
        }
        if (upgradeList.contains(Upgrade.JUMP)) {
            jumpMultiplier = 1.15f;
        }
        if (supercharged && superchargeStartTime != 0) setHealth(Constants.MAX_HEALTH);
    }

    public long getClimbStartTime() { return climbStartTime; }

    public void setSpawnPosition(Vector2 spawnPosition) { this.spawnPosition.set(spawnPosition); }
    public void resetChargeIntensity() { if (!autoblast) shotIntensity = ShotIntensity.NORMAL; }
    public void detectInput() { if (InputControls.getInstance().hasInput()) { activeStartTime = TimeUtils.nanoTime(); } }
    public boolean isSupercharged() { return supercharged; }
    public void setSupercharged(boolean supercharged) { this.supercharged = supercharged; }
    public void supercharge() {
        health = Constants.MAX_HEALTH;
        ammo = Constants.MAX_AMMO;
        turbo = Constants.MAX_TURBO;
        shotIntensity = ShotIntensity.BLAST;
        supercharged = true;
        autoblast = true;
        superchargeStartTime = TimeUtils.nanoTime();
        ammoMultiplier = .9f;
        healthMultiplier = .8f;
        turboMultiplier = .7f;
        strideMultiplier = 1.35f;
        jumpMultiplier = 1.15f;
    }
    public void dispose() { energyList.clear(); }
    public boolean isPrioritized() { return prioritized; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_HIGH;
    }
}
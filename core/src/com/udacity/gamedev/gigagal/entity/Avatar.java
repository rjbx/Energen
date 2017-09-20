package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.Gdx;
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
import com.udacity.gamedev.gigagal.app.SaveData;
import com.udacity.gamedev.gigagal.util.AssetManager;
import com.udacity.gamedev.gigagal.util.InputControls;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.*;
import com.udacity.gamedev.gigagal.util.Helpers;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

// mutable
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
    private Material weapon;
    private List<Material> weaponList; // class-level instantiation
    private ListIterator<Material> weaponToggler; // class-level instantiation
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
    private boolean canBounce;
    private boolean canMove;
    private boolean canFlip;
    private boolean canRush;
    private boolean canCut;
    private long chargeStartTime;
    private long standStartTime;
    private long lookStartTime;
    private long fallStartTime;
    private long jumpStartTime;
    private long dashStartTime;
    private long hoverStartTime;
    private long rappelStartTime;
    private long climbStartTime;
    private long strideStartTime;
    private long recoveryStartTime;
    private long swipeStartTime;
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
    private float chargeModifier;
    private float startTurbo;
    private float turbo;
    private float fallLimit;
    private float ammo;
    private float health;
    private int lives;
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
        weaponList = new ArrayList<Material>();
        weaponToggler = weaponList.listIterator();
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
            addWeapon(Material.GAS);
            weapon = weaponToggler.previous();
        }

        String savedUpgrades = SaveData.getUpgrades();
        if (!savedUpgrades.equals(Material.NATIVE.name())) {
            List<String> savedUpgradesList = Arrays.asList(savedUpgrades.split(", "));
            for (String upgradeString : savedUpgradesList) {
                addUpgrade(Upgrade.valueOf(upgradeString));
            }
        } else {
            addUpgrade(Upgrade.NONE);
        }
        dispenseUpgrades();
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
        canDispatch = false;
        canSink = false;
        canMove = false;
        canBounce = false;
        canFlip = false;
        canRush = false;
        canCut = false;
        chargeStartTime = 0;
        strideStartTime = 0;
        climbStartTime = 0;
        jumpStartTime = 0;
        fallStartTime = 0;
        dashStartTime = 0;
        swipeStartTime = 0;
        standStartTime = TimeUtils.nanoTime();
        recoveryStartTime = TimeUtils.nanoTime();
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
                enableShoot(weapon);
                enableSwipe();
            } else if (action == Action.STRIDING) {
                enableStride();
                enableDash();
                enableJump();
                enableShoot(weapon);
                enableSwipe();
            } else if (action == Action.CLIMBING) {
                enableClimb();
                enableShoot(weapon);
                enableSwipe();
            } else if (action == Action.DASHING) {
                enableDash();
                enableJump();
                enableShoot(weapon);
                enableSwipe();
            }
        } else if (groundState == GroundState.AIRBORNE) {
            velocity.y -= Constants.GRAVITY;
            if (action == Action.FALLING) {
                fall();
                enableClimb();
                enableHover();
                enableRappel();
                enableShoot(weapon);
                enableSwipe();
            } else if (action == Action.JUMPING) {
                enableJump();
                enableClimb();
                enableRappel();
                enableShoot(weapon);
                enableSwipe();
            } else if (action == Action.HOVERING) {
                enableHover();
                enableRappel();
                enableClimb();
                enableShoot(weapon);
                enableSwipe();
            } else if (action == Action.RAPPELLING) {
                enableJump();
                enableRappel();
                enableClimb();
                enableShoot(weapon);
                enableSwipe();
            } else if (action == Action.RECOILING) {
                enableRappel();
                enableShoot(weapon);
                enableSwipe();
            }
        }
//
//        if (touchedGround != null)
//        Gdx.app.log(TAG + "3", touchedGround.getClass().toString());
    }


    private void enableSwipe() {
        if (!canRush && !canCut && (groundState == GroundState.AIRBORNE || action == Action.CLIMBING) && (inputControls.downButtonPressed || inputControls.upButtonPressed)) {
            if (inputControls.jumpButtonJustPressed && action != Action.RAPPELLING) {
                resetChaseCamPosition();
                lookStartTime = TimeUtils.nanoTime();
                canFlip = true;
                bladeState = BladeState.FLIP;
            }
        } else if (canFlip) { // manual deactivation by shoot button release
            AssetManager.getInstance().getSoundAssets().getMaterialSound(weapon).stop();
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
                resetChaseCamPosition();
                lookStartTime = TimeUtils.nanoTime();
                canRush = true;
                bladeState = BladeState.RUSH;
            }
        } else if (canRush) {  // manual deactivation by dash interrupt
            AssetManager.getInstance().getSoundAssets().getMaterialSound(weapon).stop();
            swipeStartTime = 0;
            swipeTimeSeconds = 0;
            canRush = false;
            bladeState = BladeState.RETRACTED;
        }

        if (!canFlip && !canRush && groundState == GroundState.PLANTED && action != Action.CLIMBING && (inputControls.downButtonPressed || inputControls.upButtonPressed)) {
            if (inputControls.jumpButtonPressed) {
                resetChaseCamPosition();
                lookStartTime = TimeUtils.nanoTime();
                canCut = true;
                bladeState = BladeState.CUT;
            }
        } else if (canCut) {
            AssetManager.getInstance().getSoundAssets().getMaterialSound(weapon).stop();
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
                if (directionY == Direction.UP) {
                    if (velocity.y < Constants.AVATAR_MAX_SPEED) {
                        velocity.y += Constants.AVATAR_MAX_SPEED / 2.25f;
                    }
                } else if (directionY == Direction.DOWN) {
                    if (velocity.y < Constants.AVATAR_MAX_SPEED) {
                        velocity.y += Constants.AVATAR_MAX_SPEED / 4.5f;
                    }
                    if (velocity.x < Constants.AVATAR_MAX_SPEED) {
                        velocity.x += Helpers.absoluteToDirectionalValue(Constants.AVATAR_MAX_SPEED / 2.25f, directionX, Orientation.X);
                    }
                }
            } else if (swipeTimeSeconds < Constants.FLIPSWIPE_FRAME_DURATION * 5) {
                AssetManager.getInstance().getSoundAssets().getMaterialSound(weapon).play();
                swipeTimeSeconds = Helpers.secondsSince(swipeStartTime);
            } else { // auto deactivation when animation completes
                AssetManager.getInstance().getSoundAssets().getMaterialSound(weapon).stop();
                swipeStartTime = 0;
                swipeTimeSeconds = 0;
                canFlip = false;
                bladeState = BladeState.RETRACTED;
                if (inputControls.jumpButtonPressed && chargeTimeSeconds > Constants.BLAST_CHARGE_DURATION) {
                    shoot(shotIntensity, weapon, Helpers.useAmmo(shotIntensity));
                }
            }
        }

        if (canRush) {
            if (swipeStartTime == 0) {
                swipeStartTime = TimeUtils.nanoTime();
                swipeTimeSeconds = 0;
            } else if (swipeTimeSeconds < Constants.FLIPSWIPE_FRAME_DURATION * 3) {
                AssetManager.getInstance().getSoundAssets().getMaterialSound(weapon).play();
                swipeTimeSeconds = Helpers.secondsSince(swipeStartTime);
            } else { // auto deactivation when animation completes
                AssetManager.getInstance().getSoundAssets().getMaterialSound(weapon).stop();
                swipeStartTime = 0;
                swipeTimeSeconds = 0;
                canRush = false;
                bladeState = BladeState.RETRACTED;
                if (chargeTimeSeconds > Constants.BLAST_CHARGE_DURATION && action == Action.DASHING) {
                    shoot(shotIntensity, weapon, Helpers.useAmmo(shotIntensity));
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
                AssetManager.getInstance().getSoundAssets().getMaterialSound(weapon).play();
                swipeTimeSeconds = Helpers.secondsSince(swipeStartTime);
            } else { // auto deactivation when animation completes
                AssetManager.getInstance().getSoundAssets().getMaterialSound(weapon).stop();
                swipeStartTime = 0;
                swipeTimeSeconds = 0;
                canCut = false;
                bladeState = BladeState.RETRACTED;
                if (chargeTimeSeconds > Constants.BLAST_CHARGE_DURATION && inputControls.jumpButtonPressed) {
                    shoot(shotIntensity, weapon, Helpers.useAmmo(shotIntensity));
                }
                canDash = false;
                stand();
            }
        }
    }

    private void setBounds() {
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

        setBounds();
        detectInput();
    }

    public void touchAllGrounds(Array<Ground> grounds) {
        for (Ground ground : grounds) {
            touchGround(ground);
        }
        untouchGround();
    }

    private void touchGround(Groundable g) {
        if (Helpers.overlapsPhysicalObject(this, g)) {// if overlapping ground boundaries
            if (g.isDense()) { // for dense grounds: apply side, bottom collision and top collisionouchGroundBottom(ground);
                touchGroundBottom(g);
                touchGroundSide(g);
                touchGroundTop(g);
            } else { // for non-dense grounds:
                // additional ground collision instructions specific to certain types of grounds
                if (g instanceof Climbable) {
                    if (!(touchedGround != null && touchedGround.isDense() && touchedGround.getTop() == g.getTop())) { // prevents flickering canclimb state
                        canCling = true;
                    }
                    if (!(!canClimb && groundState == GroundState.PLANTED && touchedGround instanceof Skateable) // prevents from overriding handling of simultaneously touched skateable ground i.e. overriding ground physics
                            && (!(groundState == GroundState.AIRBORNE && touchedGround instanceof Rappelable))) { // prevents from overriding handling of simultaneously touched rappelable ground i.e. for rappel position reset)
                        if (!(g instanceof Unsteady) || (touchedGround == null || (!(touchedGround != null && !touchedGround.equals(g) && touchedGround.isDense() && action != Action.CLIMBING)))) {
                            carriedGround = null;
                            touchedGround = g; // saves for untouchground where condition within touchgroundtop unmet
                            if (canClimb && !inputControls.jumpButtonPressed && action == Action.STANDING) {
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
                                            setAtopGround(g);
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
                    canCling = false;
                    canClimb = false;
                    canSink = true;
                    canDash = false;
                    canHover = false;
                    lookStartTime = 0;
                    lookTimeSeconds = 0;
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
                    if ((Math.abs(velocity.x) >= Constants.AVATAR_MAX_SPEED / 8) || g instanceof Hazard) {
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
                } else if (groundState == GroundState.PLANTED) { // only when planted
                    if (Math.abs(getBottom() - g.getTop()) > 1) {
                        strideSpeed = 0;
                        velocity.x = 0;
                    }
                    if (g instanceof Pliable) {
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
                    // reset stride acceleration, disable stride and dash, and set gigagal at ground side
                    if (action != Action.STRIDING || action != Action.DASHING) {
                        strideStartTime = 0; // reset stride acceleration
                    }
                    canStride = false; // disable stride
                    canDash = false; // disable dash
                    position.x = previousFramePosition.x;
                }
            } else { // when both position and previous position overlap ground side edge

                float yTestPosition = position.y;
                if (g instanceof Cannoroll) {
                    yTestPosition = getBottom() + Constants.AVATAR_HEAD_RADIUS; // for canirol only
                }
                if (!(g instanceof Pliable) || (action == Action.FALLING && !((Pliable) g).isBeingCarried() && (!(((Pliable) g).isAtopMovingGround() && ((Pliable) g).getMovingGround() instanceof Pliable && g.getPosition().dst(position) < getHeight() / 2)))) {
                    if (Helpers.betweenTwoValues(yTestPosition, g.getBottom(), g.getTop())) { // when test position is between ground top and bottom (to prevent resetting to grounds simultaneously planted upon)
                        if (!(g instanceof Cannoroll)) {
                            if (Math.abs(position.x - g.getLeft()) < Math.abs(position.x - g.getRight())) {
                                position.x = g.getLeft() - getHalfWidth() - 1; // reset position to ground side edge
                            } else {
                                position.x = g.getRight() + getHalfWidth() + 1; // reset position to ground side edge
                            }
                        } else { // for canirol only
                            position.y = g.getTop() + Constants.AVATAR_EYE_HEIGHT; // reset position to ground top
                            setAtopGround(g);
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
            // if contact with ground bottom detected, halts upward progression and set gigagal at ground bottom
            if ((previousFramePosition.y + Constants.AVATAR_HEAD_RADIUS) < g.getBottom() + 1) {
                velocity.y = 0; // prevents from ascending above ground bottom
                if (groundState == GroundState.AIRBORNE) { // prevents fall when striding against ground bottom positioned at height distance from ground atop
                    fall(); // descend from point of contact with ground bottom
                    if (!(g instanceof Moving && ((Moving) g).getVelocity().y < 0)) { // prevents from being pushed below ground
                        position.y = g.getBottom() - Constants.AVATAR_HEAD_RADIUS;  // sets gigagal at ground bottom
                    }
                } else if (action == Action.CLIMBING) { // prevents from disengaging climb
                    canCling = true;
                    canClimb = true;
                    action = Action.CLIMBING;
                    groundState = GroundState.PLANTED;
                    if (!(g instanceof Moving && ((Moving) g).getVelocity().y < 0)) { // prevents from being pushed below ground
                        position.y = g.getBottom() - Constants.AVATAR_HEAD_RADIUS;  // sets gigagal at ground bottom
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
            // if contact with ground top detected, halt downward progression and set gigagal atop ground
            if (previousFramePosition.y - Constants.AVATAR_EYE_HEIGHT >= g.getTop() - 2) { // and not simultaneously touching two different grounds (prevents stand which interrupts striding atop)
                if ((Helpers.overlapsBetweenTwoSides(position.x, halfWidth, g.getLeft() + 1, g.getRight() - 1) || action != Action.FALLING || g instanceof Aerial)) { // prevents interrupting fall when inputting x directional against and overlapping two separate ground side
                    if (!((touchedGround instanceof Moving && ((Moving) touchedGround).getVelocity().y != 0) || (g instanceof Moving && ((Moving) g).getVelocity().y != 0)) && (action != Action.CLIMBING || getBottom() <= g.getTop())) {
                        velocity.y = 0; // velocity reset for climbing from touchground()
                        position.y = g.getTop() + Constants.AVATAR_EYE_HEIGHT; // sets Gigagal atop ground
                    }
                    setAtopGround(g); // basic ground top collision instructions common to all types of grounds
                    // additional ground top collision instructions specific to certain types of grounds
                    if (g instanceof Skateable) {
                        if (groundState == GroundState.AIRBORNE) {
                            stand(); // set groundstate to standing
                            lookStartTime = 0;
                        } else if (canClimb) {
                            canCling = false;
                        }
                    }
                    if (g instanceof Moving) {
                        Moving moving = (Moving) g;
                        lookStartTime = 0;
                        position.y = g.getTop() + Constants.AVATAR_EYE_HEIGHT;
                        velocity.x = ((Moving) g).getVelocity().x;
                        velocity.y = ((Moving) g).getVelocity().y;
//                        Gdx.app.log(TAG, position.toString() + velocity.toString() + g.getPosition() + ((Moving) g).getVelocity());
                        if (moving instanceof Pliable) {
                            if (((Pliable) moving).isAtopMovingGround() && (touchedGround == null || !touchedGround.equals(((Pliable) moving).getMovingGround()))) { // atop pliable which is atop moving ground and not simultaneously touching both
                                Pliable pliable = (Pliable) moving;
                                if (!pliable.isBeingCarried() && directionY == Direction.DOWN && lookStartTime != 0) {
                                    if (InputControls.getInstance().shootButtonJustPressed) {
                                        fall();
                                    }
                                }
                            }
                            if (inputControls.downButtonPressed) {
                                canMove = true;
                            }
                        }
                    }
                    if (g instanceof Reboundable) {
                        if (!(g instanceof Pliable && ((Pliable) g).isBeingCarried() && ((Pliable) g).getCarrier() == this)) {
                            canClimb = false;
                            canCling = false;
                        }
                    }
                    if (g instanceof Destructible) {
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
        if (groundState == GroundState.AIRBORNE && !(g instanceof Skateable)) {
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
                canCling = false;
            }
        }
    }

    private void untouchGround() {
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
        canPeer = false;
        for (Hazard hazard : hazards) {
            if (!(hazard instanceof Ammo && ((Ammo) hazard).getSource() instanceof Avatar)) {
                if (Helpers.overlapsPhysicalObject(this, hazard)) {
                    touchHazard(hazard);
                } else if (action == Action.STANDING) {
                    if (position.dst(hazard.getPosition()) < Constants.WORLD_SIZE
                            && Helpers.absoluteToDirectionalValue(position.x - hazard.getPosition().x, directionX, Orientation.X) > 0) {
                        canPeer = true;
                    }
                }
            }
        }
    }

    private void touchHazard(Hazardous h) {
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
                if (getBottom() >= h.getPosition().y && Helpers.betweenTwoValues(position.x, h.getPosition().x - Constants.SWOOPA_SHOT_RADIUS, h.getPosition().x + Constants.SWOOPA_SHOT_RADIUS)) {
                    touchGroundTop((Swoopa) h);
                } else {
                    touchedHazard = h;
                    recoil(h.getKnockback(), h);
                }
            } else if (h instanceof Armored && ((Armored) h).isVulnerable()) {
                if (h instanceof Bladed && Helpers.secondsSince(((Armored) h).getStartTime()) > (((Armored) h).getRecoverySpeed() - Constants.FLIPSWIPE_FRAME_DURATION * 6)) {
                    Gdx.app.log(TAG, "?" +  ((Bladed) h).getEquippedRegions().toString());
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

    public void touchAllPowerups(Array<Powerup> powerups) {
        for (Powerup powerup : powerups) {
            Rectangle bounds = new Rectangle(powerup.getLeft(), powerup.getBottom(), powerup.getWidth(), powerup.getHeight());
            if (getBounds().overlaps(bounds)) {
                touchPowerup(powerup);
            }
        }
        if (turbo > Constants.MAX_TURBO) {
            turbo = Constants.MAX_TURBO;
        }
    }

    private void touchPowerup(Replenishing r) {
        switch(r.getType()) {
            case AMMO:
                AssetManager.getInstance().getSoundAssets().ammo.play();
                ammo += Constants.POWERUP_AMMO;
                if (ammo > Constants.MAX_AMMO) {
                    ammo = Constants.MAX_AMMO;
                }
                break;
            case HEALTH:
                if (r instanceof Powerup) {
                    AssetManager.getInstance().getSoundAssets().health.play();
                    health += Constants.POWERUP_HEALTH;
                } else {
                    health += .1f;
                }
                if (health > Constants.MAX_HEALTH) {
                    health = Constants.MAX_HEALTH;
                }
                break;
            case TURBO:
                AssetManager.getInstance().getSoundAssets().turbo.play();
                turbo += Constants.POWERUP_TURBO;
                if (action == Action.HOVERING) {
                    hoverStartTime = TimeUtils.nanoTime();
                }
                if (action == Action.DASHING) {
                    dashStartTime = TimeUtils.nanoTime();
                }
                break;
            case LIFE:
                AssetManager.getInstance().getSoundAssets().life.play();
                lives += 1;
                break;
            case CANNON:
                AssetManager.getInstance().getSoundAssets().cannon.play();
                chargeModifier = 1;
                ammo += Constants.POWERUP_AMMO;
                break;
        }
    }

    private void handleXInputs() {
        boolean left = inputControls.leftButtonPressed;
        boolean right = inputControls.rightButtonPressed;
        boolean directionChanged = false;
        boolean inputtingX = ((left || right) && !(left && right));
        if (inputtingX && lookStartTime == 0) {
            if (left && !right) {
                directionChanged = Helpers.changeDirection(this, Direction.LEFT, Orientation.X);
            } else if (!left && right) {
                directionChanged = Helpers.changeDirection(this, Direction.RIGHT, Orientation.X);
            }
            jumpStartTime = 0;
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
                if (!canRappel && !canHurdle && !getSwipeStatus()) { // prevents accidental toggle due to simultaneous jump and directional press
                    if (((inputControls.downButtonJustPressed && inputControls.upButtonPressed) || (inputControls.upButtonJustPressed && inputControls.downButtonPressed)) && inputControls.shootButtonPressed) {
                        lookStartTime = 0;
                        toggleWeapon(directionY);
                        chargeStartTime = 0;
                        chargeTimeSeconds = 0;
                        canShoot = false; // prevents discharge only if releasing shoot before y input due to stand() condition
                    }
                }
                look(); // also sets chase cam
            }
            jumpStartTime = 0;
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
            velocity.x += Helpers.absoluteToDirectionalValue(Constants.TREADMILL_SPEED, ((Propelling) touchedGround).getDirectionX(), Orientation.X);
        } else if (!(touchedGround instanceof Moving && ((Moving) touchedGround).getVelocity().x != 0)) {
            velocity.x = 0;
        }
        fallStartTime = 0;
        action = Action.STANDING;
        groundState = GroundState.PLANTED;

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
        if (fallStartTime == 0) {
            fallStartTime = TimeUtils.nanoTime();
        }
        fallTimeSeconds = Helpers.secondsSince(fallStartTime);
        if (!(touchedGround instanceof Skateable)) {
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

        canSink = false;

        if (turbo < Constants.MAX_TURBO) {
            turbo += Constants.FALL_TURBO_INCREMENT;
        }
    }

    // disables all else by virtue of neither top level update conditions being satisfied due to state
    private void recoil(Vector2 velocity, Hazardous hazard) {
        if (Helpers.secondsSince(recoveryStartTime) > Constants.RECOVERY_TIME) {
            float xRelationship = Math.signum(position.x - hazard.getPosition().x);
            if (xRelationship != 0) {
                this.velocity.x = velocity.x * xRelationship;
            } else {
                this.velocity.x = Helpers.absoluteToDirectionalValue(velocity.x, directionX, Orientation.X);
            }
            this.velocity.y = velocity.y;
            AssetManager.getInstance().getSoundAssets().damage.play();
            shotIntensity = ShotIntensity.NORMAL;
            health -= hazard.getDamage() * healthMultiplier;
            groundState = GroundState.AIRBORNE;
            action = Action.RECOILING;
            recoveryStartTime = TimeUtils.nanoTime();
            chargeModifier = 0;
            chargeStartTime = 0;
            strideStartTime = 0;
            lookStartTime = 0;
            turbo = 0;
            canStride = false;
            canDash = false;
            canHover = false;
            canCling = false;
            canClimb = false;
            canRappel = false;
            canHurdle = false;
        }
    }

    private void enableShoot(Material weapon) {
        canDispatch = false;
        if (canShoot) {
            if (inputControls.shootButtonPressed || (action == Action.RAPPELLING && (inputControls.rightButtonPressed || inputControls.leftButtonPressed))) {
                if (chargeStartTime == 0) {
                    chargeStartTime = TimeUtils.nanoTime();
                } else if (chargeTimeSeconds > Constants.BLAST_CHARGE_DURATION) {
                    shotIntensity = ShotIntensity.BLAST;
                } else if (chargeTimeSeconds > Constants.BLAST_CHARGE_DURATION / 3) {
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
                chargeStartTime = 0;
                chargeTimeSeconds = 0;
                shoot(shotIntensity, weapon, ammoUsed);
            }
        }
    }

    public void shoot(ShotIntensity shotIntensity, Material weapon, int ammoUsed) {
        canDispatch = true;
        if (shotIntensity == ShotIntensity.BLAST) {
            AssetManager.getInstance().getSoundAssets().getMaterialSound(weapon).play();
        } else {
            AssetManager.getInstance().getSoundAssets().getMaterialSound(weapon).play(1, 2, 0);
        }
        ammo -= ammoUsed * ammoMultiplier;
    }

    private void look() {
        float offset = 0;
        if (lookStartTime == 0 && !canRush) {
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

    public void stride() {
        action = Action.STRIDING;
        groundState = GroundState.PLANTED;
        if (turbo < Constants.MAX_TURBO) {
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
        velocity.x = Helpers.absoluteToDirectionalValue(Math.min(Constants.AVATAR_MAX_SPEED * strideAcceleration + Constants.AVATAR_STARTING_SPEED, Constants.AVATAR_MAX_SPEED * strideMultiplier), directionX, Orientation.X);
        if (touchedGround instanceof Propelling) {
            velocity.x += Helpers.absoluteToDirectionalValue(Constants.TREADMILL_SPEED, ((Propelling) touchedGround).getDirectionX(), Orientation.X);
        } else if (touchedGround instanceof Skateable) {
            velocity.x = strideSpeed + Helpers.absoluteToDirectionalValue(Math.min(Constants.AVATAR_MAX_SPEED * strideAcceleration / 2 + Constants.AVATAR_STARTING_SPEED, Constants.AVATAR_MAX_SPEED * 2), directionX, Orientation.X);
        } else if (canSink) {
            velocity.x = Helpers.absoluteToDirectionalValue(10, directionX, Orientation.X);
            velocity.y = -3;
        }
    }

    private void enableDash() {
        handleXInputs();
        if (canDash) {
            dash();
        } else if (action == Action.DASHING) {
            dash();
            canDash = true; // false for one frame for triptread activation from level updater
        }
    }

    private void dash() {
        if (action != Action.DASHING) {
            startTurbo = turbo;
            action = Action.DASHING;
            groundState = GroundState.PLANTED;
            dashStartTime = TimeUtils.nanoTime();
            strideStartTime = 0;
            canStride = false;
            canDash = false;
        } else if (turbo >= 1) {
            dashTimeSeconds = Helpers.secondsSince(dashStartTime);
            turbo -= Constants.DASH_TURBO_DECREMENT * turboMultiplier;
            velocity.x = Helpers.absoluteToDirectionalValue(Constants.AVATAR_MAX_SPEED, directionX, Orientation.X);
        } else {
            canDash = false;
            dashStartTime = 0;
            stand();
        }
        if (touchedGround instanceof Skateable
        || (touchedGround instanceof Propelling && directionX == ((Propelling) touchedGround).getDirectionX())) {
            velocity.x = Helpers.absoluteToDirectionalValue(Constants.AVATAR_MAX_SPEED + Constants.TREADMILL_SPEED, directionX, Orientation.X);
        }
    }

    private void enableJump() {
        if (canJump && action != Action.JUMPING) {
            if (jumpStartTime != 0 && action == Action.STANDING) {
                if (inputControls.jumpButtonPressed) {
                    if (startTurbo == 0) {
                        startTurbo = turbo;
                        Gdx.app.log(TAG, startTurbo + "1");
                    }
                    if (turbo > 0) {
                        turbo -= Constants.HOVER_TURBO_DECREMENT;
                    }
                } else if (Helpers.secondsSince(jumpStartTime) > 1.75f) {
                    jump();
                    velocity.x += Helpers.absoluteToDirectionalValue(Constants.AVATAR_MAX_SPEED / 8, directionX, Orientation.X);
                    Gdx.app.log(TAG, startTurbo + "2");
                    velocity.y *= (1 + (startTurbo/100 * .35f)) * jumpMultiplier;
                    Gdx.app.log(TAG, velocity.y + "3");
                    startTurbo = 0;
                    jumpStartTime = 0;
                } else {
                    startTurbo = 0;
                    jumpStartTime = 0;
                }
            } else if (inputControls.jumpButtonJustPressed && lookStartTime == 0) {
                startTurbo = 0;
                jump();
            }
        }
    }

    private void jump() {
        if (canJump) {
            action = Action.JUMPING;
            groundState = GroundState.AIRBORNE;
            if (jumpStartTime <= 1.75f) {
                jumpStartTime = TimeUtils.nanoTime();
            }
            canJump = false;
        }
        velocity.x += Helpers.absoluteToDirectionalValue(Constants.AVATAR_STARTING_SPEED * Constants.STRIDING_JUMP_MULTIPLIER, directionX, Orientation.X);
        velocity.y = Constants.JUMP_SPEED;
        velocity.y *= Constants.STRIDING_JUMP_MULTIPLIER;

        Gdx.app.log(TAG, velocity.y + "3");
        if (touchedGround instanceof Reboundable) {
            if (!(touchedGround instanceof Pliable && ((Pliable) touchedGround).isBeingCarried() && ((Pliable) touchedGround).getCarrier() == this)) {
                velocity.y *= ((Reboundable) touchedGround).jumpMultiplier();
                jumpStartTime = 0;
            }
            action = Action.FALLING; // prevents from rendering stride sprite when striding against ground side and jumping on reboundable
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
            jumpStartTime = 0;
            startTurbo = turbo;
            action = Action.HOVERING; // indicates currently hovering
            groundState = GroundState.AIRBORNE;
            hoverStartTime = TimeUtils.nanoTime(); // begins timing hover duration
        }
        hoverTimeSeconds = Helpers.secondsSince(hoverStartTime); // for comparing with max hover time
        if (turbo >= 1) {
            velocity.y = 0; // disables impact of gravity
            turbo -= Constants.HOVER_TURBO_DECREMENT * turboMultiplier;
        } else {
            canHover = false;
            fall(); // when max hover time is exceeded
        }
        handleXInputs();
    }

    private void enableRappel() {
        if (action == Action.RAPPELLING) {
            rappel();
        } else if (canRappel) {
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
            if (!Helpers.movingOppositeDirection(velocity.x, directionX, Orientation.X)) {
                directionX = Helpers.getOppositeDirection(directionX);
            }
            hoverStartTime = 0;
            canJump = true;
            canRappel = false;
        }
        if (touchedGround != null) {
            if (directionX == Direction.LEFT) {
                position.x = touchedGround.getLeft() - getHalfWidth();
            } else {
                position.x = touchedGround.getRight() + getHalfWidth();
            }
        }
        float rappelTimeSeconds = Helpers.secondsSince(rappelStartTime);
        if (!inputControls.jumpButtonPressed) {
            if (rappelTimeSeconds >= Constants.RAPPEL_FRAME_DURATION) {
                velocity.x = Helpers.absoluteToDirectionalValue(Constants.AVATAR_MAX_SPEED, directionX, Orientation.X);
                if (!(touchedGround instanceof Skateable)) {
                    jump();
                } else {
                    fall();
                }
            } else {
                canHover = true;
            }
            canHurdle = false;
        } else {
            lookStartTime = 0;
            boolean yMoving = false;
            if (touchedGround instanceof Moving && ((Moving) touchedGround).getVelocity().y != 0) {
                yMoving = true;
            }
            if (touchedGround instanceof Pliable) {
                if (Helpers.inputToDirection() == Helpers.getOppositeDirection(directionX)) {
                    canMove = true;
                } else {
                    canMove = false;
                }
                if (((Pliable) touchedGround).isBeneatheGround()) { // if touchedground y is moving but not touchedground moving ground
                    canHurdle = false;
                }
                if (((Pliable) touchedGround).getMovingGround() != null && ((Pliable) touchedGround).getMovingGround().getVelocity().y != 0) {
                    touchedGround = (Groundable) ((Pliable) touchedGround).getMovingGround();
                    yMoving = true;
                }
            }

            if (!(touchedGround == null || touchedGround instanceof Skateable)) {
                if (inputControls.downButtonPressed && (touchedGround instanceof Aerial || !yMoving)) {
                    velocity.y += Constants.RAPPEL_GRAVITY_OFFSET;
                } else if (inputControls.upButtonPressed && canHurdle) {
                    canHurdle = false;
                    canRappel = false;
                    directionX = Helpers.getOppositeDirection(directionX);
                    velocity.x = Helpers.absoluteToDirectionalValue(Constants.CLIMB_SPEED / 2, directionX, Orientation.X);
                    float jumpBoost = 0;
                    if (yMoving) {
                        jumpBoost = Math.abs(((Moving) touchedGround).getVelocity().y);
                    }
                    jump();
                    velocity.y += jumpBoost;
                    Gdx.app.log(TAG, velocity.y + "vY " + velocity.x + "vX");
                } else if (turbo < 1) {
                    turbo = 0;
                    velocity.y += Constants.RAPPEL_GRAVITY_OFFSET;
                } else {
                    if (!canHurdle && !yMoving) {
                        turbo -= Constants.RAPPEL_TURBO_DECREMENT * turboMultiplier;
                    }
                    if (touchedGround instanceof Treadmill) {
                        turbo -= 2;
                    }
                    if (yMoving) {
                        velocity.y = ((Moving) touchedGround).getVelocity().y;
                    } else {
                        velocity.y = 0;
                    }
                }
            }
        }
    }

    private void enableClimb() {
        if (canCling)  {
            if (action != Action.RAPPELLING || inputControls.upButtonPressed) {
                // when overlapping all but top, set canrappel which if action enablesclimb will set canclimb to true
                if (inputControls.jumpButtonPressed) {
                    if (lookStartTime == 0) { // cannot initiate climb if already looking; must first neutralize
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
        } else {
            if (action == Action.CLIMBING) {
                fall();
                if (!(touchedGround instanceof Climbable && Helpers.overlapsBetweenTwoSides(position.x, getHalfWidth(), touchedGround.getLeft(), touchedGround.getRight())))  {
                    velocity.x = Helpers.absoluteToDirectionalValue(Constants.CLIMB_SPEED, directionX, Orientation.X);
                }
            }
        }
    }

    private void climb(Orientation orientation) {
        if (canCling) { // canrappel set to false from handleYinputs() if double tapping down
            if (action != Action.CLIMBING) { // at the time of climb initiation
                climbStartTime = 0; // overrides assignment of current time preventing nanotime - climbstarttime < doubletapspeed on next handleY() call
                groundState = GroundState.PLANTED;
                action = Action.CLIMBING;
            }
            resetChaseCamPosition();
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
        Array<TextureRegion> body = new Array<TextureRegion>();
        boolean flip = directionX == Direction.LEFT;
        boolean frontFacing = true;
        TextureRegion torso = null;
        TextureRegion legs = null;
        TextureRegion backArm = null;
        TextureRegion frontArm = null;
        Animation shoot = null;

        switch (action) {
            case STANDING:
                torso = AssetManager.getInstance().getAvatarAssets().torso.getKeyFrame(0);
                legs = AssetManager.getInstance().getAvatarAssets().legsStand;
                shoot = AssetManager.getInstance().getAvatarAssets().pointForward;
                backArm = getBackArm(AssetManager.getInstance().getAvatarAssets().relax);
                frontArm = getFrontArm(shoot, AssetManager.getInstance().getAvatarAssets().release);
                break;
            case STRIDING:
                float strideFrame = Math.min(strideAcceleration * strideAcceleration, strideAcceleration);
                torso = AssetManager.getInstance().getAvatarAssets().torso.getKeyFrame(strideTimeSeconds * Math.max(Math.abs(velocity.x / Constants.AVATAR_MAX_SPEED), .33f));
                legs = AssetManager.getInstance().getAvatarAssets().legsStride.getKeyFrame(strideFrame);
                shoot = AssetManager.getInstance().getAvatarAssets().pointForward;
                backArm = getBackArm(AssetManager.getInstance().getAvatarAssets().armSwing.getKeyFrame(strideFrame / 6));
                frontArm = getFrontArm(shoot, AssetManager.getInstance().getAvatarAssets().armCurl.getKeyFrame(strideFrame / 6));
                break;
            case CLIMBING:
                frontFacing = false;
                torso = AssetManager.getInstance().getAvatarAssets().torsoClimb;
                legs = AssetManager.getInstance().getAvatarAssets().legsClimb;
                shoot = AssetManager.getInstance().getAvatarAssets().pointForward;
                backArm = AssetManager.getInstance().getAvatarAssets().hold;
                if (lookStartTime != 0 || inputControls.shootButtonPressed) {
                    backArm = AssetManager.getInstance().getAvatarAssets().obfuscated;
                }
                frontArm = getFrontArm(shoot, AssetManager.getInstance().getAvatarAssets().obfuscated);
                break;
            case DASHING:
                torso = AssetManager.getInstance().getAvatarAssets().torso.getKeyFrame(dashTimeSeconds * Math.max(Math.abs(velocity.x / Constants.AVATAR_MAX_SPEED), .33f));
                legs = AssetManager.getInstance().getAvatarAssets().legsDash.getKeyFrame(dashTimeSeconds);
                shoot = AssetManager.getInstance().getAvatarAssets().pointForward;
                backArm = getBackArm(AssetManager.getInstance().getAvatarAssets().relax);
                frontArm = getFrontArm(shoot, AssetManager.getInstance().getAvatarAssets().armCurl.getKeyFrame(2));
                break;
            case FALLING:
                torso = AssetManager.getInstance().getAvatarAssets().torso.getKeyFrame(fallTimeSeconds * Math.max(Math.abs(velocity.x / Constants.AVATAR_MAX_SPEED), .33f));
                legs = AssetManager.getInstance().getAvatarAssets().legsFall;
                shoot = AssetManager.getInstance().getAvatarAssets().pointForward;
                backArm = getBackArm(AssetManager.getInstance().getAvatarAssets().reach);
                frontArm = getFrontArm(shoot, AssetManager.getInstance().getAvatarAssets().pointForward.getKeyFrame(0));
                break;
            case JUMPING:
                torso = AssetManager.getInstance().getAvatarAssets().torso.getKeyFrame(0);
                legs = AssetManager.getInstance().getAvatarAssets().legsFall;
                shoot = AssetManager.getInstance().getAvatarAssets().pointForward;
                backArm = getBackArm(AssetManager.getInstance().getAvatarAssets().reach);
                frontArm = getFrontArm(shoot, AssetManager.getInstance().getAvatarAssets().pointForward.getKeyFrame(0));
                break;
            case TWISTING:
                break;
            case HOVERING:
                torso = AssetManager.getInstance().getAvatarAssets().torso.getKeyFrame(hoverTimeSeconds * Math.max(Math.abs(velocity.x / Constants.AVATAR_MAX_SPEED), .33f));
                legs = AssetManager.getInstance().getAvatarAssets().legsHover.getKeyFrame(hoverTimeSeconds);
                shoot = AssetManager.getInstance().getAvatarAssets().pointForward;
                backArm = getBackArm(AssetManager.getInstance().getAvatarAssets().relax);
                frontArm = getFrontArm(shoot, AssetManager.getInstance().getAvatarAssets().release);
                break;
            case RAPPELLING:
                torso = AssetManager.getInstance().getAvatarAssets().torso.getKeyFrame(0);
                legs = AssetManager.getInstance().getAvatarAssets().legsRappel;
                shoot = AssetManager.getInstance().getAvatarAssets().pointForward;
                backArm = getBackArm(AssetManager.getInstance().getAvatarAssets().reach);
                frontArm = getFrontArm(shoot, AssetManager.getInstance().getAvatarAssets().release);
                break;
            case RECOILING:
                torso = AssetManager.getInstance().getAvatarAssets().torsoRecoil;
                legs = AssetManager.getInstance().getAvatarAssets().legsRecoil;
                shoot = AssetManager.getInstance().getAvatarAssets().pointForward;
                backArm = getBackArm(AssetManager.getInstance().getAvatarAssets().armSwing.getKeyFrame(0));
                frontArm = getFrontArm(shoot, AssetManager.getInstance().getAvatarAssets().pointForward.getKeyFrame(0));
                break;
        }

        body.add(torso);
        body.add(legs);
        body.add(backArm);
        body.add(frontArm);
        if (!frontFacing) {
            body.reverse();
        }

        for (TextureRegion region : body) {
            Helpers.drawTextureRegion(batch, viewport, region, position, Constants.AVATAR_EYE_POSITION, 1, 0, flip, false);
        }
        body.clear();
    }

    private TextureRegion getFrontArm(Animation shoot, TextureRegion nonShoot) {
        if (lookStartTime != 0) {
            if (directionY == Direction.UP) {
                shoot = AssetManager.getInstance().getAvatarAssets().pointUp;
                nonShoot =  AssetManager.getInstance().getAvatarAssets().pointUp.getKeyFrame(0);
            } else {
                shoot = AssetManager.getInstance().getAvatarAssets().pointDown;
                nonShoot = AssetManager.getInstance().getAvatarAssets().pointDown.getKeyFrame(0);
            }
        }
        if (inputControls.shootButtonPressed) {
            if (shotIntensity == ShotIntensity.NORMAL || chargeModifier != 0) {
                return (shoot.getKeyFrame(0));
            } else if (shotIntensity != ShotIntensity.BLAST) {
                return (shoot.getKeyFrame(chargeTimeSeconds / 1.25f));
            } else {
                return (shoot.getKeyFrame(chargeTimeSeconds / 2));
            }
        } else {
            return nonShoot;
        }
    }

    private TextureRegion getBackArm(TextureRegion backArm) {
        if (lookStartTime != 0) {
            if (directionY == Direction.UP) {
                backArm =  AssetManager.getInstance().getAvatarAssets().clench;
            } else {
                backArm = AssetManager.getInstance().getAvatarAssets().reach;
            }
        }
        return backArm;
    }

    private TextureRegion getCutFrame(BladeState bladeState) {
//        if (bladeState == BladeState.RUSH) {
//            if (inputControls.rightButtonPressed) {
//                region = AssetManager.getInstance().getAvatarAssets().forehand.getKeyFrame(swipeTimeSeconds);
//            } else if (inputControls.leftButtonPressed) {
//                region = AssetManager.getInstance().getAvatarAssets().backhand.getKeyFrame(swipeTimeSeconds);
//            }
//        } else if (bladeState == BladeState.CUT) {
//            region = AssetManager.getInstance().getAvatarAssets().uphand.getKeyFrame(swipeTimeSeconds);
//        } else if (bladeState == BladeState.FLIP) {
//            region = AssetManager.getInstance().getAvatarAssets().backflip.getKeyFrame(swipeTimeSeconds);
//        }
        return null;
    }

    // Getters
    @Override public final Vector2 getPosition() { return position; }
    public final void setPosition(Vector2 position) { this.position.set(position); }
    @Override public final Vector2 getVelocity() { return velocity; }
    public final void setVelocity(Vector2 velocity) { this.velocity = velocity; }
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
    @Override public final Material getWeapon() { return weapon; }
    private final float getHalfWidth() { return halfWidth; }
    public List<Material> getWeaponList() { return weaponList; }
    public List<Upgrade> getUpgrades() { return upgradeList; }
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
    public void setHealth(int health) { this.health = health; }
    public void setTurbo(float turbo) { this.turbo = turbo; }
    public void setMoveStatus(boolean state) { canMove = state; }
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
    public void toggleWeapon(Direction toggleDirection) { // to enable in-game, must discharge blast ammo
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
        setHealth(Constants.MAX_HEALTH);
    }
    public void detectInput() { if (InputControls.getInstance().hasInput()) { standStartTime = TimeUtils.nanoTime(); canPeer = false; } }
    public void setSpawnPosition(Vector2 spawnPosition) { this.spawnPosition.set(spawnPosition); }
    public void resetChargeIntensity() { shotIntensity = ShotIntensity.NORMAL; }
    public void dispose() {
        weaponList.clear();
    }
}
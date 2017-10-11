package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entity.*;
import com.udacity.gamedev.gigagal.overlay.Backdrop;
import com.udacity.gamedev.gigagal.util.AssetManager;
import com.udacity.gamedev.gigagal.util.ChaseCam;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.Direction;
import com.udacity.gamedev.gigagal.util.InputControls;
import com.udacity.gamedev.gigagal.util.Timer;
import com.udacity.gamedev.gigagal.util.Helpers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import sun.security.krb5.internal.crypto.Des;

// immutable package-private singleton
class LevelUpdater {

    // fields
    public static final String TAG = LevelUpdater.class.getName();
    private static final LevelUpdater INSTANCE = new LevelUpdater();
    private AssetManager assetManager;
    private InputControls inputControls;
    private LevelScreen levelScreen;
    private Timer timer;
    private boolean loadEx;
    private Backdrop backdrop;
    private DelayedRemovalArray<Entity> entities;
    private DelayedRemovalArray<Ground> grounds;
    private DelayedRemovalArray<Hazard> hazards;
    private DelayedRemovalArray<Powerup> powerups;
    private DelayedRemovalArray<Transport> transports;
    private DelayedRemovalArray<Impact> impacts;
    private DelayedRemovalArray<Ammo> projectiles;
    private Enums.Material levelWeapon;
    private Enums.Theme theme;
    private Music music;
    private Avatar avatar;
    private Boss boss;
    private ChaseCam chaseCam;
    private String removedHazards;
    private boolean paused;
    private boolean musicEnabled;
    private boolean hintsEnabled;
    private int score;
    private long time;
    private int savedScore;
    private long savedTime;

    // cannot be subclassed
    private LevelUpdater() {}

    // static factory method
    public static LevelUpdater getInstance() { return INSTANCE; }

    protected void create() {

        levelScreen = LevelScreen.getInstance();

        timer = Timer.getInstance();
        timer.create();

        avatar = Avatar.getInstance();
        chaseCam = ChaseCam.getInstance();
        assetManager = AssetManager.getInstance();
        inputControls = InputControls.getInstance();
        entities = new DelayedRemovalArray<Entity>();
        grounds = new DelayedRemovalArray<Ground>();
        hazards = new DelayedRemovalArray<Hazard>();
        projectiles = new DelayedRemovalArray<Ammo>();
        impacts = new DelayedRemovalArray<Impact>();
        powerups = new DelayedRemovalArray<Powerup>();
        transports = new DelayedRemovalArray<Transport>();
        loadEx = false;
        musicEnabled = false;
        hintsEnabled = true;
        removedHazards = "-1";
        score = 0;
        time = 0;
        paused = false;
    }

    protected void update(float delta) {
        if (continuing() && !paused()) {
            updateEntities(delta);
        }
    }

    protected void render(SpriteBatch batch, Viewport viewport) {

        backdrop.render(batch, viewport, new Vector2(chaseCam.camera.position.x, chaseCam.camera.position.y), Constants.BACKGROUND_CENTER, 1);

        for (Ground ground : grounds) {
            if (!ground.isDense() || ground instanceof Tripknob || ground instanceof Tripspring || ground instanceof Spring) {
                ground.render(batch, viewport);
            }
        }

        for (Transport transport : transports) {
            transport.render(batch, viewport);
        }

        for (Powerup powerup : powerups) {
            powerup.render(batch, viewport);
        }

        for (Ground ground : grounds) {
            if (ground instanceof Vines) {
                ground.render(batch, viewport);
            }
        }

        for (Hazard hazard : hazards) {
            if (!(hazard instanceof Ammo)) {
                hazard.render(batch, viewport);
            }
        }

        for (Ground ground : grounds) {
            if (ground.isDense() && !(ground instanceof Tripknob || ground instanceof Tripspring || ground instanceof Spring)) {
                ground.render(batch, viewport);
            }
        }

        avatar.render(batch, viewport);
        Blade.getInstance().render(batch, viewport);

        for (Ground ground : grounds) {
            if (ground instanceof Pliable && ((Pliable) ground).isBeingCarried()) {
                ground.render(batch, viewport);
            }
        }

        for (Hazard hazard : hazards) {
            if (hazard instanceof Ammo) {
                hazard.render(batch, viewport);
            }
        }

        for (Impact impact : impacts) {
            impact.render(batch, viewport);
        }
    }

    private void applyCollision(Impermeable impermeable) {
        impermeable.touchAllGrounds(grounds);
        impermeable.touchAllHazards(hazards);
        if (impermeable instanceof Avatar) {
            ((Avatar) impermeable).touchAllPowerups(powerups);
        }
    }

    // asset handling
    private void updateEntities(float delta) {
        if (chaseCam.getState() == Enums.ChaseCamState.CONVERT) {
            grounds.begin();
            for (int i = 0; i < grounds.size; i++) {
                Ground ground = grounds.get(i);
                if (ground instanceof Nonstatic) {
                    for (Rectangle convertBounds : chaseCam.getConvertBounds()) {
                        if (convertBounds.overlaps(new Rectangle(ground.getPosition().x, ground.getPosition().y, ground.getWidth(), ground.getHeight()))) {
                            updateGround(delta, ground);
                        }
                    }
                }
            }
            grounds.end();
        } else if (boss.isTalking()) {
            if (chaseCam.getState() != Enums.ChaseCamState.BOSS) {
                chaseCam.setState(Enums.ChaseCamState.BOSS);
            } else if (avatar.getPosition().x < boss.getRoomBounds().x + boss.getRoomBounds().width / 3) {
                music.stop();
                avatar.setVelocity(new Vector2(40, 0));
                avatar.setPosition(avatar.getPosition().mulAdd(avatar.getVelocity(), delta));
                avatar.stride();
            } else {
                if (avatar.getAction() != Enums.Action.STANDING) {
                    avatar.setAction(Enums.Action.STANDING);
                } else if (InputControls.getInstance().shootButtonJustPressed) {
                    boss.setBattleState(true);
                    if (musicEnabled) {
                        music = AssetManager.getInstance().getMusicAssets().boss;
                        music.setLooping(true);
                        music.play();
                    }
                }
            }
        } else {
            time = timer.getNanos();
            if (avatar.getDispatchStatus()) {
                if (avatar.getLookStartTime() != 0) {
                    if (avatar.getDirectionY() == Direction.UP) {
                        spawnAmmo(new Vector2(avatar.getPosition().x + Helpers.speedToVelocity(Constants.AVATAR_Y_CANNON_OFFSET.x, avatar.getDirectionX(), Enums.Orientation.X), avatar.getPosition().y + Constants.AVATAR_Y_CANNON_OFFSET.y), avatar.getDirectionY(), Enums.Orientation.Y, avatar.getShotIntensity(), avatar.getWeapon(), avatar);
                    } else {
                        spawnAmmo(new Vector2(avatar.getPosition().x + Helpers.speedToVelocity(Constants.AVATAR_Y_CANNON_OFFSET.x - 3, avatar.getDirectionX(), Enums.Orientation.X), avatar.getPosition().y - Constants.AVATAR_Y_CANNON_OFFSET.y - 8), avatar.getDirectionY(), Enums.Orientation.Y, avatar.getShotIntensity(), avatar.getWeapon(), avatar);
                    }
                } else {
                    spawnAmmo(new Vector2(avatar.getPosition().x + Helpers.speedToVelocity(Constants.AVATAR_X_CANNON_OFFSET.x, avatar.getDirectionX(), Enums.Orientation.X), avatar.getPosition().y + Constants.AVATAR_X_CANNON_OFFSET.y), avatar.getDirectionX(), Enums.Orientation.X, avatar.getShotIntensity(), avatar.getWeapon(), avatar);
                }
                avatar.resetChargeIntensity();
            }

            if (boss.getDispatchStatus()) {
                if (boss.getLookStartTime() != 0) {
                    if (boss.getDirectionY() == Direction.UP) {
                        spawnAmmo(new Vector2(boss.getPosition().x + Helpers.speedToVelocity(Constants.AVATAR_Y_CANNON_OFFSET.x, boss.getDirectionX(), Enums.Orientation.X), boss.getPosition().y + Constants.AVATAR_Y_CANNON_OFFSET.y), boss.getDirectionY(), Enums.Orientation.Y, boss.getShotIntensity(), boss.getWeapon(), boss);
                    } else {
                        spawnAmmo(new Vector2(boss.getPosition().x + Helpers.speedToVelocity(Constants.AVATAR_Y_CANNON_OFFSET.x, boss.getDirectionX(), Enums.Orientation.X), boss.getPosition().y - Constants.AVATAR_Y_CANNON_OFFSET.y - 8), boss.getDirectionY(), Enums.Orientation.Y, boss.getShotIntensity(), boss.getWeapon(), boss);
                    }
                } else {
                    spawnAmmo(new Vector2(boss.getPosition().x + Helpers.speedToVelocity(Constants.AVATAR_X_CANNON_OFFSET.x, boss.getDirectionX(), Enums.Orientation.X), boss.getPosition().y + Constants.AVATAR_X_CANNON_OFFSET.y), boss.getDirectionX(), Enums.Orientation.X, boss.getShotIntensity(), boss.getWeapon(), boss);
                }
                boss.resetChargeIntensity();
            }

            if (avatar.getTouchedHazard() != null && avatar.getAction() == Enums.Action.RECOILING) {
                Vector2 intersectionPoint = new Vector2();
                Hazardous touchedHazard = avatar.getTouchedHazard();
                intersectionPoint.x = Math.max(avatar.getLeft(), touchedHazard.getLeft());
                intersectionPoint.y = Math.max(avatar.getBottom(), touchedHazard.getBottom());
                spawnImpact(intersectionPoint, touchedHazard.getType());
            }

            // Update Transports
            transports.begin();
            for (int i = 0; i < transports.size; i++) {
                if (!updateTransport(delta, transports.get(i), i)) {
                    transports.removeIndex(i);
                }
            }
            transports.end();

            // Update Hazards
            hazards.begin();
            for (int i = 0; i < hazards.size; i++) {
                if (!updateHazard(delta, hazards.get(i))) {
                    spawnPowerup(hazards.get(i));
                    hazards.removeIndex(i);
                    removedHazards += (";" + i); // ';' delimeter prevents conflict with higher level parse (for str containing all level removal lists)
                }
            }
            hazards.end();

            // Update Grounds
            grounds.begin();
            for (int i = 0; i < grounds.size; i++) {
                if (!updateGround(delta, grounds.get(i))) {
                    grounds.removeIndex(i);
                }
            }
            grounds.end();

            // Update Impacts
            impacts.begin();
            for (int i = 0; i < impacts.size; i++) {
                if (impacts.get(i).isFinished()) {
                    impacts.removeIndex(i);
                }
            }
            impacts.end();

            // Update Powerups
            powerups.begin();
            for (int i = 0; i < powerups.size; i++) {
                if (Helpers.overlapsPhysicalObject(avatar, powerups.get(i))) {
                    powerups.removeIndex(i);
                }
            }
            powerups.end();

            avatar.updatePosition(delta);
            applyCollision(avatar);
            avatar.update(delta);
            Blade.getInstance().update(delta);
        }
    }

    public boolean updateGround(float delta, Ground ground) {
        if (ground instanceof Weaponized && ((Weaponized) ground).getDispatchStatus()) {
            Weaponized weapon = (Weaponized) ground;
            Enums.Orientation orientation = weapon.getOrientation();
            Vector2 offset = new Vector2();
            if (weapon instanceof Cannoroll) {
                offset.set(weapon.getWidth(), weapon.getHeight());
            } else {
                offset.set(weapon.getWidth() / 2, weapon.getHeight() / 2);
            }
            if (orientation == Enums.Orientation.X) {
                Vector2 ammoPositionLeft = new Vector2(weapon.getPosition().x - offset.x, weapon.getPosition().y);
                Vector2 ammoPositionRight = new Vector2(weapon.getPosition().x + offset.x, weapon.getPosition().y);
                if (Avatar.getInstance().getPosition().x < (ammoPositionLeft.x - offset.x)) {
                    LevelUpdater.getInstance().spawnAmmo(ammoPositionLeft, Enums.Direction.LEFT, orientation, weapon.getIntensity(), LevelUpdater.getInstance().getType(), ground);
                } else if (Avatar.getInstance().getPosition().x > (ammoPositionRight.x + (weapon.getWidth() / 2))) {
                    LevelUpdater.getInstance().spawnAmmo(ammoPositionRight, Enums.Direction.RIGHT, orientation, weapon.getIntensity(), LevelUpdater.getInstance().getType(), ground);
                }
            } else if (orientation == Enums.Orientation.Y) {
                Vector2 ammoPositionTop = new Vector2(weapon.getPosition().x, weapon.getPosition().y + offset.y);
                Vector2 ammoPositionBottom = new Vector2(weapon.getPosition().x, weapon.getPosition().y - offset.y);
                if (weapon instanceof Cannon) {
                    if (Avatar.getInstance().getPosition().y < (ammoPositionBottom.y - offset.y)) {
                        LevelUpdater.getInstance().spawnAmmo(ammoPositionBottom, Enums.Direction.DOWN, orientation, weapon.getIntensity(), LevelUpdater.getInstance().getType(), ground);
                    } else if (Avatar.getInstance().getPosition().y > (ammoPositionTop.y + (weapon.getHeight() / 2))) {
                        LevelUpdater.getInstance().spawnAmmo(ammoPositionTop, Enums.Direction.UP, orientation, weapon.getIntensity(), LevelUpdater.getInstance().getType(), ground);
                    }
                } else {
                    LevelUpdater.getInstance().spawnAmmo(ammoPositionTop, Enums.Direction.UP, orientation, weapon.getIntensity(), LevelUpdater.getInstance().getType(), ground);
                }
            }
        }
        boolean active = true;
        if (ground instanceof Trippable) {
            Trippable trip = (Trippable) ground;
            if (trip instanceof Triptread) {
                if (Helpers.overlapsPhysicalObject(avatar, trip) && avatar.getAction() == Enums.Action.DASHING && !avatar.getDashStatus()) {
                    trip.setState(!trip.isActive());
                }
            }
            if (trip.tripped()) {
                if (hintsEnabled
                        && !trip.maxAdjustmentsReached()
                        && !trip.getBounds().equals(Rectangle.tmp) // where tmp has bounds of (0,0,0,0)
                        && !(trip.getBounds().overlaps(new Rectangle(chaseCam.camera.position.x - chaseCam.getViewport().getWorldWidth() / 4, chaseCam.camera.position.y - chaseCam.getViewport().getWorldHeight() / 4, chaseCam.getViewport().getWorldWidth() / 2, chaseCam.getViewport().getWorldHeight() / 2)))) { // halving dimensions heightens camera sensitivity

                    chaseCam.setState(Enums.ChaseCamState.CONVERT);
                    chaseCam.setConvertBounds(trip.getBounds());
                    trip.addCamAdjustment();
                }
                for (Ground g : grounds) {
                    if (g instanceof Convertible && (g != trip || g instanceof Triptread)) {
                        if (Helpers.betweenFourValues(g.getPosition(), trip.getBounds().x, trip.getBounds().x + trip.getBounds().width, trip.getBounds().y, trip.getBounds().y + trip.getBounds().height)) {
                            ((Convertible) g).convert();
                        }
                    }
                }
                for (Hazard h : hazards) {
                    if (h instanceof Convertible && (h != trip)) {
                        if (Helpers.betweenFourValues(h.getPosition(), trip.getBounds().x, trip.getBounds().x + trip.getBounds().width, trip.getBounds().y, trip.getBounds().y + trip.getBounds().height)) {
                            ((Convertible) h).convert();
                        }
                    }
                }
            }
        }
        if (ground instanceof Nonstatic) {
            ((Nonstatic) ground).update(delta);
        }
        if (ground instanceof Compressible) {
            if (!(ground instanceof Pliable && ((Pliable) ground).isBeingCarried() && ((Pliable) ground).getCarrier() == avatar)) {
                Compressible compressible = (Compressible) ground;
                if (Helpers.overlapsPhysicalObject(avatar, ground)) {
                    if (!compressible.getState()) {
                        compressible.resetStartTime();
                    }
                    compressible.setState(true);
                } else if (compressible.getState() && !(compressible instanceof Pliable && ((Pliable) compressible).isAtopMovingGround() && Helpers.betweenTwoValues(avatar.getBottom(), ground.getTop(), ground.getTop() + 2))) {
                    if (!compressible.isBeneatheGround()) {
                        compressible.resetStartTime();
                        compressible.setState(false);
                    }
                }
            }
        }
        if (ground instanceof Pliable) {
            Pliable pliable = (Pliable) ground;
            if (!(pliable).isBeingCarried()) {
                if (Helpers.overlapsPhysicalObject(avatar, ground)) {
                    if (avatar.getMoveStatus()) {
                        if (inputControls.shootButtonPressed) {
                            if (ground instanceof Compressible) {
                                avatar.setMoveStatus(false);
                                if (ground instanceof Spring && !((Spring) ground).isBeneatheGround()) {
                                    ((Compressible) ground).resetStartTime();
                                    ((Compressible) ground).setState(false);
                                }
                            }
                            if (avatar.getCarriedGround() == null) { // prevents from carrying simultaneously and in the process setting to overlap two grounds
                                avatar.setPosition(new Vector2(ground.getPosition().x, ground.getBottom() + Constants.AVATAR_EYE_HEIGHT));
                                pliable.setCarrier(avatar);
                                Gdx.app.log(TAG, pliable.isBeingCarried() + "" + avatar.getMoveStatus());
                            }
                            avatar.setCarriedGround(pliable);
                        }
                    }
                }
            } else if (pliable.getCarrier() == avatar) {
                if (avatar.getAction() != Enums.Action.STANDING) {
                    float adjustment = .75f;
                    if (avatar.getGroundState() != Enums.GroundState.PLANTED) {
                        adjustment *= 2;
                    } else {
                        avatar.setVelocity(new Vector2(avatar.getVelocity().x / (1 + (pliable).weightFactor()), avatar.getVelocity().y));
                    }
                    avatar.setTurbo(Math.max(avatar.getTurbo() - (pliable).weightFactor() - adjustment, 0));
                    if (avatar.getTurbo() == 0) {
                        pliable.setCarrier(null);
                        avatar.setCarriedGround(null);
                        avatar.setMoveStatus(false);
                    }
                }
                if (!InputControls.getInstance().shootButtonPressed
                || avatar.getAction() == Enums.Action.RECOILING) { // move status set to false when recoiling
                    pliable.setCarrier(null);
                    avatar.setCarriedGround(null);
                    if (pliable instanceof Tossable && pliable.getVelocity().x != 0 && (InputControls.getInstance().leftButtonPressed || InputControls.getInstance().rightButtonPressed)) {
                        ((Tossable) pliable).toss(Helpers.speedToVelocity(ground.getWidth() * 13, avatar.getDirectionX(), Enums.Orientation.X));
                    }
                }
            }
        }
        if (ground instanceof Destructible) {
            if (((Destructible) ground).getHealth() < 1) {
                if (ground instanceof Box) {
                    grounds.add(new Brick(ground.getPosition().x, ground.getPosition().y, 5, 5, ((Destructible) ground).getType(), false));
                    assetManager.getSoundAssets().breakGround.play();
                }
                active = false;
            }
        }
        if (ground instanceof Chargeable) {
            Chargeable chargeable = (Chargeable) ground;
            if (avatar.getChargeTimeSeconds() != Helpers.secondsSince(0) && avatar.getDirectionX() == Direction.RIGHT
                    && (int) avatar.getRight() + 1 == chargeable.getLeft() + 1 && avatar.getPosition().y - 4 == chargeable.getTop()) {
                if (!chargeable.isActive() && chargeable instanceof Chamber) {
                    chargeable.setState(true);
                } else if (avatar.getChargeTimeSeconds() > 1) {
                    chargeable.setChargeTime(avatar.getChargeTimeSeconds());
                }
                if (ground instanceof Chamber) {
                    Chamber chamber = (Chamber) ground;
                    if (chamber.isActive() && chamber.isCharged() && avatar.getShotIntensity() == Enums.ShotIntensity.NORMAL) {
                        String savedUpgrades = SaveData.getUpgrades();
                        Enums.Upgrade upgrade = chamber.getUpgrade();
                        if (!savedUpgrades.contains(upgrade.name())) {
                            assetManager.getSoundAssets().upgrade.play();
                            avatar.addUpgrade(upgrade);
                            SaveData.setUpgrades(upgrade.name() + ", " + savedUpgrades);
                        }
                    }
                } else {
                    if (avatar.getShotIntensity() == Enums.ShotIntensity.BLAST) {
                        chargeable.charge();
                    }
                }
            } else {
                if (chargeable instanceof Chamber) {
                    chargeable.setState(false);
                } else {
                    chargeable.uncharge();
                }
                chargeable.setChargeTime(0);
            }
        }
        if (ground instanceof Strikeable) {
            projectiles.begin();
            for (int j = 0; j < projectiles.size; j++) {
                Ammo ammo = projectiles.get(j);
                if (Helpers.overlapsPhysicalObject(ammo, ground)) {
                    if (ammo.getSource() instanceof Avatar) {
                        assetManager.getSoundAssets().hitGround.play();
                    }
                    if (ammo.isActive() &&
                            (ground.isDense() // collides with all sides of dense ground
                                    || Helpers.overlapsBetweenTwoSides(ammo.getPosition().y, ammo.getHeight() / 2, ground.getTop() - 3, ground.getTop()))) { // collides only with top of non-dense ground
                        if (!ammo.getPosition().equals(Vector2.Zero)) {
                            this.spawnImpact(ammo.getPosition(), ammo.getType());
                        }
                        ammo.deactivate();
                    }
                    Strikeable strikeable = (Strikeable) ground;
                    if (strikeable instanceof Tripknob) {
                        Tripknob tripknob = (Tripknob) strikeable;
                        tripknob.resetStartTime();
                        tripknob.setState(!tripknob.isActive());
                    } else if (strikeable instanceof Cannoroll) {
                        Cannoroll cannoroll = (Cannoroll) strikeable;
                        cannoroll.convert();
                    } else if (strikeable instanceof Chargeable) {
                        Chargeable chargeable = (Chargeable) strikeable;
                        if (chargeable instanceof Chamber) {
                            chargeable.setState(false);
                        } else if (chargeable instanceof Tripchamber && ammo.getShotIntensity() == Enums.ShotIntensity.BLAST) {
                            if (chargeable.isCharged()) {
                                chargeable.setState(!chargeable.isActive());
                                chargeable.uncharge();
                            }
                        }
                    } else if (strikeable instanceof Destructible) {
                        Gdx.app.log(TAG, ((Destructible) strikeable).getHealth() + "");
                        Helpers.applyDamage((Destructible) ground, ammo);
                    } else if (strikeable instanceof Gate && ammo.getDirection() == Direction.RIGHT) { // prevents from re-unlocking after crossing gate boundary (always left to right)
                        ((Gate) strikeable).deactivate();
                    }
                }
            }
            projectiles.end();
        }
        if (active && ground instanceof Impermeable) {
            applyCollision((Impermeable) ground);
        }
        return active;
    }

    public boolean updateHazard(float delta, Hazard hazard) {
        boolean active = true;
        if (hazard instanceof Boss) {
            ((Boss) hazard).updatePosition(delta);
            applyCollision((Impermeable) hazard);
            if (boss.getRoomBounds().overlaps(avatar.getBounds())) {
                if (!boss.isBattling()) {
                    boss.setTalkState(true);
                }
            }
        }
        if (hazard instanceof Destructible) {
            Destructible destructible = (Destructible) hazard;
            projectiles.begin();
            for (int j = 0; j < projectiles.size; j++) {
                Ammo ammo = projectiles.get(j);
                if (!ammo.equals(hazard) && ammo.isActive() && Helpers.overlapsPhysicalObject(ammo, destructible)) {
                    if (!(destructible instanceof Zoomba)
                    || !((ammo.getOrientation() == Enums.Orientation.X && Helpers.betweenTwoValues(ammo.getPosition().y, destructible.getBottom() + 5, destructible.getTop() - 5))
                    || (ammo.getOrientation() == Enums.Orientation.Y && Helpers.betweenTwoValues(ammo.getPosition().x, destructible.getLeft() + 5, destructible.getRight() - 5)))) {
                        if (!(hazard instanceof Armored)) {
                            Helpers.applyDamage(destructible, ammo);
                            this.spawnImpact(ammo.getPosition(), ammo.getType());
                            ammo.deactivate();
                        } else {
                            AssetManager.getInstance().getSoundAssets().hitGround.play();
                            ammo.deactivate();
                        }
                        score += ammo.getHitScore();
                    } else {
                        ((Zoomba) destructible).convert();
                        if (avatar.getTouchedGround() != null && avatar.getTouchedGround().equals(destructible)) {
                            avatar.setPosition(new Vector2(destructible.getPosition().x, destructible.getTop() + Constants.AVATAR_EYE_HEIGHT));
                            Gdx.app.log(TAG, avatar.getPosition() + " " + destructible.getPosition());
                        }
                    }
                    if (destructible instanceof Zoomba) {
                        this.spawnImpact(ammo.getPosition(), ammo.getType());
                        ammo.deactivate();
                    }
                }
            }
            projectiles.end();

            if (Helpers.overlapsPhysicalObject(Blade.getInstance(), destructible)) {
                if (avatar.getBladeState() == Enums.BladeState.FLIP
                        || (avatar.getBladeState() == Enums.BladeState.RUSH && Helpers.betweenTwoValues(destructible.getPosition().y, avatar.getBottom(), avatar.getTop()))
                        || (avatar.getBladeState() == Enums.BladeState.CUT) && (Helpers.speedToVelocity(destructible.getPosition().x, avatar.getDirectionX(), Enums.Orientation.X) - Helpers.speedToVelocity(avatar.getPosition().x, avatar.getDirectionX(), Enums.Orientation.X) > 0)) {
                    if (!(hazard instanceof Armored)) {
                        Helpers.applyDamage(destructible, Blade.getInstance());
                    } else {
                        if (((Armored) hazard).isVulnerable()) {
                            if (Helpers.directionToOrientation(((Armored) hazard).getVulnerability()) == Enums.Orientation.Y
                                    && avatar.getBladeState() == Enums.BladeState.CUT
                                    && Helpers.getOppositeDirection(((Armored) hazard).getVulnerability()) == avatar.getDirectionY()) {
                                Helpers.applyDamage(destructible, Blade.getInstance());
                                ((Armored) hazard).resetStartTime();
                            } else if (Helpers.directionToOrientation(((Armored) hazard).getVulnerability()) == Enums.Orientation.X
                                    && avatar.getBladeState() == Enums.BladeState.RUSH
                                    && ((Armored) hazard).getVulnerability() == Helpers.inputToDirection()) {
                                Helpers.applyDamage(destructible, Blade.getInstance());
                                ((Armored) hazard).resetStartTime();
                            }
                        } else {
                            ((Armored) hazard).strikeArmor();
                        }
                    }
                }
            }

            if (destructible.getHealth() < 1) {
                spawnImpact(destructible.getPosition(), destructible.getType());
                active = false;
                score += (destructible.getKillScore() * Constants.DIFFICULTY_MULTIPLIER[SaveData.getDifficulty()]);
            }
            if (destructible instanceof Orben && ((Orben) destructible).getDispatchStatus()) {
                Vector2 ammoPositionLeft = new Vector2(destructible.getPosition().x - (destructible.getWidth() * 1.1f), destructible.getPosition().y);
                Vector2 ammoPositionRight = new Vector2(destructible.getPosition().x + (destructible.getWidth() * 1.1f), destructible.getPosition().y);
                Vector2 ammoPositionTop = new Vector2(destructible.getPosition().x, destructible.getPosition().y + (destructible.getHeight() * 1.1f));
                Vector2 ammoPositionBottom = new Vector2(destructible.getPosition().x, destructible.getPosition().y - (destructible.getHeight() * 1.1f));

                LevelUpdater.getInstance().spawnAmmo(ammoPositionLeft, Enums.Direction.LEFT, Enums.Orientation.X, Enums.ShotIntensity.BLAST, destructible.getType(), hazard);
                LevelUpdater.getInstance().spawnAmmo(ammoPositionRight, Enums.Direction.RIGHT, Enums.Orientation.X, Enums.ShotIntensity.BLAST, destructible.getType(), hazard);
                LevelUpdater.getInstance().spawnAmmo(ammoPositionBottom, Enums.Direction.DOWN, Enums.Orientation.Y, Enums.ShotIntensity.BLAST, destructible.getType(), hazard);
                LevelUpdater.getInstance().spawnAmmo(ammoPositionTop, Enums.Direction.UP, Enums.Orientation.Y, Enums.ShotIntensity.BLAST, destructible.getType(), hazard);
            }
        } else if (hazard instanceof Ammo) {
            Ammo ammo = (Ammo) hazard;
            ammo.update(delta);
            if (!ammo.isActive()) {
                active = false;
                projectiles.removeValue(ammo, false);
            }
        }
        if (hazard instanceof Nonstatic) {
            ((Nonstatic) hazard).update(delta);
        }
        if (active && hazard instanceof Impermeable) {
            applyCollision((Impermeable) hazard);
        }
        return active;
    }

    public boolean updateTransport(float delta, Transport transport, int portalIndex) {
        boolean active = true;
        if (avatar.getPosition().dst(transport.getPosition()) < transport.getWidth() / 2 && inputControls.upButtonPressed && inputControls.jumpButtonJustPressed) {
            if (transport instanceof Portal) {
                for (int j = 0; j <= portalIndex; j++) {
                    if (!(transports.get(j) instanceof Portal)) {
                        portalIndex++;
                    }
                }
                assetManager.getSoundAssets().life.play();
                int level = Arrays.asList(Enums.Theme.values()).indexOf(this.theme);
                List<String> allRestores = Arrays.asList(SaveData.getLevelRestores().split(", "));
                List<String> allTimes = Arrays.asList(SaveData.getLevelTimes().split(", "));
                List<String> allScores = Arrays.asList(SaveData.getLevelScores().split(", "));
                List<String> allRemovals = Arrays.asList(SaveData.getLevelRemovals().split(", "));
                int restores = Integer.parseInt(allRestores.get(level));
                if (restores == 0) {
                    allRestores.set(level, Integer.toString(portalIndex + 1));
                } else if (restores != (portalIndex + 1)) {
                    allRestores.set(level, Integer.toString(3));
                }
                allTimes.set(level, Long.toString(time));
                allScores.set(level, Integer.toString(score));
                allRemovals.set(level, removedHazards);
                SaveData.setLevelRestores(allRestores.toString().replace("[", "").replace("]", ""));
                SaveData.setLevelTimes(allTimes.toString().replace("[", "").replace("]", ""));
                SaveData.setLevelScores(allScores.toString().replace("[", "").replace("]", ""));
                SaveData.setLevelRemovals(allRemovals.toString().replace("[", "").replace("]", ""));

                SaveData.setTotalTime(Helpers.numStrToSum(allTimes));
                SaveData.setTotalScore((int) Helpers.numStrToSum(allScores));

                savedTime = time;
                savedScore = score;
            } else if (transport instanceof Teleport) {
                assetManager.getSoundAssets().warp.play();
                avatar.getPosition().set(transport.getDestination());
            }
        }
        return active;
    }

    protected void restoreRemovals(String removals) {
        removedHazards = removals;
        List<String> levelRemovalStrings = Arrays.asList(removedHazards.split(";"));
        List<Integer> levelRemovals = new ArrayList<Integer>();
        for (String removalStr : levelRemovalStrings) {
            levelRemovals.add(Integer.parseInt(removalStr));
        }
        for (Integer removal : levelRemovals) {
            if (removal != -1) {
                hazards.removeIndex(removal);
            }
        }
    }

    protected void clearEntities() {
        getEntities().clear();
        getGrounds().clear();
        getHazards().clear();
        getPowerups().clear();
        entities.clear();
        grounds.clear();
        hazards.clear();
        powerups.clear();
        transports.clear();
        impacts.clear();
        projectiles.clear();
    }

    protected void dispose() {
        clearEntities();
        entities = null;
        grounds = null;
        hazards = null;
        powerups = null;
        transports = null;
        impacts = null;
        projectiles = null;
    }


    // level state handling

    protected void begin() {
        grounds.sort(new Comparator<Ground>() {
            @Override
            public int compare(Ground o1, Ground o2) {
                if (o1.getBottom() > o2.getBottom()) {
                    return 1;
                } else if (o1.getBottom() < o2.getBottom()) {
                    return -1;
                }
                return 0;
            }
        });
        entities.addAll(grounds);
        entities.addAll(hazards);
        entities.addAll(powerups);
        entities.addAll(transports);
        entities.addAll(impacts);
        chaseCam.setState(Enums.ChaseCamState.FOLLOWING);
        backdrop = new Backdrop(assetManager.getBackgroundAssets().getBackground(theme));
        music = assetManager.getMusicAssets().getThemeMusic(theme);
        music.setLooping(true);
        if (musicEnabled) {
            music.play();
        }
        levelWeapon = Enums.Material.NATIVE;
        for (Enums.Material weapon : Arrays.asList(Enums.Material.values())) {
            if (weapon.theme().equals(theme)) {
                levelWeapon = weapon;
            }
        }

        avatar.setLives(3);
        avatar.respawn();

        timer.reset().start(time);
        savedTime = time;
        savedScore = score;
    }

    protected void end() {
        music.stop();
        timer.suspend();
        if (completed()) {
            SaveData.setTotalScore(SaveData.getTotalScore() + score);
            SaveData.setTotalTime(SaveData.getTotalTime() + timer.getSeconds());
            String savedWeapons = SaveData.getWeapons();
            if (!savedWeapons.contains(levelWeapon.name())) {
                avatar.addWeapon(levelWeapon);
                SaveData.setWeapons(levelWeapon.name() + ", " + savedWeapons);
            }
        }
        clearEntities();
    }

    protected void pause() {
        music.pause();
        timer.suspend();
        paused = true;
    }

    protected void unpause() {
        if (musicEnabled) {
            music.play();
        }
        paused = false;
        timer.resume();
    }

    protected void reset() {
        int level = Arrays.asList(Enums.Theme.values()).indexOf(this.theme);
        List<String> allRestores = Arrays.asList(SaveData.getLevelRestores().split(", "));
        List<String> allTimes = Arrays.asList(SaveData.getLevelTimes().split(", "));
        List<String> allScores = Arrays.asList(SaveData.getLevelScores().split(", "));
        List<String> allRemovals = Arrays.asList(SaveData.getLevelRemovals().split(", "));
        allTimes.set(level, "0");
        allScores.set(level, "0");
        allRestores.set(level, "0");
        allRemovals.set(level, "-1");
        SaveData.setLevelRestores(allRestores.toString().replace("[", "").replace("]", ""));
        SaveData.setLevelTimes(allTimes.toString().replace("[", "").replace("]", ""));
        SaveData.setLevelScores(allScores.toString().replace("[", "").replace("]", ""));
        SaveData.setLevelRemovals(allRemovals.toString().replace("[", "").replace("]", ""));
    }

    protected boolean restarted() {
        if (avatar.getFallLimit() != -10000) {
            if (avatar.getPosition().y < avatar.getFallLimit() || avatar.getHealth() < 1) {
                avatar.setHealth(0);
                avatar.setLives(avatar.getLives() - 1);
                return true;
            }
        }
        return false;
    }

    protected boolean failed() {
        if (restarted()) {
            if (avatar.getLives() < 0) {
                return true;
            }
            boss.setPosition(new Vector2(boss.getRoomBounds().x + boss.getRoomBounds().width / 2, boss.getRoomBounds().y + boss.getRoomBounds().height / 2));
            if (chaseCam.getState() == Enums.ChaseCamState.BOSS) {
                chaseCam.setState(Enums.ChaseCamState.FOLLOWING);
            }
            if (musicEnabled) {
                music.stop();
                music = AssetManager.getInstance().getMusicAssets().getThemeMusic(theme);
                music.play();
            }
            avatar.respawn();
            boss.setBattleState(false);
        }
        return false;
    }

    protected boolean completed() { return chaseCam.getState() == Enums.ChaseCamState.BOSS && boss.getHealth() < 1; }

    protected boolean continuing() { return !(completed() || failed()); }

    protected boolean paused() {
        return paused;
    }

    private void spawnAmmo(Vector2 position, Direction direction, Enums.Orientation orientation, Enums.ShotIntensity shotIntensity, Enums.Material weapon, Entity source) {
        Ammo ammo = new Ammo(position, direction, orientation, shotIntensity, weapon, source);
        hazards.add(ammo);
        projectiles.add(ammo);
    }

    private void spawnImpact(Vector2 position, Enums.Material type) {
        impacts.add(new Impact(position, type));
    }

    private void spawnPowerup(Hazard hazard) {
        if (!(hazard instanceof Ammo)) {
            switch (hazard.getType()) {
                case ORE:
                    powerups.add(new Powerup(hazard.getPosition().add(-5, 5), Enums.PowerupType.AMMO));
                    powerups.add(new Powerup(hazard.getPosition().add(5, 5), Enums.PowerupType.AMMO));
                    powerups.add(new Powerup(hazard.getPosition().add(5, -5), Enums.PowerupType.AMMO));
                    powerups.add(new Powerup(hazard.getPosition().add(-5, -5), Enums.PowerupType.AMMO));
                    powerups.add(new Powerup(hazard.getPosition(), Enums.PowerupType.AMMO));
                    break;
                case GAS:
                    powerups.add(new Powerup(hazard.getPosition(), Enums.PowerupType.AMMO));
                    break;
                case LIQUID:
                    powerups.add(new Powerup(hazard.getPosition().add(0, -5), Enums.PowerupType.AMMO));
                    powerups.add(new Powerup(hazard.getPosition().add(0, -5), Enums.PowerupType.AMMO));
                    break;
                case SOLID:
                    powerups.add(new Powerup(hazard.getPosition().add(-5, 5), Enums.PowerupType.AMMO));
                    powerups.add(new Powerup(hazard.getPosition().add(-5, 5), Enums.PowerupType.AMMO));
                    powerups.add(new Powerup(hazard.getPosition(), Enums.PowerupType.AMMO));
                    break;
            }
        }
    }

    // Public getters
    protected final Array<Entity> getEntities() { return entities; }
    protected final Array<Ground> getGrounds() { return grounds; }
    protected final Array<Hazard> getHazards() { return hazards; }
    protected final Array<Powerup> getPowerups() { return powerups; }
    protected final long getTime() { return time; }
    protected final int getScore() { return score; }
    protected final Boss getBoss() { return boss; }
    protected final Avatar getAvatar() { return avatar; }
    protected final Enums.Material getType() { return levelWeapon; }
    protected final Viewport getViewport() { return levelScreen.getViewport(); }

    // Protected getters
    protected final long getUnsavedTime() { return timer.getSeconds(); }
    protected final int getUnsavedScore() { return score - savedScore; }
    protected final void setBoss(Boss boss) { this.boss = boss; }
    protected final DelayedRemovalArray<Transport> getTransports() { return transports; }
    protected Enums.Theme getTheme() { return theme; }
    protected final boolean hasLoadEx() { return loadEx; }

    // Setters
    protected final void addEntity(Entity entity) { entities.add(entity); }
    protected final void addGround(Ground ground) { grounds.add(ground); }
    protected final void addHazard(Hazard hazard) { hazards.add(hazard); }
    protected final void addPowerup(Powerup powerup) { powerups.add(powerup); }
    protected void setTime(long time) { this.time = time; }
    protected void setScore(int score) {this.score = score; }
    protected void setTheme(Enums.Theme selectedLevel) { theme = selectedLevel; }
    protected void toggleMusic() { musicEnabled = !musicEnabled; }
    protected void toggleHints() { hintsEnabled = !hintsEnabled; }
    protected final void setLoadEx(boolean state) { loadEx = state; }
}
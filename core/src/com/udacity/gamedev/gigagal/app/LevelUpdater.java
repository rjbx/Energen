package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entity.*;
import com.udacity.gamedev.gigagal.overlay.Backdrop;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.ChaseCam;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.Direction;
import com.udacity.gamedev.gigagal.util.InputControls;
import com.udacity.gamedev.gigagal.util.Timer;
import com.udacity.gamedev.gigagal.util.Helpers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// mutable
public class LevelUpdater {

    // fields
    public static final String TAG = LevelUpdater.class.getName();
    private static final LevelUpdater INSTANCE = new LevelUpdater();
    private Assets assets;
    private InputControls inputControls;
    private Viewport viewport;
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
    private GigaGal gigaGal;
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
        levelScreen.create();

        timer = Timer.getInstance();
        timer.create();

        gigaGal = GigaGal.getInstance();
        chaseCam = ChaseCam.getInstance();
        assets = Assets.getInstance();
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

        gigaGal.render(batch, viewport);
        Blade.getInstance().render(batch, viewport);

        for (Hazard hazard : hazards) {
            if (hazard instanceof Ammo) {
                hazard.render(batch, viewport);
            }
        }

        for (Impact impact : impacts) {
            impact.render(batch, viewport);
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
            if (ChaseCam.getInstance().getState() != Enums.ChaseCamState.BOSS) {
                ChaseCam.getInstance().setState(Enums.ChaseCamState.BOSS);
            } else if (gigaGal.getPosition().x < boss.getRoomBounds().x + boss.getRoomBounds().width / 3) {
                music.stop();
                gigaGal.setVelocity(new Vector2(40, 0));
                gigaGal.setPosition(gigaGal.getPosition().mulAdd(gigaGal.getVelocity(), delta));
                gigaGal.stride();
            } else {
                if (gigaGal.getAction() != Enums.Action.STANDING) {
                    gigaGal.setAction(Enums.Action.STANDING);
                } else if (InputControls.getInstance().shootButtonJustPressed) {
                    boss.setBattleState(true);
                    if (musicEnabled) {
                        music = Assets.getInstance().getMusicAssets().boss;
                        music.setLooping(true);
                        music.play();
                    }
                }
            }
        } else {
            time = timer.getNanos();
            gigaGal.update(delta);
            Blade.getInstance().update(delta);
            if (gigaGal.getDispatchStatus()) {
                if (gigaGal.getLookStartTime() != 0) {
                    if (gigaGal.getDirectionY() == Direction.UP) {
                        spawnAmmo(new Vector2(gigaGal.getPosition().x + Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_Y_CANNON_OFFSET.x, gigaGal.getDirectionX(), Enums.Orientation.X), gigaGal.getPosition().y + Constants.GIGAGAL_Y_CANNON_OFFSET.y), gigaGal.getDirectionY(), Enums.Orientation.Y, gigaGal.getShotIntensity(), gigaGal.getWeapon(), gigaGal);
                    } else {
                        spawnAmmo(new Vector2(gigaGal.getPosition().x + Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_Y_CANNON_OFFSET.x - 3, gigaGal.getDirectionX(), Enums.Orientation.X), gigaGal.getPosition().y - Constants.GIGAGAL_Y_CANNON_OFFSET.y - 8), gigaGal.getDirectionY(), Enums.Orientation.Y, gigaGal.getShotIntensity(), gigaGal.getWeapon(), gigaGal);
                    }
                } else {
                    spawnAmmo(new Vector2(gigaGal.getPosition().x + Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_X_CANNON_OFFSET.x, gigaGal.getDirectionX(), Enums.Orientation.X), gigaGal.getPosition().y + Constants.GIGAGAL_X_CANNON_OFFSET.y), gigaGal.getDirectionX(), Enums.Orientation.X, gigaGal.getShotIntensity(), gigaGal.getWeapon(), gigaGal);
                }
                gigaGal.resetChargeIntensity();
            }

            if (boss.getDispatchStatus()) {
                if (boss.getLookStartTime() != 0) {
                    if (boss.getDirectionY() == Direction.UP) {
                        spawnAmmo(new Vector2(boss.getPosition().x + Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_Y_CANNON_OFFSET.x, boss.getDirectionX(), Enums.Orientation.X), boss.getPosition().y + Constants.GIGAGAL_Y_CANNON_OFFSET.y), boss.getDirectionY(), Enums.Orientation.Y, boss.getShotIntensity(), boss.getWeapon(), boss);
                    } else {
                        spawnAmmo(new Vector2(boss.getPosition().x + Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_Y_CANNON_OFFSET.x - 3, boss.getDirectionX(), Enums.Orientation.X), boss.getPosition().y - Constants.GIGAGAL_Y_CANNON_OFFSET.y - 8), boss.getDirectionY(), Enums.Orientation.Y, boss.getShotIntensity(), boss.getWeapon(), boss);
                    }
                } else {
                    spawnAmmo(new Vector2(boss.getPosition().x + Helpers.absoluteToDirectionalValue(Constants.GIGAGAL_X_CANNON_OFFSET.x, boss.getDirectionX(), Enums.Orientation.X), boss.getPosition().y + Constants.GIGAGAL_X_CANNON_OFFSET.y), boss.getDirectionX(), Enums.Orientation.X, boss.getShotIntensity(), boss.getWeapon(), boss);
                }
                boss.resetChargeIntensity();
            }

            if (gigaGal.getTouchedHazard() != null && gigaGal.getAction() == Enums.Action.RECOILING) {
                Vector2 intersectionPoint = new Vector2();
                Hazardous touchedHazard = gigaGal.getTouchedHazard();
                intersectionPoint.x = Math.max(gigaGal.getLeft(), touchedHazard.getLeft());
                intersectionPoint.y = Math.max(gigaGal.getBottom(), touchedHazard.getBottom());
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

            // Update Grounds
            grounds.begin();
            for (int i = 0; i < grounds.size; i++) {
                if (!updateGround(delta, grounds.get(i))) {
                    grounds.removeIndex(i);
                }
            }
            grounds.end();

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
                if (Helpers.overlapsPhysicalObject(gigaGal, powerups.get(i))) {
                    powerups.removeIndex(i);
                }
            }
            powerups.end();
        }
    }

    public boolean updateGround(float delta, Ground ground) {
        if (ground instanceof Weaponized && ((Weaponized) ground).getDispatchStatus()) {
            Weaponized weapon = (Weaponized) ground;
            Enums.Orientation orientation = weapon.getOrientation();
            Vector2 offset = new Vector2();
            if (weapon instanceof Canirol) {
                offset.set(weapon.getWidth(), weapon.getHeight());
            } else {
                offset.set(weapon.getWidth() / 2, weapon.getHeight() / 2);
            }
            if (orientation == Enums.Orientation.X) {
                Vector2 ammoPositionLeft = new Vector2(weapon.getPosition().x - offset.x, weapon.getPosition().y);
                Vector2 ammoPositionRight = new Vector2(weapon.getPosition().x + offset.x, weapon.getPosition().y);
                if (GigaGal.getInstance().getPosition().x < (ammoPositionLeft.x - offset.x)) {
                    LevelUpdater.getInstance().spawnAmmo(ammoPositionLeft, Enums.Direction.LEFT, orientation, weapon.getIntensity(), LevelUpdater.getInstance().getType(), ground);
                } else if (GigaGal.getInstance().getPosition().x > (ammoPositionRight.x + (weapon.getWidth() / 2))) {
                    LevelUpdater.getInstance().spawnAmmo(ammoPositionRight, Enums.Direction.RIGHT, orientation, weapon.getIntensity(), LevelUpdater.getInstance().getType(), ground);
                }
            } else if (orientation == Enums.Orientation.Y) {
                Vector2 ammoPositionTop = new Vector2(weapon.getPosition().x, weapon.getPosition().y + offset.y);
                Vector2 ammoPositionBottom = new Vector2(weapon.getPosition().x, weapon.getPosition().y - offset.y);
                if (weapon instanceof Cannon) {
                    if (GigaGal.getInstance().getPosition().y < (ammoPositionBottom.y - offset.y)) {
                        LevelUpdater.getInstance().spawnAmmo(ammoPositionBottom, Enums.Direction.DOWN, orientation, weapon.getIntensity(), LevelUpdater.getInstance().getType(), ground);
                    } else if (GigaGal.getInstance().getPosition().y > (ammoPositionTop.y + (weapon.getHeight() / 2))) {
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
                if (Helpers.overlapsPhysicalObject(gigaGal, trip) && gigaGal.getAction() == Enums.Action.DASHING && !gigaGal.getDashStatus()) {
                    trip.setState(!trip.isActive());
                }
            }
            if (trip.tripped()) {
                if (hintsEnabled
                        && !trip.maxAdjustmentsReached()
                        && !trip.getBounds().equals(Rectangle.tmp) // where tmp has bounds of (0,0,0,0)
                        && !(trip.getBounds().overlaps(new Rectangle(chaseCam.camera.position.x - viewport.getWorldWidth() / 4, chaseCam.camera.position.y - viewport.getWorldHeight() / 4, viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2)))) { // halving dimensions heightens camera sensitivity

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
        if (ground instanceof Reboundable) {
            if (!(ground instanceof Pliable && ((Pliable) ground).isBeingCarried() && ((Pliable) ground).getCarrier() == gigaGal)) {
                Reboundable reboundable = (Reboundable) ground;
                if (Helpers.overlapsPhysicalObject(gigaGal, ground)) {
                    if (!reboundable.getState()) {
                        reboundable.resetStartTime();
                    }
                    reboundable.setState(true);
                } else if (reboundable.getState() && !(reboundable instanceof Pliable && ((Pliable) reboundable).isAtopMovingGround() && Helpers.betweenTwoValues(gigaGal.getBottom(), ground.getTop() - 2, ground.getTop() + 2))) {
                    if (ground instanceof Compressible && !((Compressible) ground).underneathGround()) {
                        reboundable.resetStartTime();
                        reboundable.setState(false);
                    }
                }
            }
        }
        if (ground instanceof Pliable) {
            if (!((Pliable) ground).isBeingCarried() && Helpers.overlapsPhysicalObject(gigaGal, ground)) {
                if ((gigaGal.getBottom() == ground.getBottom() && (InputControls.getInstance().shootButtonJustPressed) && gigaGal.getAction() == Enums.Action.STRIDING)
                || ((Helpers.betweenTwoValues(gigaGal.getBottom(), ground.getTop() - 2, ground.getTop() + 2) && (InputControls.getInstance().shootButtonJustPressed && InputControls.getInstance().downButtonPressed)))) {
                    gigaGal.setPosition(new Vector2(gigaGal.getPosition().x, ground.getBottom() + Constants.GIGAGAL_EYE_HEIGHT));
                    if (ground instanceof Reboundable) {
                        if (ground instanceof Spring && !((Spring) ground).underneathGround()) {
                            ((Reboundable) ground).resetStartTime();
                            ((Reboundable) ground).setState(false);
                        }
                    }
                    ((Pliable) ground).setCarrier(gigaGal);
                }
            } else if (((Pliable) ground).getCarrier() == gigaGal ) {
                if (ground instanceof Draggable) {
                    gigaGal.setVelocity(new Vector2(gigaGal.getVelocity().x / (1 + ((Draggable) ground).weightFactor()), gigaGal.getVelocity().y));
                    if (gigaGal.getVelocity().y > 0) {
                        ((Pliable) ground).setCarrier(null);
                    }
                }
                if (!InputControls.getInstance().shootButtonPressed) {
                    ((Pliable) ground).setCarrier(null);
                    if (ground instanceof Tossable && (InputControls.getInstance().leftButtonPressed || InputControls.getInstance().rightButtonPressed)) {
                        ((Tossable) ground).toss((gigaGal.getVelocity().x + Helpers.absoluteToDirectionalValue(ground.getWidth() * 13, gigaGal.getDirectionX(), Enums.Orientation.X)) * ((Tossable) ground).weightFactor());
                    }
                }
            }
        }
        if (ground instanceof Destructible) {
            if (((Destructible) ground).getHealth() < 1) {
                if (ground instanceof Box) {
                    grounds.add(new Brick(ground.getPosition().x, ground.getPosition().y, 5, 5, ((Destructible) ground).getType(), false));
                    assets.getSoundAssets().breakGround.play();
                    active = false;
                }
            }
        }
        if (ground instanceof Chargeable) {
            Chargeable chargeable = (Chargeable) ground;
            if (gigaGal.getChargeTimeSeconds() != Helpers.secondsSince(0) && gigaGal.getDirectionX() == Direction.RIGHT
                    && (int) gigaGal.getRight() + 1 == chargeable.getLeft() + 1 && gigaGal.getPosition().y - 4 == chargeable.getTop()) {
                if (!chargeable.isActive() && chargeable instanceof Chamber) {
                    chargeable.setState(true);
                } else if (gigaGal.getChargeTimeSeconds() > 1) {
                    chargeable.setChargeTime(gigaGal.getChargeTimeSeconds());
                }
                if (ground instanceof Chamber) {
                    Chamber chamber = (Chamber) ground;
                    if (chamber.isActive() && chamber.isCharged() && gigaGal.getShotIntensity() == Enums.ShotIntensity.NORMAL) {
                        String savedUpgrades = SaveData.getUpgrades();
                        Enums.Upgrade upgrade = chamber.getUpgrade();
                        if (!savedUpgrades.contains(upgrade.name())) {
                            assets.getSoundAssets().upgrade.play();
                            gigaGal.addUpgrade(upgrade);
                            SaveData.setUpgrades(upgrade.name() + ", " + savedUpgrades);
                        }
                    }
                } else {
                    if (gigaGal.getShotIntensity() == Enums.ShotIntensity.BLAST) {
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
                    if (ammo.getSource() instanceof GigaGal) {
                        assets.getSoundAssets().hitGround.play();
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
                    } else if (strikeable instanceof Canirol) {
                        Canirol canirol = (Canirol) strikeable;
                        canirol.convert();
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
                        Helpers.applyDamage((Destructible) ground, ammo);
                    } else if (strikeable instanceof Gate && ammo.getDirection() == Direction.RIGHT) { // prevents from re-unlocking after crossing gate boundary (always left to right)
                        ((Gate) strikeable).deactivate();
                    }
                }
            }
            projectiles.end();
        }
        return active;
    }

    public boolean updateHazard(float delta, Hazard hazard) {
        boolean active = true;
        if (hazard instanceof Destructible) {
            Destructible destructible = (Destructible) hazard;
            projectiles.begin();
            for (int j = 0; j < projectiles.size; j++) {
                Ammo ammo = projectiles.get(j);

                if (!ammo.equals(hazard) && ammo.isActive() && Helpers.overlapsPhysicalObject(ammo, destructible)) {

                    if (!(destructible instanceof Zoomba)
                    || !((ammo.getOrientation() == Enums.Orientation.X && Helpers.betweenTwoValues(ammo.getPosition().y, destructible.getBottom() + 5, destructible.getTop() - 5))
                    || (ammo.getOrientation() == Enums.Orientation.Y && Helpers.betweenTwoValues(ammo.getPosition().x, destructible.getLeft() + 5, destructible.getRight() - 5)))) {
                        Helpers.applyDamage(destructible, ammo);
                        score += ammo.getHitScore();
                    } else {
                        ((Zoomba) destructible).convert();
                    }
                    if (destructible instanceof  Zoomba) {
                        this.spawnImpact(ammo.getPosition(), ammo.getType());
                        ammo.deactivate();
                    }
                }
//
//                if (Helpers.overlapsPhysicalObject(Blade.getInstance(), hazard)) {
//
//                }
            }
            projectiles.end();
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
        return active;
    }

    public boolean updateTransport(float delta, Transport transport, int portalIndex) {
        boolean active = true;
        if (gigaGal.getPosition().dst(transport.getPosition()) < transport.getWidth() / 2 && inputControls.upButtonPressed && inputControls.jumpButtonJustPressed) {
            if (transport instanceof Portal) {
                for (int j = 0; j <= portalIndex; j++) {
                    if (!(transports.get(j) instanceof Portal)) {
                        portalIndex++;
                    }
                }
                assets.getSoundAssets().life.play();
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
                assets.getSoundAssets().warp.play();
                gigaGal.getPosition().set(transport.getDestination());
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
        entities.addAll(grounds);
        entities.addAll(hazards);
        entities.addAll(powerups);
        entities.addAll(transports);
        entities.addAll(impacts);
        chaseCam.setState(Enums.ChaseCamState.FOLLOWING);
        backdrop = new Backdrop(assets.getBackgroundAssets().getBackground(theme));
        music = assets.getMusicAssets().getThemeMusic(theme);
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

        gigaGal.setLives(3);
        gigaGal.respawn();

        // set level attributes
        viewport = levelScreen.getViewport();

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
                gigaGal.addWeapon(levelWeapon);
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
        if (gigaGal.getFallLimit() != -10000) {
            if (gigaGal.getPosition().y < gigaGal.getFallLimit() || gigaGal.getHealth() < 1) {
                gigaGal.setHealth(0);
                gigaGal.setLives(gigaGal.getLives() - 1);
                return true;
            }
        }
        return false;
    }

    protected boolean failed() {
        if (restarted()) {
            if (gigaGal.getLives() < 0) {
                return true;
            }
            boss.setPosition(new Vector2(boss.getRoomBounds().x + boss.getRoomBounds().width / 2, boss.getRoomBounds().y + boss.getRoomBounds().height / 2));
            if (chaseCam.getState() == Enums.ChaseCamState.BOSS) {
                chaseCam.setState(Enums.ChaseCamState.FOLLOWING);
            }
            if (musicEnabled) {
                music.stop();
                music = Assets.getInstance().getMusicAssets().getThemeMusic(theme);
                music.play();
            }
            gigaGal.respawn();
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
        Ammo ammo = new Ammo(this, position, direction, orientation, shotIntensity, weapon, source);
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

    // Getters
    protected final void addEntity(Entity entity) { entities.add(entity); }
    protected final void addGround(Ground ground) { grounds.add(ground); }
    protected final void addHazard(Hazard hazard) { hazards.add(hazard); }
    protected final void addPowerup(Powerup powerup) { powerups.add(powerup); }

    // to return cloned elements; state changes set from this class
    public final Array<Entity> getEntities() {
        Array<Entity> clonedEntities = new Array<Entity>();
        for (Entity entity : entities) {
            clonedEntities.add(entity.clone());
        }
        return clonedEntities;
    }

    public final Array<Ground> getGrounds() {
        Array<Ground> clonedGrounds = new Array<Ground>();
        for (Ground ground : grounds) {
            clonedGrounds.add((Ground) ground.clone());
        }
        return clonedGrounds;
    }

    public final Array<Hazard> getHazards() {
        Array<Hazard> clonedHazards = new Array<Hazard>();
        for (Hazard hazard : hazards) {
            clonedHazards.add((Hazard) hazard.clone());
        }
        return clonedHazards;
    }

    public final Array<Powerup> getPowerups() {
        Array<Powerup> clonedPowerups = new Array<Powerup>();
        for (Powerup powerup : powerups) {
            clonedPowerups.add((Powerup) powerup.clone());
        }
        return clonedPowerups;
    }

    public final long getUnsavedTime() { return time - savedTime; }
    public final int getUnsavedScore() { return score - savedScore; }
    public final long getTime() { return time; }
    public final int getScore() { return score; }
    public final void setBoss(Boss boss) { this.boss = boss; }
    public final Boss getBoss() { return boss; }
    public final Viewport getViewport() { return viewport; }
    public final DelayedRemovalArray<Transport> getTransports() { return transports; }
    public final GigaGal getGigaGal() { return gigaGal; }
    public final Enums.Material getType() { return levelWeapon; }
    protected Enums.Theme getTheme() { return theme; }
    public final boolean hasLoadEx() { return loadEx; }

    // Setters
    protected void setTime(long time) { this.time = time; }
    protected void setScore(int score) {this.score = score; }
    protected void setTheme(Enums.Theme selectedLevel) { theme = selectedLevel; }
    protected void toggleMusic() { musicEnabled = !musicEnabled; }
    protected void toggleHints() { hintsEnabled = !hintsEnabled; }
    protected final void setLoadEx(boolean state) { loadEx = state; }
}
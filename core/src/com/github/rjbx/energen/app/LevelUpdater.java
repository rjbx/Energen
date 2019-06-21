package com.github.rjbx.energen.app;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.entity.*;
import com.github.rjbx.energen.overlay.Backdrop;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.ChaseCam;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Enums.Direction;
import com.github.rjbx.energen.util.InputControls;
import com.github.rjbx.energen.util.Timer;
import com.github.rjbx.energen.util.Helpers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
    private DelayedRemovalArray<Projectile> projectiles;
    private Enums.Energy levelEnergy;
    private Enums.Theme theme;
    private Music music;
    private Avatar avatar;
    private Boss boss;
    private ChaseCam chaseCam;
    private String removedHazards;
    private boolean goalReached;
    private boolean paused;
    private boolean musicEnabled;
    private boolean hintsEnabled;
    private int score;
    private long time;
    private int savedScore;
    private long savedTime;
    private long refreshTime;

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
        projectiles = new DelayedRemovalArray<Projectile>();
        impacts = new DelayedRemovalArray<Impact>();
        powerups = new DelayedRemovalArray<Powerup>();
        transports = new DelayedRemovalArray<Transport>();
        loadEx = false;
        musicEnabled = false;
        hintsEnabled = true;
        removedHazards = "-1";
        score = 0;
        time = 0;
        refreshTime = 0;
        paused = false;
    }

    protected void update(float delta) {
        if (continuing() && !paused()) {
            updateEntities(delta);
        }
    }

    protected void render(SpriteBatch batch, Viewport viewport) {

        if (!continuing() || batch == null || viewport == null) return;

        Rectangle renderBounds = new Rectangle(chaseCam.getCamera().position.x - (chaseCam.getViewport().getWorldWidth() / 1.25f), chaseCam.getCamera().position.y - (chaseCam.getViewport().getWorldHeight() / 1.25f), chaseCam.getViewport().getWorldWidth() * 2.5f, chaseCam.getViewport().getWorldHeight() * 2.5f);

        Vector3 camPosition = chaseCam.getCamera().position;

//        if (theme == Enums.Theme.FINAL) {
            if (camPosition.y > 810 || (camPosition.x < 3575 && camPosition.y > 535)) backdrop = new Backdrop(assetManager.getBackgroundAssets().getBackground(Enums.Theme.ELECTROMAGNETIC));
            else if (camPosition.x < -180) backdrop = new Backdrop(assetManager.getBackgroundAssets().getBackground(Enums.Theme.HOME));
            else if (camPosition.y < 35) backdrop = new Backdrop(assetManager.getBackgroundAssets().getBackground(Enums.Theme.FINAL));
            else backdrop = new Backdrop(assetManager.getBackgroundAssets().getBackground(Enums.Theme.GRAVITATIONAL));
//        } else backdrop = new Backdrop(assetManager.getBackgroundAssets().getBackground(theme));

        backdrop.render(batch, viewport, new Vector2(chaseCam.getCamera().position.x, chaseCam.getCamera().position.y), Constants.BACKGROUND_CENTER, 1);

        for (Ground ground : grounds) {
            if (!ground.isDense() || ground instanceof Tripknob || ground instanceof Tripspring || ground instanceof Spring) {
                if (renderBounds.overlaps(new Rectangle(ground.getLeft(), ground.getBottom(), ground.getWidth(), ground.getHeight()))) ground.render(batch, viewport);
            }
        }

        for (Transport transport : transports) {
            if (renderBounds.overlaps(new Rectangle(transport.getLeft(), transport.getBottom(), transport.getWidth(), transport.getHeight()))) transport.render(batch, viewport);
        }

        for (Powerup powerup : powerups) {
            if (renderBounds.overlaps(new Rectangle(powerup.getLeft(), powerup.getBottom(), powerup.getWidth(), powerup.getHeight()))) powerup.render(batch, viewport);
        }

        for (Ground ground : grounds) {
            if (ground instanceof Vines || ground instanceof Deposit) {
                if (renderBounds.overlaps(new Rectangle(ground.getLeft(), ground.getBottom(), ground.getWidth(), ground.getHeight()))) ground.render(batch, viewport);
            }
        }

        for (Hazard hazard : hazards) {
            if (!(hazard instanceof Projectile)) {
                if (renderBounds.overlaps(new Rectangle(hazard.getLeft(), hazard.getBottom(), hazard.getWidth(), hazard.getHeight()))) hazard.render(batch, viewport);
            }
        }

        for (Ground ground : grounds) {
            if (ground.isDense() && !(ground instanceof Tripknob || ground instanceof Tripspring || ground instanceof Spring || ground instanceof Deposit)) {
                if (renderBounds.overlaps(new Rectangle(ground.getLeft(), ground.getBottom(), ground.getWidth(), ground.getHeight()))) ground.render(batch, viewport);
            }
        }

        avatar.render(batch, viewport);
        Blade.getInstance().render(batch, viewport);

        for (Ground ground : grounds) {
            if (ground instanceof Pliable && ((Pliable) ground).isBeingCarried()) {
                if (renderBounds.overlaps(new Rectangle(ground.getLeft(), ground.getBottom(), ground.getWidth(), ground.getHeight()))) ground.render(batch, viewport);
            }
        }

        for (Hazard hazard : hazards) {
            if (hazard instanceof Projectile) {
                if (renderBounds.overlaps(new Rectangle(hazard.getLeft(), hazard.getBottom(), hazard.getWidth(), hazard.getHeight()))) hazard.render(batch, viewport);
            }
        }

        for (Impact impact : impacts) {
            if (renderBounds.overlaps(new Rectangle(impact.getLeft(), impact.getBottom(), impact.getWidth(), impact.getHeight()))) impact.render(batch, viewport);
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
                Ground g = grounds.get(i);
                Rectangle updateBounds = new Rectangle(chaseCam.getCamera().position.x - (chaseCam.getViewport().getWorldWidth() * 4f), chaseCam.getCamera().position.y - (chaseCam.getViewport().getWorldHeight() * 4f), chaseCam.getViewport().getWorldWidth() * 8f, chaseCam.getViewport().getWorldHeight() * 8f);
                if (updateBounds.overlaps(new Rectangle(g.getLeft(), g.getBottom(), g.getWidth(), g.getHeight()))) {
                    if (g instanceof Nonstatic) {
                        for (Rectangle convertBounds : chaseCam.getConvertBounds()) {
                            if (convertBounds.overlaps(new Rectangle(g.getPosition().x, g.getPosition().y, g.getWidth(), g.getHeight()))) {
                                updateGround(delta, g);
                            }
                        }
                    }
                }
            }
            grounds.end();
        } else {
            // TODO: Restore projectile spawn when in Boss range
            Rectangle updateBounds = new Rectangle(chaseCam.getCamera().position.x - (chaseCam.getViewport().getWorldWidth() * 4f), chaseCam.getCamera().position.y - (chaseCam.getViewport().getWorldHeight() * 4f), chaseCam.getViewport().getWorldWidth() * 8f, chaseCam.getViewport().getWorldHeight() * 8f);
            if (updateBounds.overlaps(new Rectangle(boss.getLeft(), boss.getBottom(), boss.getWidth(), boss.getHeight()))) {
                if (boss != null && (boss.isTalking() || boss.getHealth() < 1)) {
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
                }
                if (boss.getDispatchStatus()) {
                    if (boss.getLookStartTime() != 0) {
                        if (boss.getDirectionY() == Direction.UP) {
                            spawnProjectile(new Vector2(boss.getPosition().x + Helpers.speedToVelocity(Constants.AVATAR_Y_CANNON_OFFSET.x, boss.getDirectionX(), Enums.Orientation.X), boss.getPosition().y + Constants.AVATAR_Y_CANNON_OFFSET.y + 5), boss.getDirectionY(), Enums.Orientation.Y, boss.getShotIntensity(), boss.getEnergy(), boss);
                        } else {
                            spawnProjectile(new Vector2(boss.getPosition().x + Helpers.speedToVelocity(Constants.AVATAR_Y_CANNON_OFFSET.x - 3, boss.getDirectionX(), Enums.Orientation.X), boss.getPosition().y - Constants.AVATAR_Y_CANNON_OFFSET.y - 8), boss.getDirectionY(), Enums.Orientation.Y, boss.getShotIntensity(), boss.getEnergy(), boss);
                        }
                    } else {
                        spawnProjectile(new Vector2(boss.getPosition().x + Helpers.speedToVelocity(Constants.AVATAR_X_CANNON_OFFSET.x, boss.getDirectionX(), Enums.Orientation.X), boss.getPosition().y + Constants.AVATAR_X_CANNON_OFFSET.y), boss.getDirectionX(), Enums.Orientation.X, boss.getShotIntensity(), boss.getEnergy(), boss);
                    }
                    boss.resetChargeIntensity();
                }
                if (boss.getTouchedHazard() != null) {
                    Vector2 intersectionPoint = new Vector2();
                    Hazardous touchedHazard = boss.getTouchedHazard();
                    intersectionPoint.x = Math.max(boss.getLeft(), touchedHazard.getLeft());
                    intersectionPoint.y = Math.max(boss.getBottom(), touchedHazard.getBottom());
                    spawnImpact(intersectionPoint, touchedHazard.getType());
                }
            }

            time = timer.getNanos();
            if (avatar.getDispatchStatus()) {
                if (avatar.getLookStartTime() != 0) {
                    if (avatar.getDirectionY() == Direction.UP) {
                        spawnProjectile(new Vector2(avatar.getPosition().x + Helpers.speedToVelocity(Constants.AVATAR_Y_CANNON_OFFSET.x, avatar.getDirectionX(), Enums.Orientation.X), avatar.getPosition().y + Constants.AVATAR_Y_CANNON_OFFSET.y), avatar.getDirectionY(), Enums.Orientation.Y, avatar.getShotIntensity(), avatar.getEnergy(), avatar);
                    } else {
                        spawnProjectile(new Vector2(avatar.getPosition().x + Helpers.speedToVelocity(Constants.AVATAR_Y_CANNON_OFFSET.x - 3, avatar.getDirectionX(), Enums.Orientation.X), avatar.getPosition().y - Constants.AVATAR_Y_CANNON_OFFSET.y - 8), avatar.getDirectionY(), Enums.Orientation.Y, avatar.getShotIntensity(), avatar.getEnergy(), avatar);
                    }
                } else {
                    spawnProjectile(new Vector2(avatar.getPosition().x + Helpers.speedToVelocity(Constants.AVATAR_X_CANNON_OFFSET.x, avatar.getDirectionX(), Enums.Orientation.X), avatar.getPosition().y + Constants.AVATAR_X_CANNON_OFFSET.y), avatar.getDirectionX(), Enums.Orientation.X, avatar.getShotIntensity(), avatar.getEnergy(), avatar);
                }
                avatar.resetChargeIntensity();
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
                Transport t = transports.get(i);
                if (updateBounds.overlaps(new Rectangle(t.getLeft(), t.getBottom(), t.getWidth(), t.getHeight()))) {
                    if (!updateTransport(delta, t, i)) {
                        transports.removeIndex(i);
                    }
                }
            }
            transports.end();

            // Update Hazards
            hazards.begin();
            for (int i = 0; i < hazards.size; i++) {
                Hazard h = hazards.get(i);
                if (updateBounds.overlaps(new Rectangle(h.getLeft(), h.getBottom(), h.getWidth(), h.getHeight()))) {
                    if (!updateHazard(delta, h)) {
                        spawnPowerup(h);
                        hazards.removeIndex(i);
                        removedHazards += (";" + i); // ';' delimeter prevents conflict with higher level parse (for str containing all level removal lists)
                    }
                }
            }
            hazards.end();

            // Update Grounds
            if (Helpers.secondsSince(refreshTime) > 10) {
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
                refreshTime = TimeUtils.nanoTime();
            }

            grounds.begin();
            for (int i = 0; i < grounds.size; i++) {
                Ground g = grounds.get(i);
                if (updateBounds.overlaps(new Rectangle(g.getLeft(), g.getBottom(), g.getWidth(), g.getHeight()))) {
                    if (!(g instanceof Pliable)
                            || !(((Pliable) g).isBeingCarried())
                            || !(((Pliable) g).getMovingGround() instanceof Pliable)
                            || !((Pliable) ((Pliable) g).getMovingGround()).isBeingCarried()) {
                        if (!updateGround(delta, g)) {
                            grounds.removeIndex(i);
                        }
                    }
                }
            }
            grounds.end();

            // Update Impacts
            impacts.begin();
            for (int index = 0; index < impacts.size; index++) {
                Impact i = impacts.get(index);
                if (updateBounds.overlaps(new Rectangle(i.getLeft(), i.getBottom(), i.getWidth(), i.getHeight()))) {
                    if (i.isFinished()) {
                        impacts.removeIndex(index);
                    }
                }
            }
            impacts.end();

            // Update Powerups
            powerups.begin();
            for (int i = 0; i < powerups.size; i++) {
                Powerup p = powerups.get(i);
                // TODO: Restore consistently applied collision
                if (updateBounds.overlaps(new Rectangle(p.getLeft(), p.getBottom(), p.getWidth(), p.getHeight()))) {
                    if (!updatePowerup(delta, p)) {
                        powerups.removeIndex(i);
                    }
                }
            }
            powerups.end();

            avatar.updatePosition(delta);
            applyCollision(avatar);
            avatar.update(delta);
            Blade.getInstance().update(delta);

            // Update Grounds
            grounds.begin();
            for (int i = 0; i < grounds.size; i++) {
                Ground g = grounds.get(i);
                if (updateBounds.overlaps(new Rectangle(g.getLeft(), g.getBottom(), g.getWidth(), g.getHeight()))) {
                    if ((grounds.get(i) instanceof Pliable)
                            && ((((Pliable) grounds.get(i)).isBeingCarried())
                            || (((Pliable) grounds.get(i)).isAtopMovingGround()
                            && ((Pliable) grounds.get(i)).getMovingGround() instanceof Pliable
                            && ((Pliable) ((Pliable) grounds.get(i)).getMovingGround()).isBeingCarried()))) {
                        if (!updateGround(delta, grounds.get(i))) {
                            grounds.removeIndex(i);
                        }
                    }
                }
            }
            grounds.end();
        }
    }

    public boolean updateGround(float delta, Ground ground) {

        if (ground instanceof Energized && ((Energized) ground).getDispatchStatus()) {
            Energized energy = (Energized) ground;
            Enums.Orientation orientation = energy.getOrientation();
            Vector2 offset = new Vector2();
            if (energy instanceof Cannoroll) {
                offset.set(energy.getWidth(), energy.getHeight());
            }
            if (orientation == Enums.Orientation.X) {
                Vector2 ammoPositionLeft = new Vector2(energy.getPosition().x - offset.x, energy.getPosition().y);
                Vector2 ammoPositionRight = new Vector2(energy.getPosition().x + offset.x, energy.getPosition().y);
                if (Avatar.getInstance().getPosition().x < (ammoPositionLeft.x - offset.x)) {
                    LevelUpdater.getInstance().spawnProjectile(ammoPositionLeft, Enums.Direction.LEFT, orientation, energy.getIntensity(), LevelUpdater.getInstance().getType(), ground);
                } else if (Avatar.getInstance().getPosition().x > (ammoPositionRight.x + (energy.getWidth() / 2))) {
                    LevelUpdater.getInstance().spawnProjectile(ammoPositionRight, Enums.Direction.RIGHT, orientation, energy.getIntensity(), LevelUpdater.getInstance().getType(), ground);
                }
            } else if (orientation == Enums.Orientation.Y) {
                Vector2 ammoPositionTop = new Vector2(energy.getPosition().x, energy.getPosition().y + offset.y);
                Vector2 ammoPositionBottom = new Vector2(energy.getPosition().x, energy.getPosition().y - offset.y);
                if (energy instanceof Cannon) {
                    if (Avatar.getInstance().getPosition().y < (ammoPositionBottom.y - offset.y)) {
                        LevelUpdater.getInstance().spawnProjectile(ammoPositionBottom, Enums.Direction.DOWN, orientation, energy.getIntensity(), LevelUpdater.getInstance().getType(), ground);
                    } else if (Avatar.getInstance().getPosition().y > (ammoPositionTop.y + (energy.getHeight() / 2))) {
                        LevelUpdater.getInstance().spawnProjectile(ammoPositionTop, Enums.Direction.UP, orientation, energy.getIntensity(), LevelUpdater.getInstance().getType(), ground);
                    }
                } else {
                    LevelUpdater.getInstance().spawnProjectile(ammoPositionTop, Enums.Direction.UP, orientation, energy.getIntensity(), LevelUpdater.getInstance().getType(), ground);
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
                        && !(trip.getBounds().overlaps(new Rectangle(chaseCam.getCamera().position.x - chaseCam.getViewport().getWorldWidth() / 4, chaseCam.getCamera().position.y - chaseCam.getViewport().getWorldHeight() / 4, chaseCam.getViewport().getWorldWidth() / 2, chaseCam.getViewport().getWorldHeight() / 2)))) { // halving dimensions heightens camera sensitivity

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
                                float yPos = avatar.getAction() != Enums.Action.RAPPELLING ? ground.getBottom() : ground.getTop();
                                avatar.setPosition(new Vector2(ground.getPosition().x, yPos + Constants.AVATAR_EYE_HEIGHT)); // prevents overlap with and attribute inheritance of pliable stacked atop
                                pliable.setCarrier(avatar);
                            }
                            avatar.setCarriedGround(pliable);
                        }
                    }
                }
            } else if (pliable.getCarrier() == avatar) {
                if (avatar.getAction() != Enums.Action.STANDING) {
                    float adjustment = .25f;
                    if (avatar.getGroundState() != Enums.GroundState.PLANTED) {
                        adjustment *= 2;
                    } else {
                        avatar.setVelocity(new Vector2(avatar.getVelocity().x / (1 + (pliable).weightFactor()), avatar.getVelocity().y));
                    }
                    if (pliable.weightFactor() > .627f) { // prevents flickering gauge for lighter objects
                        float decrement = (pliable).weightFactor() * adjustment;
                        avatar.setTurbo(Math.max(avatar.getTurbo() - decrement, 0));
                    }
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
                    grounds.add(new Brick(ground.getPosition().x, ground.getPosition().y, 5, 5, ((Destructible) ground).getType()));
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
                Projectile projectile = projectiles.get(j);
                if (Helpers.overlapsPhysicalObject(projectile, ground)) {
                    if (projectile.getSource() instanceof Avatar) {
                        assetManager.getSoundAssets().hitGround.play();
                    }
                    if (projectile.isActive() &&
                            (ground.isDense() // collides with all sides of dense ground
                                    || Helpers.overlapsBetweenTwoSides(projectile.getPosition().y, projectile.getHeight() / 2, ground.getTop() - 3, ground.getTop()))) { // collides only with top of non-dense ground
                        if (!projectile.getPosition().equals(Vector2.Zero)) {
                            this.spawnImpact(projectile.getPosition(), projectile.getType());
                        }
                        projectile.deactivate();
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
                        } else if (chargeable instanceof Tripchamber && projectile.getShotIntensity() == Enums.ShotIntensity.BLAST) {
                            if (chargeable.isCharged()) {
                                chargeable.setState(!chargeable.isActive());
                                chargeable.uncharge();
                            }
                        }
                    } else if (strikeable instanceof Destructible) {
                        Helpers.applyDamage((Destructible) ground, projectile);
                    } else if (strikeable instanceof Gate && projectile.getDirection() == Direction.RIGHT) { // prevents from re-unlocking after crossing gate boundary (always left to right)
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
        Rectangle updateBounds = new Rectangle(chaseCam.getCamera().position.x - (chaseCam.getViewport().getWorldWidth() * 4f), chaseCam.getCamera().position.y - (chaseCam.getViewport().getWorldHeight() * 4f), chaseCam.getViewport().getWorldWidth() * 8f, chaseCam.getViewport().getWorldHeight() * 8f);
        if (!updateBounds.overlaps(new Rectangle(hazard.getLeft(), hazard.getBottom(), hazard.getWidth(), hazard.getHeight()))) return true;

        boolean active = true;
        if (hazard instanceof Boss) {
            ((Boss) hazard).updatePosition(delta);
            applyCollision((Impermeable) hazard);
            if (boss.getRoomBounds().overlaps(avatar.getBounds())) {
                if (!boss.isBattling() || boss.getHealth() < 1) {
                    boss.setTalkState(true);
                }
            }
        }
        if (hazard instanceof Destructible) {
            Destructible destructible = (Destructible) hazard;
            projectiles.begin();
            for (int j = 0; j < projectiles.size; j++) {
                Projectile projectile = projectiles.get(j);
                if (!projectile.equals(hazard) && projectile.isActive() && Helpers.overlapsPhysicalObject(projectile, destructible)) {
                    // TODO: Restore projectile spawn when in Zoomba range
                    if (!(destructible instanceof Zoomba)
                    || !((projectile.getOrientation() == Enums.Orientation.X && Helpers.betweenTwoValues(projectile.getPosition().y, destructible.getBottom() + 5, destructible.getTop() - 5))
                    || (projectile.getOrientation() == Enums.Orientation.Y && Helpers.betweenTwoValues(projectile.getPosition().x, destructible.getLeft() + 5, destructible.getRight() - 5)))) {
                        if (!(hazard instanceof Armored || hazard instanceof Boss)) {
                            Helpers.applyDamage(destructible, projectile);
                            this.spawnImpact(projectile.getPosition(), projectile.getType());
                            projectile.deactivate();
                        } else {
                            AssetManager.getInstance().getSoundAssets().hitGround.play();
                            projectile.deactivate();
                        }
                        score += projectile.getHitScore();
                    } else if (destructible instanceof Zoomba) {
                        ((Zoomba) destructible).convert();
                        if (avatar.getTouchedGround() != null && avatar.getTouchedGround().equals(destructible)) {
                            avatar.setPosition(new Vector2(destructible.getPosition().x, destructible.getTop() + Constants.AVATAR_EYE_HEIGHT));
                        }
                    }
                    if (destructible instanceof Zoomba) {
                        this.spawnImpact(projectile.getPosition(), projectile.getType());
                        projectile.deactivate();
                    }
                }
            }
            projectiles.end();

            if (Helpers.overlapsPhysicalObject(Blade.getInstance(), destructible) && !(hazard instanceof Boss)) {
                if (avatar.getBladeState() == Enums.BladeState.FLIP
                        || (avatar.getBladeState() == Enums.BladeState.RUSH && Helpers.betweenTwoValues(destructible.getPosition().y, avatar.getBottom(), avatar.getTop()))
                        || (avatar.getBladeState() == Enums.BladeState.CUT) && (Helpers.speedToVelocity(destructible.getPosition().x, avatar.getDirectionX(), Enums.Orientation.X) - Helpers.speedToVelocity(avatar.getPosition().x, avatar.getDirectionX(), Enums.Orientation.X) > 0)) {
                    if (!(hazard instanceof Armored)) {
                        Helpers.applyDamage(destructible, Blade.getInstance());
                        spawnImpact(hazard.getPosition(), Blade.getInstance().getType());
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
                if (destructible instanceof Armoroll || destructible instanceof Bladeroll) {
                    Trippable trip = (Trippable) destructible;
                    trip.convert();
                    if (trip.tripped()) {
                        if (hintsEnabled
                                && !trip.maxAdjustmentsReached()
                                && !trip.getBounds().equals(Rectangle.tmp) // where tmp has bounds of (0,0,0,0)
                                && !(trip.getBounds().overlaps(new Rectangle(chaseCam.getCamera().position.x - chaseCam.getViewport().getWorldWidth() / 4, chaseCam.getCamera().position.y - chaseCam.getViewport().getWorldHeight() / 4, chaseCam.getViewport().getWorldWidth() / 2, chaseCam.getViewport().getWorldHeight() / 2)))) { // halving dimensions heightens camera sensitivity

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
                spawnImpact(destructible.getPosition(), destructible.getType());
                active = false;
                score += (destructible.getKillScore() * Constants.DIFFICULTY_MULTIPLIER[SaveData.getDifficulty()]);
            }
            if (destructible instanceof Orben && ((Orben) destructible).getDispatchStatus()) {
                Vector2 ammoPositionLeft = new Vector2(destructible.getPosition().x - (destructible.getWidth() * 1.1f), destructible.getPosition().y);
                Vector2 ammoPositionRight = new Vector2(destructible.getPosition().x + (destructible.getWidth() * 1.1f), destructible.getPosition().y);
                Vector2 ammoPositionTop = new Vector2(destructible.getPosition().x, destructible.getPosition().y + (destructible.getHeight() * 1.1f));
                Vector2 ammoPositionBottom = new Vector2(destructible.getPosition().x, destructible.getPosition().y - (destructible.getHeight() * 1.1f));

                LevelUpdater.getInstance().spawnProjectile(ammoPositionLeft, Enums.Direction.LEFT, Enums.Orientation.X, Enums.ShotIntensity.BLAST, destructible.getType(), hazard);
                LevelUpdater.getInstance().spawnProjectile(ammoPositionRight, Enums.Direction.RIGHT, Enums.Orientation.X, Enums.ShotIntensity.BLAST, destructible.getType(), hazard);
                LevelUpdater.getInstance().spawnProjectile(ammoPositionBottom, Enums.Direction.DOWN, Enums.Orientation.Y, Enums.ShotIntensity.BLAST, destructible.getType(), hazard);
                LevelUpdater.getInstance().spawnProjectile(ammoPositionTop, Enums.Direction.UP, Enums.Orientation.Y, Enums.ShotIntensity.BLAST, destructible.getType(), hazard);
            }
        } else if (hazard instanceof Projectile) {
            Projectile projectile = (Projectile) hazard;
            projectile.update(delta);
            if (!projectile.isActive()) {
                active = false;
                projectiles.removeValue(projectile, false);
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

    public boolean updatePowerup(float delta, Powerup powerup) {
        Rectangle updateBounds = new Rectangle(chaseCam.getCamera().position.x - (chaseCam.getViewport().getWorldWidth() * 4f), chaseCam.getCamera().position.y - (chaseCam.getViewport().getWorldHeight() * 4f), chaseCam.getViewport().getWorldWidth() * 8f, chaseCam.getViewport().getWorldHeight() * 8f);
        if (!updateBounds.overlaps(new Rectangle(powerup.getLeft(), powerup.getBottom(), powerup.getWidth(), powerup.getHeight()))) return true;

        if (Helpers.overlapsPhysicalObject(avatar, powerup)
                || (avatar.getBladeState() != Enums.BladeState.RETRACTED && Helpers.overlapsPhysicalObject(Blade.getInstance(), powerup))) {
            powerup.deactivate();
            if (powerup.getType() == Enums.PowerupType.LIFE) score += Constants.POWERUP_SCORE;
        } else {
            powerup.update(delta);
        }
        return powerup.isActive();
    }

    public boolean updateTransport(float delta, Transport transport, int transportIndex) {
        Rectangle updateBounds = new Rectangle(chaseCam.getCamera().position.x - (chaseCam.getViewport().getWorldWidth() * 4f), chaseCam.getCamera().position.y - (chaseCam.getViewport().getWorldHeight() * 4f), chaseCam.getViewport().getWorldWidth() * 8f, chaseCam.getViewport().getWorldHeight() * 8f);
        if (!updateBounds.overlaps(new Rectangle(transport.getLeft(), transport.getBottom(), transport.getWidth(), transport.getHeight()))) return true;
        boolean active = true;
        if (avatar.getPosition().dst(transport.getPosition()) < transport.getWidth() / 2 && inputControls.upButtonPressed && inputControls.jumpButtonJustPressed) {
            if (transport instanceof Portal) {
                if (((Portal) transport).isGoal()) {
                    goalReached = true;
                    return false;
                }
                for (int j = 0; j <= transportIndex; j++) {
                    if (j < transports.size && !(transports.get(j) instanceof Portal)) {
                        // Persisted indeces are prior adjusted to align with list values on level load; outliers are spawns
                        transportIndex++;
                    }
                }
                assetManager.getSoundAssets().life.play();
                int level = Arrays.asList(Enums.Theme.values()).indexOf(this.theme);
                List<String> allRestores = Arrays.asList(SaveData.getLevelRestore().split(", "));
                List<String> allTimes = Arrays.asList(SaveData.getLevelTimes().split(", "));
                List<String> allScores = Arrays.asList(SaveData.getLevelScores().split(", "));
                List<String> allRemovals = Arrays.asList(SaveData.getLevelRemovals().split(", "));
                Vector2 restorePosition = avatar.getPosition();
                allRestores.set(level, restorePosition.x + ":" + restorePosition.y);
                allTimes.set(level, Long.toString(time));
                allScores.set(level, Integer.toString(score));
                allRemovals.set(level, removedHazards);
                SaveData.setLevelRestore(allRestores.toString().replace("[", "").replace("]", ""));
                SaveData.setLevelTimes(allTimes.toString().replace("[", "").replace("]", ""));
                SaveData.setLevelScores(allScores.toString().replace("[", "").replace("]", ""));
                SaveData.setLevelRemovals(allRemovals.toString().replace("[", "").replace("]", ""));

                SaveData.setTotalTime(Helpers.numStrToSum(allTimes));
                SaveData.setTotalScore((int) Helpers.numStrToSum(allScores));

                savedScore = score;
                savedTime = Long.parseLong(allTimes.get(level));
            } else if (transport instanceof Teleport) {
                if (((Teleport) transport).isGoal()) {
                    goalReached = true;
                    return false;
                }
                assetManager.getSoundAssets().warp.play();
                avatar.getPosition().set(transport.getDestination());
                avatar.setFallLimit(transport.getDestination().y - Constants.FALL_LIMIT);
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
            if (removal < hazards.size && removal != -1) {
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
        backdrop = new Backdrop(assetManager.getBackgroundAssets().getBackground(theme));
        music = assetManager.getMusicAssets().getThemeMusic(theme);
        music.setLooping(true);
        if (musicEnabled) {
            music.play();
        }
        levelEnergy = Enums.Energy.NATIVE;
        for (Enums.Energy energy : Arrays.asList(Enums.Energy.values())) {
            if (energy.theme().equals(theme)) {
                levelEnergy = energy;
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
//            int level = Arrays.asList(Enums.Theme.values()).indexOf(this.theme);
//            List<String> allTimes = Arrays.asList(SaveData.getLevelTimes().split(", "));
//            List<String> allScores = Arrays.asList(SaveData.getLevelScores().split(", "));
//            List<String> allRemovals = Arrays.asList(SaveData.getLevelRemovals().split(", "));
//            allTimes.set(level, Long.toString(time));
//            allScores.set(level, Integer.toString(score));
//            allRemovals.set(level, "1");
//            SaveData.setLevelTimes(allTimes.toString().replace("[", "").replace("]", ""));
//            SaveData.setLevelScores(allScores.toString().replace("[", "").replace("]", ""));
//            SaveData.setLevelRemovals(allRemovals.toString().replace("[", "").replace("]", ""));

            SaveData.setTotalScore(SaveData.getTotalScore() + score);
            SaveData.setTotalTime(SaveData.getTotalTime() + time);

            String savedEnergies = SaveData.getEnergies();
            if (!savedEnergies.contains(levelEnergy.name())) {
                avatar.addEnergy(levelEnergy);
                SaveData.setEnergies(levelEnergy.name() + ", " + savedEnergies);
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
        List<String> allRestores = Arrays.asList(SaveData.getLevelRestore().split(", "));
        List<String> allTimes = Arrays.asList(SaveData.getLevelTimes().split(", "));
        List<String> allScores = Arrays.asList(SaveData.getLevelScores().split(", "));
        List<String> allRemovals = Arrays.asList(SaveData.getLevelRemovals().split(", "));
        allTimes.set(level, "0");
        allScores.set(level, "0");
        allRestores.set(level, "0:0");
        allRemovals.set(level, "-1");
        SaveData.setLevelRestore(allRestores.toString().replace("[", "").replace("]", ""));
        SaveData.setLevelTimes(allTimes.toString().replace("[", "").replace("]", ""));
        SaveData.setLevelScores(allScores.toString().replace("[", "").replace("]", ""));
        SaveData.setLevelRemovals(allRemovals.toString().replace("[", "").replace("]", ""));
    }

    protected boolean restarted() {
        if (avatar.getFallLimit() != -10000) {
            if (avatar.getPosition().y < avatar.getFallLimit() || avatar.getHealth() < 1) {
                avatar.setHealth(0);
                avatar.setLives(avatar.getLives() - 1);
                if (chaseCam.getState() == Enums.ChaseCamState.BOSS) {
                    chaseCam.setState(Enums.ChaseCamState.FOLLOWING);
                }
                boss.setBattleState(false);
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
            if (musicEnabled) {
                music.stop();
                music = AssetManager.getInstance().getMusicAssets().getThemeMusic(theme);
                music.play();
            }
            avatar.respawn();
            boss.setPosition(new Vector2(boss.getRoomBounds().x + boss.getRoomBounds().width / 2, boss.getRoomBounds().y + boss.getRoomBounds().height / 2));
        }
        return false;
    }

    protected boolean completed() { return goalReached || (chaseCam.getState() == Enums.ChaseCamState.BOSS && boss.getHealth() < 1 && boss.isBattling()); }

    protected boolean continuing() { return !(completed() || failed()); }

    protected boolean paused() {
        return paused;
    }

    private void spawnImpact(Vector2 position, Enums.Energy type) {
        impacts.add(new Impact(position, type));
    }

    private void spawnProjectile(Vector2 position, Direction direction, Enums.Orientation orientation, Enums.ShotIntensity shotIntensity, Enums.Energy energy, Entity source) {
        Projectile projectile = new Projectile(position, direction, orientation, shotIntensity, energy, source);
        hazards.add(projectile);
        projectiles.add(projectile);
    }

    private void spawnPowerup(Hazard hazard) {
        if (!(hazard instanceof Projectile)) {
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
    protected final Enums.Energy getType() { return levelEnergy; }
    protected final Viewport getViewport() { return levelScreen.getViewport(); }

    // Protected getters
    protected final long getUnsavedTime() { return timer.getMillis() - TimeUtils.nanosToMillis(savedTime); }
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
    protected void setTheme(Enums.Theme selectedLevel) {
        theme = selectedLevel;

        int level = Arrays.asList(Enums.Theme.values()).indexOf(this.theme);
        savedTime = Long.parseLong(SaveData.getLevelScores().split(", ")[level]);
    }
    protected int getIndex() { return Arrays.asList(Enums.Theme.values()).indexOf(this.theme); }
    protected void toggleMusic() { musicEnabled = !musicEnabled; }
    protected void toggleHints() { hintsEnabled = !hintsEnabled; }
    protected final void setLoadEx(boolean state) { loadEx = state; }

    protected final Backdrop getBackdrop() { return backdrop; }
}

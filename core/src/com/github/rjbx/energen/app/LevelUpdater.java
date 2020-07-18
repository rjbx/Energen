package com.github.rjbx.energen.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.GdxRuntimeException;
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
    private DelayedRemovalArray<Ground> grounds;
    private DelayedRemovalArray<Hazard> hazards;
    private DelayedRemovalArray<Powerup> powerups;
    private DelayedRemovalArray<Transport> transports;
    private DelayedRemovalArray<Impact> impacts;
    private DelayedRemovalArray<Entity> scopedEntities;
    private DelayedRemovalArray<Ground> scopedGrounds;
    private DelayedRemovalArray<Hazard> scopedHazards;
    private DelayedRemovalArray<Powerup> scopedPowerups;
    private DelayedRemovalArray<Transport> scopedTransports;
    private DelayedRemovalArray<Impact> scopedImpacts;
    private Enums.Energy levelEnergy;
    private Enums.Theme theme;
    private Music music;
    private Enums.MusicStyle musicStyle;
    private Avatar avatar;
    private Boss boss;
    private ChaseCam chaseCam;
    private StringBuilder removedHazards;
    private Rectangle rescopeBounds;
    private boolean goalReached;
    private boolean paused;
    private boolean musicEnabled;
    private boolean hintsEnabled;
    private boolean entitiesUpdated;
    private boolean stateUpdated;
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
        grounds = new DelayedRemovalArray<Ground>();
        hazards = new DelayedRemovalArray<Hazard>();
        impacts = new DelayedRemovalArray<Impact>();
        powerups = new DelayedRemovalArray<Powerup>();
        transports = new DelayedRemovalArray<Transport>();
        scopedEntities = new DelayedRemovalArray<Entity>();
        scopedGrounds = new DelayedRemovalArray<Ground>();
        scopedHazards = new DelayedRemovalArray<Hazard>();
        scopedImpacts = new DelayedRemovalArray<Impact>();
        scopedPowerups = new DelayedRemovalArray<Powerup>();
        scopedTransports = new DelayedRemovalArray<Transport>();
        loadEx = false;
        musicEnabled = SaveData.hasMusic();
        musicStyle = Enums.MusicStyle.valueOf(SaveData.getStyle());
        hintsEnabled = true;
        removedHazards = new StringBuilder("-1");
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

        if (!continuing() || batch == null || viewport == null) return;

        Vector3 camPosition = chaseCam.getCamera().position;

        if (camPosition.y > 810 || (camPosition.x < 3575 && camPosition.y > 535)) backdrop = new Backdrop(assetManager.getBackgroundAssets().getBackground(Enums.Theme.ELECTROMAGNETIC));
        else if (camPosition.x < -180) backdrop = new Backdrop(assetManager.getBackgroundAssets().getBackground(Enums.Theme.HOME));
        else if (camPosition.y < 35) backdrop = new Backdrop(assetManager.getBackgroundAssets().getBackground(Enums.Theme.FINAL));
        else backdrop = new Backdrop(assetManager.getBackgroundAssets().getBackground(Enums.Theme.GRAVITATIONAL));

        backdrop.render(batch, viewport, new Vector2(chaseCam.getCamera().position.x, chaseCam.getCamera().position.y), Constants.BACKGROUND_CENTER, 1);

        for (Entity entity : scopedEntities) entity.render(batch, viewport);
    }

    private void rescopeEntities() {
        scopedEntities.clear();
        scopedEntities.add(avatar);
        scopedEntities.add(Blade.getInstance());
        scopedEntities.addAll(scopedGrounds);
        scopedEntities.addAll(scopedHazards);
        scopedEntities.addAll(scopedTransports);
        scopedEntities.addAll(scopedImpacts);
        scopedEntities.addAll(scopedPowerups);
        scopedEntities.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                if (o1.getPriority() > o2.getPriority()) return -1;
                else if (o1.getPriority() < o2.getPriority()) return 1;
                return 0;
            }
        });
    }

    private void applyCollision(Impermeable impermeable) {
        if (impermeable instanceof Humanoid) {
            ((Humanoid) impermeable).touchAllGrounds(scopedGrounds);
            ((Humanoid) impermeable).touchAllHazards(scopedHazards);
        }
    }

    // asset handling
    private void updateEntities(float delta) {
        scopedEntities.begin();
        entitiesUpdated = false;
        if (chaseCam.getState() == Enums.ChaseCamState.CONVERT) {
            grounds.begin();
            scopedGrounds.begin();
            
            for (int i = 0; i < grounds.size; i++) {
                Ground g = grounds.get(i);
                if (g instanceof Nonstatic) {
                    for (Rectangle convertBounds : chaseCam.getConvertBounds()) {
                        if (convertBounds.overlaps(new Rectangle(g.getPosition().x, g.getPosition().y, g.getWidth(), g.getHeight()))) {
                            updateGround(delta, g);
                            scopeEntity(scopedGrounds, g);
                        }
                    }
                }
            }

            grounds.end();
            scopedGrounds.end();
        } else if (boss != null && (boss.isTalking() || boss.getHealth() < 1)) {
            if (chaseCam.getState() != Enums.ChaseCamState.BOSS) {
                chaseCam.setState(Enums.ChaseCamState.BOSS);
            } else if (avatar.getPosition().x < boss.getPosition().x - boss.getRoomBounds().width / 3) {
                if (music.isPlaying()) music.stop();
                avatar.setVelocity(new Vector2(40, 0));
                avatar.setPosition(avatar.getPosition().mulAdd(avatar.getVelocity(), delta));
                avatar.stride();
            } else {
                if (avatar.getAction() != Enums.Action.STANDING) {
                    avatar.setAction(Enums.Action.STANDING);
                    music = AssetManager.getInstance().getMusicAssets().dialog;
                    music.play();
                } else if (InputControls.getInstance().shootButtonJustPressed) {
                    boss.setBattleState(true);
                    if (musicEnabled) {
                        if (music.isPlaying()) music.stop();
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
            Rectangle determinantBounds = new Rectangle(chaseCam.getCamera().position.x - (chaseCam.getViewport().getWorldWidth() * 0.5f), chaseCam.getCamera().position.y - (chaseCam.getViewport().getWorldHeight() * 0.5f), chaseCam.getViewport().getWorldWidth(), chaseCam.getViewport().getWorldHeight());
            if (rescopeBounds == null || !rescopeBounds.contains(determinantBounds)) {
                rescopeBounds = new Rectangle(chaseCam.getCamera().position.x - (chaseCam.getViewport().getWorldWidth() * 2.5f), chaseCam.getCamera().position.y - (chaseCam.getViewport().getWorldHeight() * 2.5f), chaseCam.getViewport().getWorldWidth() * 5f, chaseCam.getViewport().getWorldHeight() * 5f);

                scopedEntities.add(avatar);
                scopedEntities.add(Blade.getInstance());

                transports.begin();
                scopedTransports.begin();
                for (int i = 0; i < transports.size; i++) {
                    Transport t = transports.get(i);
                    if (rescopeBounds.overlaps(new Rectangle(t.getLeft(), t.getBottom(), t.getWidth(), t.getHeight()))) {
                        if (!updateTransport(delta, t, i)) {
                            transports.removeIndex(i);
                            unscopeEntity(scopedTransports, t);
                        } else scopeEntity(scopedTransports, t);
                    } else unscopeEntity(scopedTransports, t);
                }
                transports.end();
                scopedTransports.end();

                // Update Hazards
                hazards.begin();
                scopedHazards.begin();
                for (int i = 0; i < hazards.size; i++) {
                    Hazard h = hazards.get(i);
                    if ((!(h instanceof Gravitating) && rescopeBounds.overlaps(new Rectangle(h.getLeft(), h.getBottom(), h.getWidth(), h.getHeight()))) ||
                    (h instanceof Gravitating && rescopeBounds.contains(new Rectangle(h.getLeft(), h.getBottom() - (h.getHeight() / 2), h.getWidth(), h.getHeight())))) {
                        if (!updateHazard(delta, h)) {
                            spawnPowerup(h);
                            hazards.removeIndex(i);
                            removedHazards.append(";").append(h.getId()); // ';' delimiter prevents conflict with higher level parse (for str containing all level removal lists)
                            unscopeEntity(scopedHazards, h);
                        } else scopeEntity(scopedHazards, h);
                    } else unscopeEntity(scopedHazards, h);
                }
                hazards.end();
                scopedHazards.end();

                // Update Grounds
                grounds.sort(new Comparator<Entity>() {
                    @Override
                    public int compare(Entity o1, Entity o2) {
                        if (o1.getBottom() > o2.getBottom()) return 1;
                        else if (o1.getBottom() < o2.getBottom()) return -1;
                        return 0;
                    }
                });

                grounds.begin();
                scopedGrounds.begin();
                for (int i = 0; i < grounds.size; i++) {
                    Ground g = grounds.get(i);
                    if ((!(g instanceof Pliable)
                            || !(((Pliable) g).isBeingCarried())
                            || !(((Pliable) g).getMovingGround() instanceof Pliable)
                            || !((Pliable) ((Pliable) g).getMovingGround()).isBeingCarried())) {
                        if ((!(g instanceof Pliable) && rescopeBounds.overlaps(new Rectangle(g.getLeft(), g.getBottom(), g.getWidth(), g.getHeight())))
                        || (g instanceof Gravitating && rescopeBounds.contains(new Rectangle(g.getLeft(), g.getBottom() - (g.getHeight() / 2), g.getWidth(), g.getHeight())))) {
                            if (!updateGround(delta, g)) {
                                if (!(g instanceof Destructible)) {
                                    grounds.removeIndex(i);
                                    unscopeEntity(scopedGrounds, g);
                                }
                            } else scopeEntity(scopedGrounds, g);
                        } else unscopeEntity(scopedGrounds, g);
                    }
                }
                grounds.end();
                scopedGrounds.end();
                

                // Update Impacts
                impacts.begin();
                scopedImpacts.begin();
                for (int index = 0; index < impacts.size; index++) {
                    Impact i = impacts.get(index);
                    if (rescopeBounds.overlaps(new Rectangle(i.getLeft(), i.getBottom(), i.getWidth(), i.getHeight()))) {
                        if (i.isFinished()) {
                            impacts.removeIndex(index);
                            unscopeEntity(scopedImpacts, i);
                        } else scopeEntity(scopedImpacts, i);
                    } else unscopeEntity(scopedImpacts, i);
                }
                impacts.end();
                scopedImpacts.end();
                

                // Update Powerups
                powerups.begin();
                scopedPowerups.begin();
                for (int i = 0; i < powerups.size; i++) {
                    Powerup p = powerups.get(i);
                    if (!updatePowerup(delta, p)) {
                        powerups.removeIndex(i);
                        unscopeEntity(scopedPowerups, p);
                    } else scopeEntity(scopedPowerups, p);
                }
                powerups.end();
                scopedPowerups.end();

            } else {
                scopedTransports.begin();
                for (int i = 0; i < scopedTransports.size; i++) {
                    Transport t = scopedTransports.get(i);
                    if (!updateTransport(delta, t, i)) {
                        unscopeEntity(scopedTransports, i);
                        transports.removeValue(t, true);
                    }
                }
                scopedTransports.end();

                scopedHazards.begin();
                // Update Hazards
                for (int i = 0; i < scopedHazards.size; i++) {
                    Hazard h = scopedHazards.get(i);
                    if (!updateHazard(delta, h)) {
                        spawnPowerup(h);
                        unscopeEntity(scopedHazards, i);
                        removedHazards.append(";").append(h.getId()); // ';' delimiter prevents conflict with higher level parse (for str containing all level removal lists)
                        hazards.removeValue(h, true);
                    }
                }
                scopedHazards.end();

                scopedGrounds.begin();
                for (int i = 0; i < scopedGrounds.size; i++) {
                    Ground g = scopedGrounds.get(i);
                    if ((!(g instanceof Pliable)
                            || !(((Pliable) g).isBeingCarried())
                            || !(((Pliable) g).getMovingGround() instanceof Pliable)
                            || !((Pliable) ((Pliable) g).getMovingGround()).isBeingCarried())) {
                        if (!updateGround(delta, g)) {
                            if (!(g instanceof Destructible)) {
                                unscopeEntity(scopedGrounds, i);
                                grounds.removeValue(g, true);
                            }
                        }
                    }
                }
                scopedGrounds.end();

                scopedImpacts.begin();
                // Update Impacts
                for (int index = 0; index < scopedImpacts.size; index++) {
                    Impact i = scopedImpacts.get(index);
                    if (i.isFinished()) {
                        unscopeEntity(scopedImpacts, index);
                        impacts.removeValue(i, true);
                    }
                }
                scopedImpacts.end();
                

                scopedPowerups.begin();
                // Update Powerups
                for (int i = 0; i < scopedPowerups.size; i++) {
                    Powerup p = scopedPowerups.get(i);
                    if (!updatePowerup(delta, p)) {
                        unscopeEntity(scopedPowerups, i);
                        powerups.removeValue(p, true);
                    }
                }
                scopedPowerups.end();
                
            }
            avatar.updatePosition(delta);
            applyCollision(avatar);
            avatar.update(delta);
            Blade.getInstance().update(delta);
            if (avatar.getClimbStatus() && avatar.getClimbStartTime() == 0) entitiesUpdated = true;

            // Update Grounds
            scopedGrounds.begin();
             
            for (int i = 0; i < scopedGrounds.size; i++) {
                Ground g = scopedGrounds.get(i);
                if (g instanceof Destructible ||
                        (g instanceof Pliable
                                && ((((Pliable) g).isBeingCarried())
                                || (((Pliable) g).isAtopMovingGround()
                                && ((Pliable) g).getMovingGround() instanceof Pliable
                                && ((Pliable) ((Pliable) g).getMovingGround()).isBeingCarried())))) {
                   if (!updateGround(delta, g)) {
                        unscopeEntity(scopedGrounds, i);
                        grounds.removeValue(g, true);
                    }
                }
            }
            scopedGrounds.end();
            
        }
        scopedEntities.end();
        if (entitiesUpdated) rescopeEntities();
    }

    private boolean updateGround(float delta, Ground ground) {

        if (ground instanceof Energized && ((Energized) ground).getDispatchStatus()) {
            Energized energy = (Energized) ground;
            Enums.Orientation orientation = energy.getOrientation();
            Vector2 offset = new Vector2();
            if (energy instanceof Cannoroll) {
                offset.set(energy.getWidth(), energy.getHeight());
            }
            if (orientation == Enums.Orientation.X) {
                offset.add(Constants.X_CANNON_CENTER);
                Vector2 ammoPositionLeft = new Vector2(energy.getPosition().x - offset.x, energy.getPosition().y);
                Vector2 ammoPositionRight = new Vector2(energy.getPosition().x + offset.x, energy.getPosition().y);
                if (Avatar.getInstance().getPosition().x < (ammoPositionLeft.x - offset.x)) {
                    spawnProjectile(ammoPositionLeft, Enums.Direction.LEFT, orientation, energy.getIntensity(), getType(), ground);
                } else if (Avatar.getInstance().getPosition().x > (ammoPositionRight.x + (energy.getWidth() / 2))) {
                    spawnProjectile(ammoPositionRight, Enums.Direction.RIGHT, orientation, energy.getIntensity(), getType(), ground);
                }
            } else if (orientation == Enums.Orientation.Y) {
                offset.add(Constants.Y_CANNON_CENTER);
                Vector2 ammoPositionTop = new Vector2(energy.getPosition().x, energy.getPosition().y + offset.y);
                Vector2 ammoPositionBottom = new Vector2(energy.getPosition().x, energy.getPosition().y - offset.y);
                if (energy instanceof Cannon) {
                    if (Avatar.getInstance().getPosition().y < (ammoPositionBottom.y - offset.y)) {
                        spawnProjectile(ammoPositionBottom, Enums.Direction.DOWN, orientation, energy.getIntensity(), getType(), ground);
                    } else if (Avatar.getInstance().getPosition().y > (ammoPositionTop.y + (energy.getHeight() / 2))) {
                        spawnProjectile(ammoPositionTop, Enums.Direction.UP, orientation, energy.getIntensity(), getType(), ground);
                    }
                } else {
                    spawnProjectile(ammoPositionTop, Enums.Direction.UP, orientation, energy.getIntensity(), getType(), ground);
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
                        && !trip.getConvertBounds().equals(Rectangle.tmp) // where tmp has bounds of (0,0,0,0)
                        && !(trip.getConvertBounds().overlaps(new Rectangle(chaseCam.getCamera().position.x - chaseCam.getViewport().getWorldWidth() / 4, chaseCam.getCamera().position.y - chaseCam.getViewport().getWorldHeight() / 4, chaseCam.getViewport().getWorldWidth() / 2, chaseCam.getViewport().getWorldHeight() / 2)))) { // halving dimensions heightens camera sensitivity

                    if (chaseCam.getConvertStartTime() == 0) entitiesUpdated = true;
                    chaseCam.setState(Enums.ChaseCamState.CONVERT);
                    chaseCam.setConvertBounds(trip.getConvertBounds());
                    trip.addCamAdjustment();
                }
                for (Ground g : grounds) {
                    if (g instanceof Convertible && (g != trip || g instanceof Triptread)) {
                        if (Helpers.betweenFourValues(g.getPosition(), trip.getConvertBounds().x, trip.getConvertBounds().x + trip.getConvertBounds().width, trip.getConvertBounds().y, trip.getConvertBounds().y + trip.getConvertBounds().height)) {
                            ((Convertible) g).convert();
                       }
                    }
                }
                for (Hazard h : hazards) {
                    if (h instanceof Convertible && (h != trip)) {
                        if (Helpers.betweenFourValues(h.getPosition(), trip.getConvertBounds().x, trip.getConvertBounds().x + trip.getConvertBounds().width, trip.getConvertBounds().y, trip.getConvertBounds().y + trip.getConvertBounds().height)) {
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
                        entitiesUpdated = true;
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
                    Brick b = new Brick(ground.getPosition().x, ground.getPosition().y, 5, 5, ((Destructible) ground).getType());
                    grounds.add(b);
                    scopeEntity(scopedGrounds, b);
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
        if (active && ground instanceof Impermeable) {
            applyCollision((Impermeable) ground);
        }
        return active;
    }

    private boolean updateHazard(float delta, Hazard hazard) {
        boolean active = true;
        if (hazard instanceof Boss) {
            Boss b = (Boss) hazard;
            b.updatePosition(delta);
            applyCollision((Impermeable) hazard);
            if (b.isMiniBoss()) b.setBattleState(true);
            if (b.getRoomBounds().overlaps(avatar.getCollisionBounds())) {
                if (!b.isBattling() || b.getHealth() < 1) {
                    b.setTalkState(true);
                }
            }

            if (b.getDispatchStatus()) {
                if (b.getLookStartTime() != 0) {
                    if (b.getDirectionY() == Direction.UP) {
                        spawnProjectile(new Vector2(b.getPosition().x + Helpers.speedToVelocity(Constants.AVATAR_Y_CANNON_OFFSET.x, b.getDirectionX(), Enums.Orientation.X), b.getPosition().y + Constants.AVATAR_Y_CANNON_OFFSET.y + 5), b.getDirectionY(), Enums.Orientation.Y, b.getShotIntensity(), b.getEnergy(), b);
                    } else {
                        spawnProjectile(new Vector2(b.getPosition().x + Helpers.speedToVelocity(Constants.AVATAR_Y_CANNON_OFFSET.x - 3, b.getDirectionX(), Enums.Orientation.X), b.getPosition().y - Constants.AVATAR_Y_CANNON_OFFSET.y - 8), b.getDirectionY(), Enums.Orientation.Y, b.getShotIntensity(), b.getEnergy(), b);
                    }
                } else {
                    spawnProjectile(new Vector2(b.getPosition().x + Helpers.speedToVelocity(Constants.AVATAR_X_CANNON_OFFSET.x, b.getDirectionX(), Enums.Orientation.X), b.getPosition().y + Constants.AVATAR_X_CANNON_OFFSET.y), b.getDirectionX(), Enums.Orientation.X, b.getShotIntensity(), b.getEnergy(), b);
                }
                b.resetChargeIntensity();
            }
            if (b.getTouchedHazard() != null) {
                Vector2 intersectionPoint = new Vector2();
                Hazardous touchedHazard = b.getTouchedHazard();
                intersectionPoint.x = Math.max(b.getLeft(), touchedHazard.getLeft());
                intersectionPoint.y = Math.max(b.getBottom(), touchedHazard.getBottom());
                spawnImpact(intersectionPoint, touchedHazard.getType());
            }
        } else if (hazard instanceof Swoopa) {
            ((Swoopa) hazard).setActive(true);
        }

        if (hazard instanceof Destructible) {
            Destructible destructible = (Destructible) hazard;
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
                if (destructible instanceof Armoroll || destructible instanceof Bladeroll || (destructible instanceof Boss && ((Boss) destructible).isMiniBoss())) {
                    Trippable trip = (Trippable) destructible;
                    trip.convert();
                    if (trip.tripped()) {
                        if (hintsEnabled
                                && !trip.maxAdjustmentsReached()
                                && !trip.getConvertBounds().equals(Rectangle.tmp) // where tmp has bounds of (0,0,0,0)
                                && !(trip.getConvertBounds().overlaps(new Rectangle(chaseCam.getCamera().position.x - chaseCam.getViewport().getWorldWidth() / 4, chaseCam.getCamera().position.y - chaseCam.getViewport().getWorldHeight() / 4, chaseCam.getViewport().getWorldWidth() / 2, chaseCam.getViewport().getWorldHeight() / 2)))) { // halving dimensions heightens camera sensitivity

                            if (chaseCam.getConvertStartTime() == 0) entitiesUpdated = true;
                            chaseCam.setState(Enums.ChaseCamState.CONVERT);
                            chaseCam.setConvertBounds(trip.getConvertBounds());
                            trip.addCamAdjustment();
                        }
                        for (Ground g : scopedGrounds) {
                            if (g instanceof Convertible && (g != trip || g instanceof Triptread)) {
                                if (Helpers.betweenFourValues(g.getPosition(), trip.getConvertBounds().x, trip.getConvertBounds().x + trip.getConvertBounds().width, trip.getConvertBounds().y, trip.getConvertBounds().y + trip.getConvertBounds().height)) {
                                    ((Convertible) g).convert();
                                }
                            }
                        }
                        for (Hazard h : scopedHazards) {
                            if (h instanceof Convertible && (h != trip)) {
                                if (Helpers.betweenFourValues(h.getPosition(), trip.getConvertBounds().x, trip.getConvertBounds().x + trip.getConvertBounds().width, trip.getConvertBounds().y, trip.getConvertBounds().y + trip.getConvertBounds().height)) {
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

                spawnProjectile(ammoPositionLeft, Enums.Direction.LEFT, Enums.Orientation.X, Enums.ShotIntensity.BLAST, destructible.getType(), hazard);
                spawnProjectile(ammoPositionRight, Enums.Direction.RIGHT, Enums.Orientation.X, Enums.ShotIntensity.BLAST, destructible.getType(), hazard);
                spawnProjectile(ammoPositionBottom, Enums.Direction.DOWN, Enums.Orientation.Y, Enums.ShotIntensity.BLAST, destructible.getType(), hazard);
                spawnProjectile(ammoPositionTop, Enums.Direction.UP, Enums.Orientation.Y, Enums.ShotIntensity.BLAST, destructible.getType(), hazard);
            }
        } else if (hazard instanceof Projectile) {
            Projectile projectile = (Projectile) hazard;
            for (Hazard h : scopedHazards) {
                if (h instanceof Strikeable) {
                    if (!projectile.equals(h) && projectile.isActive() && Helpers.overlapsPhysicalObject(projectile, h)) {
                        if (h instanceof Destructible) {
                            Destructible destructible = (Destructible) h;
                            if (!(destructible instanceof Zoomba)
                                    || !((projectile.getOrientation() == Enums.Orientation.X && Helpers.betweenTwoValues(projectile.getPosition().y, destructible.getBottom() + 5, destructible.getTop() - 5))
                                    || (projectile.getOrientation() == Enums.Orientation.Y && Helpers.betweenTwoValues(projectile.getPosition().x, destructible.getLeft() + 5, destructible.getRight() - 5)))) {
                                if (!(h instanceof Armored || h instanceof Boss)) {
                                    Helpers.applyDamage(destructible, projectile);
                                } else AssetManager.getInstance().getSoundAssets().hitGround.play();
                                score += projectile.getHitScore();
                            } else {
                                ((Zoomba) destructible).convert();
                                if (avatar.getTouchedGround() != null && avatar.getTouchedGround().equals(destructible)) {
                                    avatar.setPosition(new Vector2(destructible.getPosition().x, destructible.getTop() + Constants.AVATAR_EYE_HEIGHT));
                                }
                            }
                        }
                        if (!(h instanceof Suspension && ((Convertible) h).isConverted())) {
                            this.spawnImpact(projectile.getPosition(), projectile.getType());
                            projectile.deactivate();
                        }
                    }
                }
            }

            for (Ground g : scopedGrounds) {
                if (g instanceof Strikeable) {
                    if (Helpers.overlapsPhysicalObject(projectile, g)) {
                        if (projectile.getSource() instanceof Avatar) {
                            assetManager.getSoundAssets().hitGround.play();
                        }
                        if (projectile.isActive() &&
                                (g.isDense() // collides with all sides of dense ground
                                        || Helpers.overlapsBetweenTwoSides(projectile.getPosition().y, projectile.getHeight() / 2, g.getTop() - 3, g.getTop()))) { // collides only with top of non-dense ground
                            if (!projectile.getPosition().equals(Vector2.Zero)) {
                                this.spawnImpact(projectile.getPosition(), projectile.getType());
                            }
                            projectile.deactivate();
                        }
                        Strikeable strikeable = (Strikeable) g;
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
                        }
                        if (strikeable instanceof Destructible) {
                            Helpers.applyDamage((Destructible) g, projectile);
                        } else if (strikeable instanceof Gate && projectile.getDirection() == Direction.RIGHT) { // prevents from re-unlocking after crossing gate boundary (always left to right)
                            ((Gate) strikeable).deactivate();
                        }
                    }
                }
            }
            projectile.update(delta);
            active = projectile.isActive();
        }
        if (hazard instanceof Nonstatic) {
            ((Nonstatic) hazard).update(delta);
        }
        if (active && hazard instanceof Impermeable) {
            applyCollision((Impermeable) hazard);
        }
        return active;
    }

    private boolean updatePowerup(float delta, Powerup powerup) {
        if (Helpers.overlapsPhysicalObject(avatar, powerup)
                || (avatar.getBladeState() != Enums.BladeState.RETRACTED && Helpers.overlapsPhysicalObject(Blade.getInstance(), powerup))) {
            avatar.touchPowerup(powerup);
            powerup.deactivate();
            if (powerup.getType() == Enums.PowerupType.LIFE) score += Constants.POWERUP_SCORE;
        } else {
            powerup.update(delta);
        }
        return powerup.isActive();
    }

    private boolean updateTransport(float delta, Transport transport, int transportIndex) {
       boolean active = true;
        if (avatar.getPosition().dst(transport.getPosition()) < transport.getWidth() / 2 && inputControls.upButtonPressed && inputControls.jumpButtonJustPressed) {
            if (transport instanceof Portal) {
                if (((Portal) transport).isGoal()) {
                    goalReached = true;
                    return false;
                }
                for (int j = 0; j <= transportIndex; j++) {
                    if (j < scopedTransports.size && !(scopedTransports.get(j) instanceof Portal)) {
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
                allRemovals.set(level, removedHazards.toString());
                SaveData.setLevelRestore(allRestores.toString().replace("[", "").replace("]", ""));
                SaveData.setLevelTimes(allTimes.toString().replace("[", "").replace("]", ""));
                SaveData.setLevelScores(allScores.toString().replace("[", "").replace("]", ""));
                SaveData.setLevelRemovals(allRemovals.toString().replace("[", "").replace("]", ""));
                SaveData.setSuit(avatar.getEnergy() + ";" + (avatar.isSupercharged() ? 1 : 0));
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

    void restoreRemovals(String removals) {
        removedHazards = new StringBuilder(removals);
        List<String> levelRemovalStrings = Arrays.asList(removedHazards.toString().split(";"));
        List<Integer> levelRemovals = new ArrayList<Integer>();
        for (String removalStr : levelRemovalStrings) {
            levelRemovals.add(Integer.parseInt(removalStr));
        }
        for (Integer removal : levelRemovals) {
            for (Hazard h : hazards) {
                if (removal == h.getId()) hazards.removeValue(h, true);
            }
        }
    }

    private void clearEntities() {
        scopedEntities.clear();
        grounds.clear();
        hazards.clear();
        powerups.clear();
        transports.clear();
        impacts.clear();
        scopedGrounds.clear();
        scopedHazards.clear();
        scopedImpacts.clear();
        scopedTransports.clear();
        scopedPowerups.clear();
    }

    protected void dispose() {
        clearEntities();
        scopedEntities = null;
        grounds = null;
        hazards = null;
        powerups = null;
        transports = null;
        impacts = null;
        scopedGrounds = null;
        scopedTransports = null;
        scopedPowerups = null;
        scopedImpacts = null;
        scopedHazards = null;
    }

    // level state handling
    void begin() {
        scopedEntities.addAll(grounds);
        scopedEntities.addAll(hazards);
        scopedEntities.addAll(powerups);
        scopedEntities.addAll(transports);
        scopedEntities.addAll(impacts);
        chaseCam.setState(Enums.ChaseCamState.FOLLOWING);
        backdrop = new Backdrop(assetManager.getBackgroundAssets().getBackground(theme));
        startThemeMusic();
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
        goalReached = false;
    }

    protected void end() {
        if (music.isPlaying()) music.stop();
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
        rescopeBounds = null;
    }

    protected void pause() {
        if (music.isPlaying()) music.pause();
        timer.suspend();
        paused = true;
    }

    void unpause() {
        if (musicEnabled) {
            music.play();
        }
        paused = false;
        timer.resume();
    }

    void reset() {
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

    boolean restarted() {
        if (avatar.getFallLimit() != -10000) {
            if (avatar.getPosition().y < avatar.getFallLimit() || avatar.getHealth() < 1) {
                // TODO: Discontinue level, play sound, and prevent restart until earlier of
                //  time lapse or button press
                avatar.setHealth(0);
                avatar.setLives(avatar.getLives() - 1);
                if (chaseCam.getState() == Enums.ChaseCamState.BOSS) {
                    chaseCam.setState(Enums.ChaseCamState.FOLLOWING);
                }
                boss.setBattleState(false);
                boss.setPosition(boss.getSpawnPosition());
                SaveData.setTotalTime(SaveData.getTotalTime() + time);
                List<String> allRestores = Arrays.asList(SaveData.getLevelRestore().split(", "));
                List<String> allRemovals = Arrays.asList(SaveData.getLevelRemovals().split(", "));
                List<String> allTimes = Arrays.asList(SaveData.getLevelTimes().split(", "));
                List<String> allScores = Arrays.asList(SaveData.getLevelScores().split(", "));
                int index = Arrays.asList(Enums.Theme.values()).indexOf(theme);
                boolean levelRestored = !allRestores.get(index).equals("0:0");
                if (!levelRestored) {
                    allRestores.set(index, "0:0");
                    allRemovals.set(index, "-1");
                    allTimes.set(index, "0");
                    allScores.set(index, "0");
                    SaveData.setLevelRestore(allRestores.toString().replace("[", "").replace("]", ""));
                    SaveData.setLevelRemovals(allRemovals.toString().replace("[", "").replace("]", ""));
                    SaveData.setLevelTimes(allTimes.toString().replace("[", "").replace("]", ""));
                    SaveData.setLevelScores(allScores.toString().replace("[", "").replace("]", ""));
                }
                setTime(Long.parseLong(allTimes.get(index)));
                setScore(Integer.parseInt(allScores.get(index)));
                clearEntities();
                try {
                    LevelLoader.load(theme);
                    restoreRemovals(allRemovals.get(index));
                    if (levelRestored) {
                        String[] coordinateStr = allRestores.get(index).split(":");
                        Vector2 position = new Vector2(Float.valueOf(coordinateStr[0]), Float.valueOf(coordinateStr[1]));
                        avatar.setSpawnPosition(position);
                    }
                } catch (GdxRuntimeException ex) {
                    Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
                    Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE, ex);
                }
                return true;
            }
        }
        return false;
    }

    boolean failed() {
        if (restarted()) {
            if (avatar.getLives() < 0) {
                return true;
            }
            startThemeMusic();
            avatar.respawn();
        }
        return false;
    }

    boolean completed() { return goalReached || (chaseCam.getState() == Enums.ChaseCamState.BOSS && boss.getHealth() < 1 && boss.isBattling()); }

    boolean continuing() { return !(completed() || failed()); }

    boolean paused() {
        return paused;
    }

    private void spawnImpact(Vector2 position, Enums.Energy type) {
        Impact i = new Impact(position, type);
        scopeEntity(scopedImpacts, i);
    }

    private void spawnProjectile(Vector2 position, Direction direction, Enums.Orientation orientation, Enums.ShotIntensity shotIntensity, Enums.Energy energy, Entity source) {
        Projectile projectile = new Projectile(position, direction, orientation, shotIntensity, energy, source);
        scopeEntity(scopedHazards, projectile);
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

    final long getUnsavedTime() { return timer.getMillis() - TimeUtils.nanosToMillis(savedTime); }
    final int getUnsavedScore() { return score - savedScore; }
    protected final void setBoss(Boss boss) { this.boss = boss; }
    final DelayedRemovalArray<Transport> getTransports() { return transports; }
    protected Enums.Theme getTheme() { return theme; }
    final boolean hasLoadEx() { return loadEx; }
    // Setters

    final void addGround(Ground ground) { grounds.add(ground); }
    final void addHazard(Hazard hazard) { hazards.add(hazard); }
    final void addPowerup(Powerup powerup) { powerups.add(powerup); }
    void setTime(long time) { this.time = time; }
    void setScore(int score) {this.score = score; }
    void setTheme(Enums.Theme selectedLevel) {
        theme = selectedLevel;

        int level = Arrays.asList(Enums.Theme.values()).indexOf(this.theme);
        savedTime = Long.parseLong(SaveData.getLevelScores().split(", ")[level]);
    }
    protected int getIndex() { return Arrays.asList(Enums.Theme.values()).indexOf(this.theme); }
    void toggleMusic() { musicEnabled = !SaveData.hasMusic(); SaveData.setMusic(musicEnabled); }
    void toggleStyle() {
        switch (musicStyle) {
            case CLASSIC: musicStyle = Enums.MusicStyle.AMBIENT; break;
            case AMBIENT: musicStyle = Enums.MusicStyle.CHIPTUNE; break;
            default: musicStyle = Enums.MusicStyle.CLASSIC;
        }
        SaveData.setStyle(musicStyle.name());
        startThemeMusic();
    }
    void playExitMusic() {
        if (music != null && music.isPlaying()) music.stop();
        music = assetManager.getMusicAssets().decision;
        if (music.isLooping()) music.setLooping(false);
        if (musicEnabled) music.play();
    }
    void playCompleteMusic() {
        if (music != null && music.isPlaying()) music.stop();
        music = assetManager.getMusicAssets().complete;
        if (music.isLooping()) music.setLooping(false);
        if (musicEnabled) music.play();
    }
    Enums.MusicStyle getStyle() { return musicStyle; }
    void startThemeMusic() {
        if (music != null && music.isPlaying()) music.stop();
        music = assetManager.getMusicAssets().getThemeMusic(theme, musicStyle);
        if (!music.isLooping()) music.setLooping(true);
        if (musicEnabled) music.play();
    }
    void toggleHints() { hintsEnabled = !hintsEnabled; }
    boolean isMusicEnabled() { return musicEnabled; }
    boolean areHintsEnabled() { return hintsEnabled; }
    final void setLoadEx(boolean state) { loadEx = state; }
    final Backdrop getBackdrop() { return backdrop; }

    DelayedRemovalArray<Entity> getScopedEntities() { return scopedEntities; }
    DelayedRemovalArray<Ground> getScopedGrounds() { return scopedGrounds; }
    DelayedRemovalArray<Hazard> getScopedHazards() { return scopedHazards; }
    DelayedRemovalArray<Powerup> getScopedPowerups() { return scopedPowerups; }
    DelayedRemovalArray<Transport> getScopedTransports() { return scopedTransports; }
    DelayedRemovalArray<Impact> getScopedImpacts() { return scopedImpacts; }

    private  <T extends Entity> void scopeEntity(DelayedRemovalArray<T> entities, T entity) {
        entitiesUpdated = true;
        if (!entities.contains(entity, true)) {
            entitiesUpdated = true;
            entities.add(entity);
        }
    }

    private  <T extends Entity> void unscopeEntity(DelayedRemovalArray<T> entities, T entity) {
        if (entities.removeValue(entity, true)) entitiesUpdated = true;
    }

    private  <T extends Entity> void unscopeEntity(DelayedRemovalArray<T> entities, int index) {
        entitiesUpdated = true;
        entities.removeIndex(index);
    }
}

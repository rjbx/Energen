package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entity.Ammo;
import com.udacity.gamedev.gigagal.entity.Boss;
import com.udacity.gamedev.gigagal.entity.Box;
import com.udacity.gamedev.gigagal.entity.Chamber;
import com.udacity.gamedev.gigagal.entity.Chargeable;
import com.udacity.gamedev.gigagal.entity.Convertible;
import com.udacity.gamedev.gigagal.entity.Destructible;
import com.udacity.gamedev.gigagal.entity.Gate;
import com.udacity.gamedev.gigagal.entity.Ground;
import com.udacity.gamedev.gigagal.entity.Hazard;
import com.udacity.gamedev.gigagal.entity.Impact;
import com.udacity.gamedev.gigagal.entity.Nonstatic;
import com.udacity.gamedev.gigagal.entity.Portal;
import com.udacity.gamedev.gigagal.entity.GigaGal;
import com.udacity.gamedev.gigagal.entity.Powerup;
import com.udacity.gamedev.gigagal.entity.Reboundable;
import com.udacity.gamedev.gigagal.entity.Strikeable;
import com.udacity.gamedev.gigagal.entity.Swoopa;
import com.udacity.gamedev.gigagal.entity.Teleport;
import com.udacity.gamedev.gigagal.entity.Transport;
import com.udacity.gamedev.gigagal.entity.Tripchamber;
import com.udacity.gamedev.gigagal.entity.Tripknob;
import com.udacity.gamedev.gigagal.entity.Trippable;
import com.udacity.gamedev.gigagal.entity.Triptread;
import com.udacity.gamedev.gigagal.entity.Vines;
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
    private Viewport viewport;
    private boolean loadEx;
    private Backdrop backdrop;
    private DelayedRemovalArray<Transport> transports;
    private DelayedRemovalArray<Hazard> hazards;
    private DelayedRemovalArray<Ground> grounds;
    private DelayedRemovalArray<Impact> impacts;
    private DelayedRemovalArray<Powerup> powerups;
    private DelayedRemovalArray<Ammo> projectiles;
    private DelayedRemovalArray<Object> objects;
    private Enums.Material levelWeapon;
    private Enums.Theme level;
    private Music music;
    private Boss boss;
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
        LevelScreen.getInstance().create();
        Timer.getInstance().create();
        objects = new DelayedRemovalArray<Object>();
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
        if (continuing() && !paused() && ChaseCam.getInstance().getState() != Enums.ChaseCamState.CONVERT) {
            updateAssets(delta);
            GigaGal.getInstance().update(delta);
        }
    }

    protected void render(SpriteBatch batch, Viewport viewport) {

        backdrop.render(batch, viewport, new Vector2(ChaseCam.getInstance().camera.position.x, ChaseCam.getInstance().camera.position.y), Constants.BACKGROUND_CENTER, 1);

        for (Ground ground : grounds) {
            if (!ground.isDense()) {
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
            if (ground.isDense()) {
                ground.render(batch, viewport);
            }
        }

        GigaGal.getInstance().render(batch, viewport);

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
    private void updateAssets(float delta) {

        time = Timer.getInstance().getNanos();
        
        // Update Restore Points
        transports.begin();
        for (int i = 0; i < transports.size; i++) {
            Transport transport = transports.get(i);
            if (GigaGal.getInstance().getPosition().dst(transports.get(i).getPosition()) < transport.getWidth() / 2 && InputControls.getInstance().upButtonPressed && InputControls.getInstance().jumpButtonJustPressed) {
                if (transport instanceof Portal) {
                    int portalIndex = i;
                    for (int j = 0; j <= i; j++) {
                        if (!(transports.get(j) instanceof Portal)) {
                            portalIndex++;
                        }
                    }
                    Assets.getInstance().getSoundAssets().life.play();
                    int level = Arrays.asList(Enums.Theme.values()).indexOf(this.level);
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
                    Assets.getInstance().getSoundAssets().warp.play();
                    GigaGal.getInstance().getPosition().set(transport.getDestination());
                }
            }
        }
        transports.end();

        // Update Grounds
        grounds.begin();
        for (int i = 0; i < grounds.size ; i++) {
            Ground ground = grounds.get(i);
            if (ground instanceof Trippable) {
                Trippable trip = (Trippable) ground;
                if (trip instanceof Triptread) {
                    if (Helpers.overlapsPhysicalObject(GigaGal.getInstance(), trip) && GigaGal.getInstance().getAction() == Enums.Action.DASHING && !GigaGal.getInstance().getDashStatus()) {
                        trip.setState(!trip.isActive());
                    }
                }
                if (trip.tripped()) {
                    if (hintsEnabled
                    && !trip.maxAdjustmentsReached()
                    && !trip.getBounds().equals(Rectangle.tmp) // where tmp has bounds of (0,0,0,0)
                    && !(trip.getBounds().overlaps(new Rectangle(ChaseCam.getInstance().camera.position.x - viewport.getWorldWidth() / 2, ChaseCam.getInstance().camera.position.y - viewport.getWorldHeight() / 2, viewport.getWorldWidth(), viewport.getWorldHeight())))) {
                        ChaseCam.getInstance().setState(Enums.ChaseCamState.CONVERT);
                        ChaseCam.getInstance().setConvertBounds(trip.getBounds());
                        trip.addCamAdjustment();
                    }
                    for (Ground g : grounds) {
                        if (g instanceof Convertible && g != trip) {
                            if (Helpers.betweenFourValues(g.getPosition(), trip.getBounds().x, trip.getBounds().x + trip.getBounds().width, trip.getBounds().y, trip.getBounds().y + trip.getBounds().height)) {
                                ((Convertible) g).convert();
                            }
                        }
                    }
                }
            }
            if (ground instanceof Nonstatic) {
                ((Nonstatic) ground).update(delta);
            }
            if (ground instanceof Destructible) {
                if (((Destructible) ground).getHealth() < 1) {
                    if (ground instanceof Box) {
                        Assets.getInstance().getSoundAssets().breakGround.play();
                        grounds.removeIndex(i);
                    }
                }
            }
            if (ground instanceof Chargeable) {
                Chargeable chargeable = (Chargeable) ground;
                if (chargeable instanceof Tripchamber) {
                    if (GigaGal.getInstance().getShotIntensity() == Enums.ShotIntensity.BLAST && !chargeable.isCharged()) {
                        chargeable.charge();
                    }
                } else {
                    if (GigaGal.getInstance().getChargeTimeSeconds() != Helpers.secondsSince(0) && GigaGal.getInstance().getDirectionX() == Direction.RIGHT) {
                        if (!chargeable.isActive() && chargeable instanceof Chamber) {
                            chargeable.setState(true);
                        } else if (GigaGal.getInstance().getChargeTimeSeconds() > 1) {
                            chargeable.setChargeTime(GigaGal.getInstance().getChargeTimeSeconds());
                        }
                    } else {
                        chargeable.setChargeTime(0);
                    }
                }
                if (ground instanceof Chamber) {
                    Chamber chamber = (Chamber) ground;
                    if (!chamber.isActive() && chamber.isCharged()) {
                        Assets.getInstance().getSoundAssets().upgrade.play();
                        dispenseUpgrade(chamber.getUpgrade());
                        chamber.uncharge();
                    }
                }
            }
            if (ground instanceof Strikeable) {
                projectiles.begin();
                for (int j = 0; j < projectiles.size; j++) {
                    Ammo ammo = projectiles.get(j);
                    if (Helpers.overlapsPhysicalObject(ammo, ground)) {
                        if (ammo.isFromGigagal()) {
                            Assets.getInstance().getSoundAssets().hitGround.play();
                        }
                        if (ammo.isActive() &&
                                (ground.isDense() // collides with all sides of dense ground
                                || Helpers.betweenTwoValues(ammo.getPosition().y, ground.getTop() - 7, ground.getTop()))) { // collides only with top of non-dense ground
                            if (!ammo.getPosition().equals(Vector2.Zero)) {
                                this.spawnImpact(ammo.getPosition(), ammo.getType());
                            }
                            ammo.deactivate();
                            projectiles.removeIndex(j);
                        }
                        Strikeable strikeable = (Strikeable) ground;
                        if (strikeable instanceof Tripknob) {
                            Tripknob tripknob = (Tripknob) strikeable;
                            tripknob.resetStartTime();
                            tripknob.setState(!tripknob.isActive());
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
            if (ground instanceof Reboundable) {
                Reboundable reboundable = (Reboundable) ground;
                if (Helpers.overlapsPhysicalObject(GigaGal.getInstance(), ground)) {
                    reboundable.setState(true);
                } else if (reboundable.getState()) {
                    reboundable.resetStartTime();
                    reboundable.setState(false);
                }
            }
        }
        grounds.end();

        // Update Hazards
        hazards.begin();
        for (int i = 0; i < hazards.size; i++) {
            if (hazards.get(i) instanceof Destructible) {
                Destructible destructible = (Destructible) hazards.get(i);
                destructible.update(delta);
                projectiles.begin();
                for (int j = 0; j < projectiles.size; j++) {
                    Ammo ammo = projectiles.get(j);
                    if (ammo.isActive() && ammo.getPosition().dst(destructible.getPosition()) < (destructible.getShotRadius() + ammo.getRadius())) {
                        this.spawnImpact(ammo.getPosition(), ammo.getType());
                        Helpers.applyDamage(destructible, ammo);
                        score += ammo.getHitScore();
                        ammo.deactivate();
                    }
                }
                projectiles.end();
                if (destructible.getHealth() < 1) {
                    if (destructible instanceof Swoopa) {
                        ((Swoopa) destructible).dispose();
                    }
                    spawnImpact(destructible.getPosition(), destructible.getType());
                    hazards.removeIndex(i);
                    removedHazards += (";" + i); // ';' delimeter prevents conflict with higher level parse (for str containing all level removal lists)
                    score += (destructible.getKillScore() * Constants.DIFFICULTY_MULTIPLIER[SaveData.getDifficulty()]);
                }
            } else if (hazards.get(i) instanceof Ammo) {
                Ammo ammo = (Ammo) hazards.get(i);
                ammo.update(delta);
                if (!ammo.isActive()) {
                    hazards.removeValue(ammo, false);
                    projectiles.removeValue(ammo, false);
                }
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
            if (Helpers.overlapsPhysicalObject(GigaGal.getInstance(), powerups.get(i))) {
                powerups.removeIndex(i);
            }
        }
        powerups.end();
    }

    private void dispenseUpgrade(Enums.Upgrade upgrade) {
        switch (upgrade) {
            case AMMO:
                SaveData.setAmmoMultiplier(.9f);
                GigaGal.getInstance().updateMultipliers();
                break;
            case HEALTH:
                SaveData.setHealthMultiplier(.8f);
                GigaGal.getInstance().updateMultipliers();
                break;
            case TURBO:
                SaveData.setTurboMultiplier(.7f);
                GigaGal.getInstance().updateMultipliers();
                break;
            case CANNON:
                String savedWeapons = SaveData.getWeapons();
                if (!savedWeapons.contains(Enums.Material.HYBRID.name())) {
                    GigaGal.getInstance().addWeapon(Enums.Material.HYBRID);
                    SaveData.setWeapons(Enums.Material.HYBRID.name() + ", " + savedWeapons);
                }
                break;
            default:
        }
        GigaGal.getInstance().setHealth(Constants.MAX_HEALTH);
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

    protected void dispose() {
        getEntities().clear();
        getGrounds().clear();
        getHazards().clear();
        getPowerups().clear();
        getImpacts().clear();
        hazards.clear();
        grounds.clear();
        impacts.clear();
        powerups.clear();
        transports.clear();
    }


    // level state handling

    protected void begin() {
        objects.addAll(grounds);
        objects.addAll(hazards);
        objects.addAll(powerups);
        objects.addAll(impacts);
        ChaseCam.getInstance().setState(Enums.ChaseCamState.FOLLOWING);

        backdrop = new Backdrop(Assets.getInstance().getBackgroundAssets().getBackground(level));
        music = Assets.getInstance().getMusicAssets().getThemeMusic(level);
        music.setLooping(true);
        if (musicEnabled) {
            music.play();
        }
        levelWeapon = Enums.Material.NATIVE;
        for (Enums.Material weapon : Arrays.asList(Enums.Material.values())) {
            if (weapon.theme().equals(level)) {
                levelWeapon = weapon;
            }
        }

        GigaGal.getInstance().setLives(3);
        GigaGal.getInstance().respawn();

        // set level attributes
        viewport = LevelScreen.getInstance().getViewport();

        Timer.getInstance().reset().start(time);

        savedTime = time;
        savedScore = score;
    }

    protected void end() {
        music.stop();
        Timer.getInstance().suspend();
        if (completed()) {
            SaveData.setTotalScore(SaveData.getTotalScore() + score);
            SaveData.setTotalTime(SaveData.getTotalTime() + Timer.getInstance().getSeconds());
            String savedWeapons = SaveData.getWeapons();
            if (!savedWeapons.contains(levelWeapon.name())) {
                GigaGal.getInstance().addWeapon(levelWeapon);
                SaveData.setWeapons(levelWeapon.name() + ", " + savedWeapons);
            }
        }
        dispose();
    }

    protected void pause() {
        music.pause();
        Timer.getInstance().suspend();
        paused = true;
    }

    protected void unpause() {
        if (musicEnabled) {
            music.play();
        }
        paused = false;
        Timer.getInstance().resume();
    }

    protected void reset() {
        int level = Arrays.asList(Enums.Theme.values()).indexOf(this.level);
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
        if (GigaGal.getInstance().getKillPlane() != -10000) {
            if (GigaGal.getInstance().getPosition().y < GigaGal.getInstance().getKillPlane() || GigaGal.getInstance().getHealth() < 1) {
                GigaGal.getInstance().setHealth(0);
                GigaGal.getInstance().setLives(GigaGal.getInstance().getLives() - 1);
                return true;
            }
        }
        return false;
    }

    protected boolean failed() {
        if (restarted()) {
            if (GigaGal.getInstance().getLives() < 0) {
                return true;
            }
            if (musicEnabled) {
                music.stop();
                music.play();
            }
            GigaGal.getInstance().respawn();
        }
        return false;
    }

    protected boolean completed() { return ChaseCam.getInstance().getState() == Enums.ChaseCamState.BOSS && boss.getHealth() < 1; }

    protected boolean continuing() { return !(completed() || failed()); }

    protected boolean paused() {
        return paused;
    }

    public void spawnAmmo(Vector2 position, Direction direction, Enums.Orientation orientation, Enums.ShotIntensity shotIntensity, Enums.Material weapon, boolean targetsEnemies) {
        Ammo ammo = new Ammo(this, position, direction, orientation, shotIntensity, weapon, targetsEnemies);
        hazards.add(ammo);
        projectiles.add(ammo);
    }

    public void spawnImpact(Vector2 position, Enums.Material type) {
        impacts.add(new Impact(position, type));
    }

    // Getters
    protected final void addEntity(Object object) { objects.add(object); }
    protected final void addGround(Ground ground) { grounds.add(ground); }
    protected final void addHazard(Hazard hazard) { hazards.add(hazard); }
    protected final void addPowerup(Powerup powerup) { powerups.add(powerup); }

    // to return cloned elements; state changes set from this class
    public final Array<Object> getEntities() {
        Array<Object> clonedObjects = new Array<Object>();
        for (Object object : objects) {
            clonedObjects.add(object);
        }
        return clonedObjects;
    }

    public final Array<Ground> getGrounds() {
        Array<Ground> clonedGrounds = new Array<Ground>();
        for (Ground ground : grounds) {
            clonedGrounds.add(ground.clone());
        }
        return clonedGrounds;
    }
    
    public final Array<Hazard> getHazards() {
        Array<Hazard> clonedHazards = new Array<Hazard>();
        for (Hazard hazard : hazards) {
            clonedHazards.add(hazard);
        }
        return clonedHazards;
    }

    public final Array<Powerup> getPowerups() {
        Array<Powerup> clonedPowerups = new Array<Powerup>();
        for (Powerup powerup : powerups) {
            clonedPowerups.add(powerup);
        }
        return clonedPowerups;
    }
   
    public final Array<Impact> getImpacts() {
        Array<Impact> clonedImpacts = new Array<Impact>();
        for (Impact impact : impacts) {
            clonedImpacts.add(impact);
        }
        return clonedImpacts;
    }

    public final long getUnsavedTime() { return time - savedTime; }
    public final int getUnsavedScore() { return score - savedScore; }
    public final long getTime() { return time; }
    public final int getScore() { return score; }
    public final void setBoss(Boss boss) { this.boss = boss; }
    public final Boss getBoss() { return boss; }
    public final Viewport getViewport() { return viewport; }
    public final DelayedRemovalArray<Transport> getTransports() { return transports; }
    public final GigaGal getGigaGal() { return GigaGal.getInstance(); }
    public final Enums.Material getType() { return levelWeapon; }
    protected Enums.Theme getLevel() { return level; }
    public final boolean hasLoadEx() { return loadEx; }

    // Setters
    protected void setTime(long time) { this.time = time; }
    protected void setScore(int score) {this.score = score; }
    protected void setLevel(Enums.Theme selectedLevel) { level = selectedLevel; }
    protected void toggleMusic() { musicEnabled = !musicEnabled; }
    protected void toggleHints() { hintsEnabled = !hintsEnabled; }
    protected final void setLoadEx(boolean state) { loadEx = state; }
}
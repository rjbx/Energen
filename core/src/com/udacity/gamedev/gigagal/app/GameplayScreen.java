package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.overlays.Menu;
import com.udacity.gamedev.gigagal.overlays.Message;
import com.udacity.gamedev.gigagal.overlays.OnscreenControls;
import com.udacity.gamedev.gigagal.overlays.Cursor;
import com.udacity.gamedev.gigagal.overlays.IndicatorHud;
import com.udacity.gamedev.gigagal.overlays.GaugeHud;
import com.udacity.gamedev.gigagal.util.ChaseCam;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.LevelLoader;
import com.udacity.gamedev.gigagal.util.Timer;
import com.udacity.gamedev.gigagal.util.Helpers;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GameplayScreen extends ScreenAdapter {

    // fields
    public static final String TAG = GameplayScreen.class.getName();
    private static final GameplayScreen INSTANCE = new GameplayScreen();
    private static InputControls inputControls;
    private static OnscreenControls onscreenControls;
    private GigaGalGame game;
    private Preferences prefs;
    private GaugeHud gaugeHud;
    private IndicatorHud indicatorHud;
    private Message victoryOverlay;
    private Message defeatOverlay;
    private Menu mainMenu;
    private Menu optionsMenu;
    private Message errorMessage;
    private Message debugMessage;
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private BitmapFont font;
    private ExtendViewport viewport;
    private long levelEndOverlayStartTime;
    private Level level;
    private ChaseCam chaseCam;
    private Array<Enums.LevelName> completedLevels;
    private Enums.LevelName levelName;
    private GigaGal gigaGal;
    private int totalScore;
    private Timer totalTime;
    private boolean paused;
    private boolean viewingOptions;
    private boolean levelEnded;
    private long pauseTime;
    private float pauseDuration;

    // cannot be subclassed
    private GameplayScreen() {}

    // static factory method
    public static GameplayScreen getInstance() { return INSTANCE; }

    public void create(Enums.LevelName levelName) {
        this.game = GigaGalGame.getInstance();
        this.levelName = levelName;
        prefs = game.getPreferences();
        completedLevels = new Array<Enums.LevelName>();
        totalTime = new Timer();
        paused = false;
        viewingOptions = false;
        levelEnded = false;
        pauseTime = 0;
        pauseDuration = 0;
    }

    @Override
    public void show() {
        batch = new SpriteBatch(); // shared by all overlays instantiated from this class
        renderer = new ShapeRenderer(); // shared by all overlays instantiated from this class
        renderer.setAutoShapeType(true);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE)); // shared by all overlays instantiated from this class
        font.getData().setScale(.4f); // shared by all overlays instantiated from this class
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE); // shared by all overlays instantiated from this class
        Cursor.getInstance().init();
        Menu.getInstance().create();
        errorMessage = new Message();
        errorMessage.setMessage(Constants.LEVEL_KEY_MESSAGE);
        debugMessage = new Message();
        debugMessage.setMessage(Constants.DEBUG_MODE_MESSAGE);
        victoryOverlay = new Message();defeatOverlay = new Message();
        defeatOverlay.setMessage(Constants.DEFEAT_MESSAGE);
        inputControls = InputControls.getInstance();
        onscreenControls = OnscreenControls.getInstance();
        chaseCam = ChaseCam.getInstance();

        // : Use Gdx.input.setInputProcessor() to send touch events to inputControls
        Gdx.input.setInputProcessor(inputControls);
        startNewLevel();
    }

    private static void setMainMenu() {
        Cursor.getInstance().setRange(73, 43);
        Cursor.getInstance().setOrientation(Enums.Orientation.Y);
        Cursor.getInstance().resetPosition();
        String[] optionStrings = {"RESUME", "EXIT", "OPTIONS"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
    }

    private static void setOptionsMenu() {
        Cursor.getInstance().setRange(73, 28);
        Cursor.getInstance().resetPosition();
        Cursor.getInstance().update();
        String[] optionStrings = {"BACK", "DEBUG CAM", "TOUCH PAD", "QUIT"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);


//        gaugeHud.getViewport().update(width, height, true);
//        indicatorHud.getViewport().update(width, height, true);
//
//        defeatOverlay.getViewport().update(width, height, true);
//        mainMenu.getViewport().update(width, height, true);
//        mainMenu.getCursor().getViewport().update(width, height, true);
//        optionsMenu.getViewport().update(width, height, true);
//        optionsMenu.getCursor().getViewport().update(width, height, true);
//        errorMessage.getViewport().update(width, height, true);
        level.getViewport().update(width, height, true);
        chaseCam.camera = level.getViewport().getCamera();
        onscreenControls.getViewport().update(width, height, true);
        onscreenControls.recalculateButtonPositions();
        gigaGal.setInputControls(inputControls);
        chaseCam.setInputControls(inputControls);
    }

    @Override
    public void render(float delta) {
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!chaseCam.getFollowing()) {
            level.render(batch);
            chaseCam.update(delta);
            debugMessage.render(batch, font, viewport, new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() /2 ));
            viewingOptions = false;
        }

        if (level.gigaGalFailed()) {
            if (gigaGal.getLives() > -1) {
                restartLevel();
            }
        }

        if (!(level.isGameOver() || level.isVictory())) {
            if (paused) {
                renderPause();
            } else if (inputControls.pauseButtonJustPressed) {
                level.getLevelTime().suspend();
                totalTime.suspend();
                paused = true;
                pauseTime = TimeUtils.nanoTime();
                pauseDuration = gigaGal.getPauseTimeSeconds();
                setMainMenu();
            } else {
                level.update(delta);
                chaseCam.update(delta);
                level.render(batch);
            }
            gaugeHud.render(renderer, viewport);
            indicatorHud.render(batch, font, viewport);
            onscreenControls.render(batch, viewport);
        } else {
            renderLevelEnd();
            return;
        }

        if (level.getLoadEx()) {
            font.getData().setScale(.25f);
            errorMessage.render(batch, font, viewport, new Vector2(viewport.getWorldWidth() / 2, Constants.HUD_MARGIN - 5));
            font.getData().setScale(.4f);
        }
        inputControls.update();
    }

    private void renderPause() {
        if (!viewingOptions) {
            mainMenu.render(batch, font, viewport, Cursor.getInstance());
            if (inputControls.jumpButtonJustPressed && gigaGal.getAction() == Enums.Action.STANDING) {
                gigaGal.toggleWeapon(Enums.Direction.DOWN); // enables gigagal to toggleWeapon weapon during pause without enabling other gigagal features
            }
            if (inputControls.shootButtonJustPressed) {
                if (Cursor.getInstance().getPosition() == 73 && chaseCam.getFollowing()) {
                    unpause();
                } else if (Cursor.getInstance().getPosition() == 58) {
                    unpause();
                    totalTime.suspend();
                    LevelSelectScreen.getInstance().create();
                    game.setScreen(LevelSelectScreen.getInstance());
                    this.dispose();
                    return;
                } else if (Cursor.getInstance().getPosition() == 43) {
                    setOptionsMenu();
                    viewingOptions = true;
                }
            } else if (inputControls.pauseButtonJustPressed) {
                unpause();
            }
        } else {
            optionsMenu.render(batch, font, viewport, Cursor.getInstance());
            if (inputControls.shootButtonJustPressed) {
                if (Cursor.getInstance().getPosition() == 73) {
                    viewingOptions = false;
                    setMainMenu();
                } else if (Cursor.getInstance().getPosition() == 58) {
                    if (!chaseCam.getFollowing()) {
                        optionsMenu.isSingleOption(false);
                        chaseCam.setFollowing(true);
                    } else {
                        optionsMenu.isSingleOption(true);
                        chaseCam.setFollowing(false);
                    }
                } else if (Cursor.getInstance().getPosition() == 43) {
                    onscreenControls.onMobile = Helpers.toggleBoolean(onscreenControls.onMobile);
                    prefs.putBoolean("Mobile", onscreenControls.onMobile);
                } else if (Cursor.getInstance().getPosition() == 28) {
                    game.dispose();
                    game.create();
                    return;
                }
            } else if (inputControls.pauseButtonJustPressed && chaseCam.getFollowing()) {
                viewingOptions = false;
            }
        }
    }

    private void renderLevelEnd() {
        if (level.isGameOver()) {
            if (levelEndOverlayStartTime == 0) {
                level.getLevelTime().suspend();
                totalTime.suspend();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
            }
            font.getData().setScale(1);
            defeatOverlay.render(batch, font, viewport, new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f));
            font.getData().setScale(.4f);
            if (Helpers.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION / 2) {
                levelEndOverlayStartTime = 0;
                LevelSelectScreen.getInstance().create();
                game.setScreen(LevelSelectScreen.getInstance());
                this.dispose();
                return;
            }
        } else if (level.isVictory()) {
            if (levelEndOverlayStartTime == 0) {
                level.getLevelTime().suspend();
                totalTime.suspend();
                totalScore += level.getLevelScore();
                game.getPreferences().putInteger("Score", totalScore);
                game.getPreferences().putLong("Time", totalTime.getNanoTime());
                game.getPreferences().flush();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
                victoryOverlay.setMessage(Constants.VICTORY_MESSAGE + "\n\n\n" + "GAME TOTAL\n" + "Time: " + Helpers.stopWatchToString(getTotalTime()) + "\nScore: " + getTotalScore() + "\n\nLEVEL TOTAL\n" + "Time: " + Helpers.stopWatchToString(getLevel().getLevelTime()) + "\n" + "Score: " + getLevel().getLevelScore());
            }
            victoryOverlay.render(batch, font, viewport, new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .9f));
            if (Helpers.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                levelEndOverlayStartTime = 0;
                levelComplete();
            }
        }
    }

    public void readLevelFile() throws IOException, ParseException, GdxRuntimeException {
        level = LevelLoader.load("levels/" + levelName + ".dt");
    }

    private void startNewLevel() {
//      level = Level.debugLevel();
//      String levelName = Constants.LEVELS[levelNumber];

        // get prefs
        String savedWeapons = game.getPreferences().getString("Weapons", Enums.WeaponType.NATIVE.name());
        if (!savedWeapons.equals(Enums.WeaponType.NATIVE.name())) {
            List<String> savedWeaponsList = Arrays.asList(savedWeapons.split(", "));
            for (String weaponString : savedWeaponsList) {
                if (!completedLevels.contains(Enums.WeaponType.valueOf(weaponString).levelName(), false)) {
                    completedLevels.add(Enums.WeaponType.valueOf(weaponString).levelName());
                }
            }
        }
        totalScore = game.getPreferences().getInteger("Score", totalScore);
        totalTime.start(game.getPreferences().getLong("Time", totalTime.getNanoTime()));
        totalTime.suspend();
        game.getPreferences().flush();

        // set level attributes
        level.setLevelName(levelName);
        level.setDifficulty(prefs.getInteger("Difficulty", 0));
        gaugeHud = new GaugeHud(level);
        indicatorHud = new IndicatorHud(level);
        this.gigaGal = level.getGigaGal();
        for (Enums.LevelName completedLevelName : completedLevels) {
            for (Enums.WeaponType weapon : Arrays.asList(Enums.WeaponType.values())) {
                if (completedLevelName.equals(weapon.levelName())) {
                    if (!gigaGal.getWeaponList().contains(weapon)) {
                        gigaGal.addWeapon(weapon);
                    }
                }
            }
        }
        String weaponListString = gigaGal.getWeaponList().toString();
        weaponListString = weaponListString.substring(1, weaponListString.length() - 1);
        game.getPreferences().putString("Weapons", weaponListString);
        chaseCam.camera = level.getViewport().getCamera();
        chaseCam.target = gigaGal;
        totalTime.resume();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void restartLevel() {
        gigaGal.respawn();
    }

    private void levelComplete() {
        if (!completedLevels.contains(levelName, false)) {
            completedLevels.add(levelName);
        }
        LevelSelectScreen.getInstance().create();
        game.setScreen(LevelSelectScreen.getInstance());
        this.dispose();
        return;
    }

    private void unpause() {
        gigaGal.setPauseTimeSeconds(Helpers.secondsSincePause(pauseTime) + pauseDuration);
        level.getLevelTime().resume();
        totalTime.resume();
        paused = false;
    }

    @Override
    public void dispose() {
        totalTime.stop();
        completedLevels.clear();
        inputControls.clear();
        victoryOverlay.dispose();
        defeatOverlay.dispose();
        mainMenu.dispose();
        optionsMenu.dispose();
        indicatorHud.dispose();
        errorMessage.dispose();
        gaugeHud.dispose();
        batch.dispose();
        level.dispose();
        totalTime = null;
        completedLevels = null;
        inputControls = null;
        totalTime = null;
        victoryOverlay = null;
        defeatOverlay = null;
        mainMenu = null;
        optionsMenu = null;
        indicatorHud = null;
        errorMessage = null;
        gaugeHud = null;
        batch = null;
        level = null;
        this.hide();
        super.dispose();
        System.gc();
    }

    public Level getLevel() { return level; }
    public int getTotalScore() { return totalScore; }
    public Timer getTotalTime() { return totalTime; }
    public ChaseCam getChaseCam() { return chaseCam; }
}
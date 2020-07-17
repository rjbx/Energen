package com.github.rjbx.energen.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.github.rjbx.energen.entity.Avatar;
import com.github.rjbx.energen.overlay.TouchInterface;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.InputControls;
import com.github.rjbx.energen.overlay.Menu;
import com.github.rjbx.energen.overlay.Cursor;
import com.github.rjbx.energen.overlay.IndicatorHud;
import com.github.rjbx.energen.overlay.GaugeHud;
import com.github.rjbx.energen.util.ChaseCam;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;
import com.github.rjbx.energen.util.StaticCam;

import java.util.Arrays;

// immutable package-private singleton
final class LevelScreen extends ScreenAdapter {

    // fields
    public static final String TAG = LevelScreen.class.getName();
    private static final LevelScreen INSTANCE = new LevelScreen();
    private ScreenManager screenManager;
    private OverworldScreen overworldScreen;
    private static LevelUpdater levelUpdater;
    private SpriteBatch batch;
    private ChaseCam chaseCam;
    private static ExtendViewport chaseViewport;
    private StaticCam staticCam;
    private static ExtendViewport staticViewport;
    private ShapeRenderer renderer;
    private BitmapFont font;
    private GaugeHud gaugeHud;
    private IndicatorHud indicatorHud;
    private TouchInterface touchInterface;
    private InputControls inputControls;
    private static Avatar avatar;
    private static Cursor cursor;
    private static Menu menu;
    private static Enums.MenuType menuType;
    private long levelEndOverlayStartTime;

    // cannot be subclassed
    private LevelScreen() {}

    // static factory method
    protected static LevelScreen getInstance() { return INSTANCE; }

    @Override
    public void show() {
        screenManager = ScreenManager.getInstance();
        
        batch = screenManager.getBatch();

        chaseCam = ChaseCam.getInstance();
        chaseViewport = chaseCam.getViewport();

        staticCam = StaticCam.getInstance();
        staticViewport = staticCam.getViewport();

        renderer = new ShapeRenderer(); // shared by all overlays instantiated from this class
        renderer.setAutoShapeType(true);
        
        font = AssetManager.getInstance().getFontAssets().message;
        font.setUseIntegerPositions(false);
        
        indicatorHud = IndicatorHud.getInstance();
        gaugeHud = GaugeHud.getInstance();
        touchInterface = TouchInterface.getInstance();

        inputControls = InputControls.getInstance();
        Gdx.input.setInputProcessor(inputControls); // sends touch events to inputControls

        avatar = Avatar.getInstance();
        cursor = Cursor.getInstance();

        menu = Menu.getInstance();
        levelUpdater = LevelUpdater.getInstance();
        
        overworldScreen = OverworldScreen.getInstance();
    }

    @Override
    public void resize(int width, int height) {
        levelUpdater.begin();
        chaseViewport.update(width, height, true);
        staticViewport.update(width, height, true);
        indicatorHud.create();
        gaugeHud.create();
        touchInterface.getViewport().update(width, height, true);
        touchInterface.recalculateButtonPositions();
        avatar.setInputControls(inputControls);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        levelUpdater.getBackdrop().render(batch, staticViewport, new Vector2(staticViewport.getCamera().position.x, staticViewport.getCamera().position.y), Constants.BACKGROUND_CENTER, 1);

        if (levelUpdater.continuing()) {
            if (!levelUpdater.paused()) {
                levelUpdater.update(delta);
                chaseCam.update(batch, delta);
                levelUpdater.render(batch, chaseViewport); // also rendered when viewingDebug; see pause()
                indicatorHud.render(batch, staticViewport, font); // renders after level which sets indicators to foreground
                if (inputControls.pauseButtonJustPressed) {
                    levelUpdater.pause();
                    setMainMenu();
                }
            } else {
                showPauseMenu(delta);
            }
            gaugeHud.render(renderer, staticViewport, avatar);
        } else {
            showExitOverlay();
        }
        if (levelUpdater.hasLoadEx()) {
            font.getData().setScale(.25f);
            Helpers.drawBitmapFont(batch, chaseViewport, font, Constants.LEVEL_KEY_MESSAGE, chaseViewport.getCamera().position.x, chaseViewport.getCamera().position.y - chaseViewport.getWorldHeight() / 2.5f, Align.center);
            font.getData().setScale(.4f);
        }
        inputControls.update();
        touchInterface.render(batch);
    }

    private void showPauseMenu(float delta) {
        switch (menuType) {
            case MAIN:
                if (inputControls.jumpButtonJustPressed) {
                    avatar.toggleEnergy(Enums.Direction.DOWN); // enables avatar to toggleEnergy energy during pause without enabling other avatar features
                    menu.setPromptString(Align.left, "GAUGE\n" + Constants.HUD_FUEL_LABEL + (int) avatar.getAmmo() + "\n" + Constants.HUD_HEALTH_LABEL + (int) avatar.getHealth() + "\n" + Constants.HUD_STAMINA_LABEL + (int) avatar.getTurbo() + "\n" + Constants.HUD_ENERGY_LABEL + avatar.getEnergy().toString());
//                    menu.setPromptString(Align.right, "ENERGY\n" + (avatar.getEnergy().name().toLowerCase() + "\n" + SaveData.getEnergies().replace(avatar.getEnergy().name(), "").replace(", ", "\n")).replace("\n\n", "\n").toLowerCase());
                }
                if (inputControls.justSelected()) {
                    if (cursor.getPosition() == staticViewport.getCamera().position.y - 30 && chaseCam.getState() != Enums.ChaseCamState.DEBUG) {
                        levelUpdater.unpause();
                    } else if (cursor.getPosition() == staticViewport.getCamera().position.y - 45) {
                        overworldScreen.setMainMenu();
                        screenManager.setScreen(overworldScreen);
                        levelUpdater.unpause();
                        levelUpdater.end();
                        return;
                    } else if (cursor.getPosition() == staticViewport.getCamera().position.y - 60) {
                        setOptionsMenu();
                    }
                } else if (inputControls.pauseButtonJustPressed) {
                    levelUpdater.unpause();
                }
                break;
            case OPTIONS:
                if (inputControls.justSelected()) {
                    if (cursor.getPosition() == staticViewport.getCamera().position.y + 45) {
                        setMainMenu();
                    } else if (cursor.getPosition() == staticViewport.getCamera().position.y + 30) {
                        setResetMenu();
                    } else if (cursor.getPosition() == staticViewport.getCamera().position.y + 15) {
                        if (chaseCam.getState() != Enums.ChaseCamState.DEBUG) {
                            chaseCam.setState(Enums.ChaseCamState.DEBUG);
                            setDebugMenu();
                        }
                    } else if (cursor.getPosition() == staticViewport.getCamera().position.y) {
                        SaveData.setTouchscreen(!SaveData.hasTouchscreen());
                    } else if (cursor.getPosition() == staticViewport.getCamera().position.y - 15) {
                        levelUpdater.toggleMusic();
                        menu.clearStrings();
                        if (levelUpdater.isMusicEnabled()) menu.setOptionString(4, "MUSIC ON");
                        else menu.setOptionString(4, "MUSIC OFF");
                    } else if (cursor.getPosition() == staticViewport.getCamera().position.y - 30) {
                        levelUpdater.toggleStyle();
                        menu.clearStrings();
                        menu.setOptionString(5, "STYLE " + levelUpdater.getStyle().name());
                    } else if (cursor.getPosition() == staticViewport.getCamera().position.y - 45) {
                        levelUpdater.toggleHints();
                    } else if (cursor.getPosition() == staticViewport.getCamera().position.y - 60) {
                        levelUpdater.unpause();
                        levelUpdater.end();
                        screenManager.create();
                        return;
                    }
                } else if (inputControls.pauseButtonJustPressed) {
                    setMainMenu();
                }
                break;
            case RESET:
                if (inputControls.justSelected()) {
                    if (cursor.getPosition() == staticViewport.getCamera().position.x + 50) {

                        levelUpdater.unpause();
                        levelUpdater.end();
                        levelUpdater.reset();
                        overworldScreen.loadLevel(overworldScreen.getSelection());
                    }
                    setOptionsMenu();
                }
                break;
            case DEBUG:
                boolean inBossArea = LevelUpdater.getInstance().getBoss().isBattling();
                levelUpdater.render(batch, chaseViewport);
                chaseCam.update(batch, delta);
                if (inputControls.justSelected()) {
                    if (inBossArea) {
                        chaseCam.setState(Enums.ChaseCamState.BOSS);
                    } else {
                        chaseCam.setState(Enums.ChaseCamState.FOLLOWING);
                    }
                    setOptionsMenu();
                }
                break;
        }
        menu.render(batch, font, staticViewport, cursor); // renders after debug level which sets menu to foreground
    }

    private void showExitOverlay() {
        String endMessage = "";
        float yDivisor = 0;
        if (levelUpdater.failed()) {
            endMessage = Constants.FAIL_MESSAGE;
            yDivisor = 12;
            font.getData().setScale(.4f);
            if (levelEndOverlayStartTime == 0) {
                levelUpdater.end();
                levelUpdater.playExitMusic();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
            }
            if (Helpers.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION || inputControls.shootButtonJustPressed) {
                levelEndOverlayStartTime = 0;
                overworldScreen.setMainMenu();
                screenManager.setScreen(overworldScreen);
                return;
            }
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        } else if (levelUpdater.completed()) {
            endMessage = Constants.VICTORY_MESSAGE + "\n\n\n" + "GAME TOTAL\n" + "TIME: " + Helpers.millisToString(TimeUtils.nanosToMillis(SaveData.getTotalTime())) + "\nSCORE: " + SaveData.getTotalScore() + "\n\nLEVEL TOTAL\n" + "TIME: " + Helpers.millisToString(levelUpdater.getUnsavedTime()) + "\n" + "SCORE " + levelUpdater.getScore();
            yDivisor = 3;
            if (levelEndOverlayStartTime == 0) {
                levelUpdater.end();
                levelUpdater.playCompleteMusic();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
            }
            if (Helpers.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION || inputControls.shootButtonJustPressed) {
                levelEndOverlayStartTime = 0;
                overworldScreen.setMainMenu();
                screenManager.setScreen(overworldScreen);
                return;
            }
            Color color = Color.SKY;
            Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
        Helpers.drawBitmapFont(batch, staticViewport, font, endMessage, staticViewport.getCamera().position.x, staticViewport.getCamera().position.y + staticViewport.getWorldHeight() / yDivisor, Align.center);
    }

    public final ExtendViewport getViewport() { return chaseViewport; }


    private static void setMainMenu() {
        cursor.setRange(staticViewport.getCamera().position.y - 30, staticViewport.getCamera().position.y - 60);
        cursor.setOrientation(Enums.Orientation.Y);
        cursor.resetPosition();
        String[] optionStrings = {"RESUME", "EXIT", "OPTIONS"};
        menu.setOptionStrings(Arrays.asList(optionStrings));
        menu.setPromptString(Align.left, "GAUGE\n" + Constants.HUD_FUEL_LABEL + (int) avatar.getAmmo() + "\n" + Constants.HUD_HEALTH_LABEL + (int) avatar.getHealth() + "\n" + Constants.HUD_STAMINA_LABEL + (int) avatar.getTurbo() + "\n" + Constants.HUD_ENERGY_LABEL + avatar.getEnergy().toString());
        menu.setPromptString(Align.right, "STATS\n" + "Game Time: " + Helpers.millisToString((TimeUtils.nanosToMillis(SaveData.getTotalTime()) + levelUpdater.getUnsavedTime())) + "\nLevel Time: " + Helpers.millisToString(TimeUtils.nanosToMillis(levelUpdater.getTime())) + "\nGame Score: " + (SaveData.getTotalScore() + levelUpdater.getUnsavedScore()) + "\nLevel Score: " + levelUpdater.getScore());
//        menu.setPromptString(Align.right, "ENERGY\n" + (avatar.getEnergy().name().toLowerCase() + "\n" + SaveData.getEnergies().replace(avatar.getEnergy().name(), "").replace(", ", "\n")).replace("\n\n", "\n").toLowerCase());
        menu.TextAlignment(Align.center);
        menuType = Enums.MenuType.MAIN;
    }

    private static void setOptionsMenu() {
        cursor.setRange(staticViewport.getCamera().position.y + 45, staticViewport.getCamera().position.y - 60);
        cursor.setOrientation(Enums.Orientation.Y);
        cursor.resetPosition();
        menu.isSingleOption(false);
        menu.clearStrings();
        String[] optionStrings = {"BACK", "RESET LEVEL", "DEBUG CAM", "TOUCH PAD", "MUSIC " + (levelUpdater.isMusicEnabled() ? "ON" : "OFF"), "STYLE " + levelUpdater.getStyle(), "HINTS", "QUIT"};
        menu.setOptionStrings(Arrays.asList(optionStrings));
        menu.TextAlignment(Align.center);
        menuType = Enums.MenuType.OPTIONS;
    }

    private static void setResetMenu() {
        cursor.setRange(staticViewport.getCamera().position.x - 50, staticViewport.getCamera().position.x + 50);
        cursor.setOrientation(Enums.Orientation.X);
        cursor.resetPosition();
        String[] optionStrings = {"NO", "YES"};
        menu.setOptionStrings(Arrays.asList(optionStrings));
        menu.TextAlignment(Align.center);
        menu.setPromptString(Align.center, "Are you sure you want to erase \n all progress on this level?");
        menuType = Enums.MenuType.RESET;
    }

    private static void setDebugMenu() {
        cursor.setRange(staticViewport.getCamera().position.y, staticViewport.getCamera().position.y);
        menu.isSingleOption(true);
        menu.setPromptString(Align.center, Constants.DEBUG_MODE_MESSAGE);
        menu.TextAlignment(Align.center);
        menuType = Enums.MenuType.DEBUG;
    }

    @Override
    public void dispose() {
//        totalTime.stop();
//        completedLevels.clearStrings();
//        inputControls.clearStrings();
//        victoryMessage.dispose();
//        defeatMessage.dispose();
//        indicatorHud.dispose();
//        errorMessage.dispose();
//        gaugeHud.dispose();
//        batch.dispose();
//        level.dispose();
//        totalTime = null;
//        completedLevels = null;
//        inputControls = null;
//        totalTime = null;
//        victoryMessage = null;
//        defeatMessage = null;
//        indicatorHud = null;
//        errorMessage = null;
//        gaugeHud = null;
//        batch = null;
//        level = null;
//        this.hide();
//        super.dispose();
//        System.gc();
    }
}
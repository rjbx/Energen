package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.overlay.TouchInterface;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.InputControls;
import com.udacity.gamedev.gigagal.entity.GigaGal;
import com.udacity.gamedev.gigagal.overlay.Menu;
import com.udacity.gamedev.gigagal.overlay.Cursor;
import com.udacity.gamedev.gigagal.overlay.IndicatorHud;
import com.udacity.gamedev.gigagal.overlay.GaugeHud;
import com.udacity.gamedev.gigagal.util.ChaseCam;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

import java.util.Arrays;

import static com.udacity.gamedev.gigagal.util.Enums.LevelMenu.DEBUG;
import static com.udacity.gamedev.gigagal.util.Enums.LevelMenu.MAIN;
import static com.udacity.gamedev.gigagal.util.Enums.LevelMenu.OPTIONS;
import static com.udacity.gamedev.gigagal.util.Enums.LevelMenu.RESET;

// package-private
class LevelScreen extends ScreenAdapter {

    // fields
    public static final String TAG = LevelScreen.class.getName();
    private static final LevelScreen INSTANCE = new LevelScreen();
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private BitmapFont font;
    private static ExtendViewport viewport;
    private long levelEndOverlayStartTime;
    private static Enums.LevelMenu menu;

    // cannot be subclassed
    private LevelScreen() {}

    // static factory method
    protected static LevelScreen getInstance() { return INSTANCE; }

    protected void create() {}

    @Override
    public void show() {
        batch = new SpriteBatch(); // shared by all overlays instantiated from this class
        renderer = new ShapeRenderer(); // shared by all overlays instantiated from this class
        renderer.setAutoShapeType(true);
        font = Assets.getInstance().getFontAssets().message;
        font.setUseIntegerPositions(false);
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE); // shared by all overlays instantiated from this class

        // : Use Gdx.input.setInputProcessor() to send touch events to inputControls
        Gdx.input.setInputProcessor(InputControls.getInstance());
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        LevelUpdater.getInstance().begin();
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);

        ChaseCam.getInstance().target = GigaGal.getInstance();
        ChaseCam.getInstance().camera = viewport.getCamera();
        ChaseCam.getInstance().setInputControls(InputControls.getInstance());
//        gaugeHud.getViewport().update(width, height, true);
//        indicatorHud.getViewport().update(width, height, true);
//
//        defeatMessage.getViewport().update(width, height, true);
//        mainMenu.getViewport().update(width, height, true);
//        mainMenu.getCursor().getViewport().update(width, height, true);
//        optionsMenu.getViewport().update(width, height, true);
//        optionsMenu.getCursor().getViewport().update(width, height, true);
//        errorMessage.getViewport().update(width, height, true);
        TouchInterface.getInstance().getViewport().update(width, height, true);
        TouchInterface.getInstance().recalculateButtonPositions();
        GigaGal.getInstance().setInputControls(InputControls.getInstance());
    }

    private static void setMainMenu() {
        Cursor.getInstance().setRange(viewport.getCamera().position.y, viewport.getCamera().position.y - 30);
        Cursor.getInstance().setOrientation(Enums.Orientation.Y);
        Cursor.getInstance().resetPosition();
        String[] optionStrings = {"RESUME", "EXIT", "OPTIONS"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
        Menu.getInstance().setPromptString(Align.left, Constants.HUD_AMMO_LABEL + GigaGal.getInstance().getAmmo() + "\n" + Constants.HUD_HEALTH_LABEL + GigaGal.getInstance().getHealth() + "\n" + "Turbo: " + GigaGal.getInstance().getTurbo());
        Menu.getInstance().setPromptString(Align.center, "GAME TOTAL\n" + "Time: " + Helpers.secondsToString(TimeUtils.nanosToMillis(SaveData.getTotalTime() + LevelUpdater.getInstance().getUnsavedTime())) + "\n" + "Score: " + (SaveData.getTotalScore() + LevelUpdater.getInstance().getUnsavedScore()));
        Menu.getInstance().setPromptString(Align.right, (GigaGal.getInstance().getWeapon().name() + "\n" + SaveData.getWeapons().replace(GigaGal.getInstance().getWeapon().name(), "").replace(", ", "\n")).replace("\n\n", "\n"));
        Menu.getInstance().TextAlignment(Align.center);
        menu = MAIN;
    }

    private static void setOptionsMenu() {
        Cursor.getInstance().setRange(viewport.getCamera().position.y + 45, viewport.getCamera().position.y - 45);
        Cursor.getInstance().setOrientation(Enums.Orientation.Y);
        Cursor.getInstance().resetPosition();
        Menu.getInstance().isSingleOption(false);
        Menu.getInstance().clearStrings();
        String[] optionStrings = {"BACK", "RESET LEVEL", "DEBUG CAM", "TOUCH PAD", "MUSIC", "HINTS", "QUIT"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
        Menu.getInstance().TextAlignment(Align.center);
        menu = OPTIONS;
    }

    private static void setResetMenu() {
        Cursor.getInstance().setRange(viewport.getCamera().position.x - 50, viewport.getCamera().position.x + 50);
        Cursor.getInstance().setOrientation(Enums.Orientation.X);
        Cursor.getInstance().resetPosition();
        String[] optionStrings = {"NO", "YES"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
        Menu.getInstance().TextAlignment(Align.center);
        Menu.getInstance().setPromptString(Align.center, "Are you sure you want to erase \n all progress on this level?");
        menu = RESET;
    }

    private static void setDebugMenu() {
        Cursor.getInstance().setRange(viewport.getCamera().position.y, viewport.getCamera().position.y);
        Menu.getInstance().isSingleOption(true);
        Menu.getInstance().setPromptString(Align.center, Constants.DEBUG_MODE_MESSAGE);
        Menu.getInstance().TextAlignment(Align.center);
        menu = DEBUG;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (LevelUpdater.getInstance().continuing()) {

            if (!LevelUpdater.getInstance().paused()) {
                LevelUpdater.getInstance().update(delta);
                ChaseCam.getInstance().update(batch, delta);
                LevelUpdater.getInstance().render(batch, viewport); // also rendered when viewingDebug; see pause()
                IndicatorHud.getInstance().render(batch, font, viewport, LevelUpdater.getInstance()); // renders after level which sets indicators to foreground
                if (InputControls.getInstance().pauseButtonJustPressed) {
                    LevelUpdater.getInstance().pause();
                    setMainMenu();
                }
            } else {
                showPauseMenu(delta);
            }
            GaugeHud.getInstance().render(renderer, viewport, GigaGal.getInstance());
            TouchInterface.getInstance().render(batch);
        } else {
            showExitOverlay();
        }
        if (LevelUpdater.getInstance().hasLoadEx()) {
            font.getData().setScale(.25f);
            Helpers.drawBitmapFont(batch, viewport, font, Constants.LEVEL_KEY_MESSAGE, viewport.getCamera().position.x, viewport.getCamera().position.y - viewport.getWorldHeight() / 2.5f, Align.center);
            font.getData().setScale(.4f);
        }
        InputControls.getInstance().update();
    }

    private void showPauseMenu(float delta) {
        switch (menu) {
            case MAIN:
                if (InputControls.getInstance().jumpButtonJustPressed) {
                    GigaGal.getInstance().toggleWeapon(Enums.Direction.DOWN); // enables gigagal to toggleWeapon weapon during pause without enabling other gigagal features
                    Menu.getInstance().setPromptString(Align.right, (GigaGal.getInstance().getWeapon().name() + "\n" + SaveData.getWeapons().replace(GigaGal.getInstance().getWeapon().name(), "").replace(", ", "\n")).replace("\n\n", "\n"));
                }
                if (InputControls.getInstance().shootButtonJustPressed) {
                    if (Cursor.getInstance().getPosition() == viewport.getCamera().position.y && ChaseCam.getInstance().getState() == Enums.ChaseCamState.FOLLOWING) {
                        LevelUpdater.getInstance().unpause();
                    } else if (Cursor.getInstance().getPosition() == viewport.getCamera().position.y - 15) {
                        OverworldScreen.getInstance().setMainMenu();
                        ScreenManager.getInstance().setScreen(OverworldScreen.getInstance());
                        LevelUpdater.getInstance().unpause();
                        LevelUpdater.getInstance().end();
                        return;
                    } else if (Cursor.getInstance().getPosition() == viewport.getCamera().position.y - 30) {
                        setOptionsMenu();
                    }
                } else if (InputControls.getInstance().pauseButtonJustPressed) {
                    LevelUpdater.getInstance().unpause();
                }
                break;
            case OPTIONS:
                if (InputControls.getInstance().shootButtonJustPressed) {
                    if (Cursor.getInstance().getPosition() == viewport.getCamera().position.y + 45) {
                        setMainMenu();
                    } else if (Cursor.getInstance().getPosition() == viewport.getCamera().position.y + 30) {
                        setResetMenu();
                    } else if (Cursor.getInstance().getPosition() == viewport.getCamera().position.y + 15) {
                        if (ChaseCam.getInstance().getState() != Enums.ChaseCamState.DEBUG) {
                            ChaseCam.getInstance().setState(Enums.ChaseCamState.DEBUG);
                            setDebugMenu();
                        }
                    } else if (Cursor.getInstance().getPosition() == viewport.getCamera().position.y) {
                        SaveData.toggleTouchscreen(!SaveData.hasTouchscreen());
                    } else if (Cursor.getInstance().getPosition() == viewport.getCamera().position.y - 15) {
                        LevelUpdater.getInstance().toggleMusic();
                    } else if (Cursor.getInstance().getPosition() == viewport.getCamera().position.y - 30) {
                        LevelUpdater.getInstance().toggleHints();
                    } else if (Cursor.getInstance().getPosition() == viewport.getCamera().position.y - 45) {
                        LevelUpdater.getInstance().unpause();
                        LevelUpdater.getInstance().end();
                        ScreenManager.getInstance().create();
                        return;
                    }
                } else if (InputControls.getInstance().pauseButtonJustPressed) {
                    setMainMenu();
                }
                break;
            case RESET:
                if (InputControls.getInstance().shootButtonJustPressed) {
                    if (Cursor.getInstance().getPosition() == viewport.getCamera().position.x + 50) {
                        LevelUpdater.getInstance().unpause();
                        LevelUpdater.getInstance().end();
                        LevelUpdater.getInstance().reset();
                        OverworldScreen.getInstance().loadLevel(OverworldScreen.getSelection());
                    } else {
                        setOptionsMenu();
                    }
                }
                break;
            case DEBUG:
                LevelUpdater.getInstance().render(batch, viewport);
                ChaseCam.getInstance().update(batch, delta);
                if (InputControls.getInstance().shootButtonJustPressed) {
                    ChaseCam.getInstance().setState(Enums.ChaseCamState.FOLLOWING);
                    setOptionsMenu();
                }
                break;
        }
        Menu.getInstance().render(batch, font, viewport, Cursor.getInstance()); // renders after debug level which sets menu to foreground
    }

    private void showExitOverlay() {
        String endMessage = "";
        if (LevelUpdater.getInstance().failed()) {
            endMessage = Constants.DEFEAT_MESSAGE;
            font.getData().setScale(.6f);
            if (levelEndOverlayStartTime == 0) {
                LevelUpdater.getInstance().end();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
            }
            if (Helpers.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION / 2) {
                levelEndOverlayStartTime = 0;
                OverworldScreen.getInstance().setMainMenu();
                ScreenManager.getInstance().setScreen(OverworldScreen.getInstance());
                font.getData().setScale(.4f);
                return;
            }
        } else if (LevelUpdater.getInstance().completed()) {
            endMessage = Constants.VICTORY_MESSAGE + "\n\n\n" + "GAME TOTAL\n" + "Time: " + Helpers.secondsToString(SaveData.getTotalTime()) + "\nScore: " + SaveData.getTotalScore() + "\n\nLEVEL TOTAL\n" + "Time: " + Helpers.secondsToString(LevelUpdater.getInstance().getTime()) + "\n" + "Score: " + LevelUpdater.getInstance().getScore();
            if (levelEndOverlayStartTime == 0) {
                LevelUpdater.getInstance().end();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
            }
            if (Helpers.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                levelEndOverlayStartTime = 0;
                OverworldScreen.getInstance().setMainMenu();
                ScreenManager.getInstance().setScreen(OverworldScreen.getInstance());
                return;
            }
        }
        Helpers.drawBitmapFont(batch, viewport, font, endMessage, viewport.getCamera().position.x, viewport.getCamera().position.y + viewport.getWorldHeight() / 3, Align.center);
    }

    protected ExtendViewport getViewport() { return viewport; }

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
package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.overlays.Message;
import com.udacity.gamedev.gigagal.overlays.OnscreenControls;
import com.udacity.gamedev.gigagal.overlays.Cursor;
import com.udacity.gamedev.gigagal.overlays.Backdrop;
import com.udacity.gamedev.gigagal.overlays.Menu;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// immutable
public final class StartScreen extends ScreenAdapter {

    // fields
    public static final String TAG = StartScreen.class.getName();
    private static final StartScreen INSTANCE = new StartScreen();
    private static InputControls inputControls;
    private static OnscreenControls onscreenControls;
    private com.udacity.gamedev.gigagal.app.GigaGalGame game;
    private SpriteBatch batch;
    private ExtendViewport viewport;
    private BitmapFont font;
    private BitmapFont text;
    private BitmapFont title;
    private Backdrop launchBackdrop;
    private Message launchMessage;
    private Preferences prefs;
    private List<String> choices;
    private long launchStartTime;
    private boolean launching;
    private boolean continuing;
    private boolean difficultyOptionsVisible;
    private boolean promptVisible;
    private Vector2 gigagalCenter;

    // cannot be subclassed
    private StartScreen() {}

    // static factory method
    public static StartScreen getInstance() { return INSTANCE; }

    public void create() {
        game = GigaGalGame.getInstance();
        prefs = game.getPreferences();
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        gigagalCenter = new Vector2(Constants.GIGAGAL_STANCE_WIDTH / 2, Constants.GIGAGAL_HEIGHT / 2);
        text = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        text.getData().setScale(0.5f);
        title = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        title.getData().setScale(1);
        title.setColor(Color.SKY);
        choices = new ArrayList<String>();
        launchStartTime = TimeUtils.nanoTime();
        launching = true;
        continuing = (prefs.getLong("Time", 0) != 0);
        choices.add("NO");
        choices.add("YES");
    }

    @Override
    public void show() {
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        difficultyOptionsVisible = false;
        promptVisible = false;
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE)); // shared by all overlays instantiated from this class
        font.getData().setScale(.4f); // shared by all overlays instantiated from this class
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE); // shared by all overlays instantiated from this class
        launchBackdrop = new Backdrop();
        launchMessage = new Message();
        launchMessage.setMessage(Constants.LAUNCH_MESSAGE);
        inputControls = com.udacity.gamedev.gigagal.app.InputControls.getInstance();
        onscreenControls = OnscreenControls.getInstance();
        Gdx.input.setInputProcessor(inputControls);
    }

    public static void setResumeMenu() {
        Cursor.getInstance().setRange(35, 20);
        Cursor.getInstance().setOrientation(Enums.Orientation.Y);
        Cursor.getInstance().resetPosition();
        String[] optionStrings = {"START GAME", "ERASE GAME"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
        Menu.getInstance().setPromptString(" ");
    }

    public static void setEraseMenu() {
        Cursor.getInstance().setRange(50, 150);
        Cursor.getInstance().setOrientation(Enums.Orientation.X);
        Cursor.getInstance().resetPosition();
        String[] optionStrings = {"NO", "YES"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
        Menu.getInstance().setPromptString("Are you sure you want to start \na new game and erase all saved data?");
    }

    public static void setBeginMenu() {
        Cursor.getInstance().setRange(30, 30);
        Menu.getInstance().isSingleOption(true);
        String[] option = {"PRESS START"};
        Menu.getInstance().setOptionStrings(Arrays.asList(option));
    }

    public static void setDifficultyMenu() {
        Cursor.getInstance().setRange(75, 35);
        Cursor.getInstance().setOrientation(Enums.Orientation.Y);
        Cursor.getInstance().resetPosition();
        String[] optionStrings = {"NORMAL", "HARD", "VERY HARD"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
        Menu.getInstance().isSingleOption(false);
    }

    private boolean onMobile() {
        return Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

//        cursor.getViewport().update(width, height, true);
//        onscreenControls.getViewport().update(width, height, true);
//        onscreenControls.recalculateButtonPositions();
//        startMenu.getViewport().update(width, height, true);
//        startMenu.getCursor().getViewport().update(width, height, true);
//        difficultyMenu.getViewport().update(width, height, true);
//        difficultyMenu.getCursor().getViewport().update(width, height, true);
//        eraseMenu.getViewport().update(width, height, true);
//        eraseMenu.getCursor().getViewport().update(width, height, true);
//        launchBackdrop.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!difficultyOptionsVisible) {
            if (!launching) {
                if (!promptVisible) {
                    viewport.apply();
                    batch.setProjectionMatrix(viewport.getCamera().combined);
                    batch.begin();
                    title.draw(batch, "ENERGRAFT", viewport.getWorldWidth() / 2, viewport.getWorldHeight() - Constants.HUD_MARGIN, 0, Align.center, false);

                    final Vector2 gigagalPosition = new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2);
                    Helpers.drawTextureRegion(batch, Assets.getInstance().getGigaGalAssets().fallRight, gigagalPosition, gigagalCenter);

                    batch.end();

                    Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());

                    if (inputControls.shootButtonJustPressed) {
                        if (continuing) {
                            if (Cursor.getInstance().getPosition() == 35) {
                                inputControls.shootButtonJustPressed = false;
                                LevelSelectScreen levelSelectScreen = LevelSelectScreen.getInstance();
                                levelSelectScreen.create();
                                game.setScreen(levelSelectScreen);
                                this.dispose();
                                return;
                            } else if (Cursor.getInstance().getPosition() == 20) {
                                setEraseMenu();
                                promptVisible = true;
                            }
                        } else {
                            difficultyOptionsVisible = true;
                            setDifficultyMenu();
                        }
                    }
                } else {
                    Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());
                    if (inputControls.shootButtonJustPressed) {
                        if (Cursor.getInstance().getPosition() == (150)) {
                            prefs.clear();
                            prefs.flush();
                            game.dispose();
                            game.create();
                            setBeginMenu();
                        } else {
                            setResumeMenu();
                        }
                        promptVisible = false;
                    }
                }
            } else {
                launchBackdrop.render(batch, viewport, Assets.getInstance().getOverlayAssets().logo,
                        new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .625f),
                        new Vector2(Constants.LOGO_CENTER.x * .375f, Constants.LOGO_CENTER.y * .375f));
                launchMessage.render(batch, font, viewport, new Vector2(viewport.getWorldWidth() / 2, Constants.HUD_MARGIN));
            }
            if (launching) {
                if (Helpers.secondsSince(launchStartTime) > 3) {
                    launching = false;
                    if (continuing) {
                        setResumeMenu();
                    } else {
                        setBeginMenu();
                    }
                }
            }
        } else {
            Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());
            if (inputControls.shootButtonJustPressed) {
                if (Cursor.getInstance().getPosition() == 75) {
                    prefs.putInteger("Difficulty", 0);
                } else if (Cursor.getInstance().getPosition() == 60) {
                    prefs.putInteger("Difficulty", 1);
                } else if (Cursor.getInstance().getPosition() == 45) {
                    prefs.putInteger("Difficulty", 2);
                }

                if (prefs.contains("Difficulty")) {
                    difficultyOptionsVisible = false;
                    LevelSelectScreen levelSelectScreen = LevelSelectScreen.getInstance();
                    levelSelectScreen.create();
                    game.setScreen(levelSelectScreen);
                    this.dispose();
                    return;
                }
            } else if (inputControls.pauseButtonJustPressed) {
                difficultyOptionsVisible = false;
            }
        }
        inputControls.update();
        onscreenControls.render(batch, viewport);
    }

    @Override
    public void dispose() {
        choices.clear();
        inputControls.clear();
        text.dispose();
        title.dispose();
        batch.dispose();
        choices = null;
        inputControls = null;
        launchBackdrop = null;
        text = null;
        title = null;
        batch = null;
        this.hide();
        super.dispose();
        System.gc();
    }
}
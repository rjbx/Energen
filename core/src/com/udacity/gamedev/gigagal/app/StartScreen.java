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
    private Cursor cursor;
    private Menu difficultyMenu;
    private Menu startMenu;
    private Menu eraseMenu;
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
        cursor = Cursor.getInstance(); // shared by all overlays instantiated from this class
        cursor.init();
        cursor.setRange(35, 20);
        cursor.setOrientation(Enums.Orientation.Y);
        cursor.resetPosition();
        startMenu = new Menu(this);
        difficultyMenu = new Menu(this);
        eraseMenu = new Menu(this);
        launchBackdrop = new Backdrop();
        launchMessage = new Message();
        launchMessage.setMessage(Constants.LAUNCH_MESSAGE);
        inputControls = com.udacity.gamedev.gigagal.app.InputControls.getInstance();
        onscreenControls = OnscreenControls.getInstance();
        Gdx.input.setInputProcessor(inputControls);
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

                    if (continuing) {
                        String[] optionStrings = {"START GAME", "ERASE GAME"};
                        startMenu.setOptionStrings(Arrays.asList(optionStrings));
                    } else {
                        String[] optionStrings = {"PRESS START"};
                        startMenu.isSingleOption(true);
                        startMenu.setOptionStrings(Arrays.asList(optionStrings));
                    }

                    startMenu.render(batch, font, viewport, cursor);

                    if (inputControls.shootButtonJustPressed) {
                        if (continuing) {
                            if (cursor.getPosition() == 35) {
                                inputControls.shootButtonJustPressed = false;
                                LevelSelectScreen levelSelectScreen = LevelSelectScreen.getInstance();
                                levelSelectScreen.create();
                                game.setScreen(levelSelectScreen);
                                this.dispose();
                                return;
                            } else if (cursor.getPosition() == 20) {
                                String[] optionStrings = {"NO", "YES"};
                                eraseMenu.setOptionStrings(Arrays.asList(optionStrings));
                                eraseMenu.setPromptString("Are you sure you want to start \na new game and erase all saved data?");
                                promptVisible = true;
                                cursor.setRange(50, 150);
                                cursor.setOrientation(Enums.Orientation.X);
                                cursor.resetPosition();
                                cursor.update();
                            }
                        } else {
                            difficultyOptionsVisible = true;
                            String[] optionStrings = {"NORMAL", "HARD", "VERY HARD"};
                            difficultyMenu.setOptionStrings(Arrays.asList(optionStrings));
                            cursor.setRange(75, 35);
                            cursor.setOrientation(Enums.Orientation.Y);
                            cursor.resetPosition();
                            cursor.update();
                        }
                    }
                } else {
                    eraseMenu.render(batch, font, viewport, cursor);
                    if (inputControls.shootButtonJustPressed) {
                        if (cursor.getPosition() == (150)) {
                            prefs.clear();
                            prefs.flush();
                            game.dispose();
                            game.create();
                        } else {
                            cursor.setRange(35, 20);
                            cursor.setOrientation(Enums.Orientation.Y);
                            cursor.resetPosition();
                            cursor.update();
                            promptVisible = false;
                        }
                    }
                }
            } else {
                launchBackdrop.render(batch, viewport, Assets.getInstance().getOverlayAssets().logo,
                        new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .625f),
                        new Vector2(Constants.LOGO_CENTER.x * .375f, Constants.LOGO_CENTER.y * .375f));
                launchMessage.render(batch, font, viewport, new Vector2(viewport.getWorldWidth() / 2, Constants.HUD_MARGIN));
            }

            if (Helpers.secondsSince(launchStartTime) > 3) {
                launching = false;
            }
        } else {
            difficultyMenu.render(batch, font, viewport, cursor);
            if (inputControls.shootButtonJustPressed) {
                if (cursor.getPosition() == 75) {
                    prefs.putInteger("Difficulty", 0);
                } else if (cursor.getPosition() == 60) {
                    prefs.putInteger("Difficulty", 1);
                } else if (cursor.getPosition() == 45) {
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
        difficultyMenu.dispose();
        eraseMenu.dispose();
        text.dispose();
        title.dispose();
        batch.dispose();
        choices = null;
        inputControls = null;
        launchBackdrop = null;
        difficultyMenu = null;
        eraseMenu = null;
        text = null;
        title = null;
        batch = null;
        this.hide();
        super.dispose();
        System.gc();
    }
}
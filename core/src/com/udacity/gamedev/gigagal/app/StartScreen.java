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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.overlays.ControlsOverlay;
import com.udacity.gamedev.gigagal.overlays.CursorOverlay;
import com.udacity.gamedev.gigagal.overlays.LaunchOverlay;
import com.udacity.gamedev.gigagal.overlays.OptionsOverlay;
import com.udacity.gamedev.gigagal.overlays.PromptOverlay;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

import java.util.Arrays;

// immutable
public final class StartScreen extends ScreenAdapter {

    // fields
    public static final String TAG = StartScreen.class.getName();
    private static InputControls inputControls;
    private static ControlsOverlay controlsOverlay;
    private com.udacity.gamedev.gigagal.app.GigaGalGame game;
    private SpriteBatch batch;
    private ExtendViewport viewport;
    private BitmapFont font;
    private BitmapFont text;
    private BitmapFont title;
    private CursorOverlay cursorOverlay;
    private OptionsOverlay difficultyOptionsOverlay;
    private OptionsOverlay startOptionsOverlay;
    private OptionsOverlay promptOverlay;
    private LaunchOverlay launchOverlay;
    private Preferences prefs;
    private Array<String> choices;
    private String prompt;
    private long launchStartTime;
    private boolean launching;
    private boolean continuing;
    private boolean difficultyOptionsVisible;
    private boolean promptVisible;
    private final Vector2 gigagalCenter;

    // default ctor
    public StartScreen(com.udacity.gamedev.gigagal.app.GigaGalGame game) {
        this.game = game;
        prefs = game.getPreferences();
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        gigagalCenter = new Vector2(Constants.GIGAGAL_STANCE_WIDTH / 2, Constants.GIGAGAL_HEIGHT / 2);
        text = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        text.getData().setScale(0.5f);
        title = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        title.getData().setScale(1);
        title.setColor(Color.SKY);
        choices = new Array<String>();
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
        cursorOverlay = new CursorOverlay(35, 20, Enums.Orientation.Y); // shared by all overlays instantiated from this class
        startOptionsOverlay = new OptionsOverlay(this);
        difficultyOptionsOverlay = new OptionsOverlay(this);
        promptOverlay = new OptionsOverlay(this);
        launchOverlay = new LaunchOverlay();
        inputControls = com.udacity.gamedev.gigagal.app.InputControls.getInstance();
        controlsOverlay = ControlsOverlay.getInstance();
        Gdx.input.setInputProcessor(inputControls);
    }

    private boolean onMobile() {
        return Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

//        cursorOverlay.getViewport().update(width, height, true);
//        controlsOverlay.getViewport().update(width, height, true);
//        controlsOverlay.recalculateButtonPositions();
//        startOptionsOverlay.getViewport().update(width, height, true);
//        startOptionsOverlay.getCursor().getViewport().update(width, height, true);
//        difficultyOptionsOverlay.getViewport().update(width, height, true);
//        difficultyOptionsOverlay.getCursor().getViewport().update(width, height, true);
//        promptOverlay.getViewport().update(width, height, true);
//        promptOverlay.getCursor().getViewport().update(width, height, true);
//        launchOverlay.getViewport().update(width, height, true);
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
                    Utils.drawTextureRegion(batch, Assets.getInstance().getGigaGalAssets().fallRight, gigagalPosition, gigagalCenter);

                    batch.end();

                    if (continuing) {
                        String[] optionStrings = {"START GAME", "ERASE GAME"};
                        startOptionsOverlay.setOptionStrings(Arrays.asList(optionStrings));
                    } else {
                        String[] optionStrings = {"START GAME"};
                        startOptionsOverlay.setOptionStrings(Arrays.asList(optionStrings));
                    }

                    startOptionsOverlay.render(batch, font, viewport, cursorOverlay);

                    if (inputControls.shootButtonJustPressed) {
                        if (continuing) {
                            if (cursorOverlay.getPosition() == 35) {
                                inputControls.shootButtonJustPressed = false;
                                game.setScreen(new LevelSelectScreen(game));
                                this.dispose();
                                return;
                            } else if (cursorOverlay.getPosition() == 20) {
                                String[] optionStrings = {"NO", "YES"};
                                promptOverlay.setOptionStrings(Arrays.asList(optionStrings));
                                promptOverlay.setPromptString("Are you sure you want to start \na new game and erase all saved data?");
                                promptVisible = true;
                                cursorOverlay.setRange(50, 150);
                                cursorOverlay.setOrientation(Enums.Orientation.X);
                                cursorOverlay.resetPosition();
                                cursorOverlay.update();
                            }
                        } else {
                            difficultyOptionsVisible = true;
                            String[] optionStrings = {"NORMAL", "HARD", "VERY HARD"};
                            difficultyOptionsOverlay.setOptionStrings(Arrays.asList(optionStrings));
                            cursorOverlay.setRange(75, 35);
                            cursorOverlay.setOrientation(Enums.Orientation.Y);
                            cursorOverlay.resetPosition();
                            cursorOverlay.update();
                        }
                    }
                } else {
                    promptOverlay.render(batch, font, viewport, cursorOverlay);
                    if (inputControls.shootButtonJustPressed) {
                        if (cursorOverlay.getPosition() == (150)) {
                            prefs.clear();
                            prefs.flush();
                            game.dispose();
                            game.create();
                        } else {
                            cursorOverlay.setRange(35, 20);
                            cursorOverlay.setOrientation(Enums.Orientation.Y);
                            cursorOverlay.resetPosition();
                            cursorOverlay.update();
                            promptVisible = false;
                        }
                    }
                }
            } else {
                launchOverlay.render(batch, font, viewport);
            }

            if (Utils.secondsSince(launchStartTime) > 3) {
                launching = false;
            }
        } else {
            difficultyOptionsOverlay.render(batch, font, viewport, cursorOverlay);
            if (inputControls.shootButtonJustPressed) {
                if (cursorOverlay.getPosition() == 75) {
                    difficultyOptionsVisible = false;
                    prefs.putInteger("Difficulty", 0);
                    game.setScreen(new LevelSelectScreen(game));
                } else if (cursorOverlay.getPosition() == 50) {
                    difficultyOptionsVisible = false;
                    prefs.putInteger("Difficulty", 1);
                    game.setScreen(new LevelSelectScreen(game));
                } else if (cursorOverlay.getPosition() == 35) {
                    difficultyOptionsVisible = false;
                    prefs.putInteger("Difficulty", 2);
                    game.setScreen(new LevelSelectScreen(game));
                }
                this.dispose();
                return;
            } else if (inputControls.pauseButtonJustPressed) {
                difficultyOptionsVisible = false;
            }
        }
        inputControls.update();
        controlsOverlay.render(batch, viewport);
    }

    @Override
    public void dispose() {
        choices.clear();
        inputControls.clear();
        launchOverlay.dispose();
        difficultyOptionsOverlay.dispose();
        promptOverlay.dispose();
        text.dispose();
        title.dispose();
        batch.dispose();
        choices = null;
        inputControls = null;
        launchOverlay = null;
        difficultyOptionsOverlay = null;
        promptOverlay = null;
        text = null;
        title = null;
        batch = null;
        this.hide();
        super.dispose();
        System.gc();
    }
}
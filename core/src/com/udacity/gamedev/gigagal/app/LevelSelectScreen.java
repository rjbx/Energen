package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.overlays.Menu;
import com.udacity.gamedev.gigagal.overlays.OnscreenControls;
import com.udacity.gamedev.gigagal.overlays.Cursor;
import com.udacity.gamedev.gigagal.overlays.Message;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

// immutable
public final class LevelSelectScreen extends ScreenAdapter {

    // fields
    public static final String TAG = LevelSelectScreen.class.getName();
    private static InputControls inputControls;
    private static OnscreenControls onscreenControls;
    private com.udacity.gamedev.gigagal.app.GigaGalGame game;
    private Preferences prefs;
    private ExtendViewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;
    private Cursor cursor;
    private Menu optionsOverlay;
    private Menu selectionOverlay;
    private Message errorMessage;
    private Array<Float> namePositions;
    private ArrayList<String> selectionStrings;
    private Array<Enums.LevelName> completedLevels;
    private ListIterator<String> iterator;
    private Enums.LevelName levelName;
    private Enums.LevelName selectedLevel;
    private GameplayScreen gameplayScreen;
    private boolean optionsVisible;
    private boolean messageVisible;

    // default ctor
    public LevelSelectScreen(com.udacity.gamedev.gigagal.app.GigaGalGame game) {
        this.game = game;
        prefs = game.getPreferences();
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.5f);
        namePositions = new Array<Float>();
    }

    @Override
    public void show() {
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        optionsVisible = false;
        messageVisible = false;
        batch = new SpriteBatch();
        completedLevels = new Array<Enums.LevelName>();
        cursor = new Cursor(145, 25, Enums.Orientation.Y);
        optionsOverlay = new Menu(this);
        selectionOverlay = new Menu(this);
        selectionStrings = new ArrayList();
        for (Enums.LevelName level : Enums.LevelName.values()) {
            selectionStrings.add(level.name());
        }
        selectionStrings.add("OPTIONS");
        iterator = selectionStrings.listIterator();
        cursor.setIterator(selectionStrings);
        iterator.next();
        selectionOverlay.setOptionStrings(selectionStrings);
        selectionOverlay.setAlignment(Align.left);
        errorMessage = new Message();
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
//        optionsOverlay.getViewport().update(width, height, true);
//        optionsOverlay.getCursor().getViewport().update(width, height, true);
//        errorMessage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!optionsVisible) {
            viewport.apply();

            float yPosition = viewport.getWorldHeight() / 2.5f;
            namePositions.add(yPosition);
            yPosition += 15;
            namePositions.add(yPosition);
            selectionOverlay.render(batch, font, viewport, cursor);

            if (inputControls.shootButtonJustPressed) {
                if (cursor.getPosition() <= 145 && cursor.getPosition() >= 40) {
                    selectedLevel = Enums.LevelName.valueOf(cursor.getIterator().previous());
                    gameplayScreen = new GameplayScreen(game, selectedLevel);
                    try {
                        gameplayScreen.readLevelFile();
                        game.setScreen(gameplayScreen);
                        this.dispose();
                        return;
                    } catch (IOException ex) {
                        Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
                        errorMessage.setMessage(Constants.LEVEL_READ_MESSAGE);
                        messageVisible = true;
                    } catch (ParseException ex) {
                        Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
                        errorMessage.setMessage(Constants.LEVEL_READ_MESSAGE);
                        messageVisible = true;
                    } catch (GdxRuntimeException ex) {
                        Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
                        errorMessage.setMessage(Constants.LEVEL_READ_MESSAGE);
                        messageVisible = true;
                    }
                } else {
                    optionsVisible = true;
                    String[] optionStrings = {"BACK", "TOUCH PAD", "QUIT GAME"};
                    optionsOverlay.setOptionStrings(Arrays.asList(optionStrings));
                    cursor.setIterator(null);
                    cursor.setRange(106, 76);
                    cursor.resetPosition();
                    cursor.update();
                }
            }
        } else {
            optionsOverlay.render(batch, font, viewport, cursor);
            if (inputControls.shootButtonJustPressed) {
                if (cursor.getPosition() == 106) {
                    cursor.setRange(145, 25);
                    cursor.resetPosition();
                    cursor.update();
                    optionsVisible = false;
                } else if (cursor.getPosition() == 91) {
                    onscreenControls.onMobile = Utils.toggleBoolean(onscreenControls.onMobile);
                    prefs.putBoolean("Mobile", onscreenControls.onMobile);
                } else if (cursor.getPosition() == 76) {
                    game.dispose();
                    game.create();
                }
            } else if (inputControls.pauseButtonJustPressed) {
                optionsVisible = false;
            }
        }
        if (messageVisible) {
            errorMessage.render(batch, font, viewport, new Vector2(viewport.getWorldWidth() / 2, Constants.HUD_MARGIN - 5));
        }
        inputControls.update();
        onscreenControls.render(batch, viewport);
    }

    @Override
    public void dispose() {
        completedLevels.clear();
        inputControls.clear();
        optionsOverlay.dispose();
        errorMessage.dispose();
        font.dispose();
        batch.dispose();
        iterator = null;
        completedLevels = null;
        inputControls = null;
        optionsOverlay = null;
        errorMessage = null;
        font = null;
        batch = null;
        this.hide();
        super.dispose();
        System.gc();
    }
}
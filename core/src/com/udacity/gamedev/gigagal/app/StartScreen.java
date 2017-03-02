package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.overlays.ControlsOverlay;
import com.udacity.gamedev.gigagal.overlays.CursorOverlay;
import com.udacity.gamedev.gigagal.overlays.LaunchOverlay;
import com.udacity.gamedev.gigagal.overlays.OptionsOverlay;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

import java.util.ListIterator;

// immutable
public final class StartScreen extends ScreenAdapter {

    // fields
    public static final String TAG = StartScreen.class.getName();
    private com.udacity.gamedev.gigagal.app.GigaGalGame game;
    private SpriteBatch batch;
    private ExtendViewport viewport;
    private BitmapFont text;
    private BitmapFont title;
    private ListIterator<String> iterator;
    private CursorOverlay cursor;
    private OptionsOverlay optionsOverlay;
    private LevelSelectScreen levelSelectScreen;
    private com.udacity.gamedev.gigagal.app.InputControls inputControls;
    private ControlsOverlay controlsOverlay;
    private LaunchOverlay launchOverlay;
    private long launchStartTime;
    private boolean launching;
    private final Vector2 gigagalCenter;

    // default ctor
    public StartScreen(com.udacity.gamedev.gigagal.app.GigaGalGame game) {
        this.game = game;
        levelSelectScreen = game.getLevelSelectScreen();
        cursor = new CursorOverlay(145, 40);
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        gigagalCenter = new Vector2(Constants.GIGAGAL_STANCE_WIDTH / 2, Constants.GIGAGAL_HEIGHT / 2);
        text = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        text.getData().setScale(0.5f);
        title = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        title.getData().setScale(1);
        title.setColor(Color.SKY);
        init();
    }

    public void init() {
        launchStartTime = TimeUtils.nanoTime();
        launching = true;
    }

    @Override
    public void show() {
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        batch = new SpriteBatch();
        optionsOverlay = new OptionsOverlay(this);
        optionsOverlay.init();
        launchOverlay = new LaunchOverlay();
        inputControls = com.udacity.gamedev.gigagal.app.InputControls.getInstance();
        controlsOverlay = ControlsOverlay.getInstance();
        Gdx.input.setInputProcessor(inputControls);
        init();
    }

    private boolean onMobile() {
        return Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        cursor.getViewport().update(width, height, true);
        controlsOverlay.getViewport().update(width, height, true);
        controlsOverlay.recalculateButtonPositions();
        optionsOverlay.getViewport().update(width, height, true);
        optionsOverlay.getCursor().getViewport().update(width, height, true);
        launchOverlay.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        Assets.getInstance().dispose();
    }

    public void update() {}

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!launching) {
            viewport.apply();
            final Vector2 gigagalPosition = new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2);
            batch.setProjectionMatrix(viewport.getCamera().combined);
            batch.begin();
//        cursor.render(batch);
//        cursor.update();

            title.draw(batch, "ENERGRAFT", viewport.getWorldWidth() / 2, viewport.getWorldHeight() - Constants.HUD_MARGIN, 0, Align.center, false);
            text.draw(batch, "START GAME", viewport.getWorldWidth() / 2, 30, 0, Align.center, false);
            Utils.drawTextureRegion(batch, Assets.getInstance().getGigaGalAssets().fallRight, gigagalPosition, gigagalCenter);

            batch.end();

            if (inputControls.shootButtonJustPressed) {
                game.setScreen(levelSelectScreen);
            }
            inputControls.update();
            controlsOverlay.render(batch);
        } else {
            launchOverlay.render(batch);
        }

        if (Utils.secondsSince(launchStartTime) > 3) {
            launching = false;
        }
    }
}
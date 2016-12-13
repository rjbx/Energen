package com.udacity.gamedev.gigagal;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.text.html.HTMLDocument;

// immutable
public final class LevelSelectScreen extends ScreenAdapter {

    // fields
    public static final String TAG = LevelSelectScreen.class.getName();
    private SpriteBatch batch;
    private int levelNumber;
    private Level level;
    private Array<String> completedLevels;
    private ExtendViewport viewport;
    private BitmapFont font;
    private List<String> levelNames;
    private float margin;
    private ListIterator<String> iterator;
    private String levelName;

    // default ctor
    public LevelSelectScreen() {
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.5f);
        levelNames = new ArrayList<String>();
        levelNames.addAll(Arrays.asList(Constants.LEVELS));
        iterator = levelNames.listIterator();
        levelName = iterator.next();
        margin = 0;
    }

    @Override
    public void show() {
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        levelNumber = 0;
        batch = new SpriteBatch();
        completedLevels = new Array<String>();

    }

    private boolean onMobile() {
        return Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        Assets.getInstance().dispose();
    }

    public void update() {
    }

    @Override
    public void render(float delta) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        while (iterator.hasPrevious()) {
            levelName = iterator.previous();
            levelName = levelName.replace("levels/", "");
            levelName = levelName.replace(".dt", "");
            font.draw(batch, levelName, viewport.getWorldWidth() / 7, viewport.getWorldHeight() / 2.5f + margin);
            margin += 15;
        }

        while (iterator.hasNext()) {
            iterator.next();
        }

        margin = 0;

        batch.end();
    }
}
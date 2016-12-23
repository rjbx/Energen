package com.udacity.gamedev.gigagal;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.overlays.CursorOverlay;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;

import java.util.ListIterator;

public class PauseScreen extends ScreenAdapter {

    // fields
    private GigaGalGame game;
    private SpriteBatch batch;
    private ExtendViewport viewport;
    private BitmapFont font;
    private float margin;
    private ListIterator<String> iterator;
    private CursorOverlay cursor;
    private Array<Float> namePositions;
    private int index;
    private GigaGal gigaGal;


    public PauseScreen(GigaGalGame game) {
        this.game = game;
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.5f);
        margin = 0;
        index = 0;
        namePositions = new Array<Float>();
        this.game = game;
        this.gigaGal = game.getGameplayScreen().getLevel().getGigaGal();
    }

    @Override
    public void show() {
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        cursor = new CursorOverlay();
        batch = new SpriteBatch();
    }

    private boolean onMobile() {
        return Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        cursor.getViewport().update(width, height, true);
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
        batch.begin();

        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cursor.render(batch);
        cursor.update();

        while (iterator.hasNext()) {
            iterator.next();
        }

        float verticalPosition = viewport.getWorldHeight() / 2.5f;
        namePositions.add(verticalPosition);
        String stats =
                Constants.HUD_AMMO_LABEL + gigaGal.getAmmo() + "\n" +
                Constants.HUD_HEALTH_LABEL + gigaGal.getHealth() + "\n" +
                "Turbo: " + gigaGal.getTurbo() + "\n" +
                Constants.HUD_WEAPON_LABEL + gigaGal.getWeapon() +
                gigaGal.getWeaponList().toString();
        while (iterator.hasPrevious()) {
            font.draw(batch, stats, viewport.getWorldWidth() / 7, namePositions.get(index));
            font.draw(batch, stats, (viewport.getWorldWidth() / 7) / 2, namePositions.get(index));
            verticalPosition += 15;
            namePositions.add(verticalPosition);
            index++;
        }

        index = 0;
        margin = 0;
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.getGameplayScreen().setGame(game);
            game.setScreen(game.getGameplayScreen());
        }

        if (cursor.getPosition() == viewport.getWorldWidth() / 7 && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.create();
        }
    }
}

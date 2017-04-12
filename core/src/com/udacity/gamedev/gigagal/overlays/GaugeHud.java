package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource;
import com.udacity.gamedev.gigagal.app.Level;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.util.Constants;

// immutable
public final class GaugeHud {

    // fields
    public static final String TAG = GaugeHud.class.toString();
    private static final GaugeHud INSTANCE = new GaugeHud();
    private float flickerFrequency;

    // default ctor
    private GaugeHud() {
    }

    public static GaugeHud getInstance() { return INSTANCE; }

    public void create() {
        flickerFrequency = 0.5f;
    }

    public void render(ShapeRenderer renderer, ExtendViewport viewport, GigaGal gigaGal) {

        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin();


        // backdrop
        renderer.setColor(Color.BLACK);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getScreenX(), viewport.getWorldHeight() - Constants.HUD_MARGIN, viewport.getWorldWidth(), viewport.getScreenHeight() / 25);

        // health

        if (gigaGal.getHealth() < Constants.MAX_HEALTH / 4) {
            renderer.setColor(Constants.HEALTH_CRITICAL_COLOR);
        } else if (gigaGal.getHealth() < Constants.MAX_HEALTH / 2) {
            renderer.setColor(Constants.HEALTH_LOW_COLOR);
        } else if (gigaGal.getHealth() < Constants.MAX_HEALTH) {
            renderer.setColor(Constants.HEALTH_NORMAL_COLOR);
        } else {
            renderer.setColor(Constants.HEALTH_MAX_COLOR);
        }
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getScreenX(), viewport.getWorldHeight() - Constants.HUD_MARGIN, ((float) gigaGal.getHealth() / Constants.MAX_HEALTH) * viewport.getWorldWidth() / 3, viewport.getScreenHeight() / 25);

        // turbo
        if (gigaGal.getTurbo() < Constants.MAX_TURBO) {
            renderer.setColor(Constants.TURBO_NORMAL_COLOR);
        } else {
            renderer.setColor(Constants.TURBO_MAX_COLOR);
        }
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getWorldWidth() / 3, viewport.getWorldHeight() - Constants.HUD_MARGIN, (gigaGal.getTurbo() / Constants.MAX_TURBO) * viewport.getWorldWidth() / 3, viewport.getScreenHeight() / 25);

        // ammo
        if (gigaGal.getChargeTimeSeconds() >  Constants.CHARGE_DURATION / 4) {
            renderer.setColor(Constants.AMMO_CHARGED_COLOR);
        } else {
            renderer.setColor(Constants.AMMO_NORMAL_COLOR);
        }

        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getWorldWidth() / 3 * 2, viewport.getWorldHeight() - Constants.HUD_MARGIN, ((float) gigaGal.getAmmo() / Constants.MAX_AMMO) * viewport.getWorldWidth() / 3, viewport.getScreenHeight() / 25);
        renderer.end();
    }
}
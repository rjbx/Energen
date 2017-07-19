package com.udacity.gamedev.gigagal.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entity.GigaGal;
import com.udacity.gamedev.gigagal.util.Constants;

// immutable
public final class GaugeHud {

    // fields
    public static final String TAG = GaugeHud.class.toString();
    private static final GaugeHud INSTANCE = new GaugeHud();
    private float flickerFrequency;
    private Viewport viewport; // class-level instantiation

    // default ctor
    private GaugeHud() {
        this.viewport = new ExtendViewport(
                Constants.CONTROLS_OVERLAY_VIEWPORT_SIZE,
                Constants.CONTROLS_OVERLAY_VIEWPORT_SIZE);
    }

    public static GaugeHud getInstance() { return INSTANCE; }

    public void create() {
        flickerFrequency = 0.5f;
    }

    public void render(ShapeRenderer renderer, GigaGal gigaGal) {

        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin();

        // backdrop
        renderer.setColor(Color.BLACK);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getCamera().position.x  - viewport.getWorldWidth() / 2, viewport.getCamera().position.y + viewport.getWorldHeight() / 2.25f, viewport.getWorldWidth(), viewport.getWorldHeight() / 15);

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
        renderer.rect(viewport.getCamera().position.x - viewport.getWorldWidth() / 2, viewport.getCamera().position.y + viewport.getWorldHeight() / 2.25f, ((float) gigaGal.getHealth() / Constants.MAX_HEALTH) * viewport.getWorldWidth() / 3, viewport.getWorldHeight() / 15);

        // turbo
        if (gigaGal.getTurbo() < Constants.MAX_TURBO) {
            renderer.setColor(Constants.TURBO_NORMAL_COLOR);
        } else {
            renderer.setColor(Constants.TURBO_MAX_COLOR);
        }
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getCamera().position.x  - viewport.getWorldWidth() / 6, viewport.getCamera().position.y + viewport.getWorldHeight() / 2.25f, (gigaGal.getTurbo() / Constants.MAX_TURBO) * viewport.getWorldWidth() / 3, viewport.getWorldHeight() / 15);

        // ammo
        if (gigaGal.getChargeTimeSeconds() >  Constants.CHARGE_DURATION / 4) {
            renderer.setColor(Constants.AMMO_CHARGED_COLOR);
        } else {
            renderer.setColor(Constants.AMMO_NORMAL_COLOR);
        }

        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getCamera().position.x  + viewport.getWorldWidth() / 6, viewport.getCamera().position.y + viewport.getWorldHeight() / 2.25f, ((float) gigaGal.getAmmo() / Constants.MAX_AMMO) * viewport.getWorldWidth() / 3, viewport.getWorldHeight() / 15);
        renderer.end();
    }
}
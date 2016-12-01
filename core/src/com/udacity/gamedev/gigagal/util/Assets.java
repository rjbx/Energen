package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

// immutable singleton
public final class Assets implements Disposable, AssetErrorListener {

    // fields
    public static final String TAG = Assets.class.getName();
    private static final Assets INSTANCE = new Assets();
    private GigaGalAssets gigaGalAssets;
    private PlatformAssets platformAssets;
    private BulletAssets bulletAssets;
    private ZoombaAssets zoombaAssets;
    private SpikeAssets spikeAssets;
    private FlameAssets flameAssets;
    private GeiserAssets geiserAssets;
    private WheelAssets wheelAssets;
    private ExplosionAssets explosionAssets;
    private PowerupAssets powerupAssets;
    private ExitPortalAssets exitPortalAssets;
    private OnscreenControlsAssets onscreenControlsAssets;

    private AssetManager assetManager;

    // non-instantiable; cannot be subclassed
    private Assets() {}

    // static factory
    public static Assets getInstance() { return INSTANCE; }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS, TextureAtlas.class);
        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS);
        gigaGalAssets = new GigaGalAssets(atlas);
        platformAssets = new PlatformAssets(atlas);
        bulletAssets = new BulletAssets(atlas);
        zoombaAssets = new ZoombaAssets(atlas);
        spikeAssets = new SpikeAssets(atlas);
        flameAssets = new FlameAssets(atlas);
        geiserAssets = new GeiserAssets(atlas);
        wheelAssets = new WheelAssets(atlas);
        explosionAssets = new ExplosionAssets(atlas);
        powerupAssets = new PowerupAssets(atlas);
        exitPortalAssets = new ExitPortalAssets(atlas);
        onscreenControlsAssets = new OnscreenControlsAssets(atlas);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset: " + asset.fileName, throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public class GigaGalAssets {

        public final AtlasRegion standLeft;
        public final AtlasRegion standRight;
        public final AtlasRegion strideLeft;
        public final AtlasRegion strideRight;
        public final AtlasRegion jumpLeft;
        public final AtlasRegion jumpRight;
        public final AtlasRegion dashLeft;
        public final AtlasRegion dashRight;
        public final AtlasRegion ricochetLeft;
        public final AtlasRegion ricochetRight;
        public final Animation hoverLeftAnimation;
        public final Animation hoverRightAnimation;
        public final Animation strideLeftAnimation;
        public final Animation strideRightAnimation;

        public GigaGalAssets(TextureAtlas atlas) {
            standLeft = atlas.findRegion(Constants.STAND_LEFT);
            standRight = atlas.findRegion(Constants.STAND_RIGHT);
            strideLeft = atlas.findRegion(Constants.STRIDE_LEFT_2);
            strideRight = atlas.findRegion(Constants.STRIDE_RIGHT_2);
            jumpLeft = atlas.findRegion(Constants.JUMP_LEFT);
            jumpRight = atlas.findRegion(Constants.JUMP_RIGHT);
            dashLeft = atlas.findRegion(Constants.STRIDE_LEFT_2);
            dashRight = atlas.findRegion(Constants.STRIDE_RIGHT_2);
            ricochetLeft = atlas.findRegion(Constants.RICOCHET_LEFT);
            ricochetRight = atlas.findRegion(Constants.RICOCHET_RIGHT);
            
            Array<AtlasRegion> hoverLeftFrames = new Array<AtlasRegion>();
            hoverLeftFrames.add(atlas.findRegion(Constants.HOVER_LEFT_1));
            hoverLeftFrames.add(atlas.findRegion(Constants.HOVER_LEFT_2));
            hoverLeftAnimation = new Animation(Constants.HOVER_LOOP_DURATION, hoverLeftFrames, PlayMode.LOOP);

            Array<AtlasRegion> hoverRightFrames = new Array<AtlasRegion>();
            hoverRightFrames.add(atlas.findRegion(Constants.HOVER_RIGHT_1));
            hoverRightFrames.add(atlas.findRegion(Constants.HOVER_RIGHT_2));
            hoverRightAnimation = new Animation(Constants.HOVER_LOOP_DURATION, hoverRightFrames, PlayMode.LOOP);

            Array<AtlasRegion> strideLeftFrames = new Array<AtlasRegion>();
            strideLeftFrames.add(atlas.findRegion(Constants.STRIDE_LEFT_2));
            strideLeftFrames.add(atlas.findRegion(Constants.STRIDE_LEFT_1));
            strideLeftFrames.add(atlas.findRegion(Constants.STRIDE_LEFT_2));
            strideLeftFrames.add(atlas.findRegion(Constants.STRIDE_LEFT_3));
            strideLeftAnimation = new Animation(Constants.STRIDE_LOOP_DURATION, strideLeftFrames, PlayMode.LOOP);

            Array<AtlasRegion> strideRightFrames = new Array<AtlasRegion>();
            strideRightFrames.add(atlas.findRegion(Constants.STRIDE_RIGHT_2));
            strideRightFrames.add(atlas.findRegion(Constants.STRIDE_RIGHT_1));
            strideRightFrames.add(atlas.findRegion(Constants.STRIDE_RIGHT_2));
            strideRightFrames.add(atlas.findRegion(Constants.STRIDE_RIGHT_3));
            strideRightAnimation = new Animation(Constants.STRIDE_LOOP_DURATION, strideRightFrames, PlayMode.LOOP);
        }
    }

    public class PlatformAssets {

        public final NinePatch platformNinePatch;
        public PlatformAssets(TextureAtlas atlas) {
            AtlasRegion region = atlas.findRegion(Constants.PLATFORM_SPRITE);
            int edge = Constants.PLATFORM_EDGE;
            platformNinePatch = new NinePatch(region, edge, edge, edge, edge);
        }
    }

    public class BulletAssets {

        public final AtlasRegion bullet;
        public final AtlasRegion chargeBullet;

        public BulletAssets(TextureAtlas atlas) {
            bullet = atlas.findRegion(Constants.BULLET_SPRITE);
            chargeBullet = atlas.findRegion(Constants.CHARGE_BULLET_SPRITE);
        }

    }

    public class ZoombaAssets {

        public final AtlasRegion zoomba;

        public ZoombaAssets(TextureAtlas atlas) {
            zoomba = atlas.findRegion(Constants.ZOOMBA_SPRITE);
        }
    }

    public class SpikeAssets {

        public final AtlasRegion spike;

        public SpikeAssets(TextureAtlas atlas) {
            spike = atlas.findRegion(Constants.SPIKE_SPRITE);
        }
    }

    public class FlameAssets {

        public final Animation flame;

        public FlameAssets(TextureAtlas atlas) {
            Array<AtlasRegion> flameRegions = new Array<AtlasRegion>();
            flameRegions.add(atlas.findRegion(Constants.FLAME_SPRITE_1));
            flameRegions.add(atlas.findRegion(Constants.FLAME_SPRITE_2));

            flame = new Animation(Constants.FLAME_DURATION / flameRegions.size,
                    flameRegions, PlayMode.NORMAL);
        }
    }

    public class GeiserAssets {

        public final Animation geiser;

        public GeiserAssets(TextureAtlas atlas) {
            Array<AtlasRegion> geiserRegions = new Array<AtlasRegion>();
            geiserRegions.add(atlas.findRegion(Constants.GEISER_SPRITE_1));
            geiserRegions.add(atlas.findRegion(Constants.GEISER_SPRITE_2));

            geiser = new Animation(Constants.GEISER_DURATION / geiserRegions.size,
                    geiserRegions, PlayMode.NORMAL);
        }
    }

    public class WheelAssets {

        public final Animation wheel;

        public WheelAssets(TextureAtlas atlas) {
            Array<AtlasRegion> wheelRegions = new Array<AtlasRegion>();
            wheelRegions.add(atlas.findRegion(Constants.WHEEL_SPRITE_1));
            wheelRegions.add(atlas.findRegion(Constants.WHEEL_SPRITE_2));

            wheel = new Animation(Constants.WHEEL_DURATION / wheelRegions.size,
                    wheelRegions, PlayMode.NORMAL);
        }
    }

    public class ExplosionAssets {

        public final Animation explosion;

        public ExplosionAssets(TextureAtlas atlas) {

            Array<AtlasRegion> explosionRegions = new Array<AtlasRegion>();
            explosionRegions.add(atlas.findRegion(Constants.EXPLOSION_LARGE));
            explosionRegions.add(atlas.findRegion(Constants.EXPLOSION_MEDIUM));
            explosionRegions.add(atlas.findRegion(Constants.EXPLOSION_SMALL));

            explosion = new Animation(Constants.EXPLOSION_DURATION / explosionRegions.size,
                    explosionRegions, PlayMode.NORMAL);
        }
    }

    public class PowerupAssets {
        public final AtlasRegion ammoPowerup;
        public final AtlasRegion healthPowerup;

        public PowerupAssets(TextureAtlas atlas) {
            ammoPowerup = atlas.findRegion(Constants.AMMO_POWERUP_SPRITE);
            healthPowerup = atlas.findRegion(Constants.HEALTH_POWERUP_SPRITE);
        }
    }

    public class ExitPortalAssets {

        public final Animation exitPortal;

        public ExitPortalAssets(TextureAtlas atlas) {
            final AtlasRegion exitPortal1 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_1);
            final AtlasRegion exitPortal2 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_2);
            final AtlasRegion exitPortal3 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_3);
            final AtlasRegion exitPortal4 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_4);
            final AtlasRegion exitPortal5 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_5);
            final AtlasRegion exitPortal6 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_6);

            Array<AtlasRegion> exitPortalFrames = new Array<AtlasRegion>();
            exitPortalFrames.addAll(exitPortal1, exitPortal2, exitPortal3, exitPortal4, exitPortal5, exitPortal6);

            exitPortal = new Animation(Constants.EXIT_PORTAL_FRAME_DURATION, exitPortalFrames);
        }
    }

    public class OnscreenControlsAssets {

        public final AtlasRegion moveRight;
        public final AtlasRegion moveLeft;
        public final AtlasRegion shoot;
        public final AtlasRegion blast;
        public final AtlasRegion jump;
        public final AtlasRegion hover;
        public final AtlasRegion ricochet;

        public OnscreenControlsAssets(TextureAtlas atlas) {
            moveRight = atlas.findRegion(Constants.MOVE_RIGHT_BUTTON);
            moveLeft = atlas.findRegion(Constants.MOVE_LEFT_BUTTON);
            shoot = atlas.findRegion(Constants.SHOOT_BUTTON);
            blast = atlas.findRegion(Constants.BLAST_BUTTON);
            jump = atlas.findRegion(Constants.JUMP_BUTTON);
            hover = atlas.findRegion(Constants.HOVER_BUTTON);
            ricochet = atlas.findRegion(Constants.RICOCHET_BUTTON);
        }
    }

    // Getters
    public final GigaGalAssets getGigaGalAssets() { return gigaGalAssets; }
    public final PlatformAssets getPlatformAssets() { return platformAssets; }
    public final BulletAssets getBulletAssets() { return bulletAssets; }
    public final ZoombaAssets getZoombaAssets() { return zoombaAssets; }
    public final SpikeAssets getSpikeAssets() { return spikeAssets; }
    public final FlameAssets getFlameAssets() { return flameAssets; }
    public final GeiserAssets getGeiserAssets() { return geiserAssets; }
    public final WheelAssets getWheelAssets() { return wheelAssets; }
    public final ExplosionAssets getExplosionAssets() { return explosionAssets; }
    public final PowerupAssets getPowerupAssets() { return powerupAssets; }
    public final ExitPortalAssets getExitPortalAssets() { return exitPortalAssets; }
    public final OnscreenControlsAssets getOnscreenControlsAssets() { return onscreenControlsAssets; }
}

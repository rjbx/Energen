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
    private CannonAssets cannonAssets;
    private PillarAssets pillarAssets;
    private AmmoAssets ammoAssets;
    private ZoombaAssets zoombaAssets;
    private FireyZoombaAssets fireyZoombaAssets;
    private GushingZoombaAssets gushingZoombaAssets;
    private ChargedZoombaAssets chargedZoombaAssets;
    private WhirlingZoombaAssets whirlingZoombaAssets;
    private SharpZoombaAssets sharpZoombaAssets;
    private SwoopaAssets swoopaAssets;
    private FireySwoopaAssets fireySwoopaAssets;
    private GushingSwoopaAssets gushingSwoopaAssets;
    private ChargedSwoopaAssets chargedSwoopaAssets;
    private WhirlingSwoopaAssets whirlingSwoopaAssets;
    private SharpSwoopaAssets sharpSwoopaAssets;
    private SpikeAssets spikeAssets;
    private FlameAssets flameAssets;
    private GeiserAssets geiserAssets;
    private WheelAssets wheelAssets;
    private CoilAssets coilAssets;
    private VacuumAssets vacuumAssets;
    private ExplosionAssets explosionAssets;
    private PowerupAssets powerupAssets;
    private PortalAssets portalAssets;
    private OnscreenControlsAssets onscreenControlsAssets;

    private AssetManager assetManager;

    // non-instantiable; cannot be subclassed
    private Assets() {}

    // static factory
    public static Assets getInstance() { return INSTANCE; }

    public void init(AssetManager assetManager, int levelNumber) {
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS, TextureAtlas.class);
        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS);
        gigaGalAssets = new GigaGalAssets(atlas);
        platformAssets = new PlatformAssets(atlas, levelNumber);
        cannonAssets = new CannonAssets(atlas);
        pillarAssets = new PillarAssets(atlas);
        ammoAssets = new AmmoAssets(atlas);
        zoombaAssets = new ZoombaAssets(atlas);
        fireyZoombaAssets = new FireyZoombaAssets(atlas);
        gushingZoombaAssets = new GushingZoombaAssets(atlas);
        chargedZoombaAssets = new ChargedZoombaAssets(atlas);
        whirlingZoombaAssets = new WhirlingZoombaAssets(atlas);
        sharpZoombaAssets = new SharpZoombaAssets(atlas);
        swoopaAssets = new SwoopaAssets(atlas);
        fireySwoopaAssets = new FireySwoopaAssets(atlas);
        gushingSwoopaAssets = new GushingSwoopaAssets(atlas);
        chargedSwoopaAssets = new ChargedSwoopaAssets(atlas);
        whirlingSwoopaAssets = new WhirlingSwoopaAssets(atlas);
        sharpSwoopaAssets = new SharpSwoopaAssets(atlas);
        spikeAssets = new SpikeAssets(atlas);
        flameAssets = new FlameAssets(atlas);
        geiserAssets = new GeiserAssets(atlas);
        wheelAssets = new WheelAssets(atlas);
        coilAssets = new CoilAssets(atlas);
        vacuumAssets = new VacuumAssets(atlas);
        explosionAssets = new ExplosionAssets(atlas);
        powerupAssets = new PowerupAssets(atlas);
        portalAssets = new PortalAssets(atlas);
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
        public PlatformAssets(TextureAtlas atlas, int levelNumber) {
            AtlasRegion region;
            switch(levelNumber) {
                case 0: region = atlas.findRegion(Constants.RED_PLATFORM_SPRITE); break;
                case 1: region = atlas.findRegion(Constants.GREY_PLATFORM_SPRITE); break;
                case 2: region = atlas.findRegion(Constants.BLACK_PLATFORM_SPRITE); break;
                case 3: region = atlas.findRegion(Constants.YELLOW_PLATFORM_SPRITE); break;
                case 4: region = atlas.findRegion(Constants.BLUE_PLATFORM_SPRITE); break;
                case 5: region = atlas.findRegion(Constants.CLEAR_PLATFORM_SPRITE); break;
                case 6: region = atlas.findRegion(Constants.MAGENTA_PLATFORM_SPRITE); break;
                default: region = atlas.findRegion(Constants.PLATFORM_SPRITE);
            }

            int edge = Constants.PLATFORM_EDGE;
            platformNinePatch = new NinePatch(region, edge, edge, edge, edge);
        }
    }

    public class AmmoAssets {

        public final AtlasRegion nativeShot;
        public final AtlasRegion nativeBlast;
        public final AtlasRegion fireShot;
        public final AtlasRegion fireBlast;
        public final AtlasRegion waterShot;
        public final AtlasRegion waterBlast;
        public final AtlasRegion electricShot;
        public final AtlasRegion electricBlast;
        public final AtlasRegion rubberShot;
        public final AtlasRegion rubberBlast;
        public final AtlasRegion metalShot;
        public final AtlasRegion metalBlast;
        public final AtlasRegion psychicShot;
        public final AtlasRegion psychicBlast;

        public AmmoAssets(TextureAtlas atlas) {
            nativeShot = atlas.findRegion(Constants.SHOT_NATIVE_SPRITE);
            nativeBlast = atlas.findRegion(Constants.BLAST_NATIVE_SPRITE);
            fireShot = atlas.findRegion(Constants.SHOT_FIRE_SPRITE);
            fireBlast = atlas.findRegion(Constants.BLAST_FIRE_SPRITE);
            waterShot = atlas.findRegion(Constants.SHOT_WATER_SPRITE);
            waterBlast = atlas.findRegion(Constants.BLAST_WATER_SPRITE);
            electricShot = atlas.findRegion(Constants.SHOT_ELECTRIC_SPRITE);
            electricBlast = atlas.findRegion(Constants.BLAST_ELECTRIC_SPRITE);
            rubberShot = atlas.findRegion(Constants.SHOT_RUBBER_SPRITE);
            rubberBlast = atlas.findRegion(Constants.BLAST_RUBBER_SPRITE);
            metalShot = atlas.findRegion(Constants.SHOT_METAL_SPRITE);
            metalBlast = atlas.findRegion(Constants.BLAST_METAL_SPRITE);
            psychicShot = atlas.findRegion(Constants.SHOT_PSYCHIC_SPRITE);
            psychicBlast = atlas.findRegion(Constants.BLAST_PSYCHIC_SPRITE);
        }
    }

    public class CannonAssets {

        public final AtlasRegion verticalCannon;
        public final AtlasRegion lateralCannon;


        public CannonAssets(TextureAtlas atlas) {
            verticalCannon = atlas.findRegion(Constants.VERTICAL_CANNON_SPRITE);
            lateralCannon = atlas.findRegion(Constants.LATERAL_CANNON_SPRITE);
        }
    }
    

    public class PillarAssets {

        public final AtlasRegion pillar;

        public PillarAssets(TextureAtlas atlas) {
            pillar = atlas.findRegion(Constants.PILLAR_SPRITE);
        }
    }
    

    public class ZoombaAssets {

        public final AtlasRegion zoomba;

        public ZoombaAssets(TextureAtlas atlas) {
            zoomba = atlas.findRegion(Constants.ZOOMBA_SPRITE);
        }
    }
    
    public class FireyZoombaAssets {

        public final Animation fireyZoomba;

        public FireyZoombaAssets(TextureAtlas atlas) {
            Array<AtlasRegion> fireyZoombaRegions = new Array<AtlasRegion>();
            fireyZoombaRegions.add(atlas.findRegion(Constants.FIREYZOOMBA_SPRITE_1));
            fireyZoombaRegions.add(atlas.findRegion(Constants.FIREYZOOMBA_SPRITE_2));

            fireyZoomba = new Animation(Constants.FLAME_DURATION / fireyZoombaRegions.size,
                    fireyZoombaRegions, PlayMode.NORMAL);
        }
    }

    public class GushingZoombaAssets {

        public final Animation gushingZoomba;

        public GushingZoombaAssets(TextureAtlas atlas) {
            Array<AtlasRegion> gushingZoombaRegions = new Array<AtlasRegion>();
            gushingZoombaRegions.add(atlas.findRegion(Constants.GUSHINGZOOMBA_SPRITE_1));
            gushingZoombaRegions.add(atlas.findRegion(Constants.GUSHINGZOOMBA_SPRITE_2));

            gushingZoomba = new Animation(Constants.GEISER_DURATION / gushingZoombaRegions.size,
                    gushingZoombaRegions, PlayMode.NORMAL);
        }
    }

    public class ChargedZoombaAssets {

        public final Animation chargedZoomba;

        public ChargedZoombaAssets(TextureAtlas atlas) {
            Array<AtlasRegion> chargedZoombaRegions = new Array<AtlasRegion>();
            chargedZoombaRegions.add(atlas.findRegion(Constants.CHARGEDZOOMBA_SPRITE_1));
            chargedZoombaRegions.add(atlas.findRegion(Constants.CHARGEDZOOMBA_SPRITE_2));

            chargedZoomba = new Animation(Constants.COIL_DURATION / chargedZoombaRegions.size,
                    chargedZoombaRegions, PlayMode.NORMAL);
        }
    }

    public class WhirlingZoombaAssets {

        public final Animation whirlingZoomba;

        public WhirlingZoombaAssets(TextureAtlas atlas) {
            Array<AtlasRegion> whirlingZoombaRegions = new Array<AtlasRegion>();
            whirlingZoombaRegions.add(atlas.findRegion(Constants.WHIRLINGZOOMBA_SPRITE_1));
            whirlingZoombaRegions.add(atlas.findRegion(Constants.WHIRLINGZOOMBA_SPRITE_2));

            whirlingZoomba = new Animation(Constants.WHEEL_DURATION / whirlingZoombaRegions.size,
                    whirlingZoombaRegions, PlayMode.NORMAL);
        }
    }

    public class SharpZoombaAssets {

        public final AtlasRegion sharpZoomba;

        public SharpZoombaAssets(TextureAtlas atlas) {
            sharpZoomba = atlas.findRegion(Constants.SHARPZOOMBA_SPRITE);
        }
    }

    public class SwoopaAssets {

        public final AtlasRegion swoopa;

        public SwoopaAssets(TextureAtlas atlas) {
            swoopa = atlas.findRegion(Constants.SWOOPA_SPRITE);
        }
    }

    public class FireySwoopaAssets {

        public final Animation fireySwoopa;

        public FireySwoopaAssets(TextureAtlas atlas) {
            Array<AtlasRegion> fireySwoopaRegions = new Array<AtlasRegion>();
            fireySwoopaRegions.add(atlas.findRegion(Constants.FIREYSWOOPA_SPRITE_1));
            fireySwoopaRegions.add(atlas.findRegion(Constants.FIREYSWOOPA_SPRITE_2));

            fireySwoopa = new Animation(Constants.FLAME_DURATION / fireySwoopaRegions.size,
                    fireySwoopaRegions, PlayMode.NORMAL);
        }
    }

    public class GushingSwoopaAssets {

        public final Animation gushingSwoopa;

        public GushingSwoopaAssets(TextureAtlas atlas) {
            Array<AtlasRegion> gushingSwoopaRegions = new Array<AtlasRegion>();
            gushingSwoopaRegions.add(atlas.findRegion(Constants.GUSHINGSWOOPA_SPRITE_1));
            gushingSwoopaRegions.add(atlas.findRegion(Constants.GUSHINGSWOOPA_SPRITE_2));

            gushingSwoopa = new Animation(Constants.GEISER_DURATION / gushingSwoopaRegions.size,
                    gushingSwoopaRegions, PlayMode.NORMAL);
        }
    }

    public class ChargedSwoopaAssets {

        public final Animation chargedSwoopa;

        public ChargedSwoopaAssets(TextureAtlas atlas) {
            Array<AtlasRegion> chargedSwoopaRegions = new Array<AtlasRegion>();
            chargedSwoopaRegions.add(atlas.findRegion(Constants.CHARGEDSWOOPA_SPRITE_1));
            chargedSwoopaRegions.add(atlas.findRegion(Constants.CHARGEDSWOOPA_SPRITE_2));

            chargedSwoopa = new Animation(Constants.COIL_DURATION / chargedSwoopaRegions.size,
                    chargedSwoopaRegions, PlayMode.NORMAL);
        }
    }

    public class WhirlingSwoopaAssets {

        public final Animation whirlingSwoopa;

        public WhirlingSwoopaAssets(TextureAtlas atlas) {
            Array<AtlasRegion> whirlingSwoopaRegions = new Array<AtlasRegion>();
            whirlingSwoopaRegions.add(atlas.findRegion(Constants.WHIRLINGSWOOPA_SPRITE_1));
            whirlingSwoopaRegions.add(atlas.findRegion(Constants.WHIRLINGSWOOPA_SPRITE_2));

            whirlingSwoopa = new Animation(Constants.WHEEL_DURATION / whirlingSwoopaRegions.size,
                    whirlingSwoopaRegions, PlayMode.NORMAL);
        }
    }

    public class SharpSwoopaAssets {

        public final AtlasRegion sharpSwoopa;

        public SharpSwoopaAssets(TextureAtlas atlas) {
            sharpSwoopa = atlas.findRegion(Constants.SHARPSWOOPA_SPRITE);
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

    public class CoilAssets {

        public final Animation coil;

        public CoilAssets(TextureAtlas atlas) {
            Array<AtlasRegion> coilRegions = new Array<AtlasRegion>();
            coilRegions.add(atlas.findRegion(Constants.COIL_SPRITE_1));
            coilRegions.add(atlas.findRegion(Constants.COIL_SPRITE_2));

            coil = new Animation(Constants.COIL_DURATION / coilRegions.size,
                    coilRegions, PlayMode.NORMAL);
        }
    }
    
    public class VacuumAssets {

        public final Animation vacuum;

        public VacuumAssets(TextureAtlas atlas) {
            Array<AtlasRegion> vacuumRegions = new Array<AtlasRegion>();
            vacuumRegions.add(atlas.findRegion(Constants.VACUUM_SPRITE_1));
            vacuumRegions.add(atlas.findRegion(Constants.VACUUM_SPRITE_2));
            vacuumRegions.add(atlas.findRegion(Constants.VACUUM_SPRITE_3));

            vacuum = new Animation(Constants.VACUUM_FRAME_DURATION,
                    vacuumRegions, PlayMode.LOOP_PINGPONG);
        }
    }

    public class PortalAssets {

        public final Animation portal;

        public PortalAssets(TextureAtlas atlas) {
            Array<AtlasRegion> portalRegions = new Array<AtlasRegion>();
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_1));
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_2));
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_3));
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_4));
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_5));
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_6));

            portal = new Animation(Constants.PORTAL_FRAME_DURATION,
                    portalRegions, PlayMode.NORMAL);
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
    public final CannonAssets getCannonAssets() { return cannonAssets; }
    public final PillarAssets getPillarAssets() { return pillarAssets; }
    public final AmmoAssets getAmmoAssets() { return ammoAssets; }
    public final ZoombaAssets getZoombaAssets() { return zoombaAssets; }
    public final FireyZoombaAssets getFireyZoombaAssets() { return fireyZoombaAssets; }
    public final GushingZoombaAssets getGushingZoombaAssets() { return gushingZoombaAssets; }
    public final ChargedZoombaAssets getChargedZoombaAssets() { return chargedZoombaAssets; }
    public final WhirlingZoombaAssets getWhirlingZoombaAssets() { return whirlingZoombaAssets; }
    public final SharpZoombaAssets getSharpZoombaAssets() { return sharpZoombaAssets; }
    public final SwoopaAssets getSwoopaAssets() { return swoopaAssets; }
    public final FireySwoopaAssets getFireySwoopaAssets() { return fireySwoopaAssets; }
    public final GushingSwoopaAssets getGushingSwoopaAssets() { return gushingSwoopaAssets; }
    public final ChargedSwoopaAssets getChargedSwoopaAssets() { return chargedSwoopaAssets; }
    public final WhirlingSwoopaAssets getWhirlingSwoopaAssets() { return whirlingSwoopaAssets; }
    public final SharpSwoopaAssets getSharpSwoopaAssets() { return sharpSwoopaAssets; }
    public final SpikeAssets getSpikeAssets() { return spikeAssets; }
    public final FlameAssets getFlameAssets() { return flameAssets; }
    public final GeiserAssets getGeiserAssets() { return geiserAssets; }
    public final WheelAssets getWheelAssets() { return wheelAssets; }
    public final CoilAssets getCoilAssets() { return coilAssets; }
    public final VacuumAssets getVacuumAssets() { return vacuumAssets; }
    public final ExplosionAssets getExplosionAssets() { return explosionAssets; }
    public final PowerupAssets getPowerupAssets() { return powerupAssets; }
    public final PortalAssets getPortalAssets() { return portalAssets; }
    public final OnscreenControlsAssets getOnscreenControlsAssets() { return onscreenControlsAssets; }
}

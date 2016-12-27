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
    private LadderAssets ladderAssets;
    private SlickAssets slickAssets;
    private TreadmillAssets treadmillAssets;
    private SpringAssets springAssets;
    private SinkAssets sinkAssets;
    private AmmoAssets ammoAssets;
    private ZoombaAssets zoombaAssets;
    private FieryZoombaAssets fieryZoombaAssets;
    private GushingZoombaAssets gushingZoombaAssets;
    private ChargedZoombaAssets chargedZoombaAssets;
    private WhirlingZoombaAssets whirlingZoombaAssets;
    private SharpZoombaAssets sharpZoombaAssets;
    private SwoopaAssets swoopaAssets;
    private FierySwoopaAssets fierySwoopaAssets;
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
    private HudAssets hudAssets;

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
        ladderAssets = new LadderAssets(atlas);
        slickAssets = new SlickAssets(atlas);
        treadmillAssets = new TreadmillAssets(atlas);
        springAssets = new SpringAssets(atlas);
        sinkAssets = new SinkAssets(atlas);
        ammoAssets = new AmmoAssets(atlas);
        zoombaAssets = new ZoombaAssets(atlas);
        fieryZoombaAssets = new FieryZoombaAssets(atlas);
        gushingZoombaAssets = new GushingZoombaAssets(atlas);
        chargedZoombaAssets = new ChargedZoombaAssets(atlas);
        whirlingZoombaAssets = new WhirlingZoombaAssets(atlas);
        sharpZoombaAssets = new SharpZoombaAssets(atlas);
        swoopaAssets = new SwoopaAssets(atlas);
        fierySwoopaAssets = new FierySwoopaAssets(atlas);
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
        hudAssets = new HudAssets(atlas);
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
        public final AtlasRegion recoilLeft;
        public final AtlasRegion recoilRight;
        public final AtlasRegion fallLeft;
        public final AtlasRegion fallRight;
        public final AtlasRegion lookupLeft;
        public final AtlasRegion lookupRight;
        public final AtlasRegion lookdownLeft;
        public final AtlasRegion lookdownRight;
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
            recoilLeft = atlas.findRegion(Constants.RECOIL_LEFT);
            recoilRight = atlas.findRegion(Constants.RECOIL_RIGHT);
            fallLeft = atlas.findRegion(Constants.FALL_LEFT);
            fallRight = atlas.findRegion(Constants.FALL_RIGHT);
            lookupLeft = atlas.findRegion(Constants.LOOKUP_LEFT);
            lookupRight = atlas.findRegion(Constants.LOOKUP_RIGHT);
            lookdownLeft = atlas.findRegion(Constants.LOOKDOWN_LEFT);
            lookdownRight = atlas.findRegion(Constants.LOOKDOWN_RIGHT);
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
        public final AtlasRegion fireShotAlt;
        public final AtlasRegion fireBlast;
        public final AtlasRegion fireBlastAlt;
        public final AtlasRegion waterShot;
        public final AtlasRegion waterShotAlt;
        public final AtlasRegion waterBlast;
        public final AtlasRegion waterBlastAlt;
        public final AtlasRegion electricShot;
        public final AtlasRegion electricShotAlt;
        public final AtlasRegion electricBlast;
        public final AtlasRegion rubberShot;
        public final AtlasRegion rubberShotAlt;
        public final AtlasRegion rubberBlast;
        public final AtlasRegion metalShot;
        public final AtlasRegion metalShotAlt;
        public final AtlasRegion metalBlast;
        public final AtlasRegion metalBlastAlt;
        public final AtlasRegion psychicShot;
        public final AtlasRegion psychicBlast;

        public AmmoAssets(TextureAtlas atlas) {
            nativeShot = atlas.findRegion(Constants.SHOT_NATIVE_SPRITE);
            nativeBlast = atlas.findRegion(Constants.BLAST_NATIVE_SPRITE);
            fireShot = atlas.findRegion(Constants.SHOT_FIRE_SPRITE);
            fireShotAlt = atlas.findRegion(Constants.SHOT_FIRE_SPRITE_ALT);
            fireBlast = atlas.findRegion(Constants.BLAST_FIRE_SPRITE);
            fireBlastAlt = atlas.findRegion(Constants.BLAST_FIRE_SPRITE_ALT);
            waterShot = atlas.findRegion(Constants.SHOT_WATER_SPRITE);
            waterShotAlt = atlas.findRegion(Constants.SHOT_WATER_SPRITE_ALT);
            waterBlast = atlas.findRegion(Constants.BLAST_WATER_SPRITE);
            waterBlastAlt = atlas.findRegion(Constants.BLAST_WATER_SPRITE_ALT);
            electricShot = atlas.findRegion(Constants.SHOT_ELECTRIC_SPRITE);
            electricShotAlt = atlas.findRegion(Constants.SHOT_ELECTRIC_SPRITE_ALT);
            electricBlast = atlas.findRegion(Constants.BLAST_ELECTRIC_SPRITE);
            rubberShot = atlas.findRegion(Constants.SHOT_RUBBER_SPRITE);
            rubberShotAlt = atlas.findRegion(Constants.SHOT_RUBBER_SPRITE_ALT);
            rubberBlast = atlas.findRegion(Constants.BLAST_RUBBER_SPRITE);
            metalShot = atlas.findRegion(Constants.SHOT_METAL_SPRITE);
            metalShotAlt = atlas.findRegion(Constants.SHOT_METAL_SPRITE_ALT);
            metalBlast = atlas.findRegion(Constants.BLAST_METAL_SPRITE);
            metalBlastAlt = atlas.findRegion(Constants.BLAST_METAL_SPRITE_ALT);
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

    public class LadderAssets {

        public final AtlasRegion ladder;


        public LadderAssets(TextureAtlas atlas) {
            ladder = atlas.findRegion(Constants.LADDER_SPRITE);
        }
    }

    public class PillarAssets {

        public final AtlasRegion pillar;

        public PillarAssets(TextureAtlas atlas) {
            pillar = atlas.findRegion(Constants.PILLAR_SPRITE);
        }
    }

    public class SlickAssets {

        public final Animation slick;

        public SlickAssets(TextureAtlas atlas) {
            Array<AtlasRegion> slickRegions = new Array<AtlasRegion>();
            slickRegions.add(atlas.findRegion(Constants.SLICK_SPRITE_1));
            slickRegions.add(atlas.findRegion(Constants.SLICK_SPRITE_2));

            slick = new Animation(Constants.SLICK_DURATION / slickRegions.size,
                    slickRegions, PlayMode.NORMAL);
        }
    }

    public class TreadmillAssets {

        public final Animation treadmill;

        public TreadmillAssets(TextureAtlas atlas) {
            Array<AtlasRegion> treadmillRegions = new Array<AtlasRegion>();
            treadmillRegions.add(atlas.findRegion(Constants.TREADMILL_SPRITE_1));
            treadmillRegions.add(atlas.findRegion(Constants.TREADMILL_SPRITE_2));

            treadmill = new Animation(Constants.TREADMILL_DURATION / treadmillRegions.size,
                    treadmillRegions, PlayMode.NORMAL);
        }
    }

    public class SpringAssets {

        public final Animation spring;

        public SpringAssets(TextureAtlas atlas) {
            Array<AtlasRegion> springRegions = new Array<AtlasRegion>();
            springRegions.add(atlas.findRegion(Constants.SPRING_SPRITE_1));
            springRegions.add(atlas.findRegion(Constants.SPRING_SPRITE_2));
            springRegions.add(atlas.findRegion(Constants.SPRING_SPRITE_3));

            spring = new Animation(Constants.SPRING_DURATION / springRegions.size,
                    springRegions, PlayMode.LOOP_PINGPONG);
        }
    }

    public class SinkAssets {

        public final Animation sink;

        public SinkAssets(TextureAtlas atlas) {
            Array<AtlasRegion> sinkRegions = new Array<AtlasRegion>();
            sinkRegions.add(atlas.findRegion(Constants.SINK_SPRITE_1));
            sinkRegions.add(atlas.findRegion(Constants.SINK_SPRITE_2));

            sink = new Animation(Constants.SINK_DURATION / sinkRegions.size,
                    sinkRegions, PlayMode.NORMAL);
        }
    }

    public class ZoombaAssets {

        public final AtlasRegion zoomba;

        public ZoombaAssets(TextureAtlas atlas) {
            zoomba = atlas.findRegion(Constants.ZOOMBA_SPRITE);
        }
    }
    
    public class FieryZoombaAssets {

        public final Animation fieryZoomba;

        public FieryZoombaAssets(TextureAtlas atlas) {
            Array<AtlasRegion> fieryZoombaRegions = new Array<AtlasRegion>();
            fieryZoombaRegions.add(atlas.findRegion(Constants.FIERYZOOMBA_SPRITE_1));
            fieryZoombaRegions.add(atlas.findRegion(Constants.FIERYZOOMBA_SPRITE_2));

            fieryZoomba = new Animation(Constants.FLAME_DURATION / fieryZoombaRegions.size,
                    fieryZoombaRegions, PlayMode.NORMAL);
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

        public final Animation sharpZoomba;

        public SharpZoombaAssets(TextureAtlas atlas) {
            Array<AtlasRegion> sharpZoombaRegions = new Array<AtlasRegion>();
            sharpZoombaRegions.add(atlas.findRegion(Constants.SHARPZOOMBA_SPRITE_1));
            sharpZoombaRegions.add(atlas.findRegion(Constants.SHARPZOOMBA_SPRITE_2));

            sharpZoomba = new Animation(Constants.SPIKE_DURATION / sharpZoombaRegions.size,
                    sharpZoombaRegions, PlayMode.NORMAL);
        }
    }

    public class SwoopaAssets {

        public final AtlasRegion swoopa;

        public SwoopaAssets(TextureAtlas atlas) {
            swoopa = atlas.findRegion(Constants.SWOOPA_SPRITE);
        }
    }

    public class FierySwoopaAssets {

        public final Animation fierySwoopa;

        public FierySwoopaAssets(TextureAtlas atlas) {
            Array<AtlasRegion> fierySwoopaRegions = new Array<AtlasRegion>();
            fierySwoopaRegions.add(atlas.findRegion(Constants.FIERYSWOOPA_SPRITE_1));
            fierySwoopaRegions.add(atlas.findRegion(Constants.FIERYSWOOPA_SPRITE_2));

            fierySwoopa = new Animation(Constants.FLAME_DURATION / fierySwoopaRegions.size,
                    fierySwoopaRegions, PlayMode.NORMAL);
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

        public final Animation sharpSwoopa;

        public SharpSwoopaAssets(TextureAtlas atlas) {
            Array<AtlasRegion> sharpSwoopaRegions = new Array<AtlasRegion>();
            sharpSwoopaRegions.add(atlas.findRegion(Constants.SHARPSWOOPA_SPRITE_1));
            sharpSwoopaRegions.add(atlas.findRegion(Constants.SHARPSWOOPA_SPRITE_2));

            sharpSwoopa = new Animation(Constants.SPIKE_DURATION / sharpSwoopaRegions.size,
                    sharpSwoopaRegions, PlayMode.NORMAL);
        }
    }

    public class SpikeAssets {

        public final Animation spike;

        public SpikeAssets(TextureAtlas atlas) {
            Array<AtlasRegion> spikeRegions = new Array<AtlasRegion>();
            spikeRegions.add(atlas.findRegion(Constants.SPIKE_SPRITE_1));
            spikeRegions.add(atlas.findRegion(Constants.SPIKE_SPRITE_2));

            spike = new Animation(Constants.SPIKE_DURATION / spikeRegions.size,
                    spikeRegions, PlayMode.NORMAL);
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
        public final AtlasRegion turboPowerup;

        public PowerupAssets(TextureAtlas atlas) {
            ammoPowerup = atlas.findRegion(Constants.AMMO_POWERUP_SPRITE);
            healthPowerup = atlas.findRegion(Constants.HEALTH_POWERUP_SPRITE);
            turboPowerup = atlas.findRegion(Constants.TURBO_POWERUP_SPRITE);
            
        }
    }

    public class OnscreenControlsAssets {

        public final AtlasRegion right;
        public final AtlasRegion left;
        public final AtlasRegion up;
        public final AtlasRegion down;
        public final AtlasRegion center;
        public final AtlasRegion shoot;
        public final AtlasRegion blast;
        public final AtlasRegion jump;
        public final AtlasRegion hover;
        public final AtlasRegion ricochet;
        public final AtlasRegion pause;
        public final AtlasRegion selectionCursor;

        public OnscreenControlsAssets(TextureAtlas atlas) {
            right = atlas.findRegion(Constants.RIGHT_BUTTON);
            left = atlas.findRegion(Constants.LEFT_BUTTON);
            up = atlas.findRegion(Constants.UP_BUTTON);
            down = atlas.findRegion(Constants.DOWN_BUTTON);
            center = atlas.findRegion(Constants.CENTER_BUTTON);
            shoot = atlas.findRegion(Constants.SHOOT_BUTTON);
            blast = atlas.findRegion(Constants.BLAST_BUTTON);
            jump = atlas.findRegion(Constants.JUMP_BUTTON);
            hover = atlas.findRegion(Constants.HOVER_BUTTON);
            ricochet = atlas.findRegion(Constants.RICOCHET_BUTTON);
            pause = atlas.findRegion(Constants.PAUSE_BUTTON);
            selectionCursor = atlas.findRegion(Constants.SELECTION_CURSOR);
        }
    }


    public class HudAssets {

        public final AtlasRegion shoot;
        public final AtlasRegion blast;
        public final AtlasRegion jump;
        public final AtlasRegion hover;
        public final AtlasRegion ricochet;
        public final AtlasRegion dash;
        public final AtlasRegion life;

        public HudAssets(TextureAtlas atlas) {
            shoot = atlas.findRegion(Constants.SHOOT_ICON);
            blast = atlas.findRegion(Constants.BLAST_ICON);
            jump = atlas.findRegion(Constants.JUMP_ICON);
            hover = atlas.findRegion(Constants.HOVER_ICON);
            ricochet = atlas.findRegion(Constants.RICOCHET_ICON);
            dash = atlas.findRegion(Constants.DASH_ICON);
            life = atlas.findRegion(Constants.LIFE_ICON);
        }
    }

    // Getters
    public final GigaGalAssets getGigaGalAssets() { return gigaGalAssets; }
    public final PlatformAssets getPlatformAssets() { return platformAssets; }
    public final CannonAssets getCannonAssets() { return cannonAssets; }
    public final PillarAssets getPillarAssets() { return pillarAssets; }
    public final LadderAssets getLadderAssets() { return ladderAssets; }
    public final SlickAssets getSlickAssets() { return slickAssets; }
    public final TreadmillAssets getTreadmillAssets() { return treadmillAssets; }
    public final SpringAssets getSpringAssets() { return springAssets; }
    public final SinkAssets getSinkAssets() { return sinkAssets; }
    public final AmmoAssets getAmmoAssets() { return ammoAssets; }
    public final ZoombaAssets getZoombaAssets() { return zoombaAssets; }
    public final FieryZoombaAssets getFieryZoombaAssets() { return fieryZoombaAssets; }
    public final GushingZoombaAssets getGushingZoombaAssets() { return gushingZoombaAssets; }
    public final ChargedZoombaAssets getChargedZoombaAssets() { return chargedZoombaAssets; }
    public final WhirlingZoombaAssets getWhirlingZoombaAssets() { return whirlingZoombaAssets; }
    public final SharpZoombaAssets getSharpZoombaAssets() { return sharpZoombaAssets; }
    public final SwoopaAssets getSwoopaAssets() { return swoopaAssets; }
    public final FierySwoopaAssets getFierySwoopaAssets() { return fierySwoopaAssets; }
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
    public final HudAssets getHudAssets() { return hudAssets; }
}

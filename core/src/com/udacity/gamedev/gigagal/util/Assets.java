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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private LiftAssets liftAssets;
    private LadderAssets ladderAssets;
    private VinesAssets vinesAssets;
    private RopeAssets ropeAssets;
    private PoleAssets poleAssets;
    private SlickAssets slickAssets;
    private TreadmillAssets treadmillAssets;
    private SpringAssets springAssets;
    private SinkAssets sinkAssets;
    private IceAssets iceAssets;
    private CoalsAssets coalsAssets;
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
    private OrbenAssets orbenAssets;
    private RollenAssets rollenAssets;
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
        liftAssets = new LiftAssets(atlas);
        ladderAssets = new LadderAssets(atlas);
        vinesAssets = new VinesAssets(atlas);
        ropeAssets = new RopeAssets(atlas);
        poleAssets = new PoleAssets(atlas);
        slickAssets = new SlickAssets(atlas);
        iceAssets = new IceAssets(atlas);
        treadmillAssets = new TreadmillAssets(atlas);
        springAssets = new SpringAssets(atlas);
        sinkAssets = new SinkAssets(atlas);
        coalsAssets = new CoalsAssets(atlas);
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
        orbenAssets = new OrbenAssets(atlas);
        rollenAssets = new RollenAssets(atlas);
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
        public final AtlasRegion recoilLeft;
        public final AtlasRegion recoilRight;
        public final AtlasRegion fallLeft;
        public final AtlasRegion fallRight;
        public final AtlasRegion lookupStandLeft;
        public final AtlasRegion lookupStandRight;
        public final AtlasRegion lookdownStandLeft;
        public final AtlasRegion lookdownStandRight;
        public final AtlasRegion lookupFallLeft;
        public final AtlasRegion lookupFallRight;
        public final AtlasRegion lookdownFallLeft;
        public final AtlasRegion lookdownFallRight;
        public final AtlasRegion dashLeft;
        public final AtlasRegion dashRight;
        public final AtlasRegion ricochetLeft;
        public final AtlasRegion ricochetRight;
        public final Animation hoverLeft;
        public final Animation hoverRight;
        public final Animation lookupHoverLeft;
        public final Animation lookupHoverRight;
        public final Animation lookdownHoverLeft;
        public final Animation lookdownHoverRight;
        public final Animation strideLeft;
        public final Animation strideRight;
        public final Animation climb;

        public GigaGalAssets(TextureAtlas atlas) {
            standLeft = atlas.findRegion(Constants.STAND_LEFT);
            standRight = atlas.findRegion(Constants.STAND_RIGHT);
            recoilLeft = atlas.findRegion(Constants.RECOIL_LEFT);
            recoilRight = atlas.findRegion(Constants.RECOIL_RIGHT);
            fallLeft = atlas.findRegion(Constants.FALL_LEFT);
            fallRight = atlas.findRegion(Constants.FALL_RIGHT);
            lookupStandLeft = atlas.findRegion(Constants.LOOKUP_STAND_LEFT);
            lookupStandRight = atlas.findRegion(Constants.LOOKUP_STAND_RIGHT);
            lookdownStandLeft = atlas.findRegion(Constants.LOOKDOWN_STAND_LEFT);
            lookdownStandRight = atlas.findRegion(Constants.LOOKDOWN_STAND_RIGHT);
            lookupFallLeft = atlas.findRegion(Constants.LOOKUP_FALL_LEFT);
            lookupFallRight = atlas.findRegion(Constants.LOOKUP_FALL_RIGHT);
            lookdownFallLeft = atlas.findRegion(Constants.LOOKDOWN_FALL_LEFT);
            lookdownFallRight = atlas.findRegion(Constants.LOOKDOWN_FALL_RIGHT);
            dashLeft = atlas.findRegion(Constants.STRIDE_LEFT_2);
            dashRight = atlas.findRegion(Constants.STRIDE_RIGHT_2);
            ricochetLeft = atlas.findRegion(Constants.RICOCHET_LEFT);
            ricochetRight = atlas.findRegion(Constants.RICOCHET_RIGHT);
            
            Array<AtlasRegion> hoverLeftFrames = new Array<AtlasRegion>();
            hoverLeftFrames.add(atlas.findRegion(Constants.HOVER_LEFT_1));
            hoverLeftFrames.add(atlas.findRegion(Constants.HOVER_LEFT_2));
            hoverLeft = new Animation(Constants.HOVER_LOOP_DURATION, hoverLeftFrames, PlayMode.LOOP);

            Array<AtlasRegion> hoverRightFrames = new Array<AtlasRegion>();
            hoverRightFrames.add(atlas.findRegion(Constants.HOVER_RIGHT_1));
            hoverRightFrames.add(atlas.findRegion(Constants.HOVER_RIGHT_2));
            hoverRight = new Animation(Constants.HOVER_LOOP_DURATION, hoverRightFrames, PlayMode.LOOP);

            Array<AtlasRegion> strideLeftFrames = new Array<AtlasRegion>();
            strideLeftFrames.add(atlas.findRegion(Constants.STRIDE_LEFT_2));
            strideLeftFrames.add(atlas.findRegion(Constants.STRIDE_LEFT_1));
            strideLeftFrames.add(atlas.findRegion(Constants.STRIDE_LEFT_2));
            strideLeftFrames.add(atlas.findRegion(Constants.STRIDE_LEFT_3));
            strideLeft = new Animation(Constants.STRIDE_LOOP_DURATION, strideLeftFrames, PlayMode.LOOP);

            Array<AtlasRegion> strideRightFrames = new Array<AtlasRegion>();
            strideRightFrames.add(atlas.findRegion(Constants.STRIDE_RIGHT_2));
            strideRightFrames.add(atlas.findRegion(Constants.STRIDE_RIGHT_1));
            strideRightFrames.add(atlas.findRegion(Constants.STRIDE_RIGHT_2));
            strideRightFrames.add(atlas.findRegion(Constants.STRIDE_RIGHT_3));
            strideRight = new Animation(Constants.STRIDE_LOOP_DURATION, strideRightFrames, PlayMode.LOOP);

            Array<AtlasRegion> climbFrames = new Array<AtlasRegion>();
            climbFrames.add(atlas.findRegion(Constants.CLIMB_1));
            climbFrames.add(atlas.findRegion(Constants.CLIMB_2));
            climb = new Animation(Constants.CLIMB_LOOP_DURATION, climbFrames, PlayMode.LOOP);

            Array<AtlasRegion> lookupHoverLeftFrames = new Array<AtlasRegion>();
            lookupHoverLeftFrames.add(atlas.findRegion(Constants.LOOKUP_HOVER_LEFT_1));
            lookupHoverLeftFrames.add(atlas.findRegion(Constants.LOOKUP_HOVER_LEFT_2));
            lookupHoverLeft = new Animation(Constants.HOVER_LOOP_DURATION, lookupHoverLeftFrames, PlayMode.LOOP);

            Array<AtlasRegion> lookupHoverRightFrames = new Array<AtlasRegion>();
            lookupHoverRightFrames.add(atlas.findRegion(Constants.LOOKUP_HOVER_RIGHT_1));
            lookupHoverRightFrames.add(atlas.findRegion(Constants.LOOKUP_HOVER_RIGHT_2));
            lookupHoverRight = new Animation(Constants.HOVER_LOOP_DURATION, lookupHoverRightFrames, PlayMode.LOOP);

            Array<AtlasRegion> lookdownHoverLeftFrames = new Array<AtlasRegion>();
            lookdownHoverLeftFrames.add(atlas.findRegion(Constants.LOOKDOWN_HOVER_LEFT_1));
            lookdownHoverLeftFrames.add(atlas.findRegion(Constants.LOOKDOWN_HOVER_LEFT_2));
            lookdownHoverLeft = new Animation(Constants.HOVER_LOOP_DURATION, lookdownHoverLeftFrames, PlayMode.LOOP);

            Array<AtlasRegion> lookdownHoverRightFrames = new Array<AtlasRegion>();
            lookdownHoverRightFrames.add(atlas.findRegion(Constants.LOOKDOWN_HOVER_RIGHT_1));
            lookdownHoverRightFrames.add(atlas.findRegion(Constants.LOOKDOWN_HOVER_RIGHT_2));
            lookdownHoverRight = new Animation(Constants.HOVER_LOOP_DURATION, lookdownHoverRightFrames, PlayMode.LOOP);
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

    public class LiftAssets {

        public final AtlasRegion lift;

        public LiftAssets(TextureAtlas atlas) {
            lift = atlas.findRegion(Constants.LIFT_SPRITE);
        }
    }

    public class PillarAssets {

        public final AtlasRegion pillar;

        public PillarAssets(TextureAtlas atlas) {
            pillar = atlas.findRegion(Constants.PILLAR_SPRITE);
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

        public final NinePatch ladderNinePatch;

        public LadderAssets(TextureAtlas atlas) {
            AtlasRegion region = atlas.findRegion(Constants.LADDER_SPRITE);
            int edge = Constants.LADDER_EDGE;
            ladderNinePatch = new NinePatch(region, edge, edge, edge, edge);
        }
    }

    public class VinesAssets {
    
        public final AtlasRegion vines;
    
        public VinesAssets(TextureAtlas atlas) {
            vines = atlas.findRegion(Constants.VINES_SPRITE);
        }
    }

    public class RopeAssets {

        public final AtlasRegion rope;

        public RopeAssets(TextureAtlas atlas) {
            rope = atlas.findRegion(Constants.ROPE_SPRITE);
        }
    }

    public class PoleAssets {

        public final Animation pole;

        public PoleAssets(TextureAtlas atlas) {
            Array<AtlasRegion> poleRegions = new Array<AtlasRegion>();
            poleRegions.add(atlas.findRegion(Constants.POLE_SPRITE_1));
            poleRegions.add(atlas.findRegion(Constants.POLE_SPRITE_2));

            pole = new Animation(Constants.POLE_DURATION / poleRegions.size,
                    poleRegions, PlayMode.NORMAL);
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

    public class IceAssets {

        public final Animation ice;

        public IceAssets(TextureAtlas atlas) {
            Array<AtlasRegion> iceRegions = new Array<AtlasRegion>();
            iceRegions.add(atlas.findRegion(Constants.ICE_SPRITE_1));
            iceRegions.add(atlas.findRegion(Constants.ICE_SPRITE_2));

            ice = new Animation(Constants.ICE_DURATION / iceRegions.size,
                    iceRegions, PlayMode.NORMAL);
        }
    }

    public class TreadmillAssets {

        public final Animation treadmillRight;
        public final Animation treadmillLeft;

        public TreadmillAssets(TextureAtlas atlas) {
            Array<AtlasRegion> treadmillRegions = new Array<AtlasRegion>();
            treadmillRegions.add(atlas.findRegion(Constants.TREADMILL_1_RIGHT));
            treadmillRegions.add(atlas.findRegion(Constants.TREADMILL_2_RIGHT));

            treadmillRight = new Animation(Constants.TREADMILL_DURATION / treadmillRegions.size,
                    treadmillRegions, PlayMode.NORMAL);
            
            treadmillRegions.clear();

            treadmillRegions = new Array<AtlasRegion>();
            treadmillRegions.add(atlas.findRegion(Constants.TREADMILL_1_LEFT));
            treadmillRegions.add(atlas.findRegion(Constants.TREADMILL_2_LEFT));

            treadmillLeft = new Animation(Constants.TREADMILL_DURATION / treadmillRegions.size,
                    treadmillRegions, PlayMode.NORMAL);
        }
    }

    public class SpringAssets {

        public final Animation load;
        public final Animation unload;

        public SpringAssets(TextureAtlas atlas) {
            Array<AtlasRegion> springRegions = new Array<AtlasRegion>();
            springRegions.add(atlas.findRegion(Constants.SPRING_SPRITE_1));
            springRegions.add(atlas.findRegion(Constants.SPRING_SPRITE_2));
            springRegions.add(atlas.findRegion(Constants.SPRING_SPRITE_3));

            load = new Animation(Constants.SPRING_LOAD_DURATION / springRegions.size,
                    springRegions, PlayMode.NORMAL);

            springRegions.add(atlas.findRegion(Constants.SPRING_SPRITE_4));

            unload = new Animation(Constants.SPRING_UNLOAD_DURATION / springRegions.size,
                    springRegions, PlayMode.REVERSED);
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

    public class CoalsAssets {

        public final Animation coals;

        public CoalsAssets(TextureAtlas atlas) {
            Array<AtlasRegion> coalsRegions = new Array<AtlasRegion>();
            coalsRegions.add(atlas.findRegion(Constants.COALS_SPRITE_1));
            coalsRegions.add(atlas.findRegion(Constants.COALS_SPRITE_2));

            coals = new Animation(Constants.COALS_DURATION / coalsRegions.size,
                    coalsRegions, PlayMode.NORMAL);
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
    
    public class OrbenAssets {

        public final TextureRegion dormantOrben;
        public final Animation chargedOrben;
        public final Animation fieryOrben;
        public final Animation gushingOrben;
        public final Animation sharpOrben;
        public final Animation whirlingOrben;

        public OrbenAssets(TextureAtlas atlas) {

            dormantOrben = atlas.findRegion(Constants.DORMANTORBEN_SPRITE);
            
            Array<AtlasRegion> chargedOrbenRegions = new Array<AtlasRegion>();
            chargedOrbenRegions.add(atlas.findRegion(Constants.CHARGEDORBEN_SPRITE_0));
            chargedOrbenRegions.add(atlas.findRegion(Constants.CHARGEDORBEN_SPRITE_1));
            chargedOrbenRegions.add(atlas.findRegion(Constants.CHARGEDORBEN_SPRITE_2));
    
            chargedOrben = new Animation(Constants.ORBEN_DURATION / Constants.ORBEN_REGIONS,
                      chargedOrbenRegions, PlayMode.NORMAL);
    
            Array<AtlasRegion> fieryOrbenRegions = new Array<AtlasRegion>();
            fieryOrbenRegions.add(atlas.findRegion(Constants.FIERYORBEN_SPRITE_0));
            fieryOrbenRegions.add(atlas.findRegion(Constants.FIERYORBEN_SPRITE_1));
            fieryOrbenRegions.add(atlas.findRegion(Constants.FIERYORBEN_SPRITE_2));
    
            fieryOrben = new Animation(Constants.ORBEN_DURATION / Constants.ORBEN_REGIONS,
                    fieryOrbenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gushingOrbenRegions = new Array<AtlasRegion>();
            gushingOrbenRegions.add(atlas.findRegion(Constants.GUSHINGORBEN_SPRITE_0));
            gushingOrbenRegions.add(atlas.findRegion(Constants.GUSHINGORBEN_SPRITE_1));
            gushingOrbenRegions.add(atlas.findRegion(Constants.GUSHINGORBEN_SPRITE_2));

            gushingOrben = new Animation(Constants.ORBEN_DURATION / Constants.ORBEN_REGIONS,
                    gushingOrbenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> sharpOrbenRegions = new Array<AtlasRegion>();
            sharpOrbenRegions.add(atlas.findRegion(Constants.SHARPORBEN_SPRITE_0));
            sharpOrbenRegions.add(atlas.findRegion(Constants.SHARPORBEN_SPRITE_1));
            sharpOrbenRegions.add(atlas.findRegion(Constants.SHARPORBEN_SPRITE_2));

            sharpOrben = new Animation(Constants.ORBEN_DURATION / Constants.ORBEN_REGIONS,
                    sharpOrbenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> whirlingOrbenRegions = new Array<AtlasRegion>();
            whirlingOrbenRegions.add(atlas.findRegion(Constants.WHIRLINGORBEN_SPRITE_0));
            whirlingOrbenRegions.add(atlas.findRegion(Constants.WHIRLINGORBEN_SPRITE_1));
            whirlingOrbenRegions.add(atlas.findRegion(Constants.WHIRLINGORBEN_SPRITE_2));

            whirlingOrben = new Animation(Constants.ORBEN_DURATION / Constants.ORBEN_REGIONS,
                    whirlingOrbenRegions, PlayMode.NORMAL);
        }
    }

    public class RollenAssets {

        public final Animation chargedRollen;
        public final Animation fieryRollen;
        public final Animation gushingRollen;
        public final Animation sharpRollen;
        public final Animation whirlingRollen;

        public RollenAssets(TextureAtlas atlas) {

            Array<AtlasRegion> chargedRollenRegions = new Array<AtlasRegion>();
            chargedRollenRegions.add(atlas.findRegion(Constants.CHARGEDROLLEN_SPRITE_4));
            chargedRollenRegions.add(atlas.findRegion(Constants.CHARGEDROLLEN_SPRITE_1));
            chargedRollenRegions.add(atlas.findRegion(Constants.CHARGEDROLLEN_SPRITE_2));
            chargedRollenRegions.add(atlas.findRegion(Constants.CHARGEDROLLEN_SPRITE_3));

            chargedRollen = new Animation(Constants.ROLLEN_DURATION / Constants.ROLLEN_REGIONS,
                    chargedRollenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> fieryRollenRegions = new Array<AtlasRegion>();
            fieryRollenRegions.add(atlas.findRegion(Constants.FIERYROLLEN_SPRITE_4));
            fieryRollenRegions.add(atlas.findRegion(Constants.FIERYROLLEN_SPRITE_1));
            fieryRollenRegions.add(atlas.findRegion(Constants.FIERYROLLEN_SPRITE_2));
            fieryRollenRegions.add(atlas.findRegion(Constants.FIERYROLLEN_SPRITE_3));

            fieryRollen = new Animation(Constants.ROLLEN_DURATION / Constants.ROLLEN_REGIONS,
                    fieryRollenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gushingRollenRegions = new Array<AtlasRegion>();
            gushingRollenRegions.add(atlas.findRegion(Constants.GUSHINGROLLEN_SPRITE_4));
            gushingRollenRegions.add(atlas.findRegion(Constants.GUSHINGROLLEN_SPRITE_1));
            gushingRollenRegions.add(atlas.findRegion(Constants.GUSHINGROLLEN_SPRITE_2));
            gushingRollenRegions.add(atlas.findRegion(Constants.GUSHINGROLLEN_SPRITE_3));

            gushingRollen = new Animation(Constants.ROLLEN_DURATION / Constants.ROLLEN_REGIONS,
                    gushingRollenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> sharpRollenRegions = new Array<AtlasRegion>();
            sharpRollenRegions.add(atlas.findRegion(Constants.SHARPROLLEN_SPRITE_4));
            sharpRollenRegions.add(atlas.findRegion(Constants.SHARPROLLEN_SPRITE_1));
            sharpRollenRegions.add(atlas.findRegion(Constants.SHARPROLLEN_SPRITE_2));
            sharpRollenRegions.add(atlas.findRegion(Constants.SHARPROLLEN_SPRITE_3));

            sharpRollen = new Animation(Constants.ROLLEN_DURATION / Constants.ROLLEN_REGIONS,
                    sharpRollenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> whirlingRollenRegions = new Array<AtlasRegion>();
            whirlingRollenRegions.add(atlas.findRegion(Constants.WHIRLINGROLLEN_SPRITE_4));
            whirlingRollenRegions.add(atlas.findRegion(Constants.WHIRLINGROLLEN_SPRITE_1));
            whirlingRollenRegions.add(atlas.findRegion(Constants.WHIRLINGROLLEN_SPRITE_2));
            whirlingRollenRegions.add(atlas.findRegion(Constants.WHIRLINGROLLEN_SPRITE_3));

            whirlingRollen = new Animation(Constants.ROLLEN_DURATION / Constants.ROLLEN_REGIONS,
                    whirlingRollenRegions, PlayMode.NORMAL);
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
        public final AtlasRegion climb;
        public final AtlasRegion dash;
        public final AtlasRegion life;

        public HudAssets(TextureAtlas atlas) {
            shoot = atlas.findRegion(Constants.SHOOT_ICON);
            blast = atlas.findRegion(Constants.BLAST_ICON);
            jump = atlas.findRegion(Constants.JUMP_ICON);
            hover = atlas.findRegion(Constants.HOVER_ICON);
            ricochet = atlas.findRegion(Constants.RICOCHET_ICON);
            climb = atlas.findRegion(Constants.CLIMB_ICON);
            dash = atlas.findRegion(Constants.DASH_ICON);
            life = atlas.findRegion(Constants.LIFE_ICON);
        }
    }

    // Getters
    public final GigaGalAssets getGigaGalAssets() { return gigaGalAssets; }
    public final PlatformAssets getPlatformAssets() { return platformAssets; }
    public final CannonAssets getCannonAssets() { return cannonAssets; }
    public final PillarAssets getPillarAssets() { return pillarAssets; }
    public final LiftAssets getLiftAssets() { return liftAssets; }
    public final LadderAssets getLadderAssets() { return ladderAssets; }
    public final VinesAssets getVinesAssets() { return vinesAssets; }
    public final RopeAssets getRopeAssets() { return ropeAssets; }
    public final PoleAssets getPoleAssets() { return poleAssets; }
    public final SlickAssets getSlickAssets() { return slickAssets; }
    public final IceAssets getIceAssets() { return iceAssets; }
    public final TreadmillAssets getTreadmillAssets() { return treadmillAssets; }
    public final SpringAssets getSpringAssets() { return springAssets; }
    public final SinkAssets getSinkAssets() { return sinkAssets; }
    public final CoalsAssets getCoalsAssets() { return coalsAssets; }
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
    public final OrbenAssets getOrbenAssets() { return orbenAssets; }
    public final RollenAssets getRollenAssets() { return rollenAssets; }
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

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
    private Enums.LevelName levelName;
    private GigaGalAssets gigaGalAssets;
    private BoxAssets boxAssets;
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
    private SwoopaAssets swoopaAssets;
    private OrbenAssets orbenAssets;
    private RollenAssets rollenAssets;
    private ProtrusionAssets protrusionAssets;
    private SuspensionAssets suspensionAssets;
    private ImpactAssets impactAssets;
    private PowerupAssets powerupAssets;
    private PortalAssets portalAssets;
    private OverlayAssets overlayAssets;
    private HudAssets hudAssets;
    private AssetManager assetManager;

    // non-instantiable; cannot be subclassed
    private Assets() {}

    // static factory
    public static Assets getInstance() { return INSTANCE; }

    public void setLevelName(Enums.LevelName levelName) { INSTANCE.levelName = levelName;}

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS, TextureAtlas.class);
        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS);
        gigaGalAssets = new GigaGalAssets(atlas);
        boxAssets = new BoxAssets(atlas);
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
        swoopaAssets = new SwoopaAssets(atlas);
        orbenAssets = new OrbenAssets(atlas);
        rollenAssets = new RollenAssets(atlas);
        protrusionAssets = new ProtrusionAssets(atlas);
        suspensionAssets = new SuspensionAssets(atlas);
        impactAssets = new ImpactAssets(atlas);
        powerupAssets = new PowerupAssets(atlas);
        portalAssets = new PortalAssets(atlas);
        overlayAssets = new OverlayAssets(atlas);
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
        public final AtlasRegion clingLeft;
        public final AtlasRegion clingRight;
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
            clingLeft = atlas.findRegion(Constants.CLING_LEFT);
            clingRight = atlas.findRegion(Constants.CLING_RIGHT);
            
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

    public class BoxAssets {

        public final NinePatch boxNinePatch;
        public BoxAssets(TextureAtlas atlas) {
            AtlasRegion region;
            switch (levelName) {
                case UNINTELLIGIBLE: region = atlas.findRegion(Constants.RED_BOX_SPRITE); break;
                case MECHANICAL: region = atlas.findRegion(Constants.GREY_BOX_SPRITE); break;
                case ELECTROMAGNETIC: region = atlas.findRegion(Constants.BLACK_BOX_SPRITE); break;
                case THERMAL: region = atlas.findRegion(Constants.YELLOW_BOX_SPRITE); break;
                case NUCLEAR: region = atlas.findRegion(Constants.BLUE_BOX_SPRITE); break;
                case MYSTERIOUS: region = atlas.findRegion(Constants.CLEAR_BOX_SPRITE); break;
                case FINAL: region = atlas.findRegion(Constants.MAGENTA_BOX_SPRITE); break;
                default: region = atlas.findRegion(Constants.BOX_SPRITE);
            }

            int edge = Constants.BOX_EDGE;
            boxNinePatch = new NinePatch(region, edge, edge, edge, edge);
        }
    }

    public class AmmoAssets {

        public final AtlasRegion nativeShot;
        public final AtlasRegion nativeBlast;
        public final AtlasRegion gasShot;
        public final AtlasRegion gasBlast;
        public final AtlasRegion liquidShot;
        public final AtlasRegion liquidBlast;
        public final AtlasRegion plasmaShot;
        public final AtlasRegion plasmaBlast;
        public final AtlasRegion polymerShot;
        public final AtlasRegion polymerBlast;
        public final AtlasRegion solidShot;
        public final AtlasRegion solidBlast;
        public final AtlasRegion psychicShot;
        public final AtlasRegion psychicBlast;

        public AmmoAssets(TextureAtlas atlas) {
            nativeShot = atlas.findRegion(Constants.SHOT_NATIVE_SPRITE);
            nativeBlast = atlas.findRegion(Constants.BLAST_NATIVE_SPRITE);
            gasShot = atlas.findRegion(Constants.SHOT_GAS_SPRITE);
            gasBlast = atlas.findRegion(Constants.BLAST_GAS_SPRITE);
            liquidShot = atlas.findRegion(Constants.SHOT_LIQUID_SPRITE);
            liquidBlast = atlas.findRegion(Constants.BLAST_LIQUID_SPRITE);
            plasmaShot = atlas.findRegion(Constants.SHOT_PLASMA_SPRITE);
            plasmaBlast = atlas.findRegion(Constants.BLAST_PLASMA_SPRITE);
            polymerShot = atlas.findRegion(Constants.SHOT_POLYMER_SPRITE);
            polymerBlast = atlas.findRegion(Constants.BLAST_POLYMER_SPRITE);
            solidShot = atlas.findRegion(Constants.SHOT_SOLID_SPRITE);
            solidBlast = atlas.findRegion(Constants.BLAST_SOLID_SPRITE);
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

        public final AtlasRegion yCannon;
        public final AtlasRegion xCannon;
        
        public CannonAssets(TextureAtlas atlas) {
            yCannon = atlas.findRegion(Constants.Y_CANNON_SPRITE);
            xCannon = atlas.findRegion(Constants.X_CANNON_SPRITE);
        }
    }

    public class LadderAssets {

        public final NinePatch ladderNinePatch;

        public LadderAssets(TextureAtlas atlas) {
            AtlasRegion region = atlas.findRegion(Constants.LADDER_SPRITE);
            ladderNinePatch = new NinePatch(region, Constants.LADDER_X_EDGE, Constants.LADDER_X_EDGE, Constants.LADDER_Y_EDGE, Constants.LADDER_Y_EDGE);
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

        public final Animation loaded;
        public final Animation unloaded;

        public SpringAssets(TextureAtlas atlas) {
            Array<AtlasRegion> springRegions = new Array<AtlasRegion>();
            springRegions.add(atlas.findRegion(Constants.SPRING_SPRITE_1));
            springRegions.add(atlas.findRegion(Constants.SPRING_SPRITE_2));
            springRegions.add(atlas.findRegion(Constants.SPRING_SPRITE_3));

            loaded = new Animation(Constants.SPRING_LOAD_DURATION / springRegions.size,
                    springRegions, PlayMode.NORMAL);

            springRegions.add(atlas.findRegion(Constants.SPRING_SPRITE_4));

            unloaded = new Animation(Constants.SPRING_UNLOAD_DURATION / springRegions.size,
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
        public final Animation fieryZoomba;
        public final Animation gushingZoomba;
        public final Animation chargedZoomba;
        public final Animation whirlingZoomba;
        public final Animation sharpZoomba;

        public ZoombaAssets(TextureAtlas atlas) {
            zoomba = atlas.findRegion(Constants.ZOOMBA_SPRITE);

            Array<AtlasRegion> fieryZoombaRegions = new Array<AtlasRegion>();
            fieryZoombaRegions.add(atlas.findRegion(Constants.FIERYZOOMBA_SPRITE_1));
            fieryZoombaRegions.add(atlas.findRegion(Constants.FIERYZOOMBA_SPRITE_2));

            fieryZoomba = new Animation(Constants.FLAME_DURATION / fieryZoombaRegions.size,
                    fieryZoombaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gushingZoombaRegions = new Array<AtlasRegion>();
            gushingZoombaRegions.add(atlas.findRegion(Constants.GUSHINGZOOMBA_SPRITE_1));
            gushingZoombaRegions.add(atlas.findRegion(Constants.GUSHINGZOOMBA_SPRITE_2));

            gushingZoomba = new Animation(Constants.GEISER_DURATION / gushingZoombaRegions.size,
                    gushingZoombaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> chargedZoombaRegions = new Array<AtlasRegion>();
            chargedZoombaRegions.add(atlas.findRegion(Constants.CHARGEDZOOMBA_SPRITE_1));
            chargedZoombaRegions.add(atlas.findRegion(Constants.CHARGEDZOOMBA_SPRITE_2));

            chargedZoomba = new Animation(Constants.COIL_DURATION / chargedZoombaRegions.size,
                    chargedZoombaRegions, PlayMode.NORMAL);


            Array<AtlasRegion> whirlingZoombaRegions = new Array<AtlasRegion>();
            whirlingZoombaRegions.add(atlas.findRegion(Constants.WHIRLINGZOOMBA_SPRITE_1));
            whirlingZoombaRegions.add(atlas.findRegion(Constants.WHIRLINGZOOMBA_SPRITE_2));

            whirlingZoomba = new Animation(Constants.WHEEL_DURATION / whirlingZoombaRegions.size,
                    whirlingZoombaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> sharpZoombaRegions = new Array<AtlasRegion>();
            sharpZoombaRegions.add(atlas.findRegion(Constants.SHARPZOOMBA_SPRITE_1));
            sharpZoombaRegions.add(atlas.findRegion(Constants.SHARPZOOMBA_SPRITE_2));

            sharpZoomba = new Animation(Constants.SPIKE_DURATION / sharpZoombaRegions.size,
                    sharpZoombaRegions, PlayMode.NORMAL);
        }
    }

    public class SwoopaAssets {

        public final AtlasRegion swoopa;
        public final Animation fierySwoopa;
        public final Animation gushingSwoopa;
        public final Animation chargedSwoopa;
        public final Animation whirlingSwoopa;
        public final Animation sharpSwoopa;

        public SwoopaAssets(TextureAtlas atlas) {
            swoopa = atlas.findRegion(Constants.SWOOPA_SPRITE);

            Array<AtlasRegion> fierySwoopaRegions = new Array<AtlasRegion>();
            fierySwoopaRegions.add(atlas.findRegion(Constants.FIERYSWOOPA_SPRITE_1));
            fierySwoopaRegions.add(atlas.findRegion(Constants.FIERYSWOOPA_SPRITE_2));

            fierySwoopa = new Animation(Constants.FLAME_DURATION / fierySwoopaRegions.size,
                    fierySwoopaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gushingSwoopaRegions = new Array<AtlasRegion>();
            gushingSwoopaRegions.add(atlas.findRegion(Constants.GUSHINGSWOOPA_SPRITE_1));
            gushingSwoopaRegions.add(atlas.findRegion(Constants.GUSHINGSWOOPA_SPRITE_2));

            gushingSwoopa = new Animation(Constants.GEISER_DURATION / gushingSwoopaRegions.size,
                    gushingSwoopaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> chargedSwoopaRegions = new Array<AtlasRegion>();
            chargedSwoopaRegions.add(atlas.findRegion(Constants.CHARGEDSWOOPA_SPRITE_1));
            chargedSwoopaRegions.add(atlas.findRegion(Constants.CHARGEDSWOOPA_SPRITE_2));

            chargedSwoopa = new Animation(Constants.COIL_DURATION / chargedSwoopaRegions.size,
                    chargedSwoopaRegions, PlayMode.NORMAL);


            Array<AtlasRegion> whirlingSwoopaRegions = new Array<AtlasRegion>();
            whirlingSwoopaRegions.add(atlas.findRegion(Constants.WHIRLINGSWOOPA_SPRITE_1));
            whirlingSwoopaRegions.add(atlas.findRegion(Constants.WHIRLINGSWOOPA_SPRITE_2));

            whirlingSwoopa = new Animation(Constants.WHEEL_DURATION / whirlingSwoopaRegions.size,
                    whirlingSwoopaRegions, PlayMode.NORMAL);

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
            chargedRollenRegions.add(atlas.findRegion(Constants.CHARGEDROLLEN_SPRITE_1));
            chargedRollenRegions.add(atlas.findRegion(Constants.CHARGEDROLLEN_SPRITE_2));
            chargedRollenRegions.add(atlas.findRegion(Constants.CHARGEDROLLEN_SPRITE_3));
            chargedRollenRegions.add(atlas.findRegion(Constants.CHARGEDROLLEN_SPRITE_4));
            chargedRollen = new Animation(Constants.ROLLEN_DURATION / Constants.ROLLEN_REGIONS, chargedRollenRegions, PlayMode.NORMAL);
            
            Array<AtlasRegion> fieryRollenRegions = new Array<AtlasRegion>();
            fieryRollenRegions.add(atlas.findRegion(Constants.FIERYROLLEN_SPRITE_1));
            fieryRollenRegions.add(atlas.findRegion(Constants.FIERYROLLEN_SPRITE_2));
            fieryRollenRegions.add(atlas.findRegion(Constants.FIERYROLLEN_SPRITE_3));
            fieryRollenRegions.add(atlas.findRegion(Constants.FIERYROLLEN_SPRITE_4));
            fieryRollen = new Animation(Constants.ROLLEN_DURATION / Constants.ROLLEN_REGIONS, fieryRollenRegions, PlayMode.NORMAL);
            
            Array<AtlasRegion> gushingRollenRegions = new Array<AtlasRegion>();
            gushingRollenRegions.add(atlas.findRegion(Constants.GUSHINGROLLEN_SPRITE_1));
            gushingRollenRegions.add(atlas.findRegion(Constants.GUSHINGROLLEN_SPRITE_2));
            gushingRollenRegions.add(atlas.findRegion(Constants.GUSHINGROLLEN_SPRITE_3));
            gushingRollenRegions.add(atlas.findRegion(Constants.GUSHINGROLLEN_SPRITE_4));
            gushingRollen = new Animation(Constants.ROLLEN_DURATION / Constants.ROLLEN_REGIONS, gushingRollenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> sharpRollenRegions = new Array<AtlasRegion>();
            sharpRollenRegions.add(atlas.findRegion(Constants.SHARPROLLEN_SPRITE_1));
            sharpRollenRegions.add(atlas.findRegion(Constants.SHARPROLLEN_SPRITE_2));
            sharpRollenRegions.add(atlas.findRegion(Constants.SHARPROLLEN_SPRITE_3));
            sharpRollenRegions.add(atlas.findRegion(Constants.SHARPROLLEN_SPRITE_4));
            sharpRollen = new Animation(Constants.ROLLEN_DURATION / Constants.ROLLEN_REGIONS, sharpRollenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> whirlingRollenRegions = new Array<AtlasRegion>();
            whirlingRollenRegions.add(atlas.findRegion(Constants.WHIRLINGROLLEN_SPRITE_1));
            whirlingRollenRegions.add(atlas.findRegion(Constants.WHIRLINGROLLEN_SPRITE_2));
            whirlingRollenRegions.add(atlas.findRegion(Constants.WHIRLINGROLLEN_SPRITE_3));
            whirlingRollenRegions.add(atlas.findRegion(Constants.WHIRLINGROLLEN_SPRITE_4));
            whirlingRollen = new Animation(Constants.ROLLEN_DURATION / Constants.ROLLEN_REGIONS, whirlingRollenRegions, PlayMode.NORMAL);
        }
    }

    public class ProtrusionAssets {

        public final Animation spike;
        public final Animation geiser;
        public final Animation flame;
        public final Animation rod;
        public final Animation whirl;

        public ProtrusionAssets(TextureAtlas atlas) {

            Array<AtlasRegion> whirlRegions = new Array<AtlasRegion>();
            whirlRegions.add(atlas.findRegion(Constants.WHIRL_SPRITE_1));
            whirlRegions.add(atlas.findRegion(Constants.WHIRL_SPRITE_2));
            whirl = new Animation(Constants.WHIRL_DURATION /whirlRegions.size, whirlRegions, PlayMode.NORMAL);
            
            Array<AtlasRegion> rodRegions = new Array<AtlasRegion>();
            rodRegions.add(atlas.findRegion(Constants.ROD_SPRITE_1));
            rodRegions.add(atlas.findRegion(Constants.ROD_SPRITE_2));
            rod = new Animation(Constants.ROD_DURATION /rodRegions.size, rodRegions, PlayMode.NORMAL);

            Array<AtlasRegion> spikeRegions = new Array<AtlasRegion>();
            spikeRegions.add(atlas.findRegion(Constants.SPIKE_SPRITE_1));
            spikeRegions.add(atlas.findRegion(Constants.SPIKE_SPRITE_2));
            spike = new Animation(Constants.SPIKE_DURATION /spikeRegions.size, spikeRegions, PlayMode.NORMAL);

            Array<AtlasRegion> geiserRegions = new Array<AtlasRegion>();
            geiserRegions.add(atlas.findRegion(Constants.GEISER_SPRITE_1));
            geiserRegions.add(atlas.findRegion(Constants.GEISER_SPRITE_2));
            geiser = new Animation(Constants.GEISER_DURATION / geiserRegions.size, geiserRegions, PlayMode.NORMAL);

            Array<AtlasRegion> flameRegions = new Array<AtlasRegion>();
            flameRegions.add(atlas.findRegion(Constants.FLAME_SPRITE_1));
            flameRegions.add(atlas.findRegion(Constants.FLAME_SPRITE_2));
            flame = new Animation(Constants.FLAME_DURATION / flameRegions.size, flameRegions, PlayMode.NORMAL);
        }
    }

    public class SuspensionAssets {

        public final Animation burner;
        public final Animation sharp;
        public final Animation wheel;
        public final Animation lump;
        public final Animation coil;
        public final Animation vacuum;

        public SuspensionAssets(TextureAtlas atlas) {

            Array<AtlasRegion> burnerRegions = new Array<AtlasRegion>();
            burnerRegions.add(atlas.findRegion(Constants.BURNER_SPRITE_1));
            burnerRegions.add(atlas.findRegion(Constants.BURNER_SPRITE_2));
            burner = new Animation(Constants.BURNER_DURATION / burnerRegions.size, burnerRegions, PlayMode.NORMAL);

            Array<AtlasRegion> coilRegions = new Array<AtlasRegion>();
            coilRegions.add(atlas.findRegion(Constants.COIL_SPRITE_1));
            coilRegions.add(atlas.findRegion(Constants.COIL_SPRITE_2));
            coil = new Animation(Constants.COIL_DURATION / coilRegions.size, coilRegions, PlayMode.NORMAL);

            Array<AtlasRegion> lumpRegions = new Array<AtlasRegion>();
            lumpRegions.add(atlas.findRegion(Constants.LUMP_SPRITE_1));
            lumpRegions.add(atlas.findRegion(Constants.LUMP_SPRITE_2));
            lump = new Animation(Constants.LUMP_DURATION / lumpRegions.size, lumpRegions, PlayMode.NORMAL);

            Array<AtlasRegion> sharpRegions = new Array<AtlasRegion>();
            sharpRegions.add(atlas.findRegion(Constants.SHARP_SPRITE_1));
            sharpRegions.add(atlas.findRegion(Constants.SHARP_SPRITE_2));
            sharp = new Animation(Constants.SHARP_DURATION / sharpRegions.size, sharpRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gusherRegions = new Array<AtlasRegion>();
            gusherRegions.add(atlas.findRegion(Constants.WHEEL_SPRITE_1));
            gusherRegions.add(atlas.findRegion(Constants.WHEEL_SPRITE_2));
            wheel = new Animation(Constants.WHEEL_DURATION / gusherRegions.size, gusherRegions, PlayMode.NORMAL);

            Array<AtlasRegion> vacuumRegions = new Array<AtlasRegion>();
            vacuumRegions.add(atlas.findRegion(Constants.VACUUM_SPRITE_1));
            vacuumRegions.add(atlas.findRegion(Constants.VACUUM_SPRITE_2));
            vacuumRegions.add(atlas.findRegion(Constants.VACUUM_SPRITE_3));

            vacuum = new Animation(Constants.VACUUM_FRAME_DURATION, vacuumRegions, PlayMode.LOOP_PINGPONG);
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

    public class ImpactAssets {

        public final Animation impactPlasma;
        public final Animation impactGas;
        public final Animation impactLiquid;
        public final Animation impactSolid;
        public final Animation impactNative;

        public ImpactAssets(TextureAtlas atlas) {

            Array<AtlasRegion> impactPlasmaRegions = new Array<AtlasRegion>();
            impactPlasmaRegions.add(atlas.findRegion(Constants.IMPACT_PLASMA_LARGE));
            impactPlasmaRegions.add(atlas.findRegion(Constants.IMPACT_PLASMA_MEDIUM));
            impactPlasmaRegions.add(atlas.findRegion(Constants.IMPACT_PLASMA_SMALL));

            impactPlasma = new Animation(Constants.IMPACT_DURATION / impactPlasmaRegions.size,
                    impactPlasmaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> impactGasRegions = new Array<AtlasRegion>();
            impactGasRegions.add(atlas.findRegion(Constants.IMPACT_GAS_LARGE));
            impactGasRegions.add(atlas.findRegion(Constants.IMPACT_GAS_MEDIUM));
            impactGasRegions.add(atlas.findRegion(Constants.IMPACT_GAS_SMALL));

            impactGas = new Animation(Constants.IMPACT_DURATION / impactGasRegions.size,
                    impactGasRegions, PlayMode.NORMAL);

            Array<AtlasRegion> impactLiquidRegions = new Array<AtlasRegion>();
            impactLiquidRegions.add(atlas.findRegion(Constants.IMPACT_LIQUID_LARGE));
            impactLiquidRegions.add(atlas.findRegion(Constants.IMPACT_LIQUID_MEDIUM));
            impactLiquidRegions.add(atlas.findRegion(Constants.IMPACT_LIQUID_SMALL));

            impactLiquid = new Animation(Constants.IMPACT_DURATION / impactLiquidRegions.size,
                    impactLiquidRegions, PlayMode.NORMAL);

            Array<AtlasRegion> impactSolidRegions = new Array<AtlasRegion>();
            impactSolidRegions.add(atlas.findRegion(Constants.IMPACT_SOLID_LARGE));
            impactSolidRegions.add(atlas.findRegion(Constants.IMPACT_SOLID_MEDIUM));
            impactSolidRegions.add(atlas.findRegion(Constants.IMPACT_SOLID_SMALL));

            impactSolid = new Animation(Constants.IMPACT_DURATION / impactSolidRegions.size,
                    impactSolidRegions, PlayMode.NORMAL);

            Array<AtlasRegion> impactNativeRegions = new Array<AtlasRegion>();
            impactNativeRegions.add(atlas.findRegion(Constants.IMPACT_NATIVE_LARGE));
            impactNativeRegions.add(atlas.findRegion(Constants.IMPACT_NATIVE_MEDIUM));
            impactNativeRegions.add(atlas.findRegion(Constants.IMPACT_NATIVE_SMALL));

            impactNative = new Animation(Constants.IMPACT_DURATION / impactNativeRegions.size,
                    impactNativeRegions, PlayMode.NORMAL);
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

    public class OverlayAssets {

        public final AtlasRegion right;
        public final AtlasRegion left;
        public final AtlasRegion up;
        public final AtlasRegion down;
        public final AtlasRegion center;
        public final AtlasRegion shoot;
        public final AtlasRegion jump;
        public final AtlasRegion pause;
        public final AtlasRegion selectionCursor;
        public final AtlasRegion logo;

        public OverlayAssets(TextureAtlas atlas) {
            right = atlas.findRegion(Constants.RIGHT_BUTTON);
            left = atlas.findRegion(Constants.LEFT_BUTTON);
            up = atlas.findRegion(Constants.UP_BUTTON);
            down = atlas.findRegion(Constants.DOWN_BUTTON);
            center = atlas.findRegion(Constants.CENTER_BUTTON);
            shoot = atlas.findRegion(Constants.SHOOT_BUTTON);
            jump = atlas.findRegion(Constants.JUMP_BUTTON);
            pause = atlas.findRegion(Constants.PAUSE_BUTTON);
            selectionCursor = atlas.findRegion(Constants.SELECTION_CURSOR);
            logo = atlas.findRegion(Constants.LOGO);
        }
    }

    public class HudAssets {

        public final AtlasRegion shoot;
        public final AtlasRegion blast;
        public final AtlasRegion jump;
        public final AtlasRegion hover;
        public final AtlasRegion cling;
        public final AtlasRegion climb;
        public final AtlasRegion dash;
        public final AtlasRegion life;

        public HudAssets(TextureAtlas atlas) {
            shoot = atlas.findRegion(Constants.SHOOT_ICON);
            blast = atlas.findRegion(Constants.BLAST_ICON);
            jump = atlas.findRegion(Constants.JUMP_ICON);
            hover = atlas.findRegion(Constants.HOVER_ICON);
            cling = atlas.findRegion(Constants.CLING_ICON);
            climb = atlas.findRegion(Constants.CLIMB_ICON);
            dash = atlas.findRegion(Constants.DASH_ICON);
            life = atlas.findRegion(Constants.LIFE_ICON);
        }
    }

    // Getters
    public final GigaGalAssets getGigaGalAssets() { return gigaGalAssets; }
    public final BoxAssets getBoxAssets() { return boxAssets; }
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
    public final SwoopaAssets getSwoopaAssets() { return swoopaAssets; }
    public final OrbenAssets getOrbenAssets() { return orbenAssets; }
    public final RollenAssets getRollenAssets() { return rollenAssets; }
    public final ProtrusionAssets getProtrusionAssets() { return protrusionAssets; }
    public final SuspensionAssets getSuspensionAssets() { return suspensionAssets; }
    public final ImpactAssets getImpactAssets() { return impactAssets; }
    public final PowerupAssets getPowerupAssets() { return powerupAssets; }
    public final PortalAssets getPortalAssets() { return portalAssets; }
    public final OverlayAssets getOverlayAssets() { return overlayAssets; }
    public final HudAssets getHudAssets() { return hudAssets; }
}

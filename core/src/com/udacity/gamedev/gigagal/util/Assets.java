package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.udacity.gamedev.gigagal.entity.Boss;

// immutable singleton
public final class Assets implements AssetErrorListener {

    // fields class-instantiated save for tag, assetmanager and atlas
    public static final String TAG = Assets.class.getName();
    private static final Assets INSTANCE = new Assets();
    private AssetManager assetManager;
    private GigaGalAssets gigaGalAssets;
    private BoxAssets boxAssets;
    private GroundAssets groundAssets;
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
    private HudAssets hudAssets;
    private OverlayAssets overlayAssets;
    private SoundAssets soundAssets;
    private MusicAssets musicAssets;
    private FontAssets fontAssets;

    // cannot be subclassed
    private Assets() {}

    // static factory
    public static final Assets getInstance() { return INSTANCE; }

    public final void create() {
        this.assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS); // atlas packed upon gradle build; load all at once instead of individually
        assetManager.load(Constants.POWERUP_SOUND);
        assetManager.load(Constants.LEVEL_MUSIC);
        assetManager.load(Constants.BOSS_MUSIC);
        assetManager.finishLoading();
        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS);
        gigaGalAssets = new GigaGalAssets(atlas);
        boxAssets = new BoxAssets(atlas);
        groundAssets = new GroundAssets(atlas);
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
        hudAssets = new HudAssets(atlas);
        overlayAssets = new OverlayAssets(atlas);
        soundAssets = new SoundAssets();
        musicAssets = new MusicAssets();
//        fontAssets = new fontAssets(fonts);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset: " + asset.fileName, throwable);
    }

    public void dispose() {
        assetManager.clear(); // disposes all assets
    }

    public static final class GigaGalAssets {

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
        public final AtlasRegion graspLeft;
        public final AtlasRegion graspRight;
        public final Animation hoverLeft;
        public final Animation hoverRight;
        public final Animation lookupHoverLeft;
        public final Animation lookupHoverRight;
        public final Animation lookdownHoverLeft;
        public final Animation lookdownHoverRight;
        public final Animation strideLeft;
        public final Animation strideRight;
        public final Animation climb;

        private GigaGalAssets(TextureAtlas atlas) {
            standLeft = atlas.findRegion(Constants.STAND_LEFT);
            standRight = atlas.findRegion(Constants.STAND_RIGHT);
            recoilLeft = atlas.findRegion(Constants.RECOILING_LEFT);
            recoilRight = atlas.findRegion(Constants.RECOILING_RIGHT);
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
            graspLeft = atlas.findRegion(Constants.GRASP_LEFT);
            graspRight = atlas.findRegion(Constants.GRASP_RIGHT);
            
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

    public static final class BoxAssets {

        public final NinePatch redBox;
        public final NinePatch greyBox;
        public final NinePatch blackBox;
        public final NinePatch yellowBox;
        public final NinePatch blueBox;
        public final NinePatch clearBox;
        public final NinePatch magentaBox;
        public final NinePatch box;
        public final NinePatch breakableBox;

        private BoxAssets(TextureAtlas atlas) {
            int edge = Constants.BOX_EDGE;

            redBox = new NinePatch(atlas.findRegion(Constants.RED_BOX_SPRITE), edge, edge, edge, edge);
            greyBox = new NinePatch(atlas.findRegion(Constants.GREY_BOX_SPRITE), edge, edge, edge, edge);
            blackBox = new NinePatch(atlas.findRegion(Constants.BLACK_BOX_SPRITE), edge, edge, edge, edge);
            yellowBox = new NinePatch(atlas.findRegion(Constants.YELLOW_BOX_SPRITE), edge, edge, edge, edge);
            blueBox = new NinePatch(atlas.findRegion(Constants.BLUE_BOX_SPRITE), edge, edge, edge, edge);
            clearBox = new NinePatch(atlas.findRegion(Constants.CLEAR_BOX_SPRITE), edge, edge, edge, edge);
            magentaBox= new NinePatch(atlas.findRegion(Constants.MAGENTA_BOX_SPRITE), edge, edge, edge, edge);
            box = new NinePatch(atlas.findRegion(Constants.BOX_SPRITE), edge, edge, edge, edge);
            breakableBox = new NinePatch(atlas.findRegion(Constants.BREAKABLE_BOX_SPRITE), edge, edge, edge, edge);
        }
    }

    public static final class GroundAssets {
        
        public final NinePatch ladderNinePatch;
        public final AtlasRegion lift;
        public final AtlasRegion yCannon;
        public final AtlasRegion xCannon;
        public final AtlasRegion vines;
        public final AtlasRegion rope;
        public final AtlasRegion pillar;
        public final AtlasRegion pod;
        public final AtlasRegion inactiveChamber;
        public final AtlasRegion activeChamber;
        public final Animation chargedChamber;
        public final Animation knob;
        public final Animation pole;
        public final Animation slick;
        public final Animation ice;
        public final Animation treadmillRight;
        public final Animation treadmillLeft;
        public final Animation loadedSpring;
        public final Animation unloadedSpring;
        public final Animation tripOn;
        public final Animation tripOff;
        public final Animation activePod;
        public final Animation sink;
        public final Animation coals;
        public final Animation lava;


        private GroundAssets(TextureAtlas atlas) {

            pillar = atlas.findRegion(Constants.PILLAR_SPRITE);
            lift = atlas.findRegion(Constants.LIFT_SPRITE);
            yCannon = atlas.findRegion(Constants.Y_CANNON_SPRITE);
            xCannon = atlas.findRegion(Constants.X_CANNON_SPRITE);

            AtlasRegion region = atlas.findRegion(Constants.LADDER_SPRITE);
            ladderNinePatch = new NinePatch(region, Constants.LADDER_X_EDGE, Constants.LADDER_X_EDGE, Constants.LADDER_Y_EDGE, Constants.LADDER_Y_EDGE);

            vines = atlas.findRegion(Constants.VINES_SPRITE);
            rope = atlas.findRegion(Constants.ROPE_SPRITE);

            Array<AtlasRegion> poleRegions = new Array<AtlasRegion>();
            poleRegions.add(atlas.findRegion(Constants.POLE_SPRITE_1));
            poleRegions.add(atlas.findRegion(Constants.POLE_SPRITE_2));

            pole = new Animation(Constants.POLE_DURATION / poleRegions.size,
                    poleRegions, PlayMode.NORMAL);

            Array<AtlasRegion> knobRegions = new Array<AtlasRegion>();
            knobRegions.add(atlas.findRegion(Constants.KNOB_SPRITE_1));
            knobRegions.add(atlas.findRegion(Constants.KNOB_SPRITE_2));
            knob = new Animation(Constants.POLE_DURATION / knobRegions.size,
                    knobRegions, PlayMode.NORMAL);

            Array<AtlasRegion> slickRegions = new Array<AtlasRegion>();
            slickRegions.add(atlas.findRegion(Constants.SLICK_SPRITE_1));
            slickRegions.add(atlas.findRegion(Constants.SLICK_SPRITE_2));

            slick = new Animation(Constants.SLICK_DURATION / slickRegions.size,
                    slickRegions, PlayMode.NORMAL);

            Array<AtlasRegion> iceRegions = new Array<AtlasRegion>();
            iceRegions.add(atlas.findRegion(Constants.ICE_SPRITE_1));
            iceRegions.add(atlas.findRegion(Constants.ICE_SPRITE_2));

            ice = new Animation(Constants.ICE_DURATION / iceRegions.size,
                    iceRegions, PlayMode.NORMAL);

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

            Array<AtlasRegion> springRegions = new Array<AtlasRegion>();
            springRegions.add(atlas.findRegion(Constants.SPRING_SPRITE_1));
            springRegions.add(atlas.findRegion(Constants.SPRING_SPRITE_2));
            springRegions.add(atlas.findRegion(Constants.SPRING_SPRITE_3));

            loadedSpring = new Animation(Constants.SPRING_LOAD_DURATION / springRegions.size,
                    springRegions, PlayMode.NORMAL);

            springRegions.add(atlas.findRegion(Constants.SPRING_SPRITE_4));

            unloadedSpring = new Animation(Constants.SPRING_UNLOAD_DURATION / springRegions.size,
                    springRegions, PlayMode.REVERSED);


            Array<AtlasRegion> tripRegions = new Array<AtlasRegion>();
            tripRegions.add(atlas.findRegion(Constants.TRIP_SPRITE_1));
            tripRegions.add(atlas.findRegion(Constants.TRIP_SPRITE_2));
            tripRegions.add(atlas.findRegion(Constants.TRIP_SPRITE_3));
            tripRegions.add(atlas.findRegion(Constants.TRIP_SPRITE_4));

            tripOn = new Animation(Constants.TRIP_LOAD_DURATION / tripRegions.size,
                    tripRegions, PlayMode.NORMAL);


            tripOff = new Animation(Constants.TRIP_UNLOAD_DURATION / tripRegions.size,
                    tripRegions, PlayMode.REVERSED);

            Array<AtlasRegion> podRegions = new Array<AtlasRegion>();
            podRegions.add(atlas.findRegion(Constants.POD_SPRITE_1));
            podRegions.add(atlas.findRegion(Constants.POD_SPRITE_2));

            activePod = new Animation(Constants.POD_LOAD_DURATION / podRegions.size,
                    podRegions, PlayMode.LOOP);

            pod = atlas.findRegion(Constants.POD_SPRITE_3);

            Array<AtlasRegion> chamberRegions = new Array<AtlasRegion>();
            chamberRegions.add(atlas.findRegion(Constants.CHAMBER_SPRITE_1));
            chamberRegions.add(atlas.findRegion(Constants.CHAMBER_SPRITE_2));

            chargedChamber = new Animation(Constants.CHAMBER_LOAD_DURATION / chamberRegions.size,
                    chamberRegions, PlayMode.LOOP);

            inactiveChamber = atlas.findRegion(Constants.CHAMBER_SPRITE_3);

            activeChamber = atlas.findRegion(Constants.CHAMBER_SPRITE);

            Array<AtlasRegion> sinkRegions = new Array<AtlasRegion>();
            sinkRegions.add(atlas.findRegion(Constants.SINK_SPRITE_1));
            sinkRegions.add(atlas.findRegion(Constants.SINK_SPRITE_2));

            sinkRegions.add(atlas.findRegion(Constants.SINK_SPRITE_1));
            sinkRegions.add(atlas.findRegion(Constants.SINK_SPRITE_2));

            sink = new Animation(Constants.SINK_DURATION / sinkRegions.size,
                    sinkRegions, PlayMode.NORMAL);

            Array<AtlasRegion> coalsRegions = new Array<AtlasRegion>();
            coalsRegions.add(atlas.findRegion(Constants.COALS_SPRITE_1));
            coalsRegions.add(atlas.findRegion(Constants.COALS_SPRITE_2));

            coals = new Animation(Constants.COALS_DURATION / coalsRegions.size,
                    coalsRegions, PlayMode.NORMAL);

            Array<AtlasRegion> lavaRegions = new Array<AtlasRegion>();
            lavaRegions.add(atlas.findRegion(Constants.LAVA_SPRITE_1));
            lavaRegions.add(atlas.findRegion(Constants.LAVA_SPRITE_2));

            lava = new Animation(Constants.LAVA_DURATION / lavaRegions.size,
                    lavaRegions, PlayMode.NORMAL);
        }
    }

    public static final class AmmoAssets {

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
        public final AtlasRegion hybridShot;
        public final AtlasRegion hybridBlast;

        private AmmoAssets(TextureAtlas atlas) {
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
            hybridShot = atlas.findRegion(Constants.SHOT_HYBRID_SPRITE);
            hybridBlast = atlas.findRegion(Constants.BLAST_HYBRID_SPRITE);
        }
    }

    public static final class ZoombaAssets {

        public final AtlasRegion zoomba;
        public final Animation gasZoomba;
        public final Animation liquidZoomba;
        public final Animation plasmaZoomba;
        public final Animation oreZoomba;
        public final Animation solidZoomba;

        private ZoombaAssets(TextureAtlas atlas) {
            zoomba = atlas.findRegion(Constants.ZOOMBA_SPRITE);

            Array<AtlasRegion> oreZoombaRegions = new Array<AtlasRegion>();
            oreZoombaRegions.add(atlas.findRegion(Constants.PROTRUSION_OREINGZOOMBA_SPRITE_1));
            oreZoombaRegions.add(atlas.findRegion(Constants.PROTRUSION_OREINGZOOMBA_SPRITE_2));
            oreZoomba = new Animation(Constants.SUSPENSION_ORE_DURATION / oreZoombaRegions.size, oreZoombaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> plasmaZoombaRegions = new Array<AtlasRegion>();
            plasmaZoombaRegions.add(atlas.findRegion(Constants.CHARGEDZOOMBA_SPRITE_1));
            plasmaZoombaRegions.add(atlas.findRegion(Constants.CHARGEDZOOMBA_SPRITE_2));
            plasmaZoomba = new Animation(Constants.SUSPENSION_PLASMA_DURATION / plasmaZoombaRegions.size, plasmaZoombaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gasZoombaRegions = new Array<AtlasRegion>();
            gasZoombaRegions.add(atlas.findRegion(Constants.FIERYZOOMBA_SPRITE_1));
            gasZoombaRegions.add(atlas.findRegion(Constants.FIERYZOOMBA_SPRITE_2));
            gasZoomba = new Animation(Constants.PROTRUSION_GAS_DURATION / gasZoombaRegions.size, gasZoombaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> liquidZoombaRegions = new Array<AtlasRegion>();
            liquidZoombaRegions.add(atlas.findRegion(Constants.GUSHINGZOOMBA_SPRITE_1));
            liquidZoombaRegions.add(atlas.findRegion(Constants.GUSHINGZOOMBA_SPRITE_2));
            liquidZoomba = new Animation(Constants.PROTRUSION_LIQUID_DURATION / liquidZoombaRegions.size, liquidZoombaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> solidZoombaRegions = new Array<AtlasRegion>();
            solidZoombaRegions.add(atlas.findRegion(Constants.SUSPENSION_SOLIDZOOMBA_SPRITE_1));
            solidZoombaRegions.add(atlas.findRegion(Constants.SUSPENSION_SOLIDZOOMBA_SPRITE_2));
            solidZoomba = new Animation(Constants.PROTRUSION_SOLID_DURATION / solidZoombaRegions.size, solidZoombaRegions, PlayMode.NORMAL);
        }
    }

    public static final class SwoopaAssets {

        public final AtlasRegion swoopa;
        public final Animation gasSwoopa;
        public final Animation liquidSwoopa;
        public final Animation plasmaSwoopa;
        public final Animation oreSwoopa;
        public final Animation solidSwoopa;

        private SwoopaAssets(TextureAtlas atlas) {
            swoopa = atlas.findRegion(Constants.SWOOPA_SPRITE);

            Array<AtlasRegion> oreSwoopaRegions = new Array<AtlasRegion>();
            oreSwoopaRegions.add(atlas.findRegion(Constants.PROTRUSION_OREINGSWOOPA_SPRITE_1));
            oreSwoopaRegions.add(atlas.findRegion(Constants.PROTRUSION_OREINGSWOOPA_SPRITE_2));
            oreSwoopa = new Animation(Constants.SUSPENSION_ORE_DURATION / oreSwoopaRegions.size, oreSwoopaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> plasmaSwoopaRegions = new Array<AtlasRegion>();
            plasmaSwoopaRegions.add(atlas.findRegion(Constants.CHARGEDSWOOPA_SPRITE_1));
            plasmaSwoopaRegions.add(atlas.findRegion(Constants.CHARGEDSWOOPA_SPRITE_2));
            plasmaSwoopa = new Animation(Constants.SUSPENSION_PLASMA_DURATION / plasmaSwoopaRegions.size, plasmaSwoopaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gasSwoopaRegions = new Array<AtlasRegion>();
            gasSwoopaRegions.add(atlas.findRegion(Constants.FIERYSWOOPA_SPRITE_1));
            gasSwoopaRegions.add(atlas.findRegion(Constants.FIERYSWOOPA_SPRITE_2));
            gasSwoopa = new Animation(Constants.PROTRUSION_GAS_DURATION / gasSwoopaRegions.size, gasSwoopaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> liquidSwoopaRegions = new Array<AtlasRegion>();
            liquidSwoopaRegions.add(atlas.findRegion(Constants.GUSHINGSWOOPA_SPRITE_1));
            liquidSwoopaRegions.add(atlas.findRegion(Constants.GUSHINGSWOOPA_SPRITE_2));
            liquidSwoopa = new Animation(Constants.PROTRUSION_LIQUID_DURATION / liquidSwoopaRegions.size, liquidSwoopaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> solidSwoopaRegions = new Array<AtlasRegion>();
            solidSwoopaRegions.add(atlas.findRegion(Constants.SUSPENSION_SOLIDSWOOPA_SPRITE_1));
            solidSwoopaRegions.add(atlas.findRegion(Constants.SUSPENSION_SOLIDSWOOPA_SPRITE_2));
            solidSwoopa = new Animation(Constants.PROTRUSION_SOLID_DURATION / solidSwoopaRegions.size, solidSwoopaRegions, PlayMode.NORMAL);
        }
    }

    public static final class OrbenAssets {

        public final TextureRegion dormantOrben;
        public final Animation plasmaOrben;
        public final Animation gasOrben;
        public final Animation liquidOrben;
        public final Animation solidOrben;
        public final Animation oreOrben;

        private OrbenAssets(TextureAtlas atlas) {

            dormantOrben = atlas.findRegion(Constants.ORBEN_SPRITE);

            Array<AtlasRegion> oreOrbenRegions = new Array<AtlasRegion>();
            oreOrbenRegions.add(atlas.findRegion(Constants.ORBEN_ORE_SPRITE_0));
            oreOrbenRegions.add(atlas.findRegion(Constants.ORBEN_ORE_SPRITE_1));
            oreOrbenRegions.add(atlas.findRegion(Constants.ORBEN_ORE_SPRITE_2));
            oreOrben = new Animation(Constants.ORBEN_DURATION / Constants.ORBEN_REGIONS, oreOrbenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> plasmaOrbenRegions = new Array<AtlasRegion>();
            plasmaOrbenRegions.add(atlas.findRegion(Constants.ORBEN_PLASMA_SPRITE_0));
            plasmaOrbenRegions.add(atlas.findRegion(Constants.ORBEN_PLASMA_SPRITE_1));
            plasmaOrbenRegions.add(atlas.findRegion(Constants.ORBEN_PLASMA_SPRITE_2));
            plasmaOrben = new Animation(Constants.ORBEN_DURATION / Constants.ORBEN_REGIONS, plasmaOrbenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gasOrbenRegions = new Array<AtlasRegion>();
            gasOrbenRegions.add(atlas.findRegion(Constants.ORBEN_GAS_SPRITE_0));
            gasOrbenRegions.add(atlas.findRegion(Constants.ORBEN_GAS_SPRITE_1));
            gasOrbenRegions.add(atlas.findRegion(Constants.ORBEN_GAS_SPRITE_2));
            gasOrben = new Animation(Constants.ORBEN_DURATION / Constants.ORBEN_REGIONS, gasOrbenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> liquidOrbenRegions = new Array<AtlasRegion>();
            liquidOrbenRegions.add(atlas.findRegion(Constants.ORBEN_LIQUID_SPRITE_0));
            liquidOrbenRegions.add(atlas.findRegion(Constants.ORBEN_LIQUID_SPRITE_1));
            liquidOrbenRegions.add(atlas.findRegion(Constants.ORBEN_LIQUID_SPRITE_2));
            liquidOrben = new Animation(Constants.ORBEN_DURATION / Constants.ORBEN_REGIONS, liquidOrbenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> solidOrbenRegions = new Array<AtlasRegion>();
            solidOrbenRegions.add(atlas.findRegion(Constants.ORBEN_SOLID_SPRITE_0));
            solidOrbenRegions.add(atlas.findRegion(Constants.ORBEN_SOLID_SPRITE_1));
            solidOrbenRegions.add(atlas.findRegion(Constants.ORBEN_SOLID_SPRITE_2));
            solidOrben = new Animation(Constants.ORBEN_DURATION / Constants.ORBEN_REGIONS, solidOrbenRegions, PlayMode.NORMAL);
        }
    }

    public static final class RollenAssets {

        public final Animation plasmaRollen;
        public final Animation gasRollen;
        public final Animation liquidRollen;
        public final Animation solidRollen;
        public final Animation oreRollen;

        private RollenAssets(TextureAtlas atlas) {

            Array<AtlasRegion> oreRollenRegions = new Array<AtlasRegion>();
            oreRollenRegions.add(atlas.findRegion(Constants.ROLLEN_ORE_SPRITE_1));
            oreRollenRegions.add(atlas.findRegion(Constants.ROLLEN_ORE_SPRITE_2));
            oreRollenRegions.add(atlas.findRegion(Constants.ROLLEN_ORE_SPRITE_3));
            oreRollenRegions.add(atlas.findRegion(Constants.ROLLEN_ORE_SPRITE_4));
            oreRollen = new Animation(Constants.ROLLEN_DURATION / Constants.ROLLEN_REGIONS, oreRollenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> plasmaRollenRegions = new Array<AtlasRegion>();
            plasmaRollenRegions.add(atlas.findRegion(Constants.ROLLEN_PLASMA_SPRITE_1));
            plasmaRollenRegions.add(atlas.findRegion(Constants.ROLLEN_PLASMA_SPRITE_2));
            plasmaRollenRegions.add(atlas.findRegion(Constants.ROLLEN_PLASMA_SPRITE_3));
            plasmaRollenRegions.add(atlas.findRegion(Constants.ROLLEN_PLASMA_SPRITE_4));
            plasmaRollen = new Animation(Constants.ROLLEN_DURATION / Constants.ROLLEN_REGIONS, plasmaRollenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gasRollenRegions = new Array<AtlasRegion>();
            gasRollenRegions.add(atlas.findRegion(Constants.ROLLEN_GAS_SPRITE_1));
            gasRollenRegions.add(atlas.findRegion(Constants.ROLLEN_GAS_SPRITE_2));
            gasRollenRegions.add(atlas.findRegion(Constants.ROLLEN_GAS_SPRITE_3));
            gasRollenRegions.add(atlas.findRegion(Constants.ROLLEN_GAS_SPRITE_4));
            gasRollen = new Animation(Constants.ROLLEN_DURATION / Constants.ROLLEN_REGIONS, gasRollenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> liquidRollenRegions = new Array<AtlasRegion>();
            liquidRollenRegions.add(atlas.findRegion(Constants.ROLLEN_LIQUID_SPRITE_1));
            liquidRollenRegions.add(atlas.findRegion(Constants.ROLLEN_LIQUID_SPRITE_2));
            liquidRollenRegions.add(atlas.findRegion(Constants.ROLLEN_LIQUID_SPRITE_3));
            liquidRollenRegions.add(atlas.findRegion(Constants.ROLLEN_LIQUID_SPRITE_4));
            liquidRollen = new Animation(Constants.ROLLEN_DURATION / Constants.ROLLEN_REGIONS, liquidRollenRegions, PlayMode.NORMAL);

            Array<AtlasRegion> solidRollenRegions = new Array<AtlasRegion>();
            solidRollenRegions.add(atlas.findRegion(Constants.ROLLEN_SOLID_SPRITE_1));
            solidRollenRegions.add(atlas.findRegion(Constants.ROLLEN_SOLID_SPRITE_2));
            solidRollenRegions.add(atlas.findRegion(Constants.ROLLEN_SOLID_SPRITE_3));
            solidRollenRegions.add(atlas.findRegion(Constants.ROLLEN_SOLID_SPRITE_4));
            solidRollen = new Animation(Constants.ROLLEN_DURATION / Constants.ROLLEN_REGIONS, solidRollenRegions, PlayMode.NORMAL);
        }
    }

    public static final class ProtrusionAssets {

        public final Animation solidProtrustion;
        public final Animation liquidProtrusion;
        public final Animation gasProtrusion;
        public final Animation plasmaProtrusion;
        public final Animation oreProtrusion;

        private ProtrusionAssets(TextureAtlas atlas) {

            Array<AtlasRegion> oreProtrusionRegions = new Array<AtlasRegion>();
            oreProtrusionRegions.add(atlas.findRegion(Constants.PROTRUSION_ORE_SPRITE_1));
            oreProtrusionRegions.add(atlas.findRegion(Constants.PROTRUSION_ORE_SPRITE_2));
            oreProtrusion = new Animation(Constants.PROTRUSION_ORE_DURATION / oreProtrusionRegions.size, oreProtrusionRegions, PlayMode.NORMAL);

            Array<AtlasRegion> plasmaProtrusionRegions = new Array<AtlasRegion>();
            plasmaProtrusionRegions.add(atlas.findRegion(Constants.PROTRUSION_PLASMA_SPRITE_1));
            plasmaProtrusionRegions.add(atlas.findRegion(Constants.PROTRUSION_PLASMA_SPRITE_2));
            plasmaProtrusion = new Animation(Constants.PROTRUSION_PLASMA_DURATION / plasmaProtrusionRegions.size, plasmaProtrusionRegions, PlayMode.NORMAL);

            Array<AtlasRegion> solidProtrustionRegions = new Array<AtlasRegion>();
            solidProtrustionRegions.add(atlas.findRegion(Constants.PROTRUSION_SOLID_SPRITE_1));
            solidProtrustionRegions.add(atlas.findRegion(Constants.PROTRUSION_SOLID_SPRITE_2));
            solidProtrustion = new Animation(Constants.PROTRUSION_SOLID_DURATION / solidProtrustionRegions.size, solidProtrustionRegions, PlayMode.NORMAL);

            Array<AtlasRegion> liquidProtrusionRegions = new Array<AtlasRegion>();
            liquidProtrusionRegions.add(atlas.findRegion(Constants.PROTRUSION_LIQUID_SPRITE_1));
            liquidProtrusionRegions.add(atlas.findRegion(Constants.PROTRUSION_LIQUID_SPRITE_2));
            liquidProtrusion = new Animation(Constants.PROTRUSION_LIQUID_DURATION / liquidProtrusionRegions.size, liquidProtrusionRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gasProtrusionRegions = new Array<AtlasRegion>();
            gasProtrusionRegions.add(atlas.findRegion(Constants.PROTRUSION_GAS_SPRITE_1));
            gasProtrusionRegions.add(atlas.findRegion(Constants.PROTRUSION_GAS_SPRITE_2));
            gasProtrusion = new Animation(Constants.PROTRUSION_GAS_DURATION / gasProtrusionRegions.size, gasProtrusionRegions, PlayMode.NORMAL);
        }
    }

    public static final class SuspensionAssets {

        public final Animation gasSuspension;
        public final Animation solidSuspension;
        public final Animation oreSuspension;
        public final Animation liquidSuspension;
        public final Animation plasmaSuspension;
        public final Animation antimatterSuspension;

        private SuspensionAssets(TextureAtlas atlas) {

            Array<AtlasRegion> gasSuspensionRegions = new Array<AtlasRegion>();
            gasSuspensionRegions.add(atlas.findRegion(Constants.SUSPENSION_GAS_SPRITE_1));
            gasSuspensionRegions.add(atlas.findRegion(Constants.SUSPENSION_GAS_SPRITE_2));
            gasSuspension = new Animation(Constants.SUSPENSION_GAS_DURATION / gasSuspensionRegions.size, gasSuspensionRegions, PlayMode.NORMAL);

            Array<AtlasRegion> plasmaSuspensionRegions = new Array<AtlasRegion>();
            plasmaSuspensionRegions.add(atlas.findRegion(Constants.SUSPENSION_PLASMA_SPRITE_1));
            plasmaSuspensionRegions.add(atlas.findRegion(Constants.SUSPENSION_PLASMA_SPRITE_2));
            plasmaSuspension = new Animation(Constants.SUSPENSION_PLASMA_DURATION / plasmaSuspensionRegions.size, plasmaSuspensionRegions, PlayMode.NORMAL);

            Array<AtlasRegion> liquidSuspensionRegions = new Array<AtlasRegion>();
            liquidSuspensionRegions.add(atlas.findRegion(Constants.SUSPENSION_LIQUID_SPRITE_1));
            liquidSuspensionRegions.add(atlas.findRegion(Constants.SUSPENSION_LIQUID_SPRITE_2));
            liquidSuspension = new Animation(Constants.SUSPENSION_LIQUID_DURATION / liquidSuspensionRegions.size, liquidSuspensionRegions, PlayMode.NORMAL);

            Array<AtlasRegion> solidSuspensionRegions = new Array<AtlasRegion>();
            solidSuspensionRegions.add(atlas.findRegion(Constants.SUSPENSION_SOLID_SPRITE_1));
            solidSuspensionRegions.add(atlas.findRegion(Constants.SUSPENSION_SOLID_SPRITE_2));
            solidSuspension = new Animation(Constants.SUSPENSION_SOLID_DURATION / solidSuspensionRegions.size, solidSuspensionRegions, PlayMode.NORMAL);

            Array<AtlasRegion> oreSuspensionRegions = new Array<AtlasRegion>();
            oreSuspensionRegions.add(atlas.findRegion(Constants.SUSPENSION_ORE_SPRITE_1));
            oreSuspensionRegions.add(atlas.findRegion(Constants.SUSPENSION_ORE_SPRITE_2));
            oreSuspension = new Animation(Constants.SUSPENSION_ORE_DURATION / oreSuspensionRegions.size, oreSuspensionRegions, PlayMode.NORMAL);

            Array<AtlasRegion> antimatterSuspensionRegions = new Array<AtlasRegion>();
            antimatterSuspensionRegions.add(atlas.findRegion(Constants.SUSPENSION_ANTIMATTER_SPRITE_1));
            antimatterSuspensionRegions.add(atlas.findRegion(Constants.SUSPENSION_ANTIMATTER_SPRITE_2));
            antimatterSuspensionRegions.add(atlas.findRegion(Constants.SUSPENSION_ANTIMATTER_SPRITE_3));
            this.antimatterSuspension = new Animation(Constants.SUSPENSION_ANTIMATTER_FRAME_DURATION, antimatterSuspensionRegions, PlayMode.LOOP_PINGPONG);
        }
    }

    public static final class PortalAssets {

        public final Animation portal;
        public final Animation teleport;

        private PortalAssets(TextureAtlas atlas) {
            Array<AtlasRegion> portalRegions = new Array<AtlasRegion>();
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_1));
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_2));
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_3));
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_4));
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_5));
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_6));

            portal = new Animation(Constants.PORTAL_FRAME_DURATION,
                    portalRegions, PlayMode.NORMAL);

            Array<AtlasRegion> teleportRegions = new Array<AtlasRegion>();
            teleportRegions.add(atlas.findRegion(Constants.TELEPORT_SPRITE_1));
            teleportRegions.add(atlas.findRegion(Constants.TELEPORT_SPRITE_2));
            teleportRegions.add(atlas.findRegion(Constants.TELEPORT_SPRITE_3));

            teleport = new Animation(Constants.TELEPORT_FRAME_DURATION,
                    teleportRegions, PlayMode.NORMAL);
        }        
    }

    public static final class ImpactAssets {

        public final Animation impactPlasma;
        public final Animation impact;
        public final Animation impactLiquid;
        public final Animation impactSolid;
        public final Animation impactPsychic;
        public final Animation impactHybrid;
        public final Animation impactNative;

        private ImpactAssets(TextureAtlas atlas) {

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

            impact = new Animation(Constants.IMPACT_DURATION / impactGasRegions.size,
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

            Array<AtlasRegion> impactPsychicRegions = new Array<AtlasRegion>();
            impactPsychicRegions.add(atlas.findRegion(Constants.IMPACT_PSYCHIC_LARGE));
            impactPsychicRegions.add(atlas.findRegion(Constants.IMPACT_PSYCHIC_MEDIUM));
            impactPsychicRegions.add(atlas.findRegion(Constants.IMPACT_PSYCHIC_SMALL));

            impactPsychic = new Animation(Constants.IMPACT_DURATION / impactPsychicRegions.size,
                    impactPsychicRegions, PlayMode.NORMAL);

            Array<AtlasRegion> impactHybridRegions = new Array<AtlasRegion>();
            impactHybridRegions.add(atlas.findRegion(Constants.IMPACT_HYBRID_LARGE));
            impactHybridRegions.add(atlas.findRegion(Constants.IMPACT_HYBRID_MEDIUM));
            impactHybridRegions.add(atlas.findRegion(Constants.IMPACT_HYBRID_SMALL));

            impactHybrid = new Animation(Constants.IMPACT_DURATION / impactHybridRegions.size,
                    impactHybridRegions, PlayMode.NORMAL);

            Array<AtlasRegion> impactNativeRegions = new Array<AtlasRegion>();
            impactNativeRegions.add(atlas.findRegion(Constants.IMPACT_NATIVE_LARGE));
            impactNativeRegions.add(atlas.findRegion(Constants.IMPACT_NATIVE_MEDIUM));
            impactNativeRegions.add(atlas.findRegion(Constants.IMPACT_NATIVE_SMALL));

            impactNative = new Animation(Constants.IMPACT_DURATION / impactNativeRegions.size,
                    impactNativeRegions, PlayMode.NORMAL);
        }
    }

    public static final class PowerupAssets {
        public final Animation ammoPowerup;
        public final Animation healthPowerup;
        public final Animation turboPowerup;
        public final Animation lifePowerup;
        public final Animation cannonPowerup;

        private PowerupAssets(TextureAtlas atlas) {
            
            Array<AtlasRegion> ammoPowerupRegions = new Array<AtlasRegion>();
            ammoPowerupRegions.add(atlas.findRegion(Constants.AMMO_POWERUP_SPRITE_1));
            ammoPowerupRegions.add(atlas.findRegion(Constants.AMMO_POWERUP_SPRITE_2));
            ammoPowerupRegions.add(atlas.findRegion(Constants.AMMO_POWERUP_SPRITE));
            ammoPowerup = new Animation(Constants.POWERUP_DURATION / ammoPowerupRegions.size, ammoPowerupRegions, PlayMode.NORMAL);
            
            Array<AtlasRegion> healthPowerupRegions = new Array<AtlasRegion>();
            healthPowerupRegions.add(atlas.findRegion(Constants.HEALTH_POWERUP_SPRITE_1));
            healthPowerupRegions.add(atlas.findRegion(Constants.HEALTH_POWERUP_SPRITE_2));
            healthPowerupRegions.add(atlas.findRegion(Constants.HEALTH_POWERUP_SPRITE));
            healthPowerup = new Animation(Constants.POWERUP_DURATION / healthPowerupRegions.size, healthPowerupRegions, PlayMode.NORMAL);

            Array<AtlasRegion> turboPowerupRegions = new Array<AtlasRegion>();
            turboPowerupRegions.add(atlas.findRegion(Constants.TURBO_POWERUP_SPRITE_1));
            turboPowerupRegions.add(atlas.findRegion(Constants.TURBO_POWERUP_SPRITE_2));
            turboPowerupRegions.add(atlas.findRegion(Constants.TURBO_POWERUP_SPRITE));
            turboPowerup = new Animation(Constants.POWERUP_DURATION / turboPowerupRegions.size, turboPowerupRegions, PlayMode.NORMAL);
            
            Array<AtlasRegion> lifePowerupRegions = new Array<AtlasRegion>();
            lifePowerupRegions.add(atlas.findRegion(Constants.LIFE_POWERUP_SPRITE_1));
            lifePowerupRegions.add(atlas.findRegion(Constants.LIFE_POWERUP_SPRITE_2));
            lifePowerupRegions.add(atlas.findRegion(Constants.LIFE_POWERUP_SPRITE));
            lifePowerup = new Animation(Constants.POWERUP_DURATION / lifePowerupRegions.size, lifePowerupRegions, PlayMode.NORMAL);

            Array<AtlasRegion> cannonPowerupRegions = new Array<AtlasRegion>();
            cannonPowerupRegions.add(atlas.findRegion(Constants.CANNON_POWERUP_SPRITE_1));
            cannonPowerupRegions.add(atlas.findRegion(Constants.CANNON_POWERUP_SPRITE_2));
            cannonPowerupRegions.add(atlas.findRegion(Constants.CANNON_POWERUP_SPRITE_3));
            cannonPowerupRegions.add(atlas.findRegion(Constants.CANNON_POWERUP_SPRITE));
            cannonPowerup = new Animation(Constants.POWERUP_DURATION / cannonPowerupRegions.size, cannonPowerupRegions, PlayMode.NORMAL);
        }
    }

    public static final class HudAssets {

        public final AtlasRegion shoot;
        public final AtlasRegion blast;
        public final AtlasRegion jump;
        public final AtlasRegion hover;
        public final AtlasRegion cling;
        public final AtlasRegion climb;
        public final AtlasRegion dash;
        public final AtlasRegion life;

        private HudAssets(TextureAtlas atlas) {
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

    public static final class OverlayAssets {

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

        private OverlayAssets(TextureAtlas atlas) {
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

    public final class SoundAssets {

        public final Sound powerup;

        private SoundAssets() {
            powerup = assetManager.get(Constants.POWERUP_SOUND); // use of descriptor enforces type checking
        }
    }

    public final class MusicAssets {

        public final Music level;
        public final Music boss;

        private MusicAssets() {
            level = assetManager.get(Constants.LEVEL_MUSIC); // use of descriptor enforces type checking
            boss = assetManager.get(Constants.BOSS_MUSIC); // use of descriptor enforces type checking
        }
        
        public Music getThemeMusic(Enums.Theme theme) {
            switch (theme) {
                case HOME:
                    return level;
                case MECHANICAL:
                    return level;
                case ELECTROMAGNETIC:
                    return level;
                case NUCLEAR:
                    return level;
                case THERMAL:
                    return boss;
                case GRAVITATIONAL:
                    return level;
                case MYSTERIOUS:
                    return level;
                case FINAL:
                    return level;
                default:
                    return level;
            }
        }
    }

    public static final class FontAssets {}

    // Getters
    public final GigaGalAssets getGigaGalAssets(){ return gigaGalAssets; }
    public final BoxAssets getBoxAssets(){ return boxAssets; }
    public final GroundAssets getGroundAssets(){ return groundAssets; }
    public final AmmoAssets getAmmoAssets(){ return ammoAssets; }
    public final ZoombaAssets getZoombaAssets(){ return zoombaAssets; }
    public final SwoopaAssets getSwoopaAssets(){ return swoopaAssets; }
    public final OrbenAssets getOrbenAssets(){ return orbenAssets; }
    public final RollenAssets getRollenAssets(){ return rollenAssets; }
    public final ProtrusionAssets getProtrusionAssets(){ return protrusionAssets; }
    public final SuspensionAssets getSuspensionAssets(){ return suspensionAssets; }
    public final ImpactAssets getImpactAssets(){ return impactAssets; }
    public final PowerupAssets getPowerupAssets(){ return powerupAssets; }
    public final PortalAssets getPortalAssets(){ return portalAssets; }
    public final HudAssets getHudAssets(){ return hudAssets; }
    public final OverlayAssets getOverlayAssets(){ return overlayAssets; }
    public final SoundAssets getSoundAssets() { return soundAssets; }
    public final MusicAssets getMusicAssets() { return musicAssets; }
    public final FontAssets getFontAssets() { return fontAssets; }
}
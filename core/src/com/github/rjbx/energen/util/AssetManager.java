package com.github.rjbx.energen.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.github.rjbx.energen.entity.Box;
import com.github.rjbx.energen.entity.Brick;
import com.github.rjbx.energen.entity.Ground;

// immutable singleton
public final class AssetManager implements AssetErrorListener {

    // fields class-instantiated save for tag, assetmanager and atlas
    public static final String TAG = AssetManager.class.getName();
    private static final AssetManager INSTANCE = new AssetManager();
    private com.badlogic.gdx.assets.AssetManager assetManager;
    private AvatarAssets avatarAssets;
    private BossAssets bossAssets;
    private BackgroundAssets backgroundAssets;
    private GroundAssets groundAssets;
    private AmmoAssets ammoAssets;
    private BladeAssets bladeAssets;
    private CanirolAssets canirolAssets;
    private ZoombaAssets zoombaAssets;
    private SwoopaAssets swoopaAssets;
    private OrbenAssets orbenAssets;
    private RollenAssets rollenAssets;
    private ArmorolloAssets armorolloAssets;
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
    private AssetManager() {}

    // static factory
    public static final AssetManager getInstance() { return INSTANCE; }

    public final void create() {
        this.assetManager = new com.badlogic.gdx.assets.AssetManager();
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS); // atlas packed upon gradle build; load all at once instead of individually
        assetManager.load(Constants.OUTSET_MUSIC);
        assetManager.load(Constants.OFFENCE_MUSIC);
        assetManager.load(Constants.SELECTION_MUSIC);
        assetManager.load(Constants.ENCOUNTER_MUSIC);
        assetManager.load(Constants.ESCALATION_MUSIC);
        assetManager.load(Constants.RESTART_MUSIC);
        assetManager.load(Constants.UPSTART_MUSIC);
        assetManager.load(Constants.RECKONING_MUSIC);
        assetManager.load(Constants.REELING_MUSIC);
        assetManager.load(Constants.VILLAGE_MUSIC);
        assetManager.load(Constants.CONTINUATION_MUSIC);
        assetManager.load(Constants.CONTEMPLATION_MUSIC);
        assetManager.load(Constants.COMBUSTION_MUSIC);
        assetManager.load(Constants.SENDOFF_MUSIC);
        assetManager.load(Constants.SKY_MUSIC);
        assetManager.load(Constants.STROLL_MUSIC);
        assetManager.load(Constants.PUZZLE_MUSIC);
        assetManager.load(Constants.TRIAL_MUSIC);
        assetManager.load(Constants.TUNNEL_MUSIC);
        assetManager.load(Constants.BUZZARD_MUSIC);
        assetManager.load(Constants.CHIRPY_MUSIC);
        assetManager.load(Constants.HEALTH_SOUND);
        assetManager.load(Constants.AMMO_SOUND);
        assetManager.load(Constants.TURBO_SOUND);
        assetManager.load(Constants.CANNON_SOUND);
        assetManager.load(Constants.LIFE_SOUND);
        assetManager.load(Constants.UPGRADE_SOUND);
        assetManager.load(Constants.BLAST_NATIVE_SOUND);
        assetManager.load(Constants.BLAST_ORE_SOUND);
        assetManager.load(Constants.BLAST_PLASMA_SOUND);
        assetManager.load(Constants.BLAST_LIQUID_SOUND);
        assetManager.load(Constants.BLAST_SOLID_SOUND);
        assetManager.load(Constants.BLAST_ANTIMATTER_SOUND);
        assetManager.load(Constants.BLAST_HYBRID_SOUND);
        assetManager.load(Constants.BLAST_GAS_SOUND);
        assetManager.load(Constants.SHOT_NATIVE_SOUND);
        assetManager.load(Constants.SHOT_ORE_SOUND);
        assetManager.load(Constants.SHOT_PLASMA_SOUND);
        assetManager.load(Constants.SHOT_LIQUID_SOUND);
        assetManager.load(Constants.SHOT_SOLID_SOUND);
        assetManager.load(Constants.SHOT_ANTIMATTER_SOUND);
        assetManager.load(Constants.SHOT_HYBRID_SOUND);
        assetManager.load(Constants.SHOT_GAS_SOUND);
        assetManager.load(Constants.WARP_SOUND);
        assetManager.load(Constants.HIT_SOUND);
        assetManager.load(Constants.HIT_GROUND_SOUND);
        assetManager.load(Constants.BREAK_GROUND_SOUND);
        assetManager.load(Constants.FLIGHT_SOUND);
        assetManager.load(Constants.DAMAGE_SOUND);
        assetManager.load(Constants.MESSAGE_FONT);
        assetManager.load(Constants.MENU_FONT);
        assetManager.load(Constants.INACTIVE_FONT);
        assetManager.load(Constants.TITLE_FONT);
        assetManager.finishLoading();
        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS);
        avatarAssets = new AvatarAssets(atlas);
        bossAssets = new BossAssets(atlas);
        backgroundAssets = new BackgroundAssets(atlas);
        groundAssets = new GroundAssets(atlas);
        ammoAssets = new AmmoAssets(atlas);
        bladeAssets = new BladeAssets(atlas);
        canirolAssets = new CanirolAssets(atlas);
        zoombaAssets = new ZoombaAssets(atlas);
        swoopaAssets = new SwoopaAssets(atlas);
        orbenAssets = new OrbenAssets(atlas);
        rollenAssets = new RollenAssets(atlas);
        armorolloAssets = new ArmorolloAssets(atlas);
        protrusionAssets = new ProtrusionAssets(atlas);
        suspensionAssets = new SuspensionAssets(atlas);
        impactAssets = new ImpactAssets(atlas);
        powerupAssets = new PowerupAssets(atlas);
        portalAssets = new PortalAssets(atlas);
        hudAssets = new HudAssets(atlas);
        overlayAssets = new OverlayAssets(atlas);
        soundAssets = new SoundAssets();
        musicAssets = new MusicAssets();
        fontAssets = new FontAssets();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset: " + asset.fileName, throwable);
    }

    public void dispose() {
        assetManager.clear(); // disposes all assets
    }

    public static final class AvatarAssets {

        public final AtlasRegion armPoint;
        public final AtlasRegion armReleaseForward;
        public final AtlasRegion armReleaseUp;
        public final AtlasRegion armReleaseDown;
        public final AtlasRegion handReleaseUp;
        public final AtlasRegion handReleaseDown;
        public final AtlasRegion armRaise;
        public final AtlasRegion armLower;
        public final AtlasRegion armRelax;
        public final AtlasRegion armReach;
        public final AtlasRegion handReach;
        public final AtlasRegion armClench;
        public final AtlasRegion legsStand;
        public final AtlasRegion legsFall;
        public final AtlasRegion legsRappel;
        public final AtlasRegion legsRecoil;
        public final AtlasRegion legsClimb;
        public final AtlasRegion feetStand;
        public final AtlasRegion feetFall;
        public final AtlasRegion feetRappel;
        public final AtlasRegion feetRecoil;
        public final AtlasRegion feetClimb;
        public final AtlasRegion armClimb;
        public final AtlasRegion head;
        public final AtlasRegion headClimb;
        public final AtlasRegion midsectionClimb;
        public final AtlasRegion midsection;
        public final AtlasRegion hairClimb;
        public final AtlasRegion mouthOpen;
        public final AtlasRegion mouthClosed;
        public final AtlasRegion eyesBlink;
        public final AtlasRegion obfuscated;
        public final Animation<TextureRegion> hair;
        public final Animation<TextureRegion> waist;
        public final Animation<TextureRegion> armCurl;
        public final Animation<TextureRegion> armPush;
        public final Animation<TextureRegion> armPull;
        public final Animation<TextureRegion> armSwing;
        public final Animation<TextureRegion> armDown;
        public final Animation<TextureRegion> armUp;
        public final Animation<TextureRegion> armAway;
        public final Animation<TextureRegion> armToward;
        public final Animation<TextureRegion> handCurl;
        public final Animation<TextureRegion> handSwing;
        public final Animation<TextureRegion> handRappel;
        public final Animation<TextureRegion> handMove;
        public final Animation<TextureRegion> handPoint;
        public final Animation<TextureRegion> handRaise;
        public final Animation<TextureRegion> handLower;
        public final Animation<TextureRegion> handBack;
        public final Animation<TextureRegion> handFore;
        public final Animation<TextureRegion> handDown;
        public final Animation<TextureRegion> handUp;
        public final Animation<TextureRegion> handToward;
        public final Animation<TextureRegion> handAway;
        public final Animation<TextureRegion> legsStride;
        public final Animation<TextureRegion> feetDash;
        public final Animation<TextureRegion> feetStride;
        public final Animation<TextureRegion> feetHover;
        public final Animation<TextureRegion> eyesOpen;
        public final AtlasRegion stand;
        public final AtlasRegion fall;

        private AvatarAssets(TextureAtlas atlas) {
            armClench = atlas.findRegion(Constants.ARM_CLENCH);
            armClimb = atlas.findRegion(Constants.ARM_CLIMB);
            armRelax = atlas.findRegion(Constants.ARM_RELAX);
            armReach = atlas.findRegion(Constants.ARM_REACH);
            armPoint = atlas.findRegion(Constants.ARM_POINT);
            armRaise = atlas.findRegion(Constants.ARM_RAISE_0);
            armLower = atlas.findRegion(Constants.ARM_LOWER_0);
            armReleaseForward = atlas.findRegion(Constants.ARM_RELEASE);
            armReleaseUp = atlas.findRegion(Constants.ARM_RAISE);
            armReleaseDown = atlas.findRegion(Constants.ARM_LOWER);
            handReleaseUp = atlas.findRegion(Constants.HAND_RAISE);
            handReleaseDown = atlas.findRegion(Constants.HAND_LOWER);
            handReach = atlas.findRegion(Constants.HAND_REACH);
            legsStand = atlas.findRegion(Constants.LEGS_STAND);
            legsFall = atlas.findRegion(Constants.LEGS_FALL);
            legsRappel = atlas.findRegion(Constants.LEGS_RAPPEL);
            legsRecoil = atlas.findRegion(Constants.LEGS_RECOIL);
            legsClimb = atlas.findRegion(Constants.LEGS_CLIMB);
            feetStand = atlas.findRegion(Constants.FEET_STAND);
            feetFall = atlas.findRegion(Constants.FEET_FALL);
            feetRappel = atlas.findRegion(Constants.FEET_RAPPEL);
            feetRecoil = atlas.findRegion(Constants.FEET_RECOIL);
            feetClimb = atlas.findRegion(Constants.FEET_CLIMB);
            midsection = atlas.findRegion(Constants.TORSO_0);
            head = atlas.findRegion(Constants.HEAD);
            midsectionClimb = atlas.findRegion(Constants.TORSO_CLIMB_0);
            headClimb = atlas.findRegion(Constants.HEAD_CLIMB);
            hairClimb = atlas.findRegion(Constants.HAIR_CLIMB);
            mouthOpen = atlas.findRegion(Constants.MOUTH_OPEN);
            mouthClosed = atlas.findRegion(Constants.MOUTH_CLOSED);
            eyesBlink = atlas.findRegion(Constants.EYES_BLINK);
            obfuscated = atlas.findRegion(Constants.OBFUSCATED);

            Array<AtlasRegion> hairFrames = new Array<AtlasRegion>();
            hairFrames.add(atlas.findRegion(Constants.HAIR_1));
            hairFrames.add(atlas.findRegion(Constants.HAIR_2));
            hairFrames.add(atlas.findRegion(Constants.HAIR_3));
            hair = new Animation(Constants.STRIDE_FRAME_DURATION, hairFrames, PlayMode.LOOP_PINGPONG);

            Array<AtlasRegion> peerFrames = new Array<AtlasRegion>();
            peerFrames.add(atlas.findRegion(Constants.EYES_OPEN_1));
            peerFrames.add(atlas.findRegion(Constants.EYES_OPEN_2));
            peerFrames.add(atlas.findRegion(Constants.EYES_OPEN_3));
            peerFrames.add(atlas.findRegion(Constants.EYES_OPEN_4));
            eyesOpen = new Animation(Constants.EYE_ROLL_FRAME_DURATION, peerFrames, PlayMode.LOOP);

            Array<AtlasRegion> waistFrames = new Array<AtlasRegion>();
            waistFrames.add(atlas.findRegion(Constants.WAIST_1));
            waistFrames.add(atlas.findRegion(Constants.WAIST_2));
            waistFrames.add(atlas.findRegion(Constants.WAIST_3));
            waistFrames.add(atlas.findRegion(Constants.WAIST_3));
            waistFrames.add(atlas.findRegion(Constants.WAIST_4));
            waist = new Animation(Constants.STRIDE_FRAME_DURATION, waistFrames, PlayMode.LOOP_PINGPONG);

            Array<AtlasRegion> moveFrames = new Array<AtlasRegion>();
            moveFrames.add(atlas.findRegion(Constants.HAND_MOVE_2));
            moveFrames.add(atlas.findRegion(Constants.HAND_MOVE_1));
            handMove = new Animation(Constants.STRIDE_FRAME_DURATION, moveFrames, PlayMode.LOOP);

            Array<AtlasRegion> handSwingFrames = new Array<AtlasRegion>();
            handSwingFrames.add(atlas.findRegion(Constants.HAND_CLENCH));
            handSwingFrames.add(atlas.findRegion(Constants.HAND_RELAX));
            handSwing = new Animation(Constants.STRIDE_FRAME_DURATION, handSwingFrames, PlayMode.LOOP);

            handSwingFrames.add(atlas.findRegion(Constants.HAND_REACH));
            handAway = new Animation(Constants.STRIDE_FRAME_DURATION, handSwingFrames, PlayMode.NORMAL);
            handToward = new Animation(Constants.STRIDE_FRAME_DURATION, handSwingFrames, PlayMode.REVERSED);

            Array<AtlasRegion> handCurlFrames = new Array<AtlasRegion>();
            handCurlFrames.add(atlas.findRegion(Constants.HAND_RELEASE));
            handCurlFrames.add(atlas.findRegion(Constants.HAND_CURL));
            handCurl = new Animation(Constants.STRIDE_FRAME_DURATION, handCurlFrames, PlayMode.LOOP);

            Array<AtlasRegion> chargeFrames = new Array<AtlasRegion>();
            chargeFrames.add(atlas.findRegion(Constants.HAND_SHOOT));
            chargeFrames.add(atlas.findRegion(Constants.HAND_BLAST_1));
            chargeFrames.add(atlas.findRegion(Constants.HAND_BLAST_2));
            handPoint = new Animation(Constants.FLICKER_FRAME_DURATION, chargeFrames, PlayMode.LOOP);

            Array<AtlasRegion> raiseFrames = new Array<AtlasRegion>();
            raiseFrames.add(atlas.findRegion(Constants.HAND_RAISE_0));
            raiseFrames.add(atlas.findRegion(Constants.HAND_RAISE_1));
            raiseFrames.add(atlas.findRegion(Constants.HAND_RAISE_2));
            handRaise = new Animation(Constants.FLICKER_FRAME_DURATION, raiseFrames, PlayMode.LOOP);

            Array<AtlasRegion> lowerFrames = new Array<AtlasRegion>();
            lowerFrames.add(atlas.findRegion(Constants.HAND_LOWER_0));
            lowerFrames.add(atlas.findRegion(Constants.HAND_LOWER_1));
            lowerFrames.add(atlas.findRegion(Constants.HAND_LOWER_2));
            handLower = new Animation(Constants.FLICKER_FRAME_DURATION, lowerFrames, PlayMode.LOOP);


            Array<AtlasRegion> downhandFrames = new Array<AtlasRegion>();
            downhandFrames.add(atlas.findRegion(Constants.HAND_RAISE));
            downhandFrames.add(atlas.findRegion(Constants.HAND_SHOOT));
            downhandFrames.add(atlas.findRegion(Constants.HAND_LOWER));
            handDown = new Animation(Constants.FLIPSWIPE_FRAME_DURATION, downhandFrames, PlayMode.NORMAL);

            handUp = new Animation(Constants.FLIPSWIPE_FRAME_DURATION, downhandFrames, PlayMode.REVERSED);

            Array<AtlasRegion> handAcrossFrames = new Array<AtlasRegion>();
            handAcrossFrames.add(atlas.findRegion(Constants.HAND_SHOOT));
            handAcrossFrames.add(atlas.findRegion(Constants.HAND_OUTWARD));
            handAcrossFrames.add(atlas.findRegion(Constants.HAND_CURL));
            handBack = new Animation(Constants.SIDESWIPE_FRAME_DURATION, handAcrossFrames, PlayMode.NORMAL);

            handFore = new Animation(Constants.SIDESWIPE_FRAME_DURATION, handAcrossFrames, PlayMode.REVERSED);

            Array<AtlasRegion> rappelFrames = new Array<AtlasRegion>();
            rappelFrames.add(atlas.findRegion(Constants.HAND_RAPPEL_2));
            rappelFrames.add(atlas.findRegion(Constants.HAND_RAPPEL_1));
            handRappel = new Animation(Constants.RAPPEL_FRAME_DURATION, rappelFrames, PlayMode.LOOP);

            Array<AtlasRegion> downarmFrames = new Array<AtlasRegion>();
            downarmFrames.add(atlas.findRegion(Constants.ARM_RAISE));
            downarmFrames.add(atlas.findRegion(Constants.ARM_POINT));
            downarmFrames.add(atlas.findRegion(Constants.ARM_LOWER));
            armDown = new Animation(Constants.FLIPSWIPE_FRAME_DURATION, downarmFrames, PlayMode.NORMAL);

            armUp = new Animation(Constants.FLIPSWIPE_FRAME_DURATION, downarmFrames, PlayMode.REVERSED);

            Array<AtlasRegion> armCurlFrames = new Array<AtlasRegion>();
            armCurlFrames.add(atlas.findRegion(Constants.ARM_RELEASE));
            armCurlFrames.add(atlas.findRegion(Constants.ARM_POINT));
            armCurl = new Animation(Constants.STRIDE_FRAME_DURATION, armCurlFrames, PlayMode.LOOP);
            armPush = new Animation(Constants.STRIDE_FRAME_DURATION, armCurlFrames, PlayMode.NORMAL);
            armPull = new Animation(Constants.STRIDE_FRAME_DURATION, armCurlFrames, PlayMode.REVERSED);

            Array<AtlasRegion> armSwingFrames = new Array<AtlasRegion>();
            armSwingFrames.add(atlas.findRegion(Constants.ARM_CLENCH));
            armSwingFrames.add(atlas.findRegion(Constants.ARM_RELAX));
            armSwing = new Animation(Constants.STRIDE_FRAME_DURATION, armSwingFrames, PlayMode.LOOP);

            armSwingFrames.add(atlas.findRegion(Constants.ARM_REACH));
            armAway = new Animation(Constants.STRIDE_FRAME_DURATION, armSwingFrames, PlayMode.NORMAL);
            armToward = new Animation(Constants.STRIDE_FRAME_DURATION, armSwingFrames, PlayMode.REVERSED);

            Array<AtlasRegion> legStrideFrames = new Array<AtlasRegion>();
            legStrideFrames.add(atlas.findRegion(Constants.LEGS_STRIDE_1));
            legStrideFrames.add(atlas.findRegion(Constants.LEGS_STRIDE_2));
            legStrideFrames.add(atlas.findRegion(Constants.LEGS_STRIDE_3));
            legStrideFrames.add(atlas.findRegion(Constants.LEGS_STRIDE_4));
            legStrideFrames.add(atlas.findRegion(Constants.LEGS_STRIDE_5));
            legsStride = new Animation(Constants.STRIDE_FRAME_DURATION, legStrideFrames, PlayMode.LOOP_PINGPONG);

            Array<AtlasRegion> feetStrideFrames = new Array<AtlasRegion>();
            feetStrideFrames.add(atlas.findRegion(Constants.FEET_STRIDE_1));
            feetStrideFrames.add(atlas.findRegion(Constants.FEET_STRIDE_2));
            feetStrideFrames.add(atlas.findRegion(Constants.FEET_STRIDE_3));
            feetStride = new Animation(Constants.STRIDE_FRAME_DURATION, feetStrideFrames, PlayMode.LOOP_PINGPONG);

            Array<AtlasRegion> dashFrames = new Array<AtlasRegion>();
            dashFrames.add(atlas.findRegion(Constants.FEET_DASH_2));
            dashFrames.add(atlas.findRegion(Constants.FEET_DASH_1));
            feetDash = new Animation(Constants.FLICKER_FRAME_DURATION, dashFrames, PlayMode.LOOP);

            Array<AtlasRegion> hoverFrames = new Array<AtlasRegion>();
            hoverFrames.add(atlas.findRegion(Constants.FEET_HOVER_1));
            hoverFrames.add(atlas.findRegion(Constants.FEET_HOVER_2));
            feetHover = new Animation(Constants.FLICKER_FRAME_DURATION, hoverFrames, PlayMode.LOOP);

            stand = atlas.findRegion(Constants.STAND);
            fall = atlas.findRegion(Constants.FALL);
        }
    }

    public static final class BossAssets {
        
        public class ScopedBossAssets {
            public AtlasRegion blockLeft;
            public AtlasRegion blockRight;
            public AtlasRegion lookupBlockLeft;
            public AtlasRegion lookupBlockRight;
            public AtlasRegion standLeft;
            public AtlasRegion standRight;
            public AtlasRegion fallLeft;
            public AtlasRegion fallRight;
            public AtlasRegion dashLeft;
            public AtlasRegion dashRight;
            public AtlasRegion recoilLeft;
            public AtlasRegion recoilRight;
            public AtlasRegion lookupStandLeft;
            public AtlasRegion lookupStandRight;
            public AtlasRegion lookupFallLeft;
            public AtlasRegion lookupFallRight;
            public AtlasRegion lookdownFallLeft;
            public AtlasRegion lookdownFallRight;
        }
        
        LiquidBossAssets liquid;
        GasBossAssets gas;
        PlasmaBossAssets plasma;
        OreBossAssets ore;
        

        private BossAssets(TextureAtlas atlas) {
            liquid = new LiquidBossAssets(atlas);
            gas = new GasBossAssets(atlas);
            plasma = new PlasmaBossAssets(atlas);
            ore = new OreBossAssets(atlas);
        }

        private class LiquidBossAssets extends ScopedBossAssets {

            private LiquidBossAssets(TextureAtlas atlas) {
                blockLeft = atlas.findRegion(Constants.BOSS_LIQUID_BLOCK_LEFT);
                blockRight = atlas.findRegion(Constants.BOSS_LIQUID_BLOCK_RIGHT);
                lookupBlockLeft = atlas.findRegion(Constants.BOSS_LIQUID_LOOKUP_BLOCK_LEFT);
                lookupBlockRight = atlas.findRegion(Constants.BOSS_LIQUID_LOOKUP_BLOCK_RIGHT);
                standLeft = atlas.findRegion(Constants.BOSS_LIQUID_STAND_LEFT);
                standRight = atlas.findRegion(Constants.BOSS_LIQUID_STAND_RIGHT);
                fallLeft = atlas.findRegion(Constants.BOSS_LIQUID_FALL_LEFT);
                fallRight = atlas.findRegion(Constants.BOSS_LIQUID_FALL_RIGHT);
                dashLeft = atlas.findRegion(Constants.BOSS_LIQUID_DASH_LEFT);
                dashRight = atlas.findRegion(Constants.BOSS_LIQUID_DASH_RIGHT);
                recoilLeft = atlas.findRegion(Constants.BOSS_LIQUID_RECOIL_LEFT);
                recoilRight = atlas.findRegion(Constants.BOSS_LIQUID_RECOIL_RIGHT);
                lookupStandLeft = atlas.findRegion(Constants.BOSS_LIQUID_LOOKUP_STAND_LEFT);
                lookupStandRight = atlas.findRegion(Constants.BOSS_LIQUID_LOOKUP_STAND_RIGHT);
                lookupFallLeft = atlas.findRegion(Constants.BOSS_LIQUID_LOOKUP_FALL_LEFT);
                lookupFallRight = atlas.findRegion(Constants.BOSS_LIQUID_LOOKUP_FALL_RIGHT);
                lookdownFallLeft = atlas.findRegion(Constants.BOSS_LIQUID_LOOKDOWN_FALL_LEFT);
                lookdownFallRight = atlas.findRegion(Constants.BOSS_LIQUID_LOOKDOWN_FALL_RIGHT);
            }
        }

        private class GasBossAssets extends ScopedBossAssets {

            private GasBossAssets(TextureAtlas atlas) {
                blockLeft = atlas.findRegion(Constants.BOSS_GAS_BLOCK_LEFT);
                blockRight = atlas.findRegion(Constants.BOSS_GAS_BLOCK_RIGHT);
                lookupBlockLeft = atlas.findRegion(Constants.BOSS_GAS_LOOKUP_BLOCK_LEFT);
                lookupBlockRight = atlas.findRegion(Constants.BOSS_GAS_LOOKUP_BLOCK_RIGHT);
                standLeft = atlas.findRegion(Constants.BOSS_GAS_STAND_LEFT);
                standRight = atlas.findRegion(Constants.BOSS_GAS_STAND_RIGHT);
                fallLeft = atlas.findRegion(Constants.BOSS_GAS_FALL_LEFT);
                fallRight = atlas.findRegion(Constants.BOSS_GAS_FALL_RIGHT);
                dashLeft = atlas.findRegion(Constants.BOSS_GAS_DASH_LEFT);
                dashRight = atlas.findRegion(Constants.BOSS_GAS_DASH_RIGHT);
                recoilLeft = atlas.findRegion(Constants.BOSS_GAS_RECOIL_LEFT);
                recoilRight = atlas.findRegion(Constants.BOSS_GAS_RECOIL_RIGHT);
                lookupStandLeft = atlas.findRegion(Constants.BOSS_GAS_LOOKUP_STAND_LEFT);
                lookupStandRight = atlas.findRegion(Constants.BOSS_GAS_LOOKUP_STAND_RIGHT);
                lookupFallLeft = atlas.findRegion(Constants.BOSS_GAS_LOOKUP_FALL_LEFT);
                lookupFallRight = atlas.findRegion(Constants.BOSS_GAS_LOOKUP_FALL_RIGHT);
                lookdownFallLeft = atlas.findRegion(Constants.BOSS_GAS_LOOKDOWN_FALL_LEFT);
                lookdownFallRight = atlas.findRegion(Constants.BOSS_GAS_LOOKDOWN_FALL_RIGHT);
            }
        }

        private class PlasmaBossAssets extends ScopedBossAssets {

            private PlasmaBossAssets(TextureAtlas atlas) {
                blockLeft = atlas.findRegion(Constants.BOSS_PLASMA_BLOCK_LEFT);
                blockRight = atlas.findRegion(Constants.BOSS_PLASMA_BLOCK_RIGHT);
                lookupBlockLeft = atlas.findRegion(Constants.BOSS_PLASMA_LOOKUP_BLOCK_LEFT);
                lookupBlockRight = atlas.findRegion(Constants.BOSS_PLASMA_LOOKUP_BLOCK_RIGHT);
                standLeft = atlas.findRegion(Constants.BOSS_PLASMA_STAND_LEFT);
                standRight = atlas.findRegion(Constants.BOSS_PLASMA_STAND_RIGHT);
                fallLeft = atlas.findRegion(Constants.BOSS_PLASMA_FALL_LEFT);
                fallRight = atlas.findRegion(Constants.BOSS_PLASMA_FALL_RIGHT);
                dashLeft = atlas.findRegion(Constants.BOSS_PLASMA_DASH_LEFT);
                dashRight = atlas.findRegion(Constants.BOSS_PLASMA_DASH_RIGHT);
                recoilLeft = atlas.findRegion(Constants.BOSS_PLASMA_RECOIL_LEFT);
                recoilRight = atlas.findRegion(Constants.BOSS_PLASMA_RECOIL_RIGHT);
                lookupStandLeft = atlas.findRegion(Constants.BOSS_PLASMA_LOOKUP_STAND_LEFT);
                lookupStandRight = atlas.findRegion(Constants.BOSS_PLASMA_LOOKUP_STAND_RIGHT);
                lookupFallLeft = atlas.findRegion(Constants.BOSS_PLASMA_LOOKUP_FALL_LEFT);
                lookupFallRight = atlas.findRegion(Constants.BOSS_PLASMA_LOOKUP_FALL_RIGHT);
                lookdownFallLeft = atlas.findRegion(Constants.BOSS_PLASMA_LOOKDOWN_FALL_LEFT);
                lookdownFallRight = atlas.findRegion(Constants.BOSS_PLASMA_LOOKDOWN_FALL_RIGHT);
            }
        }

        private class OreBossAssets extends ScopedBossAssets {

            private OreBossAssets(TextureAtlas atlas) {
                blockLeft = atlas.findRegion(Constants.BOSS_ORE_BLOCK_LEFT);
                blockRight = atlas.findRegion(Constants.BOSS_ORE_BLOCK_RIGHT);
                lookupBlockLeft = atlas.findRegion(Constants.BOSS_ORE_LOOKUP_BLOCK_LEFT);
                lookupBlockRight = atlas.findRegion(Constants.BOSS_ORE_LOOKUP_BLOCK_RIGHT);
                standLeft = atlas.findRegion(Constants.BOSS_ORE_STAND_LEFT);
                standRight = atlas.findRegion(Constants.BOSS_ORE_STAND_RIGHT);
                fallLeft = atlas.findRegion(Constants.BOSS_ORE_FALL_LEFT);
                fallRight = atlas.findRegion(Constants.BOSS_ORE_FALL_RIGHT);
                dashLeft = atlas.findRegion(Constants.BOSS_ORE_DASH_LEFT);
                dashRight = atlas.findRegion(Constants.BOSS_ORE_DASH_RIGHT);
                recoilLeft = atlas.findRegion(Constants.BOSS_ORE_RECOIL_LEFT);
                recoilRight = atlas.findRegion(Constants.BOSS_ORE_RECOIL_RIGHT);
                lookupStandLeft = atlas.findRegion(Constants.BOSS_ORE_LOOKUP_STAND_LEFT);
                lookupStandRight = atlas.findRegion(Constants.BOSS_ORE_LOOKUP_STAND_RIGHT);
                lookupFallLeft = atlas.findRegion(Constants.BOSS_ORE_LOOKUP_FALL_LEFT);
                lookupFallRight = atlas.findRegion(Constants.BOSS_ORE_LOOKUP_FALL_RIGHT);
                lookdownFallLeft = atlas.findRegion(Constants.BOSS_ORE_LOOKDOWN_FALL_LEFT);
                lookdownFallRight = atlas.findRegion(Constants.BOSS_ORE_LOOKDOWN_FALL_RIGHT);
            }
        }

        public ScopedBossAssets getScopedBossAssets(Enums.Energy energy) {
            switch (energy) {
                case LIQUID: return liquid;
                case GAS: return gas;
                case PLASMA: return plasma;
                default: return ore;
            }
        }
    }

    public static final class BackgroundAssets {

        public final AtlasRegion home;
        public final AtlasRegion ore;
        public final AtlasRegion plasma;
        public final AtlasRegion liquid;
        public final AtlasRegion gas;
        public final AtlasRegion solid;
        public final AtlasRegion hybrid;

        private BackgroundAssets(TextureAtlas atlas) {
            home = atlas.findRegion(Constants.BACKGROUND_HOME_SPRITE);
            ore = atlas.findRegion(Constants.BACKGROUND_ORE_SPRITE);
            plasma = atlas.findRegion(Constants.BACKGROUND_PLASMA_SPRITE);
            liquid = atlas.findRegion(Constants.BACKGROUND_LIQUID_SPRITE);
            gas = atlas.findRegion(Constants.BACKGROUND_GAS_SPRITE);
            solid = atlas.findRegion(Constants.BACKGROUND_SOLID_SPRITE);
            hybrid = atlas.findRegion(Constants.BACKGROUND_HYBRID_SPRITE);
        }

        public TextureRegion getBackground(Enums.Theme theme) {
            switch (theme) {
                case HOME:
                    return home;
                case MECHANICAL:
                    return ore;
                case ELECTROMAGNETIC:
                    return plasma;
                case NUCLEAR:
                    return gas;
                case THERMAL:
                    return plasma;
                case GRAVITATIONAL:
                    return solid;
                case FINAL:
                    return hybrid;
                default:
                    return gas;
            }
        }
    }

    public static final class GroundAssets {

        public final NinePatch ladderNinePatch;
        public final AtlasRegion deposit;
        public final AtlasRegion lift;
        public final AtlasRegion yCannon;
        public final AtlasRegion xCannon;
        public final AtlasRegion vines;
        public final AtlasRegion rope;
        public final AtlasRegion pillar;
        public final AtlasRegion pod;
        public final AtlasRegion inactiveChamber;
        public final AtlasRegion activeChamber;
        public final Animation<TextureRegion> chargedChamber;
        public final Animation<TextureRegion> knob;
        public final Animation<TextureRegion> pole;
        public final Animation<TextureRegion> slick;
        public final Animation<TextureRegion> ice;
        public final Animation<TextureRegion> treadmillRight;
        public final Animation<TextureRegion> treadmillLeft;
        public final Animation<TextureRegion> loadedSpring;
        public final Animation<TextureRegion> unloadedSpring;
        public final Animation<TextureRegion> loadedLever;
        public final Animation<TextureRegion> unloadedLever;
        public final Animation<TextureRegion> tripknobOn;
        public final Animation<TextureRegion> tripknobOff;
        public final Animation<TextureRegion> triptreadLeftOn;
        public final Animation<TextureRegion> triptreadLeftOff;
        public final Animation<TextureRegion> triptreadRightOn;
        public final Animation<TextureRegion> triptreadRightOff;
        public final Animation<TextureRegion> tripchamberOff;
        public final Animation<TextureRegion> tripchamberOn;
        public final Animation<TextureRegion> activePod;
        public final Animation<TextureRegion> sink;
        public final Animation<TextureRegion> coals;
        public final Animation<TextureRegion> lava;
        public final Animation<TextureRegion> waves;
        public final Animation<TextureRegion> gateOpen;
        public final Animation<TextureRegion> gateClose;
        public final NinePatch barrier;
        public final NinePatch box;
        public final NinePatch block;

        private GroundAssets(TextureAtlas atlas) {

            int edge = Constants.BLOCK_EDGE;

            barrier = new NinePatch(atlas.findRegion(Constants.BARRIER_SPRITE), edge, edge, edge, edge);
            box = new NinePatch(atlas.findRegion(Constants.BOX_SPRITE), 4, 2, 39, 15);
            block = new NinePatch(atlas.findRegion(Constants.BLOCK_SPRITE), edge, edge, edge, edge);

            deposit = atlas.findRegion(Constants.BOX_DEPOSIT_SPRITE);
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
            treadmillRegions.add(atlas.findRegion(Constants.TREADMILL_SPRITE_1_RIGHT));
            treadmillRegions.add(atlas.findRegion(Constants.TREADMILL_SPRITE_2_RIGHT));

            treadmillRight = new Animation(Constants.TREADMILL_DURATION / treadmillRegions.size,
                    treadmillRegions, PlayMode.NORMAL);

            treadmillRegions.clear();

            treadmillRegions = new Array<AtlasRegion>();
            treadmillRegions.add(atlas.findRegion(Constants.TREADMILL_SPRITE_1_LEFT));
            treadmillRegions.add(atlas.findRegion(Constants.TREADMILL_SPRITE_2_LEFT));

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

            Array<AtlasRegion> leverRegions = new Array<AtlasRegion>();
            leverRegions.add(atlas.findRegion(Constants.LEVER_SPRITE_1));
            leverRegions.add(atlas.findRegion(Constants.LEVER_SPRITE_2));
            leverRegions.add(atlas.findRegion(Constants.LEVER_SPRITE_4));
            leverRegions.add(atlas.findRegion(Constants.LEVER_SPRITE_3));

            loadedLever = new Animation(Constants.LEVER_LOAD_DURATION / leverRegions.size,
                    leverRegions, PlayMode.NORMAL);


            unloadedLever = new Animation(Constants.LEVER_UNLOAD_DURATION / leverRegions.size,
                    leverRegions, PlayMode.REVERSED);

            Array<AtlasRegion> tripknobRegions = new Array<AtlasRegion>();
            tripknobRegions.add(atlas.findRegion(Constants.TRIPKNOB_SPRITE_1));
            tripknobRegions.add(atlas.findRegion(Constants.TRIPKNOB_SPRITE_2));
            tripknobRegions.add(atlas.findRegion(Constants.TRIPKNOB_SPRITE_3));
            tripknobRegions.add(atlas.findRegion(Constants.TRIPKNOB_SPRITE_4));

            tripknobOn = new Animation(Constants.TRIPKNOB_LOAD_DURATION / tripknobRegions.size,
                    tripknobRegions, PlayMode.NORMAL);


            tripknobOff = new Animation(Constants.TRIPKNOB_UNLOAD_DURATION / tripknobRegions.size,
                    tripknobRegions, PlayMode.REVERSED);

            Array<AtlasRegion> triptreadLeftOffRegions = new Array<AtlasRegion>();
            triptreadLeftOffRegions.add(atlas.findRegion(Constants.TRIPTREAD_SPRITE_1_LEFT_OFF));
            triptreadLeftOffRegions.add(atlas.findRegion(Constants.TRIPTREAD_SPRITE_2_LEFT_OFF));
            triptreadLeftOff = new Animation(Constants.TRIPTREAD_DURATION / triptreadLeftOffRegions.size,
                    triptreadLeftOffRegions, PlayMode.LOOP);


            Array<AtlasRegion> triptreadLeftOnRegions = new Array<AtlasRegion>();
            triptreadLeftOnRegions.add(atlas.findRegion(Constants.TRIPTREAD_SPRITE_1_LEFT_ON));
            triptreadLeftOnRegions.add(atlas.findRegion(Constants.TRIPTREAD_SPRITE_2_LEFT_ON));
            triptreadLeftOn = new Animation(Constants.TRIPTREAD_DURATION / triptreadLeftOffRegions.size,
                    triptreadLeftOnRegions, PlayMode.LOOP);

            Array<AtlasRegion> triptreadRightOffRegions = new Array<AtlasRegion>();
            triptreadRightOffRegions.add(atlas.findRegion(Constants.TRIPTREAD_SPRITE_1_RIGHT_OFF));
            triptreadRightOffRegions.add(atlas.findRegion(Constants.TRIPTREAD_SPRITE_2_RIGHT_OFF));
            triptreadRightOff = new Animation(Constants.TRIPTREAD_DURATION / triptreadRightOffRegions.size,
                    triptreadRightOffRegions, PlayMode.LOOP);


            Array<AtlasRegion> triptreadRightOnRegions = new Array<AtlasRegion>();
            triptreadRightOnRegions.add(atlas.findRegion(Constants.TRIPTREAD_SPRITE_1_RIGHT_ON));
            triptreadRightOnRegions.add(atlas.findRegion(Constants.TRIPTREAD_SPRITE_2_RIGHT_ON));
            triptreadRightOn = new Animation(Constants.TRIPTREAD_DURATION / triptreadRightOffRegions.size,
                    triptreadRightOnRegions, PlayMode.LOOP);

            Array<AtlasRegion> tripchamberOffRegions = new Array<AtlasRegion>();
            tripchamberOffRegions.add(atlas.findRegion(Constants.TRIPCHAMBER_SPRITE_1_OFF));
            tripchamberOffRegions.add(atlas.findRegion(Constants.TRIPCHAMBER_SPRITE_2_OFF));

            tripchamberOff = new Animation(Constants.TRIPCHAMBER_LOAD_DURATION / tripchamberOffRegions.size,
                    tripchamberOffRegions, PlayMode.LOOP);

            Array<AtlasRegion> tripchamberOnRegions = new Array<AtlasRegion>();
            tripchamberOnRegions.add(atlas.findRegion(Constants.TRIPCHAMBER_SPRITE_1_ON));
            tripchamberOnRegions.add(atlas.findRegion(Constants.TRIPCHAMBER_SPRITE_2_ON));

            tripchamberOn = new Animation(Constants.TRIPCHAMBER_LOAD_DURATION / tripchamberOffRegions.size,
                    tripchamberOnRegions, PlayMode.LOOP);


            Array<AtlasRegion> gateRegions = new Array<AtlasRegion>();
            gateRegions.add(atlas.findRegion(Constants.GATE_SPRITE_0));
            gateRegions.add(atlas.findRegion(Constants.GATE_SPRITE_1));
            gateRegions.add(atlas.findRegion(Constants.GATE_SPRITE_2));
            gateRegions.add(atlas.findRegion(Constants.GATE_SPRITE_2));
            gateRegions.add(atlas.findRegion(Constants.GATE_SPRITE_3));
            gateRegions.add(atlas.findRegion(Constants.GATE_SPRITE_4));
            gateRegions.add(atlas.findRegion(Constants.GATE_SPRITE_5));

            gateOpen = new Animation(Constants.GATE_FRAME_DURATION, gateRegions, PlayMode.NORMAL);
            gateClose = new Animation(Constants.GATE_FRAME_DURATION, gateRegions, PlayMode.REVERSED);

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

            sink = new Animation(Constants.SINK_DURATION / sinkRegions.size,
                    sinkRegions, PlayMode.LOOP);

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

            Array<AtlasRegion> wavesRegions = new Array<AtlasRegion>();
            wavesRegions.add(atlas.findRegion(Constants.WAVES_SPRITE_1));
            wavesRegions.add(atlas.findRegion(Constants.WAVES_SPRITE_2));

            waves = new Animation(Constants.WAVES_DURATION / wavesRegions.size,
                    wavesRegions, PlayMode.NORMAL);
        }

        public final NinePatch getNinePatch(Ground ground) {
            if (ground instanceof Box) {
                return box;
            } else if (ground instanceof Brick) {
                return block;
            }
            return barrier;
        }
    }

    public static final class AmmoAssets {

        public final Animation<TextureRegion> nativeShot;
        public final Animation<TextureRegion> nativeBlast;
        public final Animation<TextureRegion> gasShot;
        public final Animation<TextureRegion> gasBlast;
        public final Animation<TextureRegion> liquidShot;
        public final Animation<TextureRegion> liquidBlast;
        public final Animation<TextureRegion> plasmaShot;
        public final Animation<TextureRegion> plasmaBlast;
        public final Animation<TextureRegion> oreShot;
        public final Animation<TextureRegion> oreBlast;
        public final Animation<TextureRegion> solidShot;
        public final Animation<TextureRegion> solidBlast;
        public final Animation<TextureRegion> antimatterShot;
        public final Animation<TextureRegion> antimatterBlast;
        public final Animation<TextureRegion> hybridShot;
        public final Animation<TextureRegion> hybridBlast;

        private AmmoAssets(TextureAtlas atlas) {

            Array<AtlasRegion> nativeShotRegions = new Array<AtlasRegion>();
            nativeShotRegions.add(atlas.findRegion(Constants.SHOT_NATIVE_SPRITE_1));
            nativeShotRegions.add(atlas.findRegion(Constants.SHOT_NATIVE_SPRITE_2));
            nativeShot = new Animation(Constants.SHOT_FRAME_DURATION / nativeShotRegions.size, nativeShotRegions, PlayMode.LOOP);

            Array<AtlasRegion> nativeBlastRegions = new Array<AtlasRegion>();
            nativeBlastRegions.add(atlas.findRegion(Constants.BLAST_NATIVE_SPRITE_1));
            nativeBlastRegions.add(atlas.findRegion(Constants.BLAST_NATIVE_SPRITE_2));
            nativeBlastRegions.add(atlas.findRegion(Constants.BLAST_NATIVE_SPRITE_3));
            nativeBlast = new Animation(Constants.SHOT_FRAME_DURATION, nativeBlastRegions, PlayMode.LOOP_PINGPONG);

            Array<AtlasRegion> oreShotRegions = new Array<AtlasRegion>();
            oreShotRegions.add(atlas.findRegion(Constants.SHOT_ORE_SPRITE_1));
            oreShotRegions.add(atlas.findRegion(Constants.SHOT_ORE_SPRITE_2));
            oreShot = new Animation(Constants.SHOT_FRAME_DURATION, oreShotRegions, PlayMode.LOOP);

            Array<AtlasRegion> oreBlastRegions = new Array<AtlasRegion>();
            oreBlastRegions.add(atlas.findRegion(Constants.BLAST_ORE_SPRITE_1));
            oreBlastRegions.add(atlas.findRegion(Constants.BLAST_ORE_SPRITE_2));
            oreBlastRegions.add(atlas.findRegion(Constants.BLAST_ORE_SPRITE_3));
            oreBlast = new Animation(Constants.SHOT_FRAME_DURATION, oreBlastRegions, PlayMode.LOOP_PINGPONG);

            Array<AtlasRegion> gasShotRegions = new Array<AtlasRegion>();
            gasShotRegions.add(atlas.findRegion(Constants.SHOT_GAS_SPRITE_1));
            gasShotRegions.add(atlas.findRegion(Constants.SHOT_GAS_SPRITE_2));
            gasShot = new Animation(Constants.SHOT_FRAME_DURATION, gasShotRegions, PlayMode.LOOP);

            Array<AtlasRegion> gasBlastRegions = new Array<AtlasRegion>();
            gasBlastRegions.add(atlas.findRegion(Constants.BLAST_GAS_SPRITE_1));
            gasBlastRegions.add(atlas.findRegion(Constants.BLAST_GAS_SPRITE_2));
            gasBlastRegions.add(atlas.findRegion(Constants.BLAST_GAS_SPRITE_3));
            gasBlast = new Animation(Constants.SHOT_FRAME_DURATION, gasBlastRegions, PlayMode.LOOP);

            Array<AtlasRegion> liquidShotRegions = new Array<AtlasRegion>();
            liquidShotRegions.add(atlas.findRegion(Constants.SHOT_LIQUID_SPRITE_1));
            liquidShotRegions.add(atlas.findRegion(Constants.SHOT_LIQUID_SPRITE_2));
            liquidShot = new Animation(Constants.SHOT_FRAME_DURATION, liquidShotRegions, PlayMode.LOOP);

            Array<AtlasRegion> liquidBlastRegions = new Array<AtlasRegion>();
            liquidBlastRegions.add(atlas.findRegion(Constants.BLAST_LIQUID_SPRITE_1));
            liquidBlastRegions.add(atlas.findRegion(Constants.BLAST_LIQUID_SPRITE_2));
            liquidBlastRegions.add(atlas.findRegion(Constants.BLAST_LIQUID_SPRITE_3));
            liquidBlast = new Animation(Constants.SHOT_FRAME_DURATION, liquidBlastRegions, PlayMode.LOOP_PINGPONG);

            Array<AtlasRegion> solidShotRegions = new Array<AtlasRegion>();
            solidShotRegions.add(atlas.findRegion(Constants.SHOT_SOLID_SPRITE_1));
            solidShotRegions.add(atlas.findRegion(Constants.SHOT_SOLID_SPRITE_2));
            solidShot = new Animation(Constants.SHOT_FRAME_DURATION, solidShotRegions, PlayMode.LOOP);

            Array<AtlasRegion> solidBlastRegions = new Array<AtlasRegion>();
            solidBlastRegions.add(atlas.findRegion(Constants.BLAST_SOLID_SPRITE_1));
            solidBlastRegions.add(atlas.findRegion(Constants.BLAST_SOLID_SPRITE_2));
            solidBlastRegions.add(atlas.findRegion(Constants.BLAST_SOLID_SPRITE_3));
            solidBlast = new Animation(Constants.SHOT_FRAME_DURATION, solidBlastRegions, PlayMode.LOOP_PINGPONG);

            Array<AtlasRegion> plasmaShotRegions = new Array<AtlasRegion>();
            plasmaShotRegions.add(atlas.findRegion(Constants.SHOT_PLASMA_SPRITE_1));
            plasmaShotRegions.add(atlas.findRegion(Constants.SHOT_PLASMA_SPRITE_2));
            plasmaShot = new Animation(Constants.SHOT_FRAME_DURATION, plasmaShotRegions, PlayMode.LOOP);

            Array<AtlasRegion> plasmaBlastRegions = new Array<AtlasRegion>();
            plasmaBlastRegions.add(atlas.findRegion(Constants.BLAST_PLASMA_SPRITE_1));
            plasmaBlastRegions.add(atlas.findRegion(Constants.BLAST_PLASMA_SPRITE_2));
            plasmaBlastRegions.add(atlas.findRegion(Constants.BLAST_PLASMA_SPRITE_3));
            plasmaBlast = new Animation(Constants.SHOT_FRAME_DURATION, plasmaBlastRegions, PlayMode.LOOP_PINGPONG);

            Array<AtlasRegion> antimatterShotRegions = new Array<AtlasRegion>();
            antimatterShotRegions.add(atlas.findRegion(Constants.SHOT_ANTIMATTER_SPRITE_1));
            antimatterShotRegions.add(atlas.findRegion(Constants.SHOT_ANTIMATTER_SPRITE_2));
            antimatterShot = new Animation(Constants.SHOT_FRAME_DURATION, antimatterShotRegions, PlayMode.LOOP);

            Array<AtlasRegion> antimatterBlastRegions = new Array<AtlasRegion>();
            antimatterBlastRegions.add(atlas.findRegion(Constants.BLAST_ANTIMATTER_SPRITE_1));
            antimatterBlastRegions.add(atlas.findRegion(Constants.BLAST_ANTIMATTER_SPRITE_2));
            antimatterBlastRegions.add(atlas.findRegion(Constants.BLAST_ANTIMATTER_SPRITE_3));
            antimatterBlast = new Animation(Constants.SHOT_FRAME_DURATION, antimatterBlastRegions, PlayMode.LOOP_PINGPONG);

            Array<AtlasRegion> hybridShotRegions = new Array<AtlasRegion>();
            hybridShotRegions.add(atlas.findRegion(Constants.SHOT_HYBRID_SPRITE));
            hybridShotRegions.add(atlas.findRegion(Constants.SHOT_HYBRID_SPRITE_2));
            hybridShot = new Animation(Constants.SHOT_FRAME_DURATION, hybridShotRegions, PlayMode.LOOP);

            Array<AtlasRegion> hybridBlastRegions = new Array<AtlasRegion>();
            hybridBlastRegions.add(atlas.findRegion(Constants.BLAST_HYBRID_SPRITE_1));
            hybridBlastRegions.add(atlas.findRegion(Constants.BLAST_HYBRID_SPRITE_2));
            hybridBlastRegions.add(atlas.findRegion(Constants.BLAST_HYBRID_SPRITE_3));
            hybridBlast = new Animation(Constants.SHOT_FRAME_DURATION, hybridBlastRegions, PlayMode.LOOP);
        }
    }

    public static final class BladeAssets {

        public final Animation<TextureRegion> nativeBackflip;
        public final Animation<TextureRegion> nativeForehand;
        public final Animation<TextureRegion> nativeUppercut;
        public final Animation<TextureRegion> liquidBackflip;
        public final Animation<TextureRegion> liquidForehand;
        public final Animation<TextureRegion> liquidUppercut;
        public final Animation<TextureRegion> plasmaBackflip;
        public final Animation<TextureRegion> plasmaForehand;
        public final Animation<TextureRegion> plasmaUppercut;
        public final Animation<TextureRegion> gasBackflip;
        public final Animation<TextureRegion> gasForehand;
        public final Animation<TextureRegion> gasUppercut;
        public final Animation<TextureRegion> solidBackflip;
        public final Animation<TextureRegion> solidForehand;
        public final Animation<TextureRegion> solidUppercut;
        public final Animation<TextureRegion> oreBackflip;
        public final Animation<TextureRegion> oreForehand;
        public final Animation<TextureRegion> oreUppercut;
        public final Animation<TextureRegion> antimatterBackflip;
        public final Animation<TextureRegion> antimatterForehand;
        public final Animation<TextureRegion> antimatterUppercut;
        public final Animation<TextureRegion> hybridBackflip;
        public final Animation<TextureRegion> hybridForehand;
        public final Animation<TextureRegion> hybridUppercut;

        private BladeAssets(TextureAtlas atlas) {

            Array<AtlasRegion> flipSwipeNativeRegions = new Array<AtlasRegion>();
            flipSwipeNativeRegions.add(atlas.findRegion(Constants.FLIPSWIPE_NATIVE_SPRITE_1));
            flipSwipeNativeRegions.add(atlas.findRegion(Constants.SIDESWIPE_NATIVE_SPRITE_3));
            flipSwipeNativeRegions.add(atlas.findRegion(Constants.FLIPSWIPE_NATIVE_SPRITE_3));
            flipSwipeNativeRegions.add(atlas.findRegion(Constants.FLIPSWIPE_NATIVE_SPRITE_4));
            flipSwipeNativeRegions.add(atlas.findRegion(Constants.FLIPSWIPE_NATIVE_SPRITE_5));
            flipSwipeNativeRegions.add(atlas.findRegion(Constants.FLIPSWIPE_NATIVE_SPRITE_6));
            nativeBackflip = new Animation(Constants.FLIPSWIPE_FRAME_DURATION, flipSwipeNativeRegions, PlayMode.LOOP);

            Array<AtlasRegion> sideSwipeNativeRegions = new Array<AtlasRegion>();
            sideSwipeNativeRegions.add(atlas.findRegion(Constants.SIDESWIPE_NATIVE_SPRITE_1));
            sideSwipeNativeRegions.add(atlas.findRegion(Constants.SIDESWIPE_NATIVE_SPRITE_2));
            sideSwipeNativeRegions.add(atlas.findRegion(Constants.SIDESWIPE_NATIVE_SPRITE_3));
            sideSwipeNativeRegions.add(atlas.findRegion(Constants.SIDESWIPE_NATIVE_SPRITE_3));
            nativeForehand = new Animation(Constants.STRIDE_FRAME_DURATION, sideSwipeNativeRegions, PlayMode.NORMAL);

            Array<AtlasRegion> vertSwipeNativeRegions = new Array<AtlasRegion>();
            vertSwipeNativeRegions.add(atlas.findRegion(Constants.FLIPSWIPE_NATIVE_SPRITE_1));
            vertSwipeNativeRegions.add(atlas.findRegion(Constants.SIDESWIPE_NATIVE_SPRITE_3));
            vertSwipeNativeRegions.add(atlas.findRegion(Constants.FLIPSWIPE_NATIVE_SPRITE_3));
            vertSwipeNativeRegions.add(atlas.findRegion(Constants.FLIPSWIPE_NATIVE_SPRITE_2));
            nativeUppercut = new Animation(Constants.STRIDE_FRAME_DURATION, vertSwipeNativeRegions, PlayMode.NORMAL);

            Array<AtlasRegion> flipSwipeLiquidRegions = new Array<AtlasRegion>();
            flipSwipeLiquidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_LIQUID_SPRITE_1));
            flipSwipeLiquidRegions.add(atlas.findRegion(Constants.SIDESWIPE_LIQUID_SPRITE_3));
            flipSwipeLiquidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_LIQUID_SPRITE_3));
            flipSwipeLiquidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_LIQUID_SPRITE_4));
            flipSwipeLiquidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_LIQUID_SPRITE_5));
            flipSwipeLiquidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_LIQUID_SPRITE_6));
            liquidBackflip = new Animation(Constants.FLIPSWIPE_FRAME_DURATION, flipSwipeLiquidRegions, PlayMode.LOOP);

            Array<AtlasRegion> sideSwipeLiquidRegions = new Array<AtlasRegion>();
            sideSwipeLiquidRegions.add(atlas.findRegion(Constants.SIDESWIPE_LIQUID_SPRITE_1));
            sideSwipeLiquidRegions.add(atlas.findRegion(Constants.SIDESWIPE_LIQUID_SPRITE_2));
            sideSwipeLiquidRegions.add(atlas.findRegion(Constants.SIDESWIPE_LIQUID_SPRITE_3));
            sideSwipeLiquidRegions.add(atlas.findRegion(Constants.SIDESWIPE_LIQUID_SPRITE_3));
            liquidForehand = new Animation(Constants.STRIDE_FRAME_DURATION, sideSwipeLiquidRegions, PlayMode.NORMAL);

            Array<AtlasRegion> vertSwipeLiquidRegions = new Array<AtlasRegion>();
            vertSwipeLiquidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_LIQUID_SPRITE_1));
            vertSwipeLiquidRegions.add(atlas.findRegion(Constants.SIDESWIPE_LIQUID_SPRITE_3));
            vertSwipeLiquidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_LIQUID_SPRITE_3));
            vertSwipeLiquidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_LIQUID_SPRITE_2));
            liquidUppercut = new Animation(Constants.STRIDE_FRAME_DURATION, vertSwipeLiquidRegions, PlayMode.NORMAL);

            Array<AtlasRegion> flipSwipePlasmaRegions = new Array<AtlasRegion>();
            flipSwipePlasmaRegions.add(atlas.findRegion(Constants.FLIPSWIPE_PLASMA_SPRITE_1));
            flipSwipePlasmaRegions.add(atlas.findRegion(Constants.SIDESWIPE_PLASMA_SPRITE_3));
            flipSwipePlasmaRegions.add(atlas.findRegion(Constants.FLIPSWIPE_PLASMA_SPRITE_3));
            flipSwipePlasmaRegions.add(atlas.findRegion(Constants.FLIPSWIPE_PLASMA_SPRITE_4));
            flipSwipePlasmaRegions.add(atlas.findRegion(Constants.FLIPSWIPE_PLASMA_SPRITE_5));
            flipSwipePlasmaRegions.add(atlas.findRegion(Constants.FLIPSWIPE_PLASMA_SPRITE_6));
            plasmaBackflip = new Animation(Constants.FLIPSWIPE_FRAME_DURATION, flipSwipePlasmaRegions, PlayMode.LOOP);

            Array<AtlasRegion> sideSwipePlasmaRegions = new Array<AtlasRegion>();
            sideSwipePlasmaRegions.add(atlas.findRegion(Constants.SIDESWIPE_PLASMA_SPRITE_1));
            sideSwipePlasmaRegions.add(atlas.findRegion(Constants.SIDESWIPE_PLASMA_SPRITE_2));
            sideSwipePlasmaRegions.add(atlas.findRegion(Constants.SIDESWIPE_PLASMA_SPRITE_3));
            sideSwipePlasmaRegions.add(atlas.findRegion(Constants.SIDESWIPE_PLASMA_SPRITE_3));
            plasmaForehand = new Animation(Constants.STRIDE_FRAME_DURATION, sideSwipePlasmaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> vertSwipePlasmaRegions = new Array<AtlasRegion>();
            vertSwipePlasmaRegions.add(atlas.findRegion(Constants.FLIPSWIPE_PLASMA_SPRITE_1));
            vertSwipePlasmaRegions.add(atlas.findRegion(Constants.SIDESWIPE_PLASMA_SPRITE_3));
            vertSwipePlasmaRegions.add(atlas.findRegion(Constants.FLIPSWIPE_PLASMA_SPRITE_3));
            vertSwipePlasmaRegions.add(atlas.findRegion(Constants.FLIPSWIPE_PLASMA_SPRITE_2));
            plasmaUppercut = new Animation(Constants.STRIDE_FRAME_DURATION, vertSwipePlasmaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> flipSwipeGasRegions = new Array<AtlasRegion>();
            flipSwipeGasRegions.add(atlas.findRegion(Constants.FLIPSWIPE_GAS_SPRITE_1));
            flipSwipeGasRegions.add(atlas.findRegion(Constants.SIDESWIPE_GAS_SPRITE_3));
            flipSwipeGasRegions.add(atlas.findRegion(Constants.FLIPSWIPE_GAS_SPRITE_3));
            flipSwipeGasRegions.add(atlas.findRegion(Constants.FLIPSWIPE_GAS_SPRITE_4));
            flipSwipeGasRegions.add(atlas.findRegion(Constants.FLIPSWIPE_GAS_SPRITE_5));
            flipSwipeGasRegions.add(atlas.findRegion(Constants.FLIPSWIPE_GAS_SPRITE_6));
            gasBackflip = new Animation(Constants.FLIPSWIPE_FRAME_DURATION, flipSwipeGasRegions, PlayMode.LOOP);

            Array<AtlasRegion> sideSwipeGasRegions = new Array<AtlasRegion>();
            sideSwipeGasRegions.add(atlas.findRegion(Constants.SIDESWIPE_GAS_SPRITE_1));
            sideSwipeGasRegions.add(atlas.findRegion(Constants.SIDESWIPE_GAS_SPRITE_2));
            sideSwipeGasRegions.add(atlas.findRegion(Constants.SIDESWIPE_GAS_SPRITE_3));
            sideSwipeGasRegions.add(atlas.findRegion(Constants.SIDESWIPE_GAS_SPRITE_3));
            gasForehand = new Animation(Constants.STRIDE_FRAME_DURATION, sideSwipeGasRegions, PlayMode.NORMAL);

            Array<AtlasRegion> vertSwipeGasRegions = new Array<AtlasRegion>();
            vertSwipeGasRegions.add(atlas.findRegion(Constants.FLIPSWIPE_GAS_SPRITE_1));
            vertSwipeGasRegions.add(atlas.findRegion(Constants.SIDESWIPE_GAS_SPRITE_3));
            vertSwipeGasRegions.add(atlas.findRegion(Constants.FLIPSWIPE_GAS_SPRITE_3));
            vertSwipeGasRegions.add(atlas.findRegion(Constants.FLIPSWIPE_GAS_SPRITE_2));
            gasUppercut = new Animation(Constants.STRIDE_FRAME_DURATION, vertSwipeGasRegions, PlayMode.NORMAL);

            Array<AtlasRegion> flipSwipeSolidRegions = new Array<AtlasRegion>();
            flipSwipeSolidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_SOLID_SPRITE_1));
            flipSwipeSolidRegions.add(atlas.findRegion(Constants.SIDESWIPE_SOLID_SPRITE_3));
            flipSwipeSolidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_SOLID_SPRITE_3));
            flipSwipeSolidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_SOLID_SPRITE_4));
            flipSwipeSolidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_SOLID_SPRITE_5));
            flipSwipeSolidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_SOLID_SPRITE_6));
            solidBackflip = new Animation(Constants.FLIPSWIPE_FRAME_DURATION, flipSwipeSolidRegions, PlayMode.LOOP);

            Array<AtlasRegion> sideSwipeSolidRegions = new Array<AtlasRegion>();
            sideSwipeSolidRegions.add(atlas.findRegion(Constants.SIDESWIPE_SOLID_SPRITE_1));
            sideSwipeSolidRegions.add(atlas.findRegion(Constants.SIDESWIPE_SOLID_SPRITE_2));
            sideSwipeSolidRegions.add(atlas.findRegion(Constants.SIDESWIPE_SOLID_SPRITE_3));
            sideSwipeSolidRegions.add(atlas.findRegion(Constants.SIDESWIPE_SOLID_SPRITE_3));
            solidForehand = new Animation(Constants.STRIDE_FRAME_DURATION, sideSwipeSolidRegions, PlayMode.NORMAL);

            Array<AtlasRegion> vertSwipeSolidRegions = new Array<AtlasRegion>();
            vertSwipeSolidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_SOLID_SPRITE_1));
            vertSwipeSolidRegions.add(atlas.findRegion(Constants.SIDESWIPE_SOLID_SPRITE_3));
            vertSwipeSolidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_SOLID_SPRITE_3));
            vertSwipeSolidRegions.add(atlas.findRegion(Constants.FLIPSWIPE_SOLID_SPRITE_2));
            solidUppercut = new Animation(Constants.STRIDE_FRAME_DURATION, vertSwipeSolidRegions, PlayMode.NORMAL);

            Array<AtlasRegion> flipSwipeOreRegions = new Array<AtlasRegion>();
            flipSwipeOreRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ORE_SPRITE_1));
            flipSwipeOreRegions.add(atlas.findRegion(Constants.SIDESWIPE_ORE_SPRITE_3));
            flipSwipeOreRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ORE_SPRITE_3));
            flipSwipeOreRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ORE_SPRITE_4));
            flipSwipeOreRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ORE_SPRITE_5));
            flipSwipeOreRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ORE_SPRITE_6));
            oreBackflip = new Animation(Constants.FLIPSWIPE_FRAME_DURATION, flipSwipeOreRegions, PlayMode.LOOP);

            Array<AtlasRegion> sideSwipeOreRegions = new Array<AtlasRegion>();
            sideSwipeOreRegions.add(atlas.findRegion(Constants.SIDESWIPE_ORE_SPRITE_1));
            sideSwipeOreRegions.add(atlas.findRegion(Constants.SIDESWIPE_ORE_SPRITE_2));
            sideSwipeOreRegions.add(atlas.findRegion(Constants.SIDESWIPE_ORE_SPRITE_3));
            sideSwipeOreRegions.add(atlas.findRegion(Constants.SIDESWIPE_ORE_SPRITE_3));
            oreForehand = new Animation(Constants.STRIDE_FRAME_DURATION, sideSwipeOreRegions, PlayMode.NORMAL);

            Array<AtlasRegion> vertSwipeOreRegions = new Array<AtlasRegion>();
            vertSwipeOreRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ORE_SPRITE_1));
            vertSwipeOreRegions.add(atlas.findRegion(Constants.SIDESWIPE_ORE_SPRITE_3));
            vertSwipeOreRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ORE_SPRITE_3));
            vertSwipeOreRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ORE_SPRITE_2));
            oreUppercut = new Animation(Constants.STRIDE_FRAME_DURATION, vertSwipeOreRegions, PlayMode.NORMAL);

            Array<AtlasRegion> flipSwipeAntimatterRegions = new Array<AtlasRegion>();
            flipSwipeAntimatterRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ANTIMATTER_SPRITE_1));
            flipSwipeAntimatterRegions.add(atlas.findRegion(Constants.SIDESWIPE_ANTIMATTER_SPRITE_3));
            flipSwipeAntimatterRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ANTIMATTER_SPRITE_3));
            flipSwipeAntimatterRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ANTIMATTER_SPRITE_4));
            flipSwipeAntimatterRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ANTIMATTER_SPRITE_5));
            flipSwipeAntimatterRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ANTIMATTER_SPRITE_6));
            antimatterBackflip = new Animation(Constants.FLIPSWIPE_FRAME_DURATION, flipSwipeAntimatterRegions, PlayMode.LOOP);

            Array<AtlasRegion> sideSwipeAntimatterRegions = new Array<AtlasRegion>();
            sideSwipeAntimatterRegions.add(atlas.findRegion(Constants.SIDESWIPE_ANTIMATTER_SPRITE_1));
            sideSwipeAntimatterRegions.add(atlas.findRegion(Constants.SIDESWIPE_ANTIMATTER_SPRITE_2));
            sideSwipeAntimatterRegions.add(atlas.findRegion(Constants.SIDESWIPE_ANTIMATTER_SPRITE_3));
            sideSwipeAntimatterRegions.add(atlas.findRegion(Constants.SIDESWIPE_ANTIMATTER_SPRITE_3));
            antimatterForehand = new Animation(Constants.STRIDE_FRAME_DURATION, sideSwipeAntimatterRegions, PlayMode.NORMAL);

            Array<AtlasRegion> vertSwipeAntimatterRegions = new Array<AtlasRegion>();
            vertSwipeAntimatterRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ANTIMATTER_SPRITE_1));
            vertSwipeAntimatterRegions.add(atlas.findRegion(Constants.SIDESWIPE_ANTIMATTER_SPRITE_3));
            vertSwipeAntimatterRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ANTIMATTER_SPRITE_3));
            vertSwipeAntimatterRegions.add(atlas.findRegion(Constants.FLIPSWIPE_ANTIMATTER_SPRITE_2));
            antimatterUppercut = new Animation(Constants.STRIDE_FRAME_DURATION, vertSwipeAntimatterRegions, PlayMode.NORMAL);

            Array<AtlasRegion> flipSwipeHybridRegions = new Array<AtlasRegion>();
            flipSwipeHybridRegions.add(atlas.findRegion(Constants.FLIPSWIPE_HYBRID_SPRITE_1));
            flipSwipeHybridRegions.add(atlas.findRegion(Constants.SIDESWIPE_HYBRID_SPRITE_3));
            flipSwipeHybridRegions.add(atlas.findRegion(Constants.FLIPSWIPE_HYBRID_SPRITE_3));
            flipSwipeHybridRegions.add(atlas.findRegion(Constants.FLIPSWIPE_HYBRID_SPRITE_4));
            flipSwipeHybridRegions.add(atlas.findRegion(Constants.FLIPSWIPE_HYBRID_SPRITE_5));
            flipSwipeHybridRegions.add(atlas.findRegion(Constants.FLIPSWIPE_HYBRID_SPRITE_6));
            hybridBackflip = new Animation(Constants.FLIPSWIPE_FRAME_DURATION, flipSwipeHybridRegions, PlayMode.LOOP);

            Array<AtlasRegion> sideSwipeHybridRegions = new Array<AtlasRegion>();
            sideSwipeHybridRegions.add(atlas.findRegion(Constants.SIDESWIPE_HYBRID_SPRITE_1));
            sideSwipeHybridRegions.add(atlas.findRegion(Constants.SIDESWIPE_HYBRID_SPRITE_2));
            sideSwipeHybridRegions.add(atlas.findRegion(Constants.SIDESWIPE_HYBRID_SPRITE_3));
            sideSwipeHybridRegions.add(atlas.findRegion(Constants.SIDESWIPE_HYBRID_SPRITE_3));
            hybridForehand = new Animation(Constants.STRIDE_FRAME_DURATION, sideSwipeHybridRegions, PlayMode.NORMAL);

            Array<AtlasRegion> vertSwipeHybridRegions = new Array<AtlasRegion>();
            vertSwipeHybridRegions.add(atlas.findRegion(Constants.FLIPSWIPE_HYBRID_SPRITE_1));
            vertSwipeHybridRegions.add(atlas.findRegion(Constants.SIDESWIPE_HYBRID_SPRITE_3));
            vertSwipeHybridRegions.add(atlas.findRegion(Constants.FLIPSWIPE_HYBRID_SPRITE_3));
            vertSwipeHybridRegions.add(atlas.findRegion(Constants.FLIPSWIPE_HYBRID_SPRITE_2));
            hybridUppercut = new Animation(Constants.STRIDE_FRAME_DURATION, vertSwipeHybridRegions, PlayMode.NORMAL);
        }
    }

    public static final class CanirolAssets {

        public final Animation<TextureRegion> xLeftCanirol;
        public final Animation<TextureRegion> xRightCanirol;
        public final Animation<TextureRegion> yCanirol;

        private CanirolAssets(TextureAtlas atlas) {
            Array<AtlasRegion> xLeftCanirolRegions = new Array<AtlasRegion>();
            xLeftCanirolRegions.add(atlas.findRegion(Constants.X_CANIROL_SPRITE_1));
            xLeftCanirolRegions.add(atlas.findRegion(Constants.X_CANIROL_SPRITE_2));
            xLeftCanirolRegions.add(atlas.findRegion(Constants.X_CANIROL_SPRITE_3));

            xLeftCanirolRegions.add(atlas.findRegion(Constants.X_CANIROL_SPRITE_2));
            xLeftCanirol = new Animation(Constants.CANIROL_FRAME_DURATION, xLeftCanirolRegions, PlayMode.NORMAL);

            xRightCanirol = new Animation(Constants.CANIROL_FRAME_DURATION, xLeftCanirolRegions, PlayMode.REVERSED);

            Array<AtlasRegion> yCanirolRegions = new Array<AtlasRegion>();
            yCanirolRegions.add(atlas.findRegion(Constants.Y_CANIROL_SPRITE_1));
            yCanirolRegions.add(atlas.findRegion(Constants.Y_CANIROL_SPRITE_2));
            yCanirolRegions.add(atlas.findRegion(Constants.Y_CANIROL_SPRITE_3));
            yCanirol = new Animation(Constants.CANIROL_FRAME_DURATION, yCanirolRegions, PlayMode.NORMAL);
        }
    }

    public static final class ZoombaAssets {

        public final AtlasRegion zoomba;
        public final Animation<TextureRegion> gasZoombaLeft;
        public final Animation<TextureRegion> gasZoombaRight;
        public final Animation<TextureRegion> gasZoombaDown;
        public final Animation<TextureRegion> gasZoombaUp;
        public final Array<Animation> gasAnimations;
        public final Animation<TextureRegion> liquidZoomba;
        public final Animation<TextureRegion> plasmaZoomba;
        public final Animation<TextureRegion> oreZoomba;
        public final Animation<TextureRegion> solidZoomba;

        private ZoombaAssets(TextureAtlas atlas) {
            zoomba = atlas.findRegion(Constants.ZOOMBA_SPRITE);

            Array<AtlasRegion> oreZoombaRegions = new Array<AtlasRegion>();
            oreZoombaRegions.add(atlas.findRegion(Constants.OREZOOMBA_SPRITE_1));
            oreZoombaRegions.add(atlas.findRegion(Constants.OREZOOMBA_SPRITE_2));
            oreZoomba = new Animation(Constants.SUSPENSION_ORE_DURATION / oreZoombaRegions.size, oreZoombaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> plasmaZoombaRegions = new Array<AtlasRegion>();
            plasmaZoombaRegions.add(atlas.findRegion(Constants.CHARGEDZOOMBA_SPRITE_1));
            plasmaZoombaRegions.add(atlas.findRegion(Constants.CHARGEDZOOMBA_SPRITE_2));
            plasmaZoomba = new Animation(Constants.SUSPENSION_PLASMA_DURATION / plasmaZoombaRegions.size, plasmaZoombaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gasZoombaLeftRegions = new Array<AtlasRegion>();
            gasZoombaLeftRegions.add(atlas.findRegion(Constants.FIERYZOOMBA_SPRITE_1_LEFT));
            gasZoombaLeftRegions.add(atlas.findRegion(Constants.FIERYZOOMBA_SPRITE_2_LEFT));
            gasZoombaLeft = new Animation(Constants.PROTRUSION_GAS_DURATION / gasZoombaLeftRegions.size, gasZoombaLeftRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gasZoombaRightRegions = new Array<AtlasRegion>();
            gasZoombaRightRegions.add(atlas.findRegion(Constants.FIERYZOOMBA_SPRITE_1_RIGHT));
            gasZoombaRightRegions.add(atlas.findRegion(Constants.FIERYZOOMBA_SPRITE_2_RIGHT));
            gasZoombaRight = new Animation(Constants.PROTRUSION_GAS_DURATION / gasZoombaRightRegions.size, gasZoombaRightRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gasZoombaDownRegions = new Array<AtlasRegion>();
            gasZoombaDownRegions.add(atlas.findRegion(Constants.FIERYZOOMBA_SPRITE_1_DOWN));
            gasZoombaDownRegions.add(atlas.findRegion(Constants.FIERYZOOMBA_SPRITE_2_DOWN));
            gasZoombaDown = new Animation(Constants.PROTRUSION_GAS_DURATION / gasZoombaDownRegions.size, gasZoombaDownRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gasZoombaUpRegions = new Array<AtlasRegion>();
            gasZoombaUpRegions.add(atlas.findRegion(Constants.FIERYZOOMBA_SPRITE_1_UP));
            gasZoombaUpRegions.add(atlas.findRegion(Constants.FIERYZOOMBA_SPRITE_2_UP));
            gasZoombaUp = new Animation(Constants.PROTRUSION_GAS_DURATION / gasZoombaUpRegions.size, gasZoombaUpRegions, PlayMode.NORMAL);

            gasAnimations = new Array<Animation>();
            gasAnimations.add(gasZoombaLeft);
            gasAnimations.add(gasZoombaRight);
            gasAnimations.add(gasZoombaDown);
            gasAnimations.add(gasZoombaUp);

            Array<AtlasRegion> liquidZoombaRegions = new Array<AtlasRegion>();
            liquidZoombaRegions.add(atlas.findRegion(Constants.GUSHINGZOOMBA_SPRITE_1));
            liquidZoombaRegions.add(atlas.findRegion(Constants.GUSHINGZOOMBA_SPRITE_2));
            liquidZoomba = new Animation(Constants.PROTRUSION_LIQUID_DURATION / liquidZoombaRegions.size, liquidZoombaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> solidZoombaRegions = new Array<AtlasRegion>();
            solidZoombaRegions.add(atlas.findRegion(Constants.SOLIDZOOMBA_SPRITE_1));
            solidZoombaRegions.add(atlas.findRegion(Constants.SOLIDZOOMBA_SPRITE_2));
            solidZoomba = new Animation(Constants.PROTRUSION_SOLID_DURATION / solidZoombaRegions.size, solidZoombaRegions, PlayMode.NORMAL);
        }
    }

    public static final class SwoopaAssets {

        public final AtlasRegion swoopaLeft;
        public final AtlasRegion swoopaRight;
        public final Animation<TextureRegion> gasSwoopaLeft;
        public final Animation<TextureRegion> gasSwoopaRight;
        public final Animation<TextureRegion> liquidSwoopa;
        public final Animation<TextureRegion> plasmaSwoopa;
        public final Animation<TextureRegion> oreSwoopa;
        public final Animation<TextureRegion> solidSwoopa;

        private SwoopaAssets(TextureAtlas atlas) {
            swoopaLeft = atlas.findRegion(Constants.SWOOPA_SPRITE_LEFT);
            swoopaRight = atlas.findRegion(Constants.SWOOPA_SPRITE_RIGHT);

            Array<AtlasRegion> oreSwoopaRegions = new Array<AtlasRegion>();
            oreSwoopaRegions.add(atlas.findRegion(Constants.ORESWOOPA_SPRITE_1));
            oreSwoopaRegions.add(atlas.findRegion(Constants.ORESWOOPA_SPRITE_2));
            oreSwoopa = new Animation(Constants.SUSPENSION_ORE_DURATION / oreSwoopaRegions.size, oreSwoopaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> plasmaSwoopaRegions = new Array<AtlasRegion>();
            plasmaSwoopaRegions.add(atlas.findRegion(Constants.CHARGEDSWOOPA_SPRITE_1));
            plasmaSwoopaRegions.add(atlas.findRegion(Constants.CHARGEDSWOOPA_SPRITE_2));
            plasmaSwoopa = new Animation(Constants.SUSPENSION_PLASMA_DURATION / plasmaSwoopaRegions.size, plasmaSwoopaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gasSwoopaLeftRegions = new Array<AtlasRegion>();
            gasSwoopaLeftRegions.add(atlas.findRegion(Constants.FIERYSWOOPA_SPRITE_1_LEFT));
            gasSwoopaLeftRegions.add(atlas.findRegion(Constants.FIERYSWOOPA_SPRITE_2_LEFT));
            gasSwoopaLeft = new Animation(Constants.PROTRUSION_GAS_DURATION / gasSwoopaLeftRegions.size, gasSwoopaLeftRegions, PlayMode.NORMAL);

            Array<AtlasRegion> gasSwoopaRightRegions = new Array<AtlasRegion>();
            gasSwoopaRightRegions.add(atlas.findRegion(Constants.FIERYSWOOPA_SPRITE_1_RIGHT));
            gasSwoopaRightRegions.add(atlas.findRegion(Constants.FIERYSWOOPA_SPRITE_2_RIGHT));
            gasSwoopaRight = new Animation(Constants.PROTRUSION_GAS_DURATION / gasSwoopaRightRegions.size, gasSwoopaRightRegions, PlayMode.NORMAL);

            Array<AtlasRegion> liquidSwoopaRegions = new Array<AtlasRegion>();
            liquidSwoopaRegions.add(atlas.findRegion(Constants.GUSHINGSWOOPA_SPRITE_1));
            liquidSwoopaRegions.add(atlas.findRegion(Constants.GUSHINGSWOOPA_SPRITE_2));
            liquidSwoopa = new Animation(Constants.PROTRUSION_LIQUID_DURATION / liquidSwoopaRegions.size, liquidSwoopaRegions, PlayMode.NORMAL);

            Array<AtlasRegion> solidSwoopaRegions = new Array<AtlasRegion>();
            solidSwoopaRegions.add(atlas.findRegion(Constants.SOLIDSWOOPA_SPRITE_1));
            solidSwoopaRegions.add(atlas.findRegion(Constants.SOLIDSWOOPA_SPRITE_2));
            solidSwoopa = new Animation(Constants.PROTRUSION_SOLID_DURATION / solidSwoopaRegions.size, solidSwoopaRegions, PlayMode.NORMAL);
        }
    }

    public static final class OrbenAssets {

        public final AtlasRegion dormantOrben;
        public final Animation<TextureRegion> plasmaOrben;
        public final Animation<TextureRegion> gasOrben;
        public final Animation<TextureRegion> liquidOrben;
        public final Animation<TextureRegion> solidOrben;
        public final Animation<TextureRegion> oreOrben;

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

        public final Animation<TextureRegion> plasmaRollen;
        public final Animation<TextureRegion> gasRollen;
        public final Animation<TextureRegion> liquidRollen;
        public final Animation<TextureRegion> solidRollen;
        public final Animation<TextureRegion> oreRollen;

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

    public static final class ArmorolloAssets {

        public final Animation<TextureRegion> armoredLiquid;
        public final Animation<TextureRegion> vulnerableLiquid;

        private ArmorolloAssets(TextureAtlas atlas) {
            Array<AtlasRegion> armoredLiquidRegions = new Array<AtlasRegion>();
            armoredLiquidRegions.add(atlas.findRegion(Constants.ARMOROLL_LIQUID_SPRITE_0));
            armoredLiquid = new Animation(Constants.ROLLEN_DURATION / armoredLiquidRegions.size, armoredLiquidRegions, PlayMode.NORMAL);

            Array<AtlasRegion> vulnerableLiquidRegions = new Array<AtlasRegion>();
            vulnerableLiquidRegions.add(atlas.findRegion(Constants.ARMOROLL_LIQUID_SPRITE_1));
            vulnerableLiquidRegions.add(atlas.findRegion(Constants.ARMOROLLO_LIQUID_SPRITE_2));
            vulnerableLiquidRegions.add(atlas.findRegion(Constants.ARMOROLLO_LIQUID_SPRITE_3));
            vulnerableLiquidRegions.add(atlas.findRegion(Constants.ARMOROLLO_LIQUID_SPRITE_4));
            vulnerableLiquid = new Animation(1, vulnerableLiquidRegions, PlayMode.NORMAL);
        }
    }

    public static final class ProtrusionAssets {

        public final Animation<TextureRegion> solidProtrustion;
        public final Animation<TextureRegion> liquidProtrusion;
        public final Animation<TextureRegion> gasProtrusion;
        public final Animation<TextureRegion> plasmaProtrusion;
        public final Animation<TextureRegion> oreProtrusion;
        public final AtlasRegion inactiveProtrusion;

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

            inactiveProtrusion = new AtlasRegion(atlas.findRegion(Constants.PROTRUSION_INACTIVE_SPRITE));
        }
    }

    public static final class SuspensionAssets {

        public final Animation<TextureRegion> gasSuspension;
        public final Animation<TextureRegion> solidSuspension;
        public final Animation<TextureRegion> oreSuspension;
        public final Animation<TextureRegion> liquidSuspension;
        public final Animation<TextureRegion> plasmaSuspension;
        public final Animation<TextureRegion> antimatterSuspension;
        public final AtlasRegion inactiveSuspension;

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

            inactiveSuspension = new AtlasRegion(atlas.findRegion(Constants.SUSPENSION_INACTIVE_SPRITE));
        }
    }

    public static final class PortalAssets {

        public final Animation<TextureRegion> portal;
        public final Animation<TextureRegion> teleport;

        private PortalAssets(TextureAtlas atlas) {
            Array<AtlasRegion> portalRegions = new Array<AtlasRegion>();
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_1));
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_2));
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_3));
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_4));
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_5));
            portalRegions.add(atlas.findRegion(Constants.PORTAL_SPRITE_6));

            portal = new Animation(Constants.PORTAL_FRAME_DURATION,
                    portalRegions, PlayMode.LOOP);

            Array<AtlasRegion> teleportRegions = new Array<AtlasRegion>();
            teleportRegions.add(atlas.findRegion(Constants.TELEPORT_SPRITE_1));
            teleportRegions.add(atlas.findRegion(Constants.TELEPORT_SPRITE_2));
            teleportRegions.add(atlas.findRegion(Constants.TELEPORT_SPRITE_3));

            teleport = new Animation(Constants.TELEPORT_FRAME_DURATION,
                    teleportRegions, PlayMode.NORMAL);
        }
    }

    public static final class ImpactAssets {

        public final Animation<TextureRegion> impactPlasma;
        public final Animation<TextureRegion> impactGas;
        public final Animation<TextureRegion> impactLiquid;
        public final Animation<TextureRegion> impactSolid;
        public final Animation<TextureRegion> impactPsychic;
        public final Animation<TextureRegion> impactHybrid;
        public final Animation<TextureRegion> impactNative;

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
        public final Animation<TextureRegion> ammoPowerup;
        public final Animation<TextureRegion> healthPowerup;
        public final Animation<TextureRegion> turboPowerup;
        public final Animation<TextureRegion> lifePowerup;
        public final Animation<TextureRegion> superPowerup;
        public final Animation<TextureRegion> cannonPowerup;
        public final Animation<TextureRegion> ruby;
        public final Animation<TextureRegion> sapphire;
        public final Animation<TextureRegion> emerald;

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


            Array<AtlasRegion> superPowerupRegions = new Array<AtlasRegion>();
            superPowerupRegions.add(atlas.findRegion(Constants.SUPER_POWERUP_SPRITE_1));
            superPowerupRegions.add(atlas.findRegion(Constants.SUPER_POWERUP_SPRITE_2));
            superPowerupRegions.add(atlas.findRegion(Constants.SUPER_POWERUP_SPRITE));
            superPowerup = new Animation(Constants.POWERUP_DURATION / superPowerupRegions.size, superPowerupRegions, PlayMode.NORMAL);
            
            Array<AtlasRegion> cannonPowerupRegions = new Array<AtlasRegion>();
            cannonPowerupRegions.add(atlas.findRegion(Constants.CANNON_POWERUP_SPRITE_1));
            cannonPowerupRegions.add(atlas.findRegion(Constants.CANNON_POWERUP_SPRITE_2));
            cannonPowerupRegions.add(atlas.findRegion(Constants.CANNON_POWERUP_SPRITE_3));
            cannonPowerupRegions.add(atlas.findRegion(Constants.CANNON_POWERUP_SPRITE));
            cannonPowerup = new Animation(Constants.POWERUP_DURATION / cannonPowerupRegions.size, cannonPowerupRegions, PlayMode.NORMAL);
            
            Array<AtlasRegion> rubyRegions = new Array<AtlasRegion>();
            rubyRegions.add(atlas.findRegion(Constants.RUBY_SPRITE_1));
            rubyRegions.add(atlas.findRegion(Constants.RUBY_SPRITE_2));
            ruby = new Animation(Constants.GEM_DURATION / rubyRegions.size, rubyRegions, PlayMode.NORMAL);
            
            Array<AtlasRegion> sapphireRegions = new Array<AtlasRegion>();
            sapphireRegions.add(atlas.findRegion(Constants.SAPPHIRE_SPRITE_1));
            sapphireRegions.add(atlas.findRegion(Constants.SAPPHIRE_SPRITE_2));
            sapphire = new Animation(Constants.GEM_DURATION / sapphireRegions.size, sapphireRegions, PlayMode.NORMAL);

            Array<AtlasRegion> emeraldRegions = new Array<AtlasRegion>();
            emeraldRegions.add(atlas.findRegion(Constants.EMERALD_SPRITE_1));
            emeraldRegions.add(atlas.findRegion(Constants.EMERALD_SPRITE_2));
            emerald = new Animation(Constants.GEM_DURATION / emeraldRegions.size, emeraldRegions, PlayMode.NORMAL);
        }

        public Animation<TextureRegion> getGemTexture(Enums.GemType type) {
            switch (type) {
                case RUBY:
                    return ruby;
                case SAPPHIRE:
                    return sapphire;
                case EMERALD:
                    return emerald;
            }
            return ruby;
        }
    }

    public static final class HudAssets {

        public final AtlasRegion shoot;
        public final AtlasRegion blast;
        public final AtlasRegion jump;
        public final AtlasRegion hover;
        public final AtlasRegion rappel;
        public final AtlasRegion climb;
        public final AtlasRegion dash;
        public final AtlasRegion move;
        public final AtlasRegion life;

        private HudAssets(TextureAtlas atlas) {
            shoot = atlas.findRegion(Constants.SHOOT_ICON);
            blast = atlas.findRegion(Constants.BLAST_ICON);
            jump = atlas.findRegion(Constants.JUMP_ICON);
            hover = atlas.findRegion(Constants.HOVER_ICON);
            rappel = atlas.findRegion(Constants.RAPPEL_ICON);
            climb = atlas.findRegion(Constants.CLIMB_ICON);
            dash = atlas.findRegion(Constants.DASH_ICON);
            move = atlas.findRegion(Constants.MOVE_ICON);
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
        public final AtlasRegion beast;
        public final AtlasRegion globe;

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
            logo = atlas.findRegion(Constants.LOGO_SPRITE);
            beast = atlas.findRegion(Constants.BEAST_SPRITE);
            globe = atlas.findRegion(Constants.GLOBE_SPRITE);
        }
    }

    public final class SoundAssets {

        public final Sound health;
        public final Sound ammo;
        public final Sound turbo;
        public final Sound cannon;
        public final Sound life;
        public final Sound upgrade;
        public final Sound nativeBlast;
        public final Sound oreBlast;
        public final Sound plasmaBlast;
        public final Sound gasBlast;
        public final Sound liquidBlast;
        public final Sound solidBlast;
        public final Sound antimatterBlast;
        public final Sound hybridBlast;
        public final Sound nativeShot;
        public final Sound oreShot;
        public final Sound plasmaShot;
        public final Sound gasShot;
        public final Sound liquidShot;
        public final Sound solidShot;
        public final Sound antimatterShot;
        public final Sound hybridShot;
        public final Sound warp;
        public final Sound hit;
        public final Sound hitGround;
        public final Sound breakGround;
        public final Sound damage;
        public final Sound flight;

        private SoundAssets() {

            health = assetManager.get(Constants.HEALTH_SOUND); // use of descriptor enforces type checking
            ammo = assetManager.get(Constants.AMMO_SOUND); // use of descriptor enforces type checking
            turbo = assetManager.get(Constants.TURBO_SOUND); // use of descriptor enforces type checking
            cannon = assetManager.get(Constants.CANNON_SOUND); // use of descriptor enforces type checking
            life = assetManager.get(Constants.LIFE_SOUND); // use of descriptor enforces type checking
            upgrade = assetManager.get(Constants.UPGRADE_SOUND); // use of descriptor enforces type checking
            nativeBlast = assetManager.get(Constants.BLAST_NATIVE_SOUND); // use of descriptor enforces type checking
            plasmaBlast = assetManager.get(Constants.BLAST_PLASMA_SOUND); // use of descriptor enforces type checking
            liquidBlast = assetManager.get(Constants.BLAST_LIQUID_SOUND); // use of descriptor enforces type checking
            solidBlast = assetManager.get(Constants.BLAST_SOLID_SOUND); // use of descriptor enforces type checking
            gasBlast = assetManager.get(Constants.BLAST_GAS_SOUND); // use of descriptor enforces type checking
            antimatterBlast = assetManager.get(Constants.BLAST_ANTIMATTER_SOUND); // use of descriptor enforces type checking
            hybridBlast = assetManager.get(Constants.BLAST_HYBRID_SOUND); // use of descriptor enforces type checking
            oreBlast = assetManager.get(Constants.BLAST_ORE_SOUND); // use of descriptor enforces type checking
            nativeShot = assetManager.get(Constants.SHOT_NATIVE_SOUND); // use of descriptor enforces type checking
            plasmaShot = assetManager.get(Constants.SHOT_PLASMA_SOUND); // use of descriptor enforces type checking
            liquidShot = assetManager.get(Constants.SHOT_LIQUID_SOUND); // use of descriptor enforces type checking
            solidShot = assetManager.get(Constants.SHOT_SOLID_SOUND); // use of descriptor enforces type checking
            gasShot = assetManager.get(Constants.SHOT_GAS_SOUND); // use of descriptor enforces type checking
            antimatterShot = assetManager.get(Constants.SHOT_ANTIMATTER_SOUND); // use of descriptor enforces type checking
            oreShot = assetManager.get(Constants.SHOT_ORE_SOUND); // use of descriptor enforces type checking
            hybridShot = assetManager.get(Constants.SHOT_HYBRID_SOUND); // use of descriptor enforces type checking
            warp = assetManager.get(Constants.WARP_SOUND); // use of descriptor enforces type checking
            hit = assetManager.get(Constants.HIT_SOUND); // use of descriptor enforces type checking
            hitGround = assetManager.get(Constants.HIT_GROUND_SOUND);
            breakGround = assetManager.get(Constants.BREAK_GROUND_SOUND); // use of descriptor enforces type checking
            damage = assetManager.get(Constants.DAMAGE_SOUND); // use of descriptor enforces type checking
            flight = assetManager.get(Constants.FLIGHT_SOUND);
        }

        public Sound getBlastSound(Enums.Energy energy) {
            switch (energy) {
                case NATIVE:
                    return nativeBlast;
                case ORE:
                    return oreBlast;
                case PLASMA:
                    return plasmaBlast;
                case GAS:
                    return gasBlast;
                case LIQUID:
                    return liquidBlast;
                case SOLID:
                    return solidBlast;
                case ANTIMATTER:
                    return antimatterBlast;
                case HYBRID:
                    return hybridBlast;
                default:
                    return nativeBlast;
            }
        }

        public Sound getShotSound(Enums.Energy energy) {
            switch (energy) {
                case NATIVE:
                    return nativeShot;
                case ORE:
                    return oreShot;
                case PLASMA:
                    return plasmaShot;
                case GAS:
                    return gasShot;
                case LIQUID:
                    return liquidShot;
                case SOLID:
                    return solidShot;
                case ANTIMATTER:
                    return antimatterShot;
                case HYBRID:
                    return hybridShot;
                default:
                    return nativeShot;
            }
        }
    }

    public final class MusicAssets {

        public final Music intro;
        public final Music backstory;
        public final Music overworld;
        public final Music dialog;
        public final Music boss;
        public final Music restart;
        public final Music decision;
        public final Music faceoff;
        public final Music upstart;
        public final Music reeling;
        public final Music village;
        public final Music progress;
        public final Music ending;
        public final Music complete;
        public final Music sky;
        public final Music stroll;
        public final Music puzzle;
        public final Music tunnel;
        public final Music trial;
        public final Music buzzard;
        public final Music chirpy;

        private MusicAssets() {
            intro = assetManager.get(Constants.OUTSET_MUSIC); // use of descriptor enforces type checking
            backstory = assetManager.get(Constants.OFFENCE_MUSIC); // use of descriptor enforces type checking
            overworld = assetManager.get(Constants.SELECTION_MUSIC); // use of descriptor enforces type checking
            dialog = assetManager.get(Constants.ENCOUNTER_MUSIC); // use of descriptor enforces type checking
            boss = assetManager.get(Constants.ESCALATION_MUSIC); // use of descriptor enforces type checking
            decision = assetManager.get(Constants.CONTEMPLATION_MUSIC); // use of descriptor enforces type checking
            faceoff = assetManager.get(Constants.RECKONING_MUSIC); // use of descriptor enforces type checking
            village = assetManager.get(Constants.VILLAGE_MUSIC); // use of descriptor enforces type checking
            progress = assetManager.get(Constants.CONTINUATION_MUSIC); // use of descriptor enforces type checking
            complete = assetManager.get(Constants.COMBUSTION_MUSIC); // use of descriptor enforces type checking
            ending = assetManager.get(Constants.SENDOFF_MUSIC); // use of descriptor enforces type checking
            restart = assetManager.get(Constants.RESTART_MUSIC); //  use of descriptor enforces type checking
            upstart = assetManager.get(Constants.UPSTART_MUSIC); // use of descriptor enforces type checking
            reeling = assetManager.get(Constants.REELING_MUSIC); //  use of descriptor enforces type checking
            sky = assetManager.get(Constants.SKY_MUSIC); // use of descriptor enforces type checking
            stroll = assetManager.get(Constants.STROLL_MUSIC); //  use of descriptor enforces type checking
            puzzle = assetManager.get(Constants.PUZZLE_MUSIC); // use of descriptor enforces type checking
            tunnel = assetManager.get(Constants.TUNNEL_MUSIC); // use of descriptor enforces type checking
            trial = assetManager.get(Constants.TRIAL_MUSIC); // use of descriptor enforces type checking
            buzzard = assetManager.get(Constants.BUZZARD_MUSIC); //  use of descriptor enforces type checking
            chirpy = assetManager.get(Constants.CHIRPY_MUSIC); //  use of descriptor enforces type checking
        }

        public Music getThemeMusic(Enums.Theme theme, Enums.MusicStyle style) {
            switch (theme) {
                case HOME:
                    switch (style) {
                        case CLASSIC: return village;
                        case AMBIENT: return sky;
                        case CHIPTUNE: return chirpy;
                    }
                case MECHANICAL:
                    switch (style) {
                        case CLASSIC: return upstart;
                        case AMBIENT: return stroll;
                        case CHIPTUNE: return buzzard;
                    }
                case ELECTROMAGNETIC:
                    switch (style) {
                        case CLASSIC: return progress;
                        case AMBIENT: return puzzle;
                        case CHIPTUNE: return chirpy;
                    }
                case NUCLEAR:
                    switch (style) {
                        case CLASSIC: return reeling;
                        case AMBIENT: return tunnel;
                        case CHIPTUNE: return buzzard;
                    }
                case THERMAL:
                    switch (style) {
                        case CLASSIC: return backstory;
                        case AMBIENT: return puzzle;
                        case CHIPTUNE: return chirpy;
                    }
                case GRAVITATIONAL:
                    switch (style) {
                        case CLASSIC: return village;
                        case AMBIENT: return puzzle;
                        case CHIPTUNE: return buzzard;
                    }
                case MYSTERIOUS:
                    switch (style) {
                        case CLASSIC: return upstart;
                        case AMBIENT: return trial;
                        case CHIPTUNE: return chirpy;
                    }
                case FINAL:
                    switch (style) {
                        case CLASSIC: return reeling;
                        case AMBIENT: return trial;
                        case CHIPTUNE: return buzzard;
                    }
                default:
                    switch (style) {
                        case AMBIENT: return sky;
                        case CHIPTUNE: return chirpy;
                        default: return upstart;
                    }
            }
        }
    }

    public final class FontAssets {

        public BitmapFont menu;
        public BitmapFont message;
        public BitmapFont title;
        public BitmapFont inactive;

        private FontAssets() {

            message = assetManager.get(Constants.MESSAGE_FONT);
            message.getData().setScale(.4f);

            menu = assetManager.get(Constants.MENU_FONT);
            menu.getData().setScale(0.5f);

            inactive = assetManager.get(Constants.INACTIVE_FONT);
            inactive.getData().scale(0.5f);
            inactive.setColor(Color.GRAY);

            title = assetManager.get(Constants.TITLE_FONT);
            title.getData().setScale(1);
            title.setColor(Color.WHITE);
        }
    }

    // Getters
    public final AvatarAssets getAvatarAssets() { return avatarAssets; }
    public final BossAssets getBossAssets() { return bossAssets; }
    public final BackgroundAssets getBackgroundAssets() { return backgroundAssets; }
    public final GroundAssets getGroundAssets() { return groundAssets; }
    public final AmmoAssets getAmmoAssets() { return ammoAssets; }
    public final BladeAssets getBladeAssets() { return bladeAssets; }
    public final CanirolAssets getCanirolAssets() { return canirolAssets; }
    public final ZoombaAssets getZoombaAssets() { return zoombaAssets; }
    public final SwoopaAssets getSwoopaAssets() { return swoopaAssets; }
    public final OrbenAssets getOrbenAssets() { return orbenAssets; }
    public final RollenAssets getRollenAssets() { return rollenAssets; }
    public final ArmorolloAssets getArmorolloAssets() { return armorolloAssets; }
    public final ProtrusionAssets getProtrusionAssets() { return protrusionAssets; }
    public final SuspensionAssets getSuspensionAssets() { return suspensionAssets; }
    public final ImpactAssets getImpactAssets() { return impactAssets; }
    public final PowerupAssets getPowerupAssets() { return powerupAssets; }
    public final PortalAssets getPortalAssets() { return portalAssets; }
    public final HudAssets getHudAssets(){ return hudAssets; }
    public final OverlayAssets getOverlayAssets() { return overlayAssets; }
    public final SoundAssets getSoundAssets() { return soundAssets; }
    public final MusicAssets getMusicAssets() { return musicAssets; }
    public final FontAssets getFontAssets() { return fontAssets; }
}
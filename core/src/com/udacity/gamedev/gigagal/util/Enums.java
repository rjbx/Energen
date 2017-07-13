package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.graphics.Color;

// immutable non-instantiable static
public final class Enums {

    // cannot be subclassed
    private Enums() {}

    public enum Orientation {
        X,
        Y,
        Z
    }

    public enum Direction {
        LEFT,
        RIGHT,
        DOWN,
        UP
    }

    public enum Action {
        STANDING,
        STRIDING,
        CLIMBING,
        DASHING,
        FALLING,
        JUMPING,
        TWISTING,
        HOVERING,
        RAPPELLING,
        RECOILING
    }

    public enum GroundState {
        PLANTED,
        AIRBORNE,
        SUBMERGED
    }

    public enum PowerupType {
        LIFE,
        HEALTH,
        TURBO,
        AMMO,
        CANNON
    }

    public enum ShotIntensity {
        NORMAL, CHARGED, BLAST,
    }

    public enum ReactionIntensity {
        STRONG,
        NORMAL,
        WEAK
    }

    public enum Material {

        NATIVE { @Override public final Theme theme() { return Theme.HOME; } },

        ORE { @Override public final Theme theme() { return Theme.MECHANICAL; } },
        PLASMA { @Override public final Theme theme() { return Theme.ELECTROMAGNETIC; } },
        GAS { @Override public final Theme theme() { return Theme.NUCLEAR; } },
        LIQUID { @Override public final Theme theme() { return Theme.THERMAL; } },
        SOLID { @Override public final Theme theme() { return Theme.GRAVITATIONAL; } },
        ANTIMATTER { @Override public final Theme theme() { return Theme.MYSTERIOUS; } },

        HYBRID { @Override public final Theme theme() { return Theme.FINAL; } };

        abstract public Theme theme();
    }

    public enum Theme {

        HOME { @Override public final Color color() { return Color.TAN; } },

        MECHANICAL { @Override public final Color color() { return Color.LIGHT_GRAY; } },
        ELECTROMAGNETIC { @Override public final Color color() { return Color.DARK_GRAY; } },
        NUCLEAR { @Override public final Color color() { return Color.NAVY; } },
        THERMAL { @Override public final Color color() { return Color.DARK_GRAY; } },
        GRAVITATIONAL { @Override public final Color color() { return Color.FIREBRICK; } },
        MYSTERIOUS { @Override public final Color color() { return Color.BLACK; } },

        FINAL { @Override public final Color color() { return Color.MAROON; } };

        abstract public Color color();
    }

    public enum Upgrade {
        NONE,
        AMMO,
        HEALTH,
        TURBO,
        CANNON,
        JUMP, // charge jump for increased y velocity (uses all turbo)
        STRIDE, // hold directional longer for additional x velocity boost
        HOVER // press jump and up simultaneously to hurdle out of hover (uses all turbo)
    }

    public enum LevelMenu {
        NONE,
        MAIN,
        OPTIONS,
        RESET,
        DEBUG,
        END
    }

    public enum OverworldMenu {
        MAIN,
        OPTIONS
    }

    public enum LaunchMenu {
        START,
        ERASE,
        DIFFICULTY,
    }

    public enum TimerState {
        UNSTARTED,
        RUNNING,
        STOPPED,
        SUSPENDED
    }

    public enum ChaseCamState {
        FOLLOWING,
        DEBUG,
        BOSS,
        CONVERT
    }

    public enum EffectsType {
        ELECTROLYSIS,
        INCINERATE,
        SHORT,
        MELT,
        CRUSH
    }
}
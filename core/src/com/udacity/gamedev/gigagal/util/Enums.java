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

    public enum BladeState {
        RETRACTED, RUSH, FLIP, CUT
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

        HOME { @Override public final Color color() { return new Color(0, 0.4f, .65f, 1); } },

        MECHANICAL { @Override public final Color color() { return new Color(.2f, .5f, 0, 1); } },
        ELECTROMAGNETIC { @Override public final Color color() { return new Color(.8f, .6f, 0, 1); } },
        NUCLEAR { @Override public final Color color() { return new Color(.65f, .2f, 0, 1); } },
        THERMAL { @Override public final Color color() { return new Color(.3f, .3f, .3f, 1); } },
        GRAVITATIONAL { @Override public final Color color() { return new Color(.4f, .5f, .6f, 1); } },
        MYSTERIOUS { @Override public final Color color() { return new Color(.5f, 0, .7f, 1); } },

        FINAL { @Override public final Color color() { return new Color(1, 1, 1, 1); } };

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

    public enum MenuType {
        NONE,
        START,
        ERASE,
        DIFFICULTY,
        MAIN,
        OPTIONS,
        RESET,
        DEBUG,
        END
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
        INCINERATION,
        SHORT_CIRCUITING,
        MELTING,
        CRUSHING
    }
}
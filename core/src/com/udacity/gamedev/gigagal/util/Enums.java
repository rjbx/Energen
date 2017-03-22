package com.udacity.gamedev.gigagal.util;

// immutable static
public final class Enums {

    // non-instantiable; cannot be subclassed
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
        CLINGING,
        RECOILING
    }

    public enum GroundState {
        PLANTED,
        AIRBORNE,
        SUBMERGED
    }

    public enum AmmoIntensity {
        SHOT, CHARGE_SHOT, BLAST,
    }

    public enum WeaponType {

        SOLID { @Override public final String levelName() { return "GRAVITATIONAL"; } },
        POLYMER { @Override public final String levelName() { return "MECHANICAL"; } },
        PLASMA { @Override public final String levelName() { return "MAGNETIC"; } },
        LIQUID { @Override public final String levelName() { return "PLASMA"; } },
        GAS { @Override public final String levelName() { return "NUCLEAR"; } },
        PSYCHIC { @Override public final String levelName() { return "PSYCHIC"; } },
        NATIVE { @Override public final String levelName() { return "NATIVE"; } };

        abstract public String levelName();
    }

    public enum TypeEffectiveness {
        STRONG,
        NORMAL,
        WEAK
    }
}

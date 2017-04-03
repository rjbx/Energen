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

    public enum TypeEffectiveness {
        STRONG,
        NORMAL,
        WEAK
    }

    public enum WeaponType {
        
        POLYMER { @Override public final String levelName() { return "MECHANICAL"; } },
        PLASMA { @Override public final String levelName() { return "MAGNETIC"; } },
        GAS { @Override public final String levelName() { return "NUCLEAR"; } },
        LIQUID { @Override public final String levelName() { return "ELECTRIC"; } },
        SOLID { @Override public final String levelName() { return "GRAVITATIONAL"; } },
        PSYCHIC { @Override public final String levelName() { return "PARANORMAL"; } },
        NATIVE { @Override public final String levelName() { return "FINAL"; } };

        abstract public String levelName();
    }

    public enum LevelName {

        MECHANICAL { @Override public final String weaponType() { return "POLYMER"; } },
        MAGNETIC { @Override public final String weaponType() { return "PLASMA"; } },
        NUCLEAR { @Override public final String weaponType() { return "GAS"; } },
        ELECTRIC { @Override public final String weaponType() { return "LIQUID"; } },
        GRAVITATIONAL { @Override public final String weaponType() { return "SOLID"; } },
        PARANORMAL { @Override public final String weaponType() { return "PSYCHIC"; } },
        FINAL { @Override public final String weaponType() { return "NATIVE"; } };

        abstract public String weaponType();
    }
}

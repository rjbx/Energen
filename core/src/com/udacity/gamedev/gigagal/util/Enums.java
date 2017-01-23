package com.udacity.gamedev.gigagal.util;

// immutable static
public final class Enums {

    // non-instantiable; cannot be subclassed
    private Enums() {}

    public enum AmmoIntensity {
        SHOT, CHARGE_SHOT, BLAST,
    }

    public enum WeaponType {
        ELECTRIC,
        FIRE,
        WATER,
        RUBBER,
        METAL,
        PSYCHIC,
        NATIVE
    }


    public enum TypeEffectiveness {
        STRONG,
        NORMAL,
        WEAK
    }

    public enum Direction {
        LEFT,
        RIGHT,
        DOWN,
        UP
    }

    public enum Orientation {
        X,
        Y
    }

    public enum GroundState {
        STANDING,
        STRIDING,
        CLIMBING,
        DASHING,
        AIRBORNE
    }

    public enum AerialState {
        FALLING,
        JUMPING,
        TWISTING,
        HOVERING,
        CLINGING,
        RECOILING,
        GROUNDED
    }
}

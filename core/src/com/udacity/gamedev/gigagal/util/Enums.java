package com.udacity.gamedev.gigagal.util;

public final class Enums {

    private Enums() {}

    public enum ShotIntensity {
        NORMAL, CHARGED,
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
        VERTICAL,
        LATERAL
    }

    public enum AerialState {
        JUMPING,
        HOVERING,
        RICOCHETING,
        FALLING,
        GROUNDED,
        RECOILING
    }

    public enum GroundState {
        STANDING,
        STRIDING,
        DASHING,
        AIRBORNE
    }
}

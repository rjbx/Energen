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

        NATIVE { @Override public final LevelName levelName() { return LevelName.HOME; } },
        POLYMER { @Override public final LevelName levelName() { return LevelName.MECHANICAL; } },
        PLASMA { @Override public final LevelName levelName() { return LevelName.ELECTROMAGNETIC; } },
        GAS { @Override public final LevelName levelName() { return LevelName.NUCLEAR; } },
        LIQUID { @Override public final LevelName levelName() { return LevelName.THERMAL; } },
        SOLID { @Override public final LevelName levelName() { return LevelName.GRAVITATIONAL; } },
        PSYCHIC { @Override public final LevelName levelName() { return LevelName.PARANORMAL; } },
        HYBRID { @Override public final LevelName levelName() { return LevelName.FINAL; } };

        abstract public LevelName levelName();
    }

    public enum LevelName {

        HOME { @Override public final WeaponType weaponType() { return WeaponType.NATIVE; } },
        MECHANICAL { @Override public final WeaponType weaponType() { return WeaponType.POLYMER; } },
        ELECTROMAGNETIC { @Override public final WeaponType weaponType() { return WeaponType.PLASMA; } },
        NUCLEAR { @Override public final WeaponType weaponType() { return WeaponType.GAS; } },
        THERMAL { @Override public final WeaponType weaponType() { return WeaponType.LIQUID; } },
        GRAVITATIONAL { @Override public final WeaponType weaponType() { return WeaponType.SOLID; } },
        PARANORMAL { @Override public final WeaponType weaponType() { return WeaponType.PSYCHIC; } },
        FINAL { @Override public final WeaponType weaponType() { return WeaponType.HYBRID; } };

        abstract public WeaponType weaponType();
    }
}
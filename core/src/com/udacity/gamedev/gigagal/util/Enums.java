package com.udacity.gamedev.gigagal.util;


public class Enums {

    public enum AmmoType {
        REGULAR, CHARGE
    }


    public enum Direction {
        LEFT, RIGHT
    }

    public enum AerialStatus {
        JUMPING,
        HOVERING,
        RICOCHETING,
        FALLING,
        GROUNDED,
        RECOILING,
        SLIDING
    }

    public enum TerrainStatus {
        STANDING,
        LEANING,
        WALKING,
        DASHING
    }
}

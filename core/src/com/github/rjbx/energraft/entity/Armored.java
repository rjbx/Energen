package com.github.rjbx.energraft.entity;

import com.github.rjbx.energraft.util.Enums;

public interface Armored {
    long getStartTime();
    void resetStartTime();
    void strikeArmor();
    boolean isVulnerable();
    Enums.Direction getVulnerability();
    float getRecoverySpeed();
}

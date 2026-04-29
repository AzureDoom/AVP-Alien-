package com.alien.common.gameplay.entity.living.alien.xenomorph.carrier;

public enum CarrierSpine {

    LEFT_UPPER(0, "LeftUpperDorsalEnd2"),
    RIGHT_UPPER(1, "RightUpperDorsalEnd2"),
    LEFT_MIDDLE(2, "gLeftMiddleDorsalEnd2"),
    RIGHT_MIDDLE(3, "gRightMiddleDorsalEnd2"),
    LEFT_LOWER(4, "gLeftLowerDorsalEnd5"),
    RIGHT_LOWER(5, "gRightLowerDorsalEnd5");

    public static final int COUNT = values().length;

    private final int passengerIndex;

    private final String boneName;

    CarrierSpine(int passengerIndex, String boneName) {
        this.passengerIndex = passengerIndex;
        this.boneName = boneName;
    }

    public int getPassengerIndex() {
        return passengerIndex;
    }

    public String getBoneName() {
        return boneName;
    }

    public static CarrierSpine fromIndex(int index) {
        var values = values();
        if (index < 0 || index >= values.length) {
            return null;
        }
        return values[index];
    }
}

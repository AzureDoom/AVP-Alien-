package com.alien.client.render.entity.head;

import net.minecraft.world.phys.Vec3;

public class AlienEntityHeadData {

    private static final double MULTIPLIER = 1 / 16.0;

    public static final EntityHeadData CAMEL = adjust(vec3(7, 8, 19), vec3(-3.5, 22, -24), vec3(0, 23, -9));

    public static final EntityHeadData COW = adjust(vec3(8, 8, 6), vec3(-4, 16, -14), vec3(0, 20, -8));

    public static final EntityHeadData DOLPHIN = adjust(vec3(2, 2, 4), vec3(-1, 0, -13), vec3(0, 0, -3));

    public static final EntityHeadData FOX = adjust(vec3(8, 6, 6), vec3(-4, 3.5, -8), vec3(1, 7.5, -3));

    public static final EntityHeadData GOAT = adjust(vec3(5, 7, 10), vec3(-3, 16, -14), vec3(-0.5, 10, 0));

    public static final EntityHeadData HOGLIN = adjust(vec3(14, 6, 19), vec3(-7, 21, -24), vec3(0, 22, -5));

    public static final EntityHeadData HORSE = adjust(vec3(6, 5, 7), vec3(-3, 28, -11), vec3(0, 22, -9));

    // Note: Llama pivot z is adjusted manually for better yaw alignments.
    public static final EntityHeadData LLAMA = adjust(vec3(4, 4, 9), vec3(-2, 27, -16), vec3(0, 17, -6 - 5));

    public static final EntityHeadData PANDA = adjust(vec3(13, 10, 9), vec3(-6.5, 7.5, -21), vec3(0, 12.5, -17));

    public static final EntityHeadData PIG = adjust(vec3(8, 8, 8), vec3(-4, 8, -14), vec3(0, 12, -6));

    public static final EntityHeadData PIGLIN = adjust(vec3(10, 8, 8), vec3(-5, 24, -4), vec3(0, 24, 0));

    public static final EntityHeadData PLAYER = adjust(vec3(8, 8, 8), vec3(-4, 24, -4), vec3(0, 24, 0));

    // Note: Polar Bear pivot z is adjusted manually for better yaw alignments.
    public static final EntityHeadData POLAR_BEAR = adjust(vec3(7, 7, 7), vec3(-3.5, 10, -19), vec3(0, 14, -16 - 3));

    // Note: Ravager pivot z is adjusted manually for better yaw alignments.
    public static final EntityHeadData RAVAGER = adjust(vec3(16, 20, 16), vec3(-8, 14, -24), vec3(0, 14, -10 - 2.5));

    public static final EntityHeadData SHEEP = adjust(vec3(6, 6, 8), vec3(-3, 16, -14), vec3(0, 18, -8));

    public static final EntityHeadData SNIFFER = adjust(vec3(13, 18, 11), vec3(-6.5, 5, -31), vec3(0, 12.5, -19.5));

    public static final EntityHeadData VILLAGER = adjust(vec3(8, 10, 8), vec3(-4, 24, -4), vec3(0, 24, 0));

    public static final EntityHeadData WOLF = adjust(vec3(6, 6, 4), vec3(-3, 7.5, -9), vec3(1, 10.5, -7));

    private static EntityHeadData adjust(Vec3 size, Vec3 position, Vec3 pivot) {
        var adjustedSize = size.multiply(MULTIPLIER, MULTIPLIER, MULTIPLIER);
        var adjustedPosition = position.multiply(MULTIPLIER, MULTIPLIER, MULTIPLIER);
        var adjustedPivot = pivot.multiply(MULTIPLIER, MULTIPLIER, MULTIPLIER);

        return new EntityHeadData(adjustedSize, adjustedPosition, adjustedPivot);
    }

    private static Vec3 vec3(double x, double y, double z) {
        return new Vec3(x, y, z);
    }
}

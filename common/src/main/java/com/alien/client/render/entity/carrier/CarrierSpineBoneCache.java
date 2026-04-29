package com.alien.client.render.entity.carrier;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CarrierSpineBoneCache {

    public record SpineData(Vector3d[] modelPositions, Vector3f[] rotations) {}

    private static final Map<Integer, SpineData> CACHE = new ConcurrentHashMap<>();

    public static void put(int entityId, Vector3d[] modelPositions, Vector3f[] rotations) {
        CACHE.put(entityId, new SpineData(modelPositions, rotations));
    }

    public static @Nullable SpineData get(int entityId) {
        return CACHE.get(entityId);
    }

    public static void remove(int entityId) {
        CACHE.remove(entityId);
    }
}

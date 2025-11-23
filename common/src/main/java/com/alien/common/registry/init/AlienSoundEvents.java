package com.alien.common.registry.init;

import com.alien.AlienResources;
import com.avp.common.registry.AVPDeferredHolder;
import com.avp.service.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class AlienSoundEvents {

    private static AVPDeferredHolder<SoundEvent> register(String id) {
        return Services.REGISTRY.register(
            BuiltInRegistries.SOUND_EVENT,
            AlienResources.location(id),
            () -> SoundEvent.createVariableRangeEvent(AlienResources.location(id))
        );
    }

    public static void initialize() {}
}

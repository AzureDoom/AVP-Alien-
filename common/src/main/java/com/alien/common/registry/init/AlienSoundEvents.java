package com.alien.common.registry.init;

import com.alien.AlienResources;
import com.avp.common.registry.AVPDeferredHolder;
import com.avp.service.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class AlienSoundEvents {

    public static final AVPDeferredHolder<SoundEvent> BLOCK_ACID_BURN = register("block.acid.burn");

    public static final AVPDeferredHolder<SoundEvent> BLOCK_RESIN_SPREAD = register("block.resin.spread");

    public static final AVPDeferredHolder<SoundEvent> EFFECT_BONE_CRUNCH = register("effect.bone_crunch");

    public static final AVPDeferredHolder<SoundEvent> EFFECT_HEARTBEAT_0 = register("effect.heartbeat.0");

    public static final AVPDeferredHolder<SoundEvent> EFFECT_HEARTBEAT_1 = register("effect.heartbeat.1");

    public static final AVPDeferredHolder<SoundEvent> EFFECT_HEARTBEAT_2 = register("effect.heartbeat.2");

    public static final AVPDeferredHolder<SoundEvent> EFFECT_HEARTBEAT_3 = register("effect.heartbeat.3");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_CHESTBURSTER_BURST = register("entity.chestburster.burst");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_OVOMORPH_HATCH = register("entity.ovomorph.hatch");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_OVOMORPH_LAID = register("entity.ovomorph.laid");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_OVOMORPH_ROOT = register("entity.ovomorph.root");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_OVOMORPH_SHEAR = register("entity.ovomorph.shear");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_QUEEN_ARM_ATTACK = register("entity.queen.arm_attack");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_QUEEN_BACK_HAND_ATTACK = register("entity.queen.back_hand_attack");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_QUEEN_DEATH = register("entity.queen.death");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_QUEEN_HURT = register("entity.queen.hurt");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_QUEEN_IDLE = register("entity.queen.idle");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_QUEEN_RAM_ATTACK = register("entity.queen.ram_attack");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_QUEEN_SCREAM = register("entity.queen.scream");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_QUEEN_STEP_THUMP = register("entity.queen.step_thump");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_QUEEN_STEP_THUMP_ROCK = register("entity.queen.step_thump_rock");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_QUEEN_TAIL_ATTACK = register("entity.queen.tail_attack");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_XENOMORPH_ATTACK = register("entity.xenomorph.attack");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_XENOMORPH_DEATH = register("entity.xenomorph.death");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_XENOMORPH_HISS = register("entity.xenomorph.hiss");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_XENOMORPH_HURT = register("entity.xenomorph.hurt");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_XENOMORPH_IDLE = register("entity.xenomorph.idle");

    public static final AVPDeferredHolder<SoundEvent> ENTITY_XENOMORPH_LUNGE = register("entity.xenomorph.lunge");

    public static final AVPDeferredHolder<SoundEvent> ITEM_ARMOR_EQUIP_CHITIN = register("item.armor.equip_chitin");

    public static final AVPDeferredHolder<SoundEvent> JUKEBOX_SOUNDS_ALIEN_MUSIC_1 = register("jukebox_sounds.alien_music_1");

    private static AVPDeferredHolder<SoundEvent> register(String id) {
        return Services.REGISTRY.register(
            BuiltInRegistries.SOUND_EVENT,
            AlienResources.location(id),
            () -> SoundEvent.createVariableRangeEvent(AlienResources.location(id))
        );
    }

    public static void initialize() {}
}

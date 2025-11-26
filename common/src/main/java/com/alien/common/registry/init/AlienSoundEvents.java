package com.alien.common.registry.init;

import com.alien.Alien;
import com.alien.AlienResources;
import com.blib.BLibHolder;
import com.blib.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class AlienSoundEvents {

    private static final BLibRegistry<SoundEvent> REGISTRY = Alien.MOD.createRegistry(BuiltInRegistries.SOUND_EVENT);

    public static final BLibHolder<SoundEvent> BLOCK_ACID_BURN = create("block.acid.burn");

    public static final BLibHolder<SoundEvent> BLOCK_RESIN_SPREAD = create("block.resin.spread");

    public static final BLibHolder<SoundEvent> EFFECT_BONE_CRUNCH = create("effect.bone_crunch");

    public static final BLibHolder<SoundEvent> EFFECT_HEARTBEAT_0 = create("effect.heartbeat.0");

    public static final BLibHolder<SoundEvent> EFFECT_HEARTBEAT_1 = create("effect.heartbeat.1");

    public static final BLibHolder<SoundEvent> EFFECT_HEARTBEAT_2 = create("effect.heartbeat.2");

    public static final BLibHolder<SoundEvent> EFFECT_HEARTBEAT_3 = create("effect.heartbeat.3");

    public static final BLibHolder<SoundEvent> ENTITY_CHESTBURSTER_BURST = create("entity.chestburster.burst");

    public static final BLibHolder<SoundEvent> ENTITY_OVOMORPH_HATCH = create("entity.ovomorph.hatch");

    public static final BLibHolder<SoundEvent> ENTITY_OVOMORPH_LAID = create("entity.ovomorph.laid");

    public static final BLibHolder<SoundEvent> ENTITY_OVOMORPH_ROOT = create("entity.ovomorph.root");

    public static final BLibHolder<SoundEvent> ENTITY_OVOMORPH_SHEAR = create("entity.ovomorph.shear");

    public static final BLibHolder<SoundEvent> ENTITY_QUEEN_ARM_ATTACK = create("entity.queen.arm_attack");

    public static final BLibHolder<SoundEvent> ENTITY_QUEEN_BACK_HAND_ATTACK = create("entity.queen.back_hand_attack");

    public static final BLibHolder<SoundEvent> ENTITY_QUEEN_DEATH = create("entity.queen.death");

    public static final BLibHolder<SoundEvent> ENTITY_QUEEN_HURT = create("entity.queen.hurt");

    public static final BLibHolder<SoundEvent> ENTITY_QUEEN_IDLE = create("entity.queen.idle");

    public static final BLibHolder<SoundEvent> ENTITY_QUEEN_RAM_ATTACK = create("entity.queen.ram_attack");

    public static final BLibHolder<SoundEvent> ENTITY_QUEEN_SCREAM = create("entity.queen.scream");

    public static final BLibHolder<SoundEvent> ENTITY_QUEEN_STEP_THUMP = create("entity.queen.step_thump");

    public static final BLibHolder<SoundEvent> ENTITY_QUEEN_STEP_THUMP_ROCK = create("entity.queen.step_thump_rock");

    public static final BLibHolder<SoundEvent> ENTITY_QUEEN_TAIL_ATTACK = create("entity.queen.tail_attack");

    public static final BLibHolder<SoundEvent> ENTITY_XENOMORPH_ATTACK = create("entity.xenomorph.attack");

    public static final BLibHolder<SoundEvent> ENTITY_XENOMORPH_DEATH = create("entity.xenomorph.death");

    public static final BLibHolder<SoundEvent> ENTITY_XENOMORPH_HISS = create("entity.xenomorph.hiss");

    public static final BLibHolder<SoundEvent> ENTITY_XENOMORPH_HURT = create("entity.xenomorph.hurt");

    public static final BLibHolder<SoundEvent> ENTITY_XENOMORPH_IDLE = create("entity.xenomorph.idle");

    public static final BLibHolder<SoundEvent> ENTITY_XENOMORPH_LUNGE = create("entity.xenomorph.lunge");

    public static final BLibHolder<SoundEvent> ITEM_ARMOR_EQUIP_CHITIN = create("item.armor.equip_chitin");

    public static final BLibHolder<SoundEvent> JUKEBOX_SOUNDS_ALIEN_MUSIC_1 = create("jukebox_sounds.alien_music_1");

    private static BLibHolder<SoundEvent> create(String path) {
        return REGISTRY.createHolder(path, () -> SoundEvent.createVariableRangeEvent(AlienResources.location(path)));
    }

    public static void initialize() {
        REGISTRY.registerAll();
    }
}

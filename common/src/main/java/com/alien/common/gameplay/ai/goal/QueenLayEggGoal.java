package com.alien.common.gameplay.ai.goal;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.entity.living.alien.ovomorph.Ovomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.alien.compatibility.avp_human.AVPHuman;
import com.alien.compatibility.avp_human.GeneManagerProxy;
import com.human.common.model.GeneCarrier;
import com.human.common.util.GeneIntegrityUtil;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;

import java.util.concurrent.TimeUnit;

public class QueenLayEggGoal extends Goal {

    private static final int MAX_EGG_LAY_COOLDOWN_IN_TICKS = (int) TimeUnit.MINUTES.toSeconds(1) * 20;

    private static final int MAX_EGG_COUNT_IN_HIVE = 60;

    private final Queen queen;

    private int eggLayCooldownInTicks;

    public QueenLayEggGoal(Queen queen) {
        this.queen = queen;
        this.eggLayCooldownInTicks = MAX_EGG_LAY_COOLDOWN_IN_TICKS;
    }

    @Override
    public boolean canUse() {
        if (eggLayCooldownInTicks > 0) {
            // Egg laying is currently under cooldown, decrement and return.
            eggLayCooldownInTicks--;
            return false;
        }

        // Queen must be alive to lay eggs.
        return queen.isAlive()
            // AND Queen must have an ovipositor.
            && queen.getOvipositorManager().hasOvipositor()
            // AND Queen's variant is able to reproduce.
            && AlienVariantTypes.getFor(queen.getVariant()).canReproduce()
            && queen.getHiveManager()
                .hive()
                // AND Queen must have a hive...
                .isSomeAnd(
                    // And that hive must be alive...
                    hive -> hive.isAlive()
                        // AND the queen must be within the hive to lay eggs there.
                        && hive.getSpaceManager().isEntityWithinHive(queen)
                        && hive.getFactionData()
                            .getLoadedMemberCount(member -> member.is(AlienEntityTypeTags.OVOMORPHS)) < MAX_EGG_COUNT_IN_HIVE
                )
            // AND there must be no other eggs nearby already.
            && noEggsNearby();
    }

    @Override
    public void start() {
        // Reset egg lay cooldown since it's (almost) guaranteed that the queen is about to lay an egg.
        this.eggLayCooldownInTicks = MAX_EGG_LAY_COOLDOWN_IN_TICKS;
        var level = queen.level();
        var variant = shouldBeAberrant()
            // If the queen has weak genetic integrity, then it can become aberrant.
            ? AlienVariant.ABERRANT
            // Otherwise just use the queen's current variant type.
            : queen.getVariant();
        var ovomorphType = Ovomorph.getType(variant, false);

        var ovomorph = ovomorphType == null
            ? null
            : ovomorphType.create(level);

        if (ovomorph == null) {
            return;
        }

        ovomorph.setPos(queen.getOvipositorManager().getEggLayingPosition());
        ovomorph.setPersistenceRequired();
        ovomorph.isRooted.set(false);

        switch (queen.getGeneManager()) {
            case GeneManagerProxy.EMPTY ignored -> {/* NO-OP */}
            // Transfer genes.
            case GeneManagerProxy.Wrapper wrapper -> wrapper.transfer(ovomorph.getGeneManager(), false);
        }

        level.playSound(null, queen, AlienSoundEvents.ENTITY_OVOMORPH_LAID.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
        level.addFreshEntity(ovomorph);
    }

    private boolean shouldBeAberrant() {
        if (!AVPHuman.MOD.isLoaded()) {
            return false;
        }

        var geneCarrier = (GeneCarrier) queen;
        var geneDecayLevel = GeneIntegrityUtil.getGeneDecayLevel(geneCarrier);

        return switch (geneDecayLevel) {
            case FATAL, VOLATILE -> true;
            case STABLE -> false;
            case UNSTABLE -> {
                // Ex. -1.75 -> 1.75
                var totalGeneIntegrity = Math.abs(GeneIntegrityUtil.getTotalGeneticIntegrity(geneCarrier));
                // Ex. 1.75 - 1 = 0.75
                var chance = totalGeneIntegrity - Math.floor(totalGeneIntegrity);
                // Ex. 0.75 means 75% chance to be aberrant.
                yield queen.getRandom().nextDouble() < chance;
            }
        };
    }

    private boolean noEggsNearby() {
        // Reset scanning cooldown regardless of scanner outcome.
        this.eggLayCooldownInTicks = MAX_EGG_LAY_COOLDOWN_IN_TICKS;
        // Scan for ovomorphs.
        return queen.level()
            .getEntitiesOfClass(
                Ovomorph.class,
                // 4 block radius
                getBoundingBoxAtEggLayingPosition(),
                // Must be tagged as an ovomorph...
                entity -> entity.getType().is(AlienEntityTypeTags.OVOMORPHS)
                    && !entity.isRooted.get()
            )
            // If the list of eggs is empty, then the queen is good to lay an egg.
            .isEmpty();
    }

    private AABB getBoundingBoxAtEggLayingPosition() {
        var eggPos = queen.getOvipositorManager().getEggLayingPosition();
        var halfSize = 0.5;

        return new AABB(
            eggPos.x - halfSize,
            eggPos.y - queen.level().dimensionType().height(),
            eggPos.z - halfSize,
            eggPos.x + halfSize,
            eggPos.y + 5,
            eggPos.z + halfSize
        );
    }
}

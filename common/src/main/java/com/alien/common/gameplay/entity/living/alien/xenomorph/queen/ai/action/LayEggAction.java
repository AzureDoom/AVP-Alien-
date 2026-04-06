package com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai.action;

import com.alien.common.gameplay.entity.living.alien.ovomorph.Ovomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.compatibility.avp_human.AVPHuman;
import com.alien.compatibility.avp_human.GeneManagerProxy;
import com.human.common.model.GeneCarrier;
import com.human.common.util.GeneIntegrityUtil;
import com.just.goap.action.Action;
import net.minecraft.sounds.SoundSource;

public class LayEggAction {

    public static Action.Signal perform(Action.Context<? extends Queen> context) {
        var queen = context.getActor();

        queen.getQueenData().resetEggLayCooldown();

        var level = queen.level();
        var variant = shouldBeAberrant(queen) ? AlienVariant.ABERRANT : queen.getVariant();
        var ovomorphType = Ovomorph.getType(variant, false);

        var ovomorph = ovomorphType == null ? null : ovomorphType.create(level);

        if (ovomorph == null) {
            return Action.Signal.ABORT;
        }

        ovomorph.setPos(queen.getOvipositorManager().getEggLayingPosition());
        ovomorph.setPersistenceRequired();
        ovomorph.isRooted.set(false);

        switch (queen.getGeneManager()) {
            case GeneManagerProxy.EMPTY ignored -> { /* NO-OP */ }
            case GeneManagerProxy.Wrapper wrapper -> wrapper.transfer(ovomorph.getGeneManager(), false);
        }

        level.playSound(null, queen, AlienSoundEvents.ENTITY_OVOMORPH_LAID.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
        level.addFreshEntity(ovomorph);

        return Action.Signal.CONTINUE;
    }

    private static boolean shouldBeAberrant(Queen queen) {
        if (!AVPHuman.MOD.isLoaded()) {
            return false;
        }

        var geneCarrier = (GeneCarrier) queen;
        var geneDecayLevel = GeneIntegrityUtil.getGeneDecayLevel(geneCarrier);

        return switch (geneDecayLevel) {
            case FATAL, VOLATILE -> true;
            case STABLE -> false;
            case UNSTABLE -> {
                var totalGeneIntegrity = Math.abs(GeneIntegrityUtil.getTotalGeneticIntegrity(geneCarrier));
                var chance = totalGeneIntegrity - Math.floor(totalGeneIntegrity);

                yield queen.getRandom().nextDouble() < chance;
            }
        };
    }

    private LayEggAction() {
        throw new UnsupportedOperationException();
    }
}

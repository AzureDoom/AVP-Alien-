package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying.action;

import com.alien.common.gameplay.entity.living.alien.ovomorph.Ovomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying.EggLayer;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.compatibility.avp_human.AVPHuman;
import com.alien.compatibility.avp_human.GeneManagerProxy;
import com.human.common.model.GeneCarrier;
import com.human.common.util.GeneIntegrityUtil;
import com.just.ai.goap.action.Action;
import net.minecraft.sounds.SoundSource;

public class LayEggAction {

    public static Action.Signal perform(Action.Context<? extends EggLayer> context) {
        var eggLayer = context.getActor();

        eggLayer.resetEggLayCooldown();

        var level = eggLayer.level();
        var variant = shouldBeAberrant(eggLayer) ? AlienVariant.ABERRANT : eggLayer.getVariant();
        var ovomorphType = Ovomorph.getType(variant, false);

        var ovomorph = ovomorphType == null ? null : ovomorphType.create(level);

        if (ovomorph == null) {
            return Action.Signal.ABORT;
        }

        ovomorph.setPos(eggLayer.getEggLayingPosition());
        ovomorph.setPersistenceRequired();
        ovomorph.isRooted.set(false);

        switch (eggLayer.getGeneManager()) {
            case GeneManagerProxy.EMPTY ignored -> { /* NO-OP */ }
            case GeneManagerProxy.Wrapper wrapper -> wrapper.transfer(ovomorph.getGeneManager(), false);
        }

        level.playSound(null, eggLayer.asEntity(), AlienSoundEvents.ENTITY_OVOMORPH_LAID.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
        level.addFreshEntity(ovomorph);

        return Action.Signal.CONTINUE;
    }

    private static boolean shouldBeAberrant(EggLayer eggLayer) {
        if (!AVPHuman.MOD.isLoaded() || !(eggLayer instanceof GeneCarrier geneCarrier)) {
            return false;
        }

        var geneDecayLevel = GeneIntegrityUtil.getGeneDecayLevel(geneCarrier);

        return switch (geneDecayLevel) {
            case FATAL, VOLATILE -> true;
            case STABLE -> false;
            case UNSTABLE -> {
                var totalGeneIntegrity = Math.abs(GeneIntegrityUtil.getTotalGeneticIntegrity(geneCarrier));
                var chance = totalGeneIntegrity - Math.floor(totalGeneIntegrity);

                yield eggLayer.getRandom().nextDouble() < chance;
            }
        };
    }

    private LayEggAction() {
        throw new UnsupportedOperationException();
    }
}

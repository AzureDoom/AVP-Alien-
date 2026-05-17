package com.alien.mixin;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.hive2.convoy.RaidMemberTracker;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

/**
 * Recalls materialized raid members before Minecraft serializes an unloading chunk.
 */
@Mixin(PersistentEntitySectionManager.class)
public abstract class MixinPersistentEntitySectionManager_ConvoyUnload<T extends EntityAccess> {

    @Shadow
    @Final
    private EntitySectionStorage<T> sectionStorage;

    @Inject(method = "processChunkUnload", at = @At("HEAD"))
    private void avp_alien$returnRaidMembersBeforeChunkUnload(
        long chunkPos,
        CallbackInfoReturnable<Boolean> cir
    ) {
        var raidMembers = new ArrayList<Alien>();
        this.sectionStorage.getExistingSectionsInChunk(chunkPos)
            .flatMap(EntitySection::getEntities)
            .forEach(entity -> {
                if (entity instanceof Alien alien && alien.raidMembership() != null && !alien.isRemoved()) {
                    raidMembers.add(alien);
                }
            });

        for (var alien : raidMembers) {
            RaidMemberTracker.returnUnloaded(alien);
            alien.discard();
        }
    }
}

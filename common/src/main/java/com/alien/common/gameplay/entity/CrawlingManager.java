package com.alien.common.gameplay.entity;

import com.blib.api.common.data_sync.v1.DataAccessor;
import com.blib.api.common.dismemberment.v1.Dismemberable;
import com.blib.api.common.dismemberment.v1.LimbCategories;
import com.blib.api.common.dismemberment.v1.LimbDefinitionRegistry;
import com.blib.api.common.nbt.v1.model.NBTSerializable;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigatorUser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.PathfinderMob;

public class CrawlingManager implements NBTSerializable {

    private static final String NBT_CRAWLING = "crawling";

    private final PathfinderMob entity;

    private final DataAccessor<Boolean> isCrawling;

    /**
     * Whether this entity type is permitted to crawl at all. Owned by the manager so callers (movement, animation,
     * dismemberment-eligibility) can ask one source of truth instead of poking at entity-class instanceof checks.
     */
    private final boolean canCrawl;

    public CrawlingManager(PathfinderMob entity, DataAccessor<Boolean> isCrawling, boolean canCrawl) {
        this.entity = entity;
        this.isCrawling = isCrawling;
        this.canCrawl = canCrawl;
    }

    public boolean canCrawl() {
        return canCrawl;
    }

    public void tick() {
        if (entity.level().isClientSide) {
            return;
        }

        if (!canCrawl) {
            return;
        }

        tryToCrawl();
    }

    public boolean isCrawling() {
        return isCrawling.get();
    }

    private void tryToCrawl() {
        var blockPosition = entity.blockPosition();
        var level = entity.level();
        var navigation = entity.getNavigation();

        if (level.isClientSide) {
            return;
        }

        var path = navigation.getPath();
        var pathRequestsCrawl = entity instanceof PathNavigatorUser navigatorUser
            && navigatorUser.getPathNavigator().shouldCrawl();
        var isTight = pathRequestsCrawl || isTightSpace(blockPosition);

        if (path != null && path.getNextNodeIndex() < path.getNodeCount()) {
            var previousNode = path.getPreviousNode();
            isTight = isTight || previousNode != null && isTightSpace(previousNode.asBlockPos());
            var nextNode = path.getNextNode();
            isTight = isTight || isTightSpace(nextNode.asBlockPos());
        }

        // A dismembered leg forces the stance into crawling regardless of overhead clearance — the mob lost a leg, it
        // can't stand back up.
        var hasLegOff = entity instanceof Dismemberable dismemberable && hasDetachedLegLimb(dismemberable);

        isCrawling.set(isTight || hasLegOff);
    }

    private boolean hasDetachedLegLimb(Dismemberable dismemberable) {
        var manager = dismemberable.getDismembermentManager();

        if (manager == null || !manager.hasAnyDetached()) {
            return false;
        }

        for (var definition : LimbDefinitionRegistry.getDefinitions(entity.getType())) {
            if (definition.category().equals(LimbCategories.LEG) && manager.isDetached(definition)) {
                return true;
            }
        }

        return false;
    }

    private boolean isTightSpace(BlockPos blockPos) {
        var level = entity.level();
        var above = blockPos.above();
        var aboveState = level.getBlockState(above);
        return !aboveState.isAir() && aboveState.entityCanStandOn(entity.level(), blockPos, entity);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        if (compoundTag.contains(NBT_CRAWLING)) {
            isCrawling.set(compoundTag.getBoolean(NBT_CRAWLING));
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        compoundTag.putBoolean(NBT_CRAWLING, isCrawling.get());
    }
}

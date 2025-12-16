package com.alien.compatibility.avp_human;

import com.human.common.gameplay.entity.manager.GeneContainer;
import net.minecraft.nbt.CompoundTag;

import java.util.function.Consumer;

public sealed interface GeneContainerProxy {

    static GeneContainerProxy create() {
        return AVPHuman.MOD.isLoaded()
            ? new Wrapper(new GeneContainer())
            : EMPTY.INSTANCE;
    }

    default void ifPresent(Consumer<Wrapper> consumer) {}

    default void clear() {
        if (this instanceof Wrapper(var geneContainer)) {
            geneContainer.clear();
        }
    }

    default void load(CompoundTag compoundTag) {
        if (this instanceof Wrapper(var geneContainer)) {
            geneContainer.load(compoundTag);
        }
    }

    default void save(CompoundTag compoundTag) {
        if (this instanceof Wrapper(var geneContainer)) {
            geneContainer.save(compoundTag);
        }
    }

    enum EMPTY implements GeneContainerProxy {
        INSTANCE
    }

    record Wrapper(GeneContainer geneContainer) implements GeneContainerProxy {}
}

package com.alien.client.render.dismemberment;

import com.blib.api.client.render.v1.dismemberment.ModelPartResolverRegistry;
import net.minecraft.client.model.AllayModel;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.RabbitModel;
import net.minecraft.client.model.WardenModel;
import net.minecraft.client.model.WolfModel;

/**
 * Registers per-model {@link ModelPartResolverRegistry} entries for vanilla mobs whose body parts are nested deeper
 * than direct children of the model root. The default {@code HierarchicalModel} resolver only walks one level, so mobs
 * like the warden ({@code root → bone → body → head/right_arm/left_arm}, plus {@code right_leg/left_leg} as children of
 * {@code bone}) and the allay ({@code root → root → body → right_arm/left_arm}, head as a sibling of body) need bespoke
 * navigation tables.
 */
public final class AlienLimbModelResolvers {

    private AlienLimbModelResolvers() {}

    public static void initialize() {
        registerWardenResolver();
        registerAllayResolver();
        registerReflectiveResolvers();
    }

    /**
     * Wolf/horse/fox/chicken/rabbit (and the chested-horse / undead-horse subclasses that inherit them) all keep their
     * named parts in private camelCase fields rather than via {@code root().getChild(...)}, so a single reflective
     * resolver per model class is the cleanest way to expose them. Subclasses inherit the registered resolver via the
     * registry's class-hierarchy lookup, so registering against {@code HorseModel} also covers chested + undead horse
     * variants.
     */
    private static void registerReflectiveResolvers() {
        ModelPartResolverRegistry.register(WolfModel.class, ReflectiveModelPartResolver.resolverFor(WolfModel.class));
        ModelPartResolverRegistry.register(HorseModel.class, ReflectiveModelPartResolver.resolverFor(HorseModel.class));
        ModelPartResolverRegistry.register(FoxModel.class, ReflectiveModelPartResolver.resolverFor(FoxModel.class));
        ModelPartResolverRegistry.register(ChickenModel.class, ReflectiveModelPartResolver.resolverFor(ChickenModel.class));
        ModelPartResolverRegistry.register(RabbitModel.class, ReflectiveModelPartResolver.resolverFor(RabbitModel.class));
    }

    private static void registerWardenResolver() {
        ModelPartResolverRegistry.register(WardenModel.class, (model, name) -> {
            var bone = model.root().getChild("bone");
            var body = bone.getChild("body");

            return switch (name) {
                case "head" -> body.getChild("head");
                case "body" -> body;
                case "right_arm" -> body.getChild("right_arm");
                case "left_arm" -> body.getChild("left_arm");
                case "right_leg" -> bone.getChild("right_leg");
                case "left_leg" -> bone.getChild("left_leg");
                default -> null;
            };
        });
    }

    private static void registerAllayResolver() {
        ModelPartResolverRegistry.register(AllayModel.class, (model, name) -> {
            var allayRoot = model.root().getChild("root");
            var body = allayRoot.getChild("body");

            return switch (name) {
                case "head" -> allayRoot.getChild("head");
                case "body" -> body;
                case "right_arm" -> body.getChild("right_arm");
                case "left_arm" -> body.getChild("left_arm");
                default -> null;
            };
        });
    }
}

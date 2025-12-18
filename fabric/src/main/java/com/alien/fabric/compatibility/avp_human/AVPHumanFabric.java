package com.alien.fabric.compatibility.avp_human;

import com.alien.compatibility.avp_human.AVPHuman;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;

public class AVPHumanFabric {

    public static final ResourceCondition IS_LOADED = ResourceConditions.allModsLoaded(AVPHuman.MOD.id());
}

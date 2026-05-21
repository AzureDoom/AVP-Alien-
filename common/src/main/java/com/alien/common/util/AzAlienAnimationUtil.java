package com.alien.common.util;

import com.alien.common.gameplay.entity.CrawlingManager;
import com.alien.common.gameplay.entity.living.alien.Alien;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.AzCommandBuilder;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehavior;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;
import com.blib.api.client.animation.v1.command.policy.OnPropertiesChanged;
import com.blib.api.client.animation.v1.track.AzTrackHandle;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public class AzAlienAnimationUtil {

    private static final float MAX_CRAWL_ANIMATION_SPEED = 2.0F;

    public static final AzTrackHandle<Alien> BODY = AzTrackHandle.declare("body");

    public static final AzTrackHandle<Alien> HEAD = AzTrackHandle.declare("head");

    public static final AzTrackHandle<Alien> LEFT_ARM = AzTrackHandle.declare("leftarm");

    public static final AzTrackHandle<Alien> LEFT_LEG = AzTrackHandle.declare("leftleg");

    public static final AzTrackHandle<Alien> LEFT_TITTY_ARM = AzTrackHandle.declare("lefttittyarm");

    public static final AzTrackHandle<Alien> RIGHT_ARM = AzTrackHandle.declare("rightarm");

    public static final AzTrackHandle<Alien> RIGHT_LEG = AzTrackHandle.declare("rightleg");

    public static final AzTrackHandle<Alien> RIGHT_TITTY_ARM = AzTrackHandle.declare("righttittyarm");

    public static final AzTrackHandle<Alien> TAIL = AzTrackHandle.declare("tail");

    public static final List<AzTrackHandle<Alien>> XENO_LIMBS = List.of(
        BODY,
        HEAD,
        LEFT_ARM,
        LEFT_LEG,
        RIGHT_ARM,
        RIGHT_LEG,
        TAIL
    );

    public static final List<AzTrackHandle<Alien>> XENO_QUEEN_LIMBS = List.of(
        BODY,
        HEAD,
        LEFT_ARM,
        LEFT_LEG,
        LEFT_TITTY_ARM,
        RIGHT_ARM,
        RIGHT_LEG,
        RIGHT_TITTY_ARM,
        TAIL
    );

    public static final List<AzTrackHandle<Alien>> XENO_EMPRESS_LIMBS = List.of(
        BODY,
        HEAD,
        LEFT_ARM,
        LEFT_LEG,
        LEFT_TITTY_ARM,
        RIGHT_ARM,
        RIGHT_LEG,
        RIGHT_TITTY_ARM,
        TAIL
    );

    public static float crawlAnimationSpeed(Alien alien) {
        var deltaX = alien.getX() - alien.xOld;
        var deltaZ = alien.getZ() - alien.zOld;
        var movementSpeed = (float) Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        var referenceSpeed = Math.max(0.01F, (float) alien.getAttributeValue(Attributes.MOVEMENT_SPEED));
        return Mth.clamp(
            (movementSpeed / referenceSpeed) * CrawlingManager.getCrawlSpeedMultiplier(alien),
            0.0F,
            MAX_CRAWL_ANIMATION_SPEED
        );
    }

    public static <T extends Alien> AzCommand<T> composeWithSpeed(
        List<? extends AzTrackHandle<? super T>> handles,
        String baseName,
        AzPlayBehavior playBehavior,
        AzDispatchMode dispatchMode,
        float speed
    ) {
        return AzCommand.compose(
            handles.stream()
                .map(
                    handle -> AzAlienAnimationUtil.<T>builderFor(dispatchMode)
                        .onPropertiesChanged(OnPropertiesChanged.UPDATE_IN_PLACE)
                        .play(handle, baseName + "." + handle.name(), playBehavior)
                        .setSpeed(handle, speed)
                        .build()
                )
                .toList()
        );
    }

    public static <T extends Alien> AzCommand<T> singleWithSpeed(
        AzTrackHandle<? super T> handle,
        String animationName,
        AzPlayBehavior playBehavior,
        AzDispatchMode dispatchMode,
        float speed
    ) {
        return AzAlienAnimationUtil.<T>builderFor(dispatchMode)
            .onPropertiesChanged(OnPropertiesChanged.UPDATE_IN_PLACE)
            .play(handle, animationName, playBehavior)
            .setSpeed(handle, speed)
            .build();
    }

    private static <T extends Alien> AzCommandBuilder<T> builderFor(AzDispatchMode mode) {
        return switch (mode) {
            case REPLAY -> AzCommand.replay();
            case PLAY_IF_NOT_PLAYING -> AzCommand.idempotent();
            case ENQUEUE -> AzCommand.enqueueing();
        };
    }
}

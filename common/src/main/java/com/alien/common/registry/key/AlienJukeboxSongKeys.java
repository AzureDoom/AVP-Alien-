package com.alien.common.registry.key;

import com.alien.AlienResources;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;

public class AlienJukeboxSongKeys {

    public static final ResourceKey<JukeboxSong> ALIEN_MUSIC_1 = create("alien_music_1");

    private static ResourceKey<JukeboxSong> create(String id) {
        var resourceLocation = AlienResources.location(id);
        return ResourceKey.create(Registries.JUKEBOX_SONG, resourceLocation);
    }
}

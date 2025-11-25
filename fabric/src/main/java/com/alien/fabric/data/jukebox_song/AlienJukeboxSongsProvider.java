package com.alien.fabric.data.jukebox_song;

import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.registry.key.AlienJukeboxSongKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.JukeboxSong;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class AlienJukeboxSongsProvider extends FabricDynamicRegistryProvider {

    public AlienJukeboxSongsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.add(AlienJukeboxSongKeys.ALIEN_MUSIC_1, createAlienMusic1Song());
    }

    private JukeboxSong createAlienMusic1Song() {
        return new JukeboxSong(
            AlienSoundEvents.JUKEBOX_SOUNDS_ALIEN_MUSIC_1.getHolder(),
            Component.translatable("jukebox_song.avp_alien.alien_music_1"),
            180,
            12
        );
    }

    @Override
    public @NotNull String getName() {
        return "Alien Jukebox Songs";
    }
}

package com.alien.fabric.client;

import com.alien.client.AlienClient;
import net.fabricmc.api.ClientModInitializer;

public class AlienFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AlienClient.initialize();
    }
}

package com.customwrld.commonlib;

import com.customwrld.commonlib.modules.pigeon.Listeners;
import com.customwrld.commonlib.modules.rank.RankAPI;
import com.customwrld.pigeon.Pigeon;
import com.customwrld.pigeon.listeners.Listener;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;

@Getter
public class CommonLib {

    @Getter private static CommonLib commonLib;
    @Setter private Plugin plugin;
    @Setter private boolean bukkitLoaded;

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;
    private final Pigeon pigeon;

    public CommonLib(MongoClient mongoClient, MongoDatabase mongoDatabase, Pigeon pigeon, Plugin plugin) {
        commonLib = this;

        this.mongoClient = mongoClient;
        this.mongoDatabase = mongoDatabase;
        this.pigeon = pigeon;

        this.pigeon.getPayloadsRegistry()
                .registerPayloadsInPackage("com.customwrld.commonlib.modules.pigeon.payloads");

        this.pigeon.getListenersRegistry()
                .registerListener(new Listeners());

        this.plugin = plugin;

        RankAPI.initialize();
    }

}
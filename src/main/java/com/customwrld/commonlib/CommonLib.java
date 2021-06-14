package com.customwrld.commonlib;

import com.customwrld.commonlib.modules.pigeon.Listeners;
import com.customwrld.pigeon.Pigeon;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CommonLib {

    @Getter private static CommonLib commonLib;
    @Setter private boolean bukkitLoaded;

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;
    private final Pigeon pigeon;

    public CommonLib(MongoClient mongoClient, MongoDatabase mongoDatabase, Pigeon pigeon) {
        commonLib = this;

        this.mongoClient = mongoClient;
        this.mongoDatabase = mongoDatabase;
        this.pigeon = pigeon;

        this.pigeon.getPayloadsRegistry()
                .registerPayloadsInPackage("com.customwrld.commonlib.modules.pigeon.payloads");

        this.pigeon.getListenersRegistry()
                .registerListener(new Listeners());
    }

}
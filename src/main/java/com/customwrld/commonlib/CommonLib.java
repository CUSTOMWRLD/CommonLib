package com.customwrld.commonlib;

import com.customwrld.commonlib.modules.api.GrantProvider;
import com.customwrld.commonlib.modules.api.PunishmentProvider;
import com.customwrld.commonlib.modules.pigeon.Listeners;
import com.customwrld.commonlib.modules.rank.RankAPI;
import com.customwrld.pigeon.Pigeon;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class CommonLib {

    @Getter private static CommonLib commonLib;

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;
    private final Pigeon pigeon;
    private final Set<GrantProvider> grantProviders;
    private final Set<PunishmentProvider> punishmentProviders;

    public CommonLib(MongoClient mongoClient, MongoDatabase mongoDatabase, Pigeon pigeon) {
        commonLib = this;

        this.grantProviders = new HashSet<>();
        this.punishmentProviders = new HashSet<>();

        this.mongoClient = mongoClient;
        this.mongoDatabase = mongoDatabase;
        this.pigeon = pigeon;

        this.pigeon.getConvertersRegistry()
                .registerConvertersInPackage("com.customwrld.commonlib.modules.pigeon.converters");

        this.pigeon.getPayloadsRegistry()
                .registerPayloadsInPackage("com.customwrld.commonlib.modules.pigeon.payloads");

        this.pigeon.getListenersRegistry()
                .registerListener(new Listeners());

        RankAPI.initialize();
    }

}
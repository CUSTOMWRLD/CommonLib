package com.customwrld.commonlib;

import com.customwrld.commonlib.modules.api.APIListener;
import com.customwrld.commonlib.modules.pigeon.Listeners;
import com.customwrld.commonlib.modules.rank.RankAPI;
import com.customwrld.commonlib.util.PigeonUtil;
import com.customwrld.commonlib.util.timer.TimerManager;
import com.customwrld.pigeon.Pigeon;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class CommonLib {

    @Getter private static CommonLib commonLib;

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;
    private final Pigeon pigeon;
    private final TimerManager timerManager;
    private final Set<APIListener> apiListeners;

    public CommonLib(MongoClient mongoClient, MongoDatabase mongoDatabase, Pigeon pigeon) {
        commonLib = this;

        this.mongoClient = mongoClient;
        this.mongoDatabase = mongoDatabase;
        this.pigeon = pigeon;

        this.pigeon.getConvertersRegistry()
                .registerConvertersInPackage("com.customwrld.commonlib.modules.pigeon.converters");

        this.pigeon.getPayloadsRegistry()
                .registerPayloadsInPackage("com.customwrld.commonlib.modules.pigeon.payloads");

        this.pigeon.getListenersRegistry()
                .registerListener(new Listeners());

        PigeonUtil.setPigeon(this.pigeon);

        RankAPI.initialize();

        this.timerManager = new TimerManager();

        this.apiListeners = new HashSet<>();
    }

}
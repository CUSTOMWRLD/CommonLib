package com.customwrld.commonlib.modules.pigeon.payloads.network;

import com.customwrld.commonlib.modules.network.Gamemode;
import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.Getter;

import java.util.List;

@Getter @Payload
public class ServerUpdatePayload extends BasePayload {

    @Transmit String name;
    @Transmit Gamemode gamemode;
    @Transmit Long uptime;
    @Transmit Double tps;
    @Transmit List<String> players;
    @Transmit Integer maxPlayers;

    public ServerUpdatePayload() {
        payloadId = "serverUpdatePayload";
    }

    public ServerUpdatePayload(String name, Gamemode gamemode, Long uptime, Double tps, List<String> players, Integer maxPlayers) {
        this();
        this.name = name;
        this.gamemode = gamemode;
        this.uptime = uptime;
        this.tps = tps;
        this.players = players;
        this.maxPlayers = maxPlayers;
    }

    @Override
    public void receive() {}

}

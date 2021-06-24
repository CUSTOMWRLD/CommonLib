package com.customwrld.commonlib.modules.pigeon.payloads;

import com.customwrld.commonlib.modules.network.Gamemode;
import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.Getter;

@Getter @Payload
public class GamemodeRegisterPayload extends BasePayload {

    public GamemodeRegisterPayload() {
        this.payloadId = "atom-registerGamemodePayload";
    }

    @Transmit
    Gamemode gamemode;

    public GamemodeRegisterPayload(Gamemode gamemode) {
        this();
        this.gamemode = gamemode;
    }

    @Override
    public void receive() {}

}

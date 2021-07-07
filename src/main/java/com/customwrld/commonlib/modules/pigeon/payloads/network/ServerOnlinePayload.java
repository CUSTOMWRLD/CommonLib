package com.customwrld.commonlib.modules.pigeon.payloads.network;

import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.Getter;

@Getter @Payload
public class ServerOnlinePayload extends BasePayload {

    @Transmit String name;

    public ServerOnlinePayload() {
        payloadId = "serverOnlinePayload";
    }

    public ServerOnlinePayload(String name) {
        this();
        this.name = name;
    }

    @Override
    public void receive() {}

}

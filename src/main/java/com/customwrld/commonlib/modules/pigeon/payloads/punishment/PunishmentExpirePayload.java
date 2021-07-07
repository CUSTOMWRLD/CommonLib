package com.customwrld.commonlib.modules.pigeon.payloads.punishment;

import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.Getter;

@Getter @Payload
public class PunishmentExpirePayload extends BasePayload {

    @Transmit String punishment;

    public PunishmentExpirePayload() {
        payloadId = "punishmentExpirePayload";
    }

    public PunishmentExpirePayload(String punishment) {
        this();
        this.punishment = punishment;
    }

    @Override
    public void receive() {}

}

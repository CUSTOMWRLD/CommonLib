package com.customwrld.commonlib.modules.pigeon.payloads;

import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.Getter;

@Getter @Payload
public class PunishmentRemovePayload extends BasePayload {

    @Transmit String punishment;

    public PunishmentRemovePayload() {
        payloadId = "common-punishmentRemove";
    }

    public PunishmentRemovePayload(String punishment) {
        this();
        this.punishment = punishment;
    }

    @Override
    public void receive() {}

}

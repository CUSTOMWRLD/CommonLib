package com.customwrld.commonlib.modules.pigeon.payloads;

import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.Getter;

@Getter @Payload
public class PunishmentAddPayload extends BasePayload {

    @Transmit String punishment;

    public PunishmentAddPayload() {
        payloadId = "common-punishmentAdd";
    }

    public PunishmentAddPayload(String punishment) {
        this();
        this.punishment = punishment;
    }

    @Override
    public void receive() {}

}

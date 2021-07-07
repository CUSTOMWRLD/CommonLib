package com.customwrld.commonlib.modules.pigeon.payloads.rank;

import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.Getter;

import java.util.UUID;

@Getter @Payload
public class RankRemovePayload extends BasePayload {

    @Transmit UUID rank;

    public RankRemovePayload() {
        payloadId = "RankRemovePayload";
    }

    public RankRemovePayload(UUID rank) {
        this();
        this.rank = rank;
    }

    @Override
    public void receive() {
    }

}

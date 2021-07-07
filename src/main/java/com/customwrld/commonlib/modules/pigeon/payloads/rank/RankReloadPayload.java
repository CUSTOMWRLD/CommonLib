package com.customwrld.commonlib.modules.pigeon.payloads.rank;

import com.customwrld.commonlib.modules.rank.Rank;
import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.Getter;

import java.util.UUID;

@Getter @Payload
public class RankReloadPayload extends BasePayload {

    @Transmit UUID rank;

    public RankReloadPayload() {
        payloadId = "RankReloadPayload";
    }

    public RankReloadPayload(Rank rank) {
        this();
        this.rank = rank.getUuid();
    }

    @Override
    public void receive() {
    }

}

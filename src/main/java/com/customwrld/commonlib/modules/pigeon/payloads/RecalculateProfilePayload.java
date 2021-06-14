package com.customwrld.commonlib.modules.pigeon.payloads;

import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.Getter;

import java.util.UUID;

@Getter @Payload
public class RecalculateProfilePayload extends BasePayload {

    @Transmit UUID profile;

    public RecalculateProfilePayload() {
        payloadId = "common-recalculateProfile";
    }

    public RecalculateProfilePayload(UUID profile) {
        this();
        this.profile = profile;
    }

    @Override
    public void receive() {}

}

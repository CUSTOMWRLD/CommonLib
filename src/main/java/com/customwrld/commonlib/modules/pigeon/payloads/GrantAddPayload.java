package com.customwrld.commonlib.modules.pigeon.payloads;

import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.Getter;

@Getter @Payload
public class GrantAddPayload extends BasePayload {

    @Transmit String grant;

    public GrantAddPayload() {
        payloadId = "common-grantAdd";
    }

    public GrantAddPayload(String grant) {
        this();
        this.grant = grant;
    }

    @Override
    public void receive() {}

}

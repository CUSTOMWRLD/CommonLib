package com.customwrld.commonlib.modules.pigeon.payloads.grant;

import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.Getter;

@Getter @Payload
public class GrantExpirePayload extends BasePayload {

    @Transmit String grant;

    public GrantExpirePayload() {
        payloadId = "grantExpirePayload";
    }

    public GrantExpirePayload(String grant) {
        this();
        this.grant = grant;
    }

    @Override
    public void receive() {}

}

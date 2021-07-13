package com.customwrld.commonlib.modules.pigeon.payloads.grant;

import com.customwrld.commonlib.modules.profile.Profile;
import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.Getter;

@Getter @Payload
public class GrantExpirePayload extends BasePayload {

    @Transmit Profile profile;  // Transmit the most up-to-date profile with the payload,
                                // so every single listener won't have to request the profile everytime it receives the payload.
    @Transmit String grant;

    public GrantExpirePayload() {
        payloadId = "grantExpirePayload";
    }

    public GrantExpirePayload(Profile profile, String grant) {
        this();
        this.profile = profile;
        this.grant = grant;
    }

    @Override
    public void receive() {}

}

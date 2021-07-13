package com.customwrld.commonlib.modules.pigeon.payloads.punishment;

import com.customwrld.commonlib.modules.profile.Profile;
import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.Getter;

@Getter @Payload
public class PunishmentAddPayload extends BasePayload {

    @Transmit Profile profile;  // Transmit the most up-to-date profile with the payload,
                                // so every single listener won't have to request the profile everytime it receives the payload.
    @Transmit String punishment;

    public PunishmentAddPayload() {
        payloadId = "punishmentAddPayload";
    }

    public PunishmentAddPayload(Profile profile, String punishment) {
        this();
        this.profile = profile;
        this.punishment = punishment;
    }

    @Override
    public void receive() {}

}

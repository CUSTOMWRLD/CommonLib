package com.customwrld.commonlib.modules.pigeon.payloads.network;

import com.customwrld.commonlib.modules.pigeon.enums.NetworkSetting;
import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.Getter;

import java.util.UUID;

@Getter @Payload
public class ModifyNetworkPayload extends BasePayload {

    @Transmit NetworkSetting setting;

    public ModifyNetworkPayload() {
        payloadId = "modifyNetworkPayload";
    }

    public ModifyNetworkPayload(NetworkSetting setting) {
        this();
        this.setting = setting;
    }

    @Override
    public void receive() {}

}

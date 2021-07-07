package com.customwrld.commonlib.util;

import com.customwrld.pigeon.Pigeon;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.Getter;
import lombok.Setter;

public class PigeonUtil {

    @Getter @Setter private static Pigeon pigeon;

    public static void broadcast(BasePayload payload) {
        pigeon.broadcast(payload);
    }

}

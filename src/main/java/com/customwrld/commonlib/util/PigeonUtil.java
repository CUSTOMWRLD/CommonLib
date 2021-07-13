package com.customwrld.commonlib.util;

import com.customwrld.pigeon.Pigeon;
import com.customwrld.pigeon.PostalUnit;
import com.customwrld.pigeon.payloads.bases.BasePayload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class PigeonUtil {

    @Getter @Setter private static Pigeon pigeon;

    public static void broadcast(BasePayload payload) {
        pigeon.broadcast(payload);
    }

    @Getter @AllArgsConstructor
    public enum GeneralUnits {
        ATOM(new PostalUnit("atom")),
        PROXY(new PostalUnit("proxy"));

        private final PostalUnit unit;
    }

}

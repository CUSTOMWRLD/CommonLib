package com.customwrld.commonlib.modules.bukkit.events;

import com.customwrld.commonlib.modules.grant.Grant;
import com.customwrld.commonlib.util.BaseEvent;
import lombok.Getter;

@Getter
public final class GrantExpireEvent extends BaseEvent {

    private final Grant grant;

    public GrantExpireEvent(Grant grant) {
        this.grant = grant;
    }

}
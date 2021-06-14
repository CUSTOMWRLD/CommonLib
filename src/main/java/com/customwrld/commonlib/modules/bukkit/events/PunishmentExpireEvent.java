package com.customwrld.commonlib.modules.bukkit.events;

import com.customwrld.commonlib.modules.punishment.Punishment;
import com.customwrld.commonlib.util.BaseEvent;
import lombok.Getter;

@Getter
public final class PunishmentExpireEvent extends BaseEvent {

    private final Punishment punishment;

    public PunishmentExpireEvent(Punishment punishment) {
        this.punishment = punishment;
    }

}
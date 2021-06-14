package com.customwrld.commonlib.modules.bukkit.events;

import com.customwrld.commonlib.modules.punishment.Punishment;
import com.customwrld.commonlib.util.BaseEvent;
import lombok.Getter;

@Getter
public final class PunishmentRemoveEvent extends BaseEvent {

    private final Punishment punishment;

    public PunishmentRemoveEvent(Punishment punishment) {
        this.punishment = punishment;
    }

}
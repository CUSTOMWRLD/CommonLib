package com.customwrld.commonlib.modules.bukkit.events;

import com.customwrld.commonlib.modules.punishment.Punishment;
import com.customwrld.commonlib.util.BaseEvent;
import lombok.Getter;

@Getter
public final class PunishmentAddEvent extends BaseEvent {

    private final Punishment punishment;

    public PunishmentAddEvent(Punishment punishment) {
        this.punishment = punishment;
    }

}
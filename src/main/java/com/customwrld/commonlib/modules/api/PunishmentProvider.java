package com.customwrld.commonlib.modules.api;

import com.customwrld.commonlib.modules.punishment.Punishment;

public interface PunishmentProvider {

    void punishmentAdd(Punishment punishment);

    void punishmentExpire(Punishment punishment);

    void punishmentRemove(Punishment punishment);

}

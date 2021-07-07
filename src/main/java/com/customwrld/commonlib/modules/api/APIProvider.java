package com.customwrld.commonlib.modules.api;

import com.customwrld.commonlib.modules.grant.Grant;
import com.customwrld.commonlib.modules.punishment.Punishment;

public interface APIProvider {

    void grantAdd(Grant grant);

    void grantExpire(Grant grant);

    void grantRemove(Grant grant);

    void punishmentAdd(Punishment punishment);

    void punishmentExpire(Punishment punishment);

    void punishmentRemove(Punishment punishment);

}

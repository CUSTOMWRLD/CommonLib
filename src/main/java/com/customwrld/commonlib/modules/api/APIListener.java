package com.customwrld.commonlib.modules.api;

import com.customwrld.commonlib.modules.grant.Grant;
import com.customwrld.commonlib.modules.punishment.Punishment;

public interface APIListener {

    default void grantAdd(Grant grant) {}

    default void grantExpire(Grant grant) {}

    default void grantRemove(Grant grant) {}

    default void punishmentAdd(Punishment punishment) {}

    default void punishmentExpire(Punishment punishment) {}

    default void punishmentRemove(Punishment punishment) {}

}

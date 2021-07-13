package com.customwrld.commonlib.modules.api;

import com.customwrld.commonlib.modules.grant.Grant;
import com.customwrld.commonlib.modules.profile.Profile;
import com.customwrld.commonlib.modules.punishment.Punishment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface APIListener {

    default void grantAdd(Grant grant) {}

    default void grantExpire(Grant grant) {}

    default void grantRemove(Grant grant) {}

    default void punishmentAdd(Punishment punishment) {}

    default void punishmentExpire(Punishment punishment) {}

    default void punishmentRemove(Punishment punishment) {}

}

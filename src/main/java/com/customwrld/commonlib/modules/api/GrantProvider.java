package com.customwrld.commonlib.modules.api;

import com.customwrld.commonlib.modules.grant.Grant;

public interface GrantProvider {

    void grantAdd(Grant grant);

    void grantExpire(Grant grant);

    void grantRemove(Grant grant);

}

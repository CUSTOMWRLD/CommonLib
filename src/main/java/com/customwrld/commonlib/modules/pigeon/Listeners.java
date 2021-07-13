package com.customwrld.commonlib.modules.pigeon;

import com.customwrld.commonlib.CommonLib;
import com.customwrld.commonlib.modules.grant.Grant;
import com.customwrld.commonlib.modules.pigeon.payloads.grant.GrantAddPayload;
import com.customwrld.commonlib.modules.pigeon.payloads.grant.GrantExpirePayload;
import com.customwrld.commonlib.modules.pigeon.payloads.grant.GrantRemovePayload;
import com.customwrld.commonlib.modules.pigeon.payloads.punishment.PunishmentAddPayload;
import com.customwrld.commonlib.modules.pigeon.payloads.punishment.PunishmentExpirePayload;
import com.customwrld.commonlib.modules.pigeon.payloads.punishment.PunishmentRemovePayload;
import com.customwrld.commonlib.modules.punishment.Punishment;
import com.customwrld.pigeon.annotations.PayloadHandler;
import com.customwrld.pigeon.annotations.PayloadListener;
import com.customwrld.pigeon.listeners.Listener;

@PayloadListener
public class Listeners implements Listener {

    @PayloadHandler
    public void onGrantAdd(GrantAddPayload payload) {
        Grant grant = Grant.getGrant(payload.getGrant());

        if(grant != null) {
            CommonLib.getCommonLib().getApiListeners().forEach(provider -> provider.grantAdd(grant));
        }
    }

    @PayloadHandler
    public void onGrantRemove(GrantRemovePayload payload) {
        Grant grant = Grant.getGrant(payload.getGrant());

        if(grant != null)
            CommonLib.getCommonLib().getApiListeners().forEach(provider -> provider.grantRemove(grant));
    }

    @PayloadHandler
    public void onGrantExpire(GrantExpirePayload payload) {
        Grant grant = Grant.getGrant(payload.getGrant());

        if(grant != null)
            CommonLib.getCommonLib().getApiListeners().forEach(provider -> provider.grantExpire(grant));
    }

    @PayloadHandler
    public void onPunishmentAdd(PunishmentAddPayload payload) {
        Punishment punishment = Punishment.getPunishment(payload.getPunishment());

        if(punishment != null)
            CommonLib.getCommonLib().getApiListeners().forEach(provider -> provider.punishmentAdd(punishment));
    }

    @PayloadHandler
    public void onPunishmentRemove(PunishmentRemovePayload payload) {
        Punishment punishment = Punishment.getPunishment(payload.getPunishment());

        if(punishment != null)
            CommonLib.getCommonLib().getApiListeners().forEach(provider -> provider.punishmentRemove(punishment));
    }

    @PayloadHandler
    public void onPunishmentExpire(PunishmentExpirePayload payload) {
        Punishment punishment = Punishment.getPunishment(payload.getPunishment());

        if(punishment != null)
            CommonLib.getCommonLib().getApiListeners().forEach(provider -> provider.punishmentExpire(punishment));
    }

}

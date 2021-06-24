package com.customwrld.commonlib.modules.profile;

import com.customwrld.commonlib.CommonLib;
import com.customwrld.commonlib.modules.bukkit.BukkitAPI;
import com.customwrld.commonlib.modules.bukkit.events.*;
import com.customwrld.commonlib.modules.grant.Grant;
import com.customwrld.commonlib.modules.grant.GrantAPI;
import com.customwrld.commonlib.modules.network.Gamemode;
import com.customwrld.commonlib.modules.pigeon.payloads.*;
import com.customwrld.commonlib.modules.punishment.Punishment;
import com.customwrld.commonlib.modules.rank.Rank;
import com.customwrld.commonlib.modules.rank.RankAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ProfileAPI {

    private final Profile profile;
    private final CommonLib commonLib;

    public ProfileAPI(Profile profile) {
        this.profile = profile;
        this.commonLib = CommonLib.getCommonLib();
    }

    /**
     * Returns a Cached Profile.
     */
    public static Profile getCachedProfile(UUID uuid) {
        return Profile.getProfiles().get(uuid);
    }

    /**
     * Returns if the Profile is ignoring another Profile.
     */
    public boolean isIgnoring(Profile profile) {
        return this.profile.getIgnored().contains(profile.getId());
    }

    /**
     * Returns the amount of Punishments the Profile has by a Type.
     */
    public int getPunishmentCountByType(Punishment.Type type) {
        int i = 0;

        for (Punishment punishment : this.profile.getCachedPunishments()) {

            if (punishment.getType() == type) i++;
        }

        return i;
    }

    /**
     * Returns the active Punishment by a Type.
     */
    public Punishment getActivePunishmentByType(Punishment.Type type) {
        for (Punishment punishment : this.profile.getCachedPunishments()) {
            if (punishment.getType() == type && !punishment.isRemoved() && !punishment.api().hasExpired()) {
                return punishment;
            }
        }

        return null;
    }

    /**
     * Returns either an active Ban or Blacklist.
     */
    public Punishment getActiveBan() {
        Punishment punishment = this.getActivePunishmentByType(Punishment.Type.BLACKLIST);

        if(punishment != null) {
            return punishment;
        }

        return this.getActivePunishmentByType(Punishment.Type.BAN);
    }

    /**
     * Returns a List of all the Profile's Permissions.
     */
    public List<String> getAllPermissions() {
        List<String> permissions = new ArrayList<>();

        permissions.addAll(this.getRankPermissions());
        permissions.addAll(this.profile.getPermissions());

        return permissions;
    }

    /**
     * Returns a List of all the Profile's Permissions.
     */
    public List<String> getRankPermissions() {
        List<String> permissions = new ArrayList<>();
        for (Grant grant : this.profile.getCachedGrants()) {

            Rank rank = grant.api().getRank();

            if(rank != null) {
                permissions.addAll(grant.api().getRank().api().getAllPermissions());
            }
        }
        return permissions;
    }

    /**
     * Validates the Profile's Punishments.
     */
    public void validatePunishments() {
        for (Punishment punishment : this.profile.getCachedPunishments()) {
            if (!punishment.isRemoved() && punishment.api().hasExpired()) {
                this.expirePunishment(punishment, System.currentTimeMillis());
            }
        }
    }

    /**
     * Adds a Punishment to the Profile.
     */
    public void addPunishment(Punishment punishment) {
        this.profile.getPunishments().add(punishment.getId());
        this.profile.getCachedPunishments().add(punishment);

        punishment.save();
        this.profile.save();

        this.commonLib.getPigeon().broadcast(new PunishmentAddPayload(punishment.getId()));

        if(BukkitAPI.isBukkitLoaded()) {
            Bukkit.getServer().getScheduler().runTask(CommonLib.getCommonLib().getPlugin(), () -> Bukkit.getServer().getPluginManager().callEvent(new PunishmentAddEvent(punishment)));
        }
    }

    /**
     * Removes a Punishment from the Profile.
     */
    public void removePunishment(Punishment punishment, @Nullable UUID removedByUUID, Long removedAt, String removedReason) {
        punishment.api().remove(removedByUUID, removedAt, removedReason);

        this.profile.save();

        this.commonLib.getPigeon().broadcast(new PunishmentRemovePayload(punishment.getId()));

        if(BukkitAPI.isBukkitLoaded()) {
            Bukkit.getServer().getScheduler().runTask(CommonLib.getCommonLib().getPlugin(), () -> Bukkit.getServer().getPluginManager().callEvent(new PunishmentRemoveEvent(punishment)));
        }
    }

    /**
     * Expires a Punishment on the Profile.
     */
    public void expirePunishment(Punishment punishment, Long removedAt) {
        punishment.api().remove(null, removedAt, "Punishment Expired");

        this.profile.save();

        this.commonLib.getPigeon().broadcast(new PunishmentExpirePayload(punishment.getId()));

        if(BukkitAPI.isBukkitLoaded()) {
            Bukkit.getServer().getScheduler().runTask(CommonLib.getCommonLib().getPlugin(), () -> Bukkit.getServer().getPluginManager().callEvent(new PunishmentExpireEvent(punishment)));
        }
    }

    /**
     * Validates the Profile's Grants.
     */
    public void validateGrants() {
        Iterator<Grant> iterator = this.profile.getCachedGrants().iterator();

        while (iterator.hasNext()) {
            Grant grant = iterator.next();

            if (Rank.getRank(grant.getRankUUID()) == null) {
                this.profile.getGrants().remove(grant.getId());
                grant.delete();
                iterator.remove();
            } else if (!grant.isRemoved() && grant.api().hasExpired()) {
                this.expireGrant(grant, System.currentTimeMillis());
            }
        }
    }

    /**
     * Returns the active Grant of the Profile.
     */
    public Grant getActiveGrant() {
        this.validateGrants();

        List<Grant> cachedGrants = this.profile.getCachedGrants();
        List<Grant> activeGrants = new ArrayList<>();

        for(Grant grant : cachedGrants) {
            if (!grant.isRemoved() && !grant.api().hasExpired()) {
                activeGrants.add(grant);
            }
        }

        activeGrants.sort(GrantAPI.COMPARATOR);

        if(activeGrants.isEmpty()) {
            Grant grant = new Grant(
                    this.profile.getId(),
                    RankAPI.getDefaultRank().getUuid(),
                    null,
                    System.currentTimeMillis(),
                    "Default",
                    Integer.MAX_VALUE
            );

            this.addGrant(grant);

            return grant;
        } else {
            return activeGrants.get(0);
        }
    }

    /**
     * Adds a new Grant to the Profile.
     */
    public void addGrant(Grant grant) {
        this.profile.getGrants().add(grant.getId());
        this.profile.getCachedGrants().add(grant);

        grant.save();
        this.profile.save();

        this.commonLib.getPigeon().broadcast(new GrantAddPayload(grant.getId()));

        if(BukkitAPI.isBukkitLoaded()) {
            Bukkit.getServer().getScheduler().runTask(CommonLib.getCommonLib().getPlugin(), () -> Bukkit.getServer().getPluginManager().callEvent(new GrantAddEvent(grant)));
        }
    }

    /**
     * Removes a Grant from the Profile.
     */
    public void removeGrant(Grant grant, UUID removedBy, Long removedAt, String removedReason) {
        grant.api().remove(removedBy, removedAt, removedReason);
        this.profile.save();

        this.commonLib.getPigeon().broadcast(new GrantRemovePayload(grant.getId()));

        if(BukkitAPI.isBukkitLoaded()) {
            Bukkit.getServer().getScheduler().runTask(CommonLib.getCommonLib().getPlugin(), () -> Bukkit.getServer().getPluginManager().callEvent(new GrantRemoveEvent(grant)));
        }
    }

    /**
     * Expires a Grant on the Profile.
     */
    public void expireGrant(Grant grant, Long removedAt) {
        grant.api().remove(null, removedAt, "Grant Expired");
        this.profile.save();

        this.commonLib.getPigeon().broadcast(new GrantExpirePayload(grant.getId()));

        if(BukkitAPI.isBukkitLoaded()) {
            Bukkit.getServer().getScheduler().runTask(CommonLib.getCommonLib().getPlugin(), () -> Bukkit.getServer().getPluginManager().callEvent(new GrantExpireEvent(grant)));
        }
    }

    public boolean addGamemodeStorage(GamemodeStorage storage) {
        var gamemodeStorages = this.profile.getGamemodeStorages();

        var gamemode = storage.getGamemode();
        if(gamemodeStorages.containsKey(gamemode)) { return false; }

        gamemodeStorages.put(gamemode, storage);
        return true;
    }

    public GamemodeStorage getGamemodeStorage(Gamemode gamemode) {
        return this.profile.getGamemodeStorages().get(gamemode);
    }

    public boolean hasGamemodeStorage(Gamemode gamemode) {
        return this.profile.getGamemodeStorages().containsKey(gamemode);
    }

    public boolean ensureGamemodeStorage(Gamemode gamemode, List<GamemodeStorage.StorageValue> values) {
        var anyEnsured = false;

        if(hasGamemodeStorage(gamemode)) {
            var storage = getGamemodeStorage(gamemode);
            for(GamemodeStorage.StorageValue value : values) {
                if(storage.ensure(value)) { anyEnsured = true; }
            }
        } else {
            anyEnsured = true;

            var storage = new GamemodeStorage(gamemode);
            for(GamemodeStorage.StorageValue value : values) {
                storage.add(value);
            }

            addGamemodeStorage(storage);
        }

        return anyEnsured;
    }

}

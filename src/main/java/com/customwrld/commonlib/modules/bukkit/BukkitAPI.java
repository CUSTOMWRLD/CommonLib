package com.customwrld.commonlib.modules.bukkit;

import com.customwrld.commonlib.CommonLib;
import com.customwrld.commonlib.modules.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class BukkitAPI {

    /**
     * Returns a if Bukkit is loaded or not.
     */
    public static boolean isBukkitLoaded() {
        return CommonLib.getCommonLib().isBukkitLoaded();
    }

    /**
     * Returns a Bukkit Player.
     */
    public static Player getPlayer(Profile profile) {
        return Bukkit.getPlayer(profile.getId());
    }

    /**
     * Sets up the Bukkit Player.
     */
    // TODO: Move to Flame
    public static void setupBukkitPlayer(Plugin plugin, Profile profile) {
        if(isBukkitLoaded()) {
            Player player = BukkitAPI.getPlayer(profile);
            player.setOp(profile.getStaffProfile().isOperator());

            for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
                if (attachmentInfo.getAttachment() == null || !attachmentInfo.getAttachment().getPlugin().equals(plugin)) {
                    continue;
                }

                attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> attachmentInfo.getAttachment().unsetPermission(permission));
            }

            PermissionAttachment attachment = player.addAttachment(plugin);

            for (String permission : profile.api().getAllPermissions()) {
                attachment.setPermission(permission, true);
            }

            player.recalculatePermissions();
        }
    }

}

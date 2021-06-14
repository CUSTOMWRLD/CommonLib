package com.customwrld.commonlib.modules.bukkit;

import com.customwrld.commonlib.CommonLib;
import com.customwrld.commonlib.modules.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

}

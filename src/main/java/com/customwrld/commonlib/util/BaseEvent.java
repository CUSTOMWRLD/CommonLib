package com.customwrld.commonlib.util;

import com.customwrld.commonlib.CommonLib;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BaseEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void call() {
        if(CommonLib.getCommonLib().isBukkitLoaded()) {
            Bukkit.getServer().getPluginManager().callEvent(this);
        }
    }

}
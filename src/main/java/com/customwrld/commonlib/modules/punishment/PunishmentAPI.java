package com.customwrld.commonlib.modules.punishment;

import com.customwrld.commonlib.util.Minecraft;
import com.customwrld.commonlib.util.TimeUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PunishmentAPI {

    private final Punishment punishment;

    public PunishmentAPI(Punishment punishment) {
        this.punishment = punishment;
    }

    /**
     * Returns if the Punishment is permanent or not.
     */
    public boolean isPermanent() {
        return this.punishment.getType() == Punishment.Type.BLACKLIST || this.punishment.getDuration() == Integer.MAX_VALUE;
    }

    /**
     * Returns if the Punishment is active or not.
     */
    public boolean isActive() {
        return !this.punishment.isRemoved() && (this.isPermanent() || this.getMillisRemaining() < 0L);
    }

    /**
     * Returns the remaining amount of milliseconds of the punishment.
     */
    public long getMillisRemaining() {
        return (this.punishment.getAddedAt() + this.punishment.getDuration()) - System.currentTimeMillis();
    }

    /**
     * Returns if the Punishment has expired or not.
     */
    public boolean hasExpired() {
        return (!this.isPermanent()) && (System.currentTimeMillis() >= this.punishment.getAddedAt() + this.punishment.getDuration());
    }

    /**
     * Returns the Duration String.
     */
    public String getDurationText() {
        if (this.isPermanent() || this.punishment.getDuration() == 0) {
            return "Permanent";
        } else {
            return TimeUtil.millisToRoundedTime(this.punishment.getDuration());
        }
    }

    /**
     * Returns the Remaining String.
     */
    public String getTimeRemaining() {
        if (this.punishment.isRemoved()) {
            return "Removed";
        }

        if (this.isPermanent()) {
            return "Permanent";
        }

        if (hasExpired()) {
            return "Expired";
        }

        return TimeUtil.millisToRoundedTime((this.punishment.getAddedAt() + this.punishment.getDuration()) - System.currentTimeMillis());
    }

    /**
     * Returns the Context String.
     */
    public String getContext() {
        if (!(this.punishment.getType() == Punishment.Type.BAN || this.punishment.getType() == Punishment.Type.MUTE)) {
            return this.punishment.isRemoved() ? this.punishment.getType().getUndoContext() : this.punishment.getType().getContext();
        }

        if (this.isPermanent()) {
            return (this.punishment.isRemoved() ? this.punishment.getType().getUndoContext() : "permanently " + this.punishment.getType().getContext());
        } else {
            return (this.punishment.isRemoved() ? this.punishment.getType().getUndoContext() : "temporarily " + this.punishment.getType().getContext());
        }
    }

    /**
     * Removes a Punishment.
     */
    public void remove(@Nullable UUID removedBy, Long removedAt, String removedReason) {
        this.punishment.setRemovedByUUID(removedBy);
        this.punishment.setRemoved(true);
        this.punishment.setRemovedAt(removedAt);
        this.punishment.setRemovedReason(removedReason);
        this.punishment.save();
    }

    /**
     * Returns a list of TextComponents for the Punishment Message.
     */
    public List<TextComponent> getPunishmentMessage() {
        Date date = new Date(this.punishment.getAddedAt());

        return Arrays.asList(
                Minecraft.Style.SEPARATOR,
                Component.text("You are " + this.getContext() + (!this.isPermanent() ? " for " + this.getTimeRemaining() : "") + ".")
                        .color(NamedTextColor.RED),
                Component.empty(),
                Component.text("Reason: ")
                        .color(NamedTextColor.GRAY)
                        .append(
                                Component.text(this.punishment.getAddedReason())
                                        .color(NamedTextColor.WHITE)
                        ),
                Component.text("Added On: ")
                        .color(NamedTextColor.GRAY)
                        .append(
                                Component.text(TimeUtil.dateToString(date) + " at " + TimeUtil.dateToTime(date))
                                        .color(NamedTextColor.WHITE)
                        ),
                Component.text("Punishment ID: ")
                        .color(NamedTextColor.GRAY)
                        .append(
                                Component.text(this.punishment.getId())
                                        .color(NamedTextColor.WHITE)
                        ),
                Component.empty(),
                Component.text("Appeal At: ")
                        .color(NamedTextColor.GRAY)
                        .append(
                                Component.text("https://customwrld.com/discord")
                                        .color(NamedTextColor.AQUA)
                                        .decoration(TextDecoration.UNDERLINED, true)
                        ),
                Minecraft.Style.SEPARATOR
        );
    }

}

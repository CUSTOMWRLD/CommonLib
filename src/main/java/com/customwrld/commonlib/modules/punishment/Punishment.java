package com.customwrld.commonlib.modules.punishment;

import com.customwrld.commonlib.CommonLib;
import com.customwrld.commonlib.modules.rank.RankAPI;
import com.customwrld.commonlib.util.Minecraft;
import com.customwrld.commonlib.util.Util;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bson.Document;

import java.awt.*;
import java.util.UUID;

@Getter @Setter
public class Punishment {

    @Getter private static final MongoCollection<Document> collection = CommonLib.getCommonLib().getMongoDatabase().getCollection("punishments");

    @Setter(AccessLevel.NONE) private final String id;
    private UUID playerUUID;
    private Type type;
    private UUID addedByUUID;
    private long addedAt;
    private String addedReason;
    private long duration;
    private UUID removedByUUID;
    private Long removedAt;
    private String removedReason;
    private boolean removed;
    private final PunishmentAPI api;

    public Punishment(UUID playerUUID, Type type, UUID addedByUUID, long addedAt, String addedReason, long duration) {
        this.id = Util.generateId();
        this.playerUUID = playerUUID;
        this.type = type;
        this.addedByUUID = addedByUUID;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.duration = duration;
        this.api = new PunishmentAPI(this);
    }

    private Punishment(String id) {
        this.id = id;
        this.api = new PunishmentAPI(this);
    }

    public static Punishment getPunishment(String id) {
        Document document = collection.find(Filters.eq("_id", id)).first();

        if (document != null) {
            Punishment punishment = new Punishment(document.getString("_id"));

            punishment.setPlayerUUID(UUID.fromString(document.getString("playerUUID")));
            punishment.setType(Type.valueOf(document.getString("type")));

            punishment.setAddedByUUID(document.getString("addedByUUID") != null ? UUID.fromString(document.getString("addedByUUID")) : null);
            punishment.setAddedAt(document.getLong("addedAt"));
            punishment.setAddedReason(document.getString("addedReason"));
            punishment.setDuration(document.getLong("duration"));

            punishment.setRemoved(document.getBoolean("removed"));

            if (punishment.isRemoved()) {
                punishment.setRemovedAt(document.getLong("removedAt"));
                punishment.setRemovedByUUID(document.getString("removedByUUID") != null ? UUID.fromString(document.getString("removedByUUID")) : null);
                punishment.setRemovedReason(document.getString("removedReason"));
            }

            return punishment;
        }

        return null;
    }

    public void delete() {
        collection.deleteOne(Filters.eq("_id", this.id));
    }

    public void save() {
        Document document = new Document();
        document.put("_id", this.id);
        document.put("playerUUID", this.playerUUID.toString());
        document.put("type", this.type.name());

        document.put("addedByUUID", this.addedByUUID != null ? this.addedByUUID.toString() : null);
        document.put("addedAt", this.addedAt);
        document.put("addedReason", this.addedReason);
        document.put("duration", this.duration);

        document.put("removed", this.removed);
        document.put("removedAt", this.removedAt != null ? this.removedAt : null);
        document.put("removedByUUID", this.removedByUUID != null ? this.removedByUUID.toString() : null);
        document.put("removedReason", this.removedReason != null ? this.removedReason : null);

        collection.replaceOne(Filters.eq("_id", this.id), document, new ReplaceOptions().upsert(true));
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Punishment && ((Punishment) object).id.equals(this.id);
    }

    @Getter
    @AllArgsConstructor
    public enum Type {

        BLACKLIST("Blacklist", "blacklisted", "unblacklisted", true, true,
                new Data("Blacklists", new Color(NamedTextColor.DARK_RED.value()))),
        BAN("Ban", "banned", "unbanned", true, true,
                new Data("Bans", new Color(NamedTextColor.GOLD.value()))),
        MUTE("Mute", "muted", "unmuted", false, true,
                new Data("Mutes", new Color(NamedTextColor.GREEN.value()))),
        WARN("Warning", "warned", null, false, false,
                new Data("Warnings", new Color(NamedTextColor.BLUE.value())));

        private final String readable;
        private final String context;
        private final String undoContext;
        private final boolean ban;
        private final boolean removable;
        private final Data typeData;

        @Getter
        @AllArgsConstructor
        public static class Data {

            private final String readable;
            private final Color color;

        }

    }

}
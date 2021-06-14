package com.customwrld.commonlib.modules.grant;

import com.customwrld.commonlib.CommonLib;
import com.customwrld.commonlib.util.Util;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.UUID;

@Getter @Setter
public class Grant {

    @Getter private static final MongoCollection<Document> collection = CommonLib.getCommonLib().getMongoDatabase().getCollection("grants");

    final String id;
    private UUID playerUUID;
    private UUID rankUUID;
    private UUID addedByUUID;
    private long addedAt;
    private String addedReason;
    private long duration;
    private UUID removedByUUID;
    private Long removedAt;
    private String removedReason;
    private boolean removed;
    private final GrantAPI api;

    public Grant(UUID playerUUID, UUID rankUUID, UUID addedByUUID, long addedAt, String addedReason, long duration) {
        this.id = Util.generateId();
        this.playerUUID = playerUUID;
        this.rankUUID = rankUUID;
        this.addedByUUID = addedByUUID;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.duration = duration;
        this.api = new GrantAPI(this);
    }

    private Grant(String id) {
        this.id = id;
        this.api = new GrantAPI(this);
    }

    public static Grant getGrant(String id) {
        Document document = collection.find(Filters.eq("_id", id)).first();

        if (document != null) {
            Grant grant = new Grant(document.getString("_id"));

            grant.setPlayerUUID(UUID.fromString(document.getString("playerUUID")));
            grant.setRankUUID(UUID.fromString(document.getString("rankUUID")));

            grant.setAddedByUUID(document.getString("addedByUUID") != null ? UUID.fromString(document.getString("addedByUUID")) : null);
            grant.setAddedAt(document.getLong("addedAt"));
            grant.setAddedReason(document.getString("addedReason"));
            grant.setDuration(document.getLong("duration"));

            grant.setRemoved(document.getBoolean("removed"));

            if (grant.isRemoved()) {
                grant.setRemovedAt(document.getLong("removedAt"));
                grant.setRemovedByUUID(document.getString("removedByUUID") != null ? UUID.fromString(document.getString("removedByUUID")) : null);
                grant.setRemovedReason(document.getString("removedReason"));
            }

            return grant;
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
        document.put("rankUUID", this.rankUUID.toString());

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
        return object instanceof Grant && ((Grant) object).rankUUID.equals(this.rankUUID);
    }

}
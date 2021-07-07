package com.customwrld.commonlib.modules.profile;

import com.customwrld.commonlib.CommonLib;
import com.customwrld.commonlib.modules.grant.Grant;
import com.customwrld.commonlib.modules.network.Gamemode;
import com.customwrld.commonlib.modules.punishment.Punishment;
import com.customwrld.commonlib.util.CollectionsUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@Getter @Setter
public class Profile {

    @Getter private static final MongoCollection<Document> collection = CommonLib.getCommonLib().getMongoDatabase().getCollection("profiles");
    @Getter private static final Map<UUID, Profile> profiles = new HashMap<>();

    private final UUID id;
    private final List<String> permissions;
    private final List<String> punishments;
    private final List<Punishment> cachedPunishments;
    private final List<String> grants;
    private final List<Grant> cachedGrants;
    private final List<UUID> ignored;
    private final List<UUID> friends;
    private final Options optionsProfile;
    private final Staff staffProfile;
    private String name;
    private String searchableName;
    private String currentAddress;
    private String discordId;
    private int gold;
    private Long firstSeen;
    private Long lastSeen;
    private long experience;
    private Map<Gamemode, GamemodeStorage> gamemodeStorages;
    @Accessors(fluent = true)
    private final ProfileAPI api;

    private Profile(String username, UUID id) {
        this.id = id;
        this.name = username;
        this.permissions = new ArrayList<>();
        this.punishments = new ArrayList<>();
        this.cachedPunishments = new ArrayList<>();
        this.grants = new ArrayList<>();
        this.cachedGrants = new ArrayList<>();
        this.ignored = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.optionsProfile = new Options();
        this.staffProfile = new Staff();
        this.gamemodeStorages = new HashMap<>();
        this.api = new ProfileAPI(this);
        this.load();
    }

    public static Profile getProfile(@NotNull String username) {
        for(Profile profile : profiles.values()) {
            if(profile.getName().equalsIgnoreCase(username)) {
                return profile;
            }
        }

        Document document = collection.find(Filters.eq("searchableName", username.toUpperCase())).first();

        if(document != null) {
            return new Profile(document.getString("name"), UUID.fromString(document.getString("_id")));
        } else {
            return null;
        }
    }

    public static Profile getProfile(@Nullable String name, @NotNull UUID id) {
        Profile profile = ProfileAPI.getCachedProfile(id);

        if(profile != null) {
            return profile;
        }

        profile = new Profile(name, id);

        return profile;
    }

    public static Profile getProfile(@NotNull UUID id) {
        return getProfile(null, id);
    }

    public void load() {
        Document document = collection.find(Filters.eq("_id", this.getId().toString())).first();

        if (document != null) {
            if (this.name == null) {
                this.name = document.getString("name");
            }

            this.searchableName = this.name.toUpperCase();
            this.firstSeen = Long.parseLong(document.getString("firstSeen"));
            this.lastSeen = Long.parseLong(document.getString("lastSeen"));
            this.currentAddress = document.getString("currentAddress");

            Document optionsDocument = (Document) document.get("options");
            Options options = this.getOptionsProfile();

            options.receivingFriendRequests = optionsDocument.getBoolean("receivingFriendRequests");
            options.receivingPublicChat = optionsDocument.getBoolean("receivingPublicChat");
            options.receivingConversations = optionsDocument.getBoolean("receivingConversations");
            options.receivingMessageSounds = optionsDocument.getBoolean("receivingMessageSounds");

            Document staffDocument = (Document) document.get("staff");
            Staff staff = this.getStaffProfile();

            staff.twoFactorKey = staffDocument.getString("twoFactorKey");
            staff.operator = staffDocument.getBoolean("operator");
            staff.receivingStaffMessages = staffDocument.getBoolean("receivingStaffMessages");
            staff.twoFactor = staffDocument.getBoolean("twoFactor");
            staff.locked = staffDocument.getBoolean("locked");

            this.discordId = document.getString("discordId");

            this.gold = document.getInteger("gold");

            this.experience = document.getLong("experience");

            this.gamemodeStorages.clear();
            for(Map.Entry<String, Object> ent : document.get("gamemodeStorage", Document.class).entrySet()) {
                this.gamemodeStorages.put(Gamemode.getByName(ent.getKey()), new GamemodeStorage(ent.getKey(), (Document) ent.getValue()));
            }

            this.ignored.clear();
            for (String ignored : document.getList("ignored", String.class)) {
                UUID ignoredUUID = UUID.fromString(ignored);
                this.ignored.add(ignoredUUID);
            }

            this.friends.clear();
            for (String friends : document.getList("friends", String.class)) {
                UUID friendUUID = UUID.fromString(friends);
                this.friends.add(friendUUID);
            }

            this.permissions.clear();
            this.permissions.addAll(document.getList("permissions", String.class));

            this.punishments.clear();
            this.punishments.addAll(document.getList("punishments", String.class));

            this.cachedPunishments.clear();
            this.cachedPunishments.addAll(this.punishments.stream().map(Punishment::getPunishment).filter(Objects::nonNull).collect(Collectors.toList()));

            this.grants.clear();
            this.grants.addAll(document.getList("grants", String.class));

            this.cachedGrants.clear();
            this.cachedGrants.addAll(this.grants.stream().map(Grant::getGrant).filter(Objects::nonNull).collect(Collectors.toList()));
        }

        this.api.validatePunishments();
        this.api.getActiveGrant();
    }

    public void save() {
        Document document = new Document();
        document.put("_id", this.id.toString());
        document.put("name", this.name);
        document.put("searchableName", this.name.toUpperCase());
        document.put("firstSeen", this.firstSeen != null ? this.firstSeen.toString() : String.valueOf(System.currentTimeMillis()));
        document.put("lastSeen", this.lastSeen != null ? this.lastSeen.toString() : String.valueOf(System.currentTimeMillis()));
        document.put("currentAddress", this.currentAddress);

        Document optionsDocument = new Document();
        optionsDocument.put("receivingFriendRequests", this.optionsProfile.isReceivingFriendRequests());
        optionsDocument.put("receivingPublicChat", this.optionsProfile.isReceivingPublicChat());
        optionsDocument.put("receivingConversations", this.optionsProfile.isReceivingConversations());
        optionsDocument.put("receivingMessageSounds", this.optionsProfile.isReceivingMessageSounds());
        document.put("options", optionsDocument);

        Document staffDocument = new Document();
        staffDocument.put("twoFactorKey", this.staffProfile.getTwoFactorKey());
        staffDocument.put("operator", this.staffProfile.isOperator());
        staffDocument.put("receivingStaffMessages", this.staffProfile.isReceivingStaffMessages());
        staffDocument.put("twoFactor", this.staffProfile.isTwoFactor());
        staffDocument.put("locked", this.staffProfile.isLocked());
        document.put("staff", staffDocument);

        document.put("discord", this.discordId);

        document.put("gold", this.gold);

        var storagesDocument = new Document();
        for(Map.Entry<Gamemode, GamemodeStorage> ent : this.gamemodeStorages.entrySet()) {
            var storageDocument = new Document();
            for(Map.Entry<String, GamemodeStorage.StorageValue> valueEnt : ent.getValue().getValues().entrySet()) {
                storageDocument.append(valueEnt.getKey(),
                        new Document("value", valueEnt.getValue().getValue())
                                .append("isWritable", valueEnt.getValue().isWritable()));
            }
            storagesDocument.put(ent.getKey().getName(), storageDocument);
        }
        document.put("gamemodeStorage", storagesDocument);

        document.put("experience", this.experience);

        document.put("grants", this.grants);

        document.put("punishments", this.punishments);

        document.put("permissions", this.permissions);

        document.put("ignored", this.ignored);

        document.put("friends", this.friends);

        collection.replaceOne(Filters.eq("_id", this.id.toString()), document, new ReplaceOptions().upsert(true));
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Profile other && other.id.equals(this.id);
    }

    @Getter @Setter
    public static class Options {

        private boolean receivingPartyRequests = true;
        private boolean receivingFriendRequests = true;
        private boolean receivingPublicChat = true;
        private boolean receivingConversations = true;
        private boolean receivingMessageSounds = true;

    }

    @Getter @Setter
    public static class Staff {

        private String twoFactorKey = "";
        private boolean receivingStaffMessages = true;
        private boolean twoFactor;
        private boolean locked;
        private boolean operator;

    }

}
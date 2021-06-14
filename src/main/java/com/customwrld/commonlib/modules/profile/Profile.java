package com.customwrld.commonlib.modules.profile;

import com.customwrld.commonlib.CommonLib;
import com.customwrld.commonlib.modules.grant.Grant;
import com.customwrld.commonlib.modules.punishment.Punishment;
import com.customwrld.commonlib.util.CollectionsUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
    private String currentAddress;
    private String discordId;
    private Integer gold;
    private Long firstSeen;
    private Long lastSeen;
    private Long experience;
    private Map<String, Integer> gameModeGold;
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
        this.gameModeGold = new HashMap<>();
        this.api = new ProfileAPI(this);
    }

    public static Profile getProfile(@Nullable String name, UUID id) {
        Profile profile = ProfileAPI.getCachedProfile(id);

        if(profile != null) {
            return profile;
        }

        profile = new Profile(name, id);
        profile.load();

        return profile;
    }

    public void load() {
        Document document = collection.find(Filters.eq("_id", this.id.toString())).first();

        if (document != null) {
            if (this.name == null) {
                this.setName(document.getString("name"));
            }

            this.setFirstSeen(Long.parseLong(document.getString("firstSeen")));
            this.setLastSeen(Long.parseLong(document.getString("lastSeen")));
            this.setCurrentAddress(document.getString("currentAddress"));

            Document optionsDocument = (Document) document.get("options");
            Options options = this.getOptionsProfile();

            options.setReceivingFriendRequests(optionsDocument.getBoolean("receivingFriendRequests"));
            options.setReceivingPublicChat(optionsDocument.getBoolean("receivingPublicChat"));
            options.setReceivingConversations(optionsDocument.getBoolean("receivingConversations"));
            options.setReceivingMessageSounds(optionsDocument.getBoolean("receivingMessageSounds"));

            Document staffDocument = (Document) document.get("staff");
            Staff staff = this.getStaffProfile();

            staff.setTwoFactorKey(staffDocument.getString("twoFactorKey"));
            staff.setOperator(staffDocument.getBoolean("operator"));
            staff.setReceivingStaffMessages(staffDocument.getBoolean("receivingStaffMessages"));
            staff.setTwoFactor(staffDocument.getBoolean("twoFactor"));
            staff.setLocked(staffDocument.getBoolean("locked"));

            this.setDiscordId(document.getString("discordId"));

            this.setGold(document.getInteger("gold"));

            this.getGameModeGold().clear();
            this.setGameModeGold(CollectionsUtil.genericMapToType(document.get("gameModeGold", Map.class), String.class, Integer.class));

            this.setExperience(document.getLong("experience"));

            this.getIgnored().clear();
            for (String ignored : document.getList("ignored", String.class)) {
                UUID ignoredUUID = UUID.fromString(ignored);
                this.getIgnored().add(ignoredUUID);
            }

            this.getFriends().clear();
            for (String friends : document.getList("friends", String.class)) {
                UUID friendUUID = UUID.fromString(friends);
                this.getFriends().add(friendUUID);
            }

            this.getPermissions().clear();
            this.getPermissions().addAll(document.getList("permissions", String.class));
        }
    }

    public void save() {
        Document document = new Document();
        document.put("_id", this.id.toString());
        document.put("name", this.name);
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
        document.put("gameModeGold", this.gameModeGold);
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
        return object instanceof Profile && ((Profile) object).id.equals(this.id);
    }

    @Getter @Setter
    public static class Options {

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
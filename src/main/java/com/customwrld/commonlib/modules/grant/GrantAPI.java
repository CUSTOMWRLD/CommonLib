package com.customwrld.commonlib.modules.grant;

import com.customwrld.commonlib.modules.rank.Rank;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.UUID;

public class GrantAPI {

    private final Grant grant;

    public GrantAPI(Grant grant) {
        this.grant = grant;
    }

    public static Comparator<Grant> COMPARATOR = (grant1, grant2) -> {
        Rank rank1 = Rank.getRank(grant1.getRankUUID());
        Rank rank2 = Rank.getRank(grant2.getRankUUID());

        if(rank1 != null && rank2 != null) {
            return rank2.getWeight() - rank1.getWeight();
        }

        return 0;
    };

    /**
     * Returns if the Punishment is permanent or not.
     */
    public boolean isPermanent() {
        return this.grant.getDuration() == Integer.MAX_VALUE;
    }

    /**
     * Returns if the Punishment has expired or not.
     */
    public boolean hasExpired() {
        return (!isPermanent()) && (System.currentTimeMillis() >= this.grant.getAddedAt() + this.grant.getDuration());
    }

    /**
     * Removes a Grant.
     */
    public void remove(@Nullable UUID removedBy, Long removedAt, String removedReason) {
        this.grant.setRemovedByUUID(removedBy);
        this.grant.setRemovedAt(removedAt);
        this.grant.setRemovedReason(removedReason);
        this.grant.setRemoved(true);
        this.grant.save();
    }

    /**
     * Returns the Rank from the Grant.
     */
    public Rank getRank() {
        return Rank.getRank(this.grant.getRankUUID());
    }

}

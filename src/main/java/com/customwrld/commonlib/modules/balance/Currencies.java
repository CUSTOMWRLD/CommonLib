package com.customwrld.commonlib.modules.balance;

import com.customwrld.commonlib.modules.network.Gamemode;
import com.customwrld.commonlib.modules.profile.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class Currencies {

    public static final float CONVERSION_RATE = 0.2f;

    public static int convertGold(int gold) {
        return (int) Math.floor(gold * CONVERSION_RATE);
    }

    @AllArgsConstructor
    @Getter
    public enum CurrencyType {
        GOLD("gold", "G", "Gold"),
        GMGOLD("gmgold", "GG", "Gamemode Gold");

        private final String id;
        private final String suffix;
        private final String fullName;

        public boolean canAfford(Profile profile, @Nullable Gamemode gamemode, int amount) {
            return switch (this) {
                case GOLD -> Balance.canAffordWithGold(profile, amount);
                case GMGOLD -> Balance.canAffordWithGamemodeGold(profile, gamemode, amount);
            };
        }

        public void add(Profile profile, @Nullable Gamemode gamemode, int amount) {
            switch (this) {
                case GOLD -> Balance.addGold(profile, amount);
                case GMGOLD -> Balance.addGamemodeGold(profile, gamemode, amount);
            }
        }

        public void remove(Profile profile, @Nullable Gamemode gamemode, int amount) {
            switch (this) {
                case GOLD -> Balance.addGold(profile, -amount);
                case GMGOLD -> Balance.addGamemodeGold(profile, gamemode, -amount);
            }
        }
    }

}
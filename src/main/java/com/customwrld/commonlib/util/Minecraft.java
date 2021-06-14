package com.customwrld.commonlib.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang3.StringUtils;

public class Minecraft {

    public static class Style {
        public static TextComponent CONSOLE = Component.text("CONSOLE").color(NamedTextColor.DARK_RED);
        public static TextComponent SEPARATOR = Component.text(StringUtils.repeat(" ", 80)).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.STRIKETHROUGH, true);

        public static TextComponent fromLegacy(String string) {
            return LegacyComponentSerializer.legacy('&').deserialize(string);
        }

        public static TextComponent getMessageComponent(String prefix, TextComponent component) {
            return Component.text(prefix)
                    .color(TextColor.color(NamedTextColor.DARK_AQUA))
                    .append(
                            Component.text(" » ")
                                    .color(NamedTextColor.DARK_GRAY)
                                    .decoration(TextDecoration.BOLD, true)
                    )
                    .append(component);
        }
    }

}

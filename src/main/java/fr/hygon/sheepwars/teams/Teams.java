package fr.hygon.sheepwars.teams;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public enum Teams {
    ORANGE(Component.text("orange").color(TextColor.color(255, 120, 0))),
    PURPLE(Component.text("violet").color(TextColor.color(195, 35, 255))),
    NONE(Component.text("aucune").color(TextColor.color(255, 255, 255)));

    private final Component name;

    Teams(Component name) {
        this.name = name;
    }

    public Component getName() {
        return name;
    }
}

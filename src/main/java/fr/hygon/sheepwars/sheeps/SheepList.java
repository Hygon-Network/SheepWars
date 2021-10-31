package fr.hygon.sheepwars.sheeps;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public enum SheepList {
    ASSAULT(Component.text("Mouton d'abordage"), DyeColor.WHITE, AssaultSheep.class),
    SEARCHER(Component.text("Mouton chercheur").color(TextColor.color(0, 200, 15)), DyeColor.LIME, SearchingSheep.class),
    REGENERATION(Component.text("Mouton régénérateur").color(TextColor.color(245, 100, 200)), DyeColor.PINK, RegenSheep.class),
    EXPLOSIVE(Component.text("Mouton explosif").color(TextColor.color(220, 0, 25)), DyeColor.RED, ExplosiveSheep.class),
    INCENDIARY(Component.text("Mouton incendiaire").color(TextColor.color(255, 120, 0)), DyeColor.ORANGE, IncendiarySheep.class),
    ANTIGRAVITY(Component.text("Mouton antigravitaire").color(TextColor.color(20, 20, 20)), DyeColor.BLACK, AntiGravitySheep.class);

    private final Component name;
    private final DyeColor color;
    private final Class<? extends CustomSheep> customSheep;

    SheepList(Component name, DyeColor color, Class<? extends CustomSheep> customSheep) {
        this.name = name;
        this.color = color;
        this.customSheep = customSheep;
    }


    public Component getName() {
        return name;
    }

    public DyeColor getColor() {
        return color;
    }

    public CustomSheep getCustomSheep(Player launcher) {
        try {
            return customSheep.getConstructor(Player.class).newInstance(launcher);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't spawn a sheep of type " + customSheep.getName() + ".", exception);
            launcher.sendMessage(Component.text("Une erreur est survenue et nous n'avons pas pû faire apparaître ce mouton.").color(TextColor.color(220, 0, 0)));
        }

        return null;
    }
}

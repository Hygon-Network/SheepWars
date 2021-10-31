package fr.hygon.sheepwars.sheeps;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public enum SheepList {
    ASSAULT(Component.text("Mouton d'abordage"), Material.WHITE_WOOL, AssaultSheep.class),
    SEARCHER(Component.text("Mouton chercheur").color(TextColor.color(0, 200, 15)), Material.LIME_WOOL, SearchingSheep.class),
    REGENERATION(Component.text("Mouton régénérateur").color(TextColor.color(245, 100, 200)), Material.PINK_WOOL, RegenSheep.class),
    EXPLOSIVE(Component.text("Mouton explosif").color(TextColor.color(220, 0, 25)), Material.RED_WOOL, ExplosiveSheep.class),
    INCENDIARY(Component.text("Mouton incendiaire").color(TextColor.color(255, 120, 0)), Material.ORANGE_WOOL, IncendiarySheep.class),
    ANTIGRAVITY(Component.text("Mouton à antigravité").color(TextColor.color(20, 20, 20)), Material.BLACK_WOOL, AntiGravitySheep.class),
    ICING(Component.text("Mouton glaçant").color(TextColor.color(0, 200, 250)), Material.CYAN_WOOL, IcingSheep.class);

    private final Component name;
    private final Material wool;
    private final Class<? extends CustomSheep> customSheep;

    SheepList(Component name, Material wool, Class<? extends CustomSheep> customSheep) {
        this.name = name;
        this.wool = wool;
        this.customSheep = customSheep;
    }

    public Component getName() {
        return name;
    }

    public ItemStack getItemStack() {
        ItemStack sheepItem = new ItemStack(wool, 1);
        ItemMeta sheepItemMeta = sheepItem.getItemMeta();
        sheepItemMeta.displayName(name);
        sheepItem.setItemMeta(sheepItemMeta);

        return sheepItem;
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

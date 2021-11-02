package fr.hygon.sheepwars.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public enum ItemsList {
    TEAM_SELECTOR(Material.PAPER, Component.text("» ").color(TextColor.color(NamedTextColor.GRAY))
            .append(Component.text("Choisir son équipe").color(TextColor.color(60, 130, 230))
            .append(Component.text(" «").color(TextColor.color(NamedTextColor.GRAY)))).decoration(TextDecoration.ITALIC, false), "Équipes", () -> {
        ItemStack teamSelector = new ItemStack(Material.PAPER, 1);
        ItemMeta teamSelectorMeta = teamSelector.getItemMeta();

        teamSelectorMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
        teamSelectorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        teamSelectorMeta.displayName(Component.text("» ").color(TextColor.color(NamedTextColor.GRAY))
                .append(Component.text("Choisir son équipe").color(TextColor.color(60, 130, 255))
                .append(Component.text(" «").color(TextColor.color(NamedTextColor.GRAY)))).decoration(TextDecoration.ITALIC, false));
        teamSelector.setItemMeta(teamSelectorMeta);

        return teamSelector;
    }),
    ORANGE_TEAM(Material.ORANGE_CONCRETE, Component.text("Équipe orange").color(TextColor.color(255, 120, 0)).decoration(TextDecoration.ITALIC, false), "", () -> {
        ItemStack orangeTeam = new ItemStack(Material.ORANGE_CONCRETE, 1);
        ItemMeta orangeTeamMeta = orangeTeam.getItemMeta();

        orangeTeamMeta.displayName(Component.text("» ").color(TextColor.color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)
                .append(Component.text("Équipe orange").color(TextColor.color(255, 120, 0)).decoration(TextDecoration.ITALIC, false)));
        orangeTeam.setItemMeta(orangeTeamMeta);

        return orangeTeam;
    }),
    PURPLE_TEAM(Material.PURPLE_CONCRETE, Component.text("Équipe violette").color(TextColor.color(195, 35, 255)).decoration(TextDecoration.ITALIC, false), "", () -> {
        ItemStack purpleTeam = new ItemStack(Material.PURPLE_CONCRETE, 1);
        ItemMeta purpleTeamMeta = purpleTeam.getItemMeta();

        purpleTeamMeta.displayName(Component.text("» ").color(TextColor.color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)
                .append(Component.text("Équipe violette").color(TextColor.color(195, 35, 255)).decoration(TextDecoration.ITALIC, false)));
        purpleTeam.setItemMeta(purpleTeamMeta);

        return purpleTeam;
    }),
    RANDOM_TEAM(Material.PLAYER_HEAD, Component.text("Aléatoire").color(TextColor.color(150, 150, 150)).decoration(TextDecoration.ITALIC, false), "", () -> {
        ItemStack randomTeam = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta randomTeamMeta = (SkullMeta) randomTeam.getItemMeta();

        randomTeamMeta.displayName(Component.text("» ").color(TextColor.color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false)
                .append(Component.text("Aléatoire").color(TextColor.color(150, 150, 150)).decoration(TextDecoration.ITALIC, false)));
        randomTeamMeta.setBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQyMDY0YWE0ZGUzN" +
                "WY2NjliMDRlODdiMWUwNGQ0MmVjZWU4MjliOWJjNTY2MTM4YWEyNzk0Nzk4YWZiZWMifX19");
        randomTeam.setItemMeta(randomTeamMeta);

        return randomTeam;
    }),
    ORANGE_GLASS_PANE(Material.ORANGE_STAINED_GLASS_PANE, Component.text(""), "", () -> {
        ItemStack orangeGlassPane = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE, 1);
        ItemMeta orangeGlassPaneMeta = orangeGlassPane.getItemMeta();

        orangeGlassPaneMeta.displayName(Component.text(""));
        orangeGlassPane.setItemMeta(orangeGlassPaneMeta);

        return orangeGlassPane;
    }),
    PURPLE_GLASS_PANE(Material.PURPLE_STAINED_GLASS_PANE, Component.text(""), "", () -> {
        ItemStack purpleGlassPane = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1);
        ItemMeta purpleGlassPaneMeta = purpleGlassPane.getItemMeta();

        purpleGlassPaneMeta.displayName(Component.text(""));
        purpleGlassPane.setItemMeta(purpleGlassPaneMeta);

        return purpleGlassPane;
    });


    private final Material material;
    private final Component name;
    private final String inventoryName;
    private final ItemsCode itemsCode;

    ItemsList(Material material, Component name, String inventoryName, ItemsCode itemsCode) {
        this.material = material;
        this.name = name;
        this.inventoryName = inventoryName;
        this.itemsCode = itemsCode;
    }

    public Material getMaterial() {
        return material;
    }

    public Component getName() {
        return name;
    }

    public Component getInventoryName() {
        StringBuilder result = new StringBuilder();
        int spaces = (35 - ChatColor.stripColor(this.inventoryName).length()) / 2;

        result.append(" ".repeat(Math.max(0, spaces)));

        return Component.text(result.append(this.inventoryName).toString());
    }

    public ItemStack getPreparedItemStack() {
        return itemsCode.getItemStack();
    }

    private interface ItemsCode {
        ItemStack getItemStack();
    }
}

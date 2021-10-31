package fr.hygon.sheepwars.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum ItemsList {
    TEAM_SELECTOR(Material.PAPER, Component.text("Choisir son équipe"), "Équipes", () -> {
        ItemStack teamSelector = new ItemStack(Material.PAPER, 1);
        ItemMeta teamSelectorMeta = teamSelector.getItemMeta();

        teamSelectorMeta.displayName(Component.text("Choisir son équipe"));
        teamSelector.setItemMeta(teamSelectorMeta);

        return teamSelector;
    }),
    ORANGE_TEAM(Material.ORANGE_CONCRETE, Component.text("Équipe orange"), "", () -> {
        ItemStack orangeTeam = new ItemStack(Material.ORANGE_CONCRETE, 1);
        ItemMeta orangeTeamMeta = orangeTeam.getItemMeta();

        orangeTeamMeta.displayName(Component.text("Équipe orange"));
        orangeTeam.setItemMeta(orangeTeamMeta);

        return orangeTeam;
    }),
    PURPLE_TEAM(Material.PURPLE_CONCRETE, Component.text("Équipe violette"), "", () -> {
        ItemStack purpleTeam = new ItemStack(Material.PURPLE_CONCRETE, 1);
        ItemMeta purpleTeamMeta = purpleTeam.getItemMeta();

        purpleTeamMeta.displayName(Component.text("Équipe violette"));
        purpleTeam.setItemMeta(purpleTeamMeta);

        return purpleTeam;
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

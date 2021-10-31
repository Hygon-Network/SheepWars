package fr.hygon.sheepwars.events;

import fr.hygon.sheepwars.teams.TeamManager;
import fr.hygon.sheepwars.teams.Teams;
import fr.hygon.sheepwars.utils.ItemsList;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIManager implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack clickedItem = event.getItem();

        if(clickedItem == null || clickedItem.getItemMeta() == null || clickedItem.getItemMeta().displayName() == null) {
            return;
        }

        if(clickedItem.equals(ItemsList.TEAM_SELECTOR.getPreparedItemStack())) {
            Inventory GUI = Bukkit.createInventory(null, 9, ItemsList.TEAM_SELECTOR.getInventoryName());
            GUI.setItem(0, ItemsList.ORANGE_TEAM.getPreparedItemStack());
            GUI.setItem(4, ItemsList.ORANGE_TEAM.getPreparedItemStack()); //TODO RANDOM
            GUI.setItem(8, ItemsList.PURPLE_TEAM.getPreparedItemStack());

            player.openInventory(GUI);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if(clickedItem == null || clickedItem.getItemMeta() == null || clickedItem.getItemMeta().displayName() == null) {
            return;
        }

        if(clickedItem.equals(ItemsList.ORANGE_TEAM.getPreparedItemStack())) {
            TeamManager.setTeam(player, Teams.ORANGE);
            player.sendMessage(Component.text("Vous avez été mis dans l'équipe orange."));
            player.closeInventory();
        } else if(clickedItem.equals(ItemsList.PURPLE_TEAM.getPreparedItemStack())) {
            TeamManager.setTeam(player, Teams.PURPLE);
            player.sendMessage(Component.text("Vous avez été mis dans l'équipe violette."));
            player.closeInventory();
        }
    }
}

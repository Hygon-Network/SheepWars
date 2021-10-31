package fr.hygon.sheepwars.events;

import fr.hygon.sheepwars.game.GameManager;
import fr.hygon.sheepwars.game.MapSettings;
import fr.hygon.sheepwars.utils.ItemsList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinLeaveEvent implements Listener {
    @EventHandler
    public void preLogin(AsyncPlayerPreLoginEvent event) { // TODO if the game started or if there's to much players, kick the player

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.teleport(MapSettings.spawnLocation);

        //TODO welcome message
        player.getInventory().clear();
        player.getInventory().setItem(8, ItemsList.TEAM_SELECTOR.getPreparedItemStack());
    }
}

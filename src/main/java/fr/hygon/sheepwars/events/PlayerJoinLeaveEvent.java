package fr.hygon.sheepwars.events;

import fr.hygon.sheepwars.game.GameManager;
import fr.hygon.sheepwars.game.GameStatus;
import fr.hygon.sheepwars.game.MapSettings;
import fr.hygon.sheepwars.utils.ItemsList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveEvent implements Listener {
    @EventHandler
    public void preLogin(AsyncPlayerPreLoginEvent event) {
        if(GameManager.getGameStatus() == GameStatus.STARTED) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("La partie a déjà commencé.").color(TextColor.color(200, 0, 0)));
        } else if(Bukkit.getOnlinePlayers().size() == 8) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, Component.text("Le partie est pleine.").color(TextColor.color(200, 0, 0)));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.teleport(MapSettings.spawnLocation);

        //TODO welcome message
        player.getInventory().clear();
        player.getInventory().setItem(8, ItemsList.TEAM_SELECTOR.getPreparedItemStack());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // TODO leave message
        if(GameManager.getGameStatus() == GameStatus.STARTED) {
            GameManager.playerHasDied(player);
        }
    }
}

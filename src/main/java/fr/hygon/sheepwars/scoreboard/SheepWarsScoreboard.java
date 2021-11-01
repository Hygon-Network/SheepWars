package fr.hygon.sheepwars.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class SheepWarsScoreboard implements Listener {
    private static final HashMap<UUID, CustomScoreboard> playersScoreboard = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        updateScoreboard(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playersScoreboard.remove(player.getUniqueId());
    }

    public static void updateScoreboard(Player player) {
        if(!playersScoreboard.containsKey(player.getUniqueId())) {
            CustomScoreboard customScoreboard = new CustomScoreboard(player);
            playersScoreboard.put(player.getUniqueId(), customScoreboard);
        }

        playersScoreboard.get(player.getUniqueId()).updateScoreboard();
    }

    public static CustomScoreboard getScoreboard(Player player) {
        if(!playersScoreboard.containsKey(player.getUniqueId())) {
            CustomScoreboard customScoreboard = new CustomScoreboard(player);
            playersScoreboard.put(player.getUniqueId(), customScoreboard);
        }

        return playersScoreboard.get(player.getUniqueId());
    }
}

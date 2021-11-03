package fr.hygon.sheepwars.events;

import fr.hygon.sheepwars.game.GameManager;
import fr.hygon.sheepwars.scoreboard.SheepWarsScoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnDeath implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player deadPlayer = event.getEntity();
        GameManager.playerHasDied(deadPlayer);

        if(deadPlayer.getKiller() != null && deadPlayer != deadPlayer.getKiller()) {
            Player killer = deadPlayer.getKiller();
            event.deathMessage(Component.text("☠ ").color(TextColor.color(225, 25, 25))
                    .append(Component.text("» ").color(TextColor.color(NamedTextColor.GRAY))
                    .append(deadPlayer.displayName())
                    .append(Component.text(" a été tué par ").color(TextColor.color(255,255,75))
                    .append(killer.displayName())
                    .append(Component.text(".").color(TextColor.color(255, 255, 75))))));

            killer.sendActionBar(Component.text("+5 coins (Kill)").color(TextColor.color(255, 150, 25)));
            killer.addCoins(5);
            SheepWarsScoreboard.getScoreboard(killer).addKill();
            SheepWarsScoreboard.updateScoreboard(killer);
        } else {
            event.deathMessage(Component.text("☠ ").color(TextColor.color(225, 25, 25))
                                .append(Component.text("» ").color(TextColor.color(NamedTextColor.GRAY))
                                .append(deadPlayer.displayName())
                                .append(Component.text(" est mort.").color(TextColor.color(255, 255, 75)))));
        }
    }
}

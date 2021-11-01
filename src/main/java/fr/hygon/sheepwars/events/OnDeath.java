package fr.hygon.sheepwars.events;

import fr.hygon.sheepwars.game.GameManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
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
            event.deathMessage(deadPlayer.displayName().append(Component.text(" a été tué par ").append(killer.displayName()).append(Component.text("."))));

            killer.sendActionBar(Component.text("+5 coins (1 kill)").color(TextColor.color(255, 150, 25)));
            killer.addCoins(5);
        } else {
            event.deathMessage(deadPlayer.displayName().append(Component.text(" est mort.")));
        }
    }
}

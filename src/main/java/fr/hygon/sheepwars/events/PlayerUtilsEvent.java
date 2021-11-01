package fr.hygon.sheepwars.events;

import fr.hygon.sheepwars.game.GameManager;
import fr.hygon.sheepwars.game.GameStatus;
import fr.hygon.sheepwars.game.MapSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerUtilsEvent implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if(GameManager.getGameStatus() != GameStatus.STARTED) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(event.getTo().getY() < MapSettings.mapVoidY) {
            if(GameManager.getGameStatus() == GameStatus.STARTED) {
                GameManager.playerHasDied(event.getPlayer());
            } else {
                event.getPlayer().teleport(MapSettings.spawnLocation);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}

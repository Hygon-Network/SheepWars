package fr.hygon.sheepwars.events;

import fr.hygon.sheepwars.sheeps.CustomSheep;
import fr.hygon.sheepwars.sheeps.SearchingSheep;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftSheep;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.UUID;

public class SheepActions implements Listener {
    public static final ArrayList<UUID> assaultingPlayers = new ArrayList<>();

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if(assaultingPlayers.contains(player.getUniqueId())) {
                    event.setCancelled(true);
                    assaultingPlayers.remove(player.getUniqueId());
                }
            }
        } else if(event.getEntity() instanceof Sheep) {
            net.minecraft.world.entity.animal.Sheep nmsSheep = ((CraftSheep) event.getEntity()).getHandle();

            if(nmsSheep instanceof CustomSheep && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isOnGround(Player player) {
        /* is that a method ? */
        return !player.isFlying() && player.getLocation().subtract(0, 0.1, 0).getBlock().getType().isSolid();
    }
}

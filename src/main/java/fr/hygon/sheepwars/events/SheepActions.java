package fr.hygon.sheepwars.events;

import fr.hygon.sheepwars.game.GameManager;
import fr.hygon.sheepwars.scoreboard.SheepWarsScoreboard;
import fr.hygon.sheepwars.sheeps.CustomSheep;
import fr.hygon.sheepwars.sheeps.SheepList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftSheep;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Sheep && event.getEntity() instanceof Player damaged) {
            CustomSheep customSheep = (CustomSheep) ((CraftSheep) event.getDamager()).getHandle();
            Player damager = customSheep.getLauncher().getBukkitEntity();
            event.setCancelled(true);

            damaged.damage(event.getFinalDamage());

            if(damaged.getHealth() - event.getFinalDamage() <= 0) {
                GameManager.playerHasDied(damaged);
                Bukkit.broadcast(damaged.displayName().append(Component.text(" a été tué par ").append(damager.displayName()).append(Component.text("."))));

                damager.sendActionBar(Component.text("+5 coins (1 kill)").color(TextColor.color(255, 150, 25)));
                damager.addCoins(5);
                SheepWarsScoreboard.getScoreboard(damager).addKill();
                SheepWarsScoreboard.updateScoreboard(damager);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack clickedItem = event.getItem();

        if(clickedItem == null || clickedItem.getItemMeta() == null || clickedItem.getItemMeta().displayName() == null) {
            return;
        }

        for(SheepList sheep : SheepList.values()) {
            if(clickedItem.isSimilar(sheep.getItemStack())) {
                CustomSheep customSheep = sheep.getCustomSheep(player);
                if(customSheep == null) {
                    return;
                }

                World world = Bukkit.getWorld("world");
                if(world == null) {
                    Bukkit.getLogger().severe("The name of the map isn't \"world\".");
                    return;
                }

                ((CraftWorld) world).getHandle().addEntity(customSheep, CreatureSpawnEvent.SpawnReason.CUSTOM);
                clickedItem.setAmount(clickedItem.getAmount() - 1);
            }
        }
    }

    @EventHandler
    public void onEntityTurnToBlock(EntityDropItemEvent event) {
        if(event.getEntity() instanceof FallingBlock) {
            event.setCancelled(true);
        }
    }
}

package fr.hygon.sheepwars.goals;

import fr.hygon.sheepwars.game.MapSettings;
import fr.hygon.sheepwars.sheeps.CustomSheep;
import fr.hygon.sheepwars.teams.TeamManager;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;

import java.util.ArrayList;

public class AntiGravitySheepGoal extends Goal {
    private final CustomSheep sheep;
    private final ArrayList<Player> attractedPlayers = new ArrayList<>();

    public AntiGravitySheepGoal(CustomSheep sheep) {
        this.sheep = sheep;
    }

    @Override
    public boolean canUse() {
        attractedPlayers.clear();

        for(Entity entities : sheep.getBukkitMob().getNearbyEntities(MapSettings.antiGravitySheepAttractionDistance,
                MapSettings.antiGravitySheepAttractionDistance, MapSettings.antiGravitySheepAttractionDistance)) {
            if(entities instanceof org.bukkit.entity.Player &&
                    TeamManager.getTeam((org.bukkit.entity.Player) entities) != TeamManager.getTeam(sheep.getLauncher().getBukkitEntity())) {
                attractedPlayers.add(((CraftPlayer) entities).getHandle());
            }

        }
        return !attractedPlayers.isEmpty();
    }

    @Override
    public void tick() {
        for(Player players : attractedPlayers) {
            CraftPlayer bukkitPlayer = (CraftPlayer) players.getBukkitEntity();
            bukkitPlayer.setVelocity(sheep.getBukkitMob().getLocation().toVector().subtract(bukkitPlayer.getLocation().toVector()).normalize().multiply(0.2));
            if(sheep.tickCount % 5 == 0) {
                players.setHealth(players.getHealth() - 0.F);
            }
            bukkitPlayer.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, bukkitPlayer.getLocation(), 4, 1, 1, 1, 0, null, true);
        }
    }
}

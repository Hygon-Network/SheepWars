package fr.hygon.sheepwars.sheeps;

import fr.hygon.sheepwars.game.MapSettings;
import fr.hygon.sheepwars.teams.TeamManager;
import fr.hygon.sheepwars.teams.Teams;
import net.minecraft.world.item.DyeColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class IcingSheep extends CustomSheep {
    private Teams teamToFollow = null;

    public IcingSheep(Player player) {
        super(((CraftPlayer) player).getHandle());

        setColor(DyeColor.LIGHT_BLUE);

        setPos(getLauncher().getX(), getLauncher().getY() + 1, getLauncher().getZ());
        setRot(getLauncher().getYRot(), getLauncher().getXRot());

        getBukkitEntity().setVelocity(getLauncher().getBukkitEntity().getLocation().getDirection().multiply(MapSettings.sheepVelocity));
    }

    @Override
    protected void registerGoals() {

    }

    @Override
    public void tick() {
        super.tick();

        getBukkitEntity().getWorld().spawnParticle(Particle.CLOUD, getX(), getY(), getZ(), 4, 1, 1, 1, 0, null, true);
        findPlayersInRadius(MapSettings.icingSheepRadiusEffect).forEach(player -> {
            player.setFreezeTicks(player.getMaxFreezeTicks());
            if(tickCount % 40 == 0) {
                player.damage(2, getBukkitEntity());
                player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 2.0F, 0.1F);
                player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 30, 0.2, 0.4, 0.2, 0.3, null, true);
            }
        });
    }

    private ArrayList<Player> findPlayersInRadius(int radius) {
        if(teamToFollow == null) {
            if(TeamManager.getTeam(getLauncher().getBukkitEntity()) == Teams.PURPLE) {
                teamToFollow = Teams.ORANGE;
            } else {
                teamToFollow = Teams.PURPLE;
            }
        }

        Entity bukkitSheep = getBukkitEntity();
        ArrayList<Player> players = new ArrayList<>();

        for(Entity entities : bukkitSheep.getNearbyEntities(radius, radius, radius)) {
            if(entities instanceof Player && TeamManager.getTeam((Player) entities) == teamToFollow) {
                players.add(((Player) entities));
            }
        }

        return players;
    }
}

package fr.hygon.sheepwars.goals;

import fr.hygon.sheepwars.sheeps.CustomSheep;
import fr.hygon.sheepwars.teams.TeamManager;
import fr.hygon.sheepwars.teams.Teams;
import net.minecraft.world.entity.ai.goal.Goal;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class FollowNearestEnemyGoal extends Goal {
    private final CustomSheep sheep;
    private Teams teamToFollow;
    private final double stopPoint;
    private final double speed;
    private net.minecraft.world.entity.player.Player nearestSelectedPlayer = null;

    public FollowNearestEnemyGoal(CustomSheep sheep, double stopPoint, double speed) {
        this.sheep = sheep;
        this.stopPoint = stopPoint;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        net.minecraft.world.entity.player.Player nearestPlayer = findNearestPlayer();
        if(nearestPlayer == null) {
            return false;
        } else if(nearestPlayer.getBukkitEntity().getLocation().distance(sheep.getBukkitMob().getLocation()) >= stopPoint) {
            nearestSelectedPlayer = nearestPlayer;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        this.sheep.getNavigation().moveTo(nearestSelectedPlayer, speed);
    }

    private net.minecraft.world.entity.player.Player findNearestPlayer() {
        if(teamToFollow == null) {
            Player bukkitRider = sheep.getLauncher().getBukkitEntity();

            if(TeamManager.getTeam(bukkitRider) == Teams.PURPLE) {
                teamToFollow = Teams.ORANGE;
            } else {
                teamToFollow = Teams.PURPLE;
            }
            return null;
        }

        double distance = Integer.MAX_VALUE;
        Player closestPlayer = null;

        for(Entity entities : sheep.getBukkitMob().getNearbyEntities(100, 100, 100)) {
            if(entities instanceof Player && TeamManager.getTeam((Player) entities) == teamToFollow) {
                if(closestPlayer == null) {
                    closestPlayer = (Player) entities;
                    distance = closestPlayer.getLocation().distance(sheep.getBukkitMob().getLocation());
                } else {
                    double entityDistance = entities.getLocation().distance(sheep.getBukkitMob().getLocation());
                    if(entityDistance < distance) {
                        distance = entityDistance;
                        closestPlayer = (Player) entities;
                    }
                }
            }
        }

        return closestPlayer == null ? null : ((CraftPlayer) closestPlayer).getHandle();
    }
}

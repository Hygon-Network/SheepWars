package fr.hygon.sheepwars.goals;

import fr.hygon.sheepwars.game.MapSettings;
import fr.hygon.sheepwars.sheeps.CustomSheep;
import fr.hygon.sheepwars.teams.TeamManager;
import fr.hygon.sheepwars.teams.Teams;
import fr.hygon.sheepwars.utils.CustomExplosion;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.DyeColor;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ExplodeWhenNearPlayerGoal extends Goal {
    private final CustomSheep sheep;
    private Teams teamToFollow;
    private final double stopPoint;

    int explodeTick = 0;
    int colorTick = 0;

    public ExplodeWhenNearPlayerGoal(CustomSheep sheep, double stopPoint) {
        this.sheep = sheep;
        this.stopPoint = stopPoint;
    }

    @Override
    public boolean canUse() {
        net.minecraft.world.entity.player.Player nearestPlayer = findNearestPlayer();
        if(nearestPlayer == null) {
            return false;
        } else return nearestPlayer.getBukkitEntity().getLocation().distance(sheep.getBukkitMob().getLocation()) <= stopPoint;
    }

    @Override
    public void tick() {
        explodeTick++;
        colorTick++;

        if(colorTick <= 3) {
            sheep.setColor(DyeColor.WHITE);
        } else {
            sheep.setColor(DyeColor.LIME);
            if(colorTick == 6) {
                colorTick = 0;
            }
        }

        if(explodeTick == 40) {
            CustomExplosion explosion = new CustomExplosion(sheep.getBukkitMob().getLocation(), MapSettings.searchingSheepExplosionPower, 10);
            explosion.explode();
            sheep.discard();
        }
    }

    @org.jetbrains.annotations.Nullable
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

        for(Entity entities : sheep.getBukkitMob().getNearbyEntities(stopPoint, stopPoint, stopPoint)) {
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

    public void stop() {
        explodeTick = 0;
        sheep.setColor(DyeColor.LIME);
    }
}

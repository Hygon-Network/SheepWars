package fr.hygon.sheepwars.sheeps;

import fr.hygon.sheepwars.game.MapSettings;
import fr.hygon.sheepwars.teams.TeamManager;
import net.minecraft.world.item.DyeColor;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class RegenSheep extends CustomSheep {
    private int ticksOnGround = 0;

    public RegenSheep(Player player) {
        super(((CraftPlayer) player).getHandle());

        setColor(DyeColor.PINK);

        setPos(getLauncher().getX(), getLauncher().getY() + 1, getLauncher().getZ());
        setRot(getLauncher().getYRot(), getLauncher().getXRot());

        getBukkitEntity().setVelocity(getLauncher().getBukkitEntity().getLocation().getDirection().multiply(MapSettings.sheepVelocity));
    }

    @Override
    public void tick() {
        if(isOnGround()) {
            ticksOnGround++;
        }

        for(Entity entities : getBukkitMob().getNearbyEntities(MapSettings.regenSheepHealDistance, MapSettings.regenSheepHealDistance, MapSettings.regenSheepHealDistance)) {
            if(entities instanceof Player) {
                if(TeamManager.getTeam(getLauncher().getBukkitEntity()) == TeamManager.getTeam((Player) entities)) {
                    ((Player) entities).setHealthSafely(((Player) entities).getHealth() + 0.05);
                }
            }
        }

        super.tick();

        if(ticksOnGround == 6 * 20) {
            discard();
        }
    }
}

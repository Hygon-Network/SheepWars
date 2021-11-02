package fr.hygon.sheepwars.sheeps;

import fr.hygon.sheepwars.game.MapSettings;
import fr.hygon.sheepwars.teams.TeamManager;
import net.minecraft.world.item.DyeColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
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

            if (ticksOnGround % 20 == 0) {
                getBukkitEntity().getWorld().spawnParticle(Particle.HEART, getX(), getY() + 1.2, getZ(), 10, 0.5, 0.3, 0.5, 1, null, true);
            }

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
            getBukkitEntity().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, getX(), getY(), getZ(), 1, 0, 0, 0, 1, null, true);
            getBukkitEntity().getWorld().playSound(getBukkitEntity().getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0F, 2F);
        }
    }
}

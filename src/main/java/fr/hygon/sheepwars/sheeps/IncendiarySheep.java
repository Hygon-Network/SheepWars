package fr.hygon.sheepwars.sheeps;

import fr.hygon.sheepwars.game.MapSettings;
import net.minecraft.world.item.DyeColor;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.concurrent.ThreadLocalRandom;

public class IncendiarySheep extends CustomSheep {
    public IncendiarySheep(Player player) {
        super(((CraftPlayer) player).getHandle());

        setColor(DyeColor.ORANGE);

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

        if (ticksOnGround == 80) {
            burnGround();
            discard();

            getBukkitEntity().getWorld().spawnParticle(Particle.LAVA, getX(), getY(), getZ(), 10, 2, 2, 2, 0.1, null, true);
            getBukkitEntity().getWorld().spawnParticle(Particle.FLAME, getX(), getY(), getZ(), 20, 0.2, 0.2, 0.2, 1, null, true);
            getBukkitEntity().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, getX(), getY(), getZ(), 50, 0.2, 0.2, 0.2, 1, null, true);
            getBukkitEntity().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, getX(), getY(), getZ(), 1, 0.2, 0.2, 0.2, 1, null, true);

            getBukkitEntity().getWorld().playSound(getBukkitEntity().getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0F, 1.0F);

        }

        if (ticksOnGround % 3 == 0) {
            setColor(getColor() == DyeColor.ORANGE ? DyeColor.WHITE : DyeColor.ORANGE);
            getBukkitEntity().getWorld().playSound(getBukkitEntity().getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1.0F, 0.7F);
        }

        getBukkitEntity().getWorld().spawnParticle(Particle.LAVA, getX(), getY(), getZ(), 1, 1, 1, 1, 0.1, null, true);
        getBukkitEntity().getWorld().spawnParticle(Particle.FLAME, getX(), getY(), getZ(), 2, 0.2, 0.2, 0.2, 0.1, null, true);
    }

    private void burnGround() {
        World world = level.getWorld();
        int x = getBukkitMob().getLocation().getBlockX();
        int y = getBukkitMob().getLocation().getBlockY() - 1;
        int z = getBukkitMob().getLocation().getBlockZ();

        int radius = MapSettings.incendiarySheepRadius;
        int rSquared = radius * radius;

        for (int xLoc = x - radius; xLoc <= x + radius; xLoc++) {
            for (int zLoc = z - radius; zLoc <= z + radius; zLoc++) {
                if ((x - xLoc) * (x - xLoc) + (z - zLoc) * (z - zLoc) <= rSquared) {
                    if (ThreadLocalRandom.current().nextInt(0, 100) <= MapSettings.incendiarySheepPower) {
                        int yLoc = y - 2;
                        while (yLoc < y + 5) {
                            if (world.getBlockAt(xLoc, yLoc, zLoc).getType() != Material.AIR) {
                                if (world.getBlockAt(xLoc, yLoc + 1, zLoc).getType() == Material.AIR &&
                                        world.getBlockAt(xLoc, yLoc + 2, zLoc).getType() == Material.AIR) { // We check if there's at least 2 blocks of air on top
                                    world.getBlockAt(xLoc, yLoc + 1, zLoc).setType(Material.FIRE);
                                    break;
                                }
                            }
                            yLoc++;
                        }
                    }
                }
            }
        }
    }
}

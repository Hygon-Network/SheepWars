package fr.hygon.sheepwars.utils;

import net.minecraft.world.entity.item.FallingBlockEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public record CustomExplosion(Player damager, Location centerBlock, int power, double damage) {

    public void explode() {
        List<int[]> circleBlocks = new ArrayList<>();

        int X = centerBlock.getBlockX();
        int Y = centerBlock.getBlockY();
        int Z = centerBlock.getBlockZ();
        int radiusSquared = power * power;

        for (int x = X - power; x <= X + power; x++) {
            for (int y = Y - power; y <= Y + power; y++) {
                for (int z = Z - power; z <= Z + power; z++) {
                    if ((X - x) * (X - x) + (Y - y) * (Y - y) + (Z - z) * (Z - z) <= radiusSquared) {
                        int[] block = new int[3];
                        block[0] = x;
                        block[1] = y;
                        block[2] = z;
                        circleBlocks.add(block);
                    }
                }
            }
        }

        World world = Bukkit.getWorld("world");
        if(world == null) {
            Bukkit.getLogger().severe("The name of the map isn't \"world\".");
            return;
        }

        for (int[] location : circleBlocks) {
            Block block = world.getBlockAt(location[0], location[1], location[2]);

            Material blockMaterial = block.getType();
            block.setType(Material.AIR);

            if (blockMaterial != Material.AIR && ThreadLocalRandom.current().nextInt(0, 15) == 0) {
                FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(((CraftWorld) block.getWorld()).getHandle(), location[0], location[1], location[2], CraftMagicNumbers.getBlock(blockMaterial).defaultBlockState());
                fallingBlockEntity.time = 1;

                ((CraftWorld) block.getWorld()).getHandle().addEntity(fallingBlockEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);

                fallingBlockEntity.getBukkitEntity().setVelocity(getRandomVector());
            }
        }

        for (Entity entities : world.getNearbyEntities(centerBlock, power, power, power)) {
            if (entities instanceof Player) {
                double healthDamagePercent = 100 - (entities.getLocation().distance(centerBlock) / power) * 100;
                double damageToDeal = (damage / 100) * healthDamagePercent;

                ((Player) entities).damage(damageToDeal, damager);
            }
        }
    }

    private static Vector getRandomVector() {
        return new Vector(ThreadLocalRandom.current().nextDouble(-1, 1), ThreadLocalRandom.current().nextDouble(0.3, 1), ThreadLocalRandom.current().nextDouble(-1, 1));
    }
}

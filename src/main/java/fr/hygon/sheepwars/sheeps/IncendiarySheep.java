package fr.hygon.sheepwars.sheeps;

import fr.hygon.sheepwars.game.MapSettings;
import net.minecraft.world.item.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.concurrent.ThreadLocalRandom;

public class IncendiarySheep extends CustomSheep {
    private int activeTicks = 0;

    public IncendiarySheep(Player player) {
        super(((CraftPlayer) player).getHandle());

        setColor(DyeColor.GREEN);

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

        if(isOnGround()) {
            activeTicks++;
        }

        if(activeTicks == 80) {
            burnGround();
            discard();
        }

        setColor(tickCount % 2 == 0 ? DyeColor.WHITE : DyeColor.ORANGE);
    }

    private void burnGround() {
        World world = level.getWorld();
        int x = getBukkitMob().getLocation().getBlockX();
        int y = getBukkitMob().getLocation().getBlockY() - 1;
        int z = getBukkitMob().getLocation().getBlockZ();

        int radius = MapSettings.incendiarySheepRadius;
        int rSquared = radius * radius;

        for (int xLoc = x - radius; xLoc <= x +radius; xLoc++) {
            for (int zLoc = z - radius; zLoc <= z +radius; zLoc++) {
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

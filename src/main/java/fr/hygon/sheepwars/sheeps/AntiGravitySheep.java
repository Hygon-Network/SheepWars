package fr.hygon.sheepwars.sheeps;

import fr.hygon.sheepwars.game.MapSettings;
import fr.hygon.sheepwars.goals.AntiGravitySheepGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.item.DyeColor;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class AntiGravitySheep extends CustomSheep {
    public AntiGravitySheep(Player player) {
        super(((CraftPlayer) player).getHandle());

        setColor(DyeColor.BLACK);

        setPos(getLauncher().getX(), getLauncher().getY() + 1, getLauncher().getZ());
        setRot(getLauncher().getYRot(), getLauncher().getXRot());

        getBukkitEntity().setVelocity(getLauncher().getBukkitEntity().getLocation().getDirection().multiply(MapSettings.sheepVelocity));
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new AntiGravitySheepGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
    }

    @Override
    public void tick() {
        for(int i = 0; i < 4; i ++) {
            double randomX = getX() + ThreadLocalRandom.current().nextDouble(-2, 2);
            double randomY = getY() + ThreadLocalRandom.current().nextDouble(-2, 2);
            double randomZ = getZ() + ThreadLocalRandom.current().nextDouble(-2, 2);

            level.getWorld().spawnParticle(Particle.SMOKE_LARGE, randomX, randomY, randomZ, 0, 3, 3, 3, 0, null, true);
        }
        level.getWorld().spawnParticle(Particle.LAVA, getX(), getY(), getZ(), 4, 1, 1, 1, 0, null, true);
        super.tick();
    }
}

package fr.hygon.sheepwars.sheeps;

import fr.hygon.sheepwars.game.MapSettings;
import fr.hygon.sheepwars.utils.CustomExplosion;
import net.minecraft.world.item.DyeColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ExplosiveSheep extends CustomSheep {
    private int ticksOnGround = 0;

    public ExplosiveSheep(Player player) {
        super(((CraftPlayer) player).getHandle());

        setColor(DyeColor.RED);

        setPos(getLauncher().getX(), getLauncher().getY() + 1, getLauncher().getZ());
        setRot(getLauncher().getYRot(), getLauncher().getXRot());

        getBukkitEntity().setVelocity(getLauncher().getBukkitEntity().getLocation().getDirection().multiply(MapSettings.sheepVelocity));
    }

    @Override
    public void tick() {
        if(isOnGround()) {
            ticksOnGround++;
        }

        if(ticksOnGround >= 20 && ticksOnGround < 60) {

            if (ticksOnGround % 5 == 0) {
                setColor(getColor() == DyeColor.RED ? DyeColor.WHITE : DyeColor.RED);
                getBukkitEntity().getWorld().playSound(getBukkitEntity().getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0F, 0.5F);
            }
        } else if (ticksOnGround >= 60 && ticksOnGround <= 100) {
            if (ticksOnGround % 3 == 0) {
                setColor(getColor() == DyeColor.RED ? DyeColor.WHITE : DyeColor.RED);
                getBukkitEntity().getWorld().playSound(getBukkitEntity().getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0F, 0.7F);
            }
        } else if (ticksOnGround > 100) {
        if (ticksOnGround % 2 == 0) {
            setColor(getColor() == DyeColor.RED ? DyeColor.WHITE : DyeColor.RED);
            getBukkitEntity().getWorld().playSound(getBukkitEntity().getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0F, 1F);
        }
    }

        int ticksBeforeExplosion = 6 * 20;
        if(ticksOnGround == ticksBeforeExplosion) {
            CustomExplosion customExplosion = new CustomExplosion(this, getBukkitEntity().getLocation(), MapSettings.explosiveSheepPower, MapSettings.explosiveSheepDamage);
            discard();
            customExplosion.explode();
            getBukkitEntity().getWorld().playSound(getBukkitEntity().getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2.0F, 0F);
            getBukkitEntity().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, getX(), getY(), getZ(), 50, 0.2, 0.2, 0.2, 1, null, true);
            getBukkitEntity().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, getX(), getY(), getZ(), 10, 2, 2, 2, 1, null, true);
        }
        super.tick();
    }
}

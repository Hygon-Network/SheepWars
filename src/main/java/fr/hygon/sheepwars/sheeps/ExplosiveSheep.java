package fr.hygon.sheepwars.sheeps;

import fr.hygon.sheepwars.game.MapSettings;
import fr.hygon.sheepwars.utils.CustomExplosion;
import net.minecraft.world.item.DyeColor;
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

        if(ticksOnGround % 2 == 0) {
            setColor(getColor() == DyeColor.RED ? DyeColor.WHITE : DyeColor.RED);
        }

        int ticksBeforeExplosion = 6 * 20;
        if(ticksOnGround == ticksBeforeExplosion) {
            CustomExplosion customExplosion = new CustomExplosion(getLauncher().getBukkitEntity(), getBukkitEntity().getLocation(), MapSettings.explosiveSheepPower, MapSettings.explosiveSheepDamage);
            discard();
            customExplosion.explode();
        }
        super.tick();
    }
}

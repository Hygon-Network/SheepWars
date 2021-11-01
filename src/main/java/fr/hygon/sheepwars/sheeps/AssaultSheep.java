package fr.hygon.sheepwars.sheeps;

import fr.hygon.sheepwars.Main;
import fr.hygon.sheepwars.events.SheepActions;
import fr.hygon.sheepwars.game.MapSettings;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AssaultSheep extends CustomSheep {

    public AssaultSheep(Player player) {
        super(((CraftPlayer) player).getHandle());

        setInvulnerable(true);

        setPos(getLauncher().getX(), getLauncher().getY() + 1, getLauncher().getZ());
        setRot(getLauncher().getYRot(), getLauncher().getXRot());

        getLauncher().startRiding(this, true);

        getBukkitEntity().setVelocity(getLauncher().getBukkitEntity().getLocation().getDirection().multiply(MapSettings.sheepVelocity));

        SheepActions.assaultingPlayers.add(getLauncher().getUUID());
    }

    @Override
    public void registerGoals() {

    }

    @Override
    public void tick() {
        super.tick();

        setRot(getLauncher().getYRot(), getLauncher().getXRot());
        if(isOnGround()) {
            discard();
        } else if(getPassengers().isEmpty()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(getLauncher().fallDistance == 0) {
                        SheepActions.assaultingPlayers.remove(getLauncher().getUUID());
                        cancel();
                    }
                }
            }.runTaskTimer(Main.getPlugin(), 20, 1);
            discard();
        }
    }
}
package fr.hygon.sheepwars.sheeps;

import fr.hygon.sheepwars.events.SheepActions;
import fr.hygon.sheepwars.game.MapSettings;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class AssaultSheep extends CustomSheep {

    public AssaultSheep(Player player) {
        super(((CraftPlayer) player).getHandle());

        setInvulnerable(true);

        setPos(getLauncher().getX(), getLauncher().getY() + 1, getLauncher().getZ());
        setRot(getLauncher().getYRot(), getLauncher().getXRot());

        getLauncher().startRiding(this, true);

        getBukkitEntity().setVelocity(getLauncher().getBukkitEntity().getLocation().getDirection().multiply(MapSettings.sheepVelocity));
    }

    @Override
    public void registerGoals() {

    }

    @Override
    public void tick() {
        super.tick();

        setRot(getLauncher().getYRot(), getLauncher().getXRot());
        if(isOnGround()) { //If the sheep was able to touch the ground, it means that the player stayed on it
            SheepActions.assaultingPlayers.remove(getLauncher().getUUID());
            discard();
        } else if(getPassengers().isEmpty()) {
            SheepActions.assaultingPlayers.remove(getLauncher().getUUID());
            discard();
        }
    }
}
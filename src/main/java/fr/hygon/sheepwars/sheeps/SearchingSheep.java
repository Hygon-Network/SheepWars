package fr.hygon.sheepwars.sheeps;

import fr.hygon.sheepwars.game.MapSettings;
import fr.hygon.sheepwars.goals.ExplodeWhenNearPlayerGoal;
import fr.hygon.sheepwars.goals.FollowNearestEnemyGoal;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.item.DyeColor;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SearchingSheep extends CustomSheep {
    public final ServerPlayer rider;

    public SearchingSheep(Player player) {
        super(((CraftPlayer) player).getHandle());
        rider = ((CraftPlayer) player).getHandle();
        setColor(DyeColor.LIME);

        setPos(rider.getX(), rider.getY() + 1, rider.getZ());
        getBukkitEntity().setVelocity(rider.getBukkitEntity().getLocation().getDirection().multiply(MapSettings.sheepVelocity));
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new FollowNearestEnemyGoal(this, 4, MapSettings.searchingSheepSpeed));
        this.goalSelector.addGoal(1, new ExplodeWhenNearPlayerGoal(this, 6));
        this.goalSelector.addGoal(2, new FloatGoal(this));
    }
}

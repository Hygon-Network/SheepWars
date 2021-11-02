package fr.hygon.sheepwars.sheeps;

import fr.hygon.sheepwars.game.MapSettings;
import fr.hygon.sheepwars.scoreboard.SheepWarsScoreboard;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.world.item.DyeColor;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GlowingSheep extends CustomSheep {
    private final ArrayList<Player> affectedPlayers = new ArrayList<>();

    public GlowingSheep(Player player) {
        super(((CraftPlayer) player).getHandle());

        setColor(DyeColor.YELLOW);

        setPos(getLauncher().getX(), getLauncher().getY() + 1, getLauncher().getZ());
        setRot(getLauncher().getYRot(), getLauncher().getXRot());

        getBukkitEntity().setVelocity(getLauncher().getBukkitEntity().getLocation().getDirection().multiply(MapSettings.sheepVelocity));

        setGlowingTag(true);

        Bukkit.getOnlinePlayers().forEach(players -> {
            SheepWarsScoreboard.getScoreboard(players).registerSheepColor(this, NamedTextColor.YELLOW);
            SheepWarsScoreboard.getScoreboard(players).updateScoreboard();
        });
    }

    @Override
    public void registerGoals() {

    }

    @Override
    public void tick() {
        super.tick();

        if(!isDeadOrDying()) {
            for (Entity entities : getBukkitEntity().getNearbyEntities(MapSettings.glowingSheepRadius, MapSettings.glowingSheepRadius, MapSettings.glowingSheepRadius)) {
                if (entities instanceof Player) {
                    affectedPlayers.add((Player) entities);
                    entities.setGlowing(true);
                }
            }
        }

        for(Player players : new ArrayList<>(affectedPlayers)) {
            if(players.getLocation().distance(getBukkitEntity().getLocation()) > MapSettings.glowingSheepRadius) {
                players.setGlowing(false);
                affectedPlayers.remove(players);
            }
        }

        if(ticksOnGround == 20 * MapSettings.glowingSheepDuration) {
            discard();
            removeAllGlowingEntities();
        }
    }

    public void removeAllGlowingEntities() {
        affectedPlayers.forEach(players -> {
            players.setGlowing(false);
        });

        affectedPlayers.clear();
    }
}

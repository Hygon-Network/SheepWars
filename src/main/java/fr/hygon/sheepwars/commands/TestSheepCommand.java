package fr.hygon.sheepwars.commands;

import fr.hygon.sheepwars.sheeps.*;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import java.util.Locale;

public class TestSheepCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("§cVous devez être un joueur pour exécuter cette commande.");
            return true;
        }

        if(args.length < 1) {
            player.sendMessage("§cVous devez spécifier un mouton. Liste: ");
            player.sendMessage("§fAbordage");
            player.sendMessage("§aChercheur");
            player.sendMessage("§0antigravité");
            player.sendMessage("§dRegen");
            player.sendMessage("§cExplosive");
            player.sendMessage("§6Incendiaire");
            return true;
        }

        ServerLevel world = ((CraftWorld) player.getWorld()).getHandle();

        switch (args[0].toLowerCase(Locale.ROOT)) {
            default -> player.sendMessage("§cMouton non reconnu.");
            case "abordage" -> {
                player.sendMessage("§aAbordage");
                AssaultSheep assaultSheep = new AssaultSheep(player);
                world.addEntity(assaultSheep, CreatureSpawnEvent.SpawnReason.CUSTOM);
            }
            case "chercheur" -> {
                player.sendMessage("§aChercheur");
                SearchingSheep searchingSheep = new SearchingSheep(player);
                world.addEntity(searchingSheep, CreatureSpawnEvent.SpawnReason.CUSTOM);
            }
            case "antigravité" -> {
                player.sendMessage("§0antigravité");
                AntiGravitySheep antiGravitySheep = new AntiGravitySheep(player);
                world.addEntity(antiGravitySheep, CreatureSpawnEvent.SpawnReason.CUSTOM);
            }
            case "regen" -> {
                player.sendMessage("§dRegen");
                RegenSheep regenSheep = new RegenSheep(player);
                world.addEntity(regenSheep, CreatureSpawnEvent.SpawnReason.CUSTOM);
            }
            case "explosive" -> {
                player.sendMessage("§cExplosive");
                ExplosiveSheep explosiveSheep = new ExplosiveSheep(player);
                world.addEntity(explosiveSheep, CreatureSpawnEvent.SpawnReason.CUSTOM);
            }
            case "incendiaire" -> {
                player.sendMessage("§6Incendiaire");
                IncendiarySheep incendiarySheep = new IncendiarySheep(player);
                world.addEntity(incendiarySheep, CreatureSpawnEvent.SpawnReason.CUSTOM);
            }
        }
        return false;
    }
}

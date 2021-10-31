package fr.hygon.sheepwars.commands;

import fr.hygon.sheepwars.teams.TeamManager;
import fr.hygon.sheepwars.teams.Teams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class ChangeTeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("§cVous devez être un joueur pour exécuter cette commande.");
            return true;
        }

        if(args.length < 1) {
            player.sendMessage("§cVous devez spécifier une équipe. Liste: ");
            player.sendMessage("§cORANGE");
            player.sendMessage("§bPURPLE");
            return true;
        }

        String selectedTeam = args[0].toLowerCase(Locale.ROOT);

        switch (selectedTeam) {
            case "orange" -> TeamManager.setTeam(player, Teams.ORANGE);
            case "purple" -> TeamManager.setTeam(player, Teams.PURPLE);
            default -> {
            }
        }
        return false;
    }
}

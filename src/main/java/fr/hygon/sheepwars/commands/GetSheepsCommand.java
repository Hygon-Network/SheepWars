package fr.hygon.sheepwars.commands;

import fr.hygon.sheepwars.sheeps.SheepList;
import fr.hygon.yokura.entity.player.Ranks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetSheepsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("§cVous devez être un joueur pour exécuter cette commande.");
            return true;
        }

        if(player.getRank().equals(Ranks.ADMIN)) {
            for(SheepList sheepList : SheepList.values()) {
                player.getInventory().addItem(sheepList.getItemStack());
            }
        }
        return false;
    }
}

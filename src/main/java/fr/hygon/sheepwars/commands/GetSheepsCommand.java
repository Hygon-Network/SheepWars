package fr.hygon.sheepwars.commands;

import fr.hygon.sheepwars.sheeps.SheepList;
import fr.hygon.yokura.entity.player.Ranks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GetSheepsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("§cVous devez être un joueur pour exécuter cette commande.");
            return true;
        }

        if(player.getRank().equals(Ranks.ADMIN)) {
            for(SheepList sheepList : SheepList.values()) {
                ItemStack sheepItem = sheepList.getItemStack();
                sheepItem.setAmount(64);
                player.getInventory().addItem(sheepItem);
            }
        }
        return false;
    }
}

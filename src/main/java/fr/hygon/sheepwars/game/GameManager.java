package fr.hygon.sheepwars.game;

import fr.hygon.sheepwars.Main;
import fr.hygon.sheepwars.teams.TeamManager;
import fr.hygon.yokura.YokuraAPI;
import fr.hygon.yokura.servers.Status;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GameManager {
    private static BukkitTask task = null;
    private static int timer = 20;
    private static int oldPlayersOnline = 0;

    public static void startTask() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if(Bukkit.getOnlinePlayers().size() == 2) {
                    if(oldPlayersOnline != Bukkit.getOnlinePlayers().size()) {
                        oldPlayersOnline = Bukkit.getOnlinePlayers().size();
                        YokuraAPI.setStatus(Status.STARTING);
                    }
                    timer--;
                    Bukkit.broadcast(Component.text("timer: " + timer)); //TODO
                    if(timer == 0) {
                        Bukkit.broadcast(Component.text("Démarre!")); //TODO
                        cancel();
                        startGame();
                    }
                } else {
                    if(oldPlayersOnline != Bukkit.getOnlinePlayers().size()) {
                        oldPlayersOnline = Bukkit.getOnlinePlayers().size();
                        YokuraAPI.setStatus(Status.WAITING_PLAYERS);
                    }
                    timer = 20;
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }

    public static void stopTask() {
        if(task != null) {
            task.cancel();
        }
    }

    private static void startGame() {
        YokuraAPI.setStatus(Status.WAITING_PLAYERS);
        TeamManager.fillEmptyTeams();

        for(Player players : TeamManager.getPurplePlayers()) {
            players.teleport(MapSettings.purpleSpawnLocation);
        }

        for(Player players : TeamManager.getOrangePlayers()) {
            players.teleport(MapSettings.orangeSpawnLocation);
        }
    }
}

package fr.hygon.sheepwars.game;

import fr.hygon.sheepwars.Main;
import fr.hygon.sheepwars.sheeps.SheepList;
import fr.hygon.sheepwars.teams.TeamManager;
import fr.hygon.sheepwars.teams.Teams;
import fr.hygon.yokura.YokuraAPI;
import fr.hygon.yokura.servers.Status;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager {
    private static GameStatus gameStatus = GameStatus.WAITING;

    private static BukkitTask timerTask = null;
    private static int timer = 20;
    private static int oldPlayersOnline = 0;

    private static BukkitTask woolTask = null;
    private static int timeBeforeNextWool = 20;

    private static final ArrayList<Player> playersAlive = new ArrayList<>();
    private static final ArrayList<Player> deadPlayers = new ArrayList<>();

    public static void startTask() {
        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if(Bukkit.getOnlinePlayers().size() == 2) {
                    if(oldPlayersOnline != Bukkit.getOnlinePlayers().size()) {
                        oldPlayersOnline = Bukkit.getOnlinePlayers().size();
                        YokuraAPI.setStatus(Status.STARTING);
                    }
                    switch (timer) {
                        case 20, 10, 5, 4, 3, 2, 1 -> Bukkit.broadcast(
                                Component.text("La partie commence dans ").color(TextColor.color(255, 190, 0))
                                        .append(Component.text(timer).color(TextColor.color(0, 180, 0)))
                                        .append(Component.text(" secondes.").color(TextColor.color(255, 190, 0)))
                        );
                        case 0 -> {
                            Bukkit.broadcast(Component.text("La partie commence!").color(TextColor.color(0, 150, 0)));
                            cancel();
                            startGame();
                        }
                    }

                    timer--;
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
        if(timerTask != null) {
            timerTask.cancel();
        }

        if(woolTask != null) {
            woolTask.cancel();
        }
    }

    private static void startGame() {
        gameStatus = GameStatus.STARTED;
        YokuraAPI.setStatus(Status.WAITING_PLAYERS);
        TeamManager.fillEmptyTeams();

        playersAlive.addAll(Bukkit.getOnlinePlayers());

        for(Player players : TeamManager.getPurplePlayers()) {
            players.teleport(MapSettings.purpleSpawnLocation);
        }

        for(Player players : TeamManager.getOrangePlayers()) {
            players.teleport(MapSettings.orangeSpawnLocation);
        }

        timeBeforeNextWool = MapSettings.sheepObtentionTime;

        woolTask = new BukkitRunnable() {
            @Override
            public void run() {
                timeBeforeNextWool--;

                if(timeBeforeNextWool == 0) {
                    timeBeforeNextWool = MapSettings.sheepObtentionTime;
                    Bukkit.getOnlinePlayers().forEach(players -> {
                        SheepList sheep = SheepList.values()[ThreadLocalRandom.current().nextInt(0, SheepList.values().length)];

                        players.getInventory().addItem(sheep.getItemStack());
                    });
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }

    public static GameStatus getGameStatus() {
        return gameStatus;
    }

    public static void playerHasDied(Player player) {
        playersAlive.remove(player);
        deadPlayers.add(player);

        player.setGameMode(GameMode.SPECTATOR);

        int orangePlayersAlive = 0;
        int purplePlayersAlive = 0;

        for(Player alivePlayers : playersAlive) {
            if(TeamManager.getTeam(alivePlayers) == Teams.ORANGE) {
                orangePlayersAlive++;
            } else if(TeamManager.getTeam(alivePlayers) == Teams.PURPLE) {
                purplePlayersAlive++;
            }
        }

        if(purplePlayersAlive == 0 && orangePlayersAlive > 0) {
            endGame(Teams.ORANGE);
        } else if(orangePlayersAlive == 0 && purplePlayersAlive > 0) {
            endGame(Teams.PURPLE);
        }
    }

    public static void endGame(Teams winningTeam) {
        gameStatus = GameStatus.FINISHED;

        Bukkit.broadcast(Component.text("Fin de la partie! L'équipe ").color(TextColor.color(20, 175, 225))
                .append(winningTeam.getName())
                .append(Component.text("a gagnée!").color(TextColor.color(20, 175, 225))));

        for (Player players : Bukkit.getOnlinePlayers()) {
            if(TeamManager.getTeam(players) == winningTeam) { // TODO should we only give coins to players that stayed alive or should we also give them to the players that died?
                players.addCoins(20); // TODO is 20 a good value?
            }
        }
    }
}

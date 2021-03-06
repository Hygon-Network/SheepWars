package fr.hygon.sheepwars.game;

import fr.hygon.sheepwars.Main;
import fr.hygon.sheepwars.scoreboard.SheepWarsScoreboard;
import fr.hygon.sheepwars.sheeps.SheepList;
import fr.hygon.sheepwars.teams.TeamManager;
import fr.hygon.sheepwars.teams.Teams;
import fr.hygon.yokura.YokuraAPI;
import fr.hygon.yokura.servers.Status;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
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
                if(Bukkit.getOnlinePlayers().size() >= 2) {
                    if(oldPlayersOnline != Bukkit.getOnlinePlayers().size()) {
                        oldPlayersOnline = Bukkit.getOnlinePlayers().size();
                        YokuraAPI.setStatus(Status.STARTING);
                    }
                    switch (timer) {
                        case 20, 10, 5, 4, 3, 2, 1 -> Bukkit.broadcast(Component.text("» ").color(TextColor.color(NamedTextColor.GRAY))
                                .append(Component.text("La partie commence dans ").color(TextColor.color(255, 255, 75))
                                .append(Component.text(timer).color(TextColor.color(250, 65, 65)))
                                .append(Component.text(" secondes.").color(TextColor.color(255, 255, 75))))
                        );
                        case 0 -> {
                            Bukkit.broadcast(Component.text("» ").color(TextColor.color(NamedTextColor.GRAY))
                                    .append(Component.text("La partie commence!").color(TextColor.color(255, 255, 75))));
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

        playersAlive.addAll(Bukkit.getOnlinePlayers());
        TeamManager.fillEmptyTeams();

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
                        if(!deadPlayers.contains(players)) {
                            SheepList sheep = SheepList.values()[ThreadLocalRandom.current().nextInt(0, SheepList.values().length)];
                            players.getInventory().addItem(sheep.getItemStack());
                        }
                    });
                }

                Bukkit.getOnlinePlayers().forEach(players -> {
                    SheepWarsScoreboard.getScoreboard(players).setNewWoolTime(timeBeforeNextWool);
                    SheepWarsScoreboard.updateScoreboard(players);
                });
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

        int orangePlayersAlive = TeamManager.getOrangePlayersAlive();
        int purplePlayersAlive = TeamManager.getPurplePlayersAlive();


        if(purplePlayersAlive == 0 && orangePlayersAlive > 0) {
            endGame(Teams.ORANGE);
        } else if(orangePlayersAlive == 0 && purplePlayersAlive > 0) {
            endGame(Teams.PURPLE);
        }

        for (Player players : Bukkit.getOnlinePlayers()) {
            SheepWarsScoreboard.getScoreboard(players).setOrangeAlivePlayers(orangePlayersAlive);
            SheepWarsScoreboard.getScoreboard(players).setPurpleAlivePlayers(purplePlayersAlive);
            SheepWarsScoreboard.updateScoreboard(players);
        }
    }

    public static void endGame(Teams winningTeam) {
        gameStatus = GameStatus.FINISHED;

        Bukkit.broadcast(Component.text("» ").color(TextColor.color(NamedTextColor.GRAY))
                .append(Component.text("Fin de la partie! L'Équipe ").color(TextColor.color(255, 255, 75))
                .append(winningTeam.getName())
                .append(Component.text(" a gagnée!").color(TextColor.color(255, 255, 75))
                .append(Component.text(" «").color(TextColor.color(NamedTextColor.GRAY))))));

        for (Player players : Bukkit.getOnlinePlayers()) {
            if(TeamManager.getTeam(players) == winningTeam) { // TODO should we only give coins to players that stayed alive or should we also give them to the players that died? (Shep edit: oe mais avec une réduction)
                players.addCoins(20); // TODO is 20 a good value?

                final Title.Times times = Title.Times.of(Duration.ofMillis(1000), Duration.ofMillis(5000), Duration.ofMillis(1000));
                final Title title = Title.title((Component.text("VICTOIRE!").color(TextColor.color(255, 200, 75)).decoration(TextDecoration.BOLD, true)), Component.text(""), times);
                players.showTitle(title);
                players.playSound(players.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1F);
            } else {
                final Title.Times times = Title.Times.of(Duration.ofMillis(1000), Duration.ofMillis(5000), Duration.ofMillis(1000));
                final Title title = Title.title((Component.text("DÉFAITE!").color(TextColor.color(255, 60, 45)).decoration(TextDecoration.BOLD, true)), Component.text(""), times);
                players.showTitle(title);
                players.playSound(players.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.0F, 0F);
            }
        }
    }

    public static ArrayList<Player> getPlayersAlive() {
        return playersAlive;
    }
}

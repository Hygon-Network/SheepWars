package fr.hygon.sheepwars.utils;

import fr.hygon.sheepwars.teams.Teams;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class Scoreboard implements Listener {
    private static final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    private static final org.bukkit.scoreboard.Scoreboard scoreboard = scoreboardManager.getNewScoreboard();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setScoreboard(scoreboard);
    }

    public static void updateTeam(Player player, Teams team) {
        Team orangeTeam;
        Team purpleTeam;
        Team noTeam;

        orangeTeam = scoreboard.getTeam("orangeTeam");
        if(orangeTeam == null) {
            orangeTeam = scoreboard.registerNewTeam("orangeTeam");
            orangeTeam.color(NamedTextColor.GOLD);
        }

        purpleTeam = scoreboard.getTeam("purpleTeam");
        if(purpleTeam == null) {
            purpleTeam = scoreboard.registerNewTeam("purpleTeam");
            purpleTeam.color(NamedTextColor.DARK_PURPLE);
        }

        noTeam = scoreboard.getTeam("noTeam");
        if(noTeam == null) {
            noTeam = scoreboard.registerNewTeam("noTeam");
            noTeam.color(NamedTextColor.WHITE);
        }

        if(team == Teams.ORANGE) {
            orangeTeam.addEntry(player.getName());
        } else if(team == Teams.PURPLE) {
            purpleTeam.addEntry(player.getName());
        } else {
            noTeam.addEntry(player.getName());
        }

        player.setScoreboard(scoreboard);
    }
}

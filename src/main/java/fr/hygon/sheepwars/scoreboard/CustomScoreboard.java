package fr.hygon.sheepwars.scoreboard;

import fr.hygon.sheepwars.teams.Teams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class CustomScoreboard {
    private final Scoreboard scoreboard;
    private Objective objective;

    private final Player player;

    private int time = 0;
    private int newWoolTime = 0;
    private int newBonusTime = 0;
    private int kills = 0;

    public CustomScoreboard(Player player) {
        this.player = player;
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        this.scoreboard = scoreboardManager.getNewScoreboard();

        updateScoreboard();
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setNewWoolTime(int newWoolTime) {
        this.newWoolTime = newWoolTime;
    }

    public void setNewBonusTime(int newBonusTime) {
        this.newBonusTime = newBonusTime;
    }

    public void addKill() {
        kills++;
    }

    public void updateScoreboard() {
        if(objective != null) {
            objective.unregister();
        }
        objective = scoreboard.registerNewObjective("Hygon", "dummy", Component.text("• ").color(NamedTextColor.GRAY)
                .append(Component.text("SheepWars").color(TextColor.color(255, 227, 27))
                        .append(Component.text(" •").color(NamedTextColor.GRAY))));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score bar1 = objective.getScore("§r§8§m§l---------------");
        bar1.setScore(9);

        Score game = objective.getScore("§7Partie §8»");
        game.setScore(8);

        Score deathsScores = objective.getScore("Mouton §7» §e" + newWoolTime + "s");
        deathsScores.setScore(7);

        Score killStreakScore = objective.getScore("Bonus §7» §e" + newBonusTime + "s");
        killStreakScore.setScore(6);

        Score killsScore = objective.getScore("Kills §7» §e" + kills);
        killsScore.setScore(5);

        Score empty2 = objective.getScore("§1");
        empty2.setScore(4);

        Score players = objective.getScore("§7Joueurs §8»");
        players.setScore(3);

        Score orangePlayersAlive = objective.getScore("§6Oranges §7» §a0");
        orangePlayersAlive.setScore(2);

        Score purplePlayersAlive = objective.getScore("§5Violets §7» §a0" );
        purplePlayersAlive.setScore(1);

        Score bar2 = objective.getScore("§8§m§l---------------");
        bar2.setScore(0);

        player.setScoreboard(scoreboard);
    }

    public void updateTeam(Player updatedPlayer, Teams team) {
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
            orangeTeam.addEntry(updatedPlayer.getName());
        } else if(team == Teams.PURPLE) {
            purpleTeam.addEntry(updatedPlayer.getName());
        } else {
            noTeam.addEntry(updatedPlayer.getName());
        }

        player.setScoreboard(scoreboard);
    }
}

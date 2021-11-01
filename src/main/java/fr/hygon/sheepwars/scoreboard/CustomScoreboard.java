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

        Score bar1 = objective.getScore("§r§8---------------");
        bar1.setScore(7);

        Score timeScore = objective.getScore("§6Temps: §a" + time);
        timeScore.setScore(6);

        Score empty = objective.getScore("§0");
        empty.setScore(5);

        Score game = objective.getScore("§7Partie §8»");
        game.setScore(4);

        Score deathsScores = objective.getScore("Prochaine laine §8» §a" + newWoolTime);
        deathsScores.setScore(3);

        Score killStreakScore = objective.getScore("Prochain bonus §8» §a" + newBonusTime);
        killStreakScore.setScore(2);

        Score killsScore = objective.getScore("Kills §8» §a" + kills);
        killsScore.setScore(1);

        Score bar2 = objective.getScore("§8---------------");
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

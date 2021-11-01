package fr.hygon.sheepwars.teams;

import fr.hygon.sheepwars.scoreboard.SheepWarsScoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class TeamManager {
    private static final int maxPlayerPerTeam = 4;

    private static final ArrayList<UUID> orangeTeam = new ArrayList<>();
    private static final ArrayList<UUID> purpleTeam = new ArrayList<>();

    public static Teams getTeam(Player player) {
        if(orangeTeam.contains(player.getUniqueId())) {
            return Teams.ORANGE;
        } else if(purpleTeam.contains(player.getUniqueId())) {
            return Teams.PURPLE;
        } else {
            return Teams.NONE;
        }
    }

    public static void setTeam(Player player, Teams team) {
        if(getTeam(player) != team) {
            orangeTeam.remove(player.getUniqueId());
            purpleTeam.remove(player.getUniqueId());

            Component newName;

            player.displayName(Component.text(player.getName()));

            if(team == Teams.ORANGE) {
                orangeTeam.add(player.getUniqueId());
                newName = player.displayName().color(TextColor.color(255, 120, 0));
            } else if(team == Teams.PURPLE) {
                purpleTeam.add(player.getUniqueId());
                newName = player.displayName().color(TextColor.color(195, 35, 255));
            } else {
                newName = player.displayName().color(TextColor.color(255, 255, 255));
            }

            Bukkit.getOnlinePlayers().forEach(players -> {
                SheepWarsScoreboard.getScoreboard(players).updateTeam(player, team);
                SheepWarsScoreboard.updateScoreboard(players);
            });

            player.playerListName(newName);
            player.displayName(newName);

            player.setCustomNameVisible(true);
        }
    }

    public static void fillEmptyTeams() {
        for(Player players : Bukkit.getOnlinePlayers()) {
            if(getTeam(players) == Teams.NONE) {
                if(!isFull(Teams.ORANGE)) {
                    setTeam(players, Teams.ORANGE);
                } else if(isFull(Teams.PURPLE)) {
                    setTeam(players, Teams.PURPLE);
                }
            }
        }
    }

    public static boolean isFull(Teams team) {
        if(team == Teams.ORANGE) {
            return orangeTeam.size() >= maxPlayerPerTeam;
        } else if(team == Teams.PURPLE) {
            return purpleTeam.size() >= maxPlayerPerTeam;
        } else {
            return false;
        }
    }

    public static ArrayList<Player> getOrangePlayers() {
        ArrayList<Player> orangeTeamList = new ArrayList<>();
        for (UUID uuids : orangeTeam) {
            orangeTeamList.add(Bukkit.getPlayer(uuids));
        }

        return orangeTeamList;
    }

    public static ArrayList<Player> getPurplePlayers() {
        ArrayList<Player> purpleTeamList = new ArrayList<>();
        for (UUID uuids : purpleTeam) {
            purpleTeamList.add(Bukkit.getPlayer(uuids));
        }

        return purpleTeamList;
    }
}

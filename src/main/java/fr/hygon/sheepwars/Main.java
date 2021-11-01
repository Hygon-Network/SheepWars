package fr.hygon.sheepwars;

import fr.hygon.sheepwars.commands.ChangeTeamCommand;
import fr.hygon.sheepwars.commands.GetSheepsCommand;
import fr.hygon.sheepwars.events.*;
import fr.hygon.sheepwars.game.GameManager;
import fr.hygon.sheepwars.game.MapSettings;
import fr.hygon.sheepwars.utils.Scoreboard;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        if(!MapSettings.isSheepWarsMap()) {
            getLogger().severe("This world does not contain a sheepwars world settings file!" +
                    " (It should be named sheepwars.yml in the world folder.) The plugin will be unloaded.");
            MapSettings.initFile();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        MapSettings.initFile();
        registerEvents();
        registerCommands();
        GameManager.startTask();
    }

    @Override
    public void onDisable() {
        GameManager.stopTask();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJoinLeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new Scoreboard(), this);
        getServer().getPluginManager().registerEvents(new GUIManager(), this);
        getServer().getPluginManager().registerEvents(new SheepActions(), this);
        getServer().getPluginManager().registerEvents(new OnDeath(), this);
        getServer().getPluginManager().registerEvents(new PlayerUtilsEvent(), this);
    }

    private void registerCommands() {
        getCommand("getsheeps").setExecutor(new GetSheepsCommand());
        getCommand("changeteam").setExecutor(new ChangeTeamCommand());
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}

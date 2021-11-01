package fr.hygon.sheepwars.game;

import com.google.common.base.Throwables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.logging.Level;

public class MapSettings {
    private static final File file = new File(Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/world/sheepwars.yml");
    private static YamlConfiguration configFile;

    public static boolean isSheepWarsMap() {
        return file.exists();
    }

    public static void initFile() {
        configFile = new YamlConfiguration();

        try {
            if(!file.exists()) {
                if(!file.createNewFile()) {
                    Bukkit.getLogger().severe("Couldn't create the sheepwars.yml file.");
                }
            }
            configFile.load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            Bukkit.getLogger().log(Level.SEVERE, "There's an error in your sheepwars.yml file. Please correct your syntax errors", exception);
        }

        configFile.options().copyDefaults(true);

        readConfig(MapSettings.class, null);
    }

    static void readConfig(Class<?> clazz, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(instance);
                    } catch (InvocationTargetException ex) {
                        throw Throwables.propagate(ex.getCause());
                    } catch (Exception ex) {
                        Bukkit.getLogger().log(Level.SEVERE, "Error invoking " + method, ex);
                    }
                }
            }
        }

        try {
            configFile.save(file);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + file, ex);
        }
    }

    private static void set(String path, Object val) {
        configFile.set(path, val);
    }

    private static boolean getBoolean(String path, boolean def) {
        configFile.addDefault(path, def);
        return configFile.getBoolean(path, configFile.getBoolean(path));
    }

    private static double getDouble(String path, double def) {
        configFile.addDefault(path, def);
        return configFile.getDouble(path, configFile.getDouble(path));
    }

    private static float getFloat(String path, float def) {
        return (float) getDouble(path, (double) def);
    }

    private static int getInt(String path, int def) {
        configFile.addDefault(path, def);
        return configFile.getInt(path, configFile.getInt(path));
    }

    private static <T> List getList(String path, T def) {
        configFile.addDefault(path, def);
        return (List<T>) configFile.getList(path, configFile.getList(path));
    }

    private static String getString(String path, String def) {
        configFile.addDefault(path, def);
        return configFile.getString(path, configFile.getString(path));
    }

    private static Location getLocation(String path, Location def) {
        configFile.addDefault(path, def);
        return configFile.getLocation(path, configFile.getLocation(path));
    }

    public static int sheepObtentionTime;
    private static void getSheepObtentionTime() {
        sheepObtentionTime = getInt("game-settings.sheep-obtention-time", 20);
    }

    public static int mapVoidY;
    private static void getMapVoidY() {
        mapVoidY = getInt("game-settings.map-void-y", -10);
    }

    public static Location spawnLocation;
    private static void getSpawnLocation() {
        spawnLocation = getLocation("spawns.spawn", new Location(Bukkit.getWorld("world"), 0, 100, 0));
    }

    public static Location purpleSpawnLocation;
    private static void getPurpleSpawnLocation() {
        purpleSpawnLocation = getLocation("spawns.purple", new Location(Bukkit.getWorld("world"), 0, 100, 0));
    }

    public static Location orangeSpawnLocation;
    private static void getRedSpawnLocation() {
        orangeSpawnLocation = getLocation("spawns.orange", new Location(Bukkit.getWorld("world"), 0, 100, 0));
    }

    public static double sheepVelocity;
    private static void getSheepVelocity() {
        sheepVelocity = getDouble("sheep.velocity", 15);
    }

    public static double searchingSheepSpeed;
    private static void getSearchingSheepSpeed() {
        searchingSheepSpeed = getDouble("sheep.searching-sheep.speed", 1.5);
    }

    public static int searchingSheepExplosionPower;
    private static void getSearchingSheepExplosionPower() {
        searchingSheepExplosionPower = getInt("sheep.searching-sheep.explosion-power", 10);
    }

    public static int antiGravitySheepAttractionDistance;
    private static void getAntiGravitySheepAttractionDistance() {
        antiGravitySheepAttractionDistance = getInt("sheep.antigravity-sheep.attraction-distance", 10);
    }

    public static int regenSheepHealDistance;
    private static void getRegenSheepHealDistance() {
        regenSheepHealDistance = getInt("sheep.regen-sheep.heal-distance", 4);
    }

    public static int explosiveSheepPower;
    private static void getExplosiveSheepPower() {
        explosiveSheepPower = getInt("sheep.explosive-sheep.explosion-power", 7);
    }

    public static int explosiveSheepDamage;
    private static void getExplosiveSheepDamage() {
        explosiveSheepDamage = getInt("sheep.explosive-sheep.damage", 18);
    }

    public static int incendiarySheepRadius;
    private static void getIncendiarySheepRadius() {
        incendiarySheepRadius = getInt("sheep.incendiary-sheep.flame-radius", 8);
    }

    public static int incendiarySheepPower;
    private static void getIncendiarySheepPower() {
        incendiarySheepPower = getInt("sheep.incendiary-sheep.power", 25);
        if(incendiarySheepPower < 0 || incendiarySheepPower > 100) {
            incendiarySheepPower = 25;
        }
    }

    public static int icingSheepRadiusEffect;
    private static void getIcingSheepRadiusEffect() {
        icingSheepRadiusEffect = getInt("sheep.icing-sheep.radius-effect", 10);
    }
}

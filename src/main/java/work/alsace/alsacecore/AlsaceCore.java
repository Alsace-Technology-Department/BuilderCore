package work.alsace.alsacecore;

import com.puddingkc.commands.essentials.*;
import com.puddingkc.commands.fawe.*;
import com.puddingkc.commands.puddingUtilities.*;
import com.puddingkc.events.BlockEvent;
import com.puddingkc.events.Misc;
import com.puddingkc.events.Protect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import work.alsace.alsacecore.Util.HomeDataLoader;
import work.alsace.alsacecore.Util.NoClipUtil;
import work.alsace.alsacecore.Util.WarpDataLoader;
import work.alsace.alsacecore.commands.AlsaceCoreCommand;
import work.alsace.alsacecore.commands.HatCommand;
import work.alsace.alsacecore.commands.home.DelHomeCommand;
import work.alsace.alsacecore.commands.home.HomeCommand;
import work.alsace.alsacecore.commands.home.SetHomeCommand;
import work.alsace.alsacecore.commands.teleport.TPCommand;
import work.alsace.alsacecore.commands.teleport.TPIgnoreCommand;
import work.alsace.alsacecore.commands.warp.DelWarpCommand;
import work.alsace.alsacecore.commands.warp.SetWarpCommand;
import work.alsace.alsacecore.commands.warp.WarpCommand;
import work.alsace.alsacecore.listeners.PlayerListener;

import java.io.File;
import java.util.*;

public class AlsaceCore extends JavaPlugin {

    public Map<String, Boolean> hasIgnored = new HashMap<>();
    public HashMap<UUID, HomeDataLoader> homeProfiles = new HashMap<UUID, HomeDataLoader>();
    public HashMap<String, WarpDataLoader> warpProfiles = new HashMap<String, WarpDataLoader>();
    public List<String> illegalCharacters = new ArrayList<>();
    public String motd;

    public static AlsaceCore instance;

    @Override
    public void onEnable() {
        registerCommands();
        registerListeners();
        loadConfig();
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            new NoClipUtil().checkBlock();
        }, 1L, 1L);
        getLogger().info("插件已完全加载完成");
    }

    @Override
    public void onDisable() {
        homeProfiles.clear();
        warpProfiles.clear();
        getLogger().info("插件已卸载");
    }

    private void registerCommands() {
        AlsaceCore.instance = this;
        Objects.requireNonNull(getCommand("fly")).setExecutor(new FlyCommand());
        Objects.requireNonNull(getCommand("speed")).setExecutor(new SpeedCommand());
        Objects.requireNonNull(getCommand("speed")).setTabCompleter(new SpeedCommand());
        Objects.requireNonNull(getCommand("gamemode")).setExecutor(new GameModeCommand());
        Objects.requireNonNull(getCommand("gamemode")).setTabCompleter(new GameModeCommand());
        Objects.requireNonNull(getCommand("time")).setExecutor(new TimeCommand());
        Objects.requireNonNull(getCommand("time")).setTabCompleter(new TimeCommand());
        Objects.requireNonNull(getCommand("weather")).setExecutor(new WeatherCommand());
        Objects.requireNonNull(getCommand("weather")).setTabCompleter(new WeatherCommand());
        Objects.requireNonNull(getCommand("ptime")).setExecutor(new PTimeCommand());
        Objects.requireNonNull(getCommand("ptime")).setTabCompleter(new PTimeCommand());
        Objects.requireNonNull(getCommand("pweather")).setExecutor(new PWeatherCommand());
        Objects.requireNonNull(getCommand("pweather")).setTabCompleter(new PWeatherCommand());
        Objects.requireNonNull(getCommand("head")).setExecutor(new HeadCommand());
        Objects.requireNonNull(getCommand("night")).setExecutor(new NightvisionCommand());
        Objects.requireNonNull(getCommand("advfly")).setExecutor(new AdvanceFlyCommand());
        Objects.requireNonNull(getCommand("debugstick")).setExecutor(new DebugStickCommand());
        Objects.requireNonNull(getCommand("slab")).setExecutor(new SlabCommand());
        Objects.requireNonNull(getCommand("noclip")).setExecutor(new NoClipCommand());

        Objects.requireNonNull(getCommand("/convex")).setExecutor(new ConvexCommand());
        Objects.requireNonNull(getCommand("/derot")).setExecutor(new DerotCommand());
        Objects.requireNonNull(getCommand("/cuboid")).setExecutor(new CuboidCommand());
        Objects.requireNonNull(getCommand("/twist")).setExecutor(new TwistCommand());
        Objects.requireNonNull(getCommand("/scale")).setExecutor(new ScaleCommand());
        Objects.requireNonNull(getCommand("undo")).setExecutor(new UndoCommand());

        Objects.requireNonNull(getCommand("hat")).setExecutor(new HatCommand());
        Objects.requireNonNull(getCommand("tp")).setExecutor(new TPCommand(this));
        Objects.requireNonNull(getCommand("tpignore")).setExecutor(new TPIgnoreCommand(this));

        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand());
        Objects.requireNonNull(getCommand("home")).setTabCompleter(new HomeCommand());
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SetHomeCommand());
        Objects.requireNonNull(getCommand("delhome")).setExecutor(new DelHomeCommand());
        Objects.requireNonNull(getCommand("delhome")).setTabCompleter(new DelHomeCommand());

        Objects.requireNonNull(getCommand("warp")).setExecutor(new WarpCommand());
        Objects.requireNonNull(getCommand("warp")).setTabCompleter(new WarpCommand());
        Objects.requireNonNull(getCommand("setwarp")).setExecutor(new SetWarpCommand());
        Objects.requireNonNull(getCommand("delwarp")).setExecutor(new DelWarpCommand());
        Objects.requireNonNull(getCommand("delwarp")).setTabCompleter(new DelWarpCommand());

        Objects.requireNonNull(getCommand("alsacecore")).setExecutor(new AlsaceCoreCommand(this));
        Objects.requireNonNull(getCommand("alsacecore")).setTabCompleter(new AlsaceCoreCommand(this));
        getLogger().info("指令注册完成");
    }

    private void registerListeners() {
        AlsaceCore.instance = this;
        getServer().getPluginManager().registerEvents(new Protect(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockEvent(), this);
        getServer().getPluginManager().registerEvents(new Misc(), this);
        getServer().getPluginManager().registerEvents(new AdvanceFlyCommand(), this);
        getLogger().info("事件注册完成");
    }

    public void loadConfig() {
        saveDefaultConfig();
        reloadConfig();
        if (this.getConfig().getConfigurationSection("illegal-characters") != null) {
            for (String i : this.getConfig().getStringList("illegal-characters")) {
                illegalCharacters.add(i.toLowerCase());
            }
        }

        List<String> motdLines = this.getConfig().getStringList("motd");
        try {
            StringBuilder motdBuilder = new StringBuilder();
            for (String line : motdLines) {
                motdBuilder.append(line).append("\n");
            }
            String motdString = motdBuilder.toString().trim();
        } catch (IllegalArgumentException e) {
            getLogger().warning("MOTD configuration not found or invalid");
            motd = "";
        }

        this.motd = motd;

        File folder = new File(getDataFolder(), "userdata");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        for (Player i : Bukkit.getServer().getOnlinePlayers()) {
            homeProfiles.put(i.getUniqueId(), new HomeDataLoader(i.getUniqueId()));
        }
        warpProfiles.put("warps", new WarpDataLoader("warps"));
    }
}

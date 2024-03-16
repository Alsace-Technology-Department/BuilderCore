package work.alsace.buildercore;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import work.alsace.buildercore.Utils.NoClipUtil;
import work.alsace.buildercore.Utils.Placeholder;
import work.alsace.buildercore.commands.alias.*;
import work.alsace.buildercore.commands.builderTools.*;
import work.alsace.buildercore.commands.home.DelHomeCommand;
import work.alsace.buildercore.commands.home.HomeCommand;
import work.alsace.buildercore.commands.home.HomesCommand;
import work.alsace.buildercore.commands.home.SetHomeCommand;
import work.alsace.buildercore.commands.itemEdit.ItemColorCommand;
import work.alsace.buildercore.commands.itemEdit.ItemNameCommand;
import work.alsace.buildercore.commands.itemEdit.ItemNbtCommand;
import work.alsace.buildercore.commands.players.*;
import work.alsace.buildercore.commands.teleport.*;
import work.alsace.buildercore.commands.time.PTimeCommand;
import work.alsace.buildercore.commands.time.TimeCommand;
import work.alsace.buildercore.commands.tools.BuilderCoreCommand;
import work.alsace.buildercore.commands.tools.WhoisCommand;
import work.alsace.buildercore.commands.warp.DelWarpCommand;
import work.alsace.buildercore.commands.warp.SetWarpCommand;
import work.alsace.buildercore.commands.warp.WarpCommand;
import work.alsace.buildercore.commands.warp.WarpsCommand;
import work.alsace.buildercore.commands.weather.PWeatherCommand;
import work.alsace.buildercore.commands.weather.WeatherCommand;
import work.alsace.buildercore.listeners.*;
import work.alsace.buildercore.service.HomeDataLoader;
import work.alsace.buildercore.service.TPAHandler;
import work.alsace.buildercore.service.WarpDataLoader;

import java.io.File;
import java.util.*;

public class BuilderCore extends JavaPlugin {

    private static BuilderCore instance;
    private final Map<String, Boolean> hasIgnored = new HashMap<>();
    private final HashMap<UUID, HomeDataLoader> homeProfiles = new HashMap<>();
    private final HashMap<String, WarpDataLoader> warpProfiles = new HashMap<>();
    private TPAHandler tpaHandler;

    private int backHistory;

    private String afkPrefix;
    private List<String> blockedCommands;

    @Override
    public void onEnable() {
        loadConfig();
        tpaHandler = new TPAHandler(this);
        registerCommands();
        registerListeners();
        Bukkit.getScheduler().runTaskTimer(this, () -> new NoClipUtil().checkBlock(), 1L, 1L);
        getLogger().info("插件已完全加载完成");
    }

    @Override
    public void onDisable() {
        homeProfiles.clear();
        warpProfiles.clear();
        getLogger().info("插件已卸载");
    }

    private void registerCommands() {
        BuilderCore.instance = this;
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
        Objects.requireNonNull(getCommand("back")).setExecutor(new BackCommand(this));
        Objects.requireNonNull(getCommand("afk")).setExecutor(new AFKCommand(this));

        Objects.requireNonNull(getCommand("tp")).setExecutor(new TPCommand(this));
        Objects.requireNonNull(getCommand("tphere")).setExecutor(new TPHereCommand());
        Objects.requireNonNull(getCommand("tpignore")).setExecutor(new TPIgnoreCommand(this));
        Objects.requireNonNull(getCommand("tpahere")).setExecutor(new TPACommand(this));
        Objects.requireNonNull(getCommand("tpaccept")).setExecutor(new TPACommand(this));
        Objects.requireNonNull(getCommand("tpdeny")).setExecutor(new TPACommand(this));

        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand(this));
        Objects.requireNonNull(getCommand("home")).setTabCompleter(new HomeCommand(this));
        Objects.requireNonNull(getCommand("homes")).setExecutor(new HomesCommand(this));
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SetHomeCommand(this));
        Objects.requireNonNull(getCommand("delhome")).setExecutor(new DelHomeCommand(this));
        Objects.requireNonNull(getCommand("delhome")).setTabCompleter(new DelHomeCommand(this));

        Objects.requireNonNull(getCommand("warp")).setExecutor(new WarpCommand(this));
        Objects.requireNonNull(getCommand("warp")).setTabCompleter(new WarpCommand(this));
        Objects.requireNonNull(getCommand("warps")).setExecutor(new WarpsCommand(this));
        Objects.requireNonNull(getCommand("setwarp")).setExecutor(new SetWarpCommand(this));
        Objects.requireNonNull(getCommand("delwarp")).setExecutor(new DelWarpCommand(this));
        Objects.requireNonNull(getCommand("delwarp")).setTabCompleter(new DelWarpCommand(this));

        Objects.requireNonNull(getCommand("buildercore")).setExecutor(new BuilderCoreCommand(this));
        Objects.requireNonNull(getCommand("buildercore")).setTabCompleter(new BuilderCoreCommand(this));

        Objects.requireNonNull(getCommand("whois")).setExecutor(new WhoisCommand());
        Objects.requireNonNull(getCommand("itemname")).setExecutor(new ItemNameCommand());
        Objects.requireNonNull(getCommand("itemnbt")).setExecutor(new ItemNbtCommand());
        Objects.requireNonNull(getCommand("itemcolor")).setExecutor(new ItemColorCommand());

        blockedCommands = Arrays.asList("/plugins", "/?", "/help", "/bukkit");
        getLogger().info("指令注册完成");
    }

    private void registerListeners() {
        BuilderCore.instance = this;
        getServer().getPluginManager().registerEvents(new Protect(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new Misc(this), this);
        getServer().getPluginManager().registerEvents(new AdvanceFlyCommand(), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(), this);
        getServer().getPluginManager().registerEvents(new CatListener(), this);
        getServer().getPluginManager().registerEvents(new HorseListener(), this);
        getServer().getPluginManager().registerEvents(new FoxListener(), this);

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholder(this).register();
            getServer().getPluginManager().registerEvents(new AFKListener(this), this);
        } else {
            getLogger().warning("前置插件 PlaceholderAPI 不存在，Afk功能注册失败");
        }

        getLogger().info("事件注册完成");
    }

    public void loadConfig() {
        saveDefaultConfig();
        reloadConfig();
        File folder = new File(getDataFolder(), "userdata");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        for (Player i : Bukkit.getServer().getOnlinePlayers()) {
            homeProfiles.put(i.getUniqueId(), new HomeDataLoader(i.getUniqueId(), this));
        }
        warpProfiles.put("warps", new WarpDataLoader(this));
        afkPrefix = Objects.requireNonNull(this.getConfig().getConfigurationSection("afk")).getString("placeholder");
        backHistory = this.getConfig().getInt("backHistory");
    }

    public TPAHandler getTPAHandler() {
        return tpaHandler;
    }

    public String getAfkPrefix() {
        return afkPrefix;
    }

    public List<String> getBlockedCommands() {
        return blockedCommands;
    }

    public Map<String, Boolean> getHasIgnored() {
        return hasIgnored;
    }

    public HashMap<UUID, HomeDataLoader> getHomeProfiles() {
        return homeProfiles;
    }

    public HashMap<String, WarpDataLoader> getWarpProfiles() {
        return warpProfiles;
    }

    public static BuilderCore getInstance() {
        return instance;
    }

    public int getBackHistory() {
        return backHistory;
    }
}

package work.alsace.alsacecore;

import com.puddingkc.commands.*;
import com.puddingkc.events.Protect;
import org.bukkit.plugin.java.JavaPlugin;
import work.alsace.alsacecore.Util.User;
import work.alsace.alsacecore.commands.*;
import work.alsace.alsacecore.listeners.PlayerListener;

import java.util.*;

public class AlsaceCore extends JavaPlugin {

    public Map<String, Boolean> hasIgnored = new HashMap<>();
    public HashMap<UUID, User> userProfiles = new HashMap<UUID, User>();

    public List<String> illegalCharacters = new ArrayList<>();

    public static AlsaceCore instance;
    @Override
    public void onEnable() {
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

        Objects.requireNonNull(getCommand("hat")).setExecutor(new HatCommand());
        Objects.requireNonNull(getCommand("tp")).setExecutor(new TPCommand(this));
        Objects.requireNonNull(getCommand("tpignore")).setExecutor(new TPIgnoreCommand(this));

        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand());
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SetHomeCommand());
        Objects.requireNonNull(getCommand("delhome")).setExecutor(new DelHomeCommand());

        getLogger().info("指令注册完成");
        loadConfig();
        getServer().getPluginManager().registerEvents(new Protect(),this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getLogger().info("事件注册完成");
        getLogger().info("插件已完全加载完成");
    }

    @Override
    public void onDisable() {
        getLogger().info("插件已卸载");
    }

    private void loadConfig() {
        saveDefaultConfig();
        reloadConfig();
        illegalCharacters = getConfig().getStringList("illegal-characters");
    }
}

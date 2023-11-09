package work.alsace.alsacecore;

import com.puddingkc.commands.*;
import com.puddingkc.events.protect;
import org.bukkit.plugin.java.JavaPlugin;
import work.alsace.alsacecore.commands.HatCommand;
import work.alsace.alsacecore.listeners.PlayerListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AlsaceCore extends JavaPlugin {

    public Map<String, Boolean> hasIgnored = new HashMap();
    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("fly")).setExecutor(new fly());
        Objects.requireNonNull(getCommand("speed")).setExecutor(new speed());
        Objects.requireNonNull(getCommand("speed")).setTabCompleter(new speed());
        Objects.requireNonNull(getCommand("gamemode")).setExecutor(new gamemode());
        Objects.requireNonNull(getCommand("gamemode")).setTabCompleter(new gamemode());
        Objects.requireNonNull(getCommand("time")).setExecutor(new time());
        Objects.requireNonNull(getCommand("time")).setTabCompleter(new time());
        Objects.requireNonNull(getCommand("weather")).setExecutor(new weather());
        Objects.requireNonNull(getCommand("weather")).setTabCompleter(new weather());
        Objects.requireNonNull(getCommand("ptime")).setExecutor(new ptime());
        Objects.requireNonNull(getCommand("ptime")).setTabCompleter(new ptime());

        Objects.requireNonNull(getCommand("hat")).setExecutor(new HatCommand());

        getLogger().info("指令注册完成");
        getServer().getPluginManager().registerEvents(new protect(),this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getLogger().info("事件注册完成");
        getLogger().info("插件已完全加载完成");
    }

    @Override
    public void onDisable() {
        getLogger().info("插件已卸载");
    }
}

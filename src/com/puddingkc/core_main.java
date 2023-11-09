package com.puddingkc;

import com.puddingkc.commands.*;
import com.puddingkc.events.protect;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class core_main extends JavaPlugin {
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
        getLogger().info("指令注册完成");
        getServer().getPluginManager().registerEvents(new protect(),this);
        getLogger().info("事件注册完成");
        getLogger().info("插件已完全加载完成");
    }
}

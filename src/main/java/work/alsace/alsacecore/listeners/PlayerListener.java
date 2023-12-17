package work.alsace.alsacecore.listeners;

import com.puddingkc.commands.puddingUtilities.AdvanceFlyCommand;
import com.puddingkc.events.BlockEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import work.alsace.alsacecore.AlsaceCore;
import work.alsace.alsacecore.Util.DataBaseManager;
import work.alsace.alsacecore.Util.HomeDataLoader;
import work.alsace.alsacecore.Util.NoClipUtil;
import work.alsace.alsacecore.commands.BackCommand;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PlayerListener implements Listener {
    private final AlsaceCore plugin;
    private final DataBaseManager databaseManager;

    public PlayerListener(AlsaceCore plugin) {
        this.plugin = plugin;
        this.databaseManager = plugin.getDatabaseManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        BlockEvent.slabs.add(event.getPlayer());
        plugin.homeProfiles.put(event.getPlayer().getUniqueId(), new HomeDataLoader(event.getPlayer().getUniqueId()));
        Player player = event.getPlayer();
        if (plugin.agreement) {
            boolean hasAgreed = databaseManager.hasPlayerAgreed(player.getUniqueId());
            if (!hasAgreed) {
                if (!event.getPlayer().hasPlayedBefore()) {
                    Bukkit.broadcastMessage("§a欢迎新玩家 §f" + event.getPlayer().getName() + " §a加入§f阿尔萨斯§a服务器");
                }
                sendAgreementMessage(player);
                plugin.hasAgree.put(player.getName(), true); //disagree
            } else {
                plugin.hasAgree.put(player.getName(), false); //agree
            }
        } else {
            plugin.hasAgree.put(player.getName(), false);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        this.plugin.hasIgnored.remove(event.getPlayer().getName());
        plugin.homeProfiles.remove(event.getPlayer().getUniqueId());
        if (NoClipUtil.noclip.contains(event.getPlayer()) || AdvanceFlyCommand.enabledPlayers.contains(event.getPlayer()) || BlockEvent.slabs.contains(event.getPlayer())) {
            NoClipUtil.noclip.remove(event.getPlayer());
            AdvanceFlyCommand.enabledPlayers.remove(event.getPlayer());
            BlockEvent.slabs.remove(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (plugin.hasAgree.getOrDefault(player.getName(), false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (plugin.agreement) {
            if (!databaseManager.hasPlayerAgreed(player.getUniqueId())) {
                String message = event.getMessage().toLowerCase();
                if (!message.startsWith("/agree") && !message.startsWith("/disagree")) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "你必须先同意用户协议才能使用命令！");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause().equals(TeleportCause.SPECTATE) && !event.getPlayer().hasPermission("alsace.event.spectate")) {
            event.setCancelled(true);
        }

        // 检查/back命令是否已注册
        if (plugin.getCommand("back") != null) {
            BackCommand backCommand = (BackCommand) Objects.requireNonNull(plugin.getCommand("back")).getExecutor();
            backCommand.addToHistory(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (plugin.agreement) {
            if (!databaseManager.hasPlayerAgreed(player.getUniqueId())) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "你必须先同意用户协议才能聊天！");
            }
        }
    }


    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        plugin.homeProfiles.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void playerChangeSign(SignChangeEvent event) {
        boolean cnt = false;
    }

    @EventHandler
    public void playerAnvil(PrepareAnvilEvent event) {
        ItemStack item = event.getResult();
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                String name = meta.getDisplayName();
                name = ChatColor.translateAlternateColorCodes('&', name);
                meta.setDisplayName(this.translateHexColorCodes(name));
                item.setItemMeta(meta);
                event.setResult(item);
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    private String translateHexColorCodes(String message) {
        Pattern hexPattern = Pattern.compile("#([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder(message.length() + 32);

        while (matcher.find()) {
            String group = matcher.group(1);
            char var10002 = group.charAt(0);
            matcher.appendReplacement(buffer, "§x§" + var10002 + "§" + group.charAt(1) + "§" + group.charAt(2) + "§" + group.charAt(3) + "§" + group.charAt(4) + "§" + group.charAt(5));
        }

        return matcher.appendTail(buffer).toString();
    }

    private void sendAgreementMessage(Player player) {
        TextComponent welcomeComponent = new TextComponent(ChatColor.GOLD + "您必须阅读并同意");

        TextComponent agreementLink = new TextComponent(ChatColor.GREEN + "《工业园用户协议》");
        agreementLink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://alsaceteam.feishu.cn/wiki/F8AHwoD18iq45fk3ieSc6pcGnYy"));

        TextComponent welcomeComponent1 = new TextComponent(ChatColor.GOLD + "才可以正常使用工业园的服务");

        TextComponent agreeButton = new TextComponent(ChatColor.GREEN + "[同意]");
        agreeButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/agree"));

        TextComponent disagreeButton = new TextComponent(ChatColor.RED + "[不同意]");
        disagreeButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/disagree"));

        welcomeComponent.addExtra(agreementLink);
        welcomeComponent.addExtra(welcomeComponent1);
        welcomeComponent.addExtra("\n");
        welcomeComponent.addExtra(agreeButton);
        welcomeComponent.addExtra(" ");
        welcomeComponent.addExtra(disagreeButton);

        player.spigot().sendMessage(welcomeComponent);
    }

}

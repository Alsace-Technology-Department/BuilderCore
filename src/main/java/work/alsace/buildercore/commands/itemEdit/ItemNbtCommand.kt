package work.alsace.buildercore.commands.itemEdit;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ItemNbtCommand implements CommandExecutor {

    private final String error = ChatColor.GRAY + "正确指令:\n§f/itemnbt §7- 获取手中物品的详细信息";
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
        if (sender instanceof Player player) {
            if (!sender.hasPermission("buildercore.commands.itemnbt")) {
                sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }

            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.getType().isAir()) {
                player.sendMessage(ChatColor.RED + "你手中没有物品");
                return false;
            }

            TextComponent Component = new TextComponent("§7物品NBT: §a" + Objects.requireNonNull(itemInHand.getItemMeta()).toString());
            Component.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, itemInHand.getItemMeta().toString()));
            TextComponent hoverText = new TextComponent("点击复制");
            Component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{hoverText}));

            player.sendMessage("§7物品信息");
            player.sendMessage("§7物品ID: §f" + itemInHand.getType());
            player.sendMessage("§7物品名称: §f" + Objects.requireNonNull(itemInHand.getItemMeta()).getDisplayName());
            player.spigot().sendMessage(Component);
            if (itemInHand.getItemMeta().hasCustomModelData()) {
                player.sendMessage("§7CustomModelData: §f" + itemInHand.getItemMeta().getCustomModelData());
            }
            return true;
        }
        sender.sendMessage(error);
        return false;
    }
}

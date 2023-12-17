package work.alsace.alsacecore.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class HorseListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) {
            return;
        }
        if (player.getInventory().getItemInMainHand().getType().isAir() && player.hasPermission("alsace.protect.place")) {
            if (event.getRightClicked() instanceof Horse horse) {
                Horse.Color newColor = getNextHorseColor(horse.getColor());
                horse.setColor(newColor);
            }
        } else if (player.getInventory().getItemInMainHand().getType() == Material.STICK && player.hasPermission("alsace.protect.place")) {
            if (event.getRightClicked() instanceof Horse horse) {
                Horse.Style newStyle = getNextHorseStyle(horse.getStyle());
                horse.setStyle(newStyle);
            }
        }
    }

    private Horse.Color getNextHorseColor(Horse.Color currentType) {
        return switch (currentType) {
            case BLACK -> Horse.Color.BROWN;
            case BROWN -> Horse.Color.GRAY;
            case GRAY -> Horse.Color.WHITE;
            case WHITE -> Horse.Color.CHESTNUT;
            case CHESTNUT -> Horse.Color.CREAMY;
            case CREAMY -> Horse.Color.DARK_BROWN;
            case DARK_BROWN -> Horse.Color.BLACK;
        };
    }

    private Horse.Style getNextHorseStyle(Horse.Style currentType) {
        return switch (currentType) {
            case NONE -> Horse.Style.BLACK_DOTS;
            case BLACK_DOTS -> Horse.Style.WHITE_DOTS;
            case WHITE_DOTS -> Horse.Style.WHITE;
            case WHITE -> Horse.Style.WHITEFIELD;
            case WHITEFIELD -> Horse.Style.NONE;
        };
    }

}

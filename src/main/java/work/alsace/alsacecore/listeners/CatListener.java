package work.alsace.alsacecore.listeners;

import org.bukkit.entity.Cat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class CatListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) { return; }
        if (player.getInventory().getItemInMainHand().getType().isAir() && player.hasPermission("alsace.protect.place")) {
            if (event.getRightClicked() instanceof Cat) {
                Cat cat = (Cat) event.getRightClicked();
                Cat.Type newType = getNextCatType(cat.getCatType());
                cat.setCatType(newType);
            }
        }
    }

    private Cat.Type getNextCatType(Cat.Type currentType) {
        return switch (currentType) {
            case BLACK -> Cat.Type.BRITISH_SHORTHAIR;
            case BRITISH_SHORTHAIR -> Cat.Type.CALICO;
            case CALICO -> Cat.Type.JELLIE;
            case JELLIE -> Cat.Type.PERSIAN;
            case PERSIAN -> Cat.Type.RAGDOLL;
            case RAGDOLL -> Cat.Type.RED;
            case RED -> Cat.Type.SIAMESE;
            case SIAMESE -> Cat.Type.TABBY;
            case TABBY -> Cat.Type.ALL_BLACK;
            case ALL_BLACK -> Cat.Type.WHITE;
            case WHITE -> Cat.Type.BLACK;
        };
    }
}

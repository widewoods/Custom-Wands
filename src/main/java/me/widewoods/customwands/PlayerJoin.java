package me.widewoods.customwands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    final CustomWands plugin;
    public PlayerJoin(CustomWands pl){
        this.plugin = pl;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
       //Mana.manaManager.hideBossBar(event.getPlayer().getUniqueId());
        Mana.manaManager.createManaBar(event.getPlayer().getUniqueId());
        plugin.setWandUsers(event.getPlayer());
    }
}

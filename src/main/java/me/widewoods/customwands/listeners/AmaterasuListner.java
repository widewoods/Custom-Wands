package me.widewoods.customwands.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class AmaterasuListner implements Listener {
    @EventHandler
    public void onPlayerUseEnderEye(PlayerInteractEvent event){
        if(event.getItem() == null) return;
        if(!event.getItem().getItemMeta().hasLore()) return;
        if(PlainTextComponentSerializer.plainText().serialize(event.getItem().getItemMeta().lore().get(0)).equals("Amaterasu")){
            event.setCancelled(true);
            Bukkit.getLogger().info("listner");
        }
    }
}

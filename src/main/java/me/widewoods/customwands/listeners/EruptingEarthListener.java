package me.widewoods.customwands.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EruptingEarthListener implements Listener {

    @EventHandler
    public void onFallingBlockLand(ProjectileHitEvent event){
        if(event.getEntity().getType() != EntityType.SNOWBALL) return;

        if(event.getEntity().hasMetadata("EruptingEarth")){
            if(event.getHitEntity() != null && event.getHitEntity().getType() == EntityType.FALLING_BLOCK){
                event.setCancelled(true);
            }
            if(event.getHitBlock() != null){
                Entity e = event.getEntity();
                e.getWorld().createExplosion(event.getHitBlock().getLocation(), 3);
            }
        }
    }
}

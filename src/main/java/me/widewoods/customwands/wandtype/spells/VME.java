package me.widewoods.customwands.wandtype.spells;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.CastWand;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class VME extends CastWand {
    public VME(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
        setPowerCoefficient(12);
    }


    static HashMap<UUID, Location> latchLocation = new HashMap<>();
    int runnable = 0;
    @Override
    public void castMagicEffect() {
        Block lookingAt = wandUser.getTargetBlock(null, this.power);
        if(lookingAt.getType() != Material.AIR){
            Location targetLocation = lookingAt.getLocation();
            if(latchLocation.containsKey(wandUser)){
                latchLocation.replace(wandUser.getUniqueId(), targetLocation);
            } else{
                latchLocation.put(wandUser.getUniqueId(), targetLocation);
            }

            this.runnable = new BukkitRunnable(){
                float acceleration = 1.2f;
                @Override
                public void run(){
                    wandUser.playSound(wandUser, Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 1 ,acceleration/5f);
                    Vector dir = latchLocation.get(wandUser.getUniqueId()).toVector().subtract(wandUser.getLocation().toVector());
                    Vector line = dir.clone();
                    Location particleSpawnLocation = wandUser.getLocation();
                    for(int i=0; i<Math.floor(dir.clone().length()); i++){
                        particleSpawnLocation.add(line.normalize());
                        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 255, 255), 0.5F);
                        wandUser.getWorld().spawnParticle(Particle.REDSTONE, particleSpawnLocation, 1, 0, 0, 0, 0.1f, dustOptions);
                    }
                    wandUser.setVelocity(dir.normalize().multiply(acceleration));
                    if(wandUser.getLocation().toVector().distanceSquared(latchLocation.get(wandUser.getUniqueId()).toVector()) <= 5){
                        cancel();
                    }
                    acceleration += 0.05f;
                }
            }.runTaskTimer(plugin, 0, 1).getTaskId();
        }
    }
}

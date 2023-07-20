package me.widewoods.customwands.wandtype.spells;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.CastWand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
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

            new BukkitRunnable(){
                @Override
                public void run(){
                    Vector dir = latchLocation.get(wandUser.getUniqueId()).toVector().subtract(wandUser.getLocation().toVector());
                    Vector line = dir.clone();
                    Location particleSpawnLocation = wandUser.getLocation();
                    for(int i=0; i<Math.floor(dir.clone().length()); i++){
                        particleSpawnLocation.add(line.normalize());
                        wandUser.getWorld().spawnParticle(Particle.CLOUD, particleSpawnLocation, 1, 0, 0, 0, 0.1f);
                    }
                    wandUser.setVelocity(dir.normalize().multiply(1.1f));
                    if(wandUser.getLocation().toVector().distanceSquared(latchLocation.get(wandUser.getUniqueId()).toVector()) <= 5){
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }
}

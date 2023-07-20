package me.widewoods.customwands.wandtype.spells;

import io.papermc.paper.entity.LookAnchor;
import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.CastWand;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Shunpo extends CastWand {
    public Shunpo(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
        setManaCost(1);
    }

    @Override
    public void castMagicEffect() {
        ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) wandUser.getWorld().getNearbyEntities(wandUser.getLocation(), 30, 10, 30);

        //Change to infinity?
        double minDistance = 1000000;
        Entity minDistEntity = null;
        Vector userLoc = wandUser.getLocation().toVector();
        for(Entity entity: nearbyEntities){
            if(entity == wandUser){
                continue;
            }
            Vector entityLoc = entity.getLocation().toVector();
            double distanceSquared = entityLoc.distanceSquared(userLoc);
            if(distanceSquared < minDistance){
                minDistance = distanceSquared;
                minDistEntity = entity;
            }
        }

        if(minDistEntity == null){
            return;
        } else if(minDistEntity instanceof LivingEntity){
            LivingEntity e = (LivingEntity) minDistEntity;
            wandUser.getWorld().spawnParticle(Particle.CLOUD, wandUser.getLocation(), 20, 0, 0, 0, 0.1f);
            wandUser.teleport(e.getLocation().add(e.getEyeLocation().getDirection().normalize().multiply(-1.2f).setY(0)));
            wandUser.getWorld().spawnParticle(Particle.CLOUD, wandUser.getLocation(), 20, 0, 0, 0, 0.1f);

        } else {
            Location tp = minDistEntity.getLocation();
            tp.setYaw(wandUser.getLocation().getYaw());
            tp.setPitch(wandUser.getLocation().getPitch());

            wandUser.getWorld().spawnParticle(Particle.CLOUD, wandUser.getLocation(), 20, 0, 0, 0, 0.1f);
            wandUser.teleport(tp);
            wandUser.getWorld().spawnParticle(Particle.CLOUD, wandUser.getLocation(), 20, 0, 0, 0, 0.1f);
        }

    }
}

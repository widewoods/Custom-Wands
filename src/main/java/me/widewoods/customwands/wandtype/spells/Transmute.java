package me.widewoods.customwands.wandtype.spells;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.ProjectileWand;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class Transmute extends ProjectileWand {
    public Transmute(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
        this.setParticle(Particle.SOUL, 2, 0.01f);
    }

    @Override
    public void projectileMagicEffect(Entity hitEntity, Block hitBlock) {
        if(hitEntity != null){
            if(hitEntity instanceof Player){
                return;
            }
            if(hitEntity instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) hitEntity;
                EntityType randomMob = CustomWands.mobs[ThreadLocalRandom.current().nextInt(CustomWands.mobs.length)];
                entity.getWorld().spawnEntity(entity.getLocation(), randomMob);
                entity.remove();
            }
        }
    }
}

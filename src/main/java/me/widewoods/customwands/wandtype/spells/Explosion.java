package me.widewoods.customwands.wandtype.spells;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.ProjectileWand;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public class Explosion extends ProjectileWand {
    public Explosion(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
        this.setParticle(Particle.FLAME, 4, 0.08f);
    }

    @Override
    public void projectileMagicEffect(Entity hitEntity, Block hitBlock) {
        if(hitEntity != null){
            hitEntity.getWorld().createExplosion(hitEntity.getLocation(), this.power);
        }
        if(hitBlock != null){
            hitBlock.getWorld().createExplosion(hitBlock.getLocation(), this.power);
        }
    }
}

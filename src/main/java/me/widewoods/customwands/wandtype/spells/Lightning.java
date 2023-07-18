package me.widewoods.customwands.wandtype.spells;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.ProjectileWand;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public class Lightning extends ProjectileWand {
    public Lightning(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
        this.setParticle(Particle.ELECTRIC_SPARK, 7, 0.7f);
    }

    @Override
    public void projectileMagicEffect(Entity hitEntity, Block hitBlock) {
        if(hitEntity != null){
            hitEntity.getWorld().strikeLightning(hitEntity.getLocation());
        }
        if(hitBlock != null){
            Location location = hitBlock.getLocation();
            location.add(0, 1, 0);
            hitBlock.getWorld().strikeLightning(location);
        }
    }
}

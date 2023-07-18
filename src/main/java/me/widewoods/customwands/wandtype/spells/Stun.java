package me.widewoods.customwands.wandtype.spells;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.ProjectileWand;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Stun extends ProjectileWand {
    public Stun( String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
        this.setParticle(Particle.SLIME, 7, 0.06f);
    }

    @Override
    public void projectileMagicEffect(Entity hitEntity, Block hitBlock) {
        if(hitEntity != null){
            if(hitEntity instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) hitEntity;
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, this.power, 10));
                entity.setVelocity(new Vector(0, 0, 0));
            }
        }
    }
}

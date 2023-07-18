package me.widewoods.customwands.wandtype.spells;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.ProjectileWand;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class Damage extends ProjectileWand {
    public Damage(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
        this.setParticle(Particle.SOUL_FIRE_FLAME, 4, 0.08f);
    }

    @Override
    public void projectileMagicEffect(Entity hitEntity, Block hitBlock) {
        if(hitEntity != null){
            if(hitEntity instanceof LivingEntity){
                //생명체 데미지 입히려면 LivingEntity 로 바꿔서 해야함
                LivingEntity entity = (LivingEntity) hitEntity;
                double damage = this.power;
                entity.damage(damage);
                entity.getWorld().spawnParticle(Particle.CRIT_MAGIC, entity.getEyeLocation(),
                        30, 0, 0, 0,0.5);
                entity.getWorld().playSound(entity, Sound.ENTITY_PLAYER_HURT, 2, 1);
            }
        }
    }
}

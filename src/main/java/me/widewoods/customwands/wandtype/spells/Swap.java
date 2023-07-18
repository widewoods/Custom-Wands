package me.widewoods.customwands.wandtype.spells;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.CastWand;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class Swap extends CastWand {

    public Swap(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
        setPowerCoefficient(12);
    }

    @Override
    public void castMagicEffect() {
        Entity targetEntity = wandUser.getTargetEntity(this.power);
        if(targetEntity instanceof LivingEntity){
            Location self = wandUser.getLocation();
            Location target = targetEntity.getLocation();
            float selfPitch = target.getPitch();
            float selfYaw = target.getYaw();
            float targetYaw = self.getYaw();
            float targetPitch = self.getPitch();
            self.setPitch(selfPitch);
            self.setYaw(selfYaw);
            target.setYaw(targetYaw);
            target.setPitch(targetPitch);

            targetEntity.teleport(self);
            wandUser.teleport(target);
        }
    }
}

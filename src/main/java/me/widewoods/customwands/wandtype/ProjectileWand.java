package me.widewoods.customwands.wandtype;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.Wand;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;

public abstract class ProjectileWand extends Wand {

    public ProjectileWand(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
    }

    public abstract void projectileMagicEffect(Entity hitEntity, Block hitBlock);

    @Override
    public void useMagic(Player user) {
        user.getWorld().playSound(user, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1.4f);
        Snowball snowball = user.launchProjectile(Snowball.class);

        snowball.setVelocity(snowball.getVelocity().multiply(this.speed));
        snowball.setGravity(false);
        snowball.setMetadata("Effect", new FixedMetadataValue(plugin, this.name));
        snowball.setVisibleByDefault(false);

        if(particle != null){
            final int particleSpawn = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if(snowball.isValid()){
                        snowball.getWorld().spawnParticle(particle, snowball.getLocation(), particleCount, 0, 0, 0, particleSpeed);
                    }
                }
            }, 2, 1);

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Bukkit.getScheduler().cancelTask(particleSpawn);
                    snowball.remove();
                }
            }, 200);
        }
    }
}

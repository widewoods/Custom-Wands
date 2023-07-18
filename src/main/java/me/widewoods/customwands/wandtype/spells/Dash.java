package me.widewoods.customwands.wandtype.spells;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.CastWand;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class Dash extends CastWand {

    public Dash(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
        this.setParticle(Particle.CLOUD, 5, 0.04f);
    }
    @Override
    public void castMagicEffect(){
        Vector dir = wandUser.getEyeLocation().getDirection();
        wandUser.setVelocity(dir.multiply(this.power));
    }

    @Override
    public void useMagic(Player user) {
        castMagicEffect();
        user.getWorld().playSound(user, Sound.BLOCK_AMETHYST_BLOCK_STEP, 4, 1);
        Snowball snowball = user.launchProjectile(Snowball.class);

        snowball.setVelocity(snowball.getVelocity().multiply(this.speed));
        snowball.setGravity(false);
        snowball.setMetadata("Effect", new FixedMetadataValue(plugin, this.name));
        snowball.setVisibleByDefault(false);

        if (particle != null) {
            final int particleSpawn = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    snowball.teleport(user.getLocation());
                    snowball.setVelocity(user.getVelocity());
                    if (snowball.isValid()) {
                        snowball.getWorld().spawnParticle(particle, snowball.getLocation(), particleCount, 0, 0, 0, particleSpeed);
                    }
                }
            }, 2, 2);

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Bukkit.getScheduler().cancelTask(particleSpawn);
                    snowball.remove();
                }
            }, 150);
        }
    }
}

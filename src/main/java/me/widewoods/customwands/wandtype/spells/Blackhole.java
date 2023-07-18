package me.widewoods.customwands.wandtype.spells;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.ProjectileWand;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class Blackhole extends ProjectileWand{
    public Blackhole(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
        this.setParticle(Particle.DRAGON_BREATH, 40, 0.1f);
    }

    @Override
    public void projectileMagicEffect(Entity hitEntity, Block hitBlock) {

    }

    @Override
    public void useMagic(Player user){
        user.getWorld().playSound(user, Sound.ENTITY_WARDEN_SONIC_BOOM, 0.4f, 1.4f);
        Snowball snowball = user.launchProjectile(Snowball.class);

        this.speed = 0.3f;
        snowball.setVelocity(snowball.getVelocity().multiply(this.speed));
        snowball.setGravity(false);
        snowball.setMetadata("Effect", new FixedMetadataValue(plugin, this.name));
        snowball.setVisibleByDefault(false);
        snowball.setMetadata("Uncollidable", new FixedMetadataValue(plugin, this.name));

        if(particle != null){
            final int particleSpawn = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if(snowball.isValid()){
                        snowball.getWorld().spawnParticle(particle, snowball.getLocation(), particleCount, 0, 0, 0, particleSpeed);
                        snowball.getWorld().playSound(snowball.getLocation(), Sound.ENTITY_WARDEN_TENDRIL_CLICKS, 0.4f, 0.5f);
                        Block center = snowball.getLocation().getBlock();
                        for(Block b:getSurroundingBlocks(center)){
                            if(b.getType() != Material.AIR){
                                b.setType(Material.AIR);
                            }
                        }
                        for(LivingEntity e:center.getLocation().getNearbyLivingEntities(power)){
                            if(!e.equals(user)){
                                Vector dir = center.getLocation().toVector().subtract(e.getLocation().toVector());
                                e.setVelocity(dir.normalize());
                                e.damage(0.5f);
                            }
                        }
                    }
                }
            }, 2, 3);

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Bukkit.getScheduler().cancelTask(particleSpawn);
                    snowball.remove();
                }
            }, 100);
        }
    }

    private Block[] getSurroundingBlocks(Block c){
        int i = 0;
        int p = this.power;
        int radius = 2*p + 1;
        Block[] surrounding = new Block[radius*radius*radius];
        for(int x=-p;x<=p;x++){
            for(int y=-p;y<=p;y++){
                for(int z=-p;z<=p;z++){
                    surrounding[i] = c.getRelative(x, y, z);
                    i++;
                }
            }
        }

        return surrounding;
    }
}

package me.widewoods.customwands.wandtype.spells;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.ProjectileWand;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class ShinraTensei extends ProjectileWand {
    public ShinraTensei(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
    }


    @Override
    public void projectileMagicEffect(Entity hitEntity, Block hitBlock) {
        if(hitBlock != null){
            Block center = hitBlock.getRelative(0, 1, 0);
            World world = center.getWorld();
            int vectorSize = 6;
            Vector v = new Vector(vectorSize, 0, 0);

            center.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, center.getLocation(), 20, 2, 2, 2);
            wandUser.playSound(wandUser, Sound.ENTITY_WITHER_SHOOT, 0.7f, 0.5f);
            for(int i=0; i<power; i++){
                int finalI = i;
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    int I = finalI;
                    @Override
                    public void run() {
                        for (int j = 0; j < 10*I; j++) {
                            Vector dist = v.clone().multiply(I);
                            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(105, 81, 73), 6.0F);

                            Location effectLoc = center.getLocation().clone().add(dist.clone().rotateAroundY(Math.PI / (5*I) * j));
                            world.spawnParticle(Particle.REDSTONE,
                                    effectLoc,
                                    15, 3, 3, 3, 2, dustOptions, true);
                            for(Block b: getSurroundingBlocks(effectLoc.getBlock())){
                                if(b.getType() != Material.AIR){
                                    if(Math.random() > 0.96){
                                        FallingBlock fallingBlock = b.getWorld().spawnFallingBlock(b.getLocation(), b.getBlockData());
                                        Vector dir = center.getLocation().toVector().subtract(b.getLocation().toVector());
                                        dir.normalize().multiply(-1.7);
                                        dir.setY(1.3);
                                        fallingBlock.setVelocity(dir);
                                        fallingBlock.setCancelDrop(true);
                                    }
                                    b.setType(Material.AIR);
                                }
                            }
                            effectLoc.getWorld().playSound(effectLoc, Sound.ENTITY_WITHER_SHOOT, 0.2f, 0.5f);
                        }
                        for(LivingEntity e : center.getLocation().getNearbyLivingEntities(vectorSize * I)){
                            if(e == wandUser) continue;
                            Vector dir = center.getLocation().toVector().subtract(e.getLocation().toVector());
                            if(Math.abs(dir.getY()) >= 6) continue;
                            e.setVelocity(dir.normalize().multiply(-2.4));
                        }
                    }
                }, i*2);
            }
        }
    }
    public Block[] getSurroundingBlocks(Block c){
        int i = 0;
        int p = 3;
        int radius = 2*p + 1;
        Block[] surrounding = new Block[radius*6*radius];
        for(int x=-p;x<=p;x++){
            for(int y=0;y<=5;y++){
                for(int z=-p;z<=p;z++){
                    surrounding[i] = c.getRelative(x, y, z);
                    i++;
                }
            }
        }

        return surrounding;
    }
}

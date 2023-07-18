package me.widewoods.customwands.wandtype.spells;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.ProjectileWand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class EruptingEarth extends ProjectileWand {
    public EruptingEarth(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
        this.setParticle(Particle.CRIT, 5, 0.4f);
        this.setManaCost(1);
    }

    @Override
    public void projectileMagicEffect(Entity hitEntity, Block hitBlock) {
        if(hitBlock != null){
            World w = hitBlock.getWorld();
            for(Block b: getSurroundingBlocks(hitBlock)){
                if(b != null && b.getType() != Material.AIR && Math.random() > 0.6){

                    FallingBlock erupt = w.spawnFallingBlock(b.getLocation().add(0, 1, 0), b.getBlockData());

                    double randomX = (Math.random() -0.5);
                    double randomZ = (Math.random() -0.5);

                    erupt.setVelocity(new Vector(randomX, 1, randomZ));
                    erupt.setHurtEntities(true);
                    erupt.setDropItem(false);
                    erupt.setCancelDrop(true);

                    b.setType(Material.AIR);

                    Snowball snowball = (Snowball) w.spawnEntity(b.getLocation(), EntityType.SNOWBALL);

                    snowball.setGravity(false);
                    snowball.setMetadata("EruptingEarth", new FixedMetadataValue(this.plugin, this.name));
                    snowball.setVisibleByDefault(false);

                    if(particle != null){
                        final int particleSpawn = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                if(snowball.isValid()){
                                    snowball.teleport(erupt.getLocation().add(erupt.getVelocity().normalize()));
                                    snowball.setVelocity(erupt.getVelocity());
                                } else{
                                    erupt.remove();
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
        }
    }

    private Block[] getSurroundingBlocks(Block c){
        int i = 0;
        int radius = 2*this.power + 1;
        Block[] surrounding = new Block[radius*radius*radius];
        for(int x=-this.power; x<=this.power; x++){
            for(int y=-1; y<=0; y++){
                for(int z=-this.power; z<=this.power; z++){
                    surrounding[i] = c.getRelative(x, y, z);
                    i++;
                }
            }
        }

        return surrounding;
    }
}

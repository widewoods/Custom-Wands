package me.widewoods.customwands.wandtype.spells;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.CastWand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChibakuTensei extends CastWand {
    public ChibakuTensei(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
    }

    float sound = 0.4f;
    float soundPitch = 0.5f;

    Material[] stoneTypes = {
            Material.STONE,
            Material.COBBLESTONE,
            Material.ANDESITE,
            Material.COARSE_DIRT,
            Material.MOSSY_COBBLESTONE,
            Material.TUFF,

    };
    @Override
    public void castMagicEffect() {
        Entity entity = wandUser.getTargetEntity(50);
        if(entity instanceof LivingEntity target){
            Location loc = target.getLocation();
            if(loc.getY() <= target.getWorld().getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()).getY()){
                return;
            }
            int finalPower = power;
            new BukkitRunnable(){
                int runnablePower = finalPower;
                int tick = 0;
                Location targetLocation;
                Block[] ground;

                HashMap<ArmorStand, Location> armorStandLocations = new HashMap<>();
                HashMap<ArmorStand, Location> lastArmorStandLocation = new HashMap<>();

                @Override
                public void run(){
                    int riseTime = 20 * runnablePower/3;
                    int summonRockTime = (13 * runnablePower) + 20;
                    if (tick == riseTime) {
                        targetLocation = target.getLocation();
                        ground = getGround(targetLocation, runnablePower);
                        target.setGravity(false);
                        target.setVelocity(new Vector(0, 0, 0));
                    }
                    if(tick < riseTime){
                        target.setVelocity(new Vector(0, 0.7f, 0));
                    } else if (tick <= riseTime + summonRockTime) {
                        for(int count=0; count <power/5 + 1 + (tick-riseTime)/30; count++){
                            target.setVelocity(new Vector(0, 0, 0));
                            Random random = new Random();
                            Location randomSpawnLoc = ground[random.nextInt(ground.length)].getLocation();

                            ArmorStand armorStand = target.getWorld().spawn(randomSpawnLoc.add(0, 2, 0), ArmorStand.class);
                            armorStand.setVisible(false);

                            Vector randomVector = new Vector(Math.random()-0.5f, Math.random()-0.5f, Math.random()-0.5f);
                            Location armorStandTargetLoc = targetLocation.clone().add(randomVector.multiply((tick-riseTime)/8 + 1));
                            armorStandLocations.put(armorStand, armorStandTargetLoc);
                            lastArmorStandLocation.put(armorStand, new Location(armorStand.getWorld(), 0, 10000, 0));

                            armorStand.setItem(EquipmentSlot.HEAD, new ItemStack(stoneTypes[random.nextInt(stoneTypes.length)]));
                            armorStandFinishFlight(armorStand, armorStandTargetLoc);
                        }

                    } else{
                        target.setGravity(true);
                        for(Map.Entry<ArmorStand, Location> mappedLoc: armorStandLocations.entrySet()){
                            ArmorStand as = mappedLoc.getKey();
                            armorStandFinishFlight(as, mappedLoc.getValue());
                        }
                        cancel();
                    }
                    tick++;
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }

    void armorStandFinishFlight(ArmorStand armorStand, Location destination){
        new BukkitRunnable(){
            Location lastArmorStandLocation = new Location(armorStand.getWorld(), 0, 10000, 0);
            @Override
            public void run(){
                Location armorStandLocation =  armorStand.getLocation();
                if(armorStandLocation.distance(destination) >= 2){
                    Vector direction = destination.toVector().subtract(armorStandLocation.toVector());
                    armorStand.setVelocity(direction.normalize().multiply(2.5));
                    if(lastArmorStandLocation.distance(armorStandLocation) <= 0.4f){
                        armorStand.teleport(destination);
                        armorStand.remove();
                        wandUser.getWorld().playSound(armorStand.getLocation(), Sound.BLOCK_STONE_PLACE, sound, soundPitch);
                        wandUser.playSound(wandUser, Sound.BLOCK_STONE_PLACE, sound, soundPitch);
                        cancel();
                    }
                    lastArmorStandLocation = armorStandLocation;
                } else{
                    armorStand.setVelocity(new Vector(0, 0 ,0));
                    for(Block b:getNearbyBlocks(armorStandLocation)){
                        if(b.getType() == Material.AIR){
                            Random rand = new Random();
                            b.setType(stoneTypes[rand.nextInt(stoneTypes.length)]);
                        }
                    }
                    armorStand.remove();
                    wandUser.getWorld().playSound(armorStand.getLocation(), Sound.BLOCK_STONE_PLACE, sound, soundPitch);
                    wandUser.playSound(wandUser, Sound.BLOCK_STONE_PLACE, sound, soundPitch);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 1, 1);
    }

    private Block[] getGround(Location targetLoc, int power){
        int radius = power * 5;
        int blockCount = (2*radius+1) * (2*radius+1);
        Block[] ground = new Block[blockCount];
        int i = 0;
        for(int x= targetLoc.getBlockX()-radius; x<=targetLoc.getBlockX()+radius; x++){
            for(int z=targetLoc.getBlockZ()-radius; z<=targetLoc.getBlockZ()+radius; z++){
                ground[i] = targetLoc.getWorld().getHighestBlockAt(x, z);
                i++;
            }
        }

        return ground;
    }

    /* Get blocks in a sphere like shape
       Have to find a cleaner code for this..

     */
    private Block[] getNearbyBlocks(Location loc){
        Block center = loc.getBlock();
        Block[] nearby = new Block[57];
        int i = 0;
        for(int x=-1; x<=1; x++){
            for(int y=-1; y<=1; y++){
                for(int z=-1; z<=1; z++){
                    nearby[i] = center.getRelative(x, y, z);
                    i++;
                }
            }
        }

        for(int j=-1; j<=1; j++){
            for(int k=-1; k<=1; k++){
                int i1 = Math.abs(j) + Math.abs(k);
                if(i1 == 0 || i1 == 1){
                    nearby[i] = center.getRelative(2, k, j);
                    i++;
                    nearby[i] = center.getRelative(-2, k, j);
                    i++;
                    nearby[i] = center.getRelative(k, 2, j);
                    i++;
                    nearby[i] = center.getRelative(k, -2, j);
                    i++;
                    nearby[i] = center.getRelative(k, j, 2);
                    i++;
                    nearby[i] = center.getRelative(k, j, -2);
                    i++;
                }
            }
        }



        return nearby;
    }
}

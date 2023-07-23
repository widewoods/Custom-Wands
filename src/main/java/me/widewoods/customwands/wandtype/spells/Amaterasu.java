package me.widewoods.customwands.wandtype.spells;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.CastWand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class Amaterasu extends CastWand {

    public Amaterasu(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
    }

    @Override
    public void castMagicEffect() {
        HashMap<Location, BlockData> originalData = new HashMap<>();
        final int amaterasu = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                Block b = wandUser.getTargetBlock(null, 50);
                if(b != null){
                    for(Block surrounding : getSurroundingBlocks(b)){
                        if(surrounding.getType() != Material.SOUL_FIRE && surrounding.getType() != Material.AIR && surrounding.getType() != Material.SOUL_SAND){
                            Block target = surrounding.getRelative(0, 1 ,0);
                            originalData.put(surrounding.getLocation(), surrounding.getBlockData());
                            surrounding.setType(Material.SOUL_SAND);
                            target.setType(Material.SOUL_FIRE);
                        }
                    }
                }
                Entity e = wandUser.getTargetEntity(50);
                if(e != null) {
                    if (e instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) e;
                        livingEntity.setFireTicks(20);
                    }
                }
            }
        }, 0, 1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().cancelTask(amaterasu);
                for(Map.Entry<Location, BlockData> set : originalData.entrySet()){
                    set.getKey().getBlock().setBlockData(set.getValue());
                }
            }
        }, 200);
        wandUser.setCooldown(Material.ENDER_EYE, 200);
    }

    private Block[] getSurroundingBlocks(Block c){
        int i = 0;
        int p = 1;
        Block[] surrounding = new Block[5];
        surrounding[0] = c;
        surrounding[1] = c.getRelative(0, 0, 1);
        surrounding[2] = c.getRelative(1, 0, 0);
        surrounding[3] = c.getRelative(-1, 0, 0);
        surrounding[4] = c.getRelative(0, 0, -1);

        return surrounding;
    }
}

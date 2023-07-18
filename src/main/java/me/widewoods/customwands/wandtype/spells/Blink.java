package me.widewoods.customwands.wandtype.spells;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.wandtype.CastWand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class Blink extends CastWand {
    public Blink(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
        setPowerCoefficient(12);
    }

    @Override
    public void castMagicEffect() {
        Block lookingAt = wandUser.getTargetBlock(null, this.power);
        if(lookingAt.getType() != Material.AIR){
            Location targetLocation = lookingAt.getLocation();
            targetLocation.setYaw(wandUser.getLocation().getYaw());
            targetLocation.setPitch(wandUser.getLocation().getPitch());
            Vector offset = targetLocation.toVector().subtract(wandUser.getLocation().toVector());
            targetLocation.add(offset.normalize().multiply(-1.5f));
            wandUser.teleport(targetLocation);
        }
    }
}

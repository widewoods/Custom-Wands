package me.widewoods.customwands.wandtype;

import me.widewoods.customwands.CustomWands;
import me.widewoods.customwands.Wand;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public abstract class CastWand extends Wand {

    public CastWand(String name, float speed, int power, CustomWands pl) {
        super(name, speed, power, pl);
    }

    public abstract void castMagicEffect();

    @Override
    public void useMagic(Player user) {
        user.getWorld().playSound(user, Sound.BLOCK_AMETHYST_BLOCK_STEP, 4, 1);
        castMagicEffect();
    }
}

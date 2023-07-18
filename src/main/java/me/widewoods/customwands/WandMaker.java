package me.widewoods.customwands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WandMaker implements CommandExecutor, TabExecutor {


    static HashMap<String, String> spells = new HashMap<>();
    static {
        spells.put("damage", "Damage");
        spells.put("explosion", "Explosion");
        spells.put("transmute", "Transmute");
        spells.put("lightning", "Lightning");
        spells.put("stun", "Stun");
        spells.put("dash", "Dash");
        spells.put("blink", "Blink");
        spells.put("swap", "Swap");
        spells.put("blackhole", "Blackhole");
        spells.put("eruptingearth", "Erupting Earth");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            if(strings.length == 0){
                for(Map.Entry<String, String> set: spells.entrySet()){
                    ItemStack wand = new ItemStack(Material.STICK);
                    ItemMeta wandMeta = wand.getItemMeta();
                    final Component displayName;

                    final ArrayList<Component> lore = new ArrayList<>();
                    Component text = Component.text(set.getValue())
                            .decoration(TextDecoration.BOLD, true)
                            .decoration(TextDecoration.ITALIC, false)
                            .color(TextColor.color(0x9542f5));

                    Component loreText = Component.text(set.getValue())
                            .decoration(TextDecoration.OBFUSCATED, true)
                            .decoration(TextDecoration.ITALIC, false)
                            .color(TextColor.color(0xF50100));;

                    displayName = text;
                    lore.add(loreText);
                    wandMeta.lore(lore);
                    wandMeta.displayName(displayName);
                    wandMeta.addEnchant(Enchantment.ARROW_DAMAGE, 3, true);

                    wand.setItemMeta(wandMeta);

                    player.getInventory().addItem(wand);
                }
                return true;
            } else if(strings.length != 2){
                return false;
            }

            ItemStack wand = new ItemStack(Material.STICK);
            ItemMeta wandMeta = wand.getItemMeta();
            final Component displayName;
            final ArrayList<Component> lore = new ArrayList<>();
            Component name = null;
            Component loreText = null;
            if(spells.containsKey(strings[0])){
                name = Component.text(spells.get(strings[0]))
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.ITALIC, false)
                        .color(TextColor.color(0x9542f5));
                loreText = Component.text(spells.get(strings[0]))
                        .decoration(TextDecoration.OBFUSCATED, true)
                        .decoration(TextDecoration.ITALIC, false)
                        .color(TextColor.color(0xF50100));;
            } else{
                return false;
            }
            displayName = name;
            lore.add(loreText);
            wandMeta.lore(lore);
            wandMeta.displayName(displayName);
            wandMeta.addEnchant(Enchantment.ARROW_DAMAGE, Integer.parseInt(strings[1]), true);
            wandMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);

            wand.setItemMeta(wandMeta);

            player.getInventory().addItem(wand);


        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 0){
            return null;
        } else if(strings.length == 1){
            ArrayList<String> spellNames = new ArrayList<>();
            for(Map.Entry<String, String> set: spells.entrySet()){
                spellNames.add(set.getKey());
            }
            return spellNames;
        } else if(strings.length == 2){
            ArrayList<String> power = new ArrayList<>();
            power.add("<power>");
            return power;
        }
        return null;
    }
}

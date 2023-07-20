package me.widewoods.customwands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
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

import javax.annotation.WillNotClose;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WandMaker implements CommandExecutor, TabExecutor {

    static ArrayList<WandItem> wands = new ArrayList<>();
    static {
        wands.add(new WandItem("damage", "Damage", Material.STICK));
        wands.add(new WandItem("explosion", "Explosion", Material.STICK));
        wands.add(new WandItem("transmute", "Transmute", Material.STICK));
        wands.add(new WandItem("lightning", "Lightning", Material.STICK));
        wands.add(new WandItem("stun", "Stun", Material.STICK));
        wands.add(new WandItem("dash", "Dash", Material.STICK));
        wands.add(new WandItem("blink", "Blink", Material.AMETHYST_SHARD));
        wands.add(new WandItem("swap", "Swap", Material.STICK));
        wands.add(new WandItem("blackhole", "Blackhole", Material.STICK));
        wands.add(new WandItem("eruptingearth", "Erupting Earth", Material.STICK));
        wands.add(new WandItem("shunpo", "Shunpo", Material.GOLDEN_SWORD));
        wands.add(new WandItem("phalanx", "Phalanx", Material.TRIDENT));
        wands.add(new WandItem("shinratensei", "Shinra Tensei", Material.HEART_OF_THE_SEA));
        wands.add(new WandItem("amaterasu", "Amaterasu", Material.ENDER_EYE));
        wands.add(new WandItem("VME", "Vertical Manuevering Equipment", Material.TRIPWIRE_HOOK));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            if(strings.length == 0){
                for(WandItem wandItem: wands){
                    ItemStack wand = new ItemStack(wandItem.wandMaterial);
                    ItemMeta wandMeta = wand.getItemMeta();
                    final Component displayName;

                    final ArrayList<Component> lore = new ArrayList<>();
                    Component text = Component.text(wandItem.displayName)
                            .decoration(TextDecoration.BOLD, true)
                            .decoration(TextDecoration.ITALIC, false)
                            .color(TextColor.color(0x9542f5));

                    Component loreText = Component.text(wandItem.displayName)
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

            WandItem wandItem = null;
            for(WandItem w: wands){
                Bukkit.getLogger().info("iterating");
                if(strings[0].equals(w.commandName)){
                    wandItem = w;
                    Bukkit.getLogger().info("set wand");
                    break;
                }
            }
            if(wandItem == null){
                return false;
            }

            ItemStack wand = new ItemStack(wandItem.wandMaterial);

            ItemMeta wandMeta = wand.getItemMeta();
            final Component displayName;
            final ArrayList<Component> lore = new ArrayList<>();
            Component name = null;
            Component loreText = null;
            name = Component.text(wandItem.displayName)
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.ITALIC, false)
                        .color(TextColor.color(0x9542f5));
            loreText = Component.text(wandItem.displayName)
                        .decoration(TextDecoration.OBFUSCATED, true)
                        .decoration(TextDecoration.ITALIC, false)
                        .color(TextColor.color(0xF50100));;

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
            for(WandItem w: wands){
                spellNames.add(w.commandName);
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

class WandItem{
    String commandName;
    String displayName;
    Material wandMaterial;
    public WandItem(String commandName, String displayName, Material wandMaterial){
        this.commandName = commandName;
        this.displayName = displayName;
        this.wandMaterial = wandMaterial;
    }
}

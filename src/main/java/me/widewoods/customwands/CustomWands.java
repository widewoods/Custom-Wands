package me.widewoods.customwands;

import me.widewoods.customwands.listeners.BlackholeListener;
import me.widewoods.customwands.listeners.EruptingEarthListener;
import me.widewoods.customwands.wandtype.CastWand;
import me.widewoods.customwands.wandtype.ProjectileWand;
import me.widewoods.customwands.wandtype.spells.*;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.*;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.UUID;


public final class CustomWands extends JavaPlugin implements Listener {

    // Wand 설정
    // Power = 0 이면 power 변수가 효과에 영향을 주지 않음
    ProjectileWand explosion = new Explosion("Explosion", 2f, 10, this);
    ProjectileWand damage = new Damage("Damage",2f, 10, this);
    ProjectileWand transmute = new Transmute( "Transmute", 2f, 0, this);
    ProjectileWand lightning = new Lightning( "Lightning", 2f, 0, this);
    ProjectileWand stun = new Stun("Stun",  2f, 20, this);
    ProjectileWand blackhole = new Blackhole("Blackhole", 2f, 10, this);
    ProjectileWand eruptingEarth = new EruptingEarth("Erupting Earth", 2f, 10, this);
    CastWand dash = new Dash( "Dash", 2f, 2, this);
    CastWand blink = new Blink( "Blink", 2f, 40, this);
    CastWand swap = new Swap("Swap", 2f, 100, this);

    CastWand shunpo = new Shunpo("Shunpo", 2f, 10, this);


    public static EntityType[] mobs = Arrays.stream(EntityType.values())
            .filter(type -> type.getEntityClass() != null && Mob.class.isAssignableFrom(type.getEntityClass()))
            .toArray(EntityType[]::new);

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new BlackholeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new EruptingEarthListener(), this);

        WandMaker wm = new WandMaker();
        this.getCommand("wand").setExecutor(wm);
        this.getCommand("wand").setTabCompleter(wm);


        if(Mana.manaManager == null){
            Mana.manaManager  = new Mana(this);
            Mana.manaManager.runOnEnable();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for(UUID uuid: Mana.players){
            Mana.manaManager.hideBossBar(uuid);
        }
    }

    //들고 있는 아이템의 ItemMeta lore 값을 보고 알맞는 Wand 선택 후 발사
    @EventHandler
    public void onPlayerUseWand(PlayerInteractEvent event){
        if(event.getItem() == null) return;

        if(!event.getItem().getItemMeta().hasLore()) return;

        if(!event.getAction().isRightClick()) return;

        Player player = event.getPlayer();
        String lore = PlainTextComponentSerializer.plainText().serialize(event.getItem().getItemMeta().lore().get(0));
        Wand wand = switch (lore){
            case "Damage" -> damage;
            case "Explosion" -> explosion;
            case "Transmute" -> transmute;
            case "Lightning" -> lightning;
            case "Stun" -> stun;
            case "Dash" -> dash;
            case "Blink" -> blink;
            case "Swap" -> swap;
            case "Blackhole" -> blackhole;
            case "Erupting Earth" -> eruptingEarth;
            case "Shunpo" -> shunpo;
            default -> null;
        };

        if(wand != null){
            wand.setWandUser(player);
            if(wand.useMana()){
                wand.setPower(event.getItem().getEnchantmentLevel(Enchantment.ARROW_DAMAGE));
                wand.useMagic(player);
            }
        } else{
            Bukkit.getLogger().warning("Invalid lore for wand");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileHit(ProjectileHitEvent event){
        if(event.getEntity().getType() != EntityType.SNOWBALL) return;


        if(event.getEntity().hasMetadata("Effect")){
            ProjectileWand wand = switch (event.getEntity().getMetadata("Effect").get(0).asString()){
                case "Damage" -> damage;
                case "Explosion" -> explosion;
                case "Transmute" -> transmute;
                case "Lightning" -> lightning;
                case "Stun" -> stun;
                case "Erupting Earth" -> eruptingEarth;
                default -> null;
            };
            if(wand != null){
                wand.projectileMagicEffect(event.getHitEntity(), event.getHitBlock());
                event.getEntity().remove();
            }
        }
    }
}

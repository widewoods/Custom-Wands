package me.widewoods.customwands;

import me.widewoods.customwands.listeners.AmaterasuListner;
import me.widewoods.customwands.listeners.BlackholeListener;
import me.widewoods.customwands.listeners.EruptingEarthListener;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;


public final class CustomWands extends JavaPlugin implements Listener {

    static HashMap<UUID, ArrayList<Wand>> playerWands = new HashMap<>();

    public static EntityType[] mobs = Arrays.stream(EntityType.values())
            .filter(type -> type.getEntityClass() != null && Mob.class.isAssignableFrom(type.getEntityClass()))
            .toArray(EntityType[]::new);

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new BlackholeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new EruptingEarthListener(), this);
        getServer().getPluginManager().registerEvents(new AmaterasuListner(), this);

        WandMaker wm = new WandMaker();
        this.getCommand("wand").setExecutor(wm);
        this.getCommand("wand").setTabCompleter(wm);


        if(Mana.manaManager == null){
            Mana.manaManager  = new Mana(this);
            Mana.manaManager.runOnEnable();
        }

        for(Player p: getServer().getOnlinePlayers()){
            setWandUsers(p);
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
        ArrayList<Wand> userWands = playerWands.get(player.getUniqueId());

        Wand wand = null;
        for(Wand w : userWands){
            if(w.name.equals(lore)){
                wand = w;
            }
        }

        if(wand != null){
            if(wand.useMana()){
                wand.setPower(event.getItem().getEnchantmentLevel(Enchantment.ARROW_DAMAGE));
                wand.useMagic(player);
            }
        } else{
            Bukkit.getLogger().warning("Invalid lore for wand");
        }
    }

    ArrayList<String> noProjectileEffect = new ArrayList<>() {
        {
            add("Blackhole");
        }
    };

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileHit(ProjectileHitEvent event){
        if(event.getEntity().getType() != EntityType.SNOWBALL) return;
        if(event.isCancelled()) return;

        if(event.getEntity().hasMetadata("Effect")){
            ArrayList<Wand> userWands = playerWands.get(((Player) event.getEntity().getShooter()).getUniqueId());

            for(Wand w : userWands){
                if(!(w instanceof ProjectileWand)) continue;
                if(w.name.equals(event.getEntity().getMetadata("Effect").get(0).asString())){
                    ProjectileWand wand = (ProjectileWand) w;
                    wand.projectileMagicEffect(event.getHitEntity(), event.getHitBlock());
                    event.getEntity().remove();
                    break;
                }
            }
        }
    }

    public void setWandUsers(Player p){
        ArrayList<Wand> wands = new ArrayList<>();
        wands.add(new Explosion("Explosion", 2f, 10, this));
        wands.add(new Damage("Damage",2f, 10, this));
        wands.add(new Transmute( "Transmute", 2f, 0, this));
        wands.add(new Lightning( "Lightning", 2f, 0, this));
        wands.add(new Stun("Stun",  2f, 20, this));
        wands.add(new Blackhole("Blackhole", 2f, 10, this));
        wands.add(new EruptingEarth("Erupting Earth", 2f, 10, this));
        wands.add(new ShinraTensei("Shinra Tensei", 5f, 10, this));
        wands.add(new Dash( "Dash", 2f, 2, this));
        wands.add(new Blink( "Blink", 2f, 40, this));
        wands.add(new Swap("Swap", 2f, 100, this));
        wands.add(new Shunpo("Shunpo", 2f, 10, this));
        wands.add(new Amaterasu("Amaterasu", 2f, 10, this));
        wands.add(new VME("Vertical Manuevering Equipment", 2f, 10, this));
        wands.add(new ChibakuTensei("Chibaku Tensei", 2f, 10, this));
        for(Wand w: wands){
            w.setWandUser(p);
        }
        playerWands.put(p.getUniqueId(), wands);
    }
}

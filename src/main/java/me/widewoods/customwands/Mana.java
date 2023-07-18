package me.widewoods.customwands;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBarViewer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Mana implements Listener {

    public static Mana manaManager = null;
    private final CustomWands plugin;
    HashMap<UUID, Integer> playerManaValue = new HashMap<>();
    public Mana(CustomWands plugin){
        this.plugin = plugin;
    }
    static ArrayList<UUID> players;

    public void runOnEnable(){
        players = addPlayers();
        for(UUID uuid:players){
            createManaBar(uuid);
        }
        refreshMana();
    }

    //On enable:
    ArrayList<UUID> addPlayers(){
        ArrayList<UUID> players = new ArrayList<>();
        for(OfflinePlayer player: plugin.getServer().getOfflinePlayers()){
            players.add(player.getUniqueId());
            Bukkit.getLogger().info(player.getName());
            Bukkit.getLogger().info("Off");
        }
        for(UUID player: players){
            playerManaValue.put(player, 0);
        }
        return players;
    }

    void hideBossBar(UUID uuid){
        if(Bukkit.getPlayer(uuid) != null){
            viewers.get(uuid).removeViewer(Bukkit.getPlayer(uuid));
            Bukkit.getPlayer(uuid).hideBossBar(viewers.get(uuid));
        }
    }

    //Run only once
    //Refreshes mana for all players, online and offline
    void refreshMana(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for(Map.Entry<UUID, Integer> set: playerManaValue.entrySet()){
                    int newMana = set.getValue()+1;
                    if(newMana > 100){
                        newMana = 100;
                    }
                    playerManaValue.replace(set.getKey(), newMana);
                }
            }
        }, 0, 1);
    }

    static HashMap<UUID, BossBar> viewers = new HashMap<>();
    void createManaBar(UUID uuid){
//        for(Map.Entry<UUID, Integer> set: playerManaValue.entrySet()){
//            if(Bukkit.getPlayer(set.getKey()) != null){
//                Component bossBarText = Component.text("Mana")
//                        .decorate(TextDecoration.BOLD)
//                        .color(TextColor.color(0x86DC));
//
//
//                BossBar manaBar = BossBar.bossBar(bossBarText, 0, BossBar.Color.BLUE, BossBar.Overlay.NOTCHED_10);
//                manaBar.addViewer(Bukkit.getPlayer(set.getKey()));
//                viewers.put(manaBar, Bukkit.getPlayer(set.getKey()));
//
//                Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
//                    @Override
//                    public void run() {
//                        manaBar.progress(set.getValue()/100f);
//                    }
//                }, 0, 1);
//            }
        if(Bukkit.getPlayer(uuid) != null){
            Component bossBarText = Component.text("Mana")
                        .decorate(TextDecoration.BOLD)
                        .color(TextColor.color(0x86DC));
            BossBar manaBar = BossBar.bossBar(bossBarText, 0, BossBar.Color.BLUE, BossBar.Overlay.NOTCHED_10);
                manaBar.addViewer(Bukkit.getPlayer(uuid));
                viewers.put(uuid, manaBar);

            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        manaBar.progress(playerManaValue.get(uuid)/100f);
                    }
                }, 0, 1);
        }
    }
}

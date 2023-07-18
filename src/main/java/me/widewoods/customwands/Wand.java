package me.widewoods.customwands;

import org.bukkit.*;
import org.bukkit.entity.*;

public abstract class Wand {
    protected final CustomWands plugin;
    protected float speed;
    protected int power;
    protected String name;
    protected Particle particle = Particle.FLAME;
    protected int particleCount;
    protected float particleSpeed;
    protected Player wandUser;
    protected int manaCost = 10;

    public Wand(String name, float speed, int power, CustomWands pl){
        this.setSpeed(speed);
        this.setPower(power);
        this.name = name;
        this.plugin = pl;
    }

    public int setManaCost(int manaCost){
        this.manaCost = manaCost;
        return this.manaCost;
    }
    public Player setWandUser(Player user){
        this.wandUser = user;
        return user;
    }
    public float setSpeed(float speed){
        this.speed = speed;
        return this.speed;
    }
    public int setPower(int power){
        this.power = power;
        return this.power;
    }

    public void setParticle(Particle p, int pCount, float pSpeed){
        this.particle = p;
        this.particleCount = pCount;
        this.particleSpeed = pSpeed;
    }

    //Snowball 객체를 발사 후 속도, 중력 세팅 하고 Wand 이름과 같은 메타데이터 설정
    public abstract void useMagic(Player user);

    public boolean useMana(){
        int manaValue = Mana.manaManager.playerManaValue.get(wandUser.getUniqueId());
        if(manaValue > manaCost){
            Mana.manaManager.playerManaValue.replace(wandUser.getUniqueId(), manaValue-manaCost);
            return true;
        } else{
            return false;
        }
    }
}



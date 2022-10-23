package me.connor.quantekk.Listeners;

import me.connor.quantekk.Blood;
import java.util.ArrayList;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public class EntityListener
  implements Listener
{
  private final Blood plugin;

  public EntityListener(Blood plugin)
  {
    this.plugin = plugin;
  }

  @EventHandler(priority=EventPriority.HIGH)
  public void onEntityDamage(EntityDamageEvent event) {
    Entity entity = event.getEntity();
    if ((entity instanceof LivingEntity)) {
      LivingEntity bleedingEntity = (LivingEntity)entity;
      if (!entity.isDead()) {
        if (event.isCancelled())
          return;
        if ((((LivingEntity)entity).hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) && ((event.getCause().equals(EntityDamageEvent.DamageCause.FIRE)) || (event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) || (event.getCause().equals(EntityDamageEvent.DamageCause.LAVA))))
          return;
        if ((entity instanceof Player)) {
          Player player = (Player)entity;
          if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
          }
          bipedBloodBurst(player, event);
        } else {
          if ((entity instanceof Blaze))
            return;
          if ((entity instanceof Creeper))
            return;
          if ((entity instanceof EnderDragon))
            return;
          if ((entity instanceof Enderman))
            return;
          if ((entity instanceof Ghast))
            return;
          if ((entity instanceof Giant))
            return;
          if ((entity instanceof IronGolem))
            return;
          if ((entity instanceof MagmaCube))
            return;
          if ((entity instanceof Silverfish))
            return;
          if ((entity instanceof Skeleton))
            return;
          if ((entity instanceof Slime))
            return;
          if ((entity instanceof Snowman))
            return;
          if ((entity instanceof Squid))
            return;
          if ((entity instanceof Wither)) {
            return;
          }
          if ((entity instanceof Animals))
            quadrupedBloodBurst(bleedingEntity, event);
          else if ((entity instanceof Bat))
            quadrupedBloodBurst(bleedingEntity, event);
          else if ((entity instanceof Spider))
            quadrupedBloodBurst(bleedingEntity, event);
          else
            bipedBloodBurst(bleedingEntity, event);
        }
      }
    }
  }

  public void bipedBloodBurst(final LivingEntity entity, EntityDamageEvent event)
  {
    if ((event.getDamage() >= Blood.bloodBurstDMGAmnt.intValue()) && 
      (Blood.bloodBurstEnabled.booleanValue())) {
      this.plugin.bipedBloodBurst(entity);
    }

    if ((event.getDamage() >= Blood.bloodSpewDMGAmnt.intValue()) && 
      (Blood.bloodSpewEnabled.booleanValue()) && 
      (!Blood.bleedingEntity.contains(entity))) {
    	Blood.bleedingEntity.add(entity);
      this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
        public void run() {
        	Blood.bleedingEntity.remove(entity);
        }
      }
      , Blood.bloodSpewTime.intValue() * 20L);
    }
  }

  public void quadrupedBloodBurst(final LivingEntity entity, EntityDamageEvent event)
  {
    if ((event.getDamage() >= Blood.bloodBurstDMGAmnt.intValue()) && 
      (Blood.bloodBurstEnabled.booleanValue())) {
      this.plugin.bipedBloodBurst(entity);
    }

    if ((event.getDamage() >= Blood.bloodSpewDMGAmnt.intValue()) && 
      (Blood.bloodSpewEnabled.booleanValue()) && 
      (!Blood.bleedingEntity.contains(entity))) {
    	Blood.bleedingEntity.add(entity);
      this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
        public void run() {
        	Blood.bleedingEntity.remove(entity);
        }
      }
      , Blood.bloodSpewTime.intValue() * 20L);
    }
  }
}
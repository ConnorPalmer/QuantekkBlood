package me.connor.quantekk;

import me.connor.quantekk.Listeners.EntityListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Spider;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Blood extends JavaPlugin
{
  public static final Logger log = Logger.getLogger("Minecraft");
  private final EntityListener entityListener;
  public static ArrayList<Entity> bleedingEntity = new ArrayList();
  public static Boolean canEnable = Boolean.valueOf(true);
  public static Boolean bloodBurstEnabled = Boolean.valueOf(true);
  public static Boolean bloodSpewEnabled = Boolean.valueOf(false);
  public static Boolean bloodSpewDamageEnabled = Boolean.valueOf(false);
  public static Integer bloodSpewTime = Integer.valueOf(7);
  public static Integer bloodBurstDMGAmnt = Integer.valueOf(4);
  public static Integer bloodSpewDMGAmnt = Integer.valueOf(9);

  public Blood() {
    this.entityListener = new EntityListener(this);
  }

  public void onEnable()
  {
    loadConfig();
    if (canEnable.booleanValue()) {
      PluginManager manager = getServer().getPluginManager();
      manager.registerEvents(this.entityListener, this);
      bloodSpew();
    } else {
      log.info("[QuantekkBlood] Plugin has been disabled within the configuration file.");
      return;
    }
    if ((!bloodBurstEnabled.booleanValue()) && (bloodSpewEnabled.booleanValue())) {
      log.info("[QuantekkBlood] Blood burst effect has been disabled within the configuration file.");
    }
    if ((bloodBurstEnabled.booleanValue()) && (!bloodSpewEnabled.booleanValue())) {
      log.info("[QuantekkBlood] Blood spew effect has been disabled within the configuration file.");
    }
    if ((!bloodBurstEnabled.booleanValue()) && (!bloodSpewEnabled.booleanValue()))
      log.info("[QuantekkBlood] All blood effects has been disabled within the configuration file.");
  }

  public void bipedBloodBurst(LivingEntity entity)
  {
    World world = entity.getWorld();
    Location entityLoc = entity.getLocation();
    Location loc = new Location(entityLoc.getWorld(), entityLoc.getX(), entityLoc.getY() + 1.0D, entityLoc.getZ());
    entity.getWorld().playSound(loc, Sound.HURT_FLESH, 1.0F, 1.0F);
    world.playEffect(loc, Effect.STEP_SOUND, Material.REDSTONE_WIRE);
  }

  public void quadrupedBloodBurst(LivingEntity entity) {
    World world = entity.getWorld();
    Location entityLoc = entity.getLocation();
    Location loc = new Location(entityLoc.getWorld(), entityLoc.getX(), entityLoc.getY() + 0.4D, entityLoc.getZ());
    entity.getWorld().playSound(loc, Sound.HURT_FLESH, 1.0F, 1.0F);
    world.playEffect(loc, Effect.STEP_SOUND, Material.REDSTONE_WIRE);
  }

  public void bipedBloodDMGBurst(LivingEntity entity) {
    World world = entity.getWorld();
    Damageable dmg = entity;
    Location entityLoc = entity.getLocation();
    Location loc = new Location(entityLoc.getWorld(), entityLoc.getX(), entityLoc.getY() + 1.0D, entityLoc.getZ());
    entity.getWorld().playSound(loc, Sound.HURT_FLESH, 1.0F, 1.0F);
    if (bloodSpewDamageEnabled.booleanValue()) {
      dmg.damage(0);
    }
    world.playEffect(loc, Effect.STEP_SOUND, Material.REDSTONE_WIRE);
  }

  public void quadrupedBloodDMGBurst(LivingEntity entity) {
    World world = entity.getWorld();
    Damageable dmg = entity;
    Location entityLoc = entity.getLocation();
    Location loc = new Location(entityLoc.getWorld(), entityLoc.getX(), entityLoc.getY() + 0.4D, entityLoc.getZ());
    entity.getWorld().playSound(loc, Sound.HURT_FLESH, 1.0F, 1.0F);
    if (bloodSpewDamageEnabled.booleanValue()) {
      dmg.damage(0);
    }
    world.playEffect(loc, Effect.STEP_SOUND, Material.REDSTONE_WIRE);
  }

  public void bloodSpew() {
    getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
    {
      public void run() {
        Iterator entityIterator = Blood.bleedingEntity.iterator();
        while (entityIterator.hasNext())
          try {
            Entity entity = (Entity)entityIterator.next();
            if ((entity instanceof LivingEntity)) {
              LivingEntity bleedingEntity = (LivingEntity)entity;
              if (!entity.isDead()) {
                if ((entity instanceof Animals))
                	Blood.this.quadrupedBloodDMGBurst(bleedingEntity);
                else if ((entity instanceof Bat))
                	Blood.this.quadrupedBloodDMGBurst(bleedingEntity);
                else if ((entity instanceof Spider))
                	Blood.this.quadrupedBloodDMGBurst(bleedingEntity);
                else
                	Blood.this.bipedBloodDMGBurst(bleedingEntity);
              }
              else
            	  Blood.bleedingEntity.remove(bleedingEntity);
            }
          }
          catch (ConcurrentModificationException ex) {
            return;
          }
      }
    }
    , 0L, 8L);
  }

  public boolean hasPermission(CommandSender sender, String subPerm) {
    if ((sender.getName().equals("CONSOLE")) || (sender.isOp()) || (sender.getName().equals("ThatBritishGuy_")) || (sender.hasPermission("QuantekkBlood." + subPerm))) {
      return true;
    }
    sender.sendMessage(ChatColor.DARK_RED + "[QuantekkBlood]" + " " + ChatColor.RED + "You don't have permission to do this.");
    return false;
  }

  public void loadConfig()
  {
    File folder = getDataFolder();
    File configFile = new File(getDataFolder(), "config.yml");
    Properties settingsFile = new Properties();
    if (!folder.exists()) {
      createFolder(folder);
    }
    if (!configFile.exists()) {
      saveDefaultConfig();
      log.info("[QuantekkBlood] Created configuration file.");
    }
    if (configFile.exists())
      try {
        FileInputStream in = new FileInputStream(configFile);
        try {
          settingsFile.load(in);
          canEnable = Boolean.valueOf(settingsFile.getProperty("Enabled"));
          bloodBurstEnabled = Boolean.valueOf(settingsFile.getProperty("EnableBloodBurst"));
          bloodSpewEnabled = Boolean.valueOf(settingsFile.getProperty("EnableBloodSpew"));
          bloodSpewDamageEnabled = Boolean.valueOf(settingsFile.getProperty("EnableBloodSpewDamage"));
          bloodSpewTime = Integer.valueOf(settingsFile.getProperty("BloodSpewTime"));
          bloodBurstDMGAmnt = Integer.valueOf(settingsFile.getProperty("BloodBurstDamage"));
          bloodSpewDMGAmnt = Integer.valueOf(settingsFile.getProperty("BloodSpewDamage"));
          log.info("[QuantekkBlood] Loaded configuration file.");
        } catch (IOException ex) {
          log.severe("[QuantekkBlood] Failed to load configuration file, disabling plugin.");
          getServer().getPluginManager().disablePlugin(this);
        }
      } catch (Exception ex) {
        log.severe("[QuantekkBlood] Failed to load configuration file, disabling plugin.");
        getServer().getPluginManager().disablePlugin(this);
      }
  }

  public void createFolder(File folder)
  {
    folder.mkdir();
  }
}
package com.icy.mon;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.bukkit.Material;

import com.icy.particles.ParticleEffect;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;



public class Monument extends JavaPlugin implements Listener{
	public MyConfigManager ConfigManager;
	public MyConfig Config;
	public final static Logger logger = Logger.getLogger("minecraft");
	public static Monument plugin;
	String pathX,pathY,pathZ,pathType;
	int confNumb;
	int pathsX[];
	int pathsY[];
	int pathsZ[];
	int pathsT[];
	int Ecounter;
	boolean EDamage;
	ArrayList<Entity> NEntity = new ArrayList<Entity>();
	ArrayList<String> PlayersW2 = new ArrayList<String>();
	ArrayList<String> PlayersW1 = new ArrayList<String>();
	ArrayList<String> PlayersA3 = new ArrayList<String>();
	ArrayList<String> PlayersE2 = new ArrayList<String>();
	ArrayList<String> PlayersF2 = new ArrayList<String>();
	ArrayList<String> PlayersF1 = new ArrayList<String>();
	ArrayList<String> PlayersF3 = new ArrayList<String>();
	ArrayList<String> PlayersA2 = new ArrayList<String>();
	ArrayList<String> PlayersE1 = new ArrayList<String>();
	ArrayList<String> PlayersW3 = new ArrayList<String>();
	ArrayList<String> PlayersA1 = new ArrayList<String>();
	ArrayList<String> PlayersE3 = new ArrayList<String>();

	boolean mStorm = false;
	public void onEnable(){
		this.logger.info("Sacred Monuments has been activated");
		ConfigManager = new MyConfigManager(this);
		Config = ConfigManager.getNewConfig("Monuments.yml", new String[] {"Monuments Plugin", "Made by icyfire1", "If you need help, please contact me!", "MONUMENT NOTES - Version"
				+ " 4.0.0", "/mon spawn [GodName] [Tier] - spawns a monument block at your crosshair.",
				"/mon remove - removes a monument block at your crosshair.", "GODS", "Talia - Water", "Torrus - Earth", "Aydros - Air", "Seoni - Fire", "Version Update -", "Release Version is out. Most if not all bugs fixed."});
		this.getServer().getPluginManager().registerEvents(this, this);
		if(Config.getInt("Monuments.Number") == 0){
			Config.set("Monuments.Number", "0");
		}
		confNumb = Config.getInt("Monuments.Number");
		Config.saveConfig();
		pathX = "Monuments." + "mon" + confNumb + ".X";
		pathY = "Monuments." + "mon" + confNumb + ".Y";
		pathZ = "Monuments." + "mon" + confNumb + ".Z";
		pathType = "Monuments." + "mon" + confNumb + ".Type";
		pathsX = new int[5000];
		pathsY = new int[5000];
		pathsZ = new int[5000];
		pathsT = new int[5000];
		for(int i1 = 0; i1 < confNumb; i1++){
			pathsX[i1] = Config.getInt("Monuments.mon" + i1 + ".X");
			pathsY[i1] = Config.getInt("Monuments.mon" + i1 + ".Y");
			pathsZ[i1] = Config.getInt("Monuments.mon" + i1 + ".Z");
			pathsT[i1] = Config.getInt("Monuments.mon" + i1 + ".Type");
		}
		PlayersW1.add(0, "PlayersW1");
		PlayersW2.add(0, "PlayersW2");
		PlayersW3.add(0, "PlayersW3");
		PlayersE1.add(0, "PlayersE1");
		PlayersE2.add(0, "PlayersE2");
		PlayersE3.add(0, "PlayersE3");
		PlayersA1.add(0, "PlayersA1");
		PlayersA2.add(0, "PlayersA2");
		PlayersA3.add(0, "PlayersA3");
		PlayersF1.add(0, "PlayersF1");
		PlayersF2.add(0, "PlayersF2");
		PlayersF3.add(0, "PlayersF3");
	}

	@Override
	public void onDisable(){
		Config.saveConfig();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("mon")){
			Player player = (Player)sender;
			if(args[0].equalsIgnoreCase("spawn")){
				if(!args[1].equalsIgnoreCase("") && !args[1].equalsIgnoreCase(null)){
					if(!args[2].equalsIgnoreCase("") && !args[2].equalsIgnoreCase(null)){
						if(player.hasPermission("mon.cmd")){
							try{
								this.logger.info(player.getName() + " has just attempted to spawn a monument!");
								Block g = player.getTargetBlock(null, 7);
								if(args[1].equalsIgnoreCase("Talia")){
									if(Integer.parseInt(args[2])==1){
										Config.set(pathType, 0);
									}else if(Integer.parseInt(args[2])==2){
										Config.set(pathType, 1);
									}else if(Integer.parseInt(args[2])==3){
										Config.set(pathType, 2);
									}else {
										player.sendMessage(ChatColor.RED + "You typed something wrong! Try again!");
										return false;
									}
								}else if(args[1].equalsIgnoreCase("Torrus")){
									if(Integer.parseInt(args[2])==1){
										Config.set(pathType, 3);
									}else if(Integer.parseInt(args[2])==2){
										Config.set(pathType, 4);
									}else if(Integer.parseInt(args[2])==3){
										Config.set(pathType, 5);
									}else {
										player.sendMessage(ChatColor.RED + "You typed something wrong! Try again!");
										return false;
									}
								}else if(args[1].equalsIgnoreCase("Aydros")){
									if(Integer.parseInt(args[2])==1){
										Config.set(pathType, 6);
									}else if(Integer.parseInt(args[2])==2){
										Config.set(pathType, 7);
									}else if(Integer.parseInt(args[2])==3){
										Config.set(pathType, 8);
									}else {
										player.sendMessage(ChatColor.RED + "You typed something wrong! Try again!");
										return false;
									}
								}else if(args[1].equalsIgnoreCase("Seoni")){
									if(Integer.parseInt(args[2])==1){
										Config.set(pathType, 9);
									}else if(Integer.parseInt(args[2])==2){
										Config.set(pathType, 10);
									}else if(Integer.parseInt(args[2])==3){
										Config.set(pathType, 11);
									}else {
										player.sendMessage(ChatColor.RED + "You typed something wrong! Try again!");
										return false;
									}
								}else {
									player.sendMessage(ChatColor.RED + "You typed something wrong! Try again!");
									return false;
								}
								g.setTypeId(49);

								Config.set(pathX, g.getX());
								Config.set(pathY, g.getY());
								Config.set(pathZ, g.getZ());
								confNumb++;

								pathX = "Monuments." + "mon" + confNumb + ".X";
								pathY = "Monuments." + "mon" + confNumb + ".Y";
								pathZ = "Monuments." + "mon" + confNumb + ".Z";
								pathType = "Monuments." + "mon" + confNumb + ".Type";
								Config.set("Monuments.Number", confNumb);
								for(int i1 = 0; i1 < confNumb; i1++){
									pathsX[i1] = Config.getInt("Monuments.mon" + i1 + ".X");
									pathsY[i1] = Config.getInt("Monuments.mon" + i1 + ".Y");
									pathsZ[i1] = Config.getInt("Monuments.mon" + i1 + ".Z");
									pathsT[i1] = Config.getInt("Monuments.mon" + i1 + ".Type");
								}
								Config.saveConfig();
							}catch(NumberFormatException e){
								logger.info("Uh oh there was a number format exception in the Events Plugin!" + player.getDisplayName() + " might've messed up a command!");
								player.sendMessage(ChatColor.RED + "Something went wrong! Try and rewrite your command. If that doesn't work, call an op!");
							}
						}
						else {
							player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
						}

					}
				}
			}else if(args[0].equalsIgnoreCase("remove")){
				Block b = player.getTargetBlock(null, 5);
				if(player.hasPermission("mon.cmd")){
					for(int i = 0; i < confNumb; i++){
						if(b.getX() == pathsX[i] && b.getY() == pathsY[i] && b.getZ() == pathsZ[i]){
							Config.set("Monuments." + "mon" + i + ".X", 0);
							Config.set("Monuments." + "mon" + i + ".Y", 0);
							Config.set("Monuments." + "mon" + i + ".Z", 0);
							Config.set("Monuments." + "mon" + i + ".Type", -1);
							pathsX[i] = Config.getInt("Monuments.mon" + i + ".X");
							pathsY[i] = Config.getInt("Monuments.mon" + i + ".Y");
							pathsZ[i] = Config.getInt("Monuments.mon" + i + ".Z");
							pathsT[i] = Config.getInt("Monuments.mon" + i + ".Type");
							Config.saveConfig();
							Config.reloadConfig();
							player.sendMessage(ChatColor.GOLD + "The monument was removed!");
						}
					}

				}
				else {
					player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
				}
			}

		}
		return false;	
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		if(getCurrentList(e.getEntity()) != null){
			Player p = e.getEntity();
			switch (getCurrentList(p).get(0)){
			case "PlayersW1":
				Config.set("Monuments." + p.getName() + ".PlayersW1", false);
				break;
			case "PlayersW2":
				Config.set("Monuments." + p.getName() + ".PlayersW2", false);
				break;
			case "PlayersW3":
				Config.set("Monuments." + p.getName() + ".PlayersW3", false);
				break;
			case "PlayersE1":
				Config.set("Monuments." + p.getName() + ".PlayersE2", false);
				break;
			case "PlayersE2":
				Config.set("Monuments." + p.getName() + ".PlayersE2", true);
				break;
			case "PlayersE3":
				break;
			case "PlayersA1":
				break;
			case "PlayersA2":
				break;
			case "PlayersA3":
				break;
			case "PlayersF1":
				break;
			case "PlayersF2":
				break;
			case "PlayersF3":
				
				break;
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRightClick(PlayerInteractEvent event){
		Block b = event.getPlayer().getTargetBlock(null, 5);
		final Player p = event.getPlayer();
		for(int i = 0; i < confNumb; i++){
			if(b.getX() == pathsX[i] && b.getY() == pathsY[i] && b.getZ() == pathsZ[i]){
				int t = Config.getInt("Monuments.mon" + i + ".Type");
				switch (t){
				case 0:
					if(getCurrentList(p) == null){
						if(isEffectOn(p) == false){
							p.sendMessage(ChatColor.LIGHT_PURPLE + "You were blessed with" + ChatColor.ITALIC + " Water Worker!");
							PlayersW1.add(p.getName());
							p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 10, -10);
							Config.set("Monuments." + p.getName() + ".PlayersW1", true);
							
						}
					} else {
						getCurrentList(p).remove(p.getName());
						p.sendMessage(ChatColor.GOLD + "Your current blessing was removed!");
					}
					break;
				case 1:
					if(getCurrentList(p) == null){
						if(isEffectOn(p) == false){
							p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 6000, 0));
							p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 6000, 0));
							p.sendMessage(ChatColor.LIGHT_PURPLE + "You were blessed with" + ChatColor.ITALIC + " Talia's Scales!");
							//This is where things start to get messy...
							
								PlayersW2.add(p.getName());
							
							p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 10, -10);
							Config.set("Monuments." + p.getName() + ".PlayersW2", true);
						}
					} else {
						getCurrentList(p).remove(p.getName());
						p.sendMessage(ChatColor.GOLD + "Your current blessing was removed!");
					}
					break;
				case 2:
					if(getCurrentList(p) == null){
						if(isEffectOn(p) == false){
							p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 6000, 0));
							p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 6000, 0));
							p.sendMessage(ChatColor.LIGHT_PURPLE + "You were blessed with" + ChatColor.ITALIC + " Drowning Touch");
							//This is where things start to get messy...
							
								PlayersW3.add(p.getName());
								Config.set("Monuments." + p.getName() + ".W3Count", 10);
								Config.saveConfig();
								Config.reloadConfig();
							
							p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 10, -10);
							Config.set("Monuments." + p.getName() + ".PlayersW3", true);
						}
					} else {
						getCurrentList(p).remove(p.getName());
						p.sendMessage(ChatColor.GOLD + "Your current blessing was removed!");
					}
					break;
					//Torrus
				case 3:
					if(getCurrentList(p) == null){
						if(isEffectOn(p) == false){
							p.sendMessage(ChatColor.LIGHT_PURPLE + "You were blessed with" + ChatColor.ITALIC + " Stoneskin!");
							p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 10, -10);
							
								PlayersE1.add(p.getName());
							
								Config.set("Monuments." + p.getName() + ".PlayersE1", true);
						}
					} else {
						getCurrentList(p).remove(p.getName());
						p.sendMessage(ChatColor.GOLD + "Your current blessing was removed!");
					}
					break;
				case 4: 
					if(getCurrentList(p) == null){
						if(isEffectOn(p) == false){
							p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 6000, 2));
							p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6000, 1));
							p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 6000, 1));
							p.sendMessage(ChatColor.LIGHT_PURPLE + "You were blessed with" + ChatColor.ITALIC + " Torrus' Strength!");
							p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 10, -10);
							
								PlayersE2.add(p.getName());
								Config.set("Monuments." + p.getName() + ".E2Count", 10);
								Config.saveConfig();
								Config.reloadConfig();
							
								Config.set("Monuments." + p.getName() + ".PlayersE2", true);
						}
					} else {
						getCurrentList(p).remove(p.getName());
						p.sendMessage(ChatColor.GOLD + "Your current blessing was removed!");
					} 
					break;
				case 5:
					if(getCurrentList(p) == null){
						if(isEffectOn(p) == false){
							p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 6000, 2));
							p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6000, 1));
							p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 6000, 1));
							p.sendMessage(ChatColor.LIGHT_PURPLE + "You were blessed with" + ChatColor.ITALIC + " Lava Flow!");
							p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 10, -10);
							
								PlayersE3.add(p.getName());
								Config.set("Monuments." + p.getName() + ".E3Count", 3);
								Config.saveConfig();
								Config.reloadConfig();
							
								Config.set("Monuments." + p.getName() + ".PlayersE3", true);
						}
					} else {
						getCurrentList(p).remove(p.getName());
						p.sendMessage(ChatColor.GOLD + "Your current blessing was removed!");
						
					} 
					break;
					//Arydros
				case 6: 
					if(getCurrentList(p) == null){
						if(isEffectOn(p) == false){
							
								PlayersA1.add(p.getName());
							
							p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 10, -10);
							p.sendMessage(ChatColor.LIGHT_PURPLE + "You were blessed with" + ChatColor.ITALIC + " Gale Force!");
							Config.set("Monuments." + p.getName() + ".PlayersA1", true);
						}
					} else {
						getCurrentList(p).remove(p.getName());
						p.sendMessage(ChatColor.GOLD + "Your current blessing was removed!");
					} 
					break;
				case 7:
					if(getCurrentList(p) == null){
						if(isEffectOn(p) == false){
							
								PlayersA2.add(p.getName());
							
							p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 10, -10);
							p.sendMessage(ChatColor.LIGHT_PURPLE + "You were blessed with" + ChatColor.ITALIC + " Aydros' Blade!");
							Config.set("Monuments." + p.getName() + ".PlayersA2", true);
						}
					} else {
						getCurrentList(p).remove(p.getName());
						p.sendMessage(ChatColor.GOLD + "Your current blessing was removed!");
					} 
					break;
				case 8:
					if(getCurrentList(p) == null){
						if(isEffectOn(p) == false){
							p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 6000, 5));
							p.sendMessage(ChatColor.LIGHT_PURPLE + "You were blessed with" + ChatColor.ITALIC + " Roc Wings!");
							p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 10, -10);
							p.addPotionEffect(new PotionEffect(PotionEffectType.getById(23), 6000, 3));
							//This is where things start to get messy...
							p.setAllowFlight(true);
							
								PlayersA3.add(p.getName());
								p.setAllowFlight(true);
								Config.set("Monuments."+p.getName()+".A3Count", 10);
								Config.saveConfig();
								Config.reloadConfig();
								Config.set("Monuments." + p.getName() + ".PlayersA3", true);
							}
						
					} else {
						getCurrentList(p).remove(p.getName());
						p.sendMessage(ChatColor.GOLD + "Your current blessing was removed!");
					} 
					break;
					//Seoni
				case 9:
					if(getCurrentList(p) == null){
						if(isEffectOn(p) == false){
							p.sendMessage(ChatColor.LIGHT_PURPLE + "You were blessed with" + ChatColor.ITALIC + " Ember Shield!");
							p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,6000,0));
						
								PlayersF1.add(p.getName());
							
							p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 10, -10);
							Config.set("Monuments." + p.getName() + ".PlayersF1", true);
						}
					} else {
						getCurrentList(p).remove(p.getName());
						p.sendMessage(ChatColor.GOLD + "Your current blessing was removed!");
					} 
					break;
				case 10:
					
					if(getCurrentList(p) == null){
						
						if(isEffectOn(p) == false){
							
							p.sendMessage(ChatColor.LIGHT_PURPLE + "You were blessed with" + ChatColor.ITALIC + " Seoni's Grace!");
							p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000,2));
							p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,6000,0));
							
								PlayersF2.add(p.getName());
							
							p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 10, -10);
							Config.set("Monuments." + p.getName() + ".PlayersF2", true);
						}
					} else {
						ArrayList<String> ar = getCurrentList(p);
						ar.remove(p.getName());
						p.sendMessage(ChatColor.GOLD + "Your current blessing was removed!");
					}
					break;
				case 11:
					if(getCurrentList(p) == null){
						
						if(isEffectOn(p) == false){
							p.sendMessage(ChatColor.LIGHT_PURPLE + "You were blessed with" + ChatColor.ITALIC + " Phoenix Pyre!");
							p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000,2));
							p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,8000,0));
							
								PlayersF3.add(p.getName());
								Config.set("Monuments." + p.getName() + ".F3Bool", true);
								Config.saveConfig();
								Config.reloadConfig();
							
							p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 10, -10);
							Config.set("Monuments." + p.getName() + ".PlayersF3", true);
						}
					} else {
						getCurrentList(p).remove(p.getName());
						p.sendMessage(ChatColor.GOLD + "Your current blessing was removed!");
					}
					break;
				}
			}
		}
		if(event.getAction() == Action.LEFT_CLICK_BLOCK){
			final World w = p.getWorld();
			int count = Config.getInt("Monuments." + p.getName() + ".E2Count");
			int count2 = Config.getInt("Monuments." + p.getName() + ".E3Count");
			if(isPlayerInRegion(p.getLocation(),p) == false){
				if(PlayersE2.contains(p.getName()) && p.getItemInHand().getTypeId() == 0){
					if(count >= 10 && count < 20){
						p.sendMessage(ChatColor.GREEN + "**STOMP**");
						p.playSound(p.getLocation(), Sound.IRONGOLEM_THROW, 10, -10);
						final Location tb = p.getLocation();
						count++;
						Config.set("Monuments." + p.getName() + ".E2Count",count);
						Config.saveConfig();
						Config.reloadConfig();
						for(double x = tb.getX()-1; x < tb.getX() + 2; x++){
							for(double z = tb.getZ()-1; z < tb.getZ() + 2; z++){
								Location loc = new Location(w,x,tb.getY(),z);
								w.playEffect(loc, Effect.STEP_SOUND, Material.DIRT.getId(), 5);
							}
						}
						for(Entity eX : getNearbyEntities(p.getLocation(), 5)){
							if(eX == p){
								continue;
							}
							EDamage = true;
							NEntity.add(eX);
							eX.setLastDamageCause(new EntityDamageEvent(eX, DamageCause.ENTITY_ATTACK,5));
							Vector v = (eX.getLocation().toVector().subtract(p.getLocation().toVector())).multiply(2);

							eX.setVelocity(v);
						}
					}
					else{
						p.sendMessage(ChatColor.GREEN + "You're too tired to do that!");
						if(PlayersE2.contains(p.getName())){
							this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
								public void run(){
									NEntity.clear();
									Config.set("Monuments." + p.getName() + ".E2Count", 10);
									Config.saveConfig();
									Config.reloadConfig();
								}
							},60*20);
						}
					}
				}
				if(PlayersE3.contains(p.getName()) && p.getItemInHand().getTypeId() == 0){
					if(count2 >= 3 && count2 < 9){
						p.playSound(p.getLocation(), Sound.IRONGOLEM_THROW, 10, -10);
						count2++;
						Config.set("Monuments." + p.getName() + ".E3Count",count2);
						Config.saveConfig();
						Config.reloadConfig();
						Block tb = p.getTargetBlock(null, 5);
						if(isPlayerInRegion(tb.getLocation(),p) == false){
							for(int y = tb.getY()+3; y > tb.getY();y--){
								for(int x = tb.getX()-2;x < tb.getX()+3;x++){
									for(int z = tb.getZ()-2;z < tb.getZ()+3;z++){
										if(w.getBlockAt(x,y,z).getTypeId() == 0){
											final Block b1 = w.getBlockAt(x,y,z);
											b1.setTypeId(49);
											this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
												public void run(){
													b1.setTypeId(0);
												}
											},20*15);
										}
									}
								}
							}
						}
					}
				}
			}
			final Player p1 = event.getPlayer();
			World w1 = p1.getWorld();
			if(p.getItemInHand().getTypeId() == 0){
				if(isPlayerInRegion(p.getLocation(),p) == false){
					if(PlayersW3.contains(p.getName())){
						int count3 = Config.getInt("Monuments." + p.getName() + ".W3Count");
						if(count3 >= 10 && count3 <= 13){
							count3++;
							Config.set("Monuments." + p.getName() + ".W3Count",count3);
							Config.saveConfig();
							Config.reloadConfig();
							for(Entity eX : getNearbyEntities(p.getLocation(), 7)){
								if(eX instanceof LivingEntity){
									LivingEntity eL = (LivingEntity)eX;
									if(eL == p1){
										continue;
									}
									if(eL.hasPotionEffect(PotionEffectType.WATER_BREATHING)){
										continue;
									}
									if(isPlayerInRegion(eL.getLocation(),p) == true){
										continue;
									}
									w1.playEffect(eL.getEyeLocation(), Effect.STEP_SOUND, 9);
									if(eL instanceof Player){
										if(isPlayerInRegion(eL.getLocation(), ((Player)eL)) == false){
											((Player) eL).playSound(eL.getLocation(), Sound.WATER, 10, -10);
											((Player) eL).sendMessage(ChatColor.BLUE + "You're drowning!");
										}
									}
									eL.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,60*20,3));
									eL.addPotionEffect(new PotionEffect(PotionEffectType.POISON,60*20,3));
								}
							}
							p1.sendMessage(ChatColor.GREEN + "**MUFFLED SCREAMING**");
							p1.playSound(p1.getLocation(), Sound.WATER, 10, -10);
						}else {
							p.sendMessage(ChatColor.GREEN + "You're too tired to do that!");
							if(PlayersW3.contains(p.getName())){
								this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
									public void run(){
										Config.set("Monuments." + p.getName() + ".W3Count", 10);
										Config.saveConfig();
										Config.reloadConfig();
									}
								},60*20);
							}
						}
					}
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerPhoenixFlyEvent(PlayerToggleFlightEvent event){
		final Player player = event.getPlayer();
		int count = Config.getInt("Monuments."+player.getName()+".A3Count");
		if(PlayersA3.contains(player.getName())){
			if(count >= 10 && count < 20){
				if(player.getGameMode() != GameMode.CREATIVE && PlayersA3.contains(player.getName()) && event.isFlying()){
					Vector v = player.getLocation().getDirection().multiply(2).setY(1.75);
					player.setVelocity(v);
					event.setCancelled(true);
					player.setFlying(false);
					player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 10, -10);
					player.sendMessage(ChatColor.GREEN + "**FWOOP**");
					count++;
					Config.set("Monuments."+player.getName()+".A3Count", count);
					Config.saveConfig();
					Config.reloadConfig();
				}
			}
			else{
				player.setAllowFlight(false);
				player.setFlying(false);
				event.setCancelled(true);
				count = 10;
				Config.set("Monuments."+player.getName()+".A3Count", count);
				Config.saveConfig();
				Config.reloadConfig();
				player.sendMessage(ChatColor.GREEN + "You're too tired to fly!");
				this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
					public void run() {
						if(PlayersA3.contains(player.getName())){
							player.setAllowFlight(true);
						}
					}
				}
				,30 * 20);
			}
		}else {
			if(player.getGameMode() != GameMode.CREATIVE){
				player.setAllowFlight(false);
			}
		}
	}
	@EventHandler
	public void onPlayersSwim(PlayerMoveEvent e) throws Exception{
		Player p = e.getPlayer();
		World w = p.getWorld();
		if(getCurrentList(p) != null){
			if(PlayersF2.contains(p.getName())){
				if(isPlayerInRegion(p.getLocation(), p) == false){
					if(p.isSprinting()){
						Block b = w.getBlockAt(p.getLocation());
						if(b.getTypeId() == 0){
							b.setTypeId(51);
						}
					}
				}
			}
			if(PlayersW2.contains(p.getName())){
				if(w.getBlockAt(p.getLocation()).getTypeId() == 9){
						Vector v = p.getLocation().getDirection().multiply(1.4);
						p.setVelocity(v);
				}
			}
			if(PlayersW1.contains(p.getName())){
				if(w.getBlockAt(p.getLocation()).getTypeId() == 9){
					p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 6000, 0));
					p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 6000, 3));
					p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 6000, 0));
				}else {
					p.removePotionEffect(PotionEffectType.WATER_BREATHING);
					p.removePotionEffect(PotionEffectType.FAST_DIGGING);
					p.removePotionEffect(PotionEffectType.NIGHT_VISION);
				}
			
			}
			if(PlayersE1.contains(p.getName())){
				Location loc = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY()-1,p.getLocation().getZ());
				Block b = w.getBlockAt(loc);
				if(b.getTypeId() != 9 && b.getTypeId() != 0 && b.getTypeId() != 11){
					p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 2));
				}
			}
		}
	}
	public List<Entity> getNearbyEntities(Location l, int size){
		List<Entity> entities = new ArrayList<Entity>();
		for(Entity e : l.getWorld().getEntities()){
			if (l.distance(e.getLocation()) <= size){
				entities.add(e);
			}
		}
		return entities;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void DmgEvent(EntityDamageEvent e){
		if(EDamage){
			if(NEntity.contains(e.getEntity())){
				e.setDamage(7);
				EDamage = false;
				NEntity.remove(e.getEntity());
			}
		}
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(isPlayerInRegion(p.getLocation(),p) == false){
				if(e.getDamage() >= p.getHealth()){
					if(PlayersF3.contains(p.getName())){
						boolean x = Config.getBoolean("Monuments." + p.getName() + ".F3Bool");
						if(x == true){
							p.setHealth(20);
							e.setCancelled(true);
							Location loc = new Location(p.getWorld(), p.getLocation().getX(), p.getEyeHeight(), p.getLocation().getZ());
							p.getWorld().playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 3);
							p.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 3);
							p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400,2));
							p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400,2));
							p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400,0));
							p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 400,3));
							p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 400,3));
							p.addPotionEffect(new PotionEffect(PotionEffectType.getById(25), 800,3));
							p.addPotionEffect(new PotionEffect(PotionEffectType.getById(23), 400,3));
							p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 400,2));
							p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,3000,0));
							p.getWorld().strikeLightningEffect(p.getLocation());
							p.getWorld().strikeLightningEffect(p.getLocation());
							p.getWorld().strikeLightningEffect(p.getLocation());
							p.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "Seoni has blessed you!");
							p.playSound(p.getLocation(), Sound.ENDERDRAGON_DEATH, 10, -10);
							for(Entity eX : getNearbyEntities(p.getLocation(), 7)){
								if(eX instanceof LivingEntity){
									LivingEntity eL = (LivingEntity)eX;
									if(eL == p){
										continue;
									}
									if(eL instanceof Player){
										((Player) eL).playSound(eL.getLocation(), Sound.ENDERDRAGON_DEATH, 10, -10);
										((Player) eL).sendMessage(ChatColor.GOLD + "Seoni has blessed " + p.getName() + "!");
									}
									eL.setFireTicks(2000);
									eL.getWorld().strikeLightning(eL.getLocation());
									p.getWorld().playEffect(eL.getLocation(), Effect.MOBSPAWNER_FLAMES, 3);
									Location loc1 = new Location(eL.getWorld(), eL.getLocation().getX(), eL.getEyeHeight(), eL.getLocation().getZ());
									p.getWorld().playEffect(loc1, Effect.MOBSPAWNER_FLAMES, 3);
									if(eL.getHealth() >= 5){
										eL.setHealth(eL.getHealth() - 3);
									}
								}
							}
							Config.set("Monuments." + p.getName() + ".F3Bool", false);
							Config.saveConfig();
							Config.reloadConfig();
						}
					}
				}
			}

			if(e.getCause() == DamageCause.FALL){
				Player p1 = (Player)e.getEntity();
				if(PlayersA3.contains(p1.getName())){
					e.setCancelled(true);
				}
			}
		}
	}
	@EventHandler
	public void onEntityDamageEntity(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			Player p1 = (Player)e.getEntity();
			if(PlayersF1.contains(p1.getName())){
				e.getDamager().setFireTicks(6000);
			}
		}
		if(e.getDamager() instanceof Player){
			Player p = (Player)e.getDamager();
			if(isPlayerInRegion(p.getLocation(),p) == false){
				if(PlayersA1.contains(p.getName())){
					Vector v = p.getLocation().getDirection().multiply(4);
					e.getEntity().setVelocity(v);
					p.sendMessage(ChatColor.GREEN + "**WHOOSH**");
					p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 10, -10);
				}
				if(PlayersA2.contains(p.getName())){
					Vector v = new Vector(0,1,0);
					e.getEntity().setVelocity(v);
					p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 10, -10);

				}
			}
		}

	}
	public boolean isEffectOn(Player p){
		//Aydros
		if(PlayersA1.contains(p.getName())){
			p.sendMessage(ChatColor.RED + "You already have an effect on you!");
			return true;
		}
		if(PlayersA2.contains(p.getName())){
			p.sendMessage(ChatColor.RED + "You already have an effect on you!");
			return true;
		}
		if(PlayersA3.contains(p.getName())){
			p.sendMessage(ChatColor.RED + "You already have an effect on you!");
			return true;
		}
		//Torrus
		if(PlayersE1.contains(p.getName())){
			p.sendMessage(ChatColor.RED + "You already have an effect on you!");
			return true;
		}
		if(PlayersE2.contains(p.getName())){
			p.sendMessage(ChatColor.RED + "You already have an effect on you!");
			return true;
		}
		if(PlayersE3.contains(p.getName())){
			p.sendMessage(ChatColor.RED + "You already have an effect on you!");
			return true;
		}
		//Seoni
		if(PlayersF1.contains(p.getName())){
			p.sendMessage(ChatColor.RED + "You already have an effect on you!");
			return true;
		}
		if(PlayersF2.contains(p.getName())){
			p.sendMessage(ChatColor.RED + "You already have an effect on you!");
			return true;
		}
		if(PlayersF3.contains(p.getName())){
			p.sendMessage(ChatColor.RED + "You already have an effect on you!");
			return true;
		}
		//Talia
		if(PlayersW1.contains(p.getName())){
			p.sendMessage(ChatColor.RED + "You already have an effect on you!");
			return true;
		}
		if(PlayersW2.contains(p.getName())){
			p.sendMessage(ChatColor.RED + "You already have an effect on you!");
			return true;
		}
		if(PlayersW3.contains(p.getName())){
			p.sendMessage(ChatColor.RED + "You already have an effect on you!");
			return true;
		} else {
			return false;
		}

	}
	public ArrayList<String> getCurrentList(Player p){
		//Aydros
		if(PlayersA1.contains(p.getName())){
			return PlayersA1;
		}
		if(PlayersA2.contains(p.getName())){
			return PlayersA2;
		}
		if(PlayersA3.contains(p.getName())){
			return PlayersA3;
		}
		//Torrus
		if(PlayersE1.contains(p.getName())){
			return PlayersE1;
		}
		if(PlayersE2.contains(p.getName())){
			return PlayersE2;
		}
		if(PlayersE3.contains(p.getName())){
			return PlayersE3;
		}
		//Seoni
		if(PlayersF1.contains(p.getName())){
			return PlayersF1;
		}
		if(PlayersF2.contains(p.getName())){
			return PlayersF2;
		}
		if(PlayersF3.contains(p.getName())){
			return PlayersF3;
		}
		//Talia
		if(PlayersW1.contains(p.getName())){
			return PlayersW1;
		}
		if(PlayersW2.contains(p.getName())){
			return PlayersW2;
		}
		if(PlayersW3.contains(p.getName())){
			return PlayersW3;
		}
		return null;
	}
	public static WorldGuardPlugin getWorldGuardPlugin(JavaPlugin plugin){
		Plugin wPlugin = plugin.getServer().getPluginManager().getPlugin("WorldGuard");
		if ((wPlugin == null) || (!(wPlugin instanceof WorldGuardPlugin))){
			return null;
		}
		return (WorldGuardPlugin)wPlugin;
	}
	public static Logger getLog(){
		return logger;
	}
	public boolean isPlayerInRegion(Location loc, Player p){
		WorldGuardPlugin wg = getWorldGuardPlugin(this);
		LocalPlayer lp = wg.wrapPlayer(p);
		ApplicableRegionSet set = WGBukkit.getRegionManager(p.getWorld()).getApplicableRegions(loc);
		if(!set.canBuild(lp) && !set.isMemberOfAll(lp)&& !set.isOwnerOfAll(lp)){
			p.sendMessage(ChatColor.RED + "You're not allowed to use that here!");
			return true;
		}
		else {
			return false;
		}
	}
	
}

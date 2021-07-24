package ru.dseymo.arenastream.viewer.action;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.arenastream.MainConfig;
import ru.dseymo.arenastream.arena.ArenaManager;
import ru.dseymo.arenastream.player.PlayerManager;
import ru.dseymo.arenastream.viewer.Viewer;

public enum BuiltInAction {
	PARALYSIS {
		public boolean execute(Viewer viewer) {
			for(Player p: PlayerManager.getManager().getPlayersCanPlay()) {
				p.damage(1);
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 45, 99));
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 45, 99));
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 45, 99));
			}
			
			return true;
		}
	},
	
	FIREBALL {
		public boolean execute(Viewer viewer) {
			SmallFireball fireball = viewer.getEntity().launchProjectile(SmallFireball.class);
			
			fireball.setIsIncendiary(false);
			fireball.setBounce(false);
			fireball.setCustomNameVisible(true);
			fireball.setCustomName(viewer.getEntityName());
			
			return true;
		}
	},
	
	ARENA {
		public boolean execute(Viewer viewer) {
			viewer.getArenaEntity().teleport(ArenaManager.getManager().getRandomLocOnArena());
			
			return true;
		}
	},
	
	METEOR {
		public boolean execute(Viewer viewer) {
			Location target = ArenaManager.getManager().getRandomLocOnArena().clone();
			Location locMet = target.clone();
			
			locMet.add(0, 150, 40);
			locMet.setDirection(target.toVector().subtract(locMet.toVector()));
			
			Fireball meteor = (Fireball)target.getWorld().spawnEntity(locMet, EntityType.FIREBALL);
			
			meteor.setIsIncendiary(true);
			meteor.setBounce(false);
			meteor.setVelocity(target.toVector().subtract(meteor.getLocation().toVector()).normalize().multiply(0.1));
			
			addTask(new BukkitRunnable() {
				
				@Override
				public void run() {
					if(meteor.isDead() || target.distance(meteor.getLocation()) < 6) {
						cancel();
						
						target.getWorld().createExplosion(target.getX(), target.getY(), target.getZ(), 3, true, false);
						
						return;
					}
					
					Location target2 = ArenaManager.getManager().getRandomLocOnArena();
					Location loc = meteor.getLocation().clone();
					Random r = new Random();
					
					loc.add(0, -3, -r.nextInt(10)-2);
					loc.setDirection(target2.toVector().subtract(loc.toVector()));
					
					Fireball smallMet = (Fireball)target.getWorld().spawnEntity(loc, EntityType.FIREBALL);
					
					smallMet.setBounce(false);
					smallMet.setVelocity(target2.toVector().subtract(loc.toVector()).multiply(0.05));
				}
				
				@Override
				public void cancel() {
					super.cancel();
					
					TASKS.remove(this);
					
					meteor.remove();
					
					for(Fireball fireball: ArenaManager.getManager().getRandomLocOnArena().getWorld().getEntitiesByClass(Fireball.class))
						fireball.remove();
				}
				
			}).runTaskTimer(ArenaStream.getInstance(), 1, 1);
			
			return true;
		}
	},
	
	FLOORLAVA {
		public boolean execute(Viewer viewer) {
			ArrayList<Block> blocks = ArenaManager.getManager().getArena().getArena();
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					for(Block b: blocks)
						b.setType(Material.LAVA);
					
					addTask(new BukkitRunnable() {
						
						@Override
						public void run() {
							for(Block b: blocks)
								b.setType(MainConfig.getInstance().getMaterialForArena());
						}
						
						@Override
						public void cancel() {
							super.cancel();
							
							TASKS.remove(this);
							
							for(Block b: blocks)
								b.setType(MainConfig.getInstance().getMaterialForArena());
						}
						
					}).runTaskLater(ArenaStream.getInstance(), TIME_LAVA/2);
				}
				
			}.runTaskLater(ArenaStream.getInstance(), TIME_LAVA);
			
			return true;
		}
	},
	
	ZOMBIE {
		public boolean execute(Viewer viewer) {
			Location loc = ArenaManager.getManager().getRandomLocOnArena();
			Zombie zombie = (Zombie)loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
			
			zombie.setVillager(false);
			zombie.setTarget(PlayerManager.getManager().getRandomPlayer());
			
			return true;
		}
	},
	
	SKELETON {
		public boolean execute(Viewer viewer) {
			Location loc = ArenaManager.getManager().getRandomLocOnArena();
			Skeleton skelet = (Skeleton)loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
			
			skelet.setTarget(PlayerManager.getManager().getRandomPlayer());
			
			return true;
		}
	},
	
	FREEZE {
		public boolean execute(Viewer viewer) {
			for(Player p: PlayerManager.getManager().getPlayersCanPlay())
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 65, 2));
			
			return true;
		}
	},
	
	PUMPKIN {
		public boolean execute(Viewer viewer) {
			for(Player p: PlayerManager.getManager().getPlayersCanPlay())
				p.getInventory().setHelmet(new ItemStack(Material.PUMPKIN));
			
			addTask(new BukkitRunnable() {
				
				@Override
				public void run() {
					for(Player p: PlayerManager.getManager().getPlayersCanPlay())
						p.getInventory().setHelmet(null);
				}
				
				@Override
				public void cancel() {
					super.cancel();
					
					TASKS.remove(this);
					
					for(Player p: PlayerManager.getManager().getPlayersCanPlay())
						p.getInventory().setHelmet(null);
				}
				
			}).runTaskLater(ArenaStream.getInstance(), TIME_PUMPKIN*20);
			
			return true;
		}
	},
	
	UPGRADE {
		public boolean execute(Viewer viewer) {
			return viewer.upgrade();
		}
	},
	
	SNOWBALL {
		public boolean execute(Viewer viewer) {
			Snowball snowball = viewer.getEntity().launchProjectile(Snowball.class);
			
			snowball.setBounce(false);
			snowball.setCustomNameVisible(true);
			snowball.setCustomName(viewer.getEntityName());
			
			return true;
		}
	},
	
	UFO {
		public boolean execute(Viewer viewer) {
			addTask(new BukkitRunnable() {
				private Location lastRay;
				private int ticks = 0;
				
				@Override
				public void run() {
					Location ray = ArenaManager.getManager().getRandomLocOnArena();
					
					if(lastRay != null)
						for(int i = 0; i < 50; i++) {
							lastRay.clone().add(0, i, 0).getBlock().setType(Material.AIR);
							lastRay.clone().add(1, i, 0).getBlock().setType(Material.AIR);
							lastRay.clone().add(-1, i, 0).getBlock().setType(Material.AIR);
							lastRay.clone().add(0, i, 1).getBlock().setType(Material.AIR);
							lastRay.clone().add(0, i, -1).getBlock().setType(Material.AIR);
						}
					
					if(++ticks > 5) {
						cancel();
						return;
					}
					
					for(int i = 0; i < 50; i++) {
						ray.clone().add(0, i, 0).getBlock().setType(Material.STAINED_GLASS_PANE);
						ray.clone().add(1, i, 0).getBlock().setType(Material.STAINED_GLASS_PANE);
						ray.clone().add(-1, i, 0).getBlock().setType(Material.STAINED_GLASS_PANE);
						ray.clone().add(0, i, 1).getBlock().setType(Material.STAINED_GLASS_PANE);
						ray.clone().add(0, i, -1).getBlock().setType(Material.STAINED_GLASS_PANE);
					}
					
					Player p = PlayerManager.getManager().getRandomPlayer();
					
					for(int i = 0; i < 5; i++) {
						Endermite endermite = (Endermite)ray.getWorld().spawnEntity(ray, EntityType.ENDERMITE);
						
						endermite.setMaxHealth(5);
						endermite.setHealth(5);
						endermite.setTarget(p);
					}
					
					lastRay = ray;
				}
				
				@Override
				public void cancel() {
					super.cancel();
					
					TASKS.remove(this);
					
					if(lastRay != null)
						for(int i = 0; i < 50; i++) {
							lastRay.clone().add(0, i, 0).getBlock().setType(Material.AIR);
							lastRay.clone().add(1, i, 0).getBlock().setType(Material.AIR);
							lastRay.clone().add(-1, i, 0).getBlock().setType(Material.AIR);
							lastRay.clone().add(0, i, 1).getBlock().setType(Material.AIR);
							lastRay.clone().add(0, i, -1).getBlock().setType(Material.AIR);
						}
				}
				
			}).runTaskTimer(ArenaStream.getInstance(), 20, TIMER_UFO*20);
			
			return true;
		}
	},
	
	CTHULHU {
		public boolean execute(Viewer viewer) {
			Location loc = ArenaManager.getManager().getRandomLocOnArena();
			Zombie zomb = (Zombie)loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
			Squid squid = (Squid)loc.getWorld().spawnEntity(loc, EntityType.SQUID);
			
			zomb.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*60*60, 1, false, false));
			zomb.setMaxHealth(200);
			zomb.setHealth(200);
			zomb.setMaximumNoDamageTicks(20*60*60);
			zomb.setNoDamageTicks(20*60*60);
			zomb.setTarget(PlayerManager.getManager().getRandomPlayer());
			zomb.setBaby(true);
			zomb.setVillager(false);
			
			squid.setRemainingAir(15*20*60);
			squid.setMaxHealth(40);
			squid.setHealth(40);
			
			addTask(new BukkitRunnable() {
				
				@Override
				public void run() {
					if(squid.isDead() || zomb.isDead()) {
						cancel();
						
						return;
					}
					
					zomb.setPassenger(squid);
					zomb.setHealth(200);
					
					squid.setRemainingAir(15*20*60);
					
					Location locZomb = zomb.getLocation();
					Blaze blaze = (Blaze)locZomb.getWorld().spawnEntity(locZomb, EntityType.BLAZE);
					
					blaze.setMaxHealth(1);
					blaze.setHealth(1);
					blaze.setTarget(PlayerManager.getManager().getRandomPlayer());
				}
				
				@Override
				public void cancel() {
					super.cancel();
					
					TASKS.remove(this);
					
					squid.remove();
					zomb.remove();
				}
				
			}).runTaskTimer(ArenaStream.getInstance(), 20, TIMER_CTHULHU*20);
			
			return true;
		}
	},
	
	ADDTIME {
		public boolean execute(Viewer viewer) {
			ArenaManager.getManager().addTime(TIME_ADD);
			
			return true;
		}
	},
	
	ARROWRAIN {
		public boolean execute(Viewer viewer) {
			addTask(new BukkitRunnable() {
				private int ticks = 0;
				
				@Override
				public void run() {
					if(++ticks > 6*20) {
						cancel();
						return;
					}
					
					for(int i = 0; i < 5; i++) {
						Location loc = ArenaManager.getManager().getRandomLocOnArena().add(0, 11, 0);
						Arrow arrow = (Arrow)loc.getWorld().spawnEntity(loc, EntityType.ARROW);
						
						arrow.setBounce(false);
						arrow.setCritical(true);
						arrow.setFireTicks(100);
						arrow.setKnockbackStrength(5);
					}
					
					for(Arrow arrow: ArenaManager.getManager().getRandomLocOnArena().getWorld().getEntitiesByClass(Arrow.class))
						if(arrow.isOnGround())
							arrow.remove();
				}
				
				@Override
				public void cancel() {
					super.cancel();
					
					for(Arrow arrow: ArenaManager.getManager().getRandomLocOnArena().getWorld().getEntitiesByClass(Arrow.class))
						arrow.remove();
				}
				
			}).runTaskTimer(ArenaStream.getInstance(), 10, TIMER_ARROW_RAIN);
			
			return true;
		}
	};
	
	
	private static final ArrayList<BukkitRunnable> TASKS = new ArrayList<>();
	private static final int TIME_ADD = 60; //secs
	private static final int TIME_LAVA = 30; //ticks (need >2)
	private static final int TIME_PUMPKIN = 15; //secs
	private static final int TIMER_ARROW_RAIN = 1; //ticks
	private static final int TIMER_CTHULHU = 3; //secs
	private static final int TIMER_UFO = 5; //secs
	
	public static void cancelAll() {
		for(BukkitRunnable task: new ArrayList<>(TASKS))
			task.cancel();
	}
	
	
	protected BukkitRunnable addTask(BukkitRunnable run) {
		TASKS.add(run);
		
		return run;
	}
	
	public abstract boolean execute(Viewer viewer);
}

package ru.dseymo.arenastream.viewer.boss;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.arenastream.player.PlayerManager;

public enum Ability {
	POISON_ARROW {
		public void execute(Boss boss, int level) {
			if(level < 1)
				return;
			else if(level > 4)
				level = 4;
			
			int _lvl = level;;
			
			Listener listener = new Listener() {
				@EventHandler
				public void hit(EntityDamageByEntityEvent e) {
					Entity victim = e.getEntity();
					
					if(victim.getType() == EntityType.PLAYER && e.getDamager().getType() == EntityType.ARROW) {
						Player p = (Player)victim;
						PotionEffect lastEffect = null;
						
						for(PotionEffect effect: p.getActivePotionEffects())
							if(effect.getType().equals(PotionEffectType.POISON)) {
								lastEffect = effect;
								
								p.removePotionEffect(effect.getType());
								
								break;
							}
						
						p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 25 + (lastEffect != null ? lastEffect.getDuration() : 0), _lvl-1));
					}
				}
			};
			
			Bukkit.getPluginManager().registerEvents(listener, ArenaStream.getInstance());
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					EntityDamageByEntityEvent.getHandlerList().unregister(listener);
				}
				
			}.runTaskLater(ArenaStream.getInstance(), 100);
		}
	},
	
	REGENERATION {
		public void execute(Boss boss, int level) {
			if(level < 1)
				return;
			else if(level > 20)
				level = 20;
			
			BukkitTask task = new BukkitRunnable() {
				
				@Override
				public void run() {
					LivingEntity ent = boss.getEntity();
					
					if(ent.getHealth()+1 <= ent.getMaxHealth())
						ent.setHealth(ent.getHealth()+1);
					else
						ent.setHealth(ent.getMaxHealth());
				}
				
			}.runTaskTimer(ArenaStream.getInstance(), 1, 20 - (level-1));
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					task.cancel();
				}
				
			}.runTaskLater(ArenaStream.getInstance(), 100);
		}
	},
	
	HOMING_ARROW {
		public void execute(Boss boss, int level) {
			if(level < 1)
				return;
			
			new BukkitRunnable() {
				private int ticks = 0;
				private int maxTicks = 100 + (level-1)*20;
				
				@Override
				public void run() {
					if(++ticks > maxTicks) {
						cancel();
						return;
					}
					
					for(Arrow arrow: boss.getEntity().getWorld().getEntitiesByClass(Arrow.class)) {
						if(arrow.isOnGround())
							continue;
						
						Player p = null;
						
						for(Entity ent: arrow.getNearbyEntities(20, 5, 20))
							if(ent.getType() == EntityType.PLAYER) {
								p = (Player)ent;
								break;
							}
						
						if(p != null)
							arrow.setVelocity(p.getLocation().toVector().subtract(arrow.getLocation().toVector()).normalize());
					}
				}
				
			}.runTaskTimer(ArenaStream.getInstance(), 1, 1);
		}
	},
	
	FAST_ARCHERY {
		public void execute(Boss boss, int level) {
			if(level < 1)
				return;
			else if(level > 10)
				level = 10;
			
			BukkitTask task = new BukkitRunnable() {
				
				@Override
				public void run() {
					boss.getEntity().launchProjectile(Arrow.class);
				}
				
			}.runTaskTimer(ArenaStream.getInstance(), 1, 10 - (level-1));
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					task.cancel();
				}
				
			}.runTaskLater(ArenaStream.getInstance(), 100);
		}
	},
	
	SPAWN_ASSISTANT {
		public void execute(Boss boss, int level) {
			if(level < 1)
				return;
			
			Location loc = boss.getEntity().getLocation();
			
			Zombie zomb = (Zombie)loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
			
			zomb.setBaby(true);
			zomb.setVillager(false);
			zomb.setMaxHealth(3 + (level-1)*3);
			zomb.setHealth(3 + (level-1)*3);
			zomb.setTarget(PlayerManager.getManager().getRandomPlayer());
		}
	};
	
	public abstract void execute(Boss boss, int level);
}

package ru.dseymo.arenastream.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.arenastream.arena.Arena;
import ru.dseymo.arenastream.arena.ArenaManager;
import ru.dseymo.arenastream.viewer.boss.HoloBoss;

public class PlayerRunnable extends BukkitRunnable {
	
	public PlayerRunnable() {
		runTaskTimer(ArenaStream.getInstance(), 20, 20);
	}
	
	@Override
	public void run() {
		ArenaManager arenaMan = ArenaManager.getManager();
		
		if(!arenaMan.isCreated())
			return;
		
		HoloBoss.updateHolos();
		
		Arena arena = arenaMan.getArena();
		PlayerManager playerMan = PlayerManager.getManager();
		
		for(Player p: Bukkit.getOnlinePlayers()) {
			p.setSaturation(20);
			p.removePotionEffect(PotionEffectType.REGENERATION);
			p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			p.removePotionEffect(PotionEffectType.SPEED);
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 25, 2));
			p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 25, 0));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 25, 1));
			
			if(playerMan.isPlayerCanPlay(p))
				arena.addPlayer(p);
			else
				arena.removePlayer(p);
		}
	}

}

package ru.dseymo.arenastream.viewer.boss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import ru.dseymo.arenastream.arena.ArenaManager;
import ru.dseymo.arenastream.player.PlayerManager;

public class Skill extends BukkitRunnable {
	private Boss boss;
	HashMap<Ability, Integer> abilities = new HashMap<>();
	ArrayList<PotionEffect> effects = new ArrayList<>();
	
	public Skill(Boss boss) {
		this.boss = boss;
	}
	
	@Override
	public void run() {
		boss.getEntity().teleport(ArenaManager.getManager().getRandomLocOnArena());
		boss.getEntity().setTarget(PlayerManager.getManager().getRandomPlayer());
		
		for(Entry<Ability, Integer> ability: abilities.entrySet())
			ability.getKey().execute(boss, ability.getValue());
		
		for(PotionEffect effect: effects)
			boss.getEntity().addPotionEffect(effect);
	}
	
}

package ru.dseymo.arenastream.viewer.challenge.list;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;

import ru.dseymo.arenastream.arena.ArenaManager;
import ru.dseymo.arenastream.player.PlayerManager;
import ru.dseymo.arenastream.viewer.challenge.Challenge;

public class KillMobsChallenge extends Challenge {
	private ArrayList<Monster> entities = new ArrayList<>();
	
	public KillMobsChallenge(int durationSecs, int cost, String message) {
		super(durationSecs, cost, message);
	}
	
	
	@Override
	protected void started() {
		World w = ArenaManager.getManager().getRandomLocOnArena().getWorld();
		
		for(int i = 0; i < 2; i++) {
			Zombie zomb = (Zombie)w.spawnEntity(ArenaManager.getManager().getRandomLocOnArena(), EntityType.ZOMBIE);
			
			zomb.setBaby(false);
			zomb.setVillager(false);
			zomb.setTarget(PlayerManager.getManager().getRandomPlayer());
			
			entities.add(zomb);
		}
		
		for(int i = 0; i < 2; i++) {
			Skeleton skelet = (Skeleton)w.spawnEntity(ArenaManager.getManager().getRandomLocOnArena(), EntityType.SKELETON);
			
			skelet.setTarget(PlayerManager.getManager().getRandomPlayer());
			
			entities.add(skelet);
		}
	}
	
	private boolean isWin() {
		for(Monster monster: entities)
			if(!monster.isDead())
				return false;
		
		return true;
	}
	
	@Override
	protected void tick(boolean isLastTick) {
		if(isWin()) {
			for(Player p: PlayerManager.getManager().getPlayersCanPlay())
				p.setMaxHealth(p.getMaxHealth()+2);
			
			cancel();
		} else if(isLastTick)
			for(Player p: PlayerManager.getManager().getPlayersCanPlay())
				if(p.getMaxHealth() > 4)
					p.setMaxHealth(p.getMaxHealth()-4);
	}

	@Override
	protected void stopped() {
		for(Monster monster: entities)
			monster.remove();
		
		entities.clear();
	}

}

package ru.dseymo.arenastream.viewer.boss;

import org.bukkit.Location;

import lombok.Getter;
import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.libutils.Manager;

@Getter
public class BossManager implements Manager {
	public static BossManager getManager() {
		return (BossManager)ArenaStream.getManager(BossManager.class);
	}
	
	
	private Boss current;
	private int collected = 0;
	
	@Override
	public void setup() {}

	@Override
	public void unsetup() {
		removeBoss();
		
		collected = 0;
	}
	
	
	public void paid(int money) {
		collected += money;
	}
	
	public boolean isCanSpawn() {
		return current == null && collected >= BossConfig.getInstance().getCostBoss();
	}
	
	public Boss trySpawn(Location loc) {
		if(isCanSpawn()) {
			collected = 0;
			
			return current = BossConfig.getInstance().spawnBoss(loc);
		}
		
		return null;
	}
	
	public void removeBoss() {
		if(current != null)
			current.remove();
	}
	
	public boolean isDead() {
		return current == null || current.getEntity().isDead();
	}
	
	void bossDespawned() {
		current = null;
	}

}

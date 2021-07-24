package ru.dseymo.arenastream.viewer.boss;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.libutils.mc.bukkit.hologram.AttachedHologram;

public class HoloBoss {
	private static final HashMap<UUID, HoloBoss> BOARDS = new HashMap<>();
	
	public static void add(Player p) {
		UUID uuid = p.getUniqueId();
		
		if(!BOARDS.containsKey(uuid))
			BOARDS.put(uuid, new HoloBoss(p));
	}
	
	public static void updateHolos() {
		for(HoloBoss board: BOARDS.values())
			board.update();
	}
	
	public static void remove(Player p) {
		UUID uuid = p.getUniqueId();
		
		if(BOARDS.containsKey(uuid)) {
			BOARDS.get(uuid).holo.remove();
			
			BOARDS.remove(uuid);
		}
	}
	
	public static void removeAll() {
		for(HoloBoss board: BOARDS.values())
			board.holo.remove();
		
		BOARDS.clear();
	}
	
	
	private AttachedHologram holo;
	
	private HoloBoss(Player p) {
		holo = new AttachedHologram(ArenaStream.getInstance());
		
		holo.setPlayer(p);
		holo.getHolo().addLines("&6Boss:", " ");
		holo.setDistanceFromPlayer(7.5f);
		holo.setX(35);
		holo.setY(-4);
	}
	
	public void update() {
		Boss boss = BossManager.getManager().getCurrent();
		
		if(boss != null) {
			LivingEntity ent = boss.getEntity();
			String str = "&2&l";
			double hpProc = ent.getHealth()/ent.getMaxHealth();
			int hp = (int)(hpProc*10);
			
			for(int i = 0; i < 10; i++) {
				str += '\u25A0';
				
				if(hp == i)
					str += "&f&l";
			}
			
			holo.getHolo().setText(1, "&lHP: " + str + " &r&l" + ((int)(hpProc*100)) + "%");
		} else
			holo.getHolo().setText(1, "&8&lNo Boss (Collected: " + BossManager.getManager().getCollected() + "/" + BossConfig.getInstance().getCostBoss() + ")");
	}
	
}

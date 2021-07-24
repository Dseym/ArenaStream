package ru.dseymo.arenastream.player;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.arenastream.viewer.Viewer;
import ru.dseymo.arenastream.viewer.action.Action;
import ru.dseymo.libutils.mc.bukkit.hologram.AttachedHologram;
import ru.dseymo.libutils.mc.bukkit.hologram.Hologram;

public class HoloBoard {
	private static final HashMap<UUID, HoloBoard> BOARDS = new HashMap<>();
	
	public static void action(Viewer viewer, Action action) {
		for(HoloBoard board: BOARDS.values())
			board.addAction(viewer, action);
	}
	
	public static void add(Player p) {
		UUID uuid = p.getUniqueId();
		
		if(!BOARDS.containsKey(uuid))
			BOARDS.put(uuid, new HoloBoard(p));
	}
	
	public static void remove(Player p) {
		UUID uuid = p.getUniqueId();
		
		if(BOARDS.containsKey(uuid)) {
			BOARDS.get(uuid).holo.remove();
			
			BOARDS.remove(uuid);
		}
	}
	
	public static void removeAll() {
		for(HoloBoard board: BOARDS.values())
			board.holo.remove();
		
		BOARDS.clear();
	}
	
	
	private AttachedHologram holo;
	
	private HoloBoard(Player p) {
		holo = new AttachedHologram(ArenaStream.getInstance());
		
		holo.setPlayer(p);
		holo.getHolo().addLines("&6Last Actions:", " ", " ", " ", " ", " ");
		holo.setDistanceFromPlayer(7.5f);
		holo.setX(-35);
		holo.setY(-4);
	}
	
	public void addAction(Viewer viewer, Action action) {
		if(!action.getMessage().isEmpty()) {
			Hologram holo = this.holo.getHolo();
			
			holo.setText(5, holo.getText(4));
			holo.setText(4, holo.getText(3));
			holo.setText(3, holo.getText(2));
			holo.setText(2, holo.getText(1));
			holo.setText(1, action.getMessage().replace("%viewer%", viewer.getName()));
		}
	}
	
}

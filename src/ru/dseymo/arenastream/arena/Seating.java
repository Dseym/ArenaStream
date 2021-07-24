package ru.dseymo.arenastream.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import lombok.Getter;
import ru.dseymo.arenastream.viewer.Viewer;

@Getter
public class Seating {
	private Location loc;
	private Viewer viewer;
	
	public Seating(Location loc) {
		this.loc = loc;
	}
	
	public void setViewer(Viewer viewer) {
		this.viewer = viewer;
		viewer.getEntity().teleport(loc);
	}
	
	public void clear() {
		viewer.getEntity().teleport(new Location(Bukkit.getWorlds().get(0), 0, 10, 0));
		viewer = null;
	}
	
	public boolean isFree() {
		return viewer == null;
	}
	
}

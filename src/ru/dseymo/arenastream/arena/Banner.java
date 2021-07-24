package ru.dseymo.arenastream.arena;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.scheduler.BukkitRunnable;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.libutils.mc.bukkit.hologram.Hologram;

public class Banner {
	private static final int TIME_BANNER = 25; //secs
	
	private static Banner BANNER;
	private static HashMap<String, Banner> IN_PROCESS = new HashMap<>();
	
	public static Banner tryCreateBanner(String text) {
		if(BANNER != null || text.length() > 240)
			return null;
		
		Arena arena = ArenaManager.getManager().getArena();
		Banner banner = IN_PROCESS.get(text);
		
		if(banner == null)
			banner = new Banner(text);
		
		if(++banner.supporters >= Math.floor((float)arena.getViewers().size()/2f)) {
			IN_PROCESS.clear();
			
			BANNER = banner;
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					BANNER.remove();
					
					BANNER = null;
				}
				
			}.runTaskLater(ArenaStream.getInstance(), TIME_BANNER*20);
			
			return banner;
		}
		
		return null;
	}
	
	
	private Hologram holo;
	private int supporters = 0;
	
	Banner(String text) {
		holo = new Hologram(ArenaStream.getInstance(), ArenaManager.getManager().getArena().getBanner());
		
		for(String line: split(text, 48))
			holo.addLines(line);
		
		holo.setReloadForAll(true);
	}
	
	private ArrayList<String> split(String text, int size) {
		ArrayList<String> ret = new ArrayList<String>((text.length() + size - 1) / size);

	    for (int start = 0; start < text.length(); start += size)
	        ret.add(text.substring(start, Math.min(text.length(), start + size)));
	    
	    return ret;
	}
	
	public void remove() {
		holo.remove();
	}
	
}

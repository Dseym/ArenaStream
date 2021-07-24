package ru.dseymo.arenastream.viewer;

import org.bukkit.scheduler.BukkitRunnable;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.arenastream.arena.ArenaManager;
import ru.dseymo.arenastream.arena.Seating;
import ru.dseymo.arenastream.viewer.code.Code;
import ru.dseymo.libutils.mc.bukkit.hologram.Hologram;

public class ViewerRunnable extends BukkitRunnable {
	
	public ViewerRunnable() {
		runTaskTimer(ArenaStream.getInstance(), 20, 60*20);
	}
	
	@Override
	public void run() {
		if(!ArenaManager.getManager().isEnabled())
			return;
		
		if(Math.random() > 0.5) {
			String code = "co" + (int)(Math.random()*1000f) + "de";
			
			if(!Code.contains(code)) {
				Hologram holo = new Hologram(ArenaStream.getInstance(), ArenaManager.getManager().getRandomLocOnArena());
				
				holo.addLines("&2&lCode:", "&6&o" + code);
				holo.setReloadForAll(true);
				
				Code.addCode(new Code(code, (int)(50 + Math.random()*100), holo));
			}
		}
		
		for(Seating seating: ArenaManager.getManager().getArena().getViewers()) {
			seating.getViewer().streakOnline++;
			
			ViewerManager.getManager().depositMoney(seating.getViewer(), 30);
		}
	}

}

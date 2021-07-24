package ru.dseymo.arenastream.viewer;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.arenastream.arena.Arena;
import ru.dseymo.arenastream.arena.ArenaManager;
import ru.dseymo.libutils.Manager;

public class ViewerManager implements Manager, Listener {
	public static ViewerManager getManager() {
		return (ViewerManager)ArenaStream.getManager(ViewerManager.class);
	}
	
	
	private ViewerRunnable run;
	
	@Override
	public void setup() {
		run = new ViewerRunnable();
		
		Bukkit.getPluginManager().registerEvents(this, ArenaStream.getInstance());
	}

	@Override
	public void unsetup() {
		run.cancel();
		
		PlayerInteractAtEntityEvent.getHandlerList().unregister(this);
		EntityDamageEvent.getHandlerList().unregister(this);
	}
	
	
	public float getFactor(Viewer viewer) {
		Arena arena = ArenaManager.getManager().getArena();
		
		return 1 + ((float)arena.getViewers().size()/(float)arena.getStandsSize()) + ((float)viewer.streakOnline*0.075f);
	}
	
	public void depositMoney(Viewer viewer, int money) {
		viewer.deposit((int)(money*getFactor(viewer)));
	}
	
	@EventHandler
	public void clickToViewer(PlayerInteractAtEntityEvent e) {
		if(e.getRightClicked().hasMetadata("viewer"))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void damageViewer(EntityDamageEvent e) {
		if(e.getEntity().hasMetadata("viewer"))
			e.setCancelled(true);
	}

}

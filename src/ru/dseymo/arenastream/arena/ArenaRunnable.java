package ru.dseymo.arenastream.arena;

import org.bukkit.scheduler.BukkitRunnable;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.arenastream.MainConfig;
import ru.dseymo.arenastream.player.PlayerManager;
import ru.dseymo.arenastream.viewer.boss.BossManager;

public class ArenaRunnable extends BukkitRunnable {
	private int timeStartIn = MainConfig.getInstance().getTimeStartIn();
	private int timeStopIn = MainConfig.getInstance().getTimeStopIn();
	int timeForStart = timeStartIn;
	int timeForStop = timeStopIn;
	
	public ArenaRunnable() {
		runTaskTimer(ArenaStream.getInstance(), 20, 20);
	}
	
	@Override
	public void run() {
		if(!ArenaStream.getYoutube().isConnected())
			return;
		
		ArenaManager manager = ArenaManager.getManager();
		
		if(BossManager.getManager().isDead())
			BossManager.getManager().removeBoss();
		
		if(manager.isEnabled()) {
			timeForStart = timeStartIn;
			
			manager.getArena().clearOfflineViewers();
			
			if(--timeForStop < 0)
				manager.playersWon();
			else if(PlayerManager.getManager().getPlayersCanPlay().size() == 0)
				manager.viewersWon();
		} else if(manager.isCreated()) {
			timeForStop = timeStopIn;
			
			if(PlayerManager.getManager().getPlayersCanPlay().size() == 0)
				timeForStart = timeStartIn;
			else if(--timeForStart < 0)
				manager.enable();
		}
	}

}

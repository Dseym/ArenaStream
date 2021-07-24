package ru.dseymo.arenastream.viewer.challenge;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.arenastream.viewer.Viewer;
import ru.dseymo.libutils.Manager;
import ru.dseymo.libutils.mc.bukkit.Chat;

public class ChallengeManager implements Manager {
	public static ChallengeManager getManager() {
		return (ChallengeManager)ArenaStream.getManager(ChallengeManager.class);
	}
	
	
	private Challenge current;
	
	@Override
	public void setup() {}

	@Override
	public void unsetup() {
		if(current != null)
			current.cancel();
	}
	
	public boolean isChallengeRunning() {
		return current != null;
	}
	
	public boolean tryRunChallenge(Challenge challenge, Viewer viewer) {
		if(isChallengeRunning())
			return false;
		
		challenge.runTaskTimer(ArenaStream.getInstance(), 20, 20);
		
		current = challenge;
		
		if(!challenge.getMessage().isEmpty())
			Chat.INFO.sendAll(challenge.getMessage().replace("%viewer%", viewer.getName()));
		
		return true;
	}
	
	void challengeStopped() {
		current = null;
	}

}

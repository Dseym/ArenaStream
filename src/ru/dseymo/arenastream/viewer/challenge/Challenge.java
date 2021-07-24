package ru.dseymo.arenastream.viewer.challenge;

import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;

@Getter
public abstract class Challenge extends BukkitRunnable {
	private int durationSecs, ticks = 0, cost;
	private String message;
	
	public Challenge(int durationSecs, int cost, String message) {
		this.durationSecs = durationSecs;
		this.cost = cost;
		this.message = message;
	}
	
	@Override
	public void run() {
		if(ticks == 0)
			started();
		else
			tick(ticks+1 > durationSecs);
		
		if(++ticks > durationSecs)
			cancel();
	}
	
	@Override
	public void cancel() {
		super.cancel();
		
		stopped();
		
		ChallengeManager.getManager().challengeStopped();
	}
	
	protected abstract void started();
	
	protected abstract void tick(boolean isLastTick);
	
	protected abstract void stopped();

}

package ru.dseymo.arenastream.arena;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.libutils.mc.bukkit.board.Board;

public class ArenaBoard extends Board {

	public ArenaBoard() {
		super(ArenaStream.getInstance(), "&1Arena&4Stream");
		
		setTimeUpdate(10);
	}

	@Override
	public void update() {
		ArenaManager arenaMan = ArenaManager.getManager();
		
		if(!arenaMan.isCreated())
			return;
		
		clearLines();
		
		Arena arena = arenaMan.getArena();
		
		setLines((arenaMan.isEnabled() ? "&7&lEnd in" : "&7&lStart in") + "                ",
				 (arenaMan.isEnabled() ? arenaMan.run.timeForStop : arenaMan.run.timeForStart) + " ",
				 " ",
				 "&6&lSeatings",
				 arena.getViewers().size() + "/" + arena.getStandsSize(),
				 "  ",
				 "&2&lPlayers",
				 arena.getPlayers().size() + "  ");
	}

}

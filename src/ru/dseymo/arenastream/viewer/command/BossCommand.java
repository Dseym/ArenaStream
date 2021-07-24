package ru.dseymo.arenastream.viewer.command;

import java.util.Arrays;

import org.apache.commons.lang.math.NumberUtils;

import ru.dseymo.arenastream.arena.ArenaManager;
import ru.dseymo.arenastream.viewer.AbstractCommand;
import ru.dseymo.arenastream.viewer.Viewer;
import ru.dseymo.arenastream.viewer.boss.BossManager;
import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.youtubestream.Author.AuthorRole;
import ru.dseymo.youtubestream.Command;

public class BossCommand extends AbstractCommand {

	public BossCommand() {
		super(Arrays.asList(AuthorRole.VIEWER, AuthorRole.MODERATOR));
	}
	
	@Override
	public void onExecute(Viewer viewer, Command cmd) {
		String[] args = cmd.getArgs();
		
		if(args.length == 0 || !NumberUtils.isNumber(args[0]))
			return;
		
		int cost = Integer.parseInt(args[0]);
		
		if(Debug.ENABLE || viewer.has(cost)) {
			viewer.withdraw(cost);
			
			BossManager.getManager().paid(cost);
			
			Chat.INFO.sendAll(viewer.getName() + " paid money for the boss!");
			
			if(BossManager.getManager().trySpawn(ArenaManager.getManager().getRandomLocOnArena()) != null)
				Chat.INFO.sendAll("&4&l!!! THE BOSS WAS SPAWNED !!!");
		}
	}

}

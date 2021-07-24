package ru.dseymo.arenastream.viewer.command;

import java.util.Arrays;

import ru.dseymo.arenastream.viewer.AbstractCommand;
import ru.dseymo.arenastream.viewer.Viewer;
import ru.dseymo.arenastream.viewer.action.Action;
import ru.dseymo.arenastream.viewer.action.ActionConfig;
import ru.dseymo.arenastream.viewer.action.ActionManager;
import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.youtubestream.Author.AuthorRole;
import ru.dseymo.youtubestream.Command;

public class ActionCommand extends AbstractCommand {
	
	public ActionCommand() {
		super(Arrays.asList(AuthorRole.VIEWER, AuthorRole.MODERATOR));
	}
	
	@Override
	public void onExecute(Viewer viewer, Command cmd) {
		String[] args = cmd.getArgs();
		
		if(args.length == 0)
			return;
		
		Action action = ActionConfig.getInstance().getAction(args[0]);
		
		if(action != null && ActionManager.getManager().isActionAccess(action, viewer) && (Debug.ENABLE || viewer.has(action.getCost())))
			ActionManager.getManager().executeAction(action, viewer);
	}

}

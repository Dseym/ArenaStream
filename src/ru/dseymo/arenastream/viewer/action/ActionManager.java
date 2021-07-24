package ru.dseymo.arenastream.viewer.action;

import java.util.HashMap;

import org.bukkit.Bukkit;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.arenastream.player.HoloBoard;
import ru.dseymo.arenastream.viewer.Viewer;
import ru.dseymo.libutils.Manager;
import ru.dseymo.libutils.mc.bukkit.Chat;

public class ActionManager implements Manager {
	public static ActionManager getManager() {
		return (ActionManager)ArenaStream.getManager(ActionManager.class);
	}
	
	
	private HashMap<Action, Viewer> lastActions = new HashMap<>();
	
	@Override
	public void setup() {}

	@Override
	public void unsetup() {
		lastActions.clear();
	}
	
	
	public void onUpdate() {
		lastActions.clear();
	}
	
	public void executeAction(Action action, Viewer viewer) {
		try {
			if(!BuiltInAction.valueOf(action.getId().toUpperCase()).execute(viewer))
				return;
		} catch (Exception e) {}
		
		lastActions.put(action, viewer);
		
		for(String _cmd: action.getCmds())
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), _cmd);
		
		if(!action.getMessage().isEmpty())
			Chat.INFO.sendAll(action.getMessage().replace("%viewer%", viewer.getName()));
		
		viewer.withdraw(action.getCost());
		
		HoloBoard.action(viewer, action);
	}
	
	public boolean isActionAccess(Action action, Viewer viewer) {
		if(lastActions.containsValue(viewer))
			return false;
		else
			return action.isCanTimesExecute() || !lastActions.containsKey(action);
	}

}

package ru.dseymo.arenastream.viewer.action;

import java.io.File;

import lombok.Getter;
import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.libutils.mc.bukkit.Config;

public class ActionConfig extends Config {
	@Getter
	private static ActionConfig instance = new ActionConfig(ArenaStream.getInstance().getDataFolder());
	
	
	public ActionConfig(File file) {
		super(new File(file + "/actions.yml"), true);
	}
	
	public Action getAction(String aliase) {
		for(String action: getKeys(false)) {
			String path = action + ".";
			
			if(getStringList(path + "aliases").contains(aliase)) {
				String message = getString(path + "message");
				
				if(message == null)
					message = "";
				
				return new Action(action, message, getInt(path + "cost"), getBoolean(path + "can_times_execute"), getStringList(path + "cmds"));
			}
		}
		
		return null;
	}
	
}

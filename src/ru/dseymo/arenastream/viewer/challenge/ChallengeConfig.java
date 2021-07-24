package ru.dseymo.arenastream.viewer.challenge;

import java.io.File;

import lombok.Getter;
import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.libutils.ReflUtil;
import ru.dseymo.libutils.mc.bukkit.Config;

public class ChallengeConfig extends Config {
	@Getter
	private static ChallengeConfig instance = new ChallengeConfig(ArenaStream.getInstance().getDataFolder());
	
	
	public ChallengeConfig(File file) {
		super(new File(file + "/challenge.yml"), true);
	}
	
	public Challenge getChallenge(String aliase) {
		for(String challenge: getKeys(false)) {
			String path = challenge + ".";
			
			if(getStringList(path + "aliases").contains(aliase))
				try {
					String message = getString(path + "message");
					
					if(message == null)
						message = "";
					
					return (Challenge)ReflUtil.instance(Class.forName(getString(path + "class_path")), new Class[] {int.class, int.class, String.class},
														getInt(path + "duration"), getInt(path + "cost"), message);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
		}
		
		return null;
	}
	
}

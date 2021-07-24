package ru.dseymo.arenastream;

import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import ru.dseymo.arenastream.arena.ArenaConfig;
import ru.dseymo.arenastream.arena.ArenaManager;
import ru.dseymo.arenastream.arena.cmd.ArenaCMD;
import ru.dseymo.arenastream.player.HoloBoard;
import ru.dseymo.arenastream.player.PlayerManager;
import ru.dseymo.arenastream.viewer.ViewerManager;
import ru.dseymo.arenastream.viewer.action.ActionConfig;
import ru.dseymo.arenastream.viewer.action.ActionManager;
import ru.dseymo.arenastream.viewer.boss.BossConfig;
import ru.dseymo.arenastream.viewer.boss.BossManager;
import ru.dseymo.arenastream.viewer.boss.HoloBoss;
import ru.dseymo.arenastream.viewer.challenge.ChallengeConfig;
import ru.dseymo.arenastream.viewer.challenge.ChallengeManager;
import ru.dseymo.arenastream.youtube.YoutubeAuthConfig;
import ru.dseymo.arenastream.youtube.YoutubeMessageListener;
import ru.dseymo.arenastream.youtube.cmd.YoutubeCMD;
import ru.dseymo.libutils.Manager;
import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.mc.bukkit.hologram.Hologram;
import ru.dseymo.youtubestream.YouTube;

public class ArenaStream extends JavaPlugin {
	private static HashMap<Class<?>, Manager> MANAGERS = new HashMap<>();
	@Getter
	private static ArenaStream instance;
	
	public static Manager getManager(Class<?> clazz) {
		return MANAGERS.get(clazz);
	}
	
	public static YouTube getYoutube() {
		return instance.youtube;
	}
	
	
	private YouTube youtube;
	
	@Override
	public void onEnable() {
		instance = this;
		
		initConfigs();
		initManagers();
		initCMD();
		
		Debug.ENABLE = MainConfig.getInstance().isDebugEnable();
		
		youtube = new YouTube();
		
		youtube.addListener(new YoutubeMessageListener());
		
		getLogger().info("Enabled!");
	}
	
	public void reloadConfigs() {
		MainConfig.getInstance().load();
		YoutubeAuthConfig.getInstance().load();
		ArenaConfig.getInstance().load();
		ActionConfig.getInstance().load();
		BossConfig.getInstance().load();
		ChallengeConfig.getInstance().load();
		
		Debug.ENABLE = MainConfig.getInstance().isDebugEnable();
	}
	
	private void initConfigs() {
		MainConfig.getInstance();
		YoutubeAuthConfig.getInstance();
		ArenaConfig.getInstance();
		ActionConfig.getInstance();
		BossConfig.getInstance();
		ChallengeConfig.getInstance();
	}
	
	private void initManagers() {
		MANAGERS.put(PlayerManager.class, new PlayerManager());
		MANAGERS.put(ArenaManager.class, new ArenaManager());
		MANAGERS.put(ActionManager.class, new ActionManager());
		MANAGERS.put(ViewerManager.class, new ViewerManager());
		MANAGERS.put(BossManager.class, new BossManager());
		MANAGERS.put(ChallengeManager.class, new ChallengeManager());
		
		for(Manager manager: MANAGERS.values())
			manager.setup();
	}
	
	private void initCMD() {
		new YoutubeCMD();
		new ArenaCMD();
		new ReloadArenaCMD().init();
	}
	
	@Override
	public void onDisable() {
		youtube.disconnect();
		
		HoloBoard.removeAll();
		HoloBoss.removeAll();
		Hologram.removeAll();
		
		for(Manager manager: MANAGERS.values())
			manager.unsetup();
		
		instance = null;
	}
	
}

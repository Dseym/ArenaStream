package ru.dseymo.arenastream.youtube;

import java.io.File;

import lombok.Getter;
import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.libutils.mc.bukkit.Config;

public class YoutubeAuthConfig extends Config {
	@Getter
	private static YoutubeAuthConfig instance = new YoutubeAuthConfig(ArenaStream.getInstance().getDataFolder());
	
	
	public YoutubeAuthConfig(File file) {
		super(new File(file + "/youtube_auth.yml"), true);
	}
	
	public String getGoogleAPI() {
		return getString("googleAPI");
	}
	
}

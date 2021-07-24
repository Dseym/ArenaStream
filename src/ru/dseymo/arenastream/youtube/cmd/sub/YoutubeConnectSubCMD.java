package ru.dseymo.arenastream.youtube.cmd.sub;

import org.bukkit.command.CommandSender;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.arenastream.youtube.YoutubeAuthConfig;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.bukkit.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.core.cmd.Args;
import ru.dseymo.youtubestream.Result;

public class YoutubeConnectSubCMD extends SubExtendCommand {

	public YoutubeConnectSubCMD() {
		super("connect", false, "", "[videoID] &6Connect with YouTube channel");
	}
	
	public boolean execute(CommandSender sender, Args args) {
		if(args.get(0) == null) {
			Chat.FAIL.send(sender, "Enter videoID (Example XWm8zjbM7A8)");
			return false;
		}
		
		String api = YoutubeAuthConfig.getInstance().getGoogleAPI();
		
		if(api == null) {
			Chat.FAIL.send(sender, "GoogleAPI not found in youtube_auth.yml");
			return false;
		}
		
		Result result = ArenaStream.getYoutube().connect(args.get(0), api);
		
		Chat.INFO.send(sender, result.getMessage());
		
		return true;
	}

}

package ru.dseymo.arenastream.youtube.cmd.sub;

import org.bukkit.command.CommandSender;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.bukkit.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.core.cmd.Args;
import ru.dseymo.youtubestream.YouTube;

public class YoutubeDisconnectSubCMD extends SubExtendCommand {
	
	public YoutubeDisconnectSubCMD() {
		super("disconnect", false, "", "&6Disconnect from YouTube channel");
	}
	
	public boolean execute(CommandSender sender, Args args) {
		YouTube yt = ArenaStream.getYoutube();
		
		if(!yt.isConnected()) {
			Chat.FAIL.send(sender, "No channel is connected");
			return false;
		}
		
		yt.disconnect();
		
		Chat.INFO.send(sender, "Success");
		
		return true;
	}
	
}

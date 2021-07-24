package ru.dseymo.arenastream.youtube.cmd.sub;

import org.bukkit.command.CommandSender;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.bukkit.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class YoutubeChangeTimerSubCMD extends SubExtendCommand {
	
	public YoutubeChangeTimerSubCMD() {
		super("changetimer", false, "", "[secs] &6Change chat update time (need to reconnect)");
	}
	
	public boolean execute(CommandSender sender, Args args) {
		if(args.getInt(0) == null) {
			Chat.FAIL.send(sender, "Enter update time (time>5)");
			return false;
		} else if(args.getInt(0) < 6) {
			Chat.FAIL.send(sender, "Time must be more than 5 secs");
			return false;
		}
		
		ArenaStream.getYoutube().setTimeUpdateChatInSec(args.getInt(0));
		
		Chat.SUCCESS.send(sender, "Success");
		
		return true;
	}
	
}

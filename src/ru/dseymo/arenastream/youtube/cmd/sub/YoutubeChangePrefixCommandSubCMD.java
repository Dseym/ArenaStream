package ru.dseymo.arenastream.youtube.cmd.sub;

import org.bukkit.command.CommandSender;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.bukkit.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class YoutubeChangePrefixCommandSubCMD extends SubExtendCommand {

	public YoutubeChangePrefixCommandSubCMD() {
		super("changeprefix", false, "", "[prexifChar] &6Change prefix for command (Example !)");
	}
	
	public boolean execute(CommandSender sender, Args args) {
		if(args.get(0) == null) {
			Chat.FAIL.send(sender, "Enter prefix (Example !)");
			return false;
		} else if(args.get(0).length() != 1) {
			Chat.FAIL.send(sender, "The prefix must be one character");
			return false;
		}
		
		ArenaStream.getYoutube().setPrefixCommand(args.get(0).charAt(0));
		
		Chat.SUCCESS.send(sender, "Success");
		
		return true;
	}

}

package ru.dseymo.arenastream.youtube.cmd;

import org.bukkit.command.CommandSender;

import ru.dseymo.arenastream.youtube.cmd.sub.YoutubeChangePrefixCommandSubCMD;
import ru.dseymo.arenastream.youtube.cmd.sub.YoutubeChangeTimerSubCMD;
import ru.dseymo.arenastream.youtube.cmd.sub.YoutubeConnectSubCMD;
import ru.dseymo.arenastream.youtube.cmd.sub.YoutubeDisconnectSubCMD;
import ru.dseymo.libutils.mc.bukkit.cmd.ExtendCommand;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class YoutubeCMD extends ExtendCommand {

	public YoutubeCMD() {
		super("youtube", false, "arenastream.youtube", "&6Options YouTube");
		
		addSubCMD(new YoutubeChangePrefixCommandSubCMD());
		addSubCMD(new YoutubeChangeTimerSubCMD());
		addSubCMD(new YoutubeConnectSubCMD());
		addSubCMD(new YoutubeDisconnectSubCMD());
	}
	
	public boolean execute(CommandSender sender, Args args) {
		help(sender);
		
		return false;
	}

}

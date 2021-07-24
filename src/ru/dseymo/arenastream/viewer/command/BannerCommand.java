package ru.dseymo.arenastream.viewer.command;

import java.util.Arrays;

import ru.dseymo.arenastream.arena.Banner;
import ru.dseymo.arenastream.viewer.AbstractCommand;
import ru.dseymo.arenastream.viewer.Viewer;
import ru.dseymo.youtubestream.Command;
import ru.dseymo.youtubestream.Author.AuthorRole;

public class BannerCommand extends AbstractCommand {
	
	public BannerCommand() {
		super(Arrays.asList(AuthorRole.VIEWER, AuthorRole.MODERATOR));
	}
	
	@Override
	public void onExecute(Viewer viewer, Command cmd) {
		String[] args = cmd.getArgs();
		String str = "";
		
		for(String arg: args)
			str += " " + arg;
		
		if(!str.isEmpty())
			Banner.tryCreateBanner(str.substring(1));
	}
	
}

package ru.dseymo.arenastream.viewer.command;

import java.util.Arrays;

import ru.dseymo.arenastream.viewer.AbstractCommand;
import ru.dseymo.arenastream.viewer.Viewer;
import ru.dseymo.arenastream.viewer.code.Code;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.youtubestream.Author.AuthorRole;
import ru.dseymo.youtubestream.Command;

public class CodeCommand extends AbstractCommand {

	public CodeCommand() {
		super(Arrays.asList(AuthorRole.VIEWER, AuthorRole.MODERATOR));
	}
	
	@Override
	public void onExecute(Viewer viewer, Command cmd) {
		String[] args = cmd.getArgs();
		
		if(args.length == 0)
			return;
		
		Code code = Code.giveCode(viewer, args[0]);
		
		if(code != null)
			Chat.INFO.sendAll(viewer.getName() + " entered the code: &6&o" + code.getCode() + "&7 - and received: &2&o" + code.getCost());
	}

}

package ru.dseymo.arenastream.viewer.command;

import java.util.Arrays;

import ru.dseymo.arenastream.viewer.AbstractCommand;
import ru.dseymo.arenastream.viewer.Viewer;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.youtubestream.Author.AuthorRole;
import ru.dseymo.youtubestream.Command;

public class MoneyBalanceCommand extends AbstractCommand {

	public MoneyBalanceCommand() {
		super(Arrays.asList(AuthorRole.VIEWER, AuthorRole.MODERATOR));
	}

	@Override
	public void onExecute(Viewer viewer, Command cmd) {
		Chat.NO_PREFIX.sendAll("&o" + viewer.getName() + " &7your money balance: &2&o" + viewer.getMoney());
	}

}

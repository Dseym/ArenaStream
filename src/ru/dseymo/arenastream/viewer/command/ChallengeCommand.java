package ru.dseymo.arenastream.viewer.command;

import java.util.Arrays;

import ru.dseymo.arenastream.viewer.AbstractCommand;
import ru.dseymo.arenastream.viewer.Viewer;
import ru.dseymo.arenastream.viewer.challenge.Challenge;
import ru.dseymo.arenastream.viewer.challenge.ChallengeConfig;
import ru.dseymo.arenastream.viewer.challenge.ChallengeManager;
import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.youtubestream.Command;
import ru.dseymo.youtubestream.Author.AuthorRole;

public class ChallengeCommand extends AbstractCommand {
	
	public ChallengeCommand() {
		super(Arrays.asList(AuthorRole.VIEWER, AuthorRole.MODERATOR));
	}

	@Override
	public void onExecute(Viewer viewer, Command cmd) {
		String[] args = cmd.getArgs();
		
		if(args.length == 0)
			return;
		
		Challenge challenge = ChallengeConfig.getInstance().getChallenge(args[0]);
		
		if(challenge != null && (Debug.ENABLE || viewer.has(challenge.getCost())))
			ChallengeManager.getManager().tryRunChallenge(challenge, viewer);
	}
	
}

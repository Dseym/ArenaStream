package ru.dseymo.arenastream.viewer;

import java.util.List;

import lombok.AllArgsConstructor;
import ru.dseymo.youtubestream.Author.AuthorRole;
import ru.dseymo.youtubestream.Command;

@AllArgsConstructor
public abstract class AbstractCommand {
	private List<AuthorRole> accessRoles;
	
	void execute(Viewer viewer, Command cmd) {
		if(isAccess(viewer, cmd))
			onExecute(viewer, cmd);
	}
	
	public boolean isAccess(Viewer viewer, Command cmd) {
		return accessRoles.contains(cmd.getAuthor().getRole());
	}
	
	public abstract void onExecute(Viewer viewer, Command cmd);
	
}

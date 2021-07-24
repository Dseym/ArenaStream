package ru.dseymo.arenastream.arena.cmd.sub;

import org.bukkit.command.CommandSender;

import ru.dseymo.arenastream.arena.ArenaManager;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.bukkit.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class DeleteArenaSubCMD extends SubExtendCommand {
	
	public DeleteArenaSubCMD() {
		super("delete", true, "", "&6Delete arena");
	}
	
	public boolean execute(CommandSender sender, Args args) {
		ArenaManager manager = ArenaManager.getManager();
		
		if(!manager.isCreated()) {
			Chat.FAIL.send(sender, "Arena not found");
			return false;
		}
		
		manager.removeArena();
		
		Chat.SUCCESS.send(sender, "Success");
		
		return true;
	}
	
}

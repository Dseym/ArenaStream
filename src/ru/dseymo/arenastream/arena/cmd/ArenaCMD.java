package ru.dseymo.arenastream.arena.cmd;

import org.bukkit.command.CommandSender;

import ru.dseymo.arenastream.arena.cmd.sub.CreateArenaSubCMD;
import ru.dseymo.arenastream.arena.cmd.sub.DeleteArenaSubCMD;
import ru.dseymo.libutils.mc.bukkit.cmd.ExtendCommand;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class ArenaCMD extends ExtendCommand {

	public ArenaCMD() {
		super("arena", false, "arenastream.arena", "&6Commands arena");
		
		addSubCMD(new CreateArenaSubCMD());
		addSubCMD(new DeleteArenaSubCMD());
	}
	
	public boolean execute(CommandSender sender, Args args) {
		help(sender);
		
		return false;
	}

}

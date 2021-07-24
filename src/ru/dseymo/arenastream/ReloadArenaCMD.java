package ru.dseymo.arenastream;

import org.bukkit.command.CommandSender;

import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.bukkit.cmd.Command;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class ReloadArenaCMD extends Command {

	public ReloadArenaCMD() {
		super("reloadarena", false, "arenastream.reload");
	}

	@Override
	public boolean execute(CommandSender sender, Args args) {
		ArenaStream.getInstance().reloadConfigs();
		
		Chat.SUCCESS.send(sender, "Success");
		
		return true;
	}

}

package ru.dseymo.arenastream.arena.cmd.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.CuboidRegion;

import ru.dseymo.arenastream.arena.ArenaManager;
import ru.dseymo.arenastream.arena.BlockNotFoundException;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.bukkit.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class CreateArenaSubCMD extends SubExtendCommand {

	public CreateArenaSubCMD() {
		super("create", true, "", "&6Create arena");
	}
	
	@SuppressWarnings("deprecation")
	public boolean execute(CommandSender sender, Args args) {
		Player p = (Player)sender;
		
		try {
			ArenaManager manager = ArenaManager.getManager();
			
			if(manager.isCreated()) {
				Chat.FAIL.send(sender, "Arena already created");
				return false;
			}
			
			LocalSession session = WorldEdit.getInstance().getSession(p.getName());
			
			if(session == null) {
				Chat.FAIL.send(sender, "Highlight the region");
				return false;
			}
			
			manager.createArena((CuboidRegion)session.getSelection(BukkitUtil.getLocalWorld(p.getWorld())));
			
			Chat.SUCCESS.send(sender, "Success");
		} catch (IncompleteRegionException e) {
			e.printStackTrace();
			
			Chat.FAIL.send(sender, e.getMessage());
		} catch (BlockNotFoundException e) {
			Chat.FAIL.send(sender, e.getMessage());
		}
		
		return true;
	}

}

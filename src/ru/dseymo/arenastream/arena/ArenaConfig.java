package ru.dseymo.arenastream.arena;

import java.io.File;
import java.text.ParseException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.CuboidRegion;

import lombok.Getter;
import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.libutils.mc.bukkit.Config;
import ru.dseymo.libutils.mc.bukkit.LocUtil;

public class ArenaConfig extends Config {
	@Getter
	private static ArenaConfig instance = new ArenaConfig(ArenaStream.getInstance().getDataFolder());
	
	
	public ArenaConfig(File file) {
		super(new File(file + "/arena.yml"), false);
	}
	
	@SuppressWarnings("deprecation")
	public Arena tryGetArena() throws ParseException, BlockNotFoundException {
		if(getFile().length() == 0)
			return null;
		
		Location pos1 = LocUtil.parseLoc(getString("pos1"));
		Location pos2 = LocUtil.parseLoc(getString("pos2"));
		
		return new Arena(new CuboidRegion(BukkitUtil.getLocalWorld(pos1.getWorld()), BukkitUtil.toVector(pos1), BukkitUtil.toVector(pos2)));
	}
	
	public void saveArena(Arena arena) {
		CuboidRegion region = arena.getRegion();
		World world = Bukkit.getWorld(region.getWorld().getName());
		
		setLocation("pos1", BukkitUtil.toLocation(world, region.getPos1()), false, false);
		setLocation("pos2", BukkitUtil.toLocation(world, region.getPos2()), false, false);
		
		save();
	}
	
	public void removeArena() {
		set("pos1", null);
		set("pos2", null);
		
		save();
	}
	
}

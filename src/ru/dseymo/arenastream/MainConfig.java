package ru.dseymo.arenastream;

import java.io.File;

import org.bukkit.Material;

import lombok.Getter;
import ru.dseymo.libutils.mc.bukkit.Config;

public class MainConfig extends Config {
	@Getter
	private static MainConfig instance = new MainConfig(ArenaStream.getInstance().getDataFolder());
	
	
	public MainConfig(File file) {
		super(new File(file + "/config.yml"), true);
	}
	
	public Material getMaterialForSeating() {
		try {
			return Material.getMaterial(getString("material_seating"));
		} catch (Exception e) {
			e.printStackTrace();
			return Material.MELON_BLOCK;
		}
	}
	
	public Material getMaterialForArena() {
		try {
			return Material.getMaterial(getString("material_arena"));
		} catch (Exception e) {
			e.printStackTrace();
			return Material.REDSTONE_BLOCK;
		}
	}
	
	public Material getMaterialForBanner() {
		try {
			return Material.getMaterial(getString("material_banner"));
		} catch (Exception e) {
			e.printStackTrace();
			return Material.EMERALD_BLOCK;
		}
	}
	
	public Material getMaterialForSpawn() {
		try {
			return Material.getMaterial(getString("material_spawn"));
		} catch (Exception e) {
			e.printStackTrace();
			return Material.EMERALD_BLOCK;
		}
	}
	
	public int getDistanceFromSpawnForTp() {
		return getInt("distance_from_spawn_for_tp");
	}
	
	public int getTimeCheckIsOnline() {
		return getInt("time_is_online");
	}
	
	public int getTimeStartIn() {
		return getInt("time_start_in");
	}
	
	public int getTimeStopIn() {
		return getInt("time_stop_in")*60;
	}
	
	public boolean isDebugEnable() {
		return getBoolean("debug_mode");
	}
	
}

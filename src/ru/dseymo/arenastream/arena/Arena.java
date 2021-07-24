package ru.dseymo.arenastream.arena;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.regions.CuboidRegion;

import lombok.Getter;
import ru.dseymo.arenastream.MainConfig;
import ru.dseymo.arenastream.player.HoloBoard;
import ru.dseymo.arenastream.viewer.Viewer;
import ru.dseymo.arenastream.viewer.boss.HoloBoss;

public class Arena {
	@Getter
	private CuboidRegion region;
	private ArrayList<Seating> seatings = new ArrayList<>();
	private HashMap<String, Viewer> viewers = new HashMap<>();
	@Getter
	private ArrayList<Block> arena = new ArrayList<>();
	@Getter
	private Location banner;
	@Getter
	private Location spawn;
	
	private ArrayList<UUID> players = new ArrayList<>();
	
	public Arena(CuboidRegion region) throws BlockNotFoundException {
		this.region = region;
		World world = Bukkit.getWorld(region.getWorld().getName());
		MainConfig config = MainConfig.getInstance();
		Material seatingMat = config.getMaterialForSeating(),
				 arenaMat = config.getMaterialForArena(),
				 bannerMat = config.getMaterialForBanner(),
				 spawnMat = config.getMaterialForSpawn();
		
		for(BlockVector vect: region) {
			Block b = world.getBlockAt(vect.getBlockX(), vect.getBlockY(), vect.getBlockZ());
			Material mat = b.getType();
			
			if(mat == seatingMat)
				seatings.add(new Seating(b.getLocation().add(0.5, 0, 0.5)));
			else if(mat == arenaMat)
				arena.add(b);
			else if(mat == bannerMat)
				banner = b.getRelative(BlockFace.UP).getLocation().add(0.5, 0, 0.5);
			else if(mat == spawnMat)
				spawn = b.getRelative(BlockFace.UP).getLocation().add(0.5, 0.1, 0.5);
		}
		
		if(seatings.isEmpty())
			throw new BlockNotFoundException(seatingMat, "Seatings");
		else if(arena.isEmpty())
			throw new BlockNotFoundException(arenaMat, "Arenas");
		else if(banner == null)
			throw new BlockNotFoundException(bannerMat, "Banner");
		else if(spawn == null)
			throw new BlockNotFoundException(spawnMat, "Spawn");
	}
	
	public void remove() {
		clear();
		
		seatings.clear();
		viewers.clear();
		arena.clear();
	}
	
	public void clear() {
		for(Seating seating: getViewers()) {
			seating.getViewer().remove();
			seating.clear();
		}
	}
	
	
	public void addPlayer(Player player) {
		if(!players.contains(player.getUniqueId()))
			players.add(player.getUniqueId());
		
		player.setGameMode(GameMode.ADVENTURE);
		
		ArenaManager.getManager().getBoard().addPlayer(player);
		
		HoloBoard.add(player);
		HoloBoss.add(player);
		
		if(player.getLocation().distance(spawn) > MainConfig.getInstance().getDistanceFromSpawnForTp())
			player.teleport(spawn);
	}
	
	public void removePlayer(Player player) {
		if(players.contains(player.getUniqueId()))
			players.remove(player.getUniqueId());
		
		ArenaManager.getManager().getBoard().removePlayer(player);
		
		HoloBoard.remove(player);
		HoloBoss.remove(player);
	}
	
	public ArrayList<UUID> getPlayers() {
		return new ArrayList<>(players);
	}
	
	
	public boolean containsViewer(Viewer viewer) {
		return viewers.containsKey(viewer.getId());
	}
	
	public boolean addViewer(Viewer viewer) {
		if(containsViewer(viewer))
			return false;
		
		ArrayList<Seating> seatings = getFreeSeating();
		
		if(seatings.isEmpty())
			return false;
		
		Collections.shuffle(seatings);
		
		seatings.get(0).setViewer(viewer);
		viewers.put(viewer.getId(), viewer);
		
		return true;
	}
	
	public void removeViewer(Viewer viewer) {
		if(containsViewer(viewer))
			for(Seating seating: getViewers())
				if(seating.getViewer().getId().equals(viewer.getId())) {
					seating.getViewer().remove();
					
					viewers.remove(viewer.getId());
					
					seating.clear();
				}
	}
	
	public Viewer getViewer(String id) {
		return viewers.get(id);
	}
	
	public void clearOfflineViewers() {
		for(Seating seating: getViewers())
			if(!seating.getViewer().isOnline())
				removeViewer(seating.getViewer());
	}
	
	public ArrayList<Seating> getFreeSeating() {
		ArrayList<Seating> seatings = new ArrayList<>();
		
		for(Seating seating: this.seatings)
			if(seating.isFree())
				seatings.add(seating);
		
		return seatings;
	}
	
	public int getStandsSize() {
		return seatings.size();
	}
	
	public ArrayList<Seating> getViewers() {
		ArrayList<Seating> viewers = new ArrayList<>();
		
		for(Seating seating: seatings)
			if(!seating.isFree())
				viewers.add(seating);
		
		return viewers;
	}
	
}

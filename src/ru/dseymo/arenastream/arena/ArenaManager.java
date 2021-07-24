package ru.dseymo.arenastream.arena;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.regions.CuboidRegion;

import lombok.Getter;
import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.arenastream.player.PlayerManager;
import ru.dseymo.arenastream.viewer.action.BuiltInAction;
import ru.dseymo.arenastream.viewer.boss.BossManager;
import ru.dseymo.arenastream.viewer.code.Code;
import ru.dseymo.libutils.Manager;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.bukkit.ColorUtil;

public class ArenaManager implements Manager {
	public static ArenaManager getManager() {
		return (ArenaManager)ArenaStream.getManager(ArenaManager.class);
	}
	
	
	@Getter
	private Arena arena;
	@Getter
	private ArenaBoard board;
	ArenaRunnable run;
	private boolean enabled = false;
	
	@Override
	public void setup() {
		board = new ArenaBoard();
		run = new ArenaRunnable();
		
		try {
			arena = ArenaConfig.getInstance().tryGetArena();
		} catch (ParseException | BlockNotFoundException e) {
			e.printStackTrace();
			
			Bukkit.getConsoleSender().sendMessage(ColorUtil.color("[ArenaStream] &4&lArena not loaded!"));
		}
	}

	@Override
	public void unsetup() {
		run.cancel();
		
		disable(true);
		
		if(isCreated())
			arena.remove();
		
		arena = null;
	}
	
	
	public boolean isCreated() {
		return arena != null;
	}
	
	public boolean isEnabled() {
		return isCreated() && enabled;
	}
	
	public Location getRandomLocOnArena() {
		if(ArenaManager.getManager().getArena() == null)
			return null;
		
		ArrayList<Block> arena = new ArrayList<>(ArenaManager.getManager().getArena().getArena());
		
		Collections.shuffle(arena);
		
		return arena.get(0).getRelative(BlockFace.UP).getLocation().add(0.5, 0, 0.5);
	}
	
	public Arena createArena(CuboidRegion region) throws BlockNotFoundException {
		if(isCreated())
			return null;
		
		arena = new Arena(region);
		
		ArenaConfig.getInstance().saveArena(arena);
		
		return arena;
	}
	
	public void removeArena() {
		disable(true);
		
		ArenaConfig.getInstance().removeArena();
		
		arena = null;
	}
	
	public void addTime(int secs) {
		if(isEnabled())
			run.timeForStop += secs;
	}
	
	public void viewersWon() {
		Chat.INFO.sendAll("Viewers WON!");
		
		disable(false);
	}
	
	public void playersWon() {
		Chat.INFO.sendAll("Players WON!");
		
		disable(false);
	}
	
	public void enable() {
		if(isCreated() && !enabled) {
			enabled = true;
			
			Chat.INFO.sendAll("Game started!");
			
			World world = arena.getSpawn().getWorld();
			
			world.setDifficulty(Difficulty.EASY);
			world.setTime(9000);
			world.setWeatherDuration(0);
			world.setStorm(false);
			world.setThundering(false);
			
			for(Player p: PlayerManager.getManager().getPlayersCanPlay())
				PlayerManager.getManager().resetPlayer(p);
		}
	}
	
	public void disable(boolean forced) {
		if(!isCreated())
			return;
		
		if(enabled) {
			arena.clear();
			
			if(!forced) {
				for(Player p: Bukkit.getOnlinePlayers())
					p.setGameMode(GameMode.SPECTATOR);
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						for(Player p: Bukkit.getOnlinePlayers())
							p.kickPlayer("Game ended");
					}
					
				}.runTaskLater(ArenaStream.getInstance(), 100);
			}
			
			enabled = false;
		}
		
		ArenaStream.getYoutube().disconnect();
		
		BossManager.getManager().removeBoss();
		
		BuiltInAction.cancelAll();
		
		Code.clearAll();
		
		for(Entity ent: arena.getSpawn().getWorld().getEntities())
			if(!Arrays.asList(EntityType.PLAYER).contains(ent.getType()))
				ent.remove();
	}
	
}

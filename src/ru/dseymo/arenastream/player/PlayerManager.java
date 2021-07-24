package ru.dseymo.arenastream.player;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.arenastream.arena.ArenaManager;
import ru.dseymo.libutils.Manager;
import ru.dseymo.libutils.mc.bukkit.Chat;

public class PlayerManager implements Manager, Listener {
	public static PlayerManager getManager() {
		return (PlayerManager)ArenaStream.getManager(PlayerManager.class);
	}
	
	
	private PlayerRunnable run;
	
	@Override
	public void setup() {
		run = new PlayerRunnable();
		
		Bukkit.getPluginManager().registerEvents(this, ArenaStream.getInstance());
	}

	@Override
	public void unsetup() {
		run.cancel();
		
		PlayerJoinEvent.getHandlerList().unregister(this);
		PlayerQuitEvent.getHandlerList().unregister(this);
		PlayerDeathEvent.getHandlerList().unregister(this);
		FoodLevelChangeEvent.getHandlerList().unregister(this);
		EntityDeathEvent.getHandlerList().unregister(this);
	}
	
	
	public Player getRandomPlayer() {
		ArrayList<Player> players = getPlayersCanPlay();
		
		Collections.shuffle(players);
		
		return players.get(0);
	}
	
	public boolean isPlayerCanPlay(Player player) {
		return player.getGameMode() == GameMode.ADVENTURE;
	}
	
	public ArrayList<Player> getPlayersCanPlay() {
		ArrayList<Player> players = new ArrayList<>();
		
		for(Player p: Bukkit.getOnlinePlayers())
			if(isPlayerCanPlay(p))
				players.add(p);
		
		return players;
	}
	
	public void resetPlayer(Player p) {
		p.setFoodLevel(20);
		p.setSaturation(20);
		p.setMaxHealth(40);
		p.setHealth(40);
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setExp(0);
		p.setLevel(0);
		
		for(PotionEffect effect: p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		resetPlayer(p);
		
		e.setJoinMessage("");
		
		if(ArenaManager.getManager().isCreated() && !ArenaManager.getManager().isEnabled())
			ArenaManager.getManager().getArena().addPlayer(p);
		else if(ArenaManager.getManager().isEnabled()) {
			if(p.hasPermission("arenastream.joinduringgame"))
				p.setGameMode(GameMode.SPECTATOR);
			else
				p.kickPlayer("Game already started!");
		}
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		e.setQuitMessage("");
		
		if(ArenaManager.getManager().isCreated())
			ArenaManager.getManager().getArena().removePlayer(e.getPlayer());
	}
	
	@EventHandler
	public void died(PlayerDeathEvent e) {
		Player p = e.getEntity();
		
		e.getDrops().clear();
		e.setDroppedExp(0);
		e.setDeathMessage("");
		
		if(ArenaManager.getManager().isEnabled()) {
			ArenaManager.getManager().getArena().removePlayer(p);
			
			Chat.INFO.sendAll(p.getName() + " &4&lDIED&7!");
			
			p.setGameMode(GameMode.SPECTATOR);
		}
	}
	
	@EventHandler
	public void eat(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void entityDied(EntityDeathEvent e) {
		e.setDroppedExp(0);
		e.getDrops().clear();
	}
	
}

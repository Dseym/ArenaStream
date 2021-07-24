package ru.dseymo.arenastream.viewer.boss;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lombok.Getter;
import ru.dseymo.arenastream.player.PlayerManager;

public class Boss {
	@Getter
	private Skeleton entity;
	Skill skill;
	
	Boss(Location loc) {
		entity = (Skeleton)loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
		EntityEquipment equip = entity.getEquipment();
		
		equip.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
		equip.setHelmet(new ItemStack(Material.DIAMOND_CHESTPLATE));
		equip.setHelmet(new ItemStack(Material.DIAMOND_LEGGINGS));
		equip.setHelmet(new ItemStack(Material.DIAMOND_BOOTS));
		
		entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*60*60, 0));
		entity.setMaxHealth(200);
		entity.setHealth(200);
		entity.setTarget(getRandomPlayer());
		entity.setRemoveWhenFarAway(false);
	}
	
	public void remove() {
		BossManager.getManager().bossDespawned();
		
		entity.remove();
		
		skill.cancel();
	}
	
	
	private Player getRandomPlayer() {
		ArrayList<Player> players = PlayerManager.getManager().getPlayersCanPlay();
		
		Collections.shuffle(players);
		
		return players.get(0);
	}
	
}

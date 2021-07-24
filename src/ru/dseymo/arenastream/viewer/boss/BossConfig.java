package ru.dseymo.arenastream.viewer.boss;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lombok.Getter;
import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.libutils.mc.bukkit.Config;

public class BossConfig extends Config {
	@Getter
	private static BossConfig instance = new BossConfig(ArenaStream.getInstance().getDataFolder());
	
	
	public BossConfig(File file) {
		super(new File(file + "/boss.yml"), true);
	}
	
	public Boss spawnBoss(Location loc) {
		Boss boss = new Boss(loc);
		Skill skill = new Skill(boss);
		
		skill.runTaskTimer(ArenaStream.getInstance(), getInt("skill.period")*20, getInt("skill.period")*20);
		
		boss.skill = skill;
		
		try {
			for(String str: getStringList("skill.abilities")) {
				String[] split = str.split(":");
				
				skill.abilities.put(Ability.valueOf(split[0]), Integer.parseInt(split[1]));
			}
			
			for(String str: getStringList("skill.effects")) {
				String[] split = str.split(":");
				
				skill.effects.add(new PotionEffect(PotionEffectType.getByName(split[0]), Integer.parseInt(split[1])*20, Integer.parseInt(split[2])-1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return boss;
	}
	
	public int getCostBoss() {
		return getInt("cost_boss");
	}
	
}

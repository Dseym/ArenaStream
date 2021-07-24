package ru.dseymo.arenastream.viewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.EulerAngle;

import lombok.Getter;
import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.arenastream.MainConfig;
import ru.dseymo.arenastream.arena.ArenaManager;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.bukkit.ColorUtil;
import ru.dseymo.libutils.mc.bukkit.ItemUtil;
import ru.dseymo.libutils.mc.bukkit.LocUtil;
import ru.dseymo.youtubestream.Author;
import ru.dseymo.youtubestream.Author.AuthorRole;
import ru.dseymo.youtubestream.Command;
import ru.dseymo.youtubestream.Message;
import ru.dseymo.youtubestream.SuperMessage;

public class Viewer {
	private static final int MAX_LEVEL = 3;
	
	
	private Author account;
	@Getter
	private LivingEntity entity;
	@Getter
	private int money = 0;
	private int level = 0;
	private long lastAction;
	int streakOnline = 0;
	
	public Viewer(Author author) {
		this.account = author;
		ArmorStand stand = (ArmorStand)LocUtil.ZERO.getWorld().spawnEntity(LocUtil.ZERO, EntityType.ARMOR_STAND);
		EntityEquipment equip = stand.getEquipment();
		
		equip.setArmorContents(getArmor());
		equip.setItemInHand(getHoldItem());
		
		stand.setCustomNameVisible(true);
		stand.setArms(true);
		stand.setHeadPose(new EulerAngle(0, 0, 0));
		stand.setBodyPose(new EulerAngle(0, 0, 0));
		stand.setRightArmPose(new EulerAngle(0, 0, 0));
		stand.setLeftArmPose(new EulerAngle(0, 0, 0));
		stand.setRightLegPose(new EulerAngle(-1.4, 0.3, -0.2));
		stand.setLeftLegPose(new EulerAngle(-1.4, -0.3, 0.2));
		stand.setMetadata("viewer", new FixedMetadataValue(ArenaStream.getInstance(), account));
		
		entity = stand;
		
		updateLastAction();
	}
	
	public void remove() {
		entity.remove();
	}
	
	public void update() {
		entity.setCustomName(getEntityName());
		
		EntityEquipment equip = entity.getEquipment();
		
		equip.setArmorContents(getArmor());
		equip.setItemInHand(getHoldItem());
		
		Location loc = entity.getLocation();
		ArrayList<UUID> uuids = ArenaManager.getManager().getArena().getPlayers();
		
		Collections.shuffle(uuids);
		
		Player p = Bukkit.getPlayer(uuids.get(0));
		
		loc.setDirection(p.getLocation().toVector().subtract(loc.toVector()));
		
		entity.teleport(loc);
	}
	
	
	public LivingEntity getArenaEntity() {
		Zombie entity = (Zombie)LocUtil.ZERO.getWorld().spawnEntity(LocUtil.ZERO, EntityType.ZOMBIE);
		EntityEquipment equip = entity.getEquipment();
		
		entity.setCustomNameVisible(true);
		entity.setCustomName(getEntityName());
		entity.setBaby(false);
		entity.setVillager(false);
		
		equip.setArmorContents(getArmor());
		equip.setItemInHand(getHoldItem());
		
		return entity;
	}
	
	public String getEntityName() {
		return ColorUtil.color(getPrefix() + getName());
	}
	
	public ItemStack[] getArmor() {
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		
		switch(level) {
			case 1:
				helmet = new ItemStack(Material.CHAINMAIL_HELMET);
				
				helmet.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 0);
				helmet.addUnsafeEnchantment(Enchantment.THORNS, 0);
				
				break;
				
			case 2:
				helmet = new ItemStack(Material.IRON_HELMET);
				
				helmet.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 1);
				helmet.addUnsafeEnchantment(Enchantment.THORNS, 1);
				
				break;
				
			case 3:
				helmet = new ItemStack(Material.DIAMOND_HELMET);
				
				helmet.addUnsafeEnchantment(Enchantment.THORNS, 2);
				helmet.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 2);
				
				break;
		}
		
		return new ItemStack[] {new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS),
								new ItemStack(Material.LEATHER_CHESTPLATE), helmet};
	}
	
	public ItemStack getHoldItem() {
		ItemStack item = null;
		
		switch(level) {
			case 1:
				item = ItemUtil.generateItem(Material.STICK, "Палка - пиналка");
				
				item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 0);
				item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 0);
				
				break;
				
			case 2:
				item = ItemUtil.generateItem(Material.WOOD_SWORD, "Меч - хумеч");
				
				item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
				item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
				
				break;
				
			case 3:
				item = ItemUtil.generateItem(Material.WOOD_SWORD, "Меч - охумеч");
				
				item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
				item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
				
				break;
		}
		
		return item;
	}
	
	public boolean upgrade() {
		int lastLvl = level;
		
		if(level < MAX_LEVEL)
			level++;
		
		updateLastAction();
		
		return lastLvl != level;
	}
	
	
	public String getId() {
		return account.getChannelId();
	}
	
	public String getName() {
		return account.getName();
	}
	
	public String getPrefix() {
		String prefix = "";
		
		switch(account.getRole()) {
			case MODERATOR:
				prefix = "&8[&9Moder&8] ";
				break;
				
			case OWNER:
				prefix = "&8[&4&lOwner&8] ";
				break;
				
			default: break;
		}
		
		return prefix + (account.isSponsor() ? "&2" : "&7");
	}
	
	public String getSuffix() {
		return (account.isVerified() ? " &7&l\u2713" : "") + "&8: &3";
	}
	
	public void message(Message message) {
		Chat.NO_PREFIX.sendAll(getPrefix() + getName() + "&r" + getSuffix() + (account.getRole() == AuthorRole.OWNER ? message.getText() : ColorUtil.clear(ColorUtil.color(message.getText()))));
		
		updateLastAction();
	}
	
	public void superMessage(SuperMessage message) {
		Chat.NO_PREFIX.sendAll("&2&lSuper Chat Message - " + message.getAmountString() + "&8:",
							   "  " + getPrefix() + getName() + "&r" + getSuffix() + message);
		
		updateLastAction();
	}
	
	public void command(AbstractCommand command, Command cmd) {
		updateLastAction();
		
		command.execute(this, cmd);
	}
	
	public void updateLastAction() {
		update();
		
		lastAction = new Date().getTime();
	}
	
	public boolean isOnline() {
		return new Date().getTime() < lastAction + (MainConfig.getInstance().getTimeCheckIsOnline()*60000);
	}
	
	
	public boolean has(int money) {
		return this.money >= money;
	}
	
	public void deposit(int money) {
		this.money += money;
	}
	
	public void withdraw(int money) {
		if(has(money))
			this.money -= money;
	}
	
}

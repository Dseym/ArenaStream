package ru.dseymo.arenastream.arena;

import org.bukkit.Material;

public class BlockNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public BlockNotFoundException(Material material, String block) {
		super(block + " not found (" + material + ")");
	}
	
}

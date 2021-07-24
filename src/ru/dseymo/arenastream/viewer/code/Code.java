package ru.dseymo.arenastream.viewer.code;

import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.dseymo.arenastream.viewer.Viewer;
import ru.dseymo.arenastream.viewer.ViewerManager;
import ru.dseymo.libutils.mc.bukkit.hologram.Hologram;

@AllArgsConstructor
@Getter
public class Code {
	private static final HashMap<String, Code> CODES = new HashMap<>();
	
	public static boolean contains(String code) {
		return CODES.containsKey(code);
	}
	
	public static void clearAll() {
		for(Code code: CODES.values())
			code.holo.remove();
		
		CODES.clear();
	}
	
	public static void addCode(Code code) {
		CODES.put(code.code, code);
	}
	
	public static Code giveCode(Viewer viewer, String _code) {
		if(CODES.containsKey(_code)) {
			Code code = CODES.get(_code);
			
			ViewerManager.getManager().depositMoney(viewer, code.cost);
			
			CODES.remove(_code);
			
			code.holo.remove();
			
			return code;
		} else
			return null;
	}
	
	
	private String code;
	private int cost;
	private Hologram holo;
}

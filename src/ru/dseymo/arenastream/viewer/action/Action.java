package ru.dseymo.arenastream.viewer.action;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Action {
	private String id, message;
	private int cost;
	private boolean canTimesExecute;
	private List<String> cmds;
	
	@Override
	public int hashCode() {
		return id.hashCode()*43 + 1;
	}
	
}

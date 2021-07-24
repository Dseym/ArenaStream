package ru.dseymo.arenastream.youtube;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import ru.dseymo.arenastream.ArenaStream;
import ru.dseymo.arenastream.arena.Arena;
import ru.dseymo.arenastream.arena.ArenaManager;
import ru.dseymo.arenastream.viewer.Viewer;
import ru.dseymo.arenastream.viewer.ViewerManager;
import ru.dseymo.arenastream.viewer.action.ActionManager;
import ru.dseymo.arenastream.viewer.command.ActionCommand;
import ru.dseymo.arenastream.viewer.command.BannerCommand;
import ru.dseymo.arenastream.viewer.command.BossCommand;
import ru.dseymo.arenastream.viewer.command.ChallengeCommand;
import ru.dseymo.arenastream.viewer.command.CodeCommand;
import ru.dseymo.arenastream.viewer.command.MoneyBalanceCommand;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.youtubestream.Author;
import ru.dseymo.youtubestream.Command;
import ru.dseymo.youtubestream.IMessagesListener;
import ru.dseymo.youtubestream.Message;
import ru.dseymo.youtubestream.Result;
import ru.dseymo.youtubestream.SuperMessage;

public class YoutubeMessageListener implements IMessagesListener {
	private ArrayList<String> alreadyMessaging = new ArrayList<>();
	private boolean alreadyBeDisconnect = false;
	private String videoId;
	
	private Viewer getViewer(Author author) {
		Arena arena = ArenaManager.getManager().getArena();
		Viewer viewer = arena.getViewer(author.getChannelId());
		
		if(viewer == null)
			if(!arena.addViewer(viewer = new Viewer(author)))
				viewer = null;
		
		return viewer;
	}
	
	@Override
	public void onConnect() {
		videoId = ArenaStream.getYoutube().getStream().getVideoId();
		
		alreadyBeDisconnect = false;
	}
	
	@Override
	public void onDisconnect() {
		if(!alreadyBeDisconnect) {
			ArenaStream.getYoutube().connect(videoId, YoutubeAuthConfig.getInstance().getGoogleAPI());
			
			alreadyBeDisconnect = true;
		}
	}

	@Override
	public void onError(Result result, Exception exception) {
		ArenaStream.getInstance().getLogger().info(result.getMessage());
	}
	
	@Override
	public void onUpdate() {
		ActionManager.getManager().onUpdate();
		
		alreadyMessaging.clear();
	}
	
	@Override
	public void onCommand(Command cmd) {
		if(!ArenaManager.getManager().isEnabled())
			return;
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Author author = cmd.getAuthor();
				Viewer viewer = getViewer(author);
				
				if(viewer == null)
					return;
				
				String command = cmd.getCommand();
				
				if(command.equalsIgnoreCase("money"))
					viewer.command(new MoneyBalanceCommand(), cmd);
				else if(command.equalsIgnoreCase("action"))
					viewer.command(new ActionCommand(), cmd);
				else if(command.equalsIgnoreCase("challenge"))
					viewer.command(new ChallengeCommand(), cmd);
				else if(command.equalsIgnoreCase("code"))
					viewer.command(new CodeCommand(), cmd);
				else if(command.equalsIgnoreCase("boss"))
					viewer.command(new BossCommand(), cmd);
				else if(command.equalsIgnoreCase("banner"))
					viewer.command(new BannerCommand(), cmd);
			}
			
		}.runTask(ArenaStream.getInstance());
	}

	@Override
	public void onMessage(Message mess) {
		if(!ArenaManager.getManager().isEnabled())
			return;
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Viewer viewer = getViewer(mess.getAuthor());
				
				if(viewer == null)
					return;
				
				viewer.message(mess);
				
				if(!alreadyMessaging.contains(viewer.getId())) {
					ViewerManager.getManager().depositMoney(viewer, 5);
					
					alreadyMessaging.add(viewer.getId());
				}
			}
			
		}.runTask(ArenaStream.getInstance());
	}

	@Override
	public void onNewSponsor(Author author) {
		if(!ArenaManager.getManager().isEnabled())
			return;
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Viewer viewer = getViewer(author);
				
				if(viewer == null)
					return;
				
				Chat.INFO.sendAll("New Sponsor! Welcome &2&o&l" + viewer.getName() + "&7!");
			}
			
		}.runTask(ArenaStream.getInstance());
	}

	@Override
	public void onSuperChatMessage(SuperMessage mess) {
		if(!ArenaManager.getManager().isEnabled())
			return;
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Viewer viewer = getViewer(mess.getAuthor());
				
				if(viewer == null)
					return;
				
				viewer.superMessage(mess);
			}
			
		}.runTask(ArenaStream.getInstance());
	}

}

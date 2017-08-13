package jp.motlof.dgp_auth;

import java.util.HashMap;
import java.util.Map;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class EventListener {
	
	private Map<Long, Integer> count = new HashMap<>();
	
	@EventSubscriber
	public void onJoin(UserJoinEvent e) {
		IGuild guild = e.getGuild();
		try {
			if(e.getUser().isBot()) {
				guild.getChannelByID(190496450759753729l).sendMessage("Botが追加されました Name: "+e.getUser().getName());
				return;
			}
			e.getUser().getOrCreatePMChannel().sendMessage(getSeparatorMessage(
					"Discord Game Playersへご参加いただき有難うございます。当サーバー（DGPサーバー）では元々マインクラフトをしていた人を中心としてサーバーを立て、現在は色々なジャンルの話や、雑談をすることを想定して運営しています。",
					"それらチャンネルに入る前に、参加者全員に自己紹介を書いてもらうようお願いしています。",
					"",
					"Discord Game Players → #general で趣味などを含めた自己紹介文を書いてください。",
					"確認次第、認証を行います。",
					"",
					"Have fun!                                 DGP - Admin: kazu0617",
					"",
					"",
					"===========================================",
					"このメッセージは自動的に作って、配信してるよ",
					"返信をしても、お返事出来ないから注意してね",
					"==========================================="));
		} catch (MissingPermissionsException | RateLimitException
				| DiscordException e1) {
			e1.printStackTrace();
		}
	}
	

	@EventSubscriber
	public void onMessageReceivedEvent(MessageReceivedEvent e) {
		String nickname = e.getGuild().getUserByID(345114143445221386L).getNicknameForGuild(e.getGuild());
		if(nickname == null)
			nickname = "DGP_Auth";
		if(e.getMessage().getContent().matches(".*"+nickname+"(ちゃん|ちゃーん|さん|さーん|くん|くーん).*") || isMention(e.getMessage())) {
			if(e.getMessage().getContent().contains("判定お願い")) {
				count.put(e.getGuild().getLongID(), 0);
				e.getChannel().setTypingStatus(true);
				String users = "文字列が規定を越していないのは \r\n ```";
				int count = 0;
				for(IUser user : e.getGuild().getUsers()) {
					if(user.isBot())
						continue;
					if(user.getLongID() == 190468887593222144L)
						continue;
					Checker checker = new Checker(e.getGuild(), user);
					if(!checker.isAuthOther()) {
						users += user.getName() + " ";
						count++;
					}
				}
				users += "``` \r\n以上 "+count+" 名になります。";
				e.getChannel().sendMessage(users);
				e.getChannel().setTypingStatus(false);
				return;
			}
			int i = 0;
			if(count.containsKey(e.getGuild().getLongID()))
				i = count.get(e.getGuild().getLongID());
			if(i < 3)
				e.getChannel().sendMessage("はいはーい");
			else if(i >= 3 && i < 5)
				e.getChannel().sendMessage(e.getGuild().getEmojiByName("Soo2").toString()+" 何回呼ぶの？");
			else if(i >= 5)
				e.getChannel().sendMessage(e.getGuild().getEmojiByName("Soo3").toString()+" 用もないのに何回も呼ばないで");
			i++;
			count.put(e.getGuild().getLongID(), i);
		}
	}
	/*
	 * 
	 * else if(e.getMessage().getContent().contains("お疲れ様")) {
				if(e.getMessage().getAuthor().getLongID() == 190468887593222144L) {
					e.getMessage().getChannel().sendMessage("お疲れ～、またね～ \r\n(判定プログラムを終了しました)");
					Main.discordclient.logout();
					System.exit(0);
					return;
				}
				e.getMessage().getChannel().sendMessage("お疲れ～");
				return;
			}
	 * 
	 */
	
	private String getSeparatorMessage(String... msgs) {
		String separator = "\r\n";
		String text = "";
		for(String msg : msgs)
			text = text+msg+separator;
		return text;
	}
	
	private boolean isMention(IMessage message) {
		if(message.getMentions() == null || message.getMentions().size() == 0)
			return false;
		for(IUser user : message.getMentions())
			if(user.getLongID() == Long.parseLong(message.getClient().getApplicationClientID()))
				return true;
		return false;
	}
}

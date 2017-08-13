package jp.motlof.dgp_auth;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class Checker {
	
	IUser user;
	IGuild guild;
	int count = 0;
	
	public Checker(IGuild guild, IUser user) {
		this.guild = guild;
		this.user = user;
	}
	
	public boolean isAuthOther() {
		for(IMessage message : guild.getChannelByID(190495358843879428l).getFullMessageHistory()) {
			if(message.getAuthor().getLongID() != user.getLongID())
				continue;
			String msg = message.getContent();
			msg = msg.replaceAll("\r\n", "")
					.replaceAll("\r", "")
					.replaceAll("\n", "")
					.replaceAll(" ", "")
					.replaceAll("　", "");
			count += msg.length();
		}
		System.out.println(user.getName()+ " : "+count);
		return count >= 30;
	}
}

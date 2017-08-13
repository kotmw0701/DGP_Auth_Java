package jp.motlof.dgp_auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class Main {
	
	static IDiscordClient discordclient;
	static int roop = 1;
	
	public static void main(String... args) {
		discordclient = getClient("MzQ1MTE0MTQzNDQ1MjIxMzg2.DG4TSA.Apf-NP5-YZLQTKBiJ_O2DDGypKg", true);
		EventDispatcher dispatcher = discordclient.getDispatcher();
		dispatcher.registerListener(new EventListener());
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			try {
					String text = reader.readLine();
					if(text.isEmpty())
						continue;
					if(text.startsWith("%stop")) {
						System.exit(0);
						discordclient.logout();
					} else if(text.startsWith("%remove")) {
						if(text.split(" ").length == 3) {
							long channnelid = Long.parseLong(text.split(" ")[1]);
							long userid = Long.parseLong(text.split(" ")[2]);
							for(IMessage imessage : discordclient.getChannelByID(channnelid).getMessageHistory()) {
								if(imessage.getAuthor().getLongID() == userid)
									imessage.delete();
							}
							continue;
						} else if(text.split(" ").length == 2) {
							long msgid = Long.parseLong(text.split(" ")[1]);
							discordclient.getMessageByID(msgid).delete();
						}
					}
			} catch (DiscordException | IOException | MissingPermissionsException | RateLimitException e) {
				e.printStackTrace();
				continue;
			} 
		}
	}
	
	private static IDiscordClient getClient(String token, boolean login) { // Returns an instance of the Discord client
		ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
		clientBuilder.withToken(token); // Adds the login info to the builder
		try {
			if (login) {
				return clientBuilder.login();
				// Creates the client instance and logs the client in
			} else {
				return clientBuilder.build(); // Creates the client instance but it doesn't log the client in yet, you would have to call client.login() yourself
			}
		} catch (DiscordException e) {
			e.printStackTrace();
		}
		return null;
	}
}

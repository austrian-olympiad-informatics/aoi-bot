package at.informatikolympiade;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.HashMap;

public class Main {

    //Invite: https://discord.com/api/oauth2/authorize?client_id=1070115188721844264&permissions=8&scope=bot%20applications.commands

    public static JDA JDA_INSTANCE;

    public static void main(String[] args) {
        JDA_INSTANCE = createJDA(Config.INSTANCE.getString("BotToken"));

        try {
            JDA_INSTANCE.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Creator.getInstance().createChannels();
        Creator.getInstance().createRoles();
        Creator.getInstance().setPermissions();

        Fetcher fetcher = new Fetcher();
        fetcher.start();

        while(!BotState.getInstance().appShouldClose()) {
            fetcher.waitOnMe();

            BotState.getInstance().lockMap();
            HashMap<String, Byte> roles = BotState.getInstance().getChangedRoles();
            BotState.getInstance().unlockMap();

            for(String r : roles.keySet()) {
                Creator.getInstance().setRole(r, roles.get(r));
            }
        }
    }

    private static JDA createJDA(String token) {
        JDABuilder builder = JDABuilder.createDefault(token);

        builder.setActivity(Activity.playing("with segtrees"));
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);

        return builder.build();
    }
}
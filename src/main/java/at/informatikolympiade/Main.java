package at.informatikolympiade;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.HashMap;

public class Main {

    //Invite: https://discord.com/api/oauth2/authorize?client_id=1070115188721844264&permissions=8&scope=bot%20applications.commands

    public static JDA JDA_INSTANCE;
    public static State STATE_INSTANCE;
    public static Creator CREATOR_INSTANCE;
    public static Config CONFIG;

    public static void main(String[] args) {
        CONFIG = new Config();
        JDA_INSTANCE = createJDA(CONFIG.getString("BotToken"));

        try {
            JDA_INSTANCE.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        STATE_INSTANCE = new State();
        CREATOR_INSTANCE = new Creator();
        CREATOR_INSTANCE.createChannels();
        CREATOR_INSTANCE.createRoles();
        CREATOR_INSTANCE.setPermissions();

        Fetcher fetcher = new Fetcher();
        fetcher.start();

        while(!Main.STATE_INSTANCE.appShouldClose()) {
            fetcher.waitOnMe();

            Main.STATE_INSTANCE.lockMap();
            HashMap<String, Byte> roles = Main.STATE_INSTANCE.getChangedRoles();
            Main.STATE_INSTANCE.unlockMap();

            for(String r : roles.keySet()) {
                Main.CREATOR_INSTANCE.setRole(r, roles.get(r));
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
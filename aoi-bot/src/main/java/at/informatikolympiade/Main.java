package at.informatikolympiade;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {

    //Invite: https://discord.com/api/oauth2/authorize?client_id=1070115188721844264&permissions=8&scope=bot%20applications.commands

    public static JDA JDA_INSTANCE;
    public static String[] ARGS;
    public static State STATE_INSTANCE;
    public static Creator CREATOR_INSTANCE;

    public static void main(String[] args) {
        ARGS = args;
        JDA_INSTANCE = createJDA(args[0]);
    }

    private static JDA createJDA(String token) {
        JDABuilder builder = JDABuilder.createDefault(token);

        builder.setActivity(Activity.playing("with segtrees"));
        builder.addEventListeners(new Logic());
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);

        return builder.build();
    }
}
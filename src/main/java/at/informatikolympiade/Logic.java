package at.informatikolympiade;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.HashMap;

import static java.lang.Thread.sleep;

public class Logic implements EventListener {

    @Override
    public void onEvent(GenericEvent event) {
        if (event instanceof ReadyEvent) {
            Main.STATE_INSTANCE = new State(Main.ARGS[2], Main.ARGS[3], Main.ARGS[4]);
            Main.CREATOR_INSTANCE = new Creator(Main.JDA_INSTANCE.getGuildById(Main.ARGS[1]));
            Main.CREATOR_INSTANCE.createChannels();
            Main.CREATOR_INSTANCE.createRoles();
            Main.CREATOR_INSTANCE.setPermissions();

            Fetcher fetcher = new Fetcher();
            fetcher.start();

            while(true) {
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Main.STATE_INSTANCE.getMapLock().writeLock().lock();
                HashMap<String, Byte> roles = Main.STATE_INSTANCE.getChangedRoles();
                Main.STATE_INSTANCE.getMapLock().writeLock().unlock();

                Main.STATE_INSTANCE.getMapLock().readLock().lock();
                for(String r : roles.keySet()) {
                    Main.CREATOR_INSTANCE.setRole(r, roles.get(r));
                }
                Main.STATE_INSTANCE.getMapLock().readLock().unlock();
            }
        }
    }
}

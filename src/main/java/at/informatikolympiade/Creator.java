package at.informatikolympiade;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.util.ArrayList;
import java.util.List;

public class Creator {

    private Guild guild;

    private TextChannel thisYearWienChannel;
    private TextChannel thisYearWoerglChannel;
    private TextChannel thisYearIOIChannel;
    private TextChannel thisYearCEOIChannel;
    private TextChannel thisYearEGOIChannel;

    Role thisYearBaseRole;
    Role thisYearWoerglRole;
    Role thisYearIOIRole;
    Role thisYearCEOIRole;
    Role thisYearEGOIRole;

    public Creator(Guild guild) {
        this.guild = guild;
    }

    private TextChannel createChannelIfNotExists(String name, String categoryName) {
        if(!categoryName.equals("")) {
            Category category;

            if (guild.getCategoriesByName(categoryName, true).isEmpty()) {
                category = guild.createCategory(categoryName).complete();
            } else {
                category = guild.getCategoriesByName(categoryName, true).get(0);
            }

            if(category.getTextChannels().stream().noneMatch(a -> a.getName().equals(name))) {
                return category.createTextChannel(name).complete();
            }
            return category.getTextChannels().stream().filter(a -> a.getName().equals(name)).findFirst().get();
        } else if(guild.getTextChannelsByName(name, true).stream().noneMatch(a -> a.getParentCategory() == null)) {
            return guild.createTextChannel(name).complete();
        } else {
            return guild.getTextChannelsByName(name, true).stream().filter(a -> a.getParentCategory() == null).findFirst().get();
        }
    }

    private Role createRoleIfNotExists(String name) {
        if(guild.getRolesByName(name, true).isEmpty()) {
            return guild.createRole().setName(name).complete();
        }
        return  guild.getRolesByName(name, true).get(0);
    }

    public void createChannels() {
        this.thisYearWienChannel = createChannelIfNotExists("camp-wien", Main.STATE_INSTANCE.getThisYearCategory());
        this.thisYearWoerglChannel = createChannelIfNotExists("camp-woergl", Main.STATE_INSTANCE.getThisYearCategory());
        this.thisYearIOIChannel = createChannelIfNotExists("ioi", Main.STATE_INSTANCE.getThisYearCategory());
        this.thisYearCEOIChannel = createChannelIfNotExists("ceoi", Main.STATE_INSTANCE.getThisYearCategory());
        this.thisYearEGOIChannel = createChannelIfNotExists("egoi", Main.STATE_INSTANCE.getThisYearCategory());
    }

    public void createRoles() {
        this.thisYearBaseRole = createRoleIfNotExists(Main.STATE_INSTANCE.getThisYearBaseRole());
        this.thisYearWoerglRole = createRoleIfNotExists(Main.STATE_INSTANCE.getThisYearWoerglRole());
        this.thisYearIOIRole = createRoleIfNotExists(Main.STATE_INSTANCE.getThisYearIOIRole());
        this.thisYearCEOIRole = createRoleIfNotExists(Main.STATE_INSTANCE.getThisYearCEOIRole());
        this.thisYearEGOIRole = createRoleIfNotExists(Main.STATE_INSTANCE.getThisYearEGOIRole());
    }

    private long getNormalUserTextPermissions() {
        return Permission.getRaw(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND_IN_THREADS,
                Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_ATTACH_FILES, Permission.CREATE_PUBLIC_THREADS, Permission.CREATE_PRIVATE_THREADS,
                Permission.MESSAGE_TTS, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_EXT_STICKER, Permission.MESSAGE_EMBED_LINKS,
                Permission.USE_APPLICATION_COMMANDS);
    }

    public void setPermissions() {
        this.thisYearWienChannel.upsertPermissionOverride(guild.getPublicRole()).setDenied(Permission.ALL_PERMISSIONS).complete();
        this.thisYearWoerglChannel.upsertPermissionOverride(guild.getPublicRole()).setDenied(Permission.ALL_PERMISSIONS).complete();
        this.thisYearIOIChannel.upsertPermissionOverride(guild.getPublicRole()).setDenied(Permission.ALL_PERMISSIONS).complete();
        this.thisYearCEOIChannel.upsertPermissionOverride(guild.getPublicRole()).setDenied(Permission.ALL_PERMISSIONS).complete();
        this.thisYearEGOIChannel.upsertPermissionOverride(guild.getPublicRole()).setDenied(Permission.ALL_PERMISSIONS).complete();

        this.thisYearWienChannel.upsertPermissionOverride(guild.getRolesByName("Trainer", true).get(0)).setAllowed(Permission.ALL_PERMISSIONS).complete();
        this.thisYearWoerglChannel.upsertPermissionOverride(guild.getRolesByName("Trainer", true).get(0)).setAllowed(Permission.ALL_PERMISSIONS).complete();
        this.thisYearIOIChannel.upsertPermissionOverride(guild.getRolesByName("Trainer", true).get(0)).setAllowed(Permission.ALL_PERMISSIONS).complete();
        this.thisYearCEOIChannel.upsertPermissionOverride(guild.getRolesByName("Trainer", true).get(0)).setAllowed(Permission.ALL_PERMISSIONS).complete();
        this.thisYearEGOIChannel.upsertPermissionOverride(guild.getRolesByName("Trainer", true).get(0)).setAllowed(Permission.ALL_PERMISSIONS).complete();

        this.thisYearWienChannel.upsertPermissionOverride(guild.getRolesByName("Gast Trainer", true).get(0)).setAllowed(getNormalUserTextPermissions()).complete();
        this.thisYearWoerglChannel.upsertPermissionOverride(guild.getRolesByName("Gast Trainer", true).get(0)).setAllowed(getNormalUserTextPermissions()).complete();
        this.thisYearIOIChannel.upsertPermissionOverride(guild.getRolesByName("Gast Trainer", true).get(0)).setAllowed(getNormalUserTextPermissions()).complete();
        this.thisYearCEOIChannel.upsertPermissionOverride(guild.getRolesByName("Gast Trainer", true).get(0)).setAllowed(getNormalUserTextPermissions()).complete();
        this.thisYearEGOIChannel.upsertPermissionOverride(guild.getRolesByName("Gast Trainer", true).get(0)).setAllowed(getNormalUserTextPermissions()).complete();

        List<Role> roles = new ArrayList<>(List.of(thisYearBaseRole, thisYearWoerglRole, thisYearIOIRole, thisYearCEOIRole, thisYearEGOIRole));

        roles.forEach(a -> this.thisYearWienChannel.upsertPermissionOverride(a).setAllowed(getNormalUserTextPermissions()).complete());
        roles.remove(0);
        roles.forEach(a -> this.thisYearWoerglChannel.upsertPermissionOverride(a).setAllowed(getNormalUserTextPermissions()).complete());

        this.thisYearIOIChannel.upsertPermissionOverride(thisYearIOIRole).setAllowed(getNormalUserTextPermissions()).complete();
        this.thisYearCEOIChannel.upsertPermissionOverride(thisYearCEOIRole).setAllowed(getNormalUserTextPermissions()).complete();
        this.thisYearEGOIChannel.upsertPermissionOverride(thisYearEGOIRole).setAllowed(getNormalUserTextPermissions()).complete();
    }

    public void setRole(String user, Byte roles) {
        Member m = null;
        try {
            m = guild.retrieveMemberById(user).complete();
        } catch(ErrorResponseException ignored) {}

        if(m != null) {
            byte b = roles;
            boolean ready = false;

            if((roles & 0x0001) == 1) {
                guild.addRoleToMember(m, this.thisYearEGOIRole).complete();
                ready = true;
            }
            if((roles & 0x0002) == 2) {
                guild.addRoleToMember(m, this.thisYearCEOIRole).complete();
                ready = true;
            }
            if((roles & 0x0004) == 4) {
                guild.addRoleToMember(m, this.thisYearIOIRole).complete();
                ready = true;
            }

            if(ready) return;

            if((roles >> 3) == 1) {
                guild.addRoleToMember(m, this.thisYearWoerglRole).complete();
            } else if((roles >> 4) == 1) {
                guild.addRoleToMember(m, this.thisYearBaseRole).complete();
            }
        }
    }

}

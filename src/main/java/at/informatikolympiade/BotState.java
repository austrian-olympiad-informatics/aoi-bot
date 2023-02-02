package at.informatikolympiade;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class BotState {

    private static BotState INSTANCE;

    private String thisYear;
    private String thisYearBaseRole;
    private String thisYearWoerglRole;
    private String thisYearIOIRole;
    private String thisYearCEOIRole;
    private String thisYearEGOIRole;

    private String thisYearCategory;

    private String serverURI;

    private String secret;

    private boolean shouldClose;

    private HashMap<String, Byte> roles;
    private HashMap<String, Byte> oldRoles;
    private ReentrantLock lock;

    private BotState() {
        lock = new ReentrantLock(true);
        roles = new HashMap<>();
        oldRoles = new HashMap<>();
        this.secret = Config.INSTANCE.getString("Secret");
        this.serverURI = Config.INSTANCE.getString("ServerURI");
        this.shouldClose = false;
        updateYear(Config.INSTANCE.getString("CurrentYear"));
    }

    public static BotState getInstance() {
        if(INSTANCE == null)
            return INSTANCE = new BotState();
        else
            return INSTANCE;
    }

    public void updateYear(String year){
        this.thisYear = year;
        this.thisYearBaseRole = "AOI " + thisYear;
        this.thisYearWoerglRole = "Woergl " + thisYear;
        this.thisYearIOIRole = "IOI " + thisYear;
        this.thisYearCEOIRole = "CEOI " + thisYear;
        this.thisYearEGOIRole = "EGOI " + thisYear;
        this.thisYearCategory = "AOI" + thisYear;
    }

    public String getThisYear() {
        return thisYear;
    }

    public String getThisYearCategory() {
        return thisYearCategory;
    }

    public String getThisYearBaseRole() {
        return thisYearBaseRole;
    }

    public String getThisYearWoerglRole() {
        return thisYearWoerglRole;
    }

    public String getThisYearIOIRole() {
        return thisYearIOIRole;
    }

    public String getThisYearCEOIRole() {
        return thisYearCEOIRole;
    }

    public String getThisYearEGOIRole() {
        return thisYearEGOIRole;
    }

    public String getServerURI() {
        return serverURI;
    }

    public String getSecret() {
        return secret;
    }

    public HashMap<String, Byte> getChangedRoles() {
        HashMap<String, Byte> ret = (HashMap<String, Byte>) this.roles.keySet().stream()
                .filter(k -> !this.oldRoles.containsKey(k) || !this.oldRoles.get(k).equals(roles.get(k)))
                .collect(Collectors.toMap(key -> key, key -> roles.get(key)));
        this.oldRoles = this.roles;
        return ret;
    }

    public void setUserRoles(String id, Byte roles) {
        this.roles.put(id, roles);
    }

    public void setRoles(HashMap<String, Byte> roles) {
        this.oldRoles = this.roles;
        this.roles = roles;
    }

    public void lockMap() {
        this.lock.lock();
    }
    public void unlockMap() { this.lock.unlock(); }

    public boolean appShouldClose() {
        return shouldClose;
    }

    public void close() {
        this.shouldClose = true;
    }
}

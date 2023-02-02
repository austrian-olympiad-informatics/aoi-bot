package at.informatikolympiade;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;

public class Config {

    public static Config INSTANCE = new Config();

    private HashMap<String, Object> data;


    private Config() {
        loadConfig();
    }

    public void loadConfig() {
        try {
            File file = new File(getWorkingDir() + "/config.yml");
            if(!file.exists()) file.createNewFile();
            data = new Yaml().load(new FileInputStream(getWorkingDir() + "/config.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConfig() {
        try {
            File file = new File(getWorkingDir() + "/config.yml");
            if(!file.exists()) file.createNewFile();
            new Yaml().dump(data, new PrintWriter(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getString(String key) {
        if(!data.containsKey(key)) return "";
        return (String)data.get(key);
    }

    public int getInt(String key) {
        if(!data.containsKey(key)) return 0;
        return (int)data.get(key);
    }


    public void setConfig(String key, String value) {
        data.put(key, value);
    }

    private String getWorkingDir() {
        String folder = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        folder = folder.replace("\\", "/");
        folder = folder.substring(0, folder.lastIndexOf("/")+1);
        return folder;
    }

}

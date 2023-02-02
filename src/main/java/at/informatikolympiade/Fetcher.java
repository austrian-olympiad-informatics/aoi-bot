package at.informatikolympiade;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.*;

public class Fetcher extends Thread {


    public void waitOnMe() {
        synchronized (this) {
            try { this.wait(); } catch (InterruptedException ignored) {}
        }
    }

    @Override
    public void run() {
        HttpClient client = HttpClients.createDefault();

        while(!Main.STATE_INSTANCE.appShouldClose()) {
            try {
                sleep(5000);
            } catch (InterruptedException ignored) {}
            HttpPost post = new HttpPost(Main.STATE_INSTANCE.getServerURI() + "/api/bot/discord");

            StringEntity params;
            try {
                params = new StringEntity("{\"secret\" : \"" + Main.STATE_INSTANCE.getSecret() + "\"}");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            post.addHeader("Content-Type", "application/json");
            post.setEntity(params);

            try {
                HttpResponse resp = client.execute(post);

                if(resp.getStatusLine().getStatusCode() == 200) {
                    Main.STATE_INSTANCE.lockMap();
                    String response = new BasicResponseHandler().handleResponse(resp);
                    response = response.substring(1, response.length()-2).replace("\\", "");
                    Gson users = new Gson();
                    Type type = new TypeToken<HashMap<String, Byte>>(){}.getType();
                    Main.STATE_INSTANCE.setRoles(users.fromJson(response, type));
                    Main.STATE_INSTANCE.unlockMap();
                    synchronized (this) { this.notifyAll(); }
                } else {
                    synchronized (this) { this.notifyAll(); }
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}

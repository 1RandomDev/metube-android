package de.arandomdev.metube;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIClient {

    private OkHttpClient client;
    private Gson gson;

    public APIClient() {
        client = new OkHttpClient();
        gson = new Gson();

    }

    public boolean sendToMetube(String baseUrl, String videoUrl, String format, String quality) {
        try {
            RequestBody body = RequestBody.create(
                    "{\"quality\":\""+quality+"\",\"format\":\""+format+"\",\"url\":\""+videoUrl+"\"}",
                    MediaType.parse("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(baseUrl+"/add")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            if(response.code() == 200) {
                JsonObject responseObject = gson.fromJson(responseBody, JsonObject.class);
                if(responseObject.has("status") && responseObject.get("status").getAsString().equals("ok")) {
                    return true;
                }
            }
            Log.e("APIClient", "Download failed: "+responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

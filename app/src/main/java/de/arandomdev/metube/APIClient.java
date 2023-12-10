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

    public APIResult sendToMetube(String baseUrl, String videoUrl, String format, String quality) {
        try {
            RequestBody body = RequestBody.create(
                    "{\"quality\":\""+quality+"\",\"format\":\""+format+"\",\"auto_start\":true,\"url\":\""+videoUrl+"\"}",
                    MediaType.parse("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(baseUrl+"/add")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            if(response.code() == 200) {
                JsonObject responseObject = gson.fromJson(responseBody, JsonObject.class);
                if(responseObject.has("status")) {
                    if(responseObject.get("status").getAsString().equals("ok")) {
                        return new APIResult(true, null);
                    } else {
                        String message = responseObject.get("msg").getAsString();
                        return new APIResult(false, "Error downloading video: "+message);
                    }
                }
            }
            Log.e("APIClient", "Download failed: "+responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResult(false, "Error downloading video: "+e.getMessage());
        }
        return new APIResult(false, "Error downloading video.");
    }

    public class APIResult {
        private boolean success;
        private String message;

        public APIResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean success() {
            return success;
        }

        public String message() {
            return message;
        }
    }
}

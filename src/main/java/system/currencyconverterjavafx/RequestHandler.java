package system.currencyconverterjavafx;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RequestHandler {
    public JSONObject getURL(String url) throws IOException, JSONException {
        OkHttpClient mainClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = mainClient.newCall(request).execute();
        String html = response.body().string();
        JSONObject jsonObject = new JSONObject(html);
        JSONObject exhangeRates = jsonObject.getJSONObject("rates");
        return exhangeRates;
    }
}

package networking;

import android.content.Context;
import android.support.v4.util.Consumer;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

import config.ServerConfig;
import models.Counter;
import models.JsonParser;

public class RequestSender {

    private static RequestSender instance;

    RequestQueue requestQueue;

    private RequestSender (Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public static RequestSender getInstance(Context context) {
        if (instance == null) {
            instance = new RequestSender(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void addToQueue(Request r) {
        requestQueue.add(r);
    }

    public void getAllCounters(final Consumer<List<Counter>> consumer, final Consumer<VolleyError> errorHandler) {
        JsonArrayRequest allCountersRequest = new JsonArrayRequest(Request.Method.GET,
                ServerConfig.getFullCounterEndpoint(), null,
                response -> {
                    List<Counter> counters = JsonParser.parseIntoCounterList(response);
                    consumer.accept(counters);
                },
                error -> errorHandler.accept(error));
        addToQueue(allCountersRequest);
    }

}

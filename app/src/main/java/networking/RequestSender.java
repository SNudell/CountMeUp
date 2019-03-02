package networking;

import android.content.Context;
import android.support.v4.util.Consumer;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import config.ServerConfig;
import models.Counter;
import models.JsonParser;
import utils.SharedPreferencesInterface;

public class RequestSender {

    private static RequestSender instance;

    RequestQueue requestQueue;
    Context context;

    private RequestSender (Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.context = context;
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
        ServerConfig config = new SharedPreferencesInterface(context).loadServerConfig();
        JsonArrayRequest allCountersRequest = new JsonArrayRequest(Request.Method.GET,
                config.getFullCounterEndpoint(), null,
                response -> {
                    List<Counter> counters = JsonParser.parseIntoCounterList(response);
                    consumer.accept(counters);
                },
                error -> errorHandler.accept(error));
        addToQueue(allCountersRequest);
    }

    public void getCounter(String name, final Consumer<Counter> consumer, final Consumer<VolleyError> errorHandler) {
        ServerConfig config = new SharedPreferencesInterface(context).loadServerConfig();
        JsonObjectRequest fetchCounterRequest = new JsonObjectRequest(Request.Method.GET,
                config.getFullCounterEndpoint()+"/"+name,
                null,
                response -> {
                    Counter responseCounter = JsonParser.parseCounter(response);
                    consumer.accept(responseCounter);
                },
                error -> {
                    errorHandler.accept(error);
                });
        addToQueue(fetchCounterRequest);
    }

    public void incrementCounter(Counter counter, long increment, final Consumer<Counter> consumer, final Consumer<VolleyError> errorHandler) {
        ServerConfig config = new SharedPreferencesInterface(context).loadServerConfig();
        JSONObject body = new JSONObject();
        try {
            body.put("name", counter.getName());
            body.put("increment", increment);
        } catch (JSONException e) {
            System.out.println(e);
            return;
        }
        JsonObjectRequest incrementRequest = new JsonObjectRequest(Request.Method.PUT,
                config.getFullIncrementEndpoint(),
                body,
                response -> {
                    Counter responseCounter = JsonParser.parseCounter(response);
                    consumer.accept(responseCounter);
                },
                error -> {
                    errorHandler.accept(error);
                });
        System.out.println("sending " + body.toString() + " to "+ config.getFullIncrementEndpoint());
        addToQueue(incrementRequest);
    }

    public void decrementCounter(Counter counter, long decrement, final Consumer<Counter> consumer, final Consumer<VolleyError> errorHandler) {
        ServerConfig config = new SharedPreferencesInterface(context).loadServerConfig();
        JSONObject body = new JSONObject();
        try {
            body.put("name", counter.getName());
            body.put("decrement", decrement);
        } catch (JSONException e) {
            System.out.println(e);
            return;
        }
        JsonObjectRequest decrementRequest = new JsonObjectRequest(Request.Method.PUT,
                config.getFullDecrementEndpoint(),
                body,
                response -> {
                    Counter responseCounter = JsonParser.parseCounter(response);
                    consumer.accept(responseCounter);
                },
                error -> {
                    errorHandler.accept(error);
                });
        addToQueue(decrementRequest);
    }



}

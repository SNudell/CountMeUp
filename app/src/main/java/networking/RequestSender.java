package networking;

import android.content.Context;
import android.support.v4.util.Consumer;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gustav.countmeup.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import config.ServerConfig;
import models.Counter;
import models.JsonParser;
import utils.SharedPreferencesInterface;

public class RequestSender {

    private static RequestSender instance;

    private RequestQueue requestQueue;

    private RequestSender(Context context) {
        this.requestQueue = sslQueue(context);
    }

    public static RequestSender getInstance(Context context) {
        if (instance == null) {
            instance = new RequestSender(context);
        }
        return instance;
    }

    @SuppressWarnings("unchecked assignment") // this warning seems to be an error
    private void addToQueue(Request r) {
        requestQueue.add(r);
    }

    public void getAllCounters(Context context, final Consumer<List<Counter>> consumer, final Consumer<VolleyError> errorHandler) {
        ServerConfig config = new SharedPreferencesInterface(context).loadServerConfig();
        JsonArrayRequest allCountersRequest = new JsonArrayRequest(Request.Method.GET,
                config.getFullCounterEndpoint(), null,
                response -> {
                    List<Counter> counters = JsonParser.parseIntoCounterList(response);
                    consumer.accept(counters);
                },
                errorHandler::accept);
        addToQueue(allCountersRequest);
    }

    public void getCounter(Context context, String name, final Consumer<Counter> consumer, final Consumer<VolleyError> errorHandler) {
        ServerConfig config = new SharedPreferencesInterface(context).loadServerConfig();
        JsonObjectRequest fetchCounterRequest = new JsonObjectRequest(Request.Method.GET,
                config.getFullCounterEndpoint() + "/" + name,
                null,
                response -> {
                    Counter responseCounter = JsonParser.parseCounter(response);
                    consumer.accept(responseCounter);
                },
                errorHandler::accept);
        addToQueue(fetchCounterRequest);
    }

    public void incrementCounter(Context context, Counter counter, long increment, final Consumer<Counter> consumer, final Consumer<VolleyError> errorHandler) {
        ServerConfig config = new SharedPreferencesInterface(context).loadServerConfig();
        JSONObject body = new JSONObject();
        try {
            body.put("name", counter.getName());
            body.put("increment", increment);
        } catch (JSONException e) {
            System.out.println("JSONException when parsing counter into body for increment request " + e);
            return;
        }
        JsonObjectRequest incrementRequest = new JsonObjectRequest(Request.Method.PUT,
                config.getFullIncrementEndpoint(),
                body,
                response -> {
                    Counter responseCounter = JsonParser.parseCounter(response);
                    consumer.accept(responseCounter);
                },
                errorHandler::accept);
        System.out.println("sending " + body.toString() + " to " + config.getFullIncrementEndpoint());
        addToQueue(incrementRequest);
    }

    public void decrementCounter(Context context, Counter counter, long decrement, final Consumer<Counter> consumer, final Consumer<VolleyError> errorHandler) {
        ServerConfig config = new SharedPreferencesInterface(context).loadServerConfig();
        JSONObject body = new JSONObject();
        try {
            body.put("name", counter.getName());
            body.put("decrement", decrement);
        } catch (JSONException e) {
            System.out.println("JSONException when parsing counter into body for decrement request " + e);
            return;
        }
        JsonObjectRequest decrementRequest = new JsonObjectRequest(Request.Method.PUT,
                config.getFullDecrementEndpoint(),
                body,
                response -> {
                    Counter responseCounter = JsonParser.parseCounter(response);
                    consumer.accept(responseCounter);
                },
                errorHandler::accept);
        addToQueue(decrementRequest);
    }

    public void createNewCounter(Context context, Counter newCounter, final Consumer<Counter> consumer, final Consumer<VolleyError> errorHandler) {
        ServerConfig config = new SharedPreferencesInterface(context).loadServerConfig();
        JSONObject body = new JSONObject();
        try {
            body.put("name", newCounter.getName());
            body.put("value", newCounter.get());
        } catch (JSONException e) {
            System.out.println("JSONException when parsing counter into body for createCounter request " + e);
            return;
        }
        JsonObjectRequest createRequest = new JsonObjectRequest(Request.Method.POST,
                config.getFullCounterEndpoint(),
                body,
                response -> {
                    Counter responseCounter = JsonParser.parseCounter(response);
                    consumer.accept(responseCounter);
                },
                errorHandler::accept);
        addToQueue(createRequest);
    }

    public void deleteCounter(Context context, Counter counter, final Task task, final Consumer<VolleyError> errorHandler) {
        ServerConfig config = new SharedPreferencesInterface(context).loadServerConfig();
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE,
                config.getFullCounterEndpoint() + "/" + counter.getName(),
                string -> task.execute() ,
                errorHandler::accept) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    //noinspection CharsetObjectCanBeUsed because to use the fix an android version increase would be necessary
                    String json = new String(
                            response.data,
                            "UTF-8"
                    );

                    if (json.length() == 0) {
                        return Response.success(
                                null,
                                HttpHeaderParser.parseCacheHeaders(response)
                        );
                    } else {
                        return super.parseNetworkResponse(response);
                    }
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        addToQueue(deleteRequest);
    }

    private RequestQueue sslQueue(Context context) {
        HurlStack hurlStack = new HurlStack(null, newSslSocketFactory(context));
        return Volley.newRequestQueue(context, hurlStack);
    }

    private SSLSocketFactory newSslSocketFactory(Context context) {
        try {
            KeyStore trusted = KeyStore.getInstance("BKS");
            try (InputStream in = context.getResources().openRawResource(R.raw.countmeup)) {
                trusted.load(in, "sdgoD923sdingwe".toCharArray());
            }

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(trusted);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}

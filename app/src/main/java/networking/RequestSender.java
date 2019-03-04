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

    RequestQueue requestQueue;
    Context context;

    private RequestSender(Context context) {
        this.context = context;
        this.requestQueue = sslQueue();
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
                config.getFullCounterEndpoint() + "/" + name,
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
        System.out.println("sending " + body.toString() + " to " + config.getFullIncrementEndpoint());
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

    public void createNewCounter(Counter newCounter, final Consumer<Counter> consumer, final Consumer<VolleyError> errorHandler) {
        ServerConfig config = new SharedPreferencesInterface(context).loadServerConfig();
        JSONObject body = new JSONObject();
        try {
            body.put("name", newCounter.getName());
            body.put("value", newCounter.get());
        } catch (JSONException e) {
            System.out.println(e);
            return;
        }
        JsonObjectRequest createRequest = new JsonObjectRequest(Request.Method.POST,
                config.getFullCounterEndpoint(),
                body,
                response -> {
                    Counter responseCounter = JsonParser.parseCounter(response);
                    consumer.accept(responseCounter);
                },
                error -> {
                    errorHandler.accept(error);
                });
        addToQueue(createRequest);
    }

    public void deleteCounter(Counter counter, final Task task, final Consumer<VolleyError> errorHandler) {
        ServerConfig config = new SharedPreferencesInterface(context).loadServerConfig();
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE,
                config.getFullCounterEndpoint() + "/" + counter.getName(),
                string -> {
                    task.execute();
                },
                error -> {
                    errorHandler.accept(error);
                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
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

    private RequestQueue sslQueue() {

        HurlStack hurlStack = new HurlStack(null, newSslSocketFactory());


        RequestQueue queue = Volley.newRequestQueue(context, hurlStack);
        return queue;

    }

    private SSLSocketFactory newSslSocketFactory() {
        try {
            KeyStore trusted = KeyStore.getInstance("BKS");
            InputStream in = context.getResources().openRawResource(R.raw.countmeup);
            try {
                trusted.load(in, "sdgoD923sdingwe".toCharArray());
            } finally {
                in.close();
            }

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(trusted);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            SSLSocketFactory sf = context.getSocketFactory();
            return sf;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}

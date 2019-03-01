package com.gustav.countmeup.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gustav.countmeup.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import config.ServerConfig;
import models.Counter;
import models.JsonParser;
import networking.RequestSender;

public class ServerInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_input);
        findViewById(R.id.ConnectionErrorLabel).setVisibility(View.INVISIBLE);
    }

    public void connectToServer(View view) {
        configureServerAddress();
        RequestSender.getInstance(this).getAllCounters(list -> {
            System.out.println("number of received counters = " + list.size());
            findViewById(R.id.ConnectionErrorLabel).setVisibility(View.INVISIBLE);
            Intent intent = new Intent(this, CounterListActivity.class);
            // TODO: save the list into the database
        }, error -> {
            errorConnectiongToServer(error);
        });
    }

    private void errorConnectiongToServer(VolleyError e) {
        findViewById(R.id.ConnectionErrorLabel).setVisibility(View.VISIBLE);
        System.out.println(e);

    }

    private void configureServerAddress() {
        EditText ipInput = (EditText) findViewById(R.id.IPInput);
        EditText portInput = (EditText) findViewById(R.id.PortInput);
        ServerConfig.setIp(ipInput.getText().toString());
        ServerConfig.setPort(portInput.getText().toString());
        System.out.println(ServerConfig.getFullCounterEndpoint());
    }
}

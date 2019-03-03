package com.gustav.countmeup.activitys;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.VolleyError;

import com.gustav.countmeup.R;

import config.ServerConfig;
import networking.RequestSender;
import utils.SharedPreferencesInterface;

public class ServerInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_input);
        findViewById(R.id.ConnectionErrorLabel).setVisibility(View.INVISIBLE);
        getCountersFromServer();
    }

    public void connectToServer(View view) {
        configureServerAddress();
        getCountersFromServer();
    }

    private void getCountersFromServer() {
        RequestSender.getInstance(this).getAllCounters(list -> {
            System.out.println("number of received counters = " + list.size());
            findViewById(R.id.ConnectionErrorLabel).setVisibility(View.INVISIBLE);
            Handler mainHandler = new Handler(this.getMainLooper());
            mainHandler.post(() -> continueToCounterList());
        }, error -> {
            errorConnectiongToServer(error);
        });
    }

    private void errorConnectiongToServer(VolleyError e) {
        findViewById(R.id.ConnectionErrorLabel).setVisibility(View.VISIBLE);
        System.out.println(e);
    }

    private void configureServerAddress() {
        EditText ipInput = findViewById(R.id.IPInput);
        EditText portInput = findViewById(R.id.PortInput);
        ServerConfig config = new ServerConfig(ipInput.getText().toString(), portInput.getText().toString());
        new SharedPreferencesInterface(this).saveServerConfig(config);
    }

    private void continueToCounterList() {
        Intent intent = new Intent(this, CounterListActivity.class);
        startActivity(intent);
    }
}

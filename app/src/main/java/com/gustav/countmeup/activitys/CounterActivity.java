package com.gustav.countmeup.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gustav.countmeup.R;

import models.Counter;
import networking.RequestSender;

public class CounterActivity extends AppCompatActivity {

    TextView nameView;
    TextView valueView;
    EditText deltaView;

    Counter counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        hookUpViews();
        initCounter();
    }

    private void hookUpViews() {
        nameView = (TextView) findViewById(R.id.CounterNameDisplay);
        valueView = (TextView) findViewById(R.id.CounterValueDisplay);
        deltaView = (EditText) findViewById(R.id.DeltaValueDisplay);
    }

    private void initCounter() {
        String name = getIntent().getStringExtra("counterName");
        long value = getIntent().getLongExtra("counterValue", 42);
        counter = new Counter(value, name);
        updateViews();
    }

    private void updateViews() {
        nameView.setText(counter.getName());
        valueView.setText("" + counter.get());

    }

    // called by incrementButton on click
    public void increment(View view) {
        String delta = deltaView.getText().toString();
        long deltaValue = Long.parseLong(delta);
        RequestSender sender = RequestSender.getInstance(this);
        sender.incrementCounter(counter, deltaValue,
                responseCounter -> {
                    this.counter = responseCounter;
                    System.out.println("response counter = "+ counter.getName() +": "+counter.get());
                    updateViews();
                },
                error -> {
                    System.out.println(error);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(this, "error", duration);
                    toast.show();
                }
        );
    }

    // called by decrementButton on click
    public void decrement(View view) {
        String delta = deltaView.getText().toString();
        long deltaValue = Long.parseLong(delta);
        RequestSender sender = RequestSender.getInstance(this);
        sender.decrementCounter(counter, deltaValue,
                responseCounter -> {
                    this.counter = responseCounter;
                    System.out.println("response counter = "+ counter.getName() +": "+counter.get());
                    updateViews();
                },
                error -> {
                    System.out.println(error);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(this, "error", duration);
                    toast.show();
                }
        );
    }

}

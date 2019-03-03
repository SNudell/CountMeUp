package com.gustav.countmeup.activitys;

import android.app.AlertDialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gustav.countmeup.R;

import models.Counter;
import networking.RequestSender;
import utils.Repeater;
import utils.ToastDisplayer;
import utils.ValueVerifier;

public class CounterActivity extends AppCompatActivity {

    TextView nameView;
    TextView valueView;
    EditText deltaView;

    Counter counter;

    Repeater repeater;

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
        requestUpdate();
        startUpdater();
    }

    @Override
    protected void onStop() {
        super.onStop();
        repeater.terminate();
    }

    private void hookUpViews() {
        nameView = findViewById(R.id.CounterNameDisplay);
        valueView = findViewById(R.id.CounterValueDisplay);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            valueView.setAutoSizeTextTypeUniformWithConfiguration(
                    12, 200, 2, TypedValue.COMPLEX_UNIT_DIP);
        }
        deltaView = findViewById(R.id.DeltaValueDisplay);
    }

    private void requestUpdate() {
        RequestSender.getInstance(this).getCounter(counter.getName(), newCounter -> {
            counter = newCounter;
            updateViews();
        }, new ToastDisplayer.ErrorToaster(this));
    }

    private void startUpdater() {
        repeater = new Repeater(()-> requestUpdate(),1000);
        repeater.start();
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
        long deltaValue = getDelta();
        if (deltaValue == 0) {
            return;
        }
        RequestSender sender = RequestSender.getInstance(this);
        sender.incrementCounter(counter, deltaValue,
                responseCounter -> {
                    this.counter = responseCounter;
                    System.out.println("response counter = " + counter.getName() + ": " + counter.get());
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
        long deltaValue = getDelta();
        if (deltaValue == 0) {
            return;
        }
        RequestSender sender = RequestSender.getInstance(this);
        sender.decrementCounter(counter, deltaValue,
                responseCounter -> {
                    this.counter = responseCounter;
                    System.out.println("response counter = " + counter.getName() + ": " + counter.get());
                    updateViews();
                },
                error -> {
                    ToastDisplayer.displayError(this, error);
                }
        );
    }

    public void deleteCounter(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = getString(R.string.DeleteCounterMessage) + counter.getName() + " ?";

        builder.setMessage(message)

                .setPositiveButton(R.string.ConfirmDelete, (dialog, which) -> {
                    repeater.terminate();
                    RequestSender.getInstance(this).deleteCounter(counter, () -> {
                                System.out.println("closing activity");
                                finish();
                            },
                            (error) -> {
                                ToastDisplayer.displayError(this, error);
                                repeater.start();
                            });
                })

                .setNegativeButton(R.string.CancelButton, null).create().show();
    }

    private long getDelta() {
        String delta = deltaView.getText().toString();
        if (!ValueVerifier.toastIfInvalidDelta(this, delta)) {
            return 0;
        }
        long deltaValue = Long.parseLong(delta);
        return deltaValue;
    }

}

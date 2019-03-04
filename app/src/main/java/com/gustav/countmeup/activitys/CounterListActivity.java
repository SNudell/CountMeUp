package com.gustav.countmeup.activitys;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gustav.countmeup.R;

import java.util.ArrayList;
import java.util.List;

import models.Counter;
import networking.RequestSender;
import utils.Repeater;
import utils.ToastDisplayer;
import utils.ValueVerifier;

public class CounterListActivity extends ListActivity {

    List<Counter> counters;
    ArrayAdapter<Counter> adapter;

    Repeater repeater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_list_activity);
        counters = new ArrayList<>();
        counters.add(new Counter(7, "test"));

        CounterListAdapter adapter = new CounterListAdapter(this, counters);
        getListView().setAdapter(adapter);
        this.adapter = adapter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startRepeater();
    }

    @Override
    protected void onStop() {
        super.onStop();
        repeater.terminate();
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);
        Intent intent = new Intent(this, CounterActivity.class);
        Counter counter = counters.get(position);
        intent.putExtra("counterName", counter.getName());
        intent.putExtra("counterValue", counter.get());
        startActivity(intent);
    }

    private void requestData() {
        RequestSender.getInstance(this).getAllCounters(this, counters -> {
            this.counters.clear();
            this.counters.addAll(counters);
            this.dataChanged();
        }, new ToastDisplayer.ErrorToaster(this));
    }

    private void startRepeater() {
        this.repeater = new Repeater(this::requestData, 1000);
        repeater.start();
    }

    private void dataChanged() {
        adapter.notifyDataSetChanged();
    }

    // called by floating action button
    public void addNewCounter(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText nameInput = new EditText(this);
        nameInput.setHint(R.string.NewCounterNameHint);
        EditText valueInput = new EditText(this);
        valueInput.setHint(R.string.NewCounterValueHint);
        valueInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(nameInput);
        layout.addView(valueInput);
        int padding = 50;
        layout.setPadding(padding,padding,padding,padding);
        builder.setMessage(R.string.NewCounterAlertMessage)
                .setView(layout)
                .setNegativeButton(R.string.CancelButton, null)
                .setPositiveButton(R.string.ConfirmButton, null);
        // will set the listener to null at first so i can determine when to close the dialog myself
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener((d) -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener( (b) -> {
                String name = nameInput.getText().toString().trim();
                if (!ValueVerifier.toastIfIsInvalidCounterName(this, name)) {
                    return;
                }
                String initialValue = valueInput.getText().toString();
                if (!ValueVerifier.toastIfInvalidLong(this, initialValue)) {
                    return;
                }
                long value = Long.parseLong(initialValue);
                Counter newCounter = new Counter(value, name);
                RequestSender.getInstance(this).createNewCounter(this, newCounter, counter -> requestData(), new ToastDisplayer.ErrorToaster(this));
                dialog.dismiss();
            });
        });
        dialog.show();
    }

}
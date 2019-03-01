package com.gustav.countmeup.activitys;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.gustav.countmeup.R;

import java.util.ArrayList;
import java.util.List;

import models.Counter;
import networking.RequestSender;

public class CounterListActivity extends ListActivity {

    List<Counter> counters;
    ArrayAdapter<Counter> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_list_activity);
        counters = new ArrayList<>();
        counters.add(new Counter(7, "test"));

        CounterListAdapter adapter = new CounterListAdapter(this, counters);
        getListView().setAdapter(adapter);
        this.adapter = adapter;

        requestData();
    }

    private void requestData() {
        RequestSender.getInstance(this).getAllCounters(counters -> {
            this.counters.removeAll(this.counters);
            this.counters.addAll(counters);
            this.dataChanged();
        }, error -> System.out.println(error));
    }

    private void dataChanged() {
        adapter.notifyDataSetChanged();
    }


}

package com.gustav.countmeup.activitys;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
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

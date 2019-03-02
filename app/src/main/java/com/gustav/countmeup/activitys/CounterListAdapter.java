package com.gustav.countmeup.activitys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gustav.countmeup.R;

import java.util.List;

import models.Counter;

public class CounterListAdapter extends ArrayAdapter<Counter> {

    private Context context;

    private List<Counter> counters;

    public CounterListAdapter (Context context, List<Counter> counters) {
        super(context, R.layout.counter_row, counters);
        this.context = context;
        this.counters = counters;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.counter_row, parent, false);
        TextView nameDisplay = rowView.findViewById(R.id.nameDisplay);
        TextView valueDisplay = rowView.findViewById(R.id.valueDisplay);
        nameDisplay.setText(counters.get(position).getName());
        valueDisplay.setText(""+ counters.get(position).get());
        return rowView;
    }

}

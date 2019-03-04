package com.gustav.countmeup.activitys;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    CounterListAdapter (Context context, List<Counter> counters) {
        super(context, R.layout.counter_row, counters);
        this.context = context;
        this.counters = counters;
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        CounterListViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.counter_row, parent, false);

            TextView nameDisplay = convertView.findViewById(R.id.nameDisplay);
            TextView valueDisplay = convertView.findViewById(R.id.valueDisplay);

            viewHolder = new CounterListViewHolder(nameDisplay, valueDisplay);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CounterListViewHolder) convertView.getTag();
        }


        viewHolder.nameDisplay.setText(counters.get(position).getName());
        viewHolder.valueDisplay.setText(String.valueOf(counters.get(position).get()));
        return convertView;
    }

    static class CounterListViewHolder {
        TextView nameDisplay;
        TextView valueDisplay;

        CounterListViewHolder (TextView nameDisplay, TextView valueDisplay) {
            this.nameDisplay = nameDisplay;
            this.valueDisplay = valueDisplay;
        }
    }

}

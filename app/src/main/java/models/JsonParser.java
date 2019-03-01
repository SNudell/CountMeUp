package models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public static List<Counter> parseIntoCounterList(JSONArray json) {
        List<Counter> counters = new ArrayList<>();
        int index = 0;
        while (true) {
            try {
                JSONObject jsonCounter = json.getJSONObject(index);
                counters.add(parseCounter(jsonCounter));
                index++;
            } catch (JSONException e) {
                break;
            }
        }
        return counters;
    }

    public static Counter parseCounter(JSONObject json) throws JSONException {
        long value = json.getLong("counterStatus");
        String name = json.getString("name");
        return new Counter(value, name);
    }

}

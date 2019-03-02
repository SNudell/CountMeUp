package utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.gustav.countmeup.R;

import config.ServerConfig;

public class SharedPreferencesInterface {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    private static final String SHARED_PREFERENCES_IP_KEY = "server_ip";
    private static final String SHARED_PREFERENCES_PORT_KEY = "server_port";

    public SharedPreferencesInterface(Context context) {
        this.preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        this.editor = preferences.edit();
        this.context = context;
    }

    public SharedPreferencesInterface save(String key, String value) {
        editor.putString(key, value);
        return this;
    }

    public void saveServerConfig (ServerConfig config) {
        save(SHARED_PREFERENCES_IP_KEY, config.getIp());
        save(SHARED_PREFERENCES_PORT_KEY, config.getPort());
        apply();
    }

    public ServerConfig loadServerConfig() {
        String ip = preferences.getString(SHARED_PREFERENCES_IP_KEY, context.getString(R.string.SuggestedIP));
        String port = preferences.getString(SHARED_PREFERENCES_PORT_KEY, context.getString(R.string.SuggestedPort));
        return new ServerConfig(ip, port);
    }

    public void apply() {
        editor.apply();
    }
}

package utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.gustav.countmeup.R;

import config.ServerConfig;

public class SharedPreferencesInterface {

    private SharedPreferences preferences;
    private Context context;

    private static final String SHARED_PREFERENCES_IP_KEY = "server_ip";
    private static final String SHARED_PREFERENCES_PORT_KEY = "server_port";

    public SharedPreferencesInterface(Context context) {
        this.preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        this.context = context;
    }

    public void saveServerConfig (ServerConfig config) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SHARED_PREFERENCES_IP_KEY, config.getIp());
        editor.putString(SHARED_PREFERENCES_PORT_KEY, config.getPort());
        editor.apply();
    }

    public ServerConfig loadServerConfig() {
        String ip = preferences.getString(SHARED_PREFERENCES_IP_KEY, context.getString(R.string.SuggestedIP));
        String port = preferences.getString(SHARED_PREFERENCES_PORT_KEY, context.getString(R.string.SuggestedPort));
        return new ServerConfig(ip, port);
    }
}

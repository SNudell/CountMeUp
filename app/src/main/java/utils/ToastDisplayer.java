package utils;

import android.content.Context;
import android.support.v4.util.Consumer;
import android.widget.Toast;

import com.android.volley.VolleyError;


public class ToastDisplayer {

    public static void displayError(Context context, VolleyError error) {
        System.out.println(error);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, "error", duration);
        toast.show();
    }

    public static class ErrorToaster implements Consumer<VolleyError> {

        private Context context;

        public ErrorToaster(Context context) {
            this.context = context;
        }

        @Override
        public void accept(VolleyError volleyError) {
            displayError(context, volleyError);
        }
    }
}

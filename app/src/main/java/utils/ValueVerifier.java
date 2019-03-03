package utils;

import android.content.Context;

import com.gustav.countmeup.R;

public class ValueVerifier {

    public static boolean isValidCounterName(String name) {
        for (Character c : name.toCharArray()) {
            if (Character.isLowerCase(c) || Character.isUpperCase(c) || Character.isDigit(c) || Character.isWhitespace(c)) {
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean toastIfIsInvalidCounterName(Context context, String name) {
        if (!isValidCounterName(name)) {
            ToastDisplayer.displayMessage(context, context.getString(R.string.InvalidNameMessage), 5000);
            return false;
        }
        return true;
    }

    public static boolean isValidLong(String number) {
        try {
            long value = Long.parseLong(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean toastIfInvalidDelta(Context context, String number) {
        if (!toastIfInvalidLong(context, number)) {
            return false;
        }
        long deltaValue = Long.parseLong(number);
        if (deltaValue <= 0) {
            ToastDisplayer.displayMessage(context, context.getString(R.string.NegativeDelta), 5000);
            return false;
        }
        return true;
    }

    public static boolean toastIfInvalidLong(Context context, String number) {
        if (!isValidLong(number)) {
            ToastDisplayer.displayMessage(context, context.getString(R.string.DeltaMustBeNumber));
            return false;
        }
        return true;
    }
}

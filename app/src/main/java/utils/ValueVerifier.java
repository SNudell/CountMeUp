package utils;

import android.content.Context;

import com.gustav.countmeup.R;

public class ValueVerifier {

    private static boolean isValidCounterName(String name) {
        for (Character c : name.toCharArray()) {
            if (!isAllowedCharacter(c)) {
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

    private static boolean isValidLong(String number) {
        try {
            Long.parseLong(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean toastIfInvalidDelta(Context context, String number) {
        if (toastIfInvalidLong(context, number)) {
            long deltaValue = Long.parseLong(number);
            return deltaValue > 0;
        }
        return false;
    }

    public static boolean toastIfInvalidLong(Context context, String number) {
        if (!isValidLong(number)) {
            ToastDisplayer.displayMessage(context, context.getString(R.string.DeltaMustBeNumber));
            return false;
        }
        return true;
    }

    private static boolean isAllowedCharacter(char c) {
        return Character.isLowerCase(c) || Character.isUpperCase(c) || Character.isDigit(c) || Character.isWhitespace(c);
    }
}

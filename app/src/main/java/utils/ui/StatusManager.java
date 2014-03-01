package utils.ui;

import android.graphics.Color;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AdYa on 01.03.14.
 */
public class StatusManager {

    private static ArrayList<TextView> statusViews = new ArrayList<>();

    public static void addStatusView(TextView view) {
        if (view != null && !statusViews.contains(view))
            statusViews.add(view);
    }

    public static void setStatus(String statusText, int statusColor) {
        for (TextView view : statusViews) {
            view.setTextColor(statusColor);
            view.setText(statusText);
        }
    }

    public static void setWarning(String warningText) {
        setStatus(warningText, Color.argb(200, 230, 200, 55));
    }

    public static void setError(String errorText) {
        setStatus(errorText, Color.argb(200, 235, 0, 0));
    }

    public static void setSuccess(String successText) {
        setStatus(successText, Color.argb(200, 25, 195, 65));
    }

    public static void setStatus(String statusText) {
        setStatus(statusText, Color.argb(200, 135, 135, 135));
    }


}

package com.trysafe.trysafe.Utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;

import com.trysafe.trysafe.R;

public class SnackbarHelper {

    public static void configSnackbar(Context context, Snackbar snack) {
        addMargins(snack);
        setRoundBordersBg(context, snack);
        ViewCompat.setElevation(snack.getView(), 14f);
    }


    private static void addMargins(Snackbar snack) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snack.getView().getLayoutParams();
        params.setMargins(18, 14, 18, 22);
        snack.getView().setLayoutParams(params);
    }

    private static void setRoundBordersBg(Context context, Snackbar snackbar) {
        snackbar.getView().setBackground(context.getDrawable(R.drawable.sncak_bar_background));
    }
}

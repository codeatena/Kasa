package com.general.mediaplayer.kasa.utility;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by mac on 25/05/2017.
 */

public class AlertUtility {

    static public void showAlert(Context context ,String msg)
    {
        new MaterialDialog.Builder(context)
                .content(msg)
                .positiveText(android.R.string.ok)
                .show();
    }
}

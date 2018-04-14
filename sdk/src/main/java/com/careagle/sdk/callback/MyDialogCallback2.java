package com.careagle.sdk.callback;

import android.content.DialogInterface;

/**
 * Created by hyxf on 2017/4/27.
 */

public interface MyDialogCallback2 {
    void onClickOk(DialogInterface dialogInterface, int which);

    void onCancel(DialogInterface dialogInterface, int which);
}

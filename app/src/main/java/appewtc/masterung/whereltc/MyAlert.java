package appewtc.masterung.whereltc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by masterUNG on 12/14/2016 AD.
 */

public class MyAlert {

    //Explicit
    private Context context;
    private String titleString, messageString;
    private int anInt;

    public MyAlert(Context context, String titleString, String messageString, int anInt) {
        this.context = context;
        this.titleString = titleString;
        this.messageString = messageString;
        this.anInt = anInt;
    }

    public void myDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setIcon(anInt);
        builder.setTitle(titleString);
        builder.setMessage(messageString);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }

}   // Main Class

package cvnhan.android.smscontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by NhanCao on 30-May-15.
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    public static final String RECEIVE = "cvnhan.android.smscontrol.receivesms";
    public static final String NUMBER = "cvnhan.android.smscontrol.phonenumber";
    public static final String MESSAGE = "cvnhan.android.smscontrol.message";

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Number = "number";

    public static void setNumber(Context context, String number){
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Number, number);
        editor.commit();
    }
    public static String getNumber(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(Number, "01236647568");
    }

    // broadcast a custom intent.
    public static void broadcastIntentMyReceive(Context context, String number, String message) {
        Intent intent = new Intent();
        intent.setAction(RECEIVE);
        intent.putExtra(NUMBER, number);
        intent.putExtra(MESSAGE, message);
        context.sendBroadcast(intent);
    }

    public static void sendSMSMessage(Context context, String phoneNo, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(context, "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}

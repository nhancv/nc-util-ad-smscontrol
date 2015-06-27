package cvnhan.android.smscontrol;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.content.DialogInterface.OnClickListener;

/**
 * Created by NhanCao on 30-May-15.
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    public static final String RECEIVE = "cvnhan.android.smscontrol.receivesms";
    public static final String NUMBER = "cvnhan.android.smscontrol.phonenumber";
    public static final String MESSAGE = "cvnhan.android.smscontrol.message";

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Number = "number";

    public static void setNumber(Context context, String number) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Number, number);
        editor.commit();
    }

    public static String getNumber(Context context) {
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

    public static void sendSMSMessage(Activity activity, final Context context, final String phoneNo, final String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.DialogBlack)).create();
        alertDialog.setTitle("Are you sure?");
        alertDialog.setMessage("Click Ok to send message");
        alertDialog.setButton(BUTTON_POSITIVE, "Ok", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(context, "SMS sent.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(context, "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        alertDialog.setButton(BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    static InputStream is = null;
    public static String json = "";
    static StringBuilder sb;
    public static volatile boolean parsingComplete = true;
    public static String checkUrl(String url) {
        String s = url;
        if (url.indexOf("http://") == -1) {
            s = "http://" + url;
        }
        return s;
    }
    public static void solveCommand(String url, final List<NameValuePair> params) {
        url=checkUrl(url);
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String paramString = URLEncodedUtils.format(params, "utf-8");
            url += "?" + paramString;
            Log.e("urlcommand",url);
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "utf8"), 8);
            sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            readAndParseJSONcomment(json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
    }
    @SuppressLint("NewApi")
    public static void readAndParseJSONcomment(String in) {
        try {
            JSONObject reader = new JSONObject(in);

            parsingComplete = false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }




}

package cvnhan.android.smscontrol;

import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cvnhan.android.smscontrol.fragment.PhongBa;

public class HandleJSON {

    static InputStream is = null;
    static String json = "";

    private String urlString = null;
    public volatile boolean parsingComplete = true;

    public int success = 0;
    public ArrayList<Room_Info_Values> ROOM = new ArrayList<Room_Info_Values>();
    public ArrayList<String> UserInfo = new ArrayList<String>();

    public HandleJSON(String url) {
        this.urlString = url;
    }

	/*
     * Device
	 */
    // ////////////////////readAndParseJSON////////////////////////////////////////

    public void readAndParseJSON_GetAllDevice(String inJson) {
        try {
            JSONObject reader = new JSONObject(inJson);
            for (int i = 0; i < 3; i++) {
                JSONObject obj = reader.getJSONObject("room" + i);
                int temp, humi;
                String key;
                temp = obj.getInt("temp");
                humi = obj.getInt("humi");
                key = obj.getString("key");
                ROOM.add(new Room_Info_Values(temp, humi, key));
            }
            parsingComplete = false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //Log.e("parsejson", e.toString());
        }
    }

    public void readAndParseJSON_GetResult(String inJson) {
        try {
            JSONObject reader = new JSONObject(inJson);
            success = Integer.valueOf(reader.getString("success"));
            if (success == 1) {
                parsingComplete = false;
            } else {
                parsingComplete = false;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            //Log.e("parsejson", e.toString());
        }
    }

    public void readAndParseJSON_GetUserInfo(String inJson) {
        try {
            JSONObject reader = new JSONObject(inJson);
            String user = reader.getString("user");
            String pass = reader.getString("pass");
            UserInfo.add(user);
            UserInfo.add(pass);
            parsingComplete = false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //Log.e("parsejson", e.toString());
        }
    }

    // ///////////////////////////////////////////////////////////////////////////
    // ---------------------------------FetchJSON------------------------------------

    public String fetchJSON(final String method,
                            final List<NameValuePair> params) {
        // Thread thread = new Thread(new Runnable() {
        // @Override
        // public void run() {
        // try {

        // Making HTTP request
        try {

            // check for request method
            if (method == "POST") {
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urlString);
//				Log.e("httpPost", urlString);
                if (params.size() > 0)
                    httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } else if (method == "GET") {
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();

                String paramString = URLEncodedUtils.format(params, "utf-8");
                if (params.size() > 0)
                    urlString += "?" + paramString;
                //Log.e("urlString", urlString);
                HttpGet httpGet = new HttpGet(urlString);
//				URI uri = httpGet.getURI();
//				Log.e("Host", uri.getHost());
//				Log.e("Path", uri.getPath());
//				Log.e("Query", String.valueOf(uri.getQuery()));
//				Log.e("Authority", String.valueOf(uri.getAuthority()));
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
                // Log.e("httpEntity", convertStreamToString(is));
            }

        } catch (Exception e) {

        }
        if (is != null)
            try {
                // Log.e("fetchJSON",urlString);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "utf8"), 8);
//				Log.e("Buffer", "2");
                StringBuilder sb = new StringBuilder();
//				Log.e("Buffer", "3");
                String line = null;
//				Log.e("Buffer", "4");
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
//				Log.e("Buffer", "5");
                json = sb.toString();
//				Log.e("Buffer", "6->" + json);
                int left = json.indexOf("<body>") + 6;
                int right = json.indexOf("</body>");
                json = json.substring(left, right);
//				Log.e("Buffer", "json-final: " + json);
            } catch (Exception e) {
                //Log.e("Buffer Error", "Error converting result " + e.toString());
            }
        return json;
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // });
        // thread.start();
    }

    public String fetchJSON_GetnoParams() {
        // Making HTTP request
        // Thread thread = new Thread(new Runnable() {
        // @Override
        // public void run() {
        // try {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlString);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
        } catch (ClientProtocolException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (NetworkOnMainThreadException e) {

        }
        try {
            Log.e("urlGet", urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "utf8"), 8);
            Log.e("Buffer", "2");
            StringBuilder sb = new StringBuilder();
            // Log.e("Buffer", "3");
            String line = null;
            Log.e("Buffer", "4");
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            Log.e("Buffer", "5");
            json = sb.toString();
            Log.e("Buffer", "6->" + json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // Log.e("jsontmp", json);
        return json;
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // });
        // thread.start();
    }

    public String fetchJSON_PostnoParams() {
        // Making HTTP request
        // Thread thread = new Thread(new Runnable() {
        // @Override
        // public void run() {
        // try {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urlString);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
        } catch (ClientProtocolException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (NetworkOnMainThreadException e) {

        }
        try {
            // Log.e("urlGet",urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "utf8"), 8);
            // Log.e("Buffer", "2");
            StringBuilder sb = new StringBuilder();
            // Log.e("Buffer", "3");
            String line = null;
            // Log.e("Buffer", "4");
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            // Log.e("Buffer", "5");
            json = sb.toString();
            // Log.e("Buffer", "6->" + json);
        } catch (Exception e) {
            //Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // Log.e("jsontmp", json);
        return json;
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // });
        // thread.start();
    }

    public void fetchJSON_SetDevice(final PhongBa phongBa, final View view) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(urlString);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NetworkOnMainThreadException e) {
                    e.printStackTrace();
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, "utf8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    json = sb.toString();
                    if (json.length() > 0) {
                        phongBa.updateBtn((Button) view, !phongBa.getStatus((Button) view));
                    }
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }
            }
        });
        thread.start();
    }

    // ------------------------------------------------------------------------------

    /**
     * ************************** STATIC *******************************************
     */
    public static String checkUrl(String urlString) {
        String s = urlString;
        if (urlString.indexOf("http://") == -1) {
            s = "http://" + urlString;
        }
        return s;
    }

    public static String loginProcess(String urlString, final List<NameValuePair> params) {
        urlString = checkUrl(urlString);
        HandleJSON obj = new HandleJSON(urlString);
        String s = obj.fetchJSON("GET", params);
        String key = "";
        try {
            JSONObject reader = new JSONObject(s);
            key = reader.getString("success");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //Log.e("parsejson", e.toString());
        }
//		Log.e("obj", s);
        return key;
    }

    public static ArrayList<Room_Info_Values> getAllDevice(String urlString,
                                                           final String method, final List<NameValuePair> params) {
        urlString = checkUrl(urlString);
        HandleJSON obj = new HandleJSON(urlString);
        String s = obj.fetchJSON(method, params);
        obj.readAndParseJSON_GetAllDevice(s);
        while (obj.parsingComplete)
            ;
//		Log.e("obj", s);
        return obj.ROOM;
    }

    public static boolean getResult(String urlString, final String method,
                                    final List<NameValuePair> params) {
        urlString = checkUrl(urlString);
        HandleJSON obj = new HandleJSON(urlString);
        obj.readAndParseJSON_GetResult(obj.fetchJSON(method, params));
        while (obj.parsingComplete)
            ;
        return (obj.success == 1) ? true : false;
    }

    public static void setDevice(String urlString, PhongBa phongBa, View view) {
        urlString = checkUrl(urlString);
//        Log.e("setDevice", urlString);
        HandleJSON obj = new HandleJSON(urlString);
        obj.fetchJSON_SetDevice(phongBa, view);
    }

    public static ArrayList<String> getUserInfo(String urlString,
                                                final String method, final List<NameValuePair> params) {
        urlString = checkUrl(urlString);
        HandleJSON obj = new HandleJSON(urlString);
        obj.readAndParseJSON_GetUserInfo(obj.fetchJSON(method, params));
        while (obj.parsingComplete)
            ;
        return obj.UserInfo;
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.io.InputStream istmp = is;
        java.util.Scanner s = new java.util.Scanner(istmp).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /*****************************************************************************************/
}

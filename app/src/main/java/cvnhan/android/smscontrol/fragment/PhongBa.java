package cvnhan.android.smscontrol.fragment;

import android.view.View;
import android.widget.Button;

import cvnhan.android.smscontrol.HandleJSON;
import cvnhan.android.smscontrol.R;

/**
 * Created by NhanCao on 30-May-15.
 */
public class PhongBa extends RoomBase {
    public static String SYSTEM_HOST = "192.168.2.123";

    @Override
    public void onResume() {
        setupButtonName();
        super.onResume();
    }

    private void setupButtonName() {
        tvTemp.setText("Zigbee");
        btn1.setText("So 1");
        btn2.setText("So 2");
        btn3.setText("So 3");
        btn4.setText("So 4");
        btn5.setText("So 5");
        btn6.setText("So 6");
        btn7.setText("So 7");
        btn8.setText("So 8");
        btn9.setText("So 9");
    }

    @Override
    public void onClick(final View view) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                String myURLSet = "";
                switch (view.getId()) {
                    case R.id.btn1:
                        myURLSet = SYSTEM_HOST + "/set1-05";
                        break;
                    case R.id.btn2:
                        myURLSet = SYSTEM_HOST + "/set1-06";
                        break;
                    case R.id.btn3:
                        myURLSet = SYSTEM_HOST + "/set1-07";
                        break;
                    case R.id.btn4:
                        myURLSet = SYSTEM_HOST + "/set1-08";
                        break;
                    case R.id.btn5:
                        myURLSet = SYSTEM_HOST + "/set1-09";
                        break;
                    case R.id.btn6:
                        myURLSet = SYSTEM_HOST + "/set1-10";
                        break;
                    case R.id.btn7:
                        myURLSet = SYSTEM_HOST + "/set1-11";
                        break;
                    case R.id.btn8:
                        myURLSet = SYSTEM_HOST + "/set1-12";
                        break;
                    case R.id.btn9:
                        myURLSet = SYSTEM_HOST + "/set1-13";
                        break;
                }

                if (getStatus((Button) view)) {
                    myURLSet += "-00";
                } else {
                    myURLSet += "-01";
                }
                if (myURLSet.length() > 0)
                    HandleJSON.setDevice(myURLSet, PhongBa.this, (Button) view);
            }
        }, 350);
    }
}
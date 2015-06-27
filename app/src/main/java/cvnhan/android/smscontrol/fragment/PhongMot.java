package cvnhan.android.smscontrol.fragment;

/**
 * Created by NhanCao on 30-May-15.
 */
public class PhongMot extends RoomBase{

    @Override
    public void onResume() {
        setupButtonName();
        super.onResume();
    }

    private void setupButtonName() {
        tvTemp.setText("Phong mot");
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

}
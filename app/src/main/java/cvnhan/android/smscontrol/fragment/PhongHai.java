package cvnhan.android.smscontrol.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import cvnhan.android.smscontrol.MainActivity;
import cvnhan.android.smscontrol.R;
import cvnhan.android.smscontrol.bluetooth.BluetoothService;

/**
 * Created by NhanCao on 30-May-15.
 */
public class PhongHai extends RoomBase {


    String command = "";
    // ///////////////////////
    private Handler customHandler = new Handler();
    // //////////////////////////------Control

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        setupButtonName();
        super.onResume();
    }

    private void setupButtonName() {
        tvTemp.setText("Phong hai");
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

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            if (MainActivity.BluetoothOn) {
                sendMessage(command);
//                        customHandler.postDelayed(updateTimerThread, 20);
            }

        }
    };

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    public void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (MainActivity.mControlService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            byte[] send = message.getBytes();
            MainActivity.mControlService.write(send);
            MainActivity.mOutStringBuffer.setLength(0);
            Toast.makeText(getActivity(), "Send: " + message,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMessage(char data) {
        // Check that we're actually connected before trying anything
        if (MainActivity.mControlService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // Get the message bytes and tell the BluetoothService to write
        byte[] send = new byte[1];
        send[0] = (byte) data;
        MainActivity.mControlService.write(send);
        MainActivity.mOutStringBuffer.setLength(0);

//         Toast.makeText(this, "Send: " + message,
//         Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                command = "1";
                break;
            case R.id.btn2:
                command = "2";
                break;
            case R.id.btn3:
                command = "3";
                break;
            case R.id.btn4:
                command = "4";
                break;
            case R.id.btn5:
                command = "5";
                break;
            case R.id.btn6:
                command = "6";
                break;
            case R.id.btn7:
                command = "7";
                break;
            case R.id.btn8:
                command = "8";
                break;
            case R.id.btn9:
                command = "9";
                break;
        }
        if (MainActivity.BluetoothOn && MainActivity.mControlService.getState() == BluetoothService.STATE_CONNECTED) {
            updateBtn((Button) view, !getStatus((Button) view));
            customHandler.postDelayed(updateTimerThread, 20);
        }
    }

}
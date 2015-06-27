package cvnhan.android.smscontrol;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import cvnhan.android.smscontrol.bluetooth.BluetoothService;
import cvnhan.android.smscontrol.bluetooth.DeviceListActivity;
import cvnhan.android.smscontrol.fragment.PhongBa;
import cvnhan.android.smscontrol.fragment.PhongHai;
import cvnhan.android.smscontrol.fragment.PhongMot;


public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener {
    // bluetooth
    public static boolean BluetoothOn = true;
    // private static final boolean D = true;

    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    // Name of the connected device
    public static String mConnectedDeviceName = null;
    public static StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    public static BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    public static BluetoothService mControlService = null;

    private int tabPagePos = 0;
    private Context context;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private String[] tabs = {"First room ", "Second room", "Third room"};

    MyReceiver myReceiver;
    Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager()) {
            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                super.setPrimaryItem(container, position, object);
                mCurrentFragment = (Fragment) object;
            }
        };

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
                tabPagePos = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        Button btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.broadcastIntentMyReceive(context, "012345678910", "Ok11");
                Utils.broadcastIntentMyReceive(context, "012345678910", "Ok21");
                Utils.broadcastIntentMyReceive(context, "012345678910", "Ok31");
            }
        });

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
            // finish();
            BluetoothOn = false;
            return;
        }

    }
    // bluetooth
    @Override
    public void onStart() {
        super.onStart();
        if (mBluetoothAdapter != null)
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                // Otherwise, setup the chat session
            } else {
                if (mControlService == null)
                    setupControl();
            }
    }

    private void setupControl() {
        // Log.d(TAG, "setupControl()");
        // Initialize the BluetoothService to perform bluetooth connections
        mControlService = new BluetoothService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    @Override
    protected synchronized void onResume() {
        super.onResume();
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Utils.RECEIVE);
        registerReceiver(myReceiver, filter);
        if (mControlService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't
            // started already
            if (mControlService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mControlService.start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
        ButterKnife.reset(this);
        // Stop the Bluetooth chat services
        if (mControlService != null)
            mControlService.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View promptView = layoutInflater.inflate(R.layout.dialog_edittext, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("Set phone number");
            alertDialogBuilder.setView(promptView);
            final EditText input = (EditText) promptView.findViewById(R.id.etInput);
            input.setHint(Utils.getNumber(context));
            // setup a dialog window
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // get user input and set it to result
                            Utils.setNumber(context, input.getText().toString());
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            alertDialogBuilder.show();
            return true;
        }else if (id == R.id.action_connect) {
            // connect bluetooth
            Intent serverIntent = null;
            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }


    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String number = intent.getStringExtra(Utils.NUMBER);
            String message = intent.getStringExtra(Utils.MESSAGE);

            try {
                if (message.contains("Ok")) {
                    int button = Integer.valueOf(message.substring(2, 3));
                    int status = Integer.valueOf(message.substring(3, 4));
                    if (tabPagePos == 0) {
                        ((PhongMot) mCurrentFragment).updateBtn(button, status);
                    } else if (tabPagePos == 1) {
                        ((PhongHai) mCurrentFragment).updateBtn(button, status);
                    } else if (tabPagePos == 2) {
                        ((PhongBa) mCurrentFragment).updateBtn(button, status);
                    }
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.toString());
            }
            Toast.makeText(context, "Intent Detected." + number + " - " + message, Toast.LENGTH_SHORT).show();
        }
    }



    @SuppressLint("NewApi")
    private final void setStatus(int resId) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(resId);
    }

    @SuppressLint("NewApi")
    private final void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(subTitle);
    }

    // The Handler that gets information back from the BluetoothService
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    // if (D)
                    // Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to,
                                    mConnectedDeviceName));

                            // mConversationArrayAdapter.clear();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    @SuppressWarnings("unused")
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer

                     String writeMessage = new String(writeBuf);
                     Toast.makeText(getApplicationContext(),
                     "Send:  " + writeMessage,
                     Toast.LENGTH_SHORT).show();

                    break;
                case MESSAGE_READ:
                    //byte[] readBuf = (byte[]) msg.obj;
                    //String readMessage = new String(readBuf, 0, msg.arg1);
                    // txtStatus.setText(mConnectedDeviceName + ": " + readMessage);
                    //150[pwm][temp][humi]0000000000

                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                     Toast.makeText(getApplicationContext(),
                     "Connected to " + mConnectedDeviceName,
                     Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    String toast = msg.getData().getString(TOAST);
                    Toast.makeText(getApplicationContext(), toast,
                            Toast.LENGTH_SHORT).show();
                    if (toast.equals("Unable to connect device")
                            || toast.equals("Device connection was lost")) {
                    }
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if (D)
        // Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupControl();
                    BluetoothOn = true;
                } else {
                    // User did not enable Bluetooth or an error occurred
                    // Log.d(TAG, "BT not enabled");
                    BluetoothOn = false;
                     Toast.makeText(this, R.string.bt_not_enabled_leaving,
                     Toast.LENGTH_SHORT).show();

                    // finish();
                }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras().getString(
                DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mControlService.connect(device, secure);
    }
}

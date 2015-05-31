package cvnhan.android.smscontrol.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cvnhan.android.smscontrol.R;
import cvnhan.android.smscontrol.Utils;

/**
 * Created by NhanCao on 31-May-15.
 */
public abstract class RoomBase extends Fragment implements View.OnClickListener {
    public static final String TAG = RoomBase.class.getSimpleName();
    private Context context;

    @InjectView(R.id.tvTemp)
    TextView tvTemp;
    @InjectView(R.id.tvHumi)
    TextView tvHumi;

    @InjectView(R.id.btn1)
    public Button btn1;
    @InjectView(R.id.btn2)
    public Button btn2;
    @InjectView(R.id.btn3)
    public Button btn3;
    @InjectView(R.id.btn4)
    public Button btn4;
    @InjectView(R.id.btn5)
    public Button btn5;
    @InjectView(R.id.btn6)
    public Button btn6;
    @InjectView(R.id.btn7)
    public Button btn7;
    @InjectView(R.id.btn8)
    public Button btn8;
    @InjectView(R.id.btn9)
    public Button btn9;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View rootView = inflater.inflate(R.layout.fragment_room, container, false);
        ButterKnife.inject(this, rootView);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                btn1.setTextColor(Color.GREEN);
                Utils.sendSMSMessage(context, Utils.getNumber(context), "btn1");
                break;
            case R.id.btn2:
                Utils.sendSMSMessage(context, Utils.getNumber(context), "btn2");
                break;
            case R.id.btn3:
                Utils.sendSMSMessage(context, Utils.getNumber(context), "btn3");
                break;
            case R.id.btn4:
                Utils.sendSMSMessage(context, Utils.getNumber(context), "btn4");
                break;
            case R.id.btn5:
                Utils.sendSMSMessage(context, Utils.getNumber(context), "btn5");
                break;
            case R.id.btn6:
                Utils.sendSMSMessage(context, Utils.getNumber(context), "btn6");
                break;
            case R.id.btn7:
                Utils.sendSMSMessage(context, Utils.getNumber(context), "btn7");
                break;
            case R.id.btn8:
                Utils.sendSMSMessage(context, Utils.getNumber(context), "btn8");
                break;
            case R.id.btn9:
                Utils.sendSMSMessage(context, Utils.getNumber(context), "btn9");
                break;
        }
    }

    public void updateBtn(int indexBtn, int status) {
        switch (indexBtn) {
            case 1:
                updateBtn(btn1, status);
                break;
            case 2:
                updateBtn(btn2, status);
                break;
            case 3:
                updateBtn(btn3, status);
                break;
            case 4:
                updateBtn(btn4, status);
                break;
            case 5:
                updateBtn(btn5, status);
                break;
            case 6:
                updateBtn(btn6, status);
                break;
            case 7:
                updateBtn(btn7, status);
                break;
            case 8:
                updateBtn(btn8, status);
                break;
            case 9:
                updateBtn(btn9, status);
                break;
        }
    }

    public void updateBtn(Button btn, int status) {
        if (status == 1) {
            btn.setTextColor(Color.GREEN);
        } else {
            btn.setTextColor(Color.WHITE);
        }
    }
}
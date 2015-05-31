package cvnhan.android.smscontrol;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cvnhan.android.smscontrol.fragment.PhongBa;
import cvnhan.android.smscontrol.fragment.PhongHai;
import cvnhan.android.smscontrol.fragment.PhongMot;

/**
 * Created by NhanCao on 30-May-15.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new PhongMot();
            case 1:
                return new PhongHai();
            case 2:
                return new PhongBa();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
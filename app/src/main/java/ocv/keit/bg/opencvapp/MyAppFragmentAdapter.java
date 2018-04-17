package ocv.keit.bg.opencvapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyAppFragmentAdapter extends FragmentPagerAdapter {
    public MyAppFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if (position==0)
        {
            fragment = new OpencvCameraFragmentActivity();
            Bundle args = new Bundle();
            //args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            //return fragment;
        }
        if (position==1)
        {
            fragment = new ListFacesFragmentActivity();
            Bundle args = new Bundle();
            //args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            //return fragment;
        }
        return fragment;

    }

    @Override
    public int getCount() {
        return 2;
    }
}

package mobitechs.cityriders.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import mobitechs.cityriders.Tab_Video_List;
import mobitechs.cityriders.Upcoming_Riding_List;

public class Home_Pager_Adapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public Home_Pager_Adapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Tab_Video_List tab1 = new Tab_Video_List();
                return tab1;
            case 1:
                Upcoming_Riding_List tab2 = new Upcoming_Riding_List();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
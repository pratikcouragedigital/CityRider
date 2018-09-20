package mobitechs.cityriders;


import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.UpdateClickListener;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

import org.json.JSONArray;
import org.json.JSONObject;


import java.net.MalformedURLException;
import java.net.URL;

import mobitechs.cityriders.adapter.Home_Pager_Adapter;

public class Home extends AppCompatActivity {

    TabLayout tabLayout;
//
private String packageName;
    private URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        packageName = getPackageName();

        try {
            url = new URL("https://play.google.com/store/apps/details?id=" + packageName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        AppUpdater appUpdater = new AppUpdater(this)
                .setDisplay(com.github.javiersantos.appupdater.enums.Display.DIALOG)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .setTitleOnUpdateAvailable("Update available")
                .setContentOnUpdateAvailable("Check out the latest version available of my app!")
                .setTitleOnUpdateNotAvailable("Update not available")
                .setContentOnUpdateNotAvailable("No update available. Check for updates again later!")
                .setButtonUpdate("Update now?")
                .setButtonUpdateClickListener(new UpdateClickListener(this, UpdateFrom.GOOGLE_PLAY, url))
                .setButtonDismiss(null)
                .setButtonDoNotShowAgain(null)
                .setCancelable(false);
        appUpdater.start();

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Videos"));
        tabLayout.addTab(tabLayout.newTab().setText("Rides"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        Home_Pager_Adapter adapter = new Home_Pager_Adapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.riderList:
                Intent riderList = new Intent(this, Rider_List.class);
                startActivity(riderList);
                return true;
            case R.id.addAdminCode:
                Intent intent2 = new Intent(this, IsAdmin.class);
                startActivity(intent2);
                return true;
            case R.id.joinCityRider:
                Intent intent3 = new Intent(this, Join_CityRiders.class);
                startActivity(intent3);
                return true;
//            case R.id.subscribe:
//                SubscriptionRequest();
//                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void SubscriptionRequest() {
        String url = "https://www.googleapis.com/youtube/v3/subscriptions?part=snippet&key=AIzaSyBnIAqzNEjw_ZJjqsRenVctNQMrIwHtAJw";

        AndroidNetworking.post(url)
                .setTag("subscription")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arr = response.getJSONArray("error");

                            if (arr.length() == 0) {
                                Toast.makeText(Home.this, "List Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                }
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                    }
                });
    }
}

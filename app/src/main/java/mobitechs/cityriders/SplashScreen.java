package mobitechs.cityriders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import mobitechs.cityriders.SessionManger.SessionManager;

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    ImageView image1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        //image1 = (ImageView)findViewById(R.id.logoHeader);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                SessionManager sessionManager = new SessionManager(SplashScreen.this);
                sessionManager.IsAdminCheck();

//                Intent gotoMainPage = new Intent(SplashScreen.this,Home.class);
//                startActivity(gotoMainPage);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}
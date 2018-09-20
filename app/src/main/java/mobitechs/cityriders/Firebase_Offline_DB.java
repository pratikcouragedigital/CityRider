package mobitechs.cityriders;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Firebase_Offline_DB extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

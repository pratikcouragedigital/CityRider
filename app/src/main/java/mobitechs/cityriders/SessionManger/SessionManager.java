package mobitechs.cityriders.SessionManger;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import mobitechs.cityriders.Home;
import mobitechs.cityriders.IsAdmin;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;      // Shared pref mode
    SessionManager sessionManager;

    // Sharedpref file name
    private static final String PREF_NAME = "Preference";

    // All Shared Preferences Keys
    private static final String IS_ADMIN = "IsAdmin";

    // Email address (make variable public to access from outside)
    public static final String KEY_ADMINCODE = "adminCode";
    public static final String KEY_IS_ADMIN = "isAdmin";


    public SessionManager(Context c) {
        this.context = c;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    
    public void setActivationCode(String isAdmin, String adminCode) {

        if(isAdmin.equals("true")){
            editor.putBoolean(IS_ADMIN, true);
            editor.commit();
        }else{
            editor.putBoolean(IS_ADMIN, false);
            editor.commit();
        }


        editor.putString(KEY_IS_ADMIN, isAdmin);
        editor.putString(KEY_ADMINCODE, adminCode);
        editor.commit();
    }

    public HashMap<String, String> getActivationCode() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ADMINCODE, pref.getString(KEY_ADMINCODE, null));
        user.put(KEY_IS_ADMIN, pref.getString(KEY_IS_ADMIN, null));
        return user;
    }

    public boolean ISAdmin() {
        return pref.getBoolean(IS_ADMIN, false);
    }

    public void IsAdminCheck() {

        // Check login status
        if (!this.ISAdmin()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, IsAdmin.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);
        } else {
            Intent i = new Intent(context, Home.class);
            context.startActivity(i);
        }
    }
}

package mobitechs.cityriders.firebase;

import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import mobitechs.cityriders.model.Device_Token_Items;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String REG_TOKEN ="REG_TOKEN";

    String deviceId;
    String token;
    private FirebaseDatabase database;
    private DatabaseReference deviceIdReference;
    Device_Token_Items device_token_items;

    @Override
    public void onTokenRefresh() {
        token = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN,"Your Token No:-"+ token);

        createDeviceId();
    }

    public void createDeviceId() {
        deviceId = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

        database = FirebaseDatabase.getInstance();
        deviceIdReference = database.getReference("device_token").child(deviceId);

        device_token_items = new Device_Token_Items(deviceId, token);
        deviceIdReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    deviceIdReference.setValue(device_token_items);
                }
                else {
                    deviceIdReference.push();
                    deviceIdReference.setValue(device_token_items);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MyFirebaseInstanceIdService.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

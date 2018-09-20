package mobitechs.cityriders;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import mobitechs.cityriders.image_picker.Image;
import mobitechs.cityriders.model.Rider_List_Items;
import mobitechs.cityriders.model.Riding_Event_Items;

public class Join_CityRiders extends AppCompatActivity implements View.OnClickListener, Image.OnRecyclerSetImageListener {

    EditText txtRiderName, txtContactNo,txtRiderBikeName,txtRiderCity;
    LinearLayout imageViewLinearLayout;
    ImageView firstImage;
    Button btnSubmit;
    String isApprove = "false";
     Button btnAddImage;

    String riderName, riderContactNo, riderBikeName,riderCity;
    byte[] firstImagePath;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private DatabaseReference deviceIdReference;
    Image image;

    CoordinatorLayout coordinateLayout;
    Snackbar snackbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_cityriders);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("rider_list");

        txtRiderName = (EditText) findViewById(R.id.txtRiderName);
        txtContactNo = (EditText) findViewById(R.id.txtContactNo);
        txtRiderBikeName = (EditText) findViewById(R.id.txtRiderBikeName);
        txtRiderCity = (EditText) findViewById(R.id.txtRiderCity);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
//        btnAddImage = (Button) findViewById(R.id.btnAddImage);
        firstImage = (ImageView) findViewById(R.id.riderImage);

        coordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);
        imageViewLinearLayout = (LinearLayout) findViewById(R.id.imageViewLinearLayout);
        imageViewLinearLayout.setVisibility(View.GONE);
        btnSubmit.setOnClickListener(this);
//        btnAddImage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            riderName = txtRiderName.getText().toString();
            riderContactNo = txtContactNo.getText().toString();
            riderBikeName = txtRiderBikeName.getText().toString();
            riderCity = txtRiderCity.getText().toString();

            if (riderName.equals("")) {
                snackbar.make(coordinateLayout, "Please Enter Riding Name.", Snackbar.LENGTH_LONG).show();
//                Toast.makeText(this, "Please Enter Rider Name.", Toast.LENGTH_SHORT).show();
            }
            else if (riderContactNo.equals("")) {
                snackbar.make(coordinateLayout, "Please Enter Contact No.", Snackbar.LENGTH_LONG).show();
//                Toast.makeText(this, "Please Enter Contact No.", Toast.LENGTH_SHORT).show();
            }
            else if (txtRiderCity.equals("")) {
                snackbar.make(coordinateLayout, "Please Enter City.", Snackbar.LENGTH_LONG).show();
//                Toast.makeText(this, "Please Enter City.", Toast.LENGTH_SHORT).show();
            }

            else {

                //Rider_List_Items ridingEvent_items = new Rider_List_Items(riderName, riderContactNo, riderBikeName,firstImagePath);
//                Rider_List_Items ridingEvent_items = new Rider_List_Items(riderName, riderContactNo, riderBikeName,riderCity,isApprove);
//
//                DatabaseReference childRef = myRef.push();
//                childRef.setValue(ridingEvent_items);
//
//                Intent intent = new Intent(this, Rider_List.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);


                Rider_List_Items ridingEvent_items = new Rider_List_Items(riderName, riderContactNo, riderBikeName,riderCity,isApprove);
                DatabaseReference childRef = myRef.push();
                childRef.setValue(ridingEvent_items);

                deviceIdReference = FirebaseDatabase.getInstance().getReference("device_token");
                deviceIdReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
//                            Toast.makeText(Create_Next_Ride.this, childDataSnapshot.child("token").getValue().toString(), Toast.LENGTH_SHORT).show();

                            JSONObject jsonMainObject = new JSONObject();
                            JSONObject jsonNotificationObject = new JSONObject();
                            try {
                                jsonNotificationObject.put("body", riderName + " from " + riderCity);
                                jsonNotificationObject.put("title", "New Member Joined");
                                jsonMainObject.put("to", childDataSnapshot.child("token").getValue().toString());
                                jsonMainObject.put("notification", jsonNotificationObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            AndroidNetworking.post("https://fcm.googleapis.com/fcm/send")
                                    .addJSONObjectBody(jsonMainObject) // posting json
                                    .setTag("test")
                                    .addHeaders("Authorization", "key=AAAAyXf7I8U:APA91bH0AIqeoTsBLza9VYRtoW0salqNPP2R5GPUsVAsOU2WVNwRO2daoEDpnsjeoKANWFz467e7kxHbUSCAiJzx4Y8hnpY0OPgMy-jSt3Wn8mYqVqtWKGSv1aTtLam_OFIQsLiOQG2i")
                                    .setPriority(Priority.IMMEDIATE)
                                    .build()
                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {
//                                            Toast.makeText(Create_Next_Ride.this, response.toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            Toast.makeText(Join_CityRiders.this, anError.getErrorBody(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                     public void onCancelled(DatabaseError databaseError) {

                    }
                });

                this.finish();
            }
        }
//            else if (v.getId() == R.id.btnAddImage) {
//                image = new Image(this, "JoinRider",this);
//                image.getImage();
//            }
    }

    @Override
    @TargetApi(23)
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        image.getActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        image.getRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onRecyclerImageSet(Bitmap imageToShow, byte[] imageBase64String) {
        imageViewLinearLayout.setVisibility(View.VISIBLE);
        firstImage.setImageBitmap(imageToShow);
        this.firstImagePath = imageBase64String;
    }
}

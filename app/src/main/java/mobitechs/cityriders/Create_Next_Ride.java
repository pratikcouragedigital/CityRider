package mobitechs.cityriders;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import mobitechs.cityriders.model.Riding_Event_Items;

public class Create_Next_Ride extends AppCompatActivity implements View.OnClickListener, PlaceSelectionListener {


    EditText txtRidingDate, txtNote;
    EditText txtRidingPlace,txtMeetUpPlace;

    String RidingPlace, MeetUpPlace, RidingDate, Note;
    Button btnSubmit;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    double ridingLat, ridingLongi, meetUpLat, meetUpLongi;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private StringBuilder date;

    private static final String LOG_TAG = "PlaceSelectionListener";
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final int REQUEST_RIDING_PLACE = 1000;
    private static final int REQUEST_MEETUP_PLACE = 2000;

    CoordinatorLayout coordinateLayout;
    Snackbar snackbar;
	
	String place;
    private DatabaseReference deviceIdReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_next_ride);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("riding_event");

        txtRidingPlace = (EditText) findViewById(R.id.txtRidingPlace);
        txtMeetUpPlace = (EditText) findViewById(R.id.txtMeetUpPlace);
        txtRidingDate = (EditText) findViewById(R.id.txtRidingDate);
        txtNote = (EditText) findViewById(R.id.txtNote);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        coordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);

        btnSubmit.setOnClickListener(this);
        txtRidingPlace.setOnClickListener(this);
        txtMeetUpPlace.setOnClickListener(this);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        txtRidingDate.setInputType(InputType.TYPE_NULL);

        txtRidingDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setDate(view);
                return false;
            }
        });
		
		   txtRidingPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place = "RideingPlace";
                //setTime(place);
				ridingPlaceSearch();
            }
        });
		
		   txtMeetUpPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place = "MeetupPlace";
                //setTime(place);
				meeupPlaceSearch();
            }
        });

    }

    private void meeupPlaceSearch() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder
                    (PlaceAutocomplete.MODE_OVERLAY)
                    .setBoundsBias(BOUNDS_MOUNTAIN_VIEW)
                    .build(this);
            startActivityForResult(intent, REQUEST_MEETUP_PLACE);

        }
        catch (GooglePlayServicesRepairableException |
                GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void ridingPlaceSearch() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder
                    (PlaceAutocomplete.MODE_OVERLAY)
                    .setBoundsBias(BOUNDS_MOUNTAIN_VIEW)
                    .build(this);
            startActivityForResult(intent, REQUEST_RIDING_PLACE);
        }
        catch (GooglePlayServicesRepairableException |
                GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")

    public void setDate(View view) {
        showDialog(999);
//        Toast.makeText(getApplicationContext(), "Select Date", Toast.LENGTH_SHORT)
//                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {

        txtRidingDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        date = new StringBuilder().append(year).append("-").append(month).append("-").append(day);
        RidingDate = date.toString();
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSubmit){
            RidingPlace = txtRidingPlace.getText().toString();
            MeetUpPlace = txtMeetUpPlace.getText().toString();
            RidingDate = txtRidingDate.getText().toString();
            Note = txtNote.getText().toString();

            Toast.makeText(this, RidingPlace+" and "+MeetUpPlace, Toast.LENGTH_SHORT).show();
            if(RidingPlace.equals("") || RidingDate == null){
                snackbar.make(coordinateLayout, "Please Enter Riding Place.", Snackbar.LENGTH_LONG).show();
//                Toast.makeText(this, "Please Enter Riding Place.", Toast.LENGTH_SHORT).show();
            }
            else if(MeetUpPlace.equals("") || RidingDate == null){
                snackbar.make(coordinateLayout, "Please Enter Meet Up Place.", Snackbar.LENGTH_LONG).show();
//                Toast.makeText(this, "Please Enter Meet Up Place", Toast.LENGTH_SHORT).show();
            }
            else if(RidingDate.equals("") || RidingDate == null){
                snackbar.make(coordinateLayout, "Please Enter Riding Date.", Snackbar.LENGTH_LONG).show();
//                Toast.makeText(this, "Please Enter Riding Date", Toast.LENGTH_SHORT).show();
            }
            else{
                // create confirm alert
                // if yes then directly send notification to users

                Riding_Event_Items ridingEvent_items = new Riding_Event_Items(RidingPlace, MeetUpPlace, RidingDate, Note,ridingLat,ridingLongi,meetUpLat,meetUpLongi);

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
                                jsonNotificationObject.put("body", RidingPlace + " on " + RidingDate);
                                jsonNotificationObject.put("title", "Next Bike Ride");
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
                                            Toast.makeText(Create_Next_Ride.this, anError.getErrorBody(), Toast.LENGTH_SHORT).show();
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
//        else if(v.getId() == R.id.txtRidingPlace){
//            ridingPlaceSearch();
//        }
//        else if(v.getId() == R.id.txtMeetUpPlace){
//            meeupPlaceSearch();
//        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RIDING_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                this.onPlaceSelected(place);
                txtRidingPlace.setText(place.getName());
                ridingLat = place.getLatLng().latitude;
                ridingLongi = place.getLatLng().longitude;
                txtRidingPlace.setFocusable(false);
                txtMeetUpPlace.setFocusable(true);
            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                this.onError(status);
            }
        }
        else if (requestCode == REQUEST_MEETUP_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                this.onPlaceSelected(place);
                txtMeetUpPlace.setText(place.getName());
                meetUpLat = place.getLatLng().latitude;
                meetUpLongi = place.getLatLng().longitude;

            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                this.onError(status);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onPlaceSelected(Place place) {
        Log.i(LOG_TAG, "Place Selected: " + place.getName());
//        Toast.makeText(this,"Place  " + place.getName() + " Lat "+place.getLatLng().latitude +"Logi "+ place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Status status) {
        Log.e(LOG_TAG, "onError: Status = " + status.toString());
//        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
//                Toast.LENGTH_SHORT).show();
        snackbar.make(coordinateLayout, "Place selection failed: " + status.getStatusMessage(), Snackbar.LENGTH_LONG).show();
    }
}
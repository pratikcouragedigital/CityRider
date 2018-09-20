package mobitechs.cityriders;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import mobitechs.cityriders.SessionManger.SessionManager;
import mobitechs.cityriders.image_picker.Image;
import mobitechs.cityriders.model.Rider_List_Items;
import mobitechs.cityriders.model.Riding_Event_Items;

public class Add_Rider extends AppCompatActivity implements View.OnClickListener, Image.OnRecyclerSetImageListener {

    EditText txtRiderName, txtContactNo, txtRiderBikeName, txtRiderCity;
    LinearLayout imageViewLinearLayout;
    ImageView firstImage;
    Button btnSubmit;
    String isApprove = "True";
    Button btnAddImage;
    String adminCode;
    String isAdmin;
    SessionManager sessionManager;

    String riderName, riderContactNo, riderBikeName, riderCity;
    byte[] firstImagePath;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    Image image;
    CoordinatorLayout coordinateLayout;
    Snackbar snackbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_rider);

        sessionManager = new SessionManager(this);
        HashMap<String, String> typeOfUser = sessionManager.getActivationCode();
        adminCode = typeOfUser.get(SessionManager.KEY_ADMINCODE);
        isAdmin = typeOfUser.get(SessionManager.KEY_IS_ADMIN);

        if (isAdmin.equals("true")) {
            isApprove = "True";
        }
        else {
            isApprove = "False";
        }

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
//                    Toast.makeText(this, "Please Enter Rider Name.", Toast.LENGTH_SHORT).show();
            } else if (riderContactNo.equals("")) {
                snackbar.make(coordinateLayout, "Please Enter Contact No.", Snackbar.LENGTH_LONG).show();
//                    Toast.makeText(this, "Please Enter Contact No.", Toast.LENGTH_SHORT).show();
            } else if (txtRiderCity.equals("")) {
                snackbar.make(coordinateLayout, "Please Enter City.", Snackbar.LENGTH_LONG).show();
//                    Toast.makeText(this, "Please Enter City.", Toast.LENGTH_SHORT).show();
            } else {


//                    Rider_List_Items ridingEvent_items = new Rider_List_Items(riderName, riderContactNo, riderBikeName,riderCity,isApprove);
                Rider_List_Items ridingEvent_items = new Rider_List_Items(riderName, riderContactNo, riderBikeName, riderCity, isApprove);

                DatabaseReference childRef = myRef.push();
                childRef.setValue(ridingEvent_items);

                Intent intent = new Intent(this, Rider_List.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
//        else if (v.getId() == R.id.btnAddImage) {
//            image = new Image(this, "AddRider", this);
//            image.getImage();
//        }
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

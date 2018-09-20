package mobitechs.cityriders;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import mobitechs.cityriders.SessionManger.SessionManager;

public class IsAdmin extends AppCompatActivity implements View.OnClickListener{

    Button btnAllVideos;
    RelativeLayout txtAdminCodeLayout;
    RelativeLayout isAdminLayout;

    RadioButton rdoIsAdminYes;
    RadioButton rdoIsAdminNo;
    String isAdmin ="false";
    Button btnFilterOk;
    EditText txtAdminCode;
    String adminCode="City@17";
    SessionManager sessionManager;
    View dialogView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.is_admin);

        sessionManager = new SessionManager(IsAdmin.this);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.admin_code, null);
        getLayoutDetails();
        alert.setView(dialogView);
        alert.show();

//
//        btnFilterOk = (Button) findViewById(R.id.btnFilterOk);
//        txtAdminCode = (EditText) findViewById(R.id.txtAdminCode);
//
//        txtAdminCodeLayout = (RelativeLayout) findViewById(R.id.txtAdminCodeLayout);
//        isAdminLayout = (RelativeLayout) findViewById(R.id.isAdminLayout);
//
//        rdoIsAdminYes = (RadioButton) findViewById(R.id.rdoIsAdminYes);
//        rdoIsAdminNo = (RadioButton) findViewById(R.id.rdoIsAdminNo);
//        btnFilterOk.setOnClickListener(this);
//
//
//        rdoIsAdminYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (rdoIsAdminYes.isChecked()) {
//                    isAdmin = "true";
//                    rdoIsAdminNo.setChecked(false);
//                    txtAdminCodeLayout.setVisibility(View.VISIBLE);
//
//
//                }
//            }
//        });
//        rdoIsAdminNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (rdoIsAdminNo.isChecked()) {
//                    isAdmin = "false";
//                    rdoIsAdminYes.setChecked(false);
//                    txtAdminCodeLayout.setVisibility(View.GONE);
//
//                }
//            }
//        });
    }

    private void getLayoutDetails() {

        btnFilterOk = (Button) dialogView.findViewById(R.id.btnFilterOk);
        txtAdminCode = (EditText) dialogView.findViewById(R.id.txtAdminCode);

        txtAdminCodeLayout = (RelativeLayout) dialogView.findViewById(R.id.txtAdminCodeLayout);
        isAdminLayout = (RelativeLayout) dialogView.findViewById(R.id.isAdminLayout);

        rdoIsAdminYes = (RadioButton) dialogView.findViewById(R.id.rdoIsAdminYes);
        rdoIsAdminNo = (RadioButton) dialogView.findViewById(R.id.rdoIsAdminNo);
        btnFilterOk.setOnClickListener(this);


        rdoIsAdminYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rdoIsAdminYes.isChecked()) {
                    isAdmin = "true";
                    rdoIsAdminNo.setChecked(false);
                    txtAdminCodeLayout.setVisibility(View.VISIBLE);


                }
            }
        });
        rdoIsAdminNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rdoIsAdminNo.isChecked()) {
                    isAdmin = "false";
                    rdoIsAdminYes.setChecked(false);
                    txtAdminCodeLayout.setVisibility(View.GONE);

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnFilterOk){

            String code =  txtAdminCode.getText().toString();
            if(rdoIsAdminYes.isChecked() == true){
                if(code.equals(adminCode)){
                    sessionManager.setActivationCode(isAdmin, code);
                    Intent gotoMainPage = new Intent(this,Home.class);
                    startActivity(gotoMainPage);
                }
                else if(code.equals("") || code == null){
                    Toast.makeText(this, "Please enter admin code.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Please enter valid admin code.", Toast.LENGTH_SHORT).show();
                }
            }

            else{
                code = "";
                sessionManager.setActivationCode(isAdmin, code);
                Intent gotoMainPage = new Intent(this,Home.class);
                gotoMainPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gotoMainPage);
            }
        }
    }
}
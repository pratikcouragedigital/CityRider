package mobitechs.cityriders;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import mobitechs.cityriders.SessionManger.SessionManager;
import mobitechs.cityriders.adapter.UpcomingRide_Adapter;
import mobitechs.cityriders.model.Riding_Event_Items;

//public class Upcoming_Riding_List extends AppCompatActivity implements View.OnClickListener {
public class Upcoming_Riding_List extends Fragment implements View.OnClickListener {
    private ListView listView;
    private ArrayAdapter<Riding_Event_Items> adapter;
    private FirebaseDatabase database;
    private DatabaseReference myDbRef;

    RelativeLayout txtAdminCodeLayout;
    RelativeLayout isAdminLayout;

    RadioButton rdoIsAdminYes;
    RadioButton rdoIsAdminNo;
    String isAdmin;


    public List<Riding_Event_Items> listItems = new ArrayList<Riding_Event_Items>();
    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
//    private ProgressDialog progressDialog = null;
    FloatingActionButton createRidingEventFab;
    Button btnFilterOk;
    EditText txtAdminCode;
    String adminCode;
    View dialogView;

    View v;
    SessionManager sessionManager;

    boolean isMultiSelect = false;
    //ActionMode mActionMode;
    Menu context_menu;
    ArrayList<Riding_Event_Items> multiselect_list = new ArrayList<>();



//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.upcoming_riding_list);
//
//        createRidingEventFab = (FloatingActionButton) findViewById(R.id.createRidingEventFab);
//        createRidingEventFab.setOnClickListener(this);
//
//        getList();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.upcoming_riding_list, container, false);
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sessionManager = new SessionManager(getContext());
        HashMap<String, String> typeOfUser = sessionManager.getActivationCode();
        adminCode = typeOfUser.get(SessionManager.KEY_ADMINCODE);
        isAdmin = typeOfUser.get(SessionManager.KEY_IS_ADMIN);

        createRidingEventFab = (FloatingActionButton) v.findViewById(R.id.createRidingEventFab);

        if(isAdmin.equals("true")){
            createRidingEventFab.setOnClickListener(this);
            createRidingEventFab.setVisibility(View.VISIBLE);
        }else{
            createRidingEventFab.setVisibility(View.GONE);
        }

        getList();
    }

    private void getList() {

        database = FirebaseDatabase.getInstance();
        myDbRef = database.getReference("riding_event");
        myDbRef.keepSynced(true);

        recyclerView = (RecyclerView) v.findViewById(R.id.eventsRecyclerView);
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new UpcomingRide_Adapter(listItems);
        recyclerView.setAdapter(reviewAdapter);

//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("Please Wait");
//        progressDialog.show();

        myDbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Riding_Event_Items ridingEvent_items = dataSnapshot.getValue(Riding_Event_Items.class);
//                progressDialog.dismiss();
                listItems.add(ridingEvent_items);
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Riding_Event_Items ridingEvent_items = dataSnapshot.getValue(Riding_Event_Items.class);
                listItems.add(ridingEvent_items);
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Riding_Event_Items ridingEvent_items = dataSnapshot.getValue(Riding_Event_Items.class);
                listItems.add(ridingEvent_items);
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onClick(View v) {
       if(v.getId() == R.id.createRidingEventFab){
           Intent gotoMainPage = new Intent(getContext(),Create_Next_Ride.class);
           startActivity(gotoMainPage);
       }

    }


}

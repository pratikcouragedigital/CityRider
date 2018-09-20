package mobitechs.cityriders.adapter;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import mobitechs.cityriders.R;
import mobitechs.cityriders.Rider_List;
import mobitechs.cityriders.SessionManger.SessionManager;
import mobitechs.cityriders.model.Rider_List_Items;

public class Rider_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Rider_List_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;

    private FirebaseDatabase database;
    private DatabaseReference myDbRef;

    SessionManager sessionManager;

    public Rider_List_Adapter(List<Rider_List_Items> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rider_list_items, viewGroup, false);
        viewHolder = new Rider_List_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Rider_List_Adapter.ViewHolder) {
            Rider_List_Adapter.ViewHolder vHolder = (Rider_List_Adapter.ViewHolder) viewHolder;
            Rider_List_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {

        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {


        public RelativeLayout deleteRiderLayout;
        public RelativeLayout isApprovedLayout;
        public TextView riderName;
        public TextView riderContactNo;
        public TextView riderBikeName;
        public TextView txtNotApproved;
        public View cardView;

        String adminCode,isAdmin;
        String isApproove;

        Rider_List_Items listItem = new Rider_List_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            sessionManager = new SessionManager(v.getContext());
            HashMap<String, String> typeOfUser = sessionManager.getActivationCode();
            adminCode = typeOfUser.get(SessionManager.KEY_ADMINCODE);
            isAdmin = typeOfUser.get(SessionManager.KEY_IS_ADMIN);

            riderName = (TextView) itemView.findViewById(R.id.riderName);
            riderContactNo = (TextView) itemView.findViewById(R.id.riderContactNo);
            riderBikeName = (TextView) itemView.findViewById(R.id.riderBikeName);
            txtNotApproved = (TextView) itemView.findViewById(R.id.txtNotApproved);
            deleteRiderLayout = (RelativeLayout) itemView.findViewById(R.id.deleteRiderLayout);
            isApprovedLayout = (RelativeLayout) itemView.findViewById(R.id.isApprovedLayout);

            txtNotApproved.setVisibility(View.GONE);
            if(isAdmin.equals("true")){
                deleteRiderLayout.setOnClickListener(this);
                isApprovedLayout.setOnClickListener(this);
            }
            else{
                deleteRiderLayout.setVisibility(View.GONE);
                isApprovedLayout.setVisibility(View.GONE);
            }

            database = FirebaseDatabase.getInstance();
            myDbRef = database.getReference("rider_list");
        }

        public void bindListDetails(Rider_List_Items listItem) {
            this.listItem = listItem;

            String bike = listItem.RiderBikeName;
            isApproove = listItem.IsApproved;

            riderName.setText(listItem.RiderName);
            riderContactNo.setText(listItem.RiderContact);

            if(bike.equals("")){
                riderBikeName.setVisibility(View.GONE);
            }
            else{
                riderBikeName.setVisibility(View.VISIBLE);
                riderBikeName.setText(listItem.RiderBikeName);
            }

            if(isAdmin.equals("true")){
                if(isApproove.equals("True")){
                    deleteRiderLayout.setVisibility(View.VISIBLE);
                    isApprovedLayout.setVisibility(View.GONE);
                }
                else{
                    deleteRiderLayout.setVisibility(View.GONE);
                    isApprovedLayout.setVisibility(View.VISIBLE);
                    txtNotApproved.setVisibility(View.VISIBLE);
                }
            }
            else{
                if(isApproove.equals("True")){
                    isApprovedLayout.setVisibility(View.GONE);
                    deleteRiderLayout.setVisibility(View.GONE);
                    txtNotApproved.setVisibility(View.GONE);
                }
                else{
                    deleteRiderLayout.setVisibility(View.GONE);
                    isApprovedLayout.setVisibility(View.VISIBLE);
                    txtNotApproved.setVisibility(View.VISIBLE);
                    isApprovedLayout.setBackground(v.getResources().getDrawable(R.drawable.ic_verified_user_grey_700_24dp));
                }
            }
        }

        public void deleteFromList(int position) {
            listItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listItems.size());
            notifyDataSetChanged();
        }

        @Override
        public void onClick(final View v) {
            if (v.getId() == R.id.deleteRiderLayout) {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setMessage("Do you want to delete this Rider?");
                alertbox.setTitle("Delete");

                alertbox.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listItem = listItems.get(getAdapterPosition());
                                String contactNo = listItem.RiderContact;
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                Query query = ref.child("rider_list").orderByChild("RiderContact").equalTo(contactNo);

                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                            snapshot.getRef().removeValue();
                                        }
//                                        listItems.remove(getAdapterPosition());
//                                        notifyItemRemoved(getAdapterPosition());
//                                        notifyItemRangeChanged(getAdapterPosition(), listItems.size());

                                        deleteFromList(getPosition());

                                        Intent gotoList = new Intent (v.getContext(), Rider_List.class);
                                        gotoList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        v.getContext().startActivity(gotoList);


                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("", "onCancelled", databaseError.toException());
                                    }
                                });
                            }
                        });
                alertbox.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertbox.show();
            }
            if (v.getId() == R.id.isApprovedLayout) {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setMessage("Do you want to approve this Rider?");
                alertbox.setTitle("Approve Rider");

                alertbox.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listItem = listItems.get(getAdapterPosition());
                                String contactNo = listItem.RiderContact;
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                Query query = ref.child("rider_list").orderByChild("RiderContact").equalTo(contactNo);

                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                            snapshot.getRef().child("IsApproved").setValue("True");
                                        }
                                        Intent gotoList = new Intent (v.getContext(), Rider_List.class);
                                        gotoList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        v.getContext().startActivity(gotoList);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("", "onCancelled", databaseError.toException());
                                    }
                                });
                            }
                        });
                alertbox.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertbox.show();
            }
        }
    }


}
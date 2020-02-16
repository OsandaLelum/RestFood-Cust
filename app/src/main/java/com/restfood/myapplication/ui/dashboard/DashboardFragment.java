package com.restfood.myapplication.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.restfood.myapplication.Auth;
import com.restfood.myapplication.FoodData;
import com.restfood.myapplication.InventryBottomSheetDialog;
import com.restfood.myapplication.OrderData;
import com.restfood.myapplication.R;
import com.restfood.myapplication.ui.home.FoodAvailableAdapter;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DashboardFragment extends Fragment {
    private RecyclerView rView;
    private OrderAdapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;

    private DashboardViewModel dashboardViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<OrderData> orderList = new ArrayList<>();
    ArrayList<String> docIdList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //this is pre process of ui or recyclerview
        rView = root.findViewById(R.id.order_recycler_view);
        rView.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(getActivity());

        //getOrder();

        orderList.add(new OrderData(true, "Pizza", "AA", "gfnufng", "gjfng", "gbuyfg", 522, "gjhbg"));

        getOrder();
        postsetUi();


        return root;
    }


    //this is to set ui after the data base transaction
    private void postsetUi() {
        rAdapter = new OrderAdapter(orderList);
        rView.setLayoutManager(rLayoutManager);
        rView.setAdapter(rAdapter);

        //this part for real time update
        //to handle in recyclerview
        rAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            //click a card
            //to get bottomsheet
            @Override
            public void onItemClick(int position) {
                OrderBottomSheetDialog bottomsheet = new OrderBottomSheetDialog();
                bottomsheet.show(getFragmentManager(),docIdList.get(position) );
            }

            // update the three function
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDone(int position) {
                updateStatus(position, "Done");
                orderList.get(position).setStatus("Done");
                storeHistory(orderList.get(position),position);

                //Toast.makeText(getActivity().getApplicationContext(), "Order Removed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPrepare(int position) {
                updateStatus(position, "Preparing");
                orderList.get(position).setStatus("Preparing");
                updateUI();
            }

            @Override
            public void onReady(int position) {
                updateStatus(position, "Ready");
                orderList.get(position).setStatus("Ready");
                updateUI();
                //set status to database remove from list

            }

            @Override
            public void onSwitchClick(int position, boolean click) {

            }

        });

        Toast.makeText(getContext(), "Updated", Toast.LENGTH_LONG).show();
    }

    //this is to store data to shop path
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void storeHistory(OrderData obj, final int position)
    {
        LocalDate currentdate = LocalDate.now();
        Month currentMonth = currentdate.getMonth();
        int currentYear = currentdate.getYear();



        //this part is to store order
        final String yea=String.valueOf(currentYear);
        final String mon=currentMonth.toString();
        final String day=String.valueOf(currentdate.getDayOfMonth());
        final String dat=yea+"-"+mon+"-"+day;


        //String dat=String.valueOf(year)+"/"+String.valueOf(month);
        //Toast.makeText(getContext(),"DocumentSnapshot added with ID: " + dat, Toast.LENGTH_SHORT).show();


        Task<DocumentReference> documentReferenceTask = db.collection("shop")
                .document(new Auth().getUId())
                .collection("orders")
                .document(new Auth().getUId())
                .collection(dat)
                .add(obj)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        //Toast.makeText(getContext(),"DocumentSnapshot added with ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                        orderList.remove(position);
                        updateUI();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                        //Toast.makeText(getContext(),"Erroe adding doc", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    //refresh the recycler view
    private void updateUI() {
        rAdapter.notifyDataSetChanged();
    }


    //this is to update the status of order
    //pos- position of object in list
    //status- what you update
    private void updateStatus(int pos, final String status) {

        db.collection("orders")
                .document(docIdList.get(pos))
                .update("Status", status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("This done", "DocumentSnapshot successfully updated!");
                        //Toast.makeText(getActivity().getApplicationContext(),foodList.get(position).getFoodName()+":Updated",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("This error", "Error updating document", e);
                        Toast.makeText(getActivity().getApplicationContext(), "Server issue", Toast.LENGTH_LONG).show();
                    }
                });
    }


    //get order document from database
    //&assinging to a array
    private void getOrder() {
        orderList.clear();
        db.collection("orders")
                .whereEqualTo("Shop", new Auth().getUId())
                .whereEqualTo("Status","Pending")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //this is temp list to get from database
                            //orderList.clear();
                            ArrayList<OrderData> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("Doc Id Are:", document.getId());

                                //creating the OrderData object
                                //OrderData taskItem = new OrderData(Boolean.valueOf(document.get("Done").toString()), document.get("Notes").toString(), document.get("OrderId").toString(), document.get("PaymentMode").toString(), document.get("PaymentStatus").toString(), document.get("Status").toString(), Integer.valueOf(document.get("Total").toString()), document.get("User").toString());
                                try
                                {
                                    OrderData taskItem=new OrderData(Boolean.valueOf(document.get("Done").toString()), document.get("Notes").toString(), document.get("OrderId").toString(), document.get("PaymentMode").toString(), document.get("PaymentStatus").toString(), document.get("Status").toString(), Integer.valueOf(document.get("Total").toString()), document.get("User").toString());
                                    taskItem.setTempName(document.get("Food_Names").toString().substring(1,document.get("Food_Names").toString().length()-1));
                                    taskItem.setTempQty(document.get("Qty_List").toString().substring(1,document.get("Qty_List").toString().length()-1));
                                    orderList.add(taskItem);
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                                }

                                docIdList.add(document.getId());

                                //Toast.makeText(getContext(),.((List<String>) document.get("Food_Names")).get(0),Toast.LENGTH_LONG).show();
                            }

                            //Collections.copy(orderList,list);
                            postsetUi();

                        } else {
                            postsetUi();


                        }
                    }
                });

        db.collection("orders")
                .whereEqualTo("Shop", new Auth().getUId())
                .whereEqualTo("Status","Preparing")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //this is temp list to get from database
                            //orderList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("Doc Id Are:", document.getId());

                                //creating the OrderData object
                                //OrderData taskItem = new OrderData(Boolean.valueOf(document.get("Done").toString()), document.get("Notes").toString(), document.get("OrderId").toString(), document.get("PaymentMode").toString(), document.get("PaymentStatus").toString(), document.get("Status").toString(), Integer.valueOf(document.get("Total").toString()), document.get("User").toString());
                                try
                                {
                                    OrderData taskItem=new OrderData(Boolean.valueOf(document.get("Done").toString()), document.get("Notes").toString(), document.get("OrderId").toString(), document.get("PaymentMode").toString(), document.get("PaymentStatus").toString(), document.get("Status").toString(), Integer.valueOf(document.get("Total").toString()), document.get("User").toString());
                                    taskItem.setTempName(document.get("Food_Names").toString().substring(1,document.get("Food_Names").toString().length()-1));
                                    taskItem.setTempQty(document.get("Qty_List").toString().substring(1,document.get("Qty_List").toString().length()-1));
                                    orderList.add(taskItem);
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                                }



                                docIdList.add(document.getId());

                                //Toast.makeText(getContext(),.((List<String>) document.get("Food_Names")).get(0),Toast.LENGTH_LONG).show();
                            }

                            //Collections.copy(orderList,list);
                            postsetUi();

                        } else {
                            postsetUi();


                        }
                    }
                });

        db.collection("orders")
                .whereEqualTo("Shop", new Auth().getUId())
                .whereEqualTo("Status","Ready")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //this is temp list to get from database
                            //orderList.clear();
                            ArrayList<OrderData> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("Doc Id Are:", document.getId());

                                //creating the OrderData object
                                //OrderData taskItem = new OrderData(Boolean.valueOf(document.get("Done").toString()), document.get("Notes").toString(), document.get("OrderId").toString(), document.get("PaymentMode").toString(), document.get("PaymentStatus").toString(), document.get("Status").toString(), Integer.valueOf(document.get("Total").toString()), document.get("User").toString());
                                try
                                {
                                    OrderData taskItem=new OrderData(Boolean.valueOf(document.get("Done").toString()), document.get("Notes").toString(), document.get("OrderId").toString(), document.get("PaymentMode").toString(), document.get("PaymentStatus").toString(), document.get("Status").toString(), Integer.valueOf(document.get("Total").toString()), document.get("User").toString());
                                    taskItem.setTempName(document.get("Food_Names").toString().substring(1,document.get("Food_Names").toString().length()-1));
                                    taskItem.setTempQty(document.get("Qty_List").toString().substring(1,document.get("Qty_List").toString().length()-1));
                                    orderList.add(taskItem);
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                                }



                                docIdList.add(document.getId());

                                //Toast.makeText(getContext(),.((List<String>) document.get("Food_Names")).get(0),Toast.LENGTH_LONG).show();
                            }

                            //Collections.copy(orderList,list);
                            postsetUi();

                        } else {
                            postsetUi();


                        }
                    }
                });

    }


}
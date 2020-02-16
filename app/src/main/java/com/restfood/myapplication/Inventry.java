package com.restfood.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.restfood.myapplication.ui.home.FoodAvailableAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class
Inventry extends AppCompatActivity {

    //recycler view part
    private RecyclerView rView;
   // private InventoryAdapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;




    FloatingActionButton buttonAdd;
    ArrayList<InventryData> itemList;
    ArrayList<String> docIdList;
    private String uId;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventry);

        itemList=new ArrayList<>();
        docIdList=new ArrayList<>();

        buttonAdd=findViewById(R.id.floatingbutton_invent);


        onbegi();


        //this is pre process of ui or recyclerview
        rView=findViewById(R.id.inventory_recycler_view);
        rView.setHasFixedSize(true);
        rLayoutManager=new LinearLayoutManager(getApplicationContext());

//        itemList.add(new InventryData("Salt",10.0,20.0,30.0));
//        itemList.add(new InventryData("Salt",10,20,30));
//        itemList.add(new InventryData("Salt",10,20,30));
//        itemList.add(new InventryData("Salt",10,20,30));

        getDoc();

        postUi();






        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InventryBottomSheetDialog bottomsheet=new InventryBottomSheetDialog();
                bottomsheet.show(getSupportFragmentManager(),"Hi this is ");
            }
        });
    }







    private void postUi()
    {
        //assigning values
//        rAdapter=new InventoryAdapter(itemList);
//        rView.setLayoutManager(rLayoutManager);
//        rView.setAdapter(rAdapter);
//
//        rAdapter.setOnItemClickListener(new InventoryAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Intent inta=new Intent(Inventry.this,DetailInventory.class);
////                //inta.putExtra("Demo",obj.getFoodName());
//                inta.putExtra("DocId",docIdList.get(position));
//                startActivity(inta);
//
//
//            }
//        });


    }


    private void getDoc()
    {
        try{
            db.collection("shop")
                    .document(new Auth().getUId())
                    .collection("inventory")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //this is temp list to get from database
                                List<InventryData> list = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Log.d("this_id", document.getId() + " => " + document.getData());

                                    docIdList.add(document.getId());
                                    Log.d("Doc Id Are:",document.getId());
                                    try
                                    {
                                        double price=Double.valueOf(document.get("price").toString());
                                        double minQty=Double.valueOf(document.get("minQty").toString());
                                        double quantity=Double.valueOf(document.get("quantity").toString());

                                        InventryData taskItem = new InventryData(document.get("name").toString(),quantity,price,minQty);
                                        list.add(taskItem);
                                        itemList.add(taskItem);
                                    }
                                    catch (Exception e)
                                    {
                                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();

                                    }

                                    //foodList.add(new FoodData("Sausage Pizza","Pizza",500,2,30));

                                }
                                //Collections.copy(itemList,list);
                                postUi();
                                //Toast.makeText(getApplicationContext(),String.valueOf(itemList.size()),Toast.LENGTH_LONG).show();


                            } else {
                                //postUi();
                                //  Toast.makeText(getApplicationContext(),"No Foods",Toast.LENGTH_LONG).show();
                                //                            Log.d(TAG, "Error getting documents: ", task.getException());

                            }
                        }
                    });
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Server error Unable to retreve data",Toast.LENGTH_LONG).show();
        }

    }

    //user id retreve
    private void onbegi()
    {
        Auth auth=new Auth();
        uId=auth.getUId();
    }

    private void add()
    {
        InventryBottomSheetDialog bottomsheet=new InventryBottomSheetDialog();
        bottomsheet.show(getSupportFragmentManager(),"Hi this is ");
    }
}

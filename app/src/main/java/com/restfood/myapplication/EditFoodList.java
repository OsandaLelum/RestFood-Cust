package com.restfood.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class EditFoodList extends AppCompatActivity {
    private RecyclerView rView;
    private FoodListEditAdapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;

    ArrayList<FoodData> foodList=new ArrayList<>();
    ArrayList<String> docIdList=new ArrayList<>();

    private String uId;


    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food_list);

        //this set to next activity


//        this.foodList.add(new FoodData("Cheese Pizza","Pizza",250,10,20));
//        this.foodList.add(new FoodData("Sausage Pizza","Pizza",500,2,30));

        onbegi();

        //this is pre process of ui or recyclerview
        rView = findViewById(R.id.edit_food_list_recycler_view);
        rView.setHasFixedSize(true);
        rLayoutManager=new LinearLayoutManager(this);


        postsetUi();
        getFoodList();
    }

    private void onbegi()
    {
        Auth auth=new Auth();
        uId=auth.getUId();
    }

    //used inside getFoodLsit
    private void postsetUi()
    {
        //assigning list to adepter
        rAdapter=new FoodListEditAdapter(foodList);
        rView.setLayoutManager(rLayoutManager);
        rView.setAdapter(rAdapter);

        rAdapter.setOnItemClickListener(new FoodListEditAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                FoodData obj=foodList.get(position);

               Intent inta=new Intent(EditFoodList.this,EditFood.class);
//                //inta.putExtra("Demo",obj.getFoodName());
                inta.putExtra("DocId",docIdList.get(position));
                startActivity(inta);
                //Toast.makeText(getApplicationContext(),docIdList.get(position),Toast.LENGTH_LONG).show();
            }
        });


    }

    //this function is to get food list form firestore
    private void getFoodList()
    {
        db.collection("shop")
                .document(uId)
                .collection("FoodList")
                .orderBy("category", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //this is temp list to get from database
                                List<FoodData> list = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Log.d("this_id", document.getId() + " => " + document.getData());

                                    docIdList.add(document.getId());
                                    Log.d("Doc Id Are:",document.getId());
                                    FoodData taskItem = document.toObject(FoodData.class);
                                    list.add(taskItem);

                                    foodList.add(new FoodData("Sausage Pizza","Pizza",500,2,30));
//                                    int num=foodList.size();
//                                    Log.d("number",String.valueOf(num));
                                }
//                                Toast.makeText(getApplicationContext(),demo,Toast.LENGTH_LONG).show();
//                                Log.d("firestorelist", list.toString());
                                Collections.copy(foodList,list);
                                postsetUi();

                            } else {
                                postsetUi();
                                Toast.makeText(getApplicationContext(),"No Foods",Toast.LENGTH_LONG).show();
                //                            Log.d(TAG, "Error getting documents: ", task.getException());

                            }
                        }
                    });
    }
}

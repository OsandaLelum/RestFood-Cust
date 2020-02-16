package com.restfood.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.restfood.myapplication.Auth;
import com.restfood.myapplication.EditFood;
import com.restfood.myapplication.EditFoodList;
import com.restfood.myapplication.FoodData;
import com.restfood.myapplication.FoodListEditAdapter;
import com.restfood.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private RecyclerView rView;
    private FoodAvailableAdapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;

    ArrayList<FoodData> foodList=new ArrayList<>();
    ArrayList<String> docIdList=new ArrayList<>();

    private String uId;


    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
        onbegi();

        //this is pre process of ui or recyclerview
        rView = root.findViewById(R.id.food_available_recycler_view);
        rView.setHasFixedSize(true);
        rLayoutManager=new LinearLayoutManager(getActivity());

        getFoodList();
        return root;
    }

    private void onbegi()
    {
        Auth auth=new Auth();
        uId=auth.getUId();
    }

    private void postsetUi()
    {
        //assigning list to adepter
        rAdapter=new FoodAvailableAdapter(foodList);
        rView.setLayoutManager(rLayoutManager);
        rView.setAdapter(rAdapter);

        rAdapter.setOnItemClickListener(new FoodAvailableAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                FoodData obj=foodList.get(position);

//                Intent inta=new Intent(EditFoupodList.this, EditFood.class);
////                //inta.putExtra("Demo",obj.getFoodName());
//                inta.putExtra("DocId",docIdList.get(position));
//                startActivity(inta);
                //Toast.makeText(getActivity().getApplicationContext(),foodList.get(position).getFoodName(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSwitchClick(int position,boolean click) {
                updateAvailable(position,click);

                Log.d("This is stage ",String.valueOf(position));
                Log.d("This is bol",String.valueOf(click));

            }
        });
    }


    private void updateAvailable(final int position, final boolean click)
    {
        //to use in query
        String docId=docIdList.get(position);

        db.collection("shop")
                .document(uId)
                .collection("FoodList")
                .document(docId)
                .update("isAvailable",click)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("This done", "DocumentSnapshot successfully updated!");
                        foodList.get(position).setIsAvailable(click);
                        //Toast.makeText(getActivity().getApplicationContext(),foodList.get(position).getFoodName()+":Updated",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("This error", "Error updating document", e);
                        Toast.makeText(getActivity().getApplicationContext(),foodList.get(position).getFoodName()+":Fail Update",Toast.LENGTH_LONG).show();
                    }
                });

        String clickx=String.valueOf(click);

        db.collection("shop")
                .document(uId)
                .collection("FoodList")
                .document(docId)
                .update("available",clickx)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("This done", "DocumentSnapshot successfully updated!");
                        foodList.get(position).setIsAvailable(click);
                        //Toast.makeText(getActivity().getApplicationContext(),foodList.get(position).getFoodName()+":Updated",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("This error", "Error updating document", e);
                        Toast.makeText(getActivity().getApplicationContext(),foodList.get(position).getFoodName()+":Fail Update",Toast.LENGTH_LONG).show();
                    }
                });

    }


    private void getFoodList()
    {
        db.collection("shop")
                .document(uId)
                .collection("FoodList")
                .orderBy("foodName", Query.Direction.ASCENDING)
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

                            }
                            Collections.copy(foodList,list);
                            postsetUi();

                        } else {
                            postsetUi();
                          //  Toast.makeText(getApplicationContext(),"No Foods",Toast.LENGTH_LONG).show();
                            //                            Log.d(TAG, "Error getting documents: ", task.getException());

                        }
                    }
                });
    }
}
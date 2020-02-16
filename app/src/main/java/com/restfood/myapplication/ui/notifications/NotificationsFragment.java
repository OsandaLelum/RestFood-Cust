package com.restfood.myapplication.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.restfood.myapplication.Add_FoodItem;
import com.restfood.myapplication.Auth;
import com.restfood.myapplication.EditFoodList;
import com.restfood.myapplication.EditProfile;
import com.restfood.myapplication.Inventry;
import com.restfood.myapplication.MainActivity;
import com.restfood.myapplication.Qr;
import com.restfood.myapplication.R;
import com.restfood.myapplication.Summary;

import java.util.HashMap;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    private Button button_add_food;
    private TextView edit_profile;
    private Button buttonSummery;
    private Button buttonSignout;
    private Button buttonQr;
    private Button buttonInventry;

    private Button buttonViewFood;
    public TextView textview;

    Map<String,Object> shopProfile=new HashMap<>();
    FirebaseFirestore db= FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);

        //here root
        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        textview = root.findViewById(R.id.shop_name);
        getShopData();

//        notificationsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        //here initializing variable
        button_add_food=root.findViewById(R.id.button_add_food);
        edit_profile=root.findViewById(R.id.edit_profile);
        buttonViewFood=root.findViewById(R.id.button_view_food);
        buttonSignout=root.findViewById(R.id.button_signout);
        buttonQr=root.findViewById(R.id.button_qr);
        buttonInventry=root.findViewById(R.id.button_invent);

        button_add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getActivity(), Add_FoodItem.class);
                startActivity(intent2);
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });

        buttonViewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), EditFoodList.class);
                startActivity(intent);
            }
        });

        buttonSummery=root.findViewById(R.id.button_summery);
        buttonSummery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(),"This is not developed yet",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(), Summary.class);
                startActivity(intent);
            }
        });

        buttonQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(getActivity(),Qr.class);
                startActivity(intent3);

            }
        });

        buttonInventry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(getActivity(), Inventry.class);
                startActivity(intent3);

            }
        });

   /*   buttonSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });
   */


        return root;
    }

    private void getShopData()
    {
        Source source=Source.CACHE;
        ///this is
        db.collection("shop")
                .document(new Auth().getUId())
                .get(source)
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                shopProfile=document.getData();
                                //shop_name_text.setText(shopProfile.get("shopName").toString());
                               // Toast.makeText(getActivity().getApplicationContext(),shopProfile.get("shopName").toString(),Toast.LENGTH_LONG).show();
                                assignVal();

                            } else {

                            }
                        } else {

                        }
                    }
                });

    }

    private void assignVal()
    {
        textview.setText(shopProfile.get("shopName").toString());
    }



    private void editProfile()
    {
        Intent intent=new Intent(getActivity(), EditProfile.class);
        startActivity(intent);
    }





}
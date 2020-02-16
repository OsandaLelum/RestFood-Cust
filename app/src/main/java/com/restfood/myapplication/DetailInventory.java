package com.restfood.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailInventory extends AppCompatActivity {
    String docId;

    InventryData obj;

    TextView nameTextView;
    TextInputEditText priceEditText;
    TextInputEditText qtyEditText;
    TextInputEditText minQtyEditText;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_inventory);

        Intent inta=getIntent();
        docId=inta.getStringExtra("DocId");

        //assigning values
        priceEditText=findViewById(R.id.item_price_text);
        qtyEditText=findViewById(R.id.item_qty_text);
        minQtyEditText=findViewById(R.id.item_min_qty_text);
        nameTextView=findViewById(R.id.item_name);

        createFoodData();

    }


    private void assignValues()
    {
        nameTextView.setText(obj.getName());
        priceEditText.setText(String.valueOf(obj.getPrice()));
        minQtyEditText.setText(String.valueOf(obj.getMinQty()));
        qtyEditText.setText(String.valueOf(obj.getQuantity()));

    }


    //to get value to object
    private void createFoodData()
    {
        db.collection("shop")
                .document(new Auth().getUId())
                .collection("FoodList")
                .document(docId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //assign value to foodData object
                        obj=documentSnapshot.toObject(InventryData.class);
                        //assignValues();
                        Toast.makeText(getApplicationContext(),"current data updated",Toast.LENGTH_LONG).show();
                        //Log.d("This is test of int:",String.valueOf(foodObj));
                    }

                });


    }




    public void onSubmit(View view)
    {

    }

    public void deleteItem(View view)
    {
        db.collection("shop")
                .document(new Auth().getUId())
                .collection("inventory")
                .document(docId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        Toast.makeText(getApplicationContext(),"Successfully deleted!",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log.w(TAG, "Error deleting document", e);
                        Toast.makeText(getApplicationContext(),"Error deleting",Toast.LENGTH_LONG).show();
                    }
                });

        finish();

    }
}

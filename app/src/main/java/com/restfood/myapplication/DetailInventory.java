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
                .collection("inventory")
                .document(docId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        try
                        {
                            //Toast.makeText(getApplicationContext(),docId,Toast.LENGTH_LONG).show();
                            double price=Double.valueOf(documentSnapshot.get("price").toString());
                            double minQty=Double.valueOf(documentSnapshot.get("minQty").toString());
                            double quantity=Double.valueOf(documentSnapshot.get("quantity").toString());
                            //String name=documentSnapshot.get("name").toString();

                            //InventryData taskItem = new InventryData(name,quantity,price,minQty);
                            obj=new InventryData(documentSnapshot.get("name").toString(),quantity,price,minQty);

                            assignValues();

                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();

                        }

                        Toast.makeText(getApplicationContext(),obj.getName(),Toast.LENGTH_LONG).show();
                    }

                });

    }




    public void onSubmit(View view)
    {
        String price=priceEditText.getText().toString();
        String qty=qtyEditText.getText().toString();
        String minQty=minQtyEditText.getText().toString();

        InventryData objx=new InventryData(obj.getName(),Double.valueOf(qty),Double.valueOf(price),Double.valueOf(minQty));

        db.collection("shop")
                .document(new Auth().getUId())
                .collection("inventory")
                .document(docId)
                .set(objx)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_LONG).show();
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Update failure",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });



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

package com.restfood.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class InventryBottomSheetDialog extends BottomSheetDialogFragment {


    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private TextView increaseTextView;
    private TextView decreaseTextView;

    private EditText itemEditText;
    private EditText qtyEditText;

    public InventryData temp;

    double num=0.0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.bottom_sheet_layout,container,false);


        increaseTextView=v.findViewById(R.id.increace);
        decreaseTextView=v.findViewById(R.id.reduce);
        itemEditText=v.findViewById(R.id.item_edit_text);
        qtyEditText=v.findViewById(R.id.qty_edit_text);

        increaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });

        decreaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem();
                //Toast.makeText(getContext(), "So sadx",Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    private void removeItem()
    {
        String item=itemEditText.getText().toString();
        double qty;
        qty = Double.parseDouble(qtyEditText.getText().toString());
        double dbVal=getInventDoc(item);

        updateItem(item,qty);




//            if(Double.compare(qty,dbVal)<0)
//            {
//                qty=dbVal-qty;
//                updateItem(item,qty);
//
//            }
//            else
//            {
//                Toast.makeText(getContext(),"Invalid Input", Toast.LENGTH_SHORT).show();
//
//
//            }







    }


    private void updateItem(String name,final double qty)
    {
        db.collection("shop")
                .document(new Auth().getUId())
                .collection("inventory")
                .whereEqualTo("name",name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //Toast.makeText(getContext(), (CharSequence) document.getData(),Toast.LENGTH_LONG).show();
                                num=document.getDouble("quantity");

                                if(qty<=num)
                                {


                                    double i=num-qty;
                                    updatex(document.getId(),i);
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Invalid input",Toast.LENGTH_LONG).show();
                                }



                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(getContext(),"Incorrect Item name",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }



    private void updatex(String id,double qty)
    {
        db.collection("shop")
                .document(new Auth().getUId())
                .collection("inventory")
                .document(id)
                .update("quantity",qty)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Toast.makeText(getContext(),"Inventory Updated", Toast.LENGTH_SHORT).show();
                        setTextField();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error updating document", e);
                        Toast.makeText(getContext(),"Unable to Update", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private double getInventDoc(String name)
    {
        num=0.0;

        Toast.makeText(getContext(), "So sadttt",Toast.LENGTH_LONG).show();
        db.collection("shop")
                .document(new Auth().getUId())
                .collection("inventory")
                .whereEqualTo("name",name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //Toast.makeText(getContext(), (CharSequence) document.getData(),Toast.LENGTH_LONG).show();
                                num=document.getDouble("quantity");

                                //Toast.makeText(getContext(), String.valueOf(num),Toast.LENGTH_LONG).show();

                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(getContext(),"Incorrect Item name",Toast.LENGTH_LONG).show();

                        }
                    }
                });
        Toast.makeText(getContext(),String.valueOf(num),Toast.LENGTH_LONG).show();
        return num;




    }

    private void addItem()
    {
        String item=itemEditText.getText().toString();
        String qty=qtyEditText.getText().toString();

        if(item.trim().length()!=0 && qty.trim().length()!=0)
        {
            InventryData obj=new InventryData(itemEditText.getText().toString().trim(),Double.valueOf(qtyEditText.getText().toString()));

            db.collection("shop")
                    .document(new Auth().getUId())
                    .collection("inventory")
                    .add(obj)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(getContext(),"Inventory Updated", Toast.LENGTH_SHORT).show();
                            setTextField();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error adding document", e);
                            Toast.makeText(getContext(),"Error", Toast.LENGTH_SHORT).show();
                        }
                    });


        }



    }


    private void setTextField()
    {
        itemEditText.getText().clear();
        qtyEditText.getText().clear();
    }


}

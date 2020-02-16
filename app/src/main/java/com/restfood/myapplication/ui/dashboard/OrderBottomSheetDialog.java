package com.restfood.myapplication.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.restfood.myapplication.Auth;
import com.restfood.myapplication.OrderData;
import com.restfood.myapplication.R;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static com.restfood.myapplication.R.drawable.buttonstyle1;
import static com.restfood.myapplication.R.drawable.buttonstyle2;
import static com.restfood.myapplication.R.drawable.cancelbutton;

public class OrderBottomSheetDialog extends BottomSheetDialogFragment {
    TextView tom;
    String docId;  //getting the data passed by fragment4

    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    public OrderData orederObj;

    //ui components for payment
    private TextView totalTextView;
    private TextView paymentMethodTextView;
    private TextView paymentStatusTextView;
    private Button paymetButton;
    private Button cancelButton;

    //button to handle order
    private Button preparingButton;
    private Button readyButton;
    private Button doneButton;

    private TextView orderIdTextView;
    private TextView foodName;
    private TextView noteTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.order_bottom_sheet_layout,container,false);
        docId=getTag();
        //set the ui
        totalTextView=v.findViewById(R.id.order_total);
        paymentMethodTextView=v.findViewById(R.id.order_payment_method);
        paymentStatusTextView=v.findViewById(R.id.order_payment_status);
        paymetButton=v.findViewById(R.id.order_payment_button);
        cancelButton=v.findViewById(R.id.cancel_order);

        //order handling
        preparingButton=v.findViewById(R.id.button_preparing);
        readyButton=v.findViewById(R.id.button_ready);
        doneButton=v.findViewById(R.id.button_done);

        orderIdTextView=v.findViewById(R.id.order_title);
        foodName=v.findViewById(R.id.food_list);
        noteTextView=v.findViewById(R.id.note);


        getOrder(docId);




        return v;
    }

    //set the ui after the db transaction
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    private void setUi()
    {
        totalTextView.setText(String.valueOf(orederObj.getTotal()));
        paymentStatusTextView.setText(orederObj.getPaymentStatus());
        paymentMethodTextView.setText(orederObj.getPaymentMode());

        String title=orederObj.getOrderId();
        orderIdTextView.setText(title);


        //Toast.makeText(getContext(),orederObj.getStatus(),Toast.LENGTH_LONG).show();
        String status=orederObj.getStatus();
        Toast.makeText(getContext(),status,Toast.LENGTH_LONG).show();
        if(status.equals("Preparing"))
        {
            //setting button color
            readyButton.setBackground(getResources().getDrawable(buttonstyle2));
            preparingButton.setBackground(getResources().getDrawable(buttonstyle1));
            doneButton.setBackground(getResources().getDrawable(buttonstyle2));

            preparingButton.setTextColor(R.color.white);
            preparingButton.setTextColor(R.color.button_blue);
            doneButton.setTextColor(R.color.button_blue);


        }
        else if(status.equals("Ready"))
        {
            //ui change part
            readyButton.setBackground(getResources().getDrawable(buttonstyle1));
            preparingButton.setBackground(getResources().getDrawable(buttonstyle1));
            doneButton.setBackground(getResources().getDrawable(buttonstyle2));

            readyButton.setTextColor(R.color.white);
            preparingButton.setTextColor(R.color.white);
            doneButton.setTextColor(R.color.button_blue);

            //db part


        }
        else if(status.equals("Done"))
        {
            //ui change part
            readyButton.setBackground(getResources().getDrawable(buttonstyle1));
            preparingButton.setBackground(getResources().getDrawable(buttonstyle1));
            doneButton.setBackground(getResources().getDrawable(buttonstyle1));

            readyButton.setTextColor(R.color.white);
            preparingButton.setTextColor(R.color.white);
            doneButton.setTextColor(R.color.white);



        }

        if(orederObj.getPaymentStatus().equals("Paid"))
        {
            paymetButton.setText("Paid");   //this check and update
        }


        //this is to butoon
        paymetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //paymetButton.setText("Paid");
                if(orederObj.getPaymentStatus().equals("Paid"))
                {
                    Toast.makeText(getActivity(),"Already Paid",Toast.LENGTH_LONG).show();

                }
                else
                {
                    updatePayment(orederObj.getOrderId(),"Paid");
                    paymetButton.setText("Paid");   //this check and update
                }


            }
        });


        //button to handle cancel part
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrder(orederObj.getOrderId(),"Cancel");
            }
        });


        ///oder handling part

        preparingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                readyButton.setBackground(getResources().getDrawable(buttonstyle2));
                preparingButton.setBackground(getResources().getDrawable(buttonstyle1));
                doneButton.setBackground(getResources().getDrawable(buttonstyle2));

                preparingButton.setTextColor(R.color.white);
                preparingButton.setTextColor(R.color.button_blue);
                doneButton.setTextColor(R.color.button_blue);

                updateStatus(orederObj.getOrderId(),"Preparing");


            }
        });


        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyButton.setBackground(getResources().getDrawable(buttonstyle1));
                preparingButton.setBackground(getResources().getDrawable(buttonstyle1));
                doneButton.setBackground(getResources().getDrawable(buttonstyle2));

                readyButton.setTextColor(R.color.white);
                preparingButton.setTextColor(R.color.white);
                doneButton.setTextColor(R.color.button_blue);

                updateStatus(orederObj.getOrderId(),"Ready");


            }
        });


        doneButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                readyButton.setBackground(getResources().getDrawable(buttonstyle1));
                preparingButton.setBackground(getResources().getDrawable(buttonstyle1));
                doneButton.setBackground(getResources().getDrawable(buttonstyle1));

                readyButton.setTextColor(R.color.white);
                preparingButton.setTextColor(R.color.white);
                doneButton.setTextColor(R.color.white);

                updateStatus(orederObj.getOrderId(),"Done");
                orederObj.setStatus("Done");    //setting to object
                storeHistory(orederObj);        //this is calling the function

            }
        });


        //this is to set the food list of order
        List<String> fName=orederObj.convertFoodName();
        List<String> fQty=orederObj.convertFoodQty();

        String name="Order List:\n";
        int i;
        for (i=0;i<fName.size();i++)
        {
            name=name+"\t"+fName.get(i)+"\t:"+fQty.get(i)+"\n";
        }

        foodName.setText(name);


        String note="Note:";
        //this part is to set the note
        if(orederObj.getNotes()!=null)
        {
            note=note+orederObj.getNotes();
            noteTextView.setText(note);
        }
        else
        {
            note=note+"Empty";
            noteTextView.setText(note);
        }




    }


    //status- what you update
    private void updateStatus(String docId, final String status) {
        db.collection("orders")
                .document(docId)
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


    //this is to set the payment status
    private void updatePayment(String docId, final String payment) {
        db.collection("orders")
                .document(docId)
                .update("PaymentStatus",payment)
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

    //this is to set the payment status
    private void cancelOrder(String docId, final String cancel) {
        db.collection("orders")
                .document(docId)
                .update("Status",cancel)
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

    //this is to store data to shop path
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void storeHistory(OrderData obj)
    {
        LocalDate currentdate = LocalDate.now();
        Month currentMonth = currentdate.getMonth();
        int currentYear = currentdate.getYear();

//        Date date=new Date();
//        int month=date.getMonth();
//        int year=date.getYear();

        //this part is to store order
        final String yea=String.valueOf(currentYear);
        final String mon=currentMonth.toString();
        final String day=String.valueOf(currentdate.getDayOfMonth());
        final String dat=yea+"-"+mon+"-"+day;


        db.collection("shop")
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
                        //orderList.remove(position);



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




    //this method is to retreve from database again for
    //real time update
    private void getOrder(String id)
    {
        //this is to cache the data
        Source sou=Source.CACHE;

        try
        {
            db.collection("orders")
                    .document(id)
                    .get(sou)
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    try{
                                        orederObj=new OrderData(Boolean.valueOf(document.get("Done").toString()), document.get("Notes").toString(), document.get("OrderId").toString(), document.get("PaymentMode").toString(), document.get("PaymentStatus").toString(), document.get("Status").toString(), Integer.valueOf(document.get("Total").toString()), document.get("User").toString());
                                        orederObj.setTempName(document.get("Food_Names").toString().substring(1,document.get("Food_Names").toString().length()-1));
                                        orederObj.setTempQty(document.get("Qty_List").toString().substring(1,document.get("Qty_List").toString().length()-1));
                                    }
                                    catch (Exception e)
                                    {
                                        Log.d("ERROR",e.toString());
                                    }

                                    //call the function here

                                    setUi();        //this part is setting the data

                                } else {
                                    //Log.d(TAG, "No such document");
                                }
                            } else {
                                //Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
        }catch (Exception e)
        {
            Log.d("what",e.toString());
        }





    }


}

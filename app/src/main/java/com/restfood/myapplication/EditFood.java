package com.restfood.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditFood extends AppCompatActivity {
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    //this is doc number for
    String docId;
    FoodData foodObj;

    private TextInputEditText food_name_edit_text;
    private TextInputLayout food_name_layout;

    private TextInputEditText food_price_edit_text;
    private TextInputLayout food_price_layout;

    private TextInputEditText min_duration_text;
    private TextInputLayout min_duration_layout;

    private TextInputEditText max_duration_text;

    private TextInputEditText cat_text;



    private TextInputLayout des_layout;
    private TextInputEditText des_text;

    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);

        //to get doc Id from previeus activity
        Intent inta=getIntent();
        docId=inta.getStringExtra("DocId");

        createUiVariable();
        createFoodData();


    }

    //assigning xml component to that
    private void createUiVariable()
    {
        food_name_edit_text=findViewById(R.id.food_name_text);
        food_name_layout=findViewById(R.id.food_name_layout);

        food_price_edit_text=findViewById(R.id.food_price_text);
        food_price_layout=findViewById(R.id.food_price_layout);

        min_duration_text=findViewById(R.id.min_pre_time_text);
        min_duration_layout=findViewById(R.id.min_pre_time_layout);
        max_duration_text=findViewById(R.id.max_pre_time_text);
        cat_text=findViewById(R.id.food_cat_text);


        des_text=findViewById(R.id.food_des_text);
        des_layout=findViewById(R.id.food_des_layout);

        cancelButton=findViewById(R.id.button_delete);
    }

    public void assignValue()
    {
        String x;
        if(foodObj.getFoodName()!=null)
        {
            food_name_edit_text.setText(foodObj.getFoodName());

        }

        food_price_edit_text.setText(String.valueOf(foodObj.getPrice()));
        min_duration_text.setText(String.valueOf(foodObj.getMinDuration()));
        max_duration_text.setText(String.valueOf(foodObj.getMaxDuration()));

        if(foodObj.getDescription()!=null)
        {
            des_text.setText(foodObj.getDescription());
        }

        if(foodObj.getCategory()!=null)
        {
            cat_text.setText(foodObj.getCategory());
        }

            //this is for test
//        food_name_edit_text.setText("test");
//        food_price_edit_text.setText("test");
//        min_duration_text.setText("test");
//        max_duration_text.setText("test");

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
                        foodObj=documentSnapshot.toObject(FoodData.class);
                        assignValue();
                        Toast.makeText(getApplicationContext(),"current data updated",Toast.LENGTH_LONG).show();
                        //Log.d("This is test of int:",String.valueOf(foodObj));
                    }

                });


    }


    //this is validation for both
    //max and min
    //if max or min one can be null
    //didn't check for  becuse for ready madefood
    private boolean checkDuration()
    {
        String min_time=min_duration_text.getText().toString();
        String max_time=max_duration_text.getText().toString();

        if(!min_time.isEmpty() | !max_time.isEmpty())
        {
            min_duration_layout.setError(null);
            return true;
        }
        else
        {
            min_duration_layout.setError("Enter Duration");
            return false;
        }

    }

    //validation checking for price
    private boolean checkPrice()
    {
        String food_price=food_price_edit_text.getText().toString();

        if(food_price.length()==0)
        {
            food_price_layout.setError("Price is Empty");
            return false;
        }
        else
        {

            try
            {
                int price=Integer.parseInt(food_price);
                if(price>0)
                {
                    food_price_layout.setError(null);
                    return true;

                }
                else
                {
                    food_price_layout.setError("Food Price is 0");
                    return false;

                }
            }
            catch (NumberFormatException e)
            {
                food_price_layout.setError("Dandanaka done");
                return false;
            }
        }
    }


    //validation checking for food name
    private boolean checkFoodName()
    {
        String food_name=food_name_edit_text.getText().toString().trim();

        if(food_name.length()==0)
        {
            food_name_layout.setError("Length is 0");
            return false;
        }
        else
        {
            if(food_name.length()<4)
            {
                food_name_layout.setError("Food Name is too short");
            }
            else
            {
                food_name_layout.setError(null);
            }

            return true;
        }
    }

    //this is to convert string from textfield to int
    private int convertToInt(String num)
    {
        if(num==null)
        {
            return 0;
        }
        else
        {
            return Integer.parseInt(num);
        }
    }


    //this work for button click
    //this add data to database
    public void onSubmit(View v)
    {
        if(checkFoodName() & checkPrice() & checkDuration())
        {
            updateFood();
        }

    }

    private void updateFood()
    {
        FoodData food=new FoodData(food_name_edit_text.getText().toString(),cat_text.getText().toString(),convertToInt(food_price_edit_text.getText().toString()),convertToInt(min_duration_text.getText().toString()),convertToInt(max_duration_text.getText().toString()),foodObj.getIsVeg()  );

        db.collection("shop")
                .document(new Auth().getUId())
                .collection("FoodList")
                .document(docId)
                .set(food)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Update failure",Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void deleteFood(View view)
    {
        db.collection("shop")
            .document(new Auth().getUId())
            .collection("FoodList")
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

    //coped from android studio doc have to change
    //just for desiging
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();


        //Toast.makeText(getApplicationContext(),String.valueOf(checked), Toast.LENGTH_SHORT).show();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton_veg:
                if (checked)
                    // Pirates are the best
                    //this.isVeg=true;
                break;
            case R.id.radioButton_non_veg:
                if (checked)
                    // Ninjas rule
                    //this.isVeg=false;
                break;
        }

        //Toast.makeText(getApplicationContext(),String.valueOf(isVeg), Toast.LENGTH_SHORT).show();

    }
}

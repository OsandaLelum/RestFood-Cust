package com.restfood.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private EditText editTextMobile;
    private TextView mytext;
    private Button googleButton;

    private FirebaseAuth myauth;
    private FirebaseUser myuser;
    private String myid;
    int RC_SIGN_IN=2;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myauth=FirebaseAuth.getInstance();
        myuser=myauth.getCurrentUser();     //user's id

        editTextMobile = findViewById(R.id.editTextMobile);
       // Toast.makeText(getApplicationContext(),myuser.getUid(),Toast.LENGTH_LONG);


        //is user null
        if(myuser!=null)
        {
            Intent intent2 = new Intent(MainActivity.this, main_bottom_navigation.class);
            startActivity(intent2);
        }

        googleButton=findViewById(R.id.google_signin);


        //google sign in
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Choose authentication providers
                List<AuthUI.IdpConfig> providers = Arrays.asList(

                        new AuthUI.IdpConfig.GoogleBuilder().build());

                // Create and launch sign-in intent
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);

            }
        });




        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextMobile.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 9){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, VerifyPhone.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });





    }
    




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Toast.makeText(getApplicationContext(),user.toString(),Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(MainActivity.this, main_bottom_navigation.class);
                startActivity(intent2);

                // ...
            } else {
                Toast.makeText(getApplicationContext(),"Sorry",Toast.LENGTH_LONG).show();
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

}

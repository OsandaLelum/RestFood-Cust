package com.restfood.myapplication;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


//this auth class has basic information about
//to avoid un same object again and again

public class Auth {

    private FirebaseAuth fbauth= FirebaseAuth.getInstance();

    //this
    private String uId;


    //constructor
    public Auth()
    {
        setUId();

    }


    public String getUId()
    {
        return uId;
    }

    private void setUId()
    {
        uId=fbauth.getCurrentUser().getUid();
    }

    public String getPhoneNo()
    {
        return fbauth.getCurrentUser().getPhoneNumber();
    }











}

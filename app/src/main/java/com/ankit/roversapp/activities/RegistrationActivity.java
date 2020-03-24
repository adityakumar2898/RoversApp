package com.ankit.roversapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ankit.roversapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistrationActivity extends AppCompatActivity implements View.OnTouchListener,View.OnClickListener  {


    private EditText nameEdit,usernameEdit,emailEdit,phnNumberEdit,passwordEdit,confirmPaswordEdit;
    private String nameString,usernameString,emailString,phnNumberString,passwordString,confirmPaswordString;
    private Button registerButton;
    private TextView signin;
    private Intent intent;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        nameEdit=(EditText)findViewById(R.id.regis_name_edit);
        usernameEdit=(EditText)findViewById(R.id.regis_username_edit);
        emailEdit=(EditText)findViewById(R.id.regis_email_edit);
        phnNumberEdit=(EditText)findViewById(R.id.regis_phonenumber_edit);
        passwordEdit=(EditText)findViewById(R.id.regis_password_edit);
        confirmPaswordEdit=(EditText)findViewById(R.id.regis_confirmpassword_edit);
        registerButton=(Button)findViewById(R.id.register_button);
        signin=(TextView)findViewById(R.id.signin);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        registerButton.setOnClickListener(this);
        signin.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void registerAction(View view) {
        gettingEditTextData();


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        if (nameString.isEmpty() || usernameString.isEmpty() || emailString.isEmpty() || phnNumberString.isEmpty() || passwordString.isEmpty() || confirmPaswordString.isEmpty() ) {
            Toast.makeText(this, "Enter All Details", Toast.LENGTH_LONG).show();
        }
        else if(nameString.length()<3) {
            Toast.makeText(this, "Name should be minimum 3 alphabets long", Toast.LENGTH_LONG).show();
        }
        else if(usernameString.length()<3) {
            Toast.makeText(this, "Username should be minimum 3 alphabets long", Toast.LENGTH_LONG).show();
        }
        else if(phnNumberString.length()<10) {
            Toast.makeText(this, "Phone Number should be of 10 digts", Toast.LENGTH_LONG).show();

        }
        else if(!passwordString.equals(confirmPaswordString))
        {
            Toast.makeText(this, "Password and Confirm Password do not match. Passwords are case sensitive", Toast.LENGTH_LONG).show();
        }
        else{
            register(emailString,passwordString);
        }
    }

    public void register(String email,String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegistrationActivity.this, task -> {

            if(task.isSuccessful()) {
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(RegistrationActivity.this, task2 -> {
                    if(task2.isSuccessful()) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nameString).build();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user != null) {
                            user.updateProfile(request);
                            mAuth.signOut();
                            Log.i("SignIn","Success");

                            Toast.makeText(RegistrationActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                            login();
                        } else {
                            Log.i("SignIn","Failed");
                        }
                    }
                });


            }
            else {
                Toast.makeText(RegistrationActivity.this, "Some Error Occurred! Please Try Again.", Toast.LENGTH_LONG).show();
            }
        });


    }


    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.register_button:
                registerAction(view);
                break;
            case R.id.signin:
                login();
                break;
            default:
                Log.i("Error", "EditTextClicked: "+ view.getId());
         }

//        if (usernameString.equals("") && passwordString.equals("")) {
//            usernameEdit.setError("Please Enter Username");
//            passwordEdit.setError("Please Enter Password");
//        } else if (usernameString.equals("")) {
//            usernameEdit.setError("Please Enter Username");
//        } else if (passwordString.equals("")) {
//            passwordEdit.setError("Please Enter Password");
//        } else {
//            login();
//        }
    }





    //Getting EditText Data
    public void gettingEditTextData()
    {

        nameString=nameEdit.getText().toString();
        usernameString=usernameEdit.getText().toString();
        emailString=emailEdit.getText().toString();
        phnNumberString=phnNumberEdit.getText().toString();
        passwordString=passwordEdit.getText().toString();
        confirmPaswordString=confirmPaswordEdit.getText().toString();
    }


    private void login() {
        intent=new Intent(RegistrationActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    //OnTouch Events for EditTexts
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

//        gettingEditTextData();
//
//
//        if(usernameString.length()==0)
//        {
//            usernameEdit.setError("Please Enter Username");
//        }
//
        return false;
    }

}
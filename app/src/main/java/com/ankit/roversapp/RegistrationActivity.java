package com.ankit.roversapp;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener  {


    private EditText nameEditText, usernameEditText, emailEditText, phoneEditText, passwordEditText, confirmPasswordEditText;
    private String name, username, email, phone, password, confirmPassword;
    private Button registerButton;
    private TextView login;
    private Intent intent;

    private ConstraintLayout parent;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        nameEditText = findViewById(R.id.nameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passEditText);
        confirmPasswordEditText = findViewById(R.id.confPassEditText);
        registerButton = findViewById(R.id.registerBtn);
        login = findViewById(R.id.loginTextView);
        parent = findViewById(R.id.parent);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(this);
        login.setOnClickListener(this);
        parent.setOnTouchListener((v, e) -> {
            hideKeyboard(v);
            return false;
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void hideKeyboard(View view) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            parent.clearFocus();
        }
    }

    public void registerAction(View view) {
        gettingEditTextData();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ) {
            Toast.makeText(this, "Enter All Details", Toast.LENGTH_LONG).show();
        }
        else if(name.length()<3) {
            Toast.makeText(this, "Name should be minimum 3 alphabets long", Toast.LENGTH_LONG).show();
        }
        else if(username.length()<3) {
            Toast.makeText(this, "Username should be minimum 3 alphabets long", Toast.LENGTH_LONG).show();
        }
        else if(phone.length()<10) {
            Toast.makeText(this, "Phone Number should be of 10 digts", Toast.LENGTH_LONG).show();

        }
        else if(!password.equals(confirmPassword))
        {
            Toast.makeText(this, "Password and Confirm Password do not match. Passwords are case sensitive", Toast.LENGTH_LONG).show();
        }
        else{
            register(email,password);
        }
    }

    public void register(String email,String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegistrationActivity.this, task -> {

            if(task.isSuccessful()) {
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(RegistrationActivity.this, task2 -> {
                    if(task2.isSuccessful()) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();

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
            case R.id.registerBtn:
                registerAction(view);
                break;
            case R.id.loginTextView:
                login();
                break;
            default:
                Log.i("Error", "EditTextClicked: "+ view.getId());
         }
    }





    //Getting EditText Data
    public void gettingEditTextData() {
        name = nameEditText.getText().toString();
        username = usernameEditText.getText().toString();
        email = emailEditText.getText().toString();
        phone = phoneEditText.getText().toString();
        password = passwordEditText.getText().toString();
        confirmPassword = confirmPasswordEditText.getText().toString();
    }


    private void login() {
        intent=new Intent(RegistrationActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
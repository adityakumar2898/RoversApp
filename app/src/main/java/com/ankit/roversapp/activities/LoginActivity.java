package com.ankit.roversapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ankit.roversapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginActivity extends AppCompatActivity implements View.OnTouchListener,View.OnClickListener{

    private EditText emailEdit, passwordEdit;
    private String emailString, passwordString;
    private Button loginButton;
    private Intent intent;
    private TextView signup;

    //Firebase
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailEdit = (EditText)findViewById(R.id.login_username_edit);
        passwordEdit = (EditText) findViewById(R.id.login_password_edit);
        loginButton = (Button) findViewById(R.id.login_button);
        signup = (TextView) findViewById(R.id.signup);

        mAuth = FirebaseAuth.getInstance();

//        passwordEdit.setOnTouchListener(this);
        loginButton.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, task -> {
            if(task.isSuccessful()) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.login_button) {

            gettingEditTextData();


            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


            if (emailString != null && passwordString != null) {
                login(emailString, passwordString);
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_LONG).show();
            }

        }


        else if(view.getId()==R.id.signup)
        {
            intent=new Intent(LoginActivity.this,RegistrationActivity.class);
            startActivity(intent);
        }

//        if (emailString.equals("") && passwordString.equals("")) {
//            emailEdit.setError("Please Enter Username");
//            passwordEdit.setError("Please Enter Password");
//        } else if (emailString.equals("")) {
//            emailEdit.setError("Please Enter Username");
//        } else if (passwordString.equals("")) {
//            passwordEdit.setError("Please Enter Password");
//        } else {
//            login();
//        }
    }





    //Getting EditText Data
    public void gettingEditTextData()
    {
        emailString=emailEdit.getText().toString();
        passwordString=passwordEdit.getText().toString();
    }


    //OnTouch Events for EditTexts
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

//        gettingEditTextData();
//
//
//        if(emailString.length()==0)
//        {
//            emailEdit.setError("Please Enter Username");
//        }
//
        return false;
    }











}

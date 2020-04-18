package com.ankit.roversapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import info.androidhive.fontawesome.FontDrawable;

public class LoginActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, View.OnFocusChangeListener {

    private EditText emailEdit, passwordEdit;
    private String emailString, passwordString;
    private Button loginButton, facebookBtn, googleBtn;
    private Intent intent;
    private TextView signup;
    ConstraintLayout parent;

    private final float ICON_SIZE = 20f;
    private FontDrawable drawableUser, drawablePassword, drawableFacebook, drawableGoogle;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.activity_login);
//        getSupportActionBar().hide(); //hide the title bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen

        emailEdit = findViewById(R.id.emailTextView);
        passwordEdit = findViewById(R.id.passwordTextView);
        loginButton = findViewById(R.id.registerBtn);
        facebookBtn = findViewById(R.id.facebookBtn);
        googleBtn = findViewById(R.id.googleBtn);
        signup = findViewById(R.id.signUp);
        parent = findViewById(R.id.parent);

        mAuth = FirebaseAuth.getInstance();

        setIcons();

        //Set Listeners
//        passwordEdit.setOnTouchListener(this);
        emailEdit.setOnFocusChangeListener(this);
        passwordEdit.setOnFocusChangeListener(this);
        loginButton.setOnClickListener(this);
        signup.setOnClickListener(this);
        parent.setOnTouchListener(this);
    }

    public void setIcons() {

        drawableUser = new FontDrawable(LoginActivity.this, R.string.fa_user, true, false);
        drawableUser.setTextSize(ICON_SIZE);

        drawablePassword = new FontDrawable(LoginActivity.this, R.string.fa_lock_solid, true, false);
        drawablePassword.setTextSize(ICON_SIZE);

        drawableFacebook = new FontDrawable(LoginActivity.this, R.string.fa_facebook, false, true);
        drawableFacebook.setTextSize(ICON_SIZE);
        drawableFacebook.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));

        drawableGoogle = new FontDrawable(LoginActivity.this, R.string.fa_google, false, true);
        drawableGoogle.setTextSize(ICON_SIZE);
        drawableGoogle.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));

        emailEdit.setCompoundDrawablesWithIntrinsicBounds(drawableUser, null, null, null);
        passwordEdit.setCompoundDrawablesWithIntrinsicBounds(drawablePassword, null, null, null);
        facebookBtn.setCompoundDrawablesWithIntrinsicBounds(drawableFacebook, null, null, null);
        googleBtn.setCompoundDrawablesWithIntrinsicBounds(drawableGoogle, null, null, null);
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void hideKeyboard(View view) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            parent.clearFocus();
            view.performClick();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerBtn:
                gettingEditTextData();

                if (!("".equals(emailString) && "".equals(passwordString))) {
                    login(emailString, passwordString);
                } else {
//                    Toast.makeText(this, "Email or Password can't be blank", Toast.LENGTH_LONG).show();
                    Snackbar.make(view, "Email or Password can't be Blank", Snackbar.LENGTH_SHORT).setBackgroundTint(ContextCompat.getColor(this, R.color.dark_blue)).show();
                }
                break;
            case R.id.signUp:
                intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                break;
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
    public void gettingEditTextData() {
        emailString = emailEdit.getText().toString();
        passwordString = passwordEdit.getText().toString();
    }

    //OnTouch Events
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        hideKeyboard(view);
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.emailTextView:
                if (hasFocus) {
                    drawableUser.setTextColor(ContextCompat.getColor(this, R.color.button_blue));
                    emailEdit.setTextColor(ContextCompat.getColor(this, R.color.button_blue));
                } else {
                    drawableUser.setTextColor(ContextCompat.getColor(this, R.color.Black));
                    emailEdit.setTextColor(ContextCompat.getColor(this, R.color.Black));
                }
                break;
            case R.id.passwordTextView:
                if (hasFocus) {
                    drawablePassword.setTextColor(ContextCompat.getColor(this, R.color.button_blue));
                    passwordEdit.setTextColor(ContextCompat.getColor(this, R.color.button_blue));
                } else {
                    drawablePassword.setTextColor(ContextCompat.getColor(this, R.color.Black));
                    passwordEdit.setTextColor(ContextCompat.getColor(this, R.color.Black));
                }
                break;
            case R.id.signUp:
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                break;
        }

    }
}

package com.ankit.roversapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import info.androidhive.fontawesome.FontDrawable;

public class MainActivity extends AppCompatActivity {

    //Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        FontDrawable navIcon = new FontDrawable(this, R.string.menu, true, false);
        navIcon.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        navIcon.setTextSize(20f);
        toolbar.setNavigationIcon(navIcon);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
    }

    private void logoutAction() {
        if(mAuth.getCurrentUser() != null) {
            mAuth.signOut();
            Toast.makeText(this, "Signing Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logoutAction();
                return true;
            default:
                Log.d("MenuItem", item.toString());
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        FontDrawable logoutIcon = new FontDrawable(this, R.string.fa_sign_out_alt_solid, true, false);
        logoutIcon.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        logoutIcon.setTextSize(20f);
        menu.findItem(R.id.logout).setIcon(logoutIcon);
        return super.onPrepareOptionsMenu(menu);
    }
}

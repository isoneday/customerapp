package com.imastudio.customerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.imastudio.customerapp.helper.SessionManager;

public class MainActivity extends AppCompatActivity {

    private SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = new SessionManager(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.mn_logout){
            manager.logout();
            finish();
            startActivity(new Intent(MainActivity.this,LoginRegisterActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void onHistory(View view) {

    }

    public void onGoride(View view) {
    startActivity(new Intent(this,MapsActivity.class));
    }
}

package com.abhishek.graphmugsdemo.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.abhishek.graphmugsdemo.R;
import com.abhishek.graphmugsdemo.fragments.GraphFragment;
import com.abhishek.graphmugsdemo.fragments.PieChartFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    //Variables
    BottomNavigationView navigationView;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialisation
        navigationView = findViewById(R.id.bottomNavigation);
        fragment = new GraphFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.bodyContainer, fragment).commit();

        navigationView.setOnItemSelectedListener(item -> {
            fragment = null;
            switch (item.getItemId()) {
                case R.id.graph:
                    fragment = new GraphFragment();
                    break;
                case R.id.pieChart:
                    fragment = new PieChartFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.bodyContainer, fragment).commit();
            return true;
        });
    }
}
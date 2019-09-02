package com.example.procurement;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    public static FragmentManager fm;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeActivity.fm.beginTransaction().replace(R.id.fragment_container,new DashboardFragment(), null).commit();
                    return true;
                case R.id.navigation_dashboard:
                    HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderFragment(),null).commit();
                    return true;
                case R.id.navigation_notifications:
                    HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new ProfileFragment(), null).commit();
                    return true;
                case R.id.navigation_noti:
                    HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new NotificationFragment(), null).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm = getSupportFragmentManager();

        if(findViewById(R.id.fragment_container)!=null) {
            if(savedInstanceState!=null) {
                return;
            }
            FragmentTransaction ft = fm.beginTransaction();
            DashboardFragment df = new DashboardFragment();
            ft.add(R.id.fragment_container,df,null);
            ft.commit();
        }

    }

}

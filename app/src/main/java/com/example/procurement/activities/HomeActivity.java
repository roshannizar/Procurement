package com.example.procurement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.procurement.R;
import com.example.procurement.fragments.DashboardFragment;
import com.example.procurement.fragments.OrderStatusFragment;
import com.example.procurement.fragments.ProfileFragment;
import com.example.procurement.fragments.RequisitionViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    public static FragmentManager fm;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new DashboardFragment(), null).commit();
                    return true;
                case R.id.navigation_order:
                    HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderStatusFragment(), null).commit();
                    return true;
                case R.id.navigation_profile:
                    HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new ProfileFragment(), null).commit();
                    return true;
                case R.id.navigation_requisition:
                    HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new RequisitionViewFragment(),null).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.signout) {
            SignOutUser();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm = getSupportFragmentManager();

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            FragmentTransaction ft = fm.beginTransaction();
            DashboardFragment df = new DashboardFragment();
            //InventoryDialog df = new InventoryDialog();
            ft.add(R.id.fragment_container, df, null);
            ft.commit();
        }

    }

    public void SignOutUser() {
        SignInActivity.SignOutUserFirebase();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}

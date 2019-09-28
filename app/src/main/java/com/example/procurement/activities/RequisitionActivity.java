package com.example.procurement.activities;

import android.os.Bundle;

import com.example.procurement.R;
import com.example.procurement.fragments.RequisitionActivityFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

public class RequisitionActivity extends AppCompatActivity {

    public static FragmentManager fm2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        fm2 = getSupportFragmentManager();

        if (findViewById(R.id.fragment_container_requisition) != null) {
            if (savedInstanceState != null) {
                return;
            }
            FragmentTransaction ft1 = fm2.beginTransaction();
            RequisitionActivityFragment df1 = new RequisitionActivityFragment();
            ft1.add(R.id.fragment_container_requisition, df1, null);
            ft1.commit();
        }
    }

}

package com.example.procurement.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.activities.HomeActivity;

public class CreatePurchaseOrderFragment extends Fragment {

    private EditText etCompany, etVendor;
    private CardView cvTotal;
    private RecyclerView rvItem;
    private TextView addProduct;
    private Button btnGenerate;
    private ImageView btnBack;

    public CreatePurchaseOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_purchase_order, container, false);

        btnBack = rootView.findViewById(R.id.btnBack);
        etCompany = rootView.findViewById(R.id.etCompany);
        etVendor = rootView.findViewById(R.id.etVendor);
        cvTotal = rootView.findViewById(R.id.cvTotal);
        rvItem = rootView.findViewById(R.id.rvItemView);
        addProduct = rootView.findViewById(R.id.addProduct);
        btnGenerate = rootView.findViewById(R.id.btnGenerate);
        addProduct = rootView.findViewById(R.id.addProduct);
        getBack();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getBack() {
        btnBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new RequisitionViewFragment(), null).commit();
                    }
                }
        );
    }
}

package com.example.procurement.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;

public class CreatePurchaseOrderFragment extends Fragment {

    private EditText etCompany, etVendor;
    private CardView cvTotal;
    private RecyclerView rvItem;
    private TextView addProduct;
    private Button btnGenerate;

    public CreatePurchaseOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_purchase_order, container, false);

        etCompany = rootView.findViewById(R.id.etCompany);
        etVendor = rootView.findViewById(R.id.etVendor);
        cvTotal = rootView.findViewById(R.id.cvTotal);
        rvItem = rootView.findViewById(R.id.rvItemView);
        addProduct = rootView.findViewById(R.id.addProduct);
        btnGenerate = rootView.findViewById(R.id.btnGenerate);
        addProduct = rootView.findViewById(R.id.addProduct);

        return rootView;
    }
}

package com.example.procurement.fragments;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.procurement.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class RequisitionActivityFragment extends Fragment {

    public RequisitionActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requisition, container, false);
    }
}

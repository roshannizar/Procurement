package com.example.procurement.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.activities.HomeActivity;
import com.example.procurement.models.Order;
import com.example.procurement.models.Requisition;
import com.example.procurement.utils.CommonConstants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;

import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;

public class CreatePurchaseOrderFragment extends Fragment {

    private Spinner spCompany, spVendor;
    private TextView txtOrderId, txtRequisitionId, txtOrderName, txtDeliveryDate,
            txtDescription, txtStatusView, txtSubTotal, txtTax, txtTotal, txtOrderedDate;
    private Button btnUpdate;
    private CardView cvTotal;
    private RecyclerView productItem;
    private TextView addProduct;
    private Button btnGenerate;
    private ImageView btnBack;
    private String requisitionKey;
    private DocumentReference requisitionRef;
    private CollectionReference orderDbRef;
    private Requisition requisition;

    public CreatePurchaseOrderFragment(String requisitionKey) {
        this.requisitionKey = requisitionKey;
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

        requisitionRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_REQUISITION).document(requisitionKey);
        orderDbRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_ORDER);

        btnBack = rootView.findViewById(R.id.btnBack);
        spCompany = rootView.findViewById(R.id.spCompany);
        spVendor = rootView.findViewById(R.id.spVendor);

        txtOrderId = rootView.findViewById(R.id.txtOrderId);
        txtRequisitionId = rootView.findViewById(R.id.txtRequisitionId);
        txtOrderName = rootView.findViewById(R.id.txtOrderName);
        txtOrderedDate = rootView.findViewById(R.id.txtOrderedDate);
        txtDeliveryDate = rootView.findViewById(R.id.txtDeliveryDate);
        txtDescription = rootView.findViewById(R.id.txtDescription);
        txtStatusView = rootView.findViewById(R.id.txtStatusView);
        txtSubTotal = rootView.findViewById(R.id.txtSubTotal);
        txtTax = rootView.findViewById(R.id.txtTax);
        txtTotal = rootView.findViewById(R.id.txtTotal);


        cvTotal = rootView.findViewById(R.id.cvTotal);
        cvTotal.setVisibility(View.GONE);

        productItem = rootView.findViewById(R.id.rvItemView);
        addProduct = rootView.findViewById(R.id.addProduct);
        btnGenerate = rootView.findViewById(R.id.btnGenerate);
        //addProduct = rootView.findViewById(R.id.addProduct);
        getBack();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.create_order_menu, menu);
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

    private void readStatusData() {
        requisitionRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                requisition = new Requisition();
                requisition = documentSnapshot.toObject(Requisition.class);

                if (requisition != null) {

                 //   txtOrderId.setText(order.getOrderID());
                    txtRequisitionId.setText(requisition.getRequisitionNo());
//                    txtOrderName.setText(order.getOrderName());
                    txtOrderedDate.setText(new Date().toString());
                    txtDeliveryDate.setText(requisition.getDeliveryDate());
//                    txtDescription.setText(order.getDescription());
                   // txtStatusView.setText(orderStatus);

//                    double subTotal = order.getSubTotal();
//                    double tax = subTotal * 0.10;
//                    double total = subTotal + tax;
//                    txtSubTotal.setText(String.valueOf(subTotal));
//                    txtTax.setText(String.valueOf(tax));
//                    txtTotal.setText(String.valueOf(total));
                }
            }
        });
    }

    private void EditOrder() {
        btnUpdate.setText("Edit");
        btnUpdate.setBackgroundResource(R.drawable.badge_approved);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}

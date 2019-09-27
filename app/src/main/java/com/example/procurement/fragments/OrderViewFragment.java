package com.example.procurement.fragments;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.R;
import com.example.procurement.activities.HomeActivity;
import com.example.procurement.adapters.InventoryAdapter;
import com.example.procurement.models.Inventory;
import com.example.procurement.models.Order;
import com.example.procurement.utils.CommonConstants;
import com.example.procurement.utils.FileUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;
import static com.example.procurement.utils.CommonConstants.GENERATE_ORDER_FRAGMENT_TAG;
import static com.example.procurement.utils.CommonConstants.ORDER_EDIT_FRAGMENT_TAG;

public class OrderViewFragment extends Fragment {
    private static final String TAG = "OrderViewFragment";

    private String orderKey;
    private Order order;
    private CollectionReference orderDBRef;
    private ImageView btnBack;
    private RecyclerView productItemView;
    private EditText etCompany, etVendor;
    private TextView txtOrderId, txtRequisitionId, txtDeliveryDate,
            txtDescription, txtStatusView, txtSubTotal, txtTax, txtTotal, txtOrderedDate;
    private Button btnUpdate;
    private ArrayList<Inventory> iInventory;
    private InventoryAdapter inventoryAdapter;
    private Inventory i;
    private Font titleFont, blackFont;
    private Document document;
    private Context mContext;

    public OrderViewFragment(String orderKey) {
        this.orderKey = orderKey;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_purchase_order_view, container, false);

        orderDBRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_ORDER);
        mContext = getActivity();

        btnBack = rootView.findViewById(R.id.btnBack);
        txtOrderId = rootView.findViewById(R.id.txtOrderId);
        txtRequisitionId = rootView.findViewById(R.id.txtRequisitionId);
        txtOrderedDate = rootView.findViewById(R.id.txtOrderedDate);
        txtDeliveryDate = rootView.findViewById(R.id.txtDeliveryDate);
        txtDescription = rootView.findViewById(R.id.txtDescription);
        txtStatusView = rootView.findViewById(R.id.txtStatusView);
        txtSubTotal = rootView.findViewById(R.id.txtSubTotal);
        txtTax = rootView.findViewById(R.id.txtTax);
        txtTotal = rootView.findViewById(R.id.txtTotal);
        etCompany = rootView.findViewById(R.id.etCompany);
        etVendor = rootView.findViewById(R.id.etVendor);
        btnUpdate = rootView.findViewById(R.id.btnUpdate);

        iInventory = new ArrayList<>();
        inventoryAdapter = new InventoryAdapter(getActivity(), iInventory);

        productItemView = rootView.findViewById(R.id.rvItemView);
        productItemView.setLayoutManager(new LinearLayoutManager(getActivity()));
        productItemView.setItemAnimator(new DefaultItemAnimator());

        btnUpdate.setText("Delete");
        btnUpdate.setBackgroundResource(R.drawable.badge_denied);

        DeleteOrder();
        readData();
        //WriteDataValues();
        getBack();

        return rootView;
    }

    private void WriteDataValues() {

        for (int j = 1; j <= 5; j++) {
            i = new Inventory(String.valueOf(j), "Sand Heap", "", 7, 2);
            iInventory.add(i);
        }


        inventoryAdapter = new InventoryAdapter(getActivity(), iInventory);
        productItemView.setAdapter(inventoryAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.view_order_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_download) {
            try {
                String destinationPath = FileUtils.getAppPath(mContext) + order.getOrderID() + "--> " + order.getRequisitionID() + ".pdf";
                createPdf(order, destinationPath);
            } catch (IllegalArgumentException e) {
                Log.w(GENERATE_ORDER_FRAGMENT_TAG, "Error writing document", e);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void getBack() {
        btnBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderStatusFragment(), null).commit();
                    }
                }
        );
    }

    private void readData() {
        orderDBRef.document(orderKey).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                order = new Order();
                order = documentSnapshot.toObject(Order.class);

                if (order != null) {

                    String orderStatus = order.getOrderStatus();

                    etVendor.setText(order.getVendor());
                    etCompany.setText(order.getCompany());
                    txtOrderId.setText(order.getOrderID());
                    txtRequisitionId.setText(order.getRequisitionID());
                    txtOrderedDate.setText(order.getOrderedDate());
                    txtDeliveryDate.setText(order.getDeliveryDate());
                    txtDescription.setText(order.getDescription());
                    txtStatusView.setText(orderStatus);

                    switch (orderStatus) {
                        case CommonConstants.ORDER_STATUS_APPROVED:
                            txtStatusView.setBackgroundResource(R.drawable.badge_approved);
                            changeStatusOrder();
                            break;
                        case CommonConstants.ORDER_STATUS_PENDING:
                            txtStatusView.setBackgroundResource(R.drawable.badge_pending);
                            break;
                        case CommonConstants.ORDER_STATUS_PLACED:
                            txtStatusView.setBackgroundResource(R.drawable.badge_placed);
                            break;
                        case CommonConstants.ORDER_STATUS_HOLD:
                            txtStatusView.setBackgroundResource(R.drawable.badge_hold);
                            EditOrder();
                            break;
                        case CommonConstants.ORDER_STATUS_DRAFT:
                            txtStatusView.setBackgroundResource(R.drawable.badge_draft);
                            EditOrder();
                            break;
                        default:
                            txtStatusView.setBackgroundResource(R.drawable.badge_denied);
                    }

                    double subTotal = order.getSubTotal();
                    double tax = subTotal * 0.10;
                    double total = subTotal + tax;
                    txtSubTotal.setText(String.valueOf(subTotal));
                    txtTax.setText(String.valueOf(tax));
                    txtTotal.setText(String.valueOf(total));
                }
            }
        });
    }

    private void changeStatusOrder() {
        btnUpdate.setText("Mark As Placed");
        btnUpdate.setBackgroundResource(R.drawable.badge_placed);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.setOrderStatus(getString(R.string.placed));
                orderDBRef.document(order.getOrderKey())
                        .set(order)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderViewFragment(order.getOrderKey()), null).commit();
                                Log.d(ORDER_EDIT_FRAGMENT_TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(ORDER_EDIT_FRAGMENT_TAG, "Error writing document", e);
                            }
                        });
            }
        });
    }

    private void EditOrder() {
        btnUpdate.setText("Edit");
        btnUpdate.setBackgroundResource(R.drawable.badge_approved);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new EditOrderFragment(order), null).commit();
            }
        });
    }

    private void DeleteOrder() {
        if (btnUpdate.getText().equals("Delete")) {

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to Delete order?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    orderDBRef.document(orderKey)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderStatusFragment(), null).commit();
                                                    Toast.makeText(getActivity(), "Order Deleted Successfully", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error deleting document", e);
                                                }
                                            });
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
            });
        }
    }

    private void createPdf(Order order, String dest) {

        if (new File(dest).exists()) {
            new File(dest).delete();
        }

        try {
            /**
             * Creating Document
             */
            document = new Document();
            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(dest));

            // Open to write
            document.open();

            // Document Settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("S.Praveenkumar");
            document.addCreator("S.Praveenkumar");

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));


            /***
             * Variables for further use....
             */
            BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
            float mTitleFontSize = 18.0f;
            float mValueFontSize = 14.0f;

            BaseFont urName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);

            titleFont = new Font(urName, mTitleFontSize, Font.NORMAL, BaseColor.BLACK);
            blackFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);

            Paragraph emptyParagraph = new Paragraph("");
            // Title Order Details...
            Paragraph titleParagraph = new Paragraph(new Chunk("Purchase Order Details", titleFont));
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph);
            document.add(emptyParagraph);

            createParagraph("Vendor : " + order.getVendor());
            createParagraph("Company : " + order.getCompany());

            document.add(emptyParagraph);
            document.add(new Chunk(lineSeparator));
            document.add(emptyParagraph);

            createParagraph("Order ID : " + order.getOrderID());
            document.add(emptyParagraph);

            createParagraph("Requisition ID : " + order.getRequisitionID());
            document.add(emptyParagraph);
            document.add(emptyParagraph);

            createParagraph("Ordered Date : " + order.getOrderedDate());
            createParagraph("Delivery Date : " + order.getDeliveryDate());
            document.add(emptyParagraph);
            document.add(emptyParagraph);

            createParagraph("Description : " + order.getDescription());
            document.add(emptyParagraph);
            document.add(new Chunk(lineSeparator));
            document.add(emptyParagraph);


            document.close();
            FileUtils.openFile(mContext, new File(dest));

            Toast.makeText(mContext, "PDF Created Successfully... :)", Toast.LENGTH_SHORT).show();

        } catch (IOException | DocumentException ie) {
            Log.w(GENERATE_ORDER_FRAGMENT_TAG, "Error writing document", ie);
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(mContext, "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }

    private void createParagraph(String value) {
        try {
            Paragraph paragraph = new Paragraph(new Chunk(value, blackFont));
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.w(GENERATE_ORDER_FRAGMENT_TAG, "Error writing document", e);
        }
    }

}

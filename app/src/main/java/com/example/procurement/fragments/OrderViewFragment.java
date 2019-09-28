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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
import java.text.DecimalFormat;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.procurement.activities.SignInActivity.siteManagerDBRef;

public class OrderViewFragment extends Fragment {
    private String orderKey;
    private Order order;
    private DocumentReference orderDBRef;
    private ImageView btnBack;
    private RecyclerView productItemView;
    private EditText etCompany, etVendor;
    private TextView txtOrderId, txtRequisitionId, txtDeliveryDate,
            txtDescription, txtStatusView, txtSubTotal, txtTax, txtTotal, txtOrderedDate;
    private Button btnUpdate;
    private ArrayList<Inventory> inventoryList;
    private InventoryAdapter adapter;
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

        orderDBRef = siteManagerDBRef.collection(CommonConstants.COLLECTION_ORDER).document(orderKey);
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

        inventoryList = new ArrayList<>();
        adapter = new InventoryAdapter(getActivity(), inventoryList, "Order");

        productItemView = rootView.findViewById(R.id.rvItemView);
        productItemView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        productItemView.setItemAnimator(new DefaultItemAnimator());

        btnUpdate.setText("Delete");
        btnUpdate.setBackgroundResource(R.drawable.badge_denied);

        DeleteOrder();
        readData();
        getBack();

        return rootView;
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
                Log.w(TAG, "Error writing document", e);
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
        orderDBRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

                    DecimalFormat df = new DecimalFormat("#.##");
                    double subTotal = order.getSubTotal();
                    double tax = subTotal * 0.10d;
                    double total = subTotal + tax;
                    txtSubTotal.setText(String.valueOf(subTotal));
                    txtTax.setText((df.format(tax)));
                    txtTotal.setText(String.valueOf(total));

                }
            }
        });

        orderDBRef.collection(CommonConstants.COLLECTION_INVENTORIES).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                }

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Inventory inventory = document.toObject(Inventory.class);
                        inventoryList.add(inventory);
                        System.out.println(inventory.getItemName());
                    }


                    if (inventoryList != null) {
                        adapter = new InventoryAdapter(mContext, inventoryList, "Order");
//                        progressBar.setVisibility(View.GONE);
//                        imgLoader.setVisibility(View.INVISIBLE);
//                        txtLoader.setVisibility(View.INVISIBLE);
//                        txtWait.setVisibility(View.INVISIBLE);
                        productItemView.setAdapter(adapter);

//                        if (orders.size() == 0) {
//                            imgLoader.refreshDrawableState();
//                            imgLoader.setImageResource(R.drawable.ic_safebox);
//                            imgLoader.setVisibility(View.VISIBLE);
//                            txtLoader.setVisibility(View.VISIBLE);
//                            txtWait.setVisibility(View.VISIBLE);
//                            txtLoader.setText("Purchase Order is empty!");
//                            txtWait.setText("No point in waiting!");
//                        }
                    }

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
                orderDBRef.set(order)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                HomeActivity.fm.beginTransaction().replace(R.id.fragment_container, new OrderViewFragment(order.getOrderKey()), null).commit();
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
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
                                    orderDBRef.delete()
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
            Log.w(TAG, "Error writing document", ie);
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(mContext, "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }

    private void createParagraph(String value) {
        try {
            Paragraph paragraph = new Paragraph(new Chunk(value, blackFont));
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.w(TAG, "Error writing document", e);
        }
    }

}

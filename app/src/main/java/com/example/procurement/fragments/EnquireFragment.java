package com.example.procurement.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.procurement.PMS;
import com.example.procurement.R;
import com.example.procurement.adapters.EnquireAdapter;
import com.example.procurement.models.Enquire;
import com.example.procurement.utils.CommonConstants;
import com.example.procurement.utils.RecyclerTouchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.procurement.utils.CommonConstants.ENQUIRE_FRAGMENT_TAG;

public class EnquireFragment extends Fragment {

    private EnquireAdapter mAdapter;
    private List<Enquire> enquireList;
    private RecyclerView recyclerView;
    private Context mContext;
    private FloatingActionButton fab;
    private DatabaseReference enquireDatabaseRef;
    private ProgressBar progressBar;
    private String orderKey;

    public EnquireFragment(String orderKey) {
        this.orderKey = orderKey;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_enquire, container, false);
        mContext = getContext();
        recyclerView = rootView.findViewById(R.id.rvLoading);
        progressBar = rootView.findViewById(R.id.progressBar);

        enquireDatabaseRef = PMS.DatabaseRef
                .child(CommonConstants.FIREBASE_ORDER_DB)
                .child(orderKey)
                .child(CommonConstants.COLLECTION_ENQUIRIES)
                .getRef();

        enquireList = new ArrayList<>();
        mAdapter = new EnquireAdapter(mContext, enquireList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

        fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBubbleDialog( null, -1);
            }
        });

        readNotesData();
        return rootView;
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence options[] = new CharSequence[]{"Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Enquiry Options");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    deleteNote(position);
                }
            }
        });
        builder.show();
    }

    /**
     * Inserting new note in db
     * and refreshing the list
     */
    private void createNote(String enquiry) {
        // inserting note in db and getting
        DatabaseReference reference = enquireDatabaseRef.push();
        reference.setValue(new Enquire(reference.getKey(), enquiry, DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date())));
    }


    /**
     * Deleting note from SQLite and removing the
     * item from the list by its position
     */
    private void deleteNote(int position) {
        // deleting the note from db
        String id = enquireList.get(position).getKey();
        enquireDatabaseRef.child(id).removeValue();
    }


    private void readNotesData() {
        enquireDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                enquireList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Enquire enquire = data.getValue(Enquire.class);
                    enquireList.add(enquire);
                }

                if (enquireList != null) {
                    mAdapter = new EnquireAdapter(mContext, enquireList);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setAdapter(mAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(ENQUIRE_FRAGMENT_TAG, "Failed to read value.", error.toException());
            }
        });
    }

    /**
     * Shows alert dialog with EditText options to enter / edit
     * a note.
     * when shouldUpdate=true, it automatically displays old note and changes the
     * button text to UPDATE
     */
    private void showBubbleDialog(final Enquire enquire, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mContext);
        View view = layoutInflaterAndroid.inflate(R.layout.layout_add_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(mContext);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputText = view.findViewById(R.id.note);
        inputText.setHint(R.string.hint_enter_enquiry);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(getString(R.string.lbl_new_enquiry_title));

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(CommonConstants.SAVE_STRING, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton(CommonConstants.CANCEL_STRING,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputText.getText().toString())) {
                    Toast.makeText(mContext, "Enter enquiry!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // create new note
                createNote(inputText.getText().toString());

            }
        });
    }
}

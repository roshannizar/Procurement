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
import com.example.procurement.adapters.NoteAdapter;
import com.example.procurement.models.Note;
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

import static com.example.procurement.utils.CommonConstants.NOTE_FRAGMENT_TAG;

public class NoteFragment extends Fragment {
    private NoteAdapter mAdapter;
    private List<Note> notesList;
    private RecyclerView recyclerView;
    private Context mContext;
    private DatabaseReference notesDatabaseRef;
    private ProgressBar progressBar;
    private String orderKey;

    public NoteFragment(String orderKey) {
        this.orderKey = orderKey;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        mContext = getContext();
        recyclerView = rootView.findViewById(R.id.rvLoading);
        progressBar = rootView.findViewById(R.id.progressBar);


        notesDatabaseRef = PMS.DatabaseRef
                .child(CommonConstants.FIREBASE_ORDER_DB)
                .child(orderKey)
                .child(CommonConstants.FIREBASE_NOTES_DB)
                .getRef();

        notesList = new ArrayList<>();
        mAdapter = new NoteAdapter(mContext, notesList);

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

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBubbleDialog(false, null, -1);
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
        CharSequence options[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Choose option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showBubbleDialog(true, notesList.get(position), position);
                } else {
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
    private void createNote(String note) {
        // inserting note in db and getting
        DatabaseReference reference = notesDatabaseRef.push();
        reference.setValue(new Note(reference.getKey(), note, DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date())));
    }

    /**
     * Updating note in db and updating
     * item in the list by its position
     */
    private void updateNote(String noteText, int position) {
        // updating note text
        String id = notesList.get(position).getKey();
        notesDatabaseRef.child(id).setValue(new Note(id, noteText, DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date())));
    }

    /**
     * Deleting note from SQLite and removing the
     * item from the list by its position
     */
    private void deleteNote(int position) {
        // deleting the note from db
        String id = notesList.get(position).getKey();
        notesDatabaseRef.child(id).removeValue();
    }


    private void readNotesData() {
        notesDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                notesList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Note note = data.getValue(Note.class);
                    notesList.add(note);
                }

                if (notesList != null) {
                    mAdapter = new NoteAdapter(mContext, notesList);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setAdapter(mAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(NOTE_FRAGMENT_TAG, "Failed to read value.", error.toException());
            }
        });
    }

    /**
     * Shows alert dialog with EditText options to enter / edit
     * a note.
     * when shouldUpdate=true, it automatically displays old note and changes the
     * button text to UPDATE
     */
    private void showBubbleDialog(final boolean shouldUpdate, final Note note, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mContext);
        View view = layoutInflaterAndroid.inflate(R.layout.layout_add_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(mContext);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputText = view.findViewById(R.id.note);
        inputText.setHint(R.string.hint_enter_enquiry);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title)
                : getString(R.string.lbl_edit_note_title));

        if (shouldUpdate && note != null) {
            inputText.setText(note.getNote());
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? CommonConstants.UPDATE_STRING
                        : CommonConstants.SAVE_STRING, new DialogInterface.OnClickListener() {
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
                    Toast.makeText(mContext, "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && note != null) {
                    // update note by it's id
                    updateNote(inputText.getText().toString(), position);
                } else {
                    // create new note
                    createNote(inputText.getText().toString());
                }
            }
        });
    }
}

package com.example.procurement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.procurement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.procurement.utils.CommonConstants.COLLECTION_SITE_MANGER;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "";
    public static String name = "", email="" ,uid="";
    Button btnSignIn;
    CheckBox cbShowPassword;
    EditText txtEmail, txtPassword;
    private FirebaseAuth mAuth;
    public static DocumentReference siteManagerDBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signin);

        btnSignIn = findViewById(R.id.btnSignIn);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        cbShowPassword = findViewById(R.id.cbShowPassword);

        mAuth = FirebaseAuth.getInstance();

        getHome();
        showPassword();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void SignIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG,"signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            getCurrentUser();
                            updateUI(user);
                            Intent i = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Log.d(TAG,"signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication Failed.",Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null) {
            Intent i = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void showPassword() {
        cbShowPassword.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(cbShowPassword.isChecked()) {
                            txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        } else {
                            txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }
                    }
                }
        );
    }

    private void getHome() {
        btnSignIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = txtEmail.getText().toString();
                        String password = txtPassword.getText().toString();

                        SignIn(email, password);
                    }
                }
        );
    }

    public static void getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user !=null) {
            name = user.getDisplayName();
            email = user.getEmail();
            uid = user.getUid();
            boolean emailVerified = user.isEmailVerified();
            siteManagerDBRef = FirebaseFirestore.getInstance().collection(COLLECTION_SITE_MANGER).document(uid);
        }
    }

    public static void SignOutUserFirebase() {
        FirebaseAuth.getInstance().signOut();
    }
}

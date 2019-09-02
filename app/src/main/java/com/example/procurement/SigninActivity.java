package com.example.procurement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SigninActivity extends AppCompatActivity {

    Button btnSignIn;
    CheckBox cbShowPassword;
    EditText txtEmail, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signin);

        btnSignIn = findViewById(R.id.btnSignIn);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        cbShowPassword = findViewById(R.id.cbShowPassword);

        getHome();
        showPassword();
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
                        Intent i = new Intent(SigninActivity.this,HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
        );
    }
}

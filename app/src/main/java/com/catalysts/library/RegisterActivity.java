package com.catalysts.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class RegisterActivity extends AppCompatActivity {
    EditText txtUsername;
    EditText txtFullName;
    EditText txtEmail;
    EditText txtPassword;

    RadioGroup radioGroupGender;
    String txtGender;

    Button btnRegister;

    TextView txtAlreadyUser;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtUsername = findViewById(R.id.txt_username);
        txtFullName = findViewById(R.id.txt_full_name);
        txtEmail = findViewById(R.id.txt_email);
        txtPassword = findViewById(R.id.txt_password);

        radioGroupGender = findViewById(R.id.gender_radio_group);

        btnRegister = findViewById(R.id.btn_register);
        txtAlreadyUser = findViewById(R.id.txt_already_a_user);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
                txtGender = radioButton.getText().toString();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString();
                String fullName = txtFullName.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                String gender = txtGender;


                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(username)
                        || TextUtils.isEmpty(username) || TextUtils.isEmpty(username)) {
                    makeText(RegisterActivity.this, "Empty credentials!", LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    makeText(RegisterActivity.this, "Password too short!", LENGTH_SHORT).show();
                } else if (gender == null) {
                    makeText(RegisterActivity.this, "No gender selected!", LENGTH_SHORT).show();

                } else {
                    registerUser(username, fullName, email, password, gender);
                }

            }
        });

        txtAlreadyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerUser(final String username, final String fullName, final String email, final String password, final String gender) {
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
                map.put("username", username);
                map.put("fullName", fullName);
                map.put("email", email);
                map.put("password", password);
                map.put("gender", gender);
                // TODO - ce campuri mai adaugam in baza de date pentru un USER?

                databaseReference
                        .child("Users")
                        .child(firebaseAuth.getCurrentUser().getUid())
                        .setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    makeText(RegisterActivity.this, "Successfully registered user!", LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                makeText(RegisterActivity.this, e.getMessage(), LENGTH_SHORT).show();
            }
        });
    }
}
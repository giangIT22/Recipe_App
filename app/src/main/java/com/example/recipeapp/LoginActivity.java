package com.example.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipeapp.fragment.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUserInfo;
    private EditText userName;
    private EditText password;
    private ArrayList<Account> accounts;
    private Button btnLogin;
    private TextView btnRegister;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (EditText) findViewById(R.id.txtUserName);
        password = (EditText) findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btn_login);
        login();

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void login()
    {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accounts = new ArrayList<>();
                mDatabase = FirebaseDatabase.getInstance().getReference("Account");
                mDatabase.addValueEventListener(new ValueEventListener()  {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int flag = 0;
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            Account account = dataSnapshot.getValue(Account.class);
                            account.setKey(dataSnapshot.getKey());
                            accounts.add(account);
                        }

                        for (Account account: accounts) {
                            if (account.getUsername().equals(userName.getText().toString()) == true
                                    && account.getPass().equals(password.getText().toString()) == true) {
                                flag = 1;
                                key = account.getKey();
                                break;
                            }
                        }
                        if (flag == 1) {
                            mDatabaseUserInfo = FirebaseDatabase.getInstance().getReference();
                            mDatabaseUserInfo.child("user_name").setValue(userName.getText().toString());
                            mDatabaseUserInfo.child("user_id").setValue(key);
                            Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}
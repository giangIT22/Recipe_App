package com.example.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipeapp.fragment.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private EditText userName;
    private EditText password;
    private ArrayList<Account> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (EditText) findViewById(R.id.txtUserName);
        password = (EditText) findViewById(R.id.txtPassword);
    }

    public void btn_login(View view) {
        this.accounts = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("Account");
        mDatabase.addValueEventListener(new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int flag = 0;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Account account = dataSnapshot.getValue(Account.class);
                    accounts.add(account);
                }

                for (Account account: accounts) {
                    if (account.getUsername().equals(userName.getText().toString()) == true
                            && account.getPass().equals(password.getText().toString()) == true) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 1) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userName", userName.getText().toString());
                    intent.putExtra("data", bundle);
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

    public void btn_Register(View view) {
        Account account = new Account(userName.getText().toString(), password.getText().toString());
        String myCurrentDateTime = DateFormat.getDateTimeInstance().
                format((Calendar.getInstance().getTime()));
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Account");
        mDatabase.child(myCurrentDateTime).setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
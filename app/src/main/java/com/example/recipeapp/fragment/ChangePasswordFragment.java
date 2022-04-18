package com.example.recipeapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipeapp.Account;
import com.example.recipeapp.LoginActivity;
import com.example.recipeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePasswordFragment extends Fragment {
    private EditText currentPass;
    private EditText newPass;
    private EditText rePass;
    private Button btnChangePass;
    private DatabaseReference mDataBase;
    private DatabaseReference databaseReferenceName;
    private DatabaseReference databaseReferenceId;
    private String userName;
    private String userId;
    private String oldPass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_changepassword, container, false);
        currentPass = view.findViewById(R.id.txt_current_pass);
        newPass = view.findViewById(R.id.txt_new_password);
        rePass = view.findViewById(R.id.txtRePassword);
        btnChangePass = view.findViewById(R.id.btn_change_pass);

        databaseReferenceName = FirebaseDatabase.getInstance().getReference("user_name");
        databaseReferenceName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferenceId = FirebaseDatabase.getInstance().getReference("user_id");
        databaseReferenceId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userId = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        changPassword();
        getActivity().setTitle("Thay đổi mật khẩu");

        return view;
    }

    public void changPassword()
    {
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataBase = FirebaseDatabase.getInstance().getReference("Account").child(userId);
                mDataBase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        oldPass = snapshot.getValue(Account.class).getPass();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if (currentPass.getText().toString().trim().equals(oldPass) == false) {
                    Toast.makeText(getActivity(), "Mật khẩu hiện tại không chính xác", Toast.LENGTH_SHORT).show();
                } else if (rePass.getText().toString().trim().equals(newPass.getText().toString()) == false) {
                    Toast.makeText(getActivity(), "Mật khẩu nhập lại không chính xác", Toast.LENGTH_SHORT).show();
                } else {
                    Account account = new Account(userName,newPass.getText().toString().trim());
                    mDataBase.setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ChangePasswordFragment.this.getContext(), LoginActivity.class);
                            startActivity(i);
                        }
                    });
                }
            }
        });
    }

}
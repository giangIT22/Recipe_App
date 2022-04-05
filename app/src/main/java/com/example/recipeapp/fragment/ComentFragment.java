package com.example.recipeapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipeapp.Comment;
import com.example.recipeapp.CommentAdapter;
import com.example.recipeapp.R;
import com.example.recipeapp.UploadRecipe;
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

public class ComentFragment extends Fragment {
    private DatabaseReference databaseReference;
    private DatabaseReference getComment;
    private ArrayList<Comment> comments;
    private Button btnAddComment;
    private EditText content;
    private String userNameCurrent;
    private CommentAdapter commentAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        btnAddComment = (Button) view.findViewById(R.id.btn_add_comment);
        content = (EditText) view.findViewById(R.id.edit_text_comment);

        databaseReference = FirebaseDatabase.getInstance().getReference("user_name");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userNameCurrent = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getActivity().getIntent().getBundleExtra("data");
                Comment comment = new Comment(content.getText().toString(), userNameCurrent);
                comment.setRecipeId(bundle.getString("key"));
                String myCurrentDateTime = DateFormat.getDateTimeInstance().
                        format((Calendar.getInstance().getTime()));
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Comments");
                mDatabase.child(myCurrentDateTime).setValue(comment);
            }
        });
        showComments(view);
        return view;
    }

    public void showComments(View view)
    {
        comments = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyler_view_comment);
        commentAdapter = new CommentAdapter(comments, getActivity());
        recyclerView.setAdapter(commentAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        getComment = FirebaseDatabase.getInstance().getReference("Comments");
        Bundle bundle = getActivity().getIntent().getBundleExtra("data");
        
        getComment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.clear();
                for (DataSnapshot item:snapshot.getChildren()) {
                    Comment comment = item.getValue(Comment.class);
                    if (comment.getRecipeId().equals(bundle.getString("key"))) {
                        comments.add(comment);
                    }
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
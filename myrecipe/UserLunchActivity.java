package com.example.user.myrecipe;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserLunchActivity extends AppCompatActivity {


    private RecyclerView lunch_List;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private Query mQuery;
    private String currentuserid;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_lunch);

        mAuth = FirebaseAuth.getInstance();

        currentuserid = mAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Lunch");
        mQuery = mDatabase.orderByChild("UserID").equalTo(currentuserid);

        lunch_List = (RecyclerView) findViewById(R.id.user_lunch);
        lunch_List.setHasFixedSize(true);
        lunch_List.setLayoutManager(new LinearLayoutManager(UserLunchActivity.this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<Lunch, UserLunchActivity.ULunchViewHolder> firebaseRecyclerAdapterU;

        firebaseRecyclerAdapterU = new FirebaseRecyclerAdapter<Lunch, UserLunchActivity.ULunchViewHolder>(
                Lunch.class,
                R.layout.list_of_recipe,
                UserLunchActivity.ULunchViewHolder.class,
                mQuery) {
            @Override
            protected void populateViewHolder(UserLunchActivity.ULunchViewHolder viewHolder, Lunch model, int position) {
                final String post_key = getRef(position).getKey();

                viewHolder.setRecipename(model.getRecipeName());
                viewHolder.setDuration(model.getDuration());
                viewHolder.setServing(model.getServing());
                viewHolder.setImage(getApplicationContext(), model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserLunchActivity.this, SingleRecipeActivity.class);
                        intent.putExtra("lunch_id", post_key);
                        startActivity(intent);
                    }
                });
            }

        };

        lunch_List.setAdapter(firebaseRecyclerAdapterU);


    }
    private static class ULunchViewHolder extends RecyclerView.ViewHolder {


        View mView;

        public ULunchViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setRecipename(String recipename) {
            TextView post_recipename = (TextView) mView.findViewById(R.id.list_recipename);
            post_recipename.setText("Recipe Name: " + recipename);
        }

        public void setDuration(String duration) {
            TextView post_duration = (TextView) mView.findViewById(R.id.list_duration);
            post_duration.setText("Duration: " + duration + " minutes");
        }

        public void setServing(String serving) {
            TextView post_serving = (TextView) mView.findViewById(R.id.list_serving);
            post_serving.setText("Serving " + serving);
        }

        public void setImage(Context ctx, String image) {

            ImageView post_image = (ImageView) mView.findViewById(R.id.list_image);
            Glide.with(ctx).load(image).into(post_image);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_account){

            intent = new Intent(UserLunchActivity.this, AccountActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_search){

            intent = new Intent(UserLunchActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_feedback){

            intent = new Intent(UserLunchActivity.this, FeedbackActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}


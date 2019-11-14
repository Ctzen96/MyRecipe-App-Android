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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DinnerActivity extends AppCompatActivity {

    private RecyclerView dinner_List;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinner);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Dinner");

        dinner_List = (RecyclerView)findViewById(R.id.dinner_recyclerview);
        dinner_List.setHasFixedSize(true);
        dinner_List.setLayoutManager(new LinearLayoutManager(DinnerActivity.this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Dinner, DinnerActivity.DinnerViewHolder> firebaseRecyclerAdapter;

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Dinner, DinnerActivity.DinnerViewHolder>(Dinner.class,
                R.layout.list_of_recipe, DinnerActivity.DinnerViewHolder.class, mDatabase) {
            @Override
            protected void populateViewHolder(DinnerActivity.DinnerViewHolder viewHolder, Dinner model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setRecipename(model.getRecipeName());
                viewHolder.setDuration(model.getDuration());
                viewHolder.setServing(model.getServing());
                viewHolder.setImage(getApplicationContext(), model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(DinnerActivity.this, SingleRecipeActivity.class);
                        intent.putExtra("dinner_id",post_key);
                        startActivity(intent);

                    }
                });
            }
        };

        dinner_List.setAdapter(firebaseRecyclerAdapter);
    }

    private static class DinnerViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public DinnerViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setRecipename(String recipename) {
            TextView post_recipename = (TextView)mView.findViewById(R.id.list_recipename);
            post_recipename.setText("Recipe Name: "+recipename);
        }

        public void setDuration(String duration) {
            TextView post_duration = (TextView)mView.findViewById(R.id.list_duration);
            post_duration.setText("Duration: " +duration +" minutes");
        }

        public void setServing(String serving) {
            TextView post_serving = (TextView)mView.findViewById(R.id.list_serving);
            post_serving.setText("Serving " +serving);
        }

        public void setImage(Context ctx, String image){

            ImageView post_image = (ImageView)mView.findViewById(R.id.list_image);
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

            intent = new Intent(DinnerActivity.this, AccountActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_search){

            intent = new Intent(DinnerActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_feedback){

            intent = new Intent(DinnerActivity.this, FeedbackActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}

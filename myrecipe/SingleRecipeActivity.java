package com.example.user.myrecipe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleRecipeActivity extends AppCompatActivity {

    private String Bpost_key = null, Lpost_key = null, Dpost_key = null;
    private String post_recipename, post_serving, post_duration, post_ingredient, post_preparation,
            post_user, post_image, post_id;
    private TextView Single_recipename, Single_serving, Single_duration,
            Single_ingredient, Single_preparation, Single_user;
    private ImageView Single_image;
    private Button deletebtn;
    private FirebaseAuth mAuth;
    private Context context = this;
    private Intent intent;


    private DatabaseReference BDatabase, LDatabase, DDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recipe);

        mAuth = FirebaseAuth.getInstance();

        BDatabase = FirebaseDatabase.getInstance().getReference().child("Breakfast");
        LDatabase = FirebaseDatabase.getInstance().getReference().child("Lunch");
        DDatabase = FirebaseDatabase.getInstance().getReference().child("Dinner");

        Bpost_key = getIntent().getStringExtra("breakfast_id");
        Lpost_key = getIntent().getStringExtra("lunch_id");
        Dpost_key = getIntent().getStringExtra("dinner_id");


        Single_recipename = (TextView)findViewById(R.id.display_recipename);
        Single_serving = (TextView)findViewById(R.id.display_serving);
        Single_duration = (TextView)findViewById(R.id.display_duration);
        Single_ingredient = (TextView)findViewById(R.id.display_ingredient);
        Single_preparation = (TextView)findViewById(R.id.display_prepration);
        Single_user = (TextView)findViewById(R.id.display_user);
        Single_image = (ImageView)findViewById(R.id.display_recipeimage);
        deletebtn = (Button) findViewById(R.id.removebtn);


        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alertbuilder = new AlertDialog.Builder(context);
                alertbuilder.setTitle("Remove Recipe");
                alertbuilder
                        .setMessage("Are you sure want do remove the recipe?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(Bpost_key!=null){
                                    BDatabase.child(Bpost_key).removeValue();
                                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                                    
                                }

                                if(Lpost_key!=null){
                                    LDatabase.child(Lpost_key).removeValue();
                                    Snackbar snackbar = Snackbar.make(v,"Recipe is deleted", Snackbar.LENGTH_SHORT );
                                    snackbar.show();
                                }

                                if(Dpost_key!=null){
                                    DDatabase.child(Dpost_key).removeValue();
                                    Snackbar snackbar = Snackbar.make(v,"Recipe is deleted", Snackbar.LENGTH_SHORT );
                                    snackbar.show();
                                }
                                
                                finish();
                            }

                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertbuilder.create();

                alertDialog.show();
            }
        });

        if(Bpost_key!=null){
            BDatabase.child(Bpost_key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    post_recipename = (String) dataSnapshot.child("RecipeName").getValue();
                    post_serving = (String) dataSnapshot.child("Serving").getValue();
                    post_duration = (String) dataSnapshot.child("Duration").getValue();
                    post_ingredient = (String) dataSnapshot.child("Ingredient").getValue();
                    post_preparation = (String) dataSnapshot.child("Preparation").getValue();
                    post_user = (String) dataSnapshot.child("Username").getValue();
                    post_image = (String) dataSnapshot.child("Image").getValue();
                    post_id = (String) dataSnapshot.child("UserID").getValue();

                    setDisplay();

                    if(mAuth.getCurrentUser().getUid() .equals(post_id)){

                        deletebtn.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }
        if(Lpost_key!=null){
            LDatabase.child(Lpost_key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    post_recipename = (String) dataSnapshot.child("RecipeName").getValue();
                    post_serving = (String) dataSnapshot.child("Serving").getValue();
                    post_duration = (String) dataSnapshot.child("Duration").getValue();
                    post_ingredient = (String) dataSnapshot.child("Ingredient").getValue();
                    post_preparation = (String) dataSnapshot.child("Preparation").getValue();
                    post_user = (String) dataSnapshot.child("Username").getValue();
                    post_image = (String) dataSnapshot.child("Image").getValue();
                    post_id = (String) dataSnapshot.child("UserID").getValue();

                    setDisplay();

                    if(mAuth.getCurrentUser().getUid() .equals(post_id)){

                        deletebtn.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        if(Dpost_key!=null){
            DDatabase.child(Dpost_key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    post_recipename = (String) dataSnapshot.child("RecipeName").getValue();
                    post_serving = (String) dataSnapshot.child("Serving").getValue();
                    post_duration = (String) dataSnapshot.child("Duration").getValue();
                    post_ingredient = (String) dataSnapshot.child("Ingredient").getValue();
                    post_preparation = (String) dataSnapshot.child("Preparation").getValue();
                    post_user = (String) dataSnapshot.child("Username").getValue();
                    post_image = (String) dataSnapshot.child("Image").getValue();
                    post_id = (String) dataSnapshot.child("UserID").getValue();

                    setDisplay();

                    if(mAuth.getCurrentUser().getUid() .equals(post_id)){

                        deletebtn.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public void setDisplay(){

        Single_recipename.setText(post_recipename);
        Single_serving.setText(post_serving+" person");
        Single_duration.setText(post_duration+" minutes");
        Single_ingredient.setText(post_ingredient);
        Single_preparation.setText(post_preparation);
        Single_user.setText(post_user);

        Glide.with(SingleRecipeActivity.this).load(post_image).into(Single_image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_account){

            intent = new Intent(SingleRecipeActivity.this, AccountActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_search){

            intent = new Intent(SingleRecipeActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_feedback){

            intent = new Intent(SingleRecipeActivity.this, FeedbackActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}

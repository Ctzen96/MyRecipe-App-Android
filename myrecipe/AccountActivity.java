package com.example.user.myrecipe;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {

    private Button recipebtn, loginbtn, logoutbtn;
    private TextView useremail, username;
    private Intent intent;

    private DatabaseReference mDatabaseUser;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);

        recipebtn = (Button)findViewById(R.id.account_recipebtn);
        loginbtn = (Button)findViewById(R.id.account_loginbtn);
        logoutbtn = (Button)findViewById(R.id.account_logoutbtn);
        username = (TextView)findViewById(R.id.account_username);
        useremail = (TextView)findViewById(R.id.account_useremail);

        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){

                    recipebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(AccountActivity.this,"Please log in", Toast.LENGTH_SHORT).show();
                        }
                    });

                    loginbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intent = new Intent(AccountActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    mCurrentUser = mAuth.getCurrentUser();
                    mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String email = (String) dataSnapshot.child("Email").getValue();
                            String name = (String) dataSnapshot.child("Name").getValue();
                            username.setText(name);
                            useremail.setText(email);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    recipebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intent = new Intent(AccountActivity.this, MyRecipeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }
                    });
                    loginbtn.setVisibility(View.INVISIBLE);
                    logoutbtn.setVisibility(View.VISIBLE);
                }

            }
        };

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                intent = new Intent(AccountActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_account){

            intent = new Intent(AccountActivity.this, AccountActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_search){

            intent = new Intent(AccountActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_feedback){

            intent = new Intent(AccountActivity.this, FeedbackActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_feedback){

            intent = new Intent(AccountActivity.this, FeedbackActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}

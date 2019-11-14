package com.example.user.myrecipe;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeedbackActivity extends AppCompatActivity {

    private SeekBar seekbar;
    private EditText comment;
    private Button submitbtn;
    private Intent intent;
    private TextView tv;

    private DatabaseReference mDatabase, mDatabaseUser, mDatabaseGuest;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setMax(5);
        seekbar.setProgress(3);
        submitbtn = (Button) findViewById(R.id.submitbtn);
        tv = (TextView) findViewById(R.id.rate);
        comment = (EditText) findViewById(R.id.comments);

        final String comments = comment.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        mDatabaseGuest = FirebaseDatabase.getInstance().getReference().child("Feedback").child("Guest");

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String num = String.valueOf(progress);
                tv.setText(num);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {

                    submitbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String progress = tv.getText().toString();
                            mDatabase = mDatabaseGuest.push();
                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    mDatabase.child("Rating").setValue(progress);
                                    mDatabase.child("Comment").setValue(comments)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(FeedbackActivity.this, "Your feedback has been sent. Thank you very much.", Toast.LENGTH_SHORT).show();
                                                        intent = new Intent(FeedbackActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }

                            });


                        }
                    });
                } else {
                    mCurrentUser = mAuth.getCurrentUser();
                    mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String email = (String) dataSnapshot.child("Email").getValue();
                            String name = (String) dataSnapshot.child("Name").getValue();

                            submitbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final String progress = tv.getText().toString();
                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Feedback").child("User").push();
                                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            mDatabase.child("Rating").setValue(progress);
                                            mDatabase.child("Comment").setValue(comments);
                                            mDatabase.child("UserID").setValue(mCurrentUser.getUid());
                                            mDatabase.child("Username").setValue(dataSnapshot.child("Name").getValue())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(FeedbackActivity.this, "Your feedback has been sent. Thank you very much.", Toast.LENGTH_SHORT).show();
                                                                intent = new Intent(FeedbackActivity.this, MainActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }

                                    });


                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }



            }
        };
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

            intent = new Intent(FeedbackActivity.this, AccountActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_search){

            intent = new Intent(FeedbackActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_feedback){

            intent = new Intent(FeedbackActivity.this, FeedbackActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_about){

            intent = new Intent(FeedbackActivity.this, AboutUsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}

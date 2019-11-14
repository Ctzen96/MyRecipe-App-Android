package com.example.user.myrecipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.UUID;

public class CreateRecipeActivity extends AppCompatActivity {

    private EditText Create_Recipename, Create_Serving, Create_Duration, Create_Ingredient, Create_Preparation;
    TextView Create_Username;
    Button  Createbtn;
    RadioButton Create_Breakfast, Create_Lunch, Create_Dinner;
    ImageButton Create_Imagebtn;
    Intent intent;
    Uri imageUri = null;

    private StorageReference mStorage;
    private DatabaseReference mDatabase, mDatabaseUser;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;


    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        Create_Recipename = (EditText)findViewById(R.id.create_recipename);
        Create_Serving = (EditText)findViewById(R.id.create_serving);
        Create_Duration = (EditText)findViewById(R.id.create_duration);
        Create_Ingredient = (EditText)findViewById(R.id.create_ingredient);
        Create_Preparation = (EditText)findViewById(R.id.create_prepration);
        Create_Breakfast = (RadioButton)findViewById(R.id.create_rb_breakfast);
        Create_Lunch = (RadioButton) findViewById(R.id.create_rb_lunch);
        Create_Dinner = (RadioButton)findViewById(R.id.create_rb_dinner);
        Create_Username = (TextView)findViewById(R.id.create_user);
        Create_Imagebtn = (ImageButton)findViewById(R.id.create_recipeimage);
        Createbtn = (Button)findViewById(R.id.create_createbtn);

        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = (String) dataSnapshot.child("Name").getValue();
                Create_Username.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Create_Imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, GALLERY_REQUEST);
            }
        });

        Createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCreate();
            }
        });


    }


    private void startCreate() {

        String recipename_val = Create_Recipename.getText().toString();
        String serving_val = Create_Serving.getText().toString();
        String duration_val = Create_Duration.getText().toString();
        String ingredient_val = Create_Ingredient.getText().toString();
        String preparaton_val = Create_Preparation.getText().toString();

        if(!TextUtils.isEmpty(recipename_val) && !TextUtils.isEmpty(serving_val) && !TextUtils.isEmpty(duration_val)
                && !TextUtils.isEmpty(ingredient_val) && !TextUtils.isEmpty(preparaton_val)){
            if(Create_Breakfast.isChecked()){

                UploadtoBreakfastStorage(recipename_val,serving_val,duration_val,
                        ingredient_val,preparaton_val);
            }
            if(Create_Lunch.isChecked()){

                UploadtoLunchStorage(recipename_val,serving_val,duration_val,
                        ingredient_val,preparaton_val);
            }
            if(Create_Dinner.isChecked()){

                UploadtoDinnerStorage(recipename_val,serving_val,duration_val,
                        ingredient_val,preparaton_val);
            }
        }
        else{
            Toast.makeText(this, "Please fill in all the text fields.", Toast.LENGTH_SHORT).show();
        }



    }

    private void UploadtoBreakfastStorage(final String recipename_val, final String serving_val, final String duration_val,
                                          final String ingredient_val, final String preparaton_val){

        final StorageReference filepath;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        filepath = mStorage.child("Breakfast/"+UUID.randomUUID().toString());

        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(CreateRecipeActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateRecipeActivity.this, "Upload Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                        .getTotalByteCount());
                progressDialog.setMessage("Uploaded "+(int)progress+"%");
            }
        });

        Task<Uri> urlTask = filepath.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){

                    Toast.makeText(CreateRecipeActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }

                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    final Uri downloaduri = task.getResult();


                    final DatabaseReference BDatabase = mDatabase.child("Breakfast").push();

                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            BDatabase.child("Image").setValue(downloaduri.toString());
                            BDatabase.child("RecipeName").setValue(recipename_val);
                            BDatabase.child("Serving").setValue(serving_val);
                            BDatabase.child("Duration").setValue(duration_val);
                            BDatabase.child("Ingredient").setValue(ingredient_val);
                            BDatabase.child("Preparation").setValue(preparaton_val);
                            BDatabase.child("UserID").setValue(mCurrentUser.getUid());
                            BDatabase.child("Username").setValue(dataSnapshot.child("Name").getValue())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                intent = new Intent(CreateRecipeActivity.this, MyRecipeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                else{
                    Toast.makeText(CreateRecipeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void UploadtoLunchStorage(final String recipename_val, final String serving_val, final String duration_val, final String ingredient_val, final String preparaton_val) {

        final StorageReference filepath;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        filepath = mStorage.child("Lunch/"+UUID.randomUUID().toString());

        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(CreateRecipeActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateRecipeActivity.this, "Upload Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                        .getTotalByteCount());
                progressDialog.setMessage("Uploaded "+(int)progress+"%");
            }
        });

        Task<Uri> urlTask = filepath.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){

                    Toast.makeText(CreateRecipeActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }

                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    final Uri downloaduri = task.getResult();


                    final DatabaseReference BDatabase = mDatabase.child("Lunch").push();

                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            BDatabase.child("Image").setValue(downloaduri.toString());
                            BDatabase.child("RecipeName").setValue(recipename_val);
                            BDatabase.child("Serving").setValue(serving_val);
                            BDatabase.child("Duration").setValue(duration_val);
                            BDatabase.child("Ingredient").setValue(ingredient_val);
                            BDatabase.child("Preparation").setValue(preparaton_val);
                            BDatabase.child("UserID").setValue(mCurrentUser.getUid());
                            BDatabase.child("Username").setValue(dataSnapshot.child("Name").getValue())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                intent = new Intent(CreateRecipeActivity.this, MyRecipeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                else{
                    Toast.makeText(CreateRecipeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void UploadtoDinnerStorage(final String recipename_val, final String serving_val, final String duration_val, final String ingredient_val, final String preparaton_val) {
        final StorageReference filepath;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        filepath = mStorage.child("Dinner/"+UUID.randomUUID().toString());

        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(CreateRecipeActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateRecipeActivity.this, "Upload Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                        .getTotalByteCount());
                progressDialog.setMessage("Uploaded "+(int)progress+"%");
            }
        });

        Task<Uri> urlTask = filepath.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){

                    Toast.makeText(CreateRecipeActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }

                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    final Uri downloaduri = task.getResult();


                    final DatabaseReference BDatabase = mDatabase.child("Dinner").push();

                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            BDatabase.child("Image").setValue(downloaduri.toString());
                            BDatabase.child("RecipeName").setValue(recipename_val);
                            BDatabase.child("Serving").setValue(serving_val);
                            BDatabase.child("Duration").setValue(duration_val);
                            BDatabase.child("Ingredient").setValue(ingredient_val);
                            BDatabase.child("Preparation").setValue(preparaton_val);
                            BDatabase.child("UserID").setValue(mCurrentUser.getUid());
                            BDatabase.child("Username").setValue(dataSnapshot.child("Name").getValue())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                intent = new Intent(CreateRecipeActivity.this, MyRecipeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                else{
                    Toast.makeText(CreateRecipeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            imageUri = data.getData();

            Create_Imagebtn.setImageURI(imageUri);

            String uri = imageUri.toString();

            Toast.makeText(CreateRecipeActivity.this, uri, Toast.LENGTH_SHORT).show();
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

            intent = new Intent(CreateRecipeActivity.this, AccountActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_search){

            intent = new Intent(CreateRecipeActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_feedback){

            intent = new Intent(CreateRecipeActivity.this, FeedbackActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_about){

            intent = new Intent(CreateRecipeActivity.this, AboutUsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



}

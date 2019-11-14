package com.example.user.myrecipe;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class SearchActivity extends AppCompatActivity {

    private String searchtext;
    private Intent intent;

    private EditText search_item;
    private Button searchbtn, voicebtn;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private RecyclerView search_list;

    private DatabaseReference BDatabase, LDatabase, DDatabase;
    private  Query SearchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_item = (EditText)findViewById(R.id.search_et);
        searchbtn = (Button)findViewById(R.id.searchbtn);
        voicebtn = (Button)findViewById(R.id.microbtn);

        BDatabase = FirebaseDatabase.getInstance().getReference("Breakfast");

        search_list = (RecyclerView)findViewById(R.id.search_recyclerview);
        search_list.setHasFixedSize(true);
        search_list.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        voicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                speak();
            }
        });
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchtext = search_item.getText().toString().trim();
                firebaseSearchB(searchtext);
            }
        });
    }

    //Speech to text sensor
    private void speak() {
        Intent intent =  new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi,speak something");

        try{
            startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);

        }catch(Exception e) {
            Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    //Receiving data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK  &&  null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    search_item.setText(result.get(0));
                    searchtext=result.get(0);
                    firebaseSearchB(searchtext);
                }
                break;
        }
    }

    private void firebaseSearchB(String searchtext) {

        SearchQuery = BDatabase.orderByChild("RecipeName").startAt(searchtext).endAt(searchtext+"\uf8ff");

        FirebaseRecyclerAdapter<Breakfast, SearchActivity.SearchViewHolder> firebaseRecyclerAdapter;


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Breakfast, SearchActivity.SearchViewHolder>(Breakfast.class,
                R.layout.list_of_recipe, SearchActivity.SearchViewHolder.class, SearchQuery) {
            @Override
            protected void populateViewHolder(SearchActivity.SearchViewHolder viewHolder, Breakfast model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setDetails(getApplicationContext(),model.getRecipeName(), model.getServing(), model.getServing(),model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(SearchActivity.this, SingleRecipeActivity.class);
                        intent.putExtra("breakfast_id",post_key);
                        startActivity(intent);
                    }
                });
            }
        };

        search_list.setAdapter(firebaseRecyclerAdapter);
    }

    private static class SearchViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setDetails(Context ctx, String recipename, String duration, String serving, String image){
            TextView post_recipename = (TextView)mView.findViewById(R.id.list_recipename);
            TextView post_duration = (TextView)mView.findViewById(R.id.list_duration);
            TextView post_serving = (TextView)mView.findViewById(R.id.list_serving);
            ImageView post_image = (ImageView)mView.findViewById(R.id.list_image);

            post_recipename.setText("Recipe Name: "+recipename);
            post_duration.setText("Duration: " +duration +" minutes");
            post_serving.setText("Serving " +serving);
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

            intent = new Intent(SearchActivity.this, AccountActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_search){

            intent = new Intent(SearchActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_feedback){

            intent = new Intent(SearchActivity.this, FeedbackActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_about){

            intent = new Intent(SearchActivity.this, AboutUsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}

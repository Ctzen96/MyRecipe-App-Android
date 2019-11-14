package com.example.user.myrecipe;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AboutUsActivity extends AppCompatActivity {

    private TextView web1, web2, web3, contactnum, email;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        web1 = (TextView)findViewById(R.id.website1);
        web2 = (TextView)findViewById(R.id.website2);
        web3 = (TextView)findViewById(R.id.website3);
        contactnum = (TextView)findViewById(R.id.contactnum);
        email = (TextView)findViewById(R.id.email);

        web1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.kuali.com/recipes/"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        web2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.nyonyacooking.com/"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        web3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://rasamalaysia.com/recipes/malaysian-recipes/"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        contactnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:+09548613578"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uriText =
                        "mailto:myRecipe_email@gmail.com" +
                                "?subject=" + Uri.encode("some subject text here") +
                                "&body=" + Uri.encode("some text here");

                Uri uri = Uri.parse(uriText);

                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(sendIntent, "Send email"));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_account){

            intent = new Intent(AboutUsActivity.this, AccountActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_search){

            intent = new Intent(AboutUsActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_feedback){

            intent = new Intent(AboutUsActivity.this, FeedbackActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_about){

            intent = new Intent(AboutUsActivity.this, AboutUsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}

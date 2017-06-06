package com.njitdev.sa_android.school_announcement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.njitdev.sa_android.R;

public class AnnouncementArticleBodyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_article_body);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Toast.makeText(getApplicationContext(),bundle.get("articleID")+"",Toast.LENGTH_SHORT).show();
        }
    }
}

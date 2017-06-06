package com.njitdev.sa_android.school_announcement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.utils.ModelListener;

public class AnnouncementArticleBodyActivity extends AppCompatActivity {

    int articleID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_article_body);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Toast.makeText(getApplicationContext(),bundle.getString("articleID"),Toast.LENGTH_SHORT).show();
            articleID = Integer.parseInt(bundle.getString("articleID"));
        }
        fetchArticleBody();
    }

    private void setBusy(boolean busy) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarforArticle);
        if (busy) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void fetchArticleBody(){
        setBusy(true);
        AnnouncementArticleModels.fetchList(articleID, new ModelListener() {
            @Override
            public void onData(Object result, String message) {
                setBusy(false);
                if(result == null){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }else {
                    TextView tx = (TextView) findViewById(R.id.textViewforArticle);
                    tx.setText(result.toString());
                }
            }
        });
    }
}

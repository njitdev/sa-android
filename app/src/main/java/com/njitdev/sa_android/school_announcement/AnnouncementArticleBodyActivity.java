package com.njitdev.sa_android.school_announcement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.utils.ModelListener;

public class AnnouncementArticleBodyActivity extends AppCompatActivity {

    String articleID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_article_body);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //articleID = Integer.parseInt(bundle.getString("articleID"));
            articleID = bundle.getString("articleID");
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

    private void fetchArticleBody() {
        setBusy(true);
        AnnouncementModels.fetchArticleBody(articleID, new ModelListener() {
            @Override
            public void onData(Object result, String message) {
                //setBusy(false);
                if (result == null) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    WebView wb = (WebView) findViewById(R.id.webViewforArticle);
                    wb.getSettings().setJavaScriptEnabled(true);
                    wb.loadData(result.toString(), "text/html;charset=utf-8", null);
                    wb.getSettings().setLoadWithOverviewMode(true);
                    wb.getSettings().setUseWideViewPort(true);
                }
            }
        });
    }

}

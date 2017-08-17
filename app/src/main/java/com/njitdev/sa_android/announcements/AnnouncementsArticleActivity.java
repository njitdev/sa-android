/*
    sa-android
    Copyright (C) 2017 sa-android authors

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.njitdev.sa_android.announcements;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.models.announcements.AnnouncementModels;
import com.njitdev.sa_android.utils.ModelListener;

public class AnnouncementsArticleActivity extends AppCompatActivity {

    private String articleID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements_article);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
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
        AnnouncementModels.fetchArticleBody(articleID, new ModelListener<String>() {
            @Override
            public void onData(String result, String message) {
                setBusy(false);
                if (result == null) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    WebView wb = (WebView) findViewById(R.id.webViewforArticle);
                    wb.loadData(result, "text/html;charset=utf-8", null);
                    wb.getSettings().setLoadWithOverviewMode(true);
                    wb.getSettings().setUseWideViewPort(true);
                }
            }
        });
    }
}

package com.njitdev.sa_android.school_announcement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.utils.ModelListener;

import java.util.ArrayList;
import java.util.List;

public class SchoolAnnouncementActivity extends AppCompatActivity {

    private List<Article> mArticles = new ArrayList<>();
    private List<String> articleTitles = new ArrayList<>();
    private ArrayAdapter<String> announcementsAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mArticles.clear();
                    fetchAnnouncementsList(1);
                    return true;
                case R.id.navigation_dashboard:
                    mArticles.clear();
                    fetchAnnouncementsList(2);
                    return true;
                case R.id.navigation_notifications:
                    mArticles.clear();
                    fetchAnnouncementsList(3);
                    return true;
            }
            return false;
        }
    };

    private void setBusy(boolean busy) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarforAnnouncement);
        if (busy) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_announcement);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //initData(savedInstanceState);
        //initAdapter(articleTitles);
        announcementsAdapter = new ArrayAdapter<>(this, R.layout.dl3, articleTitles);
        final ListView list = (ListView) findViewById(R.id.listViewSchoolAnnouncement);
        list.setAdapter(announcementsAdapter);

        //set onitemclicklistener;
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SchoolAnnouncementActivity.this, AnnouncementArticleBodyActivity.class);
                intent.putExtra("articleID", mArticles.get(position).article_id);
                startActivity(intent);
            }
        });
    }

    private void initData(Bundle savedInstanceState){
        if(savedInstanceState != null)
            Log.e("tag","tag");
            articleTitles = savedInstanceState.getStringArrayList("articleTitles");

    }

    private void initAdapter(List<String> articleTitles){
        announcementsAdapter = new ArrayAdapter<>(this, R.layout.dl3, articleTitles);
    }

    private void fetchAnnouncementsList(int category) {

        final View n = findViewById(R.id.navigation);
        n.setVisibility(View.INVISIBLE);

        setBusy(true);

        AnnouncementModels.fetchArticleList(category, new ModelListener() {
            @Override
            public void onData(Object result, String message) {
                setBusy(false);
                if (result != null) {
                    n.setVisibility(View.VISIBLE);
                    ArrayList<Article> articles = (ArrayList<Article>) result;
                    mArticles.addAll(articles);
                    updateListView();
                }
            }
        });
    }

    private void updateListView() {
        articleTitles.clear();
        for (int i = 0; i < mArticles.size(); i++) {
            articleTitles.add(mArticles.get(i).article_title);
        }
        announcementsAdapter.notifyDataSetChanged();
    }
}

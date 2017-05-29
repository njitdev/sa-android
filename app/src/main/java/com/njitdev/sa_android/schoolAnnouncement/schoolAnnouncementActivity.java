package com.njitdev.sa_android.schoolAnnouncement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.utils.ModelListener;

import java.util.ArrayList;
import java.util.List;

public class schoolAnnouncementActivity extends AppCompatActivity {

    private List<Article> mArticles = new ArrayList<>();
    private List<String> articleTitles = new ArrayList<>();

    private TextView mTextMessage;
    private ArrayAdapter<String> announcementsAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    fetchAnnouncementsList(1);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    fetchAnnouncementsList(2);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    fetchAnnouncementsList(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_announcement);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Initialize ListView
        announcementsAdapter = new ArrayAdapter<>(this, R.layout.dl3, articleTitles);
        final ListView list = (ListView) findViewById(R.id.listViewSchoolAnnouncement);
        list.setAdapter(announcementsAdapter);
    }

    private void fetchAnnouncementsList(int category){

        mTextMessage.setText("正在更新通知信息");

        AnnouncementModels.fetchList(category, new ModelListener() {
            @Override
            public void onData(Object result, String message) {
                if (result == null) {
                    mTextMessage.setText(message);
                } else {
                    ArrayList<Article> articles = (ArrayList<Article>) result;
                    mArticles.addAll(articles);
                    mTextMessage.setText("");
                    updateListView();
                }
            }
        });
    }

    private void updateListView() {
        articleTitles.clear();
        for (int i = 0; i < mArticles.size(); i++){
            articleTitles.add(mArticles.get(i).article_title);
        }
        announcementsAdapter.notifyDataSetChanged();
    }
}

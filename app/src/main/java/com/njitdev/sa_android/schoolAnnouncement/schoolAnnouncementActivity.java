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

public class schoolAnnouncementActivity extends AppCompatActivity {

    ArrayList<Article> mArticles = new ArrayList<>();

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    fetchMessage(1);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    fetchMessage(2);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    fetchMessage(3);
                    return true;
            }
            return false;
        }

    };

    private void fetchMessage(int category){

        mTextMessage.setText("正在更新通知信息");

        AnnouncementModels.fetchList(new ModelListener() {
            @Override
            public void onData(boolean success, Object object) {
                if (object == null) {
                    mTextMessage.setText("获取通知信息失败");
                } else {
                    ArrayList<Article> myarticle = (ArrayList<Article>) object;
                    mArticles.addAll(myarticle);
                    mTextMessage.setText("");
                    showMessage();
                }
            }
        }, category);
    }

    private void showMessage() {

        String[] titles = new String[mArticles.size()];
        for (int i=0;i<mArticles.size();i++){
            titles[i] = mArticles.get(i).getArticle_title();
        }

        final ListView list = (ListView) findViewById(R.id.listViewSchoolAnnouncement);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.dl3, titles);
        list.setAdapter(adapter1);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_announcement);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}

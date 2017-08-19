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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.models.announcements.AnnouncementModels;
import com.njitdev.sa_android.models.announcements.Article;
import com.njitdev.sa_android.utils.ModelListener;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementsActivity extends AppCompatActivity {

    private List<Article> mArticles = new ArrayList<>();
    private AnnouncementsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        // Remove ActionBar shadow
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setElevation(0);

        // Initialize TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabCategories);
        tabLayout.addTab(tabLayout.newTab().setText("校内通知").setTag(1));
        tabLayout.addTab(tabLayout.newTab().setText("公示公告").setTag(2));
        tabLayout.addTab(tabLayout.newTab().setText("校内简讯").setTag(3));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Integer category = (Integer) tab.getTag();
                if (category != null) fetchAnnouncementsList(category);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Init adapter and ListView
        mAdapter = new AnnouncementsAdapter(this, R.layout.list_item_announcement, mArticles);
        final ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(mAdapter);

        // Set OnItemClickListener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AnnouncementsActivity.this, AnnouncementsArticleActivity.class);
                intent.putExtra("articleID", mArticles.get(position).article_id);
                startActivity(intent);
            }
        });

        // Fetch default category
        fetchAnnouncementsList(1);
    }

    private void setBusy(boolean busy) {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabCategories);
        tabLayout.setEnabled(!busy);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pbBusy);
        if (busy) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void fetchAnnouncementsList(int category) {
        mArticles.clear();
        mAdapter.notifyDataSetChanged();

        setBusy(true);
        AnnouncementModels.fetchArticleList(category, new ModelListener<List<Article>>() {
            @Override
            public void onData(List<Article> result, String message) {
                setBusy(false);
                if (result != null) {
                    mArticles.addAll(result);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AnnouncementsActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

class AnnouncementsAdapter extends ArrayAdapter<Article> {

    private Context mContext;
    private int mResource;
    private List<Article> mArticles;

    AnnouncementsAdapter(Context context, int resource, List<Article> articles) {
        super(context, resource, articles);
        mContext = context;
        mResource = resource;
        mArticles = articles;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        }

        TextView lblTitle = (TextView) convertView.findViewById(R.id.lblTitle);
        TextView lblSubTitle = (TextView) convertView.findViewById(R.id.lblSubtitle);

        Article article = mArticles.get(position);

        lblTitle.setText(article.article_title);

        // Generate subtitle
        String subTitle = "";
        if (article.article_department != null) subTitle += article.article_department;
        if (article.article_date != null) subTitle += " " + article.article_date;
        lblSubTitle.setText(subTitle);

        return convertView;
    }
}

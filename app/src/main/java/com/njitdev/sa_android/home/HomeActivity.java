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

package com.njitdev.sa_android.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.announcements.AnnouncementsActivity;
import com.njitdev.sa_android.classschedule.ClassScheduleActivity;
import com.njitdev.sa_android.grades.GradesActivity;
import com.njitdev.sa_android.library.LibraryActivity;
import com.njitdev.sa_android.login.LoginActivity;
import com.njitdev.sa_android.messageboard.MessageBoardActivity;
import com.njitdev.sa_android.models.school.ClassSchedule;
import com.njitdev.sa_android.models.school.GradeItem;
import com.njitdev.sa_android.models.school.SchoolSystemModels;
import com.njitdev.sa_android.models.school.StudentBasicInfo;
import com.njitdev.sa_android.utils.ModelListener;
import com.njitdev.sa_android.utils.SAGlobal;
import com.njitdev.sa_android.utils.SAUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    // Help detecting new user sessions
    private String mLastSessionID = "";

    // School system data of current session
    private StudentBasicInfo mStudentBasicInfo;

    // UI data
    private boolean mUIBusy = false;
    private int mRemainingFetch = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // App init
        SAUtils.appInit(getApplicationContext());

        // Initialize menu list
        updateMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Skip if UI is busy
        if (mUIBusy)
            return onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.menuItemUpdateData:
                if (SAGlobal.studentSessionID == null) {
                    Toast.makeText(this, "请先登录账号~", Toast.LENGTH_SHORT).show();
                } else {
                    autoUpdateData();
                }
                return true;

            case R.id.menuItemLogin:
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                return true;
        }
        return onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Update data
        autoUpdateData();
    }

    private void setUIBusy(Boolean busy) {
        mUIBusy = busy;

        ListView listView = (ListView) findViewById(R.id.listView);
        ProgressBar pbBusy = (ProgressBar) findViewById(R.id.pbBusy);

        listView.setEnabled(!busy);

        if (busy) {
            pbBusy.setVisibility(View.VISIBLE);
        } else {
            pbBusy.setVisibility(View.INVISIBLE);
        }
    }

    // Create main menu
    // This re-creates adapter and list items every time
    private void updateMenu() {
        HomeMenuAdapter mMenuAdapter;
        List<HomeMenuItem> mMenuItems;

        mMenuItems = new ArrayList<>();
        mMenuAdapter = new HomeMenuAdapter(this, R.layout.list_item_home, mMenuItems);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(mMenuAdapter);

        // OnClick
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        // Login if not already
                        if (SAGlobal.studentSessionID == null)
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        break;
                    case 1:
                        // Class Schedule
                        if (SAGlobal.dataClassSchedule != null) {
                            startActivity(new Intent(HomeActivity.this, ClassScheduleActivity.class));
                        } else {
                            Toast.makeText(HomeActivity.this, "没有课程表数据，请先登录", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        // Grades
                        if (SAGlobal.dataClassSchedule != null) {
                            startActivity(new Intent(HomeActivity.this, GradesActivity.class));
                        } else {
                            Toast.makeText(HomeActivity.this, "没有成绩数据，请先登录", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        // Library
                        startActivity(new Intent(HomeActivity.this, LibraryActivity.class));
                        break;
                    case 4:
                        // Announcements
                        startActivity(new Intent(HomeActivity.this, AnnouncementsActivity.class));
                        break;
                    case 5:
                        // Message baord
                        startActivity(new Intent(HomeActivity.this, MessageBoardActivity.class));
                        break;
                }
            }
        });

        // Re-create menu items
        mMenuItems.clear();

        // Login / user info
        HomeMenuItem item = new HomeMenuItem("", null, R.drawable.ic_user);
        if (mStudentBasicInfo != null) {
            item.title = mStudentBasicInfo.student_name;

            // Subtitle
            item.subtitle = "";
            if (mStudentBasicInfo.student_department != null)
                item.subtitle += mStudentBasicInfo.student_department;

            if (mStudentBasicInfo.student_enroll_year != null)
                item.subtitle += ", " + mStudentBasicInfo.student_enroll_year + " 级";

        } else {
            item.title = "未登录";
            item.subtitle = "请登录教务系统账号";
        }
        mMenuItems.add(item);

        // Class schedule
        mMenuItems.add(new HomeMenuItem("课程表", "查看本学期课表", R.drawable.ic_class_schedule));

        // Grades
        item = new HomeMenuItem("成绩查询", "查询筛选成绩", R.drawable.ic_grades);
        if (SAGlobal.dataGrades != null) {
            int n = SAGlobal.dataGrades.size();
            if (n == 0) {
                item.subtitle = "还没有成绩";
            } else {
                item.subtitle = n + " 门课程";
            }
        }
        mMenuItems.add(item);

        // Completely static items
        mMenuItems.add(new HomeMenuItem("图书馆", "检索图书馆藏", R.drawable.ic_library));
        mMenuItems.add(new HomeMenuItem("校内通知", "学校官方通知", R.drawable.ic_announcements));
        mMenuItems.add(new HomeMenuItem("留言板", "App 讨论区", R.drawable.ic_msgboard));

        // Update ListView
        mMenuAdapter.notifyDataSetChanged();
    }

    // Automatically update school systems data and attempt to login if required
    // TODO: Currently this just assumes user is logged in
    private void autoUpdateData() {

        // If session changed
        if (SAGlobal.studentSessionID != null && !SAGlobal.studentSessionID.equals(mLastSessionID)) {
            // Copy string
            mLastSessionID = new String(SAGlobal.studentSessionID);

            // Track data fetching progress
            mRemainingFetch = 3;

            setUIBusy(true);

            // Fetch basic info
            SchoolSystemModels.studentBasicInfo(SAGlobal.studentSessionID, null, new ModelListener<StudentBasicInfo>() {
                @Override
                public void onData(StudentBasicInfo result, String message) {
                    if (result == null) {
                        Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        mStudentBasicInfo = result;
                        updateMenu();
                    }

                    // Unlock UI if all data fetched
                    if (--mRemainingFetch == 0) setUIBusy(false);
                }
            });

            // Fetch class schedule
            SchoolSystemModels.fetchClassSchedule(SAGlobal.studentSessionID, new ModelListener<List<List<ClassSchedule>>>() {
                @Override
                public void onData(List<List<ClassSchedule>> result, String message) {
                    if (result == null) {
                        Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        SAGlobal.dataClassSchedule = result;
                        updateMenu();
                    }
                    if (--mRemainingFetch == 0) setUIBusy(false);
                }
            });

            // Fetch grades
            SchoolSystemModels.fetchGrades(SAGlobal.studentSessionID, new ModelListener<List<GradeItem>>() {
                @Override
                public void onData(List<GradeItem> result, String message) {
                    if (result == null) {
                        Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        SAGlobal.dataGrades = result;
                        updateMenu();
                    }
                    if (--mRemainingFetch == 0) setUIBusy(false);
                }
            });
        }
    }
}

class HomeMenuItem {
    String title;
    String subtitle;
    int iconResource;

    HomeMenuItem(String title, String subtitle, int iconResource) {
        this.title = title;
        this.subtitle = subtitle;
        this.iconResource = iconResource;
    }
}

class HomeMenuAdapter extends ArrayAdapter<HomeMenuItem> {

    private Context mContext;
    private int mResource;
    private List<HomeMenuItem> mMenuItems;

    HomeMenuAdapter(Context context, int resource, List<HomeMenuItem> menuItems) {
        super(context, resource, menuItems);
        mContext = context;
        mResource = resource;
        mMenuItems = menuItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);

        // Locate item
        HomeMenuItem menuItem = mMenuItems.get(position);

        // Icon
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
        imgIcon.setImageResource(menuItem.iconResource);

        // Text
        TextView lblTitle = (TextView) convertView.findViewById(R.id.lblTitle);
        TextView lblSubTitle = (TextView) convertView.findViewById(R.id.lblSubtitle);

        lblTitle.setText(menuItem.title);
        lblSubTitle.setText(menuItem.subtitle);

        return convertView;
    }
}

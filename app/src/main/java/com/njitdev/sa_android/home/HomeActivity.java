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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.njitdev.sa_android.test.TestActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // App init
        SAUtils.appInit(getApplicationContext());

        // Initialize menu list
        updateMenu();

        // TODO: For testing
        Button buttonHomeTest = (Button) findViewById(R.id.buttonHomeTest);
        buttonHomeTest.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomeActivity.this, TestActivity.class));
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView lblSessionID = (TextView) findViewById(R.id.lblSessionID);
        lblSessionID.setText("session_id: " + SAGlobal.studentSessionID);

        // Update data
        autoUpdateData();
    }

    private void setUIBusy(Boolean busy) {
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
                        // Login (for now)
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(HomeActivity.this, ClassScheduleActivity.class));
                        break;
                    case 2:
                        // Grades
                        startActivity(new Intent(HomeActivity.this, GradesActivity.class));
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
            item.title = "点此登录";
            item.subtitle = null;
        }
        mMenuItems.add(item);

        // Class schedule
        mMenuItems.add(new HomeMenuItem("课程表", null, R.drawable.ic_class_schedule));

        // Grades
        mMenuItems.add(new HomeMenuItem("成绩查询", null, R.drawable.ic_grades));

        // Completely static items
        mMenuItems.add(new HomeMenuItem("图书馆", null, R.drawable.ic_library));
        mMenuItems.add(new HomeMenuItem("校内通知", null, R.drawable.ic_announcements));
        mMenuItems.add(new HomeMenuItem("留言板", null, R.drawable.ic_msgboard));

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

            setUIBusy(true);

            // Fetch basic info
            SchoolSystemModels.studentBasicInfo(SAGlobal.studentSessionID, null, new ModelListener<StudentBasicInfo>() {
                @Override
                public void onData(StudentBasicInfo result, String message) {
                    setUIBusy(false);
                    if (result == null) {
                        Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        mStudentBasicInfo = result;
                        updateMenu();
                    }
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
                    }
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
                    }
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
        lblTitle.setText(menuItem.title);

        return convertView;
    }
}

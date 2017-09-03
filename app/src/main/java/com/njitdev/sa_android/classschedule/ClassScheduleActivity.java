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

package com.njitdev.sa_android.classschedule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.models.school.ClassSchedule;
import com.njitdev.sa_android.utils.SAGlobal;

import java.util.ArrayList;
import java.util.List;

public class ClassScheduleActivity extends AppCompatActivity {

    private List<List<ClassSchedule>> mClassSchedule;
    private List<Object> mSectionedClassScheduleList;
    private int mSelectedWeekNumber;
    private String[] mWeekDays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_schedule);

        // Process data
        mClassSchedule = SAGlobal.dataClassSchedule;
        mSelectedWeekNumber = SAGlobal.currentWeekNumber;

        if (mClassSchedule == null) {
            Toast.makeText(this, "ERROR: Null Data", Toast.LENGTH_SHORT).show();
            this.finish();
        } else if (mClassSchedule.size() == 0) {
            Toast.makeText(this, "课程表为空，请稍后更新数据", Toast.LENGTH_SHORT).show();
            this.finish();
        } else {
            populateClassScheduleList();
            initListView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_classschedule, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Update week number in menu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String weekNumber = "第" + mSelectedWeekNumber + "周";
        menu.findItem(R.id.menuItemWeekNumber).setTitle(weekNumber);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.menuItemPreviousWeek:
                showPreviousWeek();
                return true;

            case R.id.menuItemNextWeek:
                showNextWeek();
                return true;

            case R.id.menuItemWeekNumber:
                return true;
        }
        return onOptionsItemSelected(item);
    }

    private void showPreviousWeek() {
        if (mSelectedWeekNumber > 1) {
            mSelectedWeekNumber--;
            populateClassScheduleList();
            refreshActionBarMenu(this);
            initListView();
        }
    }

    private void showNextWeek() {
        if (mSelectedWeekNumber >= mClassSchedule.size() - 1) {
            Toast.makeText(this, "已经是最后一周啦", Toast.LENGTH_SHORT).show();
        } else {
            mSelectedWeekNumber++;
            refreshActionBarMenu(this);
            populateClassScheduleList();
            initListView();
        }
    }

    // Refresh action bar to update week number
    private void refreshActionBarMenu(Activity activity) {
        activity.invalidateOptionsMenu();
    }

    // Generate contents for mSectionedClassScheduleList, as
    // Section header string, ClassSchedule objects, ...
    // e.g. "周一", ClassSchedule, ClassSchedule, "周二", ClassSchedule, ...
    private void populateClassScheduleList() {
        mSectionedClassScheduleList = new ArrayList<>();

        // Create a list with 7 empty lists
        List<List<ClassSchedule>> groupedClasses = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            groupedClasses.add(new ArrayList<ClassSchedule>());
        }

        // Get classes of selected week and fill each list in groupedClasses with classes of each day
        List<ClassSchedule> weekClasses = mClassSchedule.get(mSelectedWeekNumber);
        for (int i = 0; i < weekClasses.size(); i++) {
            ClassSchedule c = weekClasses.get(i);
            groupedClasses.get(c.day_of_week - 1).add(c);
        }

        // Fill mSectionedClassScheduleList as defined structure
        for (int i = 0; i < groupedClasses.size(); i++) {
            if (groupedClasses.get(i).size() != 0) {
                mSectionedClassScheduleList.add(mWeekDays[i]);
            }
            for (int j = 0; j < groupedClasses.get(i).size(); j++) {
                mSectionedClassScheduleList.add(groupedClasses.get(i).get(j));
            }
        }
    }

    private void initListView() {
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ClassScheduleAdapterWithTitle(listView.getContext(), mSectionedClassScheduleList));
    }

    private class ClassScheduleAdapterWithTitle extends BaseAdapter {

        private final int CLASS_ITEM = 0;
        private final int HEADER = 1;

        List<Object> mList;
        private Context mContext;

        ClassScheduleAdapterWithTitle(Context context, List<Object> list) {
            mList = list;
            mContext = context;
        }

        @Override
        public int getItemViewType(int position) {
            if (mList.get(position) instanceof ClassSchedule) {
                return CLASS_ITEM;
            } else {
                return HEADER;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        @NonNull
        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                switch (getItemViewType(position)) {
                    case CLASS_ITEM:
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_class_schedule, parent, false);
                        break;

                    case HEADER:
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_section_header, parent, false);
                        break;
                }
            }

            switch (getItemViewType(position)) {
                case CLASS_ITEM:
                    TextView lblSessions = (TextView) convertView.findViewById(R.id.lblSessions);
                    TextView lblTitle = (TextView) convertView.findViewById(R.id.lblTitle);
                    TextView lblInstructor = (TextView) convertView.findViewById(R.id.lblInstructor);
                    TextView lblLocation = (TextView) convertView.findViewById(R.id.lblLocation);

                    ClassSchedule classSchedule = (ClassSchedule) mList.get(position);

                    lblSessions.setText(classSchedule.classes_in_day);
                    lblTitle.setText(classSchedule.title);
                    lblInstructor.setText(classSchedule.instructor);
                    lblLocation.setText(classSchedule.location);
                    break;

                case HEADER:
                    TextView lblHeader = (TextView) convertView.findViewById(R.id.textView_classSchedule_header);
                    lblHeader.setText((String) mList.get(position));
                    break;
            }
            return convertView;
        }
    }
}

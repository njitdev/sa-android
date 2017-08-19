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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.models.school.ClassSchedule;
import com.njitdev.sa_android.utils.SAGlobal;

import java.util.LinkedList;
import java.util.List;

public class ClassScheduleActivity extends AppCompatActivity {

    private ClassScheduleAdapter mAdapter;
    private List<List<ClassSchedule>> mClassSchedule;
    private List<ClassSchedule> mClassScheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_schedule);

        // Process data
        mClassSchedule = SAGlobal.dataClassSchedule;
        if (mClassSchedule == null) {
            Toast.makeText(this, "DEBUG: No Class Data", Toast.LENGTH_SHORT).show();
        } else {
            populateClassScheduleList();
            initListView();
        }
    }

    // TODO: Group by day
    private void populateClassScheduleList() {
        mClassScheduleList = new LinkedList<>();
        for (int i = 0; i < mClassSchedule.size(); i++) {
            for (int j = 0; j < mClassSchedule.get(i).size(); j++) {
                mClassScheduleList.add(mClassSchedule.get(i).get(j));
            }
        }
    }

    private void initListView() {
        final ListView listView = (ListView) findViewById(R.id.listView);
        mAdapter = new ClassScheduleAdapter(listView.getContext(), R.layout.list_item_class_schedule, mClassScheduleList);
        listView.setAdapter(mAdapter);
    }

    private class ClassScheduleAdapter extends ArrayAdapter<ClassSchedule> {

        private Context mContext;
        private int mResource;
        private List<ClassSchedule> mClassSchedules;

        ClassScheduleAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ClassSchedule> classSchedules) {
            super(context, resource, classSchedules);
            this.mContext = context;
            this.mResource = resource;
            this.mClassSchedules = classSchedules;
        }

        @NonNull
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            }

            TextView lblSessions = (TextView) convertView.findViewById(R.id.lblSessions);
            TextView lblTitle = (TextView) convertView.findViewById(R.id.lblTitle);
            TextView lblInstructor = (TextView) convertView.findViewById(R.id.lblInstructor);
            TextView lblLocation = (TextView) convertView.findViewById(R.id.lblLocation);

            ClassSchedule classSchedule = mClassSchedules.get(position);

            lblSessions.setText(classSchedule.classes_in_day);
            lblTitle.setText(classSchedule.title);
            lblInstructor.setText(classSchedule.instructor);
            lblLocation.setText(classSchedule.location);

            return convertView;
        }
    }
}

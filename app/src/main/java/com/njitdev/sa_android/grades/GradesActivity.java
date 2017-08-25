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

package com.njitdev.sa_android.grades;

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
import com.njitdev.sa_android.models.school.GradeItem;
import com.njitdev.sa_android.utils.SAGlobal;

import java.util.ArrayList;
import java.util.List;

public class GradesActivity extends AppCompatActivity {

    private List<GradeItem> mGrade = new ArrayList<>();
    private GradesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        // Create adapter
        // Set data
        mGrade = SAGlobal.dataGrades;

        if (mGrade == null) {
            Toast.makeText(this, "没有成绩数据，请先登录或更新数据", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        } else if (mGrade.size() == 0) {
            Toast.makeText(this, "还没有成绩", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }

        mAdapter = new GradesAdapter(this, R.layout.list_item_grades, mGrade);
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(mAdapter);
    }
}

class GradesAdapter extends ArrayAdapter<GradeItem> {

    private Context mContext;
    private int mResource;
    private List<GradeItem> mGradeItem;

    GradesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<GradeItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mGradeItem = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        // Controls
        TextView lblCourseName = (TextView) convertView.findViewById(R.id.lblCourseName);
        TextView lblCourseInfo = (TextView) convertView.findViewById(R.id.lblCourseInfo);
        TextView lblScore = (TextView) convertView.findViewById(R.id.lblScore);
        TextView lblCredits = (TextView) convertView.findViewById(R.id.lblCredits);

        // Data
        GradeItem grade = mGradeItem.get(position);
        lblCourseName.setText(grade.course_name);
        lblCredits.setText(grade.credits);
        lblScore.setText(grade.score);

        String courseInfo = grade.course_isrequired + ", " + grade.course_category + ", " + grade.exam_type;
        lblCourseInfo.setText(courseInfo);

        return convertView;
    }
}

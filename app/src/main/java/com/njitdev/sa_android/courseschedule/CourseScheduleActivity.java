package com.njitdev.sa_android.courseschedule;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.models.school.ClassSchedule;
import com.njitdev.sa_android.utils.SAGlobal;

import java.util.LinkedList;
import java.util.List;

public class CourseScheduleActivity extends AppCompatActivity {

    private ClassScheduleAdapter mAdapter;
    private List<ClassSchedule> mClassScheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_schedule);
        populateClassScheduleList();
        initListView();
    }

    private void populateClassScheduleList(){
        mClassScheduleList = new LinkedList<>();
            for(int i = 0; i < SAGlobal.mClassSchedule.size(); i++){
            for(int j = 0; j < SAGlobal.mClassSchedule.get(i).size(); j++){
                mClassScheduleList.add(SAGlobal.mClassSchedule.get(i).get(j));
            }
        }
    }

    private void initListView() {
        final ListView listView = (ListView) findViewById(R.id.listViewClassSchedule);

        // Create adapter
        mAdapter = new ClassScheduleAdapter(listView.getContext(), R.layout.list_item_class_schedule, mClassScheduleList);
        listView.setAdapter(mAdapter);

    }

    class ClassScheduleAdapter extends ArrayAdapter<ClassSchedule>{

        private Context mContext;
        private int mResource;
        private List<ClassSchedule> mClassSchedules;

        public ClassScheduleAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ClassSchedule> classSchedules) {
            super(context, resource, classSchedules);
            this.mContext = context;
            this.mResource = resource;
            this.mClassSchedules = classSchedules;
        }

        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            }

            TextView TVclassInDay = (TextView) convertView.findViewById(R.id.TextViewClassInDay);
            TextView TVclassTitle = (TextView) convertView.findViewById(R.id.TextViewClassTitle);
            TextView TVclassInstructor = (TextView) convertView.findViewById(R.id.TextViewClassInstructor);
            TextView TVclassLocation = (TextView) convertView.findViewById(R.id.TextViewClassLocation);

            ClassSchedule classSchedule = mClassSchedules.get(position);

            TVclassInDay.setText(classSchedule.getClasses_in_day());
            TVclassTitle.setText(classSchedule.getTitle());
            TVclassInstructor.setText(classSchedule.getInstructor());
            TVclassLocation.setText(classSchedule.getLocation());

            return convertView;
        }

    }
}

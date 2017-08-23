package com.njitdev.sa_android.classschedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.models.school.ClassSchedule;
import com.njitdev.sa_android.utils.SAGlobal;

import java.util.LinkedList;
import java.util.List;

public class ClassScheduleActivity extends AppCompatActivity {

    //  private ClassScheduleAdapter mAdapter;
    private List<List<ClassSchedule>> mClassSchedule;
    private List<Object> mClassScheduleList;

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
        //TO DO AUG 20;
        //populateClassList by classified day of week;
        List<List<ClassSchedule>> temp = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            List<ClassSchedule> t = new LinkedList<>();
            temp.add(t);
        }
        for (int i = 0; i < mClassSchedule.get(SAGlobal.currentWeekNumber).size(); i++) {
            ClassSchedule ctemp = mClassSchedule.get(SAGlobal.currentWeekNumber).get(i);
            temp.get(ctemp.day_of_week - 1).add(ctemp);
        }
        //populate data list with day as title
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).size() != 0) {
                mClassScheduleList.add("周" + (i + 1));
            }
            for (int j = 0; j < temp.get(i).size(); j++) {
                mClassScheduleList.add(temp.get(i).get(j));
            }
        }
    }

    private void initListView() {
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ClassScheduleAdapterwithTitle(listView.getContext(), mClassScheduleList));
    }

    private class ClassScheduleAdapterwithTitle extends BaseAdapter {

        List<Object> mList;
        private final int CLASS_ITEM = 0;
        private final int HEADER = 1;
        private Context mContext;
        private LayoutInflater mLayoutInflater;

        public ClassScheduleAdapterwithTitle(Context context, List<Object> list) {
            mList = list;
            //   mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_title, parent, false);
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

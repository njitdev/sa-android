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

package com.njitdev.sa_android.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.models.school.ClassSchedule;
import com.njitdev.sa_android.models.school.GradeItem;
import com.njitdev.sa_android.models.school.SchoolSystemModels;
import com.njitdev.sa_android.utils.ModelListener;
import com.njitdev.sa_android.utils.SAGlobal;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button buttonTestReturn = (Button) findViewById(R.id.buttonTestReturn);
        buttonTestReturn.setText("return");
        buttonTestReturn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        populateListView();

        SchoolSystemModels.fetchClassSchedule(SAGlobal.student_session_id, new ModelListener<List<List<ClassSchedule>>>() {
            @Override
            public void onData(List<List<ClassSchedule>> result, String message) {
                if (result == null) {
                    Toast.makeText(TestActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("sa-api", "" + result.size());
                Log.d("sa-api", "current_week_in_term " + SAGlobal.current_week_in_term);
            }
        });

        SchoolSystemModels.fetchGrade(SAGlobal.student_session_id, new ModelListener<ArrayList<GradeItem>>() {
            @Override
            public void onData(ArrayList<GradeItem> result, String message) {
                if (result == null) {
                    Toast.makeText(TestActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("sa-api", "grade " + result.size());
            }
        });
    }

    private void populateListView() {
        String[] data = {"yellow", "blue", "New Color!"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                data);

        ListView list = (ListView) findViewById(R.id.listViewTest);
        list.setAdapter(adapter);
    }
}

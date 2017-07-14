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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.njitdev.sa_android.R;
import com.njitdev.sa_android.library.LibraryActivity;
import com.njitdev.sa_android.login.LoginActivity;
import com.njitdev.sa_android.messageboard.MessageBoardActivity;
import com.njitdev.sa_android.school_announcement.SchoolAnnouncementActivity;
import com.njitdev.sa_android.test.TestActivity;
import com.njitdev.sa_android.utils.SAGlobal;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize shared request queue
        SAGlobal.sharedRequestQueue = Volley.newRequestQueue(getApplicationContext());

        Button buttonHomeMessageBoard = (Button) findViewById(R.id.buttonHomeMessageBoard);
        buttonHomeMessageBoard.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, MessageBoardActivity.class);
                        startActivity(intent);
                    }
                });

        Button buttonHomeTest = (Button) findViewById(R.id.buttonHomeTest);
        buttonHomeTest.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, TestActivity.class);
                        startActivity(intent);
                    }
                });

        Button buttonHomeSchoolAnnouncement = (Button) findViewById(R.id.buttonHomeSchoolAnnouncement);
        buttonHomeSchoolAnnouncement.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, SchoolAnnouncementActivity.class);
                        startActivity(intent);
                    }
                });

        Button btnLibrary = (Button) findViewById(R.id.btnLibrary);
        btnLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LibraryActivity.class);
                startActivity(intent);
            }
        });

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView lblSessionID = (TextView) findViewById(R.id.lblSessionID);
        lblSessionID.setText("session_id: " + SAGlobal.student_session_id);
    }
}

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
                        openMessageBoard();
                    }
                });

        Button buttonHomeTest = (Button) findViewById(R.id.buttonHomeTest);
        buttonHomeTest.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        test();
                    }
                });

        Button buttonHomeSchoolAnnouncement = (Button) findViewById(R.id.buttonHomeSchoolAnnouncement);
        buttonHomeSchoolAnnouncement.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        schoolAnnouncement();
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

    public void openMessageBoard() {
        Intent intent = new Intent(this, MessageBoardActivity.class);
        startActivity(intent);
    }

    public void test() {
        Intent intent2 = new Intent(this, TestActivity.class);
        startActivity(intent2);
    }

    public void schoolAnnouncement() {
        Intent intent3 = new Intent(this, SchoolAnnouncementActivity.class);
        startActivity(intent3);
    }
}

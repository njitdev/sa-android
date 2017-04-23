package com.njitdev.sa_android.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.volley.toolbox.Volley;
import com.njitdev.sa_android.R;
import com.njitdev.sa_android.messageboard.MessageBoardActivity;
import com.njitdev.sa_android.test.TestActivity;
import com.njitdev.sa_android.utils.SAGlobal;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SAGlobal.getInstance().sharedRequestQueue = Volley.newRequestQueue(getApplicationContext());

        Button buttonHomeMessageBoard = (Button)findViewById(R.id.buttonHomeMessageBoard);
        buttonHomeMessageBoard.setText("留言板");
        buttonHomeMessageBoard.setOnClickListener(
                new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                openMessageBoard();
            }
        });

      Button buttonHomeTest = (Button)findViewById(R.id.buttonHomeTest);
        buttonHomeTest.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        test();
                    }
                });

    }

    public void openMessageBoard() {
        Intent intent = new Intent(this, MessageBoardActivity.class);
        startActivity(intent);
    }

    public void test() {
        Intent intent2 = new Intent(this, TestActivity.class);
        startActivity(intent2);
    }
}

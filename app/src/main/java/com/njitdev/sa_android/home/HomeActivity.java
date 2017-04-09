package com.njitdev.sa_android.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.messageboard.MessageBoardActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button buttonHomeMessageBoard = (Button)findViewById(R.id.buttonHomeMessageBoard);
        buttonHomeMessageBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMessageBoard(v);
            }
        });
    }

    public void openMessageBoard(View view) {
        Intent intent = new Intent(this, MessageBoardActivity.class);
        startActivity(intent);
    }
}

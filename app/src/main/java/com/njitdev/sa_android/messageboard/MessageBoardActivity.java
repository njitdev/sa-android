package com.njitdev.sa_android.messageboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.home.HomeActivity;
import com.njitdev.sa_android.utils.ModelListener;

import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class MessageBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);

        final TextView textMessageboardCounter = (TextView)findViewById(R.id.textMessageboardCounter);

        textMessageboardCounter.setText("get message number");

        Models.fetchList(new ModelListener() {
            @Override
            public void onData(boolean success, Object object) {
                if(object == null){
                    textMessageboardCounter.setText("Fail to get messages");
                }else{
                   ArrayList<Post> posts = (ArrayList<Post>)object;
                   textMessageboardCounter.setText("Got "+posts.size()+" Messages");
                    //textMessageboardCounter.setText("Got Messages");
                }
            }
        });

        Button buttonMessageboardReturn = (Button)findViewById(R.id.buttonMessageboardReturn);
        buttonMessageboardReturn.setText("Home");

        buttonMessageboardReturn.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}

package com.njitdev.sa_android.messageboard;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.home.HomeActivity;
import com.njitdev.sa_android.utils.ModelListener;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

public class MessageBoardActivity extends AppCompatActivity {
    ArrayList<Post> posts;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.messageboard,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;


            case R.id.MessageBoardnewMessage:
            Intent intent = new Intent(this, NewMessage.class);
            startActivity(intent);
            return true;
        }

        return onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);

    //    ActionBar actionBar = getSupportActionBar();
    //    actionBar.setDisplayHomeAsUpEnabled(true);

        final TextView textMessageboardCounter = (TextView)findViewById(R.id.textMessageboardCounter);

        textMessageboardCounter.setText("正在更新留言信息");

        Models.fetchList(new ModelListener() {
            @Override
            public void onData(boolean success, Object object) {
                if(object == null){
                    textMessageboardCounter.setText("Fail to get messages");
                }else{
                   posts = (ArrayList<Post>)object;
                   textMessageboardCounter.setText("共有 "+posts.size()+" 条留言");
                    messageList();
                }
            }
        });

    }

    private void messageList(){
        List<String> message = new ArrayList<String>();
        for(Post p:posts){
            message.add(p.text);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,R.layout.dl3, message);
        ListView list = (ListView)findViewById(R.id.listViewMessageboardText);
        list.setAdapter(adapter);
    }

}

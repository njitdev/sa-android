package com.njitdev.sa_android.messageboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.njitdev.sa_android.R;
import com.njitdev.sa_android.utils.ModelListener;
import com.njitdev.sa_android.utils.SAConfig;
import com.njitdev.sa_android.utils.SAGlobal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageBoardActivity extends AppCompatActivity {
    ArrayList<Post> posts = new ArrayList<>();
    String id;
    String message;
    int count=0;
    MessageAdapter adapter1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.messageboard,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.MessageBoardnewMessage:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MessageBoardActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.activity_new_message, null);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();


                Button b = (Button) mView.findViewById(R.id.ButtonNewMessage);
                final TextView idtv = (TextView) mView.findViewById(R.id.textViewNewMessageID);
                final TextView messagetv = (TextView) mView.findViewById(R.id.textViewNewMessageMessage);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        id = idtv.getText().toString();
                        message = messagetv.getText().toString();
                        if(id.length()==0||message.length()==0){
                            Toast.makeText(getApplicationContext(), "用户名或留言不能为空", Toast.LENGTH_LONG).show();
                        }else {
                            createNewMessage();
                            dialog.cancel();
                        }
                    }
                });
            return true;
        }
        return onOptionsItemSelected(item);
    }

    public void createNewMessage(){
        String url = SAConfig.baseURL + "/app/msgboard/gdut/posts";
        Map<String, String> postMessage = new HashMap<>();
        postMessage.put("installation_id", "IDbyIris");
        postMessage.put("text",message);
        postMessage.put("user_name",id);

        JSONObject post = new JSONObject(postMessage);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, post, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "添加留言成功", Toast.LENGTH_LONG).show();
                showMessage();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "添加留言失败（from网络库）", Toast.LENGTH_LONG).show();
            }
        });

        SAGlobal.getInstance().sharedRequestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);
        fetchMessage(count);
    }

    private void fetchMessage(int page){
        final TextView textMessageboardCounter = (TextView) findViewById(R.id.textMessageboardCounter);
        textMessageboardCounter.setText("正在更新留言信息");

        Models.fetchList(new ModelListener() {
            @Override
            public void onData(boolean success, Object object) {
                if (object == null) {
                    textMessageboardCounter.setText("获取留言信息失败");
                } else {
                    ArrayList<Post> mypost = (ArrayList<Post>) object;
                    posts.addAll(mypost);
                    textMessageboardCounter.setText("");
                    showMessage();
                }
            }
        }, page);
    }

    private void showMessage() {
        if (adapter1 != null) {
            adapter1.notifyDataSetChanged();
            return;
        }

        final ListView list = (ListView) findViewById(R.id.listViewMessageboardText);

        adapter1 = new MessageAdapter(list.getContext(), R.layout.message_board_rowcontext, posts);
        list.setAdapter(adapter1);

        list.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (list.getLastVisiblePosition() - list.getHeaderViewsCount() -
                        list.getFooterViewsCount()) >= (adapter1.getCount() - 1)) {
                    count++;
                    fetchMessage(count);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }
}

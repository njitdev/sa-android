package com.njitdev.sa_android.messageboard;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.utils.ModelListener;

import java.util.ArrayList;

public class MessageBoardActivity extends AppCompatActivity {

    MessageAdapter messageAdapter;
    ArrayList<Post> mPosts = new ArrayList<>();
    int pageNum = 0; // Current page number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);

        initListView();
        fetchMessage(pageNum);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.messageboard,menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Navigation bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.MessageBoardnewMessage:
                newPost();
                return true;
        }
        return onOptionsItemSelected(item);
    }

    private void initListView() {
        final ListView listView = (ListView) findViewById(R.id.listViewMessageboardText);

        // Create adapter
        messageAdapter = new MessageAdapter(listView.getContext(), R.layout.message_board_rowcontext, mPosts);
        listView.setAdapter(messageAdapter);

        // Set listener for scroll down;
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() -
                        listView.getFooterViewsCount()) >= (messageAdapter.getCount() - 1)) {
                    pageNum++;
                    fetchMessage(pageNum);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
        });
    }

    private void setBusy(boolean busy) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressLoading);
        if (busy) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    //fetch json data and show
    private void fetchMessage(int page) {
        setBusy(true);
        Models.fetchList(page, new ModelListener() {
            @Override
            public void onData(Object result, String message) {
                setBusy(false);

                if (result == null) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    ArrayList<Post> posts = (ArrayList<Post>) result;
                    mPosts.addAll(posts);

                    // Update ListView
                    messageAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    // New post dialog
    public void newPost() {
        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MessageBoardActivity.this);
        View view = getLayoutInflater().inflate(R.layout.activity_new_message, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Check and get input value
        final TextView txtUserName = (TextView) view.findViewById(R.id.txtUserName);
        final TextView txtUserContact = (TextView) view.findViewById(R.id.txtUserContact);
        final TextView txtPostText = (TextView) view.findViewById(R.id.txtPostText);
        Button btnSubmitMessage = (Button) view.findViewById(R.id.btnSubmitMessage);

        btnSubmitMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = txtUserName.getText().toString();
                String user_contact = txtUserContact.getText().toString();
                String text = txtPostText.getText().toString();

                if (user_name.length() == 0 || text.length() == 0) {
                    Toast.makeText(getApplicationContext(), "用户名或留言不能为空", Toast.LENGTH_LONG).show();
                } else {
                    Post post = new Post();
                    post.user_name = user_name;
                    post.text = text;
                    post.user_contact = user_contact;

                    setBusy(true);
                    Models.submitPost("IDbyIris", post, new ModelListener() {
                        @Override
                        public void onData(Object result, String message) {
                            setBusy(false);

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            if (result == Boolean.TRUE) {
                                mPosts.clear();
                                fetchMessage(0);
                            }
                        }
                    });
                    dialog.cancel();
                }
            }
        });
    }
}

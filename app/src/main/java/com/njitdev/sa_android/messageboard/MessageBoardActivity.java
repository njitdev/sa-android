package com.njitdev.sa_android.messageboard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.utils.ModelListener;
import com.njitdev.sa_android.utils.SAGlobal;

import java.util.ArrayList;
import java.util.List;

public class MessageBoardActivity extends AppCompatActivity {

    MessageBoardAdapter mAdapter;
    ArrayList<Post> mPosts = new ArrayList<>();
    int mPageNum = 0; // Current page number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);

        initListView();
        fetchMessage(mPageNum);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.messageboard, menu);
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
                newPostDialog();
                return true;
        }
        return onOptionsItemSelected(item);
    }

    private void initListView() {
        final ListView listView = (ListView) findViewById(R.id.listViewMessageboardText);

        // Create adapter
        mAdapter = new MessageBoardAdapter(listView.getContext(), R.layout.list_item_message_board, mPosts);
        listView.setAdapter(mAdapter);

        // Set listener for scroll down;
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() -
                        listView.getFooterViewsCount()) >= (mAdapter.getCount() - 1)) {
                    mPageNum++;
                    fetchMessage(mPageNum);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private void setUIBusy(boolean busy) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressLoading);
        if (busy) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    // Fetch and display list
    private void fetchMessage(int page) {
        setUIBusy(true);
        MessageBoardModels.fetchList(page, new ModelListener<List<Post>>() {
            @Override
            public void onData(List<Post> result, String message) {
                setUIBusy(false);

                if (result == null) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    mPosts.addAll(result);

                    // Update ListView
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    // New post dialog
    public void newPostDialog() {
        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MessageBoardActivity.this);
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_new_message, (ViewGroup) this.findViewById(R.id.content));
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Dialog buttons
        Button btnSubmitMessage = (Button) dialogView.findViewById(R.id.btnSubmitMessage);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);

        btnSubmitMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                TextView txtUserName = (TextView) dialogView.findViewById(R.id.lblUserName);
                TextView txtUserContact = (TextView) dialogView.findViewById(R.id.txtUserContact);
                TextView txtPostText = (TextView) dialogView.findViewById(R.id.txtPostText);
                String user_name = txtUserName.getText().toString();
                String user_contact = txtUserContact.getText().toString();
                String text = txtPostText.getText().toString();

                if (user_name.length() == 0 || text.length() == 0) {
                    Toast.makeText(getApplicationContext(), "用户名或留言不能为空", Toast.LENGTH_LONG).show();
                } else {
                    Post post = new Post();
                    post.installation_id = SAGlobal.installation_id;
                    post.user_name = user_name;
                    post.text = text;
                    post.user_contact = user_contact;

                    setUIBusy(true);

                    // Submit
                    MessageBoardModels.submitPost(post, new ModelListener<Boolean>() {
                        @Override
                        public void onData(Boolean result, String message) {
                            setUIBusy(false);

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            if (result == Boolean.TRUE) {
                                // Reload list
                                mPosts.clear();
                                fetchMessage(0);
                            }
                        }
                    });
                    dialog.dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }
}

class MessageBoardAdapter extends ArrayAdapter<Post> {

    private Context mContext;
    private List<Post> mPosts;

    MessageBoardAdapter(Context context, int resource, List<Post> posts) {
        super(context, resource, posts);
        mContext = context;
        mPosts = posts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_message_board, parent, false);
        }

        TextView lblUserName = (TextView) convertView.findViewById(R.id.lblUserName);
        TextView lblUserTitle = (TextView) convertView.findViewById(R.id.lblUserTitle);
        TextView lblMessage = (TextView) convertView.findViewById(R.id.lblMessage);

        Post post = mPosts.get(position);

        lblUserName.setText(post.user_name);
        lblMessage.setText(post.text);
        lblUserTitle.setText(post.user_title == null ? "" : post.user_title);

        return convertView;
    }
}

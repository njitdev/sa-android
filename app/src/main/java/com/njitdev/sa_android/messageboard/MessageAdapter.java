package com.njitdev.sa_android.messageboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.njitdev.sa_android.R;

import java.util.List;

/**
 * Created by WZ on 4/16/17.
 */

public class MessageAdapter extends ArrayAdapter<Post>{
    protected Context mContext;
    protected List<Post> mPosts;

    public MessageAdapter(Context context, int resource, List<Post> posts) {
        super(context, resource, posts);
        mContext = context;
        mPosts = posts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.message_board_rowcontext, null
            );
            holder = new ViewHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.messageBoardRowUserName);
            holder.messageContext = (TextView) convertView.findViewById(R.id.messageBoardRowtext);
            holder.time = (TextView) convertView.findViewById(R.id.messageBoardRowTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        Post post = mPosts.get(position);

        String username = post.user_name;
        holder.userName.setText(username);

        String message = post.text;
        holder.messageContext.setText(message);

     //   String time = post.creation_time;
     //   holder.time.setText(time);

        return convertView;
    }

    public static class ViewHolder {
        TextView userName;
        TextView messageContext;
        TextView time;
    }
}

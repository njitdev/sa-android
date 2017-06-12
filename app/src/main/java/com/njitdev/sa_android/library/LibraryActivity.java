package com.njitdev.sa_android.library;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.utils.ModelListener;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    private List<Book> mBooks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // Create adapter
        ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayAdapter libraryAdapter = new LibraryAdapter(this, android.R.layout.simple_list_item_1, mBooks);
        listView.setAdapter(libraryAdapter);

        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        final EditText txtKeyword = (EditText) findViewById(R.id.txtKeyword);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibraryModels.search(txtKeyword.getText().toString(), new ModelListener() {
                    @Override
                    public void onData(Object result, String message) {
                        // Update data of adapter
                        mBooks.clear();
                        mBooks.addAll((ArrayList<Book>)result);
                        libraryAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}

class LibraryAdapter extends ArrayAdapter {

    private Context mContext;
    private List<Book> mBooks;

    public LibraryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Book> objects) {
        super(context, resource, objects);
        mContext = context;
        mBooks = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_library_search, parent, false);
        }

        // Controls
        TextView lblTitle = (TextView) convertView.findViewById(R.id.lblTitle);
        TextView lblSubtitle = (TextView) convertView.findViewById(R.id.lblSubtitle);

        // Data
        Book b = mBooks.get(position);
        lblTitle.setText(b.title);
        lblSubtitle.setText(b.author);

        return convertView;
    }
}

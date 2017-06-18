package com.njitdev.sa_android.library;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        final ListView listView = (ListView) findViewById(R.id.listView);
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
                        if (result == null) {
                            Toast.makeText(LibraryActivity.this, message, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Update data of adapter
                        mBooks.clear();
                        mBooks.addAll((ArrayList<Book>)result);
                        libraryAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
//        // Hide keyboard
//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LibraryActivity.this, BookDetailsActivity.class);
                Book book = mBooks.get(position);
                // Pass book info to details activity
                intent.putExtra("book_id", book.id);
                intent.putExtra("book_title", book.title);
                intent.putExtra("book_author", book.author);
                intent.putExtra("book_publisher", book.publisher);
                intent.putExtra("book_year", book.year);
                intent.putExtra("book_acquisition_number", book.acquisition_number);
                intent.putExtra("book_inventory", book.inventory);
                intent.putExtra("book_available", book.available);

                startActivity(intent);
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
        TextView lblAuthor = (TextView) convertView.findViewById(R.id.lblAuthor);
        TextView lblInventory = (TextView) convertView.findViewById(R.id.lblInventory);
        TextView lblAvailable = (TextView) convertView.findViewById(R.id.lblAvailable);
        TextView lblYear = (TextView) convertView.findViewById(R.id.lblYear);
        TextView lblPublisher = (TextView) convertView.findViewById(R.id.lblPublisher);
        TextView lblAcqNum = (TextView) convertView.findViewById(R.id.lblAcqNum);

        // Data
        Book b = mBooks.get(position);
        lblTitle.setText(b.title);
        lblAuthor.setText(b.author);
        String storeNum = "馆藏:" + b.inventory;
        String availableNum = "可借:" + b.available;
        lblInventory.setText(storeNum);
        lblAvailable.setText(availableNum);
        lblYear.setText(b.year);
        lblPublisher.setText(b.publisher);
        lblAcqNum.setText(b.acquisition_number);

        return convertView;
    }
}

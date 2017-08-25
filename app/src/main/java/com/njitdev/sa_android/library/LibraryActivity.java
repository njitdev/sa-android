/*
    sa-android
    Copyright (C) 2017 sa-android authors

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.njitdev.sa_android.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.models.library.Book;
import com.njitdev.sa_android.models.library.LibraryModels;
import com.njitdev.sa_android.utils.ModelListener;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    private List<Book> mBooks = new ArrayList<>();
    private LibraryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // Set not busy
        setUIBusy(false);

        // Create adapter
        mAdapter = new LibraryAdapter(this, R.layout.list_item_library_search, mBooks);
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(mAdapter);

        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        final EditText txtKeyword = (EditText) findViewById(R.id.txtKeyword);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check the input
                if (txtKeyword.getText().toString().isEmpty()) {
                    Toast.makeText(LibraryActivity.this, "请输入关键字", Toast.LENGTH_SHORT).show();
                } else {
                    // Set busy
                    setUIBusy(true);

                    LibraryModels.search(txtKeyword.getText().toString(), new ModelListener<List<Book>>() {
                        @Override
                        public void onData(List<Book> result, String message) {

                            // Set not busy
                            setUIBusy(false);

                            if (result == null) {
                                Toast.makeText(LibraryActivity.this, message, Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Update data of adapter
                            mBooks.clear();
                            mBooks.addAll(result);
                            mAdapter.notifyDataSetChanged();

                            // Hide keyboard
                            View view = LibraryActivity.this.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                        }
                    });
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LibraryActivity.this, LibraryBookDetailsActivity.class);
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

    private void setUIBusy(Boolean busy) {
        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        EditText txtKeyword = (EditText) findViewById(R.id.txtKeyword);

        // Enable / disable
        btnSearch.setEnabled(!busy);
        txtKeyword.setEnabled(!busy);

        // Spinner
        ProgressBar pbSearch = (ProgressBar) findViewById(R.id.pbSearch);
        if (busy) {
            pbSearch.setVisibility(View.VISIBLE);
        } else {
            pbSearch.setVisibility(View.INVISIBLE);
        }
    }
}

class LibraryAdapter extends ArrayAdapter<Book> {

    private Context mContext;
    private int mResource;
    private List<Book> mBooks;

    LibraryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Book> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mBooks = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
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

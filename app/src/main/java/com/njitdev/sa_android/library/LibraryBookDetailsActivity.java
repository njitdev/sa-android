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
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.models.library.BookInventory;
import com.njitdev.sa_android.models.library.LibraryModels;
import com.njitdev.sa_android.utils.ModelListener;

import java.util.ArrayList;
import java.util.List;

public class LibraryBookDetailsActivity extends AppCompatActivity {

    private List<BookInventory> mInventory = new ArrayList<>();
    private DetailsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_book_details);

        // Create adapter
        ListView listView = (ListView) findViewById(R.id.listView);
        mAdapter = new DetailsAdapter(this, R.layout.list_item_book_details, mInventory);
        listView.setAdapter(mAdapter);
        final ProgressBar pbBusy = (ProgressBar) findViewById(R.id.pbBusy);

        // Show ProgressBar
        pbBusy.setVisibility(View.VISIBLE);

        String book_id = getIntent().getStringExtra("book_id");
        String book_title = getIntent().getStringExtra("book_title");
        String book_author = getIntent().getStringExtra("book_author");
        String book_publisher = getIntent().getStringExtra("book_publisher");
        String book_year = getIntent().getStringExtra("book_year");
        String book_acquisition_number = getIntent().getStringExtra("book_acquisition_number");
        String book_inventory = getIntent().getStringExtra("book_inventory");
        String book_available = getIntent().getStringExtra("book_available");

        LibraryModels.details(book_id, new ModelListener<List<BookInventory>>() {
            @Override
            public void onData(List<BookInventory> result, String message) {
                // Hide ProgressBar
                pbBusy.setVisibility(View.INVISIBLE);

                if (result == null) {
                    Toast.makeText(LibraryBookDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                mInventory.clear();
                mInventory.addAll(result);
                mAdapter.notifyDataSetChanged();
            }
        });

        TextView lblTitle = (TextView) findViewById(R.id.lblTitle);
        TextView lblAuthor = (TextView) findViewById(R.id.lblAuthor);
        TextView lblInventory = (TextView) findViewById(R.id.lblInventory);
        TextView lblAvailable = (TextView) findViewById(R.id.lblAvailable);
        TextView lblYear = (TextView) findViewById(R.id.lblYear);
        TextView lblPublisher = (TextView) findViewById(R.id.lblPublisher);
        TextView lblAcqNum = (TextView) findViewById(R.id.lblAcqNum);

        if (book_title != null) lblTitle.setText(book_title);
        if (book_author != null) lblAuthor.setText(book_author);
        String storeNum = "馆藏:" + book_inventory;
        String availableNum = "可借:" + book_available;
        if (book_inventory != null) lblInventory.setText(storeNum);
        if (book_available != null) lblAvailable.setText(availableNum);
        if (book_year != null) lblYear.setText(book_year);
        if (book_publisher != null) lblPublisher.setText(book_publisher);

        if (book_acquisition_number != null) lblAcqNum.setText(book_acquisition_number);
    }
}

class DetailsAdapter extends ArrayAdapter<BookInventory> {

    private Context mContext;
    private int mResource;
    private List<BookInventory> mInventory;

    DetailsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<BookInventory> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mInventory = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        // Controls
        TextView lblLocation = (TextView) convertView.findViewById(R.id.lblLocation);
        TextView lblAvailability = (TextView) convertView.findViewById(R.id.lblAvailability);
        TextView lblLoginNumber = (TextView) convertView.findViewById(R.id.lblLoginNumber);
        TextView lblType = (TextView) convertView.findViewById(R.id.lblType);

        // Data
        BookInventory b = mInventory.get(position);
        lblLocation.setText(b.location);
        lblAvailability.setText(b.availability);
        lblLoginNumber.setText(b.login_number);
        lblType.setText(b.type);

        return convertView;
    }
}

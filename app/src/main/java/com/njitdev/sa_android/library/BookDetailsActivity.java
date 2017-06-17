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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.utils.ModelListener;

import java.util.ArrayList;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {

    private List<BookInventory> mInventory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Create adapter
        ListView listView = (ListView) findViewById(R.id.listView);
        final DetailAdapter detailAdapter = new DetailAdapter(this, android.R.layout.simple_list_item_1, mInventory);
        listView.setAdapter(detailAdapter);

        // How to get ID?
        String bookId = getIntent().getStringExtra("Book_Id");
        LibraryModels.details(bookId, new ModelListener() {
                    @Override
                    public void onData(Object result, String message) {
                        if (result == null) {
                            Toast.makeText(BookDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mInventory.clear();
                        mInventory.addAll((ArrayList<BookInventory>)result);
                        detailAdapter.notifyDataSetChanged();
                    }
                });

    }
}


class DetailAdapter extends ArrayAdapter {

    private Context mContext;
    private List<BookInventory> mInventory;

    public DetailAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<BookInventory> objects) {
        super(context, resource, objects);
        mContext = context;
        mInventory = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_book_details, parent, false);
        }

        // Controls
        TextView txtLocation = (TextView) convertView.findViewById(R.id.txtLocation);
        TextView txtAvailability = (TextView) convertView.findViewById(R.id.txtAvailability);
        TextView txtAcquisition_number = (TextView) convertView.findViewById(R.id.txtAcquisition_number);
        TextView txtType = (TextView) convertView.findViewById(R.id.txtType);

        // Data
        BookInventory b = mInventory.get(position);
        txtLocation.setText(b.location);
        txtAvailability.setText(b.availability);
        txtAcquisition_number.setText(b.acquisition_number);
        txtType.setText(b.type);

        return convertView;
    }
}

package com.njitdev.sa_android.library;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.utils.ModelListener;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        final EditText txtKeyword = (EditText) findViewById(R.id.txtKeyword);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibraryModels.search(txtKeyword.getText().toString(), new ModelListener() {
                    @Override
                    public void onData(Object result, String message) {
                        Log.d("sa-android", "Result Size: " + ((ArrayList<Book>)result).size());
                    }
                });
            }
        });

    }
}

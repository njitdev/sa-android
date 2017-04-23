package com.njitdev.sa_android.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.njitdev.sa_android.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button buttonTestReturn = (Button)findViewById(R.id.buttonTestReturn);
        buttonTestReturn.setText("return");
        buttonTestReturn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        populateListView();
    }

    private void populateListView(){
        String[] data = {"yellow","blue","New Color!"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.dl3,
                data);

        ListView list = (ListView) findViewById(R.id.listViewTest);
        list.setAdapter(adapter);

    }
}

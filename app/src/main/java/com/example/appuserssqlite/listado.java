package com.example.appuserssqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class listado extends AppCompatActivity {
    TextView mfullname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);
        mfullname = findViewById(R.id.tvfullname);
        mfullname.setText(getIntent().getStringExtra("mfullname"));
    }
}
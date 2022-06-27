package com.example.accountinggoods.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.accountinggoods.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void operationsClick(View view) {
        Intent i = new Intent(MainActivity.this, OperationsLogActivity.class);
        startActivity(i);
    }

    public void suppliersButtonClick(View view) {
        Intent i = new Intent(MainActivity.this, SuppliersActivity.class);
        startActivity(i);
    }

    public void reportButtonClick(View view) {
        Intent i = new Intent(MainActivity.this, ReportActivity.class);
        startActivity(i);
    }
    public void programButtonClick(View view) {
        Intent i = new Intent(MainActivity.this, help_about_the_program.class);
        startActivity(i);
    }
}
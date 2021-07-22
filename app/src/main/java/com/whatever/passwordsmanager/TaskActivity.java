package com.whatever.passwordsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class TaskActivity extends AppCompatActivity {

    MaterialButton btn_add_pass;
    MaterialButton btn_view_passwords;

    //SQLITE
    static SQLiteDatabase db;
    SQLiteOpenHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //Initialising UI
        btn_add_pass=findViewById(R.id.btn_add_password);
        btn_view_passwords=findViewById(R.id.btn_View_password);

        //initialising Database
        helper=new myHelper(this);
        db=helper.getWritableDatabase();


        //OnClickEvents
        btn_add_pass.setOnClickListener(v -> {

            startActivity(new Intent(this,AddNewPasswordActivity.class));
        });

        btn_view_passwords.setOnClickListener(v -> {

            startActivity(new Intent(this,Auth2Activity.class));

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
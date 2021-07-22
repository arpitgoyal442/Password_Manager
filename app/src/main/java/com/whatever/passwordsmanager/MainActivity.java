package com.whatever.passwordsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btn_setKey;
    private EditText edt_text_key;
     static SharedPreferences pref;
     static SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref=getPreferences(Context.MODE_PRIVATE);
        editor=pref.edit();




        //Initialising  Ui Components
        btn_setKey=findViewById(R.id.btn_setKey);
        edt_text_key=findViewById(R.id.edt_text_key);

        //On Click On btn_SetKey
        btn_setKey.setOnClickListener(v -> {

//            startActivity(new Intent(this,HomeActivity.class));
            //Save this key into Storage And From the next time we will see if we have key in storage or not
            // if we have key in storage then no need to show this main activity just show  InputKeyActivity

            String masterKey=edt_text_key.getText().toString();
            editor.putString("masterKey",masterKey);
            editor.apply();
            Toast.makeText(this, "All set now", Toast.LENGTH_SHORT).show();
            navigateToOtherActivity();
            finish();



        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        if(pref.contains("masterKey")){
            navigateToOtherActivity();
            finish();
        }
    }

    private void navigateToOtherActivity(){
        startActivity(new Intent(this,HomeActivity.class)) ;
    }
}
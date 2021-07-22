package com.whatever.passwordsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {

    TextView txt_welcome;
    EditText edt_txt_key;
    MaterialButton btn_done;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        //Initialising Ui Components
        txt_welcome=findViewById(R.id.Welcome);
        edt_txt_key=findViewById(R.id.edt_text_key);
        btn_done=findViewById(R.id.auth2_btn);



        btn_done.setOnClickListener(v -> {

            String enteredkey=edt_txt_key.getText().toString();
//            String masterKey=getSharedPreferences("masterKey", Context.MODE_PRIVATE);
            String masterKey=MainActivity.pref.getString("masterKey","null");
            if(enteredkey.equals(masterKey))
                startActivity(new Intent(this,TaskActivity.class));

            else {
                edt_txt_key.setText("");
                Toast.makeText(this, "Wrong Master Key Entered", Toast.LENGTH_SHORT).show();
            }

        });



    }


}
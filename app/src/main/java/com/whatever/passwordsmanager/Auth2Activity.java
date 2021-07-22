package com.whatever.passwordsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class Auth2Activity extends AppCompatActivity {

    MaterialButton btn;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth2);

        btn=findViewById(R.id.auth2_btn);
        editText=findViewById(R.id.auth2_edt);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entered=editText.getText().toString();
                String masterKey=MainActivity.pref.getString("masterKey","null");
                if(entered.equals(masterKey))
                    startActivity(new Intent(Auth2Activity.this,ViewPasswordsActivity.class));
                else{
                    editText.setText("");
                    Toast.makeText(Auth2Activity.this, "Wrong MasterKey", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
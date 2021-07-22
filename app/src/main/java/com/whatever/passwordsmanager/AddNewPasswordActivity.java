package com.whatever.passwordsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

public class AddNewPasswordActivity extends AppCompatActivity {

    private MaterialButton btn_save;
    private EditText edt_title;
    private EditText edt_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_password);

        //Initialising Ui Components
        btn_save=findViewById(R.id.auth2_btn);
        edt_title=findViewById(R.id.popup_edt_title);
        edt_password=findViewById(R.id.auth2_edt);

        btn_save.setOnClickListener(v -> {

            putInDatabase();
        });



    }

    private void putInDatabase(){
        String title=edt_title.getText().toString();
        String pass=edt_password.getText().toString();

        if(title.equals("")||pass.equals(""))
        {
            Toast.makeText(this, "Empty Field", Toast.LENGTH_SHORT).show();
            return;
        }

        //Todo: Encrypt the password
        String encryptedText=encryptText(pass);

        //ToDo: Add  new encrypted password to database .Do it in background
        insertData(title,encryptedText);
    }

    public static String encryptText(String p)
    {

        try {
            String encryptedText= AESCrypt.encrypt(MainActivity.pref.getString("masterKey","null"),p);
            return encryptedText;

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return "null";
        }
    }

    private  void insertData( String title,String password )
    {
        ContentValues values = new ContentValues();
        values.put("Title", title);
        values.put("Password", password);

        TaskActivity.db.insert("AllPasswords", null, values);
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        edt_title.setText("");
        edt_password.setText("");
    }
}
package com.whatever.passwordsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.scottyab.aescrypt.AESCrypt;

import org.w3c.dom.Text;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class ViewPasswordsActivity extends AppCompatActivity {


    //List for pass and title
    List<String> alltitles,allpasswords;
    ListView my_list_view;
    Cursor cursor;
    ListView my_listView;
    simpleAdapter adapter;
     ArrayList<TextView> passid;

    boolean copyvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_passwords);



        passid=new ArrayList<>();
        my_list_view=findViewById(R.id.my_listView);
        alltitles=new ArrayList<>();
        allpasswords=new ArrayList<>();
        my_list_view=findViewById(R.id.my_listView);

         cursor = TaskActivity.db.query("AllPasswords",
                new String[] {"_id","Title", "Password"},
                null, null, null, null, null);



                if(cursor.moveToFirst()==false)
                {
                    Toast.makeText(this, "Nothing to show", Toast.LENGTH_SHORT).show();
                    return;
                }

        cursor.moveToFirst();

        do{

            String title= cursor.getString(1);
            String pass= cursor.getString(2);

            String decryptpass=decryptText(pass);
            //Toast.makeText(this, decryptpass, Toast.LENGTH_SHORT).show();


            alltitles.add(title);
            allpasswords.add(decryptpass);




        }while (cursor.moveToNext());
//        cursor.close();
//        TaskActivity.db.close();

         adapter=new simpleAdapter(this,alltitles,allpasswords);
        my_list_view.setAdapter(adapter);



    }

    public class simpleAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private Context mcontext;
        private TextView title;
        private TextView password;

        MaterialButton btn_copy;
        MaterialButton switch_type;
        MaterialButton delete_pass;


        private  List<String> titlearray;
        private  List<String> passarray;





        public simpleAdapter(Context c, List<String>t,List<String>p)
        {
            mcontext=c;
            titlearray=t;
            passarray=p;



            layoutInflater=LayoutInflater.from(mcontext);
        }


        @Override
        public int getCount() {
            return passarray.size();
        }

        @Override
        public Object getItem(int position) {
            return passarray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null)
                convertView=layoutInflater.inflate(R.layout.singlepasswordlayout,null);

            title=convertView.findViewById(R.id.single_title);
            password=convertView.findViewById(R.id.single_password);
            passid.add(password);

            title.setText(titlearray.get(position));
            password.setText(passarray.get(position));


            //COPY BUTTON

            btn_copy=convertView.findViewById(R.id.btn_copy_pass);
            btn_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("password", passarray.get(position));
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(mcontext, "Copied", Toast.LENGTH_SHORT).show();
                        copyvalue=false;


                }
            });

            switch_type=convertView.findViewById(R.id.btn_switch_type);
            switch_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(passid.get(position).getInputType()==InputType.TYPE_CLASS_TEXT)
                        passid.get(position).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    else passid.get(position).setInputType(InputType.TYPE_CLASS_TEXT);
//                  EditText edt_p=  getView(position,v,parent).findViewById(R.id.single_password);
//                  if(edt_p.getInputType()==InputType.TYPE_CLASS_TEXT)
//                      edt_p.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                  else edt_p.setInputType(InputType.TYPE_CLASS_TEXT);

//                    TextView edt_p=my_list_view.getChildAt(position).findViewById(R.id.single_password);
//                    if(edt_p.getInputType()==InputType.TYPE_CLASS_TEXT)
//                      edt_p.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                  else edt_p.setInputType(InputType.TYPE_CLASS_TEXT);
//
//
                }
            });

            delete_pass=convertView.findViewById(R.id.btn_delete_pass);
            delete_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskActivity.db.delete("AllPasswords",  "Title = ?", new String[]{alltitles.get(position)});
                    alltitles.remove(position);
                    allpasswords.remove(position);
//                    Toast.makeText(mcontext, Integer.toString(position), Toast.LENGTH_SHORT).show();
                    passid.remove(position);

                    adapter.notifyDataSetChanged();

                }
            });

            return convertView;

        }
    }

    private String decryptText(String p)
    {

        try {
            String decryptedText= AESCrypt.decrypt(MainActivity.pref.getString("masterKey","null"),p);
            return decryptedText;

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();


    }

    public void onButtonShowPopupWindowClick(View view,int pos) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popuplayout, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupView.findViewById(R.id.auth2_btn).setOnClickListener(v1 -> {

            EditText t= popupView.findViewById(R.id.popup_edt_title);
            alltitles.set(pos,t.getText().toString());
            EditText p=popupView.findViewById(R.id.auth2_edt);
            String newpass=p.getText().toString();

            if( newpass.equals("")||newpass.equals("")){
                Toast.makeText(this, "Invalid Data", Toast.LENGTH_SHORT).show();

                popupWindow.dismiss();
            }
                String encrpass = AddNewPasswordActivity.encryptText(newpass);
                allpasswords.set(pos, newpass);

                ContentValues cv = new ContentValues();
                cv.put("Title", t.getText().toString()); //These Fields should be your String values of actual column names
                cv.put("Password", encrpass);
                TaskActivity.db.update("AllPasswords", cv, "Title = ?", new String[]{alltitles.get(pos)});

            popupWindow.dismiss();


        });


//


    }


    public boolean popupcheckkey(View view,int pos) {


        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupView.findViewById(R.id.btn_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {


                EditText t = popupView.findViewById(R.id.popup_masterkey);
                String str = t.getText().toString();
                String masterKey = MainActivity.pref.getString("masterKey", "null");
                if (str.equals(masterKey))
                    copyvalue=true;
                else copyvalue=false;

            }
        });

        popupWindow.dismiss();
        return copyvalue;



//


    }


}
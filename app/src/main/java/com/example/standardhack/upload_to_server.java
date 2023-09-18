package com.example.standardhack;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class upload_to_server {

    private static final String SERVER_URL = "http://192.168.1.2:8000";

    public static String UploadFile(File file) {

        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");

            // Create a DataOutputStream to write the file data
            DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

            int responseCode = httpURLConnection.getResponseCode();
            try {
            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String response = reader.readLine();
                reader.close();
//                Handle the response here

                Log.d("Success: ", "Successfully uploaded the data"+response);
                return response.toString();
            } else {
//                Handle the error
                Log.d("Server Error: ", "Error Occurred on server");
            }
            } catch (IOException oe) {
                Log.d("Server Error: ", oe.toString());
            }
        } catch (Exception e) {
            Log.d("Error: ", e.toString());
        }
        return null;
    }
}
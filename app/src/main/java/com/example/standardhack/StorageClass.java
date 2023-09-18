package com.example.standardhack;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class StorageClass {
    public String getExternalSdCard() {
        String path = null;
        String sdCardFile = null;

        List<String> sdCardPossiblePath = Arrays.asList("external_sd", "external", "extSdCard", "sdcard");

        for (String sdPath : sdCardPossiblePath) {
            File file = new File("/mnt/", sdPath);

            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();
                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                File testWritable = new File(path, "test_" + timeStamp);

                if (testWritable.mkdirs()) {
                    testWritable.delete();
                } else {
                    path = null;
                }
            }
        }
        if (path != null) {
            sdCardFile = String.valueOf(new File(path));
        } else {
            sdCardFile = String.valueOf(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
        }
        Log.i("LogData: ", sdCardFile);
        return sdCardFile.toString();
    }

    public void WriteIntoFile(String body, String filename) {
        FileOutputStream fos = null;
        try {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            final File dir = new File(directory, "/Android/");
            if(!dir.exists()) {
                if(!dir.mkdirs()) {
                    Log.d("Error", "Can't create the directory");
                }
            }
            final File myFile = new File(dir, filename+".txt");
            if(!myFile.exists()) {
                try {
                    myFile.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            fos = new FileOutputStream(myFile, true);
            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss a");
            String dateToStr = format.format(today);

            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(body.toString());
            writer.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
